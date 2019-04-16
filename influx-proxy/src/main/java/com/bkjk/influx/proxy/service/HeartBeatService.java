package com.bkjk.influx.proxy.service;

import com.bkjk.influx.proxy.common.ProxyConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * @Program: influx-proxy
 * @Description: 保持和OPS模块的心跳
 * @Author: shaoze.wang
 * @Create: 2019/3/1 14:28
 **/
@Component
@Slf4j
public class HeartBeatService {
    @Autowired
    private InetUtils inetUtils;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ops.path:}")
    private String opsPath;

    @Value("${server.port:8080}")
    private int port;

    @Value("${management.server.port:8080}")
    private int managementPort;

    @Autowired
    private ProxyConfiguration proxyConfiguration;

    @Scheduled(fixedRate = 5_000)
    public void heartBeat() {
        if(proxyConfiguration.isEnable()||StringUtils.isEmpty(opsPath)){
            return;
        }
        try{
            InetUtils.HostInfo hostInfo = inetUtils.findFirstNonLoopbackHostInfo();
            restTemplate.getForObject(opsPath + "/ping?ip={1}&port={2}&managementPort={3}&hostname={4}",
                    String.class,
                    hostInfo.getIpAddress(),
                    port,
                    managementPort,
                    hostInfo.getHostname());
        }catch (Throwable ignore){
            log.warn("Failed to ping {}",opsPath);
        }
    }
}
