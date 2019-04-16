/**
 * =============================================================
 * Copyright 2018 Lianjia Group All Rights Reserved
 * CompanyName: 上海链家有限公司
 * SystemName: 贝壳
 * ClassName: ManagementInfoWarpPo
 * version: 1.0.0
 * date: 2019/3/20
 * author: Tyson
 * =============================================================
 */
package com.bkjk.influx.proxy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Pattern;

/**
 * @author Tyson
 * @version V1.0
 * @Description: TODO
 * @date 2019/3/20下午4:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagementInfoWarpPo extends BasePo {
    private String nodeUrl;
    @Pattern(regexp = "^[0-9a-zA-Z_]{1,}$", message = "由数字、26个英文字母或者下划线组成的字符串")
    private String databaseName;
    @Pattern(regexp = "^[0-9a-zA-Z_]{1,}$", message = "由数字、26个英文字母或者下划线组成的字符串")
    private String retentionName;
    @Range(min = 0 ,max = 60 ,message = "最长不超过60天")
    private Integer retentionDuration;
    private Integer deleteTag;
    private boolean retentionDefault;
    private String synStatus;
}
