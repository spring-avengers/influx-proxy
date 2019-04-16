package com.bkjk.influx.proxy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/1/25 17:57
 **/
@Data
public abstract class BasePo {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField
    private Date createdDate;

    @TableField
    private Date updateDate;
}
