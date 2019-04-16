/**
 * =============================================================
 * Copyright 2018 Lianjia Group All Rights Reserved
 * CompanyName: 上海链家有限公司
 * SystemName: 贝壳
 * ClassName: NodePo
 * version: 1.0.0
 * date: 2019/3/14
 * author: Tyson
 * =============================================================
 */
package com.bkjk.influx.proxy.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * @author Tyson
 * @version V1.0
 * @Description: TODO
 * @date 2019/3/14下午4:19
 */
@Data
public class NodePo {

    private Long id;
    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9\\u4e00-\\u9fa5]{1,48}", message = "名称支持中文、英文、数字，最多输入24个汉字")
    private String name;
    @NotBlank
    private String url;
    @Range(min = 0 ,max = 100000 ,message = "写入超时时间在0-100000范围内")
    private Integer writeTimeout;
    @Range(min = 0 ,max = 100000 ,message = "查询超时时间在0-100000范围内")
    private Integer queryTimeout;
    private boolean online;

}
