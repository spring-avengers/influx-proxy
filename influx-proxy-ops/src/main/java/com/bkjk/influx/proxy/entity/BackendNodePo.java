package com.bkjk.influx.proxy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/1/25 17:56
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("backend_node")
public class BackendNodePo extends BasePo {
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
