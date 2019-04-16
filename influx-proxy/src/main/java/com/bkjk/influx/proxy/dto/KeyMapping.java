package com.bkjk.influx.proxy.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/1/25 18:05
 **/
@Data
public class KeyMapping extends BaseDto {
    @NotNull
    private String databaseRegex;
    @NotNull
    private String measurementRegex;
    @NotNull
    private String backendNodeNames;
}
