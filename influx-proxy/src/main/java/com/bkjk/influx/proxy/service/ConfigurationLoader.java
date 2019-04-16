package com.bkjk.influx.proxy.service;

import com.bkjk.influx.proxy.common.ProxyConfiguration;
import com.bkjk.influx.proxy.dto.BackendNode;
import com.bkjk.influx.proxy.dto.KeyMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @Program: influx-proxy
 * @Description: 从本地或者ops接口获取配置信息
 * @Author: shaoze.wang
 * @Create: 2019/3/1 13:04
 **/
@Service
public class ConfigurationLoader {

    private ArrayList EMPTY=new ArrayList();

    @Value("${ops.path:}")
    private String opsPath;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProxyConfiguration proxyConfiguration;

    public List<BackendNode> getAllNodes(){
        if(proxyConfiguration.isEnable()){
            return proxyConfiguration.getNode();
        }else if(!StringUtils.isEmpty(opsPath)){
            ResponseEntity<List<BackendNode>> response = restTemplate.exchange(opsPath+"/node", HttpMethod.GET, null, new ParameterizedTypeReference<List<BackendNode>>() {
            });
            return response.getBody();
        }
        return EMPTY;
    }

    public List<KeyMapping> getAllMappings(){
        if(proxyConfiguration.isEnable()){
            return proxyConfiguration.getMapping();
        }else if(!StringUtils.isEmpty(opsPath)){
            ResponseEntity<List<KeyMapping>> response = restTemplate.exchange(opsPath+"/mapping", HttpMethod.GET, null, new ParameterizedTypeReference<List<KeyMapping>>() {
            });
            return response.getBody();
        }
        return EMPTY;
    }
}
