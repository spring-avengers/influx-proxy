package com.bkjk.influx.proxy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/1/25 18:05
 **/
@Data
@TableName("key_mapping")
public class KeyMappingPo extends BasePo {
    @NotBlank
    private String databaseRegex;
    @NotBlank
    private String measurementRegex;
    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9\\u4e00-\\u9fa5]{1,48}", message = "名称支持中文、英文、数字，最多输入24个汉字")
    private String backendNodeNames;
}
