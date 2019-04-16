package com.bkjk.influx.proxy.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/1/25 17:57
 **/
@Data
public abstract class BaseDto {
    private Long id;

    private Date createdDate;

    private Date updateDate;
}
