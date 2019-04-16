package com.bkjk.influx.proxy.handler;

import com.bkjk.influx.proxy.InfluxProxyApplicationTests;
import com.bkjk.influx.proxy.dto.BackendNode;
import com.bkjk.influx.proxy.dto.KeyMapping;
import com.bkjk.influx.proxy.service.BackendService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaml.snakeyaml.Yaml;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class InfluxProxyHandlerTest extends InfluxProxyApplicationTests {
    @Autowired
    private InfluxProxyHandler influxProxyHandler;
    @Autowired
    private BackendService backendService;


    @Test
    public void testYml(){
        KeyMapping node = new KeyMapping();
        node.setDatabaseRegex("dev");
        BackendNode node2 = new BackendNode();
        node2.setNodeName("dev");
        HashMap<String,Object> config=new HashMap<>();
        config.put("node",Arrays.asList(node,node2).stream().collect(Collectors.toList()));

        Yaml yaml=new Yaml();
        System.out.println(yaml.dumpAsMap(config));
    }

    private Object beanToMap(Object source){
        HashMap ret=new HashMap<>();
        return ret;
    }

}