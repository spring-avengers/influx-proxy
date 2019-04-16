package com.bkjk.influx.proxy.service;

import com.bkjk.influx.proxy.dto.BackendNode;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/4/8 16:54
 **/
@Service
public class MonitorService {

    /**
     *  @param node 后端节点
     * @param lineBody 按行切割的指标数据
     * @param throwable 异常
     * @param start 请求的开始时间，单位纳秒
     */
    public void onWrite(BackendNode node, Collection<String> lineBody, Throwable throwable, long start) {
        long cost=System.nanoTime()-start;
        getTimer("write_timer",node.getNodeName(),throwable).record(cost, TimeUnit.NANOSECONDS);
        getSummary("write_line",node.getNodeName(),throwable).record(lineBody.size());
    }

    private Timer getTimer(String name,String node,Throwable exception){
        String ex=exception==null?"none":exception.getClass().getCanonicalName();
        return Metrics.timer(name, Arrays.asList(
                getOrCreateTag("node",node),
                getOrCreateTag("exception",ex)
        ));
    }

    private DistributionSummary getSummary(String name, String node, Throwable exception){
        String ex=exception==null?"none":exception.getClass().getCanonicalName();
        return Metrics.summary(name, Arrays.asList(
                getOrCreateTag("node",node),
                getOrCreateTag("exception",ex)
        ));
    }

    private static Tag getOrCreateTag(String key,String value){
        if(TAG_CACHE.size() > MAX_TAG_CACHE_SIZE){
            synchronized (TAG_CACHE){
                TAG_CACHE.clear();
            }
        }
        return TAG_CACHE.computeIfAbsent(String.format("%s:%s",key,value),(ignore)->Tag.of(key,value));
    }

    private static final ConcurrentHashMap<String,Tag> TAG_CACHE=new ConcurrentHashMap<>();

    public static final int MAX_TAG_CACHE_SIZE=10000;

}
