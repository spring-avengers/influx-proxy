/**
 * =============================================================
 * Copyright 2018 Lianjia Group All Rights Reserved
 * CompanyName: 上海链家有限公司
 * SystemName: 贝壳
 * ClassName: InfluxDBService
 * version: 1.0.0
 * date: 2019/3/18
 * author: Tyson
 * =============================================================
 */
package com.bkjk.influx.proxy.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bkjk.influx.proxy.common.Constants;
import com.bkjk.influx.proxy.common.PageVO;
import com.bkjk.influx.proxy.common.SynStatusEnum;
import com.bkjk.influx.proxy.entity.ManagementInfoPo;
import com.bkjk.influx.proxy.entity.ManagementInfoWarpPo;
import com.bkjk.influx.proxy.entity.QueryResult;
import com.bkjk.influx.proxy.exception.InfluxDBProxyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.util.StringUtils;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * @author Tyson
 * @version V1.0
 * @Description: TODO
 * @date 2019/3/18下午8:06
 */
@Service
public class InfluxDBService {

    private static final String FILED_NAME = "databases";
    private static final String DEFAULT_DATABASE_NAME = "_internal";

    @Autowired
    private BackendNodeService backendService;

    @Autowired
    private ManagementInfoService managementInfoService;

    @Autowired
    private RestTemplate restTemplate;

    public List<String> queryInfluxDBData() {
        // 查询所有后端节点
        return backendService.getAllNodesWithoutCondtion().stream()
                .map(backendNode -> {
                    String url = backendNode.getUrl() + "/query?pretty=true&db=micrometerDb&q=select * from cpu";
                    return restTemplate.getForObject(url, String.class);
                })
                .collect(Collectors.toList());
    }


    public PageVO<ManagementInfoWarpPo> queryInfluxDBInfo(Integer offset, Integer limit) {

        //查出本地存的influxdb信息
        PageVO<ManagementInfoPo> allManagementInfo = managementInfoService.getAllManagementInfo(offset, limit);
        List<ManagementInfoPo> localManagementInfo = allManagementInfo.getRows();
        List<ManagementInfoPo> managementInfoPoList = new ArrayList<>(allManagementInfo.getSize());
        List<ManagementInfoWarpPo> finalResult = new ArrayList<>(allManagementInfo.getSize());


        //url可能会重复 去重
        List<ManagementInfoPo> distinctData = localManagementInfo.parallelStream().filter(distinctByKey(ManagementInfoPo::getNodeUrl))
                .collect(Collectors.toList());

        distinctData.forEach(node -> {
            QueryResult databaseResult = getQueryResult(String.format(Constants.DATABASE_URL, node.getNodeUrl()));
            List<String> databaseNameList = new ArrayList<>();
            databaseResult.getResults().stream().forEach(result -> {
                result.getSeries().stream().forEach(series -> {
                    if (StringUtils.equals(FILED_NAME, series.getName())) {
                        List<String> collect = series.getValues().stream().map(item -> {
                            return (String) item.get(0);
                        }).collect(Collectors.toList());
                        databaseNameList.addAll(collect);
                    }
                });
            });
            //去除默认创建的databaseName _internal
            List<String> filterNameList = databaseNameList.stream().filter(item -> !item.contains(DEFAULT_DATABASE_NAME)).collect(Collectors.toList());

            filterNameList.stream().forEach(databaseName -> {
                QueryResult retentionResult = getQueryResult(String.format(Constants.RETENTION_URL, node.getNodeUrl(), databaseName));
                retentionResult.getResults().stream().forEach(result -> {
                    result.getSeries().stream().forEach(series -> {
                        List<ManagementInfoPo> collect = series.getValues().stream().map(item -> {
                            String s = (String) item.get(1);
                            int hour = Integer.valueOf(s.indexOf('h') == -1 ? "0" : s.substring(0, s.indexOf('h')));
                            return ManagementInfoPo.builder()
                                    .nodeUrl(node.getNodeUrl())
                                    .databaseName(databaseName)
                                    .retentionName((String) item.get(0))
                                    .retentionDuration(hour < 24 ? 0 : hour / 24)
                                    .isDefault((boolean) item.get(4))
                                    .build();
                        }).collect(Collectors.toList());
                        managementInfoPoList.addAll(collect);
                    });
                });
            });
        });

        Map<String, ManagementInfoPo> managementInfoPoMap = managementInfoPoList.stream().collect(Collectors.toMap(k -> k.getNodeUrl() + k.getDatabaseName() + k.getRetentionName(), Function.identity()));
        for (ManagementInfoPo managementInfoPo : localManagementInfo) {
            String key = managementInfoPo.getNodeUrl() + managementInfoPo.getDatabaseName() + managementInfoPo.getRetentionName();
            ManagementInfoWarpPo build = ManagementInfoWarpPo.builder().nodeUrl(managementInfoPo.getNodeUrl())
                    .databaseName(managementInfoPo.getDatabaseName())
                    .retentionName(managementInfoPo.getRetentionName())
                    .retentionDuration(managementInfoPo.getRetentionDuration())
                    .retentionDefault(managementInfoPo.isDefault())
                    .build();
            build.setId(managementInfoPo.getId());
            ManagementInfoPo diff = managementInfoPoMap.get(key);
            if (diff != null && diff.getRetentionDuration().equals(managementInfoPo.getRetentionDuration())) {
                build.setSynStatus(SynStatusEnum.IS_SYN.getDesc());
                finalResult.add(build);
            } else {
                build.setSynStatus(SynStatusEnum.NOT_SYN.getDesc());
                finalResult.add(build);
            }
        }

        return new PageVO<ManagementInfoWarpPo>(finalResult, allManagementInfo.getPage(), allManagementInfo.getSize(), allManagementInfo.getTotal());

    }


    @Transactional(rollbackFor = Exception.class)
    public void addNewRetention(ManagementInfoWarpPo managementInfoWarpPo) {
        if (managementInfoWarpPo.getId() != null) {
            throw new IllegalArgumentException("创建时ID必须为空");
        }
        managementInfoService.addNewRetention(managementInfoWarpPo);
        //同步到influxdb
        try {
            String createDatabaseUrl = String.format(Constants.CREATE_DATABASE_URL, managementInfoWarpPo.getNodeUrl(), managementInfoWarpPo.getDatabaseName());
            restTemplate.postForObject(createDatabaseUrl, null, String.class);
            String isDefault = "";
            if (managementInfoWarpPo.isRetentionDefault()) {
                isDefault = "default";
            }
            String createRetentionUrl = String.format(Constants.BASE_URL, managementInfoWarpPo.getNodeUrl(), managementInfoWarpPo.getDatabaseName());
            String createRetentionData = String.format(Constants.CREATE_RETENTION_URL, managementInfoWarpPo.getRetentionName(), managementInfoWarpPo.getDatabaseName(), managementInfoWarpPo.getRetentionDuration(), isDefault);
            URI createUri = getUri(createRetentionUrl, createRetentionData);
            restTemplate.postForObject(createUri, null, String.class);
        } catch (RestClientException e) {
            throw new InfluxDBProxyException(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRetention(ManagementInfoPo managementInfoPo) {
        //根据 节点更新本地信息
        ManagementInfoPo lastManagementInfo = managementInfoService.updateRetention(managementInfoPo);
        //再更新influxdb
        String isDefault = "";
        if (lastManagementInfo.isDefault()) {
            isDefault = "default";
        }
        String updateRetentionUrl = String.format(Constants.BASE_URL, lastManagementInfo.getNodeUrl(), lastManagementInfo.getDatabaseName());
        String updateRetentionData = String.format(Constants.UPDATE_RETENTION_URL, lastManagementInfo.getRetentionName(), lastManagementInfo.getDatabaseName(), lastManagementInfo.getRetentionDuration(), isDefault);
        try {
            URI uri = getUri(updateRetentionUrl, updateRetentionData);
            restTemplate.postForObject(uri, null, String.class);
        } catch (RestClientException e) {
            throw new InfluxDBProxyException(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRetention(Long id) {
        //根据ID软删除本地信息
        ManagementInfoPo managementInfoPo = managementInfoService.deleteRetentionById(id);
        //再删除influxdb
        String deleteRetentionUrl = String.format(Constants.BASE_URL, managementInfoPo.getNodeUrl(), managementInfoPo.getDatabaseName());
        String deleteRetentionData = String.format(Constants.DELETE_RETENTION_URL, managementInfoPo.getRetentionName(), managementInfoPo.getDatabaseName());

        try {
            URI uri = getUri(deleteRetentionUrl, deleteRetentionData);
            restTemplate.postForObject(uri, null, String.class);
        } catch (RestClientException e) {
            throw new InfluxDBProxyException(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void synLocalDataToInfluxDB(Long id) {
        ManagementInfoPo managementInfoPo = managementInfoService.selectRetentionById(id);
        //同步influxdb
        String isDefault = "";
        if (managementInfoPo.isDefault()) {
            isDefault = "default";
        }
        String updateRetentionUrl = String.format(Constants.BASE_URL, managementInfoPo.getNodeUrl(), managementInfoPo.getDatabaseName());
        String updateRetentionData = String.format(Constants.UPDATE_RETENTION_URL, managementInfoPo.getRetentionName(), managementInfoPo.getDatabaseName(), managementInfoPo.getRetentionDuration(), isDefault);

        try {
            URI uri = getUri(updateRetentionUrl, updateRetentionData);
            restTemplate.postForObject(uri, null, String.class);
        } catch (RestClientException e) {
            throw new InfluxDBProxyException(e.getMessage());
        }
    }

    private static URI getUri(String updateRetentionUrl, String updateRetentionData) {
        Map<String, String> params = Collections.singletonMap("q", updateRetentionData);
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(updateRetentionUrl);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            uriComponentsBuilder.queryParam(entry.getKey(), entry.getValue());
        }
        return uriComponentsBuilder.build().encode().toUri();
    }

    private QueryResult getQueryResult(String databaseUrl) {
        String forObject = restTemplate.getForObject(databaseUrl, String.class);
        JSONObject jsonObject = JSON.parseObject(forObject);
        return jsonObject.toJavaObject(QueryResult.class);
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}

