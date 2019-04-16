package com.bkjk.influx.proxy.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/1/25 17:56
 **/
@Data
public class BackendNode extends BaseDto {
    @NotNull
    private String nodeName;
    @NotNull
    private String url;
    private Integer writeTimeout;
    private Integer queryTimeout;
    // 单位毫秒
    private Integer healthCheckInterval;
    private boolean online;
    private String statusDescription;
}
