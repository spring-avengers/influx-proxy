package com.bkjk.influx.proxy.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Builder;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Program: influx-proxy
 * @Description: 监控 influx-proxy 实例的状态 TPS 流量
 * @Author: shaoze.wang
 * @Create: 2019/3/1 14:23
 **/
@Service
public class ProxyMonitorService {


    private static final Cache<String, ProxyInstance> proxyInstanceCache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS).build();


    @Data
    @Builder
    public static class ProxyInstance{
        private String ip;
        private int port;
        private int managementPort;
        private String hostname;
        // lastTimestamp 不加入toString equals hashCode
        private Date lastTimestamp;

        @Override
        public String toString() {
            return "ProxyInstance{" +
                    "ip='" + ip + '\'' +
                    ", port=" + port +
                    ", managementPort=" + managementPort +
                    ", hostname='" + hostname + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProxyInstance that = (ProxyInstance) o;
            return port == that.port &&
                    managementPort == that.managementPort &&
                    Objects.equals(ip, that.ip) &&
                    Objects.equals(hostname, that.hostname);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ip, port, managementPort, hostname);
        }
    }

    public void onPing(ProxyInstance proxyInstance){
        @Nullable ProxyInstance existProxyInstance = proxyInstanceCache.getIfPresent(proxyInstance.toString());
        if(existProxyInstance==null){
           proxyInstanceCache.put(proxyInstance.toString(),proxyInstance);
        }
        existProxyInstance=proxyInstanceCache.getIfPresent(proxyInstance.toString());
        if(existProxyInstance!=null){
            existProxyInstance.setLastTimestamp(new Date());
        }
    }

    public Collection<ProxyInstance> getProxyInstances(){
        return Collections.unmodifiableCollection(proxyInstanceCache.asMap().values());
    }

}
