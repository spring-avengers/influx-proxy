package com.bkjk.influx.proxy.filter;

import com.google.common.io.ByteStreams;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/3/5 10:16
 **/
@Slf4j
@Component
public class GzipWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {

        HttpHeaders headers = serverWebExchange.getRequest().getHeaders();
        boolean debugCount = headers.containsKey("x-mid") && headers.containsKey("x-debug");
        if (debugCount) {
            log.info("On received x-mid {}, contentLength {}, path {}", headers.get("x-mid"), serverWebExchange.getRequest().getHeaders().getContentLength(), serverWebExchange.getRequest().getQueryParams());
        }
        if (Optional.ofNullable(serverWebExchange.getRequest().getHeaders().get("Content-Encoding"))
                .orElse(Collections.emptyList()).stream()
                .anyMatch(s -> s.toLowerCase().contains("gzip"))) {
            // 解压body，并重新设置content-length

            if (debugCount) {
                log.info("header {}", serverWebExchange.getRequest().getHeaders());
            }
            return serverWebExchange.getRequest().getBody()
                    .collectList()
                    .map(dataBuffers -> {
                        Assert.notEmpty(dataBuffers, "DataBuffers");
                        if (debugCount) {
                            log.info("On body input x-mid {} , dataBuffers {}", headers.get("x-mid"), dataBuffers.size());
                        }
                        return dataBuffers.get(0).factory().join(dataBuffers);
                    })
                    .flatMap(dataBuffer -> {
                        try (GZIPInputStream gzipInputStream = new GZIPInputStream(dataBuffer.asInputStream(true))) {
                            byte[] byteBody = ByteStreams.toByteArray(gzipInputStream);
                            if (debugCount) {
                                log.info("On body input x-mid {}, byteBody {}", headers.get("x-mid"), byteBody.length);
                            }
                            return webFilterChain.filter(serverWebExchange.mutate()
                                    .request(new ServerHttpRequestDecorator(serverWebExchange.getRequest()) {
                                        @Override
                                        public Flux<DataBuffer> getBody() {
                                            return Flux.just(dataBuffer.factory().wrap(byteBody));
                                        }

                                        @Override
                                        public HttpHeaders getHeaders() {
                                            long contentLength = super.getHeaders().getContentLength();
                                            HttpHeaders httpHeaders = new HttpHeaders();
                                            httpHeaders.putAll(super.getHeaders());
                                            // 由于修改了传递参数，需要重新设置CONTENT_LENGTH，长度是字节长度，不是字符串长度
                                            httpHeaders.remove(HttpHeaders.CONTENT_LENGTH);
                                            if (contentLength > 0) {
                                                httpHeaders.setContentLength(byteBody.length);
                                            } else {
                                                // TODO: this causes a 'HTTP/1.1 411 Length Required' on httpbin.org
                                                httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                                            }
                                            return httpHeaders;
                                        }
                                    })
                                    .build());
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                            return Mono.error(e);
                        }
                    })
                    .then();
        }
        return webFilterChain.filter(serverWebExchange);
    }
}
