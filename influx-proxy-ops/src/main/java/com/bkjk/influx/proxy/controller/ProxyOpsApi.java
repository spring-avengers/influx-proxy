package com.bkjk.influx.proxy.controller;

import com.bkjk.influx.proxy.entity.BackendNodePo;
import com.bkjk.influx.proxy.entity.KeyMappingPo;
import com.bkjk.influx.proxy.mapper.BackendNodeMapper;
import com.bkjk.influx.proxy.mapper.KeyMappingMapper;
import com.bkjk.influx.proxy.service.ProxyMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @Program: influx-proxy
 * @Description: 供influx-proxy调用的接口
 * @Author: shaoze.wang
 * @Create: 2019/3/1 13:33
 **/
@RestController
@RequestMapping("/api/v1")
public class ProxyOpsApi {
    @Autowired
    private BackendNodeMapper backendNodeMapper;

    @Autowired
    private KeyMappingMapper keyMappingMapper;

    @Autowired
    private ProxyMonitorService proxyMonitorService;

    @GetMapping("/node")
    public List<BackendNodePo> getAllBackend() {
        return backendNodeMapper.findAll().stream().filter(BackendNodePo::isOnline).collect(Collectors.toList());
    }

    @GetMapping("/nodeUrl")
    public List<BackendNodePo> getDistinctBackend() {
        return backendNodeMapper.findAll().parallelStream().filter(BackendNodePo::isOnline).collect(
                Collectors.collectingAndThen(Collectors.toCollection(
                        () -> new TreeSet<>(Comparator.comparing(o -> o.getUrl()))), ArrayList::new));
    }

    @GetMapping("/mapping")
    public List<KeyMappingPo> getAllKeyMapping() {
        return keyMappingMapper.findAll();
    }

    @GetMapping("/ping")
    public void ping(@RequestParam("ip") String ip,
                     @RequestParam("port") int port,
                     @RequestParam("managementPort") int managementPort,
                     @RequestParam("hostname") String hostname) {
        proxyMonitorService.onPing(ProxyMonitorService.ProxyInstance.builder()
                .ip(ip)
                .port(port)
                .managementPort(managementPort)
                .hostname(hostname)
                .build());
    }

}
