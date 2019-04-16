package com.bkjk.influx.proxy.handler;


import com.bkjk.influx.proxy.dto.BackendNode;
import com.bkjk.influx.proxy.dto.QueryResult;
import com.bkjk.influx.proxy.exception.InfluxDBException;
import com.bkjk.influx.proxy.service.BackendService;
import com.bkjk.influx.proxy.service.MonitorService;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static com.bkjk.influx.proxy.common.Constants.DEFAULT_QUERY_TIMEOUT;
import static com.bkjk.influx.proxy.common.Constants.DEFAULT_WRITE_TIMEOUT;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/1/25 18:40
 **/
@Component
@Slf4j
public class InfluxProxyHandler {

    @Autowired
    private BackendService backendService;

    @Autowired
    private MonitorService monitorService;

    // 专用线程异步刷新缓存
    private final Scheduler refreshScheduler = Schedulers.newSingle("refresh-cache", true);

    public Mono<ServerResponse> debug(ServerRequest request) {
        return ServerResponse.ok().syncBody("暂不支持 debug");
    }

    public Mono<ServerResponse> ping(ServerRequest request) {
        //  ping所有后端节点，任意一个节点返回非200状态，则以这个节点的状态作为Response，如果没有节点返回非200，则返回 200（不返回204）
        return Flux.fromIterable(backendService.getAllBackend())
                .flatMap(influxClient -> influxClient.getWebClient()
                        .get()
                        .uri(request.path())
                        .headers(h -> {
                            request.headers().asHttpHeaders().forEach((a, b) -> {
                                if (!a.equalsIgnoreCase("host")) {
                                    h.addAll(a, b);
                                }
                            });
                        })
                        .exchange())
                .filter(clientResponse -> !clientResponse.statusCode().is2xxSuccessful())
                .map(ClientResponse::statusCode)
                .switchIfEmpty(Mono.just(HttpStatus.NO_CONTENT))
                .single()
                .flatMap(status->ServerResponse.status(status).build());
    }

    private AtomicLong counter=new AtomicLong();
    public Mono<ServerResponse> write(ServerRequest request) {
        HttpHeaders headers = request.headers().asHttpHeaders();
        boolean debugCount=headers.containsKey("x-mid")&&headers.containsKey("x-debug");
        if(debugCount){
            counter.incrementAndGet();
            log.info("Before body split. x-mid {}, counter {}, contentLength {}",headers.get("x-mid"),counter,request.headers().contentLength());
        }
        String db = request.queryParam("db").orElseThrow(()->InfluxDBException.create("database name required"));
        // 从body中获取文本，按行匹配，分发到不同的list，然后按表名分发到不同的后端，结果聚合后返回给前端
        List<Integer> errors = new ArrayList<>();
        return request.bodyToMono(String.class).flatMap(body -> {
            String[] lines = body.split("\n");
            if(debugCount){
                log.info("Got x-mid {}, counter {}, bodyLine {}",headers.get("x-mid"),counter,lines.length);
            }
            Mono<Map<String, Collection<String>>> lineByMeasurements = Flux
                    .fromArray(lines)// 按行分割body
                    .filter(s -> !StringUtils.isEmpty(s))// 去掉空行
                    .map(String::trim)// 去掉前后空白字符
                    .collectMultimap(line -> line.substring(0, line.indexOf(","))) // 逗号分隔，第一位是measurement
                    ;

            Mono<Map<BackendNode, Collection<Collection<String>>>> nodeByMeasurements = lineByMeasurements
                    .flatMap(o -> Flux.fromIterable(o.entrySet())
                            .map(entry -> Tuples.of(entry, backendService.getNodeByDbAndMeasurement(db, entry.getKey())))// 从缓存中查询measurement对应的后端节点
                            .filter(tuple -> tuple.getT2().isPresent()) // 跳过没有找到influxdb节点的数据
                            .collectMultimap(tuple -> tuple.getT2().get(), tuple -> tuple.getT1().getValue()))//以BackendNode作为key对每行数据进行分组
                    ;
            return nodeByMeasurements
                    .flatMap(o -> Flux.fromIterable(o.entrySet())
                            .flatMap(backendNodeListEntry -> { // 每个Node分别发送write请求，并聚合返回结果
                                BackendNode node = backendNodeListEntry.getKey();
                                if(node.getUrl().equalsIgnoreCase("http://mock")){
                                    return Mono.just("");
                                }
                                Collection<String> lineBody = backendNodeListEntry.getValue().stream().flatMap(Collection::stream).collect(Collectors.toList());
                                WebClient webClient = backendService.getWebClientFromCacheOrCreate(node);
                                Duration timeout = Duration.ofMillis(Optional.ofNullable(node.getWriteTimeout()).orElse(DEFAULT_WRITE_TIMEOUT));
                                long start = System.nanoTime();
                                return webClient
                                        .method(request.method())
                                        .uri(uriBuilder -> uriBuilder.path(request.path())
                                                .queryParams(request.queryParams())
                                                .build())
                                        .headers(h -> {
                                            request.headers().asHttpHeaders().forEach((a, b) -> {
                                                if (!a.equalsIgnoreCase("host") && !a.equalsIgnoreCase("Content-Encoding")) {
                                                    h.addAll(a, b);
                                                }
                                            });
                                        })
                                        .syncBody(String.join("\n", lineBody))
                                        .exchange()
                                        .timeout(timeout)
                                        .doOnError(throwable -> {
                                            monitorService.onWrite(node,lineBody,throwable,start);
                                        })
                                        .doOnNext(clientResponse -> {
                                            // TODO 统计后端写入数量
                                            monitorService.onWrite(node,lineBody,null, start);
                                        })
                                        .flatMap(clientResponse -> {
                                            if (clientResponse.statusCode().value() != HttpStatus.NO_CONTENT.value()) {
                                                errors.add(clientResponse.statusCode().value());
                                            }
                                            return clientResponse.bodyToMono(Object.class);
                                        });
                            })
                            .collectList())
                    .doOnSuccess(o->{
                        if(debugCount){
                            log.info("success "+headers.get("x-mid").toString());
                        }
                    })
                    .onErrorResume((throwable -> {
                        if(debugCount){
                            log.info("fail "+headers.get("x-mid").toString(),throwable);
                        }
                        log.error(throwable.getMessage(),throwable);
                        return Mono.just(new ArrayList<>());
                    }))
                    .flatMap(o ->
                            ServerResponse.status(errors.size() == 0
                                    ? HttpStatus.NO_CONTENT.value()
                                    : errors.get(0)).syncBody(o));
        });
    }

    public Mono<ServerResponse> query(ServerRequest request) {
        // 从request中提取参数列表，包括queryString formData和multipartData
        return extractValuesToBind(request)
                .flatMap(m -> {
                    MultiValueMap<String, String> params = fromRequest(m);
                    // 查询所有后端节点
                    List<Mono<Result>> queryResultList = backendService.getAllBackend().stream()
                            .map(influxClient -> {
                                Duration timeout = Duration.ofMillis(Optional.ofNullable(influxClient.getBackendNode().getQueryTimeout()).orElse(DEFAULT_QUERY_TIMEOUT));
                                WebClient webClient = influxClient.getWebClient();
                                return webClient
                                        .method(HttpMethod.POST)
                                        .uri(uriBuilder -> uriBuilder.path(request.path())
                                                .queryParams(params)
                                                .build())
                                        .headers(h -> {
                                            request.headers().asHttpHeaders().forEach((k, v) -> {
                                                if (k.equalsIgnoreCase("host")
                                                        || k.equalsIgnoreCase("Accept-Encoding")) {

                                                } else {
                                                    h.addAll(k, v);
                                                }
                                            });
                                        })
                                        .body(BodyInserters.fromFormData(params))
                                        .exchange()
                                        .timeout(timeout)
                                        .flatMap(clientResponse -> clientResponse.bodyToMono(QueryResult.class))
                                        .flatMap(queryResult -> Mono.just(Result.builder().influxClient(influxClient).queryResult(queryResult).build()));
                            })
                            .collect(Collectors.toList());
                    // 聚合返回结果
                    return Mono.zip(queryResultList, arr -> {
                        return Arrays.asList(arr).stream()
                                .map(o -> (Result) o)
                                .filter(o -> o.getQueryResult() != null)
                                .reduce(new QueryResult(new ArrayList<>()), (realResult, result) -> {
                                    QueryResult queryResult = result.getQueryResult();
                                    if (queryResult.getError() != null) {
                                        realResult.setError(queryResult.getError());
                                    }
                                    if (queryResult.getResults() != null) {
                                        // 聚合各个节点的返回值
                                        List<QueryResult.Result> queryResults = queryResult.getResults();
                                        List<QueryResult.Result> realResults = realResult.getResults();
                                        List<QueryResult.Result> finalResults = new ArrayList<>();
                                        for (int i = 0; i < queryResults.size() || i < realResults.size(); i++) {
                                            ArrayList<QueryResult.Series> finalSeries = new ArrayList<>();
                                            QueryResult.Result finalResult = new QueryResult.Result();
                                            combineSeries(queryResults,i,finalSeries,result);
                                            combineSeries(realResults,i,finalSeries,result);
                                            finalResult.setSeries(finalSeries);
                                            finalResult.setStatementId(i);
                                            finalResults.add(finalResult);
                                        }
                                        realResult.setResults(finalResults);
                                    }
                                    return realResult;
                                }, (a, b) -> a);
                    });
                })
                .flatMap(r -> ServerResponse.ok().syncBody(r));
    }

    private void combineSeries(List<QueryResult.Result> queryResults,int i,ArrayList<QueryResult.Series> finalSeries,Result result){
        if (i < queryResults.size()) {
            if (queryResults.get(i).getSeries() != null) {
                finalSeries.addAll(queryResults.get(i).getSeries());
            }
            if (queryResults.get(i).getError() != null) {
                log.error("Node [{}] [{}] error :{}", result.getInfluxClient().getBackendNode().getNodeName(), result.getInfluxClient().getBackendNode().getUrl(), queryResults.get(i).getError());
            }
        }

    }


    public Mono<ServerResponse> refreshAllBackend(ServerRequest request) {
        Mono.fromRunnable(() -> {
            log.info("RefreshCache manually");
            backendService.autoEntityRefreshCache();
        }).subscribeOn(refreshScheduler).subscribe();
        return ServerResponse.accepted().syncBody("Accepted");
    }

    public static Mono<Map<String, Object>> extractValuesToBind(ServerRequest request) {
        MultiValueMap<String, String> queryParams = request.queryParams();
        Mono<MultiValueMap<String, String>> formData = request.formData();
        Mono<MultiValueMap<String, Part>> multipartData = request.multipartData();

        return Mono.zip(Mono.just(queryParams), formData, multipartData)
                .map(tuple -> {
                    Map<String, Object> result = new TreeMap<>();
                    tuple.getT1().forEach((key, values) -> addBindValue(result, key, values));
                    tuple.getT2().forEach((key, values) -> addBindValue(result, key, values));
                    tuple.getT3().forEach((key, values) -> addBindValue(result, key, values));
                    return result;
                });
    }

    private static void addBindValue(Map<String, Object> params, String key, List<?> values) {
        if (!CollectionUtils.isEmpty(values)) {
            values = values.stream()
                    .map(value -> value instanceof FormFieldPart ? ((FormFieldPart) value).value() : value)
                    .collect(Collectors.toList());
            params.put(key, values.size() == 1 ? values.get(0) : values);
        }
    }

    public MultiValueMap<String, String> fromRequest(Map<String, Object> m) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        m.forEach((k, v) -> {
            params.add(k, v.toString());
        });
        return params;
    }

    @Data
    @Builder
    private static class Result {
        QueryResult queryResult;
        InfluxClient influxClient;

    }
}
