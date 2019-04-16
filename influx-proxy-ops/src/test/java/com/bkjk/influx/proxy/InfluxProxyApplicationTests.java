package com.bkjk.influx.proxy;

import com.bkjk.influx.proxy.entity.BackendNodePo;
import com.bkjk.influx.proxy.entity.KeyMappingPo;
import com.bkjk.influx.proxy.mapper.BackendNodeMapper;
import com.bkjk.influx.proxy.mapper.KeyMappingMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InfluxProxyOpsApplication.class)
public class InfluxProxyApplicationTests {
    @Autowired
    private KeyMappingMapper keyMappingMapper;
    @Autowired
    private BackendNodeMapper backendNodeMapper;

    private List<BackendNodePo> backendNodeEntityArrayList=new ArrayList<>();
    private List<KeyMappingPo> keyMappingEntityArrayList=new ArrayList<>();

    @Before
    public void before() {
        keyMappingEntityArrayList.add(addMapping(".*", "mem", "local"));
        keyMappingEntityArrayList.add(addMapping(".*", "cpu", "local"));
        keyMappingEntityArrayList.add(addMapping(".*", "uptime", "local"));
        keyMappingEntityArrayList.add(addMapping(".*", "cpu_load_short", "dev"));
        keyMappingEntityArrayList.add(addMapping(".*", ".*", "local"));
        backendNodeEntityArrayList.add(addNewNode("local"));
        backendNodeEntityArrayList.add(addNewNode("dev", "http://172.29.64.250:18086"));
    }

    @After
    public void after(){
        keyMappingEntityArrayList.forEach(e-> keyMappingMapper.deleteById(e.getId()));
        backendNodeEntityArrayList.forEach(e -> backendNodeMapper.deleteById(e.getId()));
    }

    private BackendNodePo addNewNode(String name) {
        BackendNodePo localNode = new BackendNodePo();
        localNode.setNodeName(name);
        localNode.setUrl("http://localhost:8086");
        backendNodeMapper.save(localNode);
        return localNode;
    }

    private BackendNodePo addNewNode(String name, String url) {
        BackendNodePo localNode = new BackendNodePo();
        localNode.setNodeName(name);
        localNode.setUrl(url);
        backendNodeMapper.save(localNode);
        return localNode;
    }

    private KeyMappingPo addMapping(String dbRegex, String measurementRegex, String nodeName) {
        KeyMappingPo defaultKeyMapping = new KeyMappingPo();
        defaultKeyMapping.setDatabaseRegex(dbRegex);
        defaultKeyMapping.setMeasurementRegex(measurementRegex);
        defaultKeyMapping.setBackendNodeNames(nodeName);
        keyMappingMapper.save(defaultKeyMapping);
        return defaultKeyMapping;
    }

}

