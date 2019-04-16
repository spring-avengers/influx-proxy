/**
 * =============================================================
 * Copyright 2018 Lianjia Group All Rights Reserved
 * CompanyName: 上海链家有限公司
 * SystemName: 贝壳
 * ClassName: ManagementInfoPo
 * version: 1.0.0
 * date: 2019/3/19
 * author: Tyson
 * =============================================================
 */
package com.bkjk.influx.proxy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bkjk.influx.proxy.common.StringToBoolJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

/**
 *
 * @Description: TODO
 * @author Tyson
 * @date 2019/3/19下午4:29
 * @version V1.0
 */
@Data
@TableName("management_info")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagementInfoPo extends BasePo {

    private String nodeUrl;
    //不能中文
    private String databaseName;
    //不能中文
    private String retentionName;
    //整数单位是天
    @Range(min = 0 ,max = 60 ,message = "最长不超过60天")
    private Integer retentionDuration;
    private Integer deleteTag;

    @JsonDeserialize(using = StringToBoolJsonDeserializer.class)
    private boolean isDefault;

}
