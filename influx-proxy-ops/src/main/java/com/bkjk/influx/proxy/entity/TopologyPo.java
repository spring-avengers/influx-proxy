package com.bkjk.influx.proxy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Program: influx-proxy
 * @Description: 存储实时拓扑信息
 * @Author: shaoze.wang
 * @Create: 2019/1/25 18:09
 **/
@Data
@TableName("topology")
public class TopologyPo extends BasePo {
    private String nodeName;
    private String databaseName;
    private String measurement;
}
