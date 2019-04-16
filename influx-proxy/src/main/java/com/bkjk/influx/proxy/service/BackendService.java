package com.bkjk.influx.proxy.service;

import com.bkjk.influx.proxy.dto.BackendNode;
import com.bkjk.influx.proxy.dto.KeyMapping;
import com.bkjk.influx.proxy.handler.InfluxClient;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.bkjk.influx.proxy.common.Constants.DEFAULT_QUERY_TIMEOUT;
import static com.bkjk.influx.proxy.common.Constants.DEFAULT_WRITE_TIMEOUT;

@Service
@Slf4j
public class BackendService {
    private ReentrantReadWriteLock dbLoadLock = new ReentrantReadWriteLock();

    // 缓存所有数据库实体，每隔一段时间检测一次，如有变更则刷新缓存。这样可以防止数据库慢或者宕机后proxy找不到后端节点
    private volatile Collection<BackendNode> allBackendNode = new ArrayList<>();
    private volatile Collection<KeyMapping> allKeyMapping = new ArrayList<>();
    private volatile ConcurrentHashMap<String, WebClient> webClientCache = new ConcurrentHashMap<>();

    // 缓存所有后端节点对应得InfluxClient
    private volatile Collection<InfluxClient> allBackendClient = new ArrayList<>();

    // 按db和measurement缓存 节点
    private final ConcurrentHashMap<String, BackendNode> nodeByDbAndMeasurementCache = new ConcurrentHashMap<>();

    @Autowired
    private ConfigurationLoader configurationLoader;

    @PostConstruct
    @Scheduled(fixedRate = 5_000)
    public void autoEntityRefreshCache() {
        try {
            // 缓存数据库中查询到得实体
            // 每隔几秒中检测下后端节点配置是否有变更，如果有，则更新本地缓存
            List<BackendNode> allNodes = configurationLoader.getAllNodes();
            List<KeyMapping> allMappings = configurationLoader.getAllMappings();
            if (null == allNodes || null == allMappings) {
                log.error("Wrong config. Node and mapping CANNOT be null");
                return;
            }
            dbLoadLock.readLock().lock();
            try {
                if (!allBackendNode.equals(allNodes) || !allKeyMapping.equals(allMappings)) {
                    log.info("Config reloading...");
                    dbLoadLock.readLock().unlock();
                    dbLoadLock.writeLock().lock();
                    try {
                        allBackendNode = allNodes;
                        allKeyMapping = allMappings;
                        log.info("node: {}", allBackendNode);
                        log.info("mapping: {}", allKeyMapping);
                    } finally {
                        dbLoadLock.readLock().lock();
                        dbLoadLock.writeLock().unlock();
                    }
                    refreshAllBackend();
                    log.info("Config reloaded.");
                }
                if (allBackendNode.isEmpty()) {
                    log.warn("No node config");
                }
                if (allKeyMapping.isEmpty()) {
                    log.warn("No mapping config");
                }
            } finally {
                dbLoadLock.readLock().unlock();
            }
        } catch (Throwable ignore) {
            log.warn(ignore.getMessage());
        }

    }


    private void refreshAllBackend() {
        allBackendClient = Collections.unmodifiableCollection(allBackendNode)
                .stream()
                .map(backendNodeEntity -> {
                    return InfluxClient.builder().backendNode(backendNodeEntity).webClient(getWebClientFromCacheOrCreate(backendNodeEntity)).build();
                })
                .collect(Collectors.toList());
        synchronized (webClientCache) {
            webClientCache.clear();
        }
        synchronized (nodeByDbAndMeasurementCache) {
            nodeByDbAndMeasurementCache.clear();
        }
    }

    protected Optional<BackendNode> getBackendNodeByDbAndMeasurement(String databaseName, String measurement) {
        Collection<BackendNode> allNodes;
        Collection<KeyMapping> allMappings;
        dbLoadLock.readLock().lock();
        try {
            allNodes = Collections.unmodifiableCollection(allBackendNode);
            allMappings = Collections.unmodifiableCollection(allKeyMapping);
        } finally {
            dbLoadLock.readLock().unlock();
        }
        KeyMapping mapping = allMappings.stream()
                .filter(o -> match(o.getDatabaseRegex(), databaseName) && match(o.getMeasurementRegex(), measurement))
                .findFirst()
                .orElseGet(() -> null);
        if (null == mapping) {
            return Optional.empty();
        }
        return allNodes.stream().filter(node -> Arrays.asList(mapping.getBackendNodeNames().split(",")).contains(node.getNodeName())).findFirst();
    }

    private boolean match(String regex, String test) {
        if (regex == null) {
            return false;
        }
        return Pattern.matches(regex, test);
    }

    public Collection<InfluxClient> getAllBackend() {
        return Collections.unmodifiableCollection(allBackendClient);
    }

    public WebClient getWebClientFromCacheOrCreate(BackendNode node) {
        WebClient client = webClientCache.get(node.getUrl());
        if (client != null) {
            return client;
        }
        synchronized (webClientCache) {
            client = webClientCache.get(node.getUrl());
            if (client != null) {
                return client;
            }
            int queryTimeout=Optional.ofNullable(node.getQueryTimeout()).orElse(DEFAULT_QUERY_TIMEOUT);
            int writeTimeout=Optional.ofNullable(node.getWriteTimeout()).orElse(DEFAULT_WRITE_TIMEOUT);
            int timeout=Math.max(queryTimeout,writeTimeout);

            TcpClient tcpClient = TcpClient.create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
                    .doOnConnected(conn -> conn
                            .addHandlerLast(new ReadTimeoutHandler(timeout))
                            .addHandlerLast(new WriteTimeoutHandler(timeout)));
            WebClient webClient = WebClient.builder()
                    .baseUrl(node.getUrl())
                    .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient).keepAlive(false)))
                    .filter(logRequest())
                    .build();
            webClientCache.put(node.getUrl(), webClient);
            return webClient;
        }
    }

    private static final Logger requestLog = LoggerFactory.getLogger("com.bkjk.influx.proxy.request");

    public static ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            if (requestLog.isDebugEnabled()) {
                requestLog.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
                clientRequest.headers()
                        .forEach((name, values) -> values.forEach(value -> requestLog.debug("{}={}", name, value)));
            }
            return next.exchange(clientRequest);
        };
    }

    public Optional<BackendNode> getNodeByDbAndMeasurement(String db, String measurement) {
        String cacheKey = db + ":" + measurement;
        BackendNode node = nodeByDbAndMeasurementCache.get(cacheKey);
        if (node != null) {
            return Optional.of(node);
        }
        synchronized (nodeByDbAndMeasurementCache) {
            if (nodeByDbAndMeasurementCache.contains(cacheKey)) {
                return Optional.of(nodeByDbAndMeasurementCache.get(cacheKey));
            }
            Optional<BackendNode> nodeOptional = getBackendNodeByDbAndMeasurement(db, measurement);
            if (nodeOptional.isPresent()) {
                log.info("Server [{}] for {}.{}", nodeOptional.get().getUrl(), db, measurement);
                nodeByDbAndMeasurementCache.put(cacheKey, nodeOptional.get());
            } else {
                log.error("No server for {}.{}", db, measurement);
            }
            return nodeOptional;
        }
    }

}
