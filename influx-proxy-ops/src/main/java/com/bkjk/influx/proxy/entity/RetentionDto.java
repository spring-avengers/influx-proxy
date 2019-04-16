/**
 * =============================================================
 * Copyright 2018 Lianjia Group All Rights Reserved
 * CompanyName: 上海链家有限公司
 * SystemName: 贝壳
 * ClassName: RetentionDto
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

/**
 *
 * @Description: TODO
 * @author Tyson
 * @date 2019/3/20下午2:49
 * @version V1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RetentionDto {
    private String name;
    private String duration;
}
