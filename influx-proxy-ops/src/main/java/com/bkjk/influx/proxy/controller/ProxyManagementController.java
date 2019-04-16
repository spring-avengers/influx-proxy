package com.bkjk.influx.proxy.controller;

import com.bkjk.influx.proxy.common.PageVO;
import com.bkjk.influx.proxy.common.ResultVO;
import com.bkjk.influx.proxy.entity.*;
import com.bkjk.influx.proxy.service.BackendNodeService;
import com.bkjk.influx.proxy.service.InfluxDBService;
import com.bkjk.influx.proxy.service.KeyMappingService;
import com.bkjk.influx.proxy.service.ProxyMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/2/27 11:07
 **/
@RestController
@RequestMapping("/management")
public class ProxyManagementController {

    @Autowired
    private ProxyMonitorService proxyMonitorService;

    @Autowired
    private BackendNodeService backendNodeService;

    @Autowired
    private KeyMappingService keyMappingService;

    @Autowired
    private InfluxDBService influxDBService;

    @GetMapping("/nodes")
    public PageVO<BackendNodePo> getAllBackend(@RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                               @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        return backendNodeService.geAllNodes(offset, limit);
    }

    @PostMapping("/nodes")
    public ResultVO addNewNode(@Validated @RequestBody NodePo nodePo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResultVO(false, bindingResult.getFieldError().getDefaultMessage());
        }
        backendNodeService.addNewNode(nodePo);
        return new ResultVO(true);
    }

    @PutMapping("/nodes/update")
    public ResultVO mutateNode(@Validated @RequestBody NodePo nodePo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResultVO(false, bindingResult.getFieldError().getDefaultMessage());
        }
        backendNodeService.updateNode(nodePo);
        return new ResultVO(true);
    }

    @DeleteMapping("/nodes/{id}")
    public ResultVO deleteNode(@PathVariable("id") Long id) {
        backendNodeService.deleteNodeById(id);
        return new ResultVO(true);
    }


    @GetMapping("/keyMappings")
    public PageVO<KeyMappingPo> getAllKeyMapping(@RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                 @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        return keyMappingService.getAllKeyMapping(offset, limit);
    }

    @PostMapping("/keyMappings")
    public ResultVO addNewKeyMapping(@Validated @RequestBody KeyMappingPo keyMappingEntity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResultVO(false, bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            keyMappingService.addNewKeyMapping(keyMappingEntity);
        } catch (Exception e) {
            return new ResultVO(false, e.getMessage());
        }
        return new ResultVO(true);
    }

    @PutMapping("/keyMappings/update")
    public ResultVO mutateKeyMapping(@Validated @RequestBody KeyMappingPo keyMappingEntity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResultVO(false, bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            keyMappingService.updateKeyMapping(keyMappingEntity);
        } catch (Exception e) {
            return new ResultVO(false, e.getMessage());
        }
        return new ResultVO(true);
    }

    @DeleteMapping("/keyMappings/{id}")
    public ResultVO deleteKeyMapping(@PathVariable("id") Long id) {
        keyMappingService.deleteKeyMappingById(id);
        return new ResultVO(true);
    }

    @GetMapping("/proxyInstances")
    public Collection<ProxyMonitorService.ProxyInstance> getAllProxyInstance() {
        return proxyMonitorService.getProxyInstances();
    }


    @GetMapping("/retention")
    public PageVO<ManagementInfoWarpPo> queryInfluxDBInfo(@RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                          @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        return influxDBService.queryInfluxDBInfo(offset, limit);
    }

    @PostMapping("/retention")
    public ResultVO addNewRetention(@Validated @RequestBody ManagementInfoWarpPo managementInfoWarpPo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResultVO(false, bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            influxDBService.addNewRetention(managementInfoWarpPo);
        } catch (Exception e) {
            return new ResultVO(false, e.getMessage());
        }
        return new ResultVO(true);
    }

    @PutMapping("/retention/update")
    public ResultVO updateRetention(@Validated @RequestBody ManagementInfoPo managementInfoPo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResultVO(false, bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            influxDBService.updateRetention(managementInfoPo);
        } catch (Exception e) {
            return new ResultVO(false, e.getMessage());
        }
        return new ResultVO(true);
    }

    @DeleteMapping("/retention/{id}")
    public ResultVO deleteRetention(@PathVariable("id") Long id) {
        influxDBService.deleteRetention(id);
        return new ResultVO(true);
    }

    @PostMapping("/retention/syn/{id}")
    public ResultVO synRetention(@PathVariable("id") Long id) {
        influxDBService.synLocalDataToInfluxDB(id);
        return new ResultVO(true);
    }
}
