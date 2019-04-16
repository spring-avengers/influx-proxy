package com.bkjk.influx.proxy;

import com.bkjk.influx.proxy.entity.BackendNodePo;
import com.bkjk.influx.proxy.entity.KeyMappingPo;
import com.bkjk.influx.proxy.mapper.BackendNodeMapper;
import com.bkjk.influx.proxy.mapper.KeyMappingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/1/25 18:28
 **/
@Component
public class Init {

    @Value("${spring.profiles.active:}")
    private String profile;
    @Autowired
    private KeyMappingMapper keyMappingMapper;
    @Autowired
    private BackendNodeMapper backendNodeMapper;

    @PostConstruct
    public void init(){
        if(profile.toLowerCase().contains("dev")){
            // 默认全部转发到本地
            if(keyMappingMapper.count()==0){
                addNewMapping(".*","cpu_load_short","dev");
                addNewMapping(".*",".*","local");
            }

            if(backendNodeMapper.count()==0){
                addNewNode("local","http://localhost:8086");
                addNewNode("dev","http://172.29.64.250:18086");
            }
        }
    }

    private void addNewNode(String name,String url){
        BackendNodePo localNode=new BackendNodePo();
        localNode.setNodeName(name);
        localNode.setUrl(url);
        backendNodeMapper.save(localNode);
    }

    private void addNewMapping(String dbRegex,String measurementRegex,String nodeName){
        KeyMappingPo defaultKeyMapping=new KeyMappingPo();
        defaultKeyMapping.setDatabaseRegex(dbRegex);
        defaultKeyMapping.setMeasurementRegex(measurementRegex);
        defaultKeyMapping.setBackendNodeNames(nodeName);
        keyMappingMapper.save(defaultKeyMapping);
    }
}
