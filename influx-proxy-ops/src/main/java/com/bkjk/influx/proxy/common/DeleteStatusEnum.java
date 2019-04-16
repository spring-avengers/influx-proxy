package com.bkjk.influx.proxy.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyson
 * @version V1.0
 * @Description: TODO
 * @date 2019/3/20上午11:13
 */
@Getter
@AllArgsConstructor
public enum DeleteStatusEnum {

    /**
     * 删除状态 0- 未删除
     */
    NOT_DELETE(0, "未删除"),

    /**
     * 删除状态 1- 已删除
     */
    IS_DELETE(1, "已删除");


    private Integer code;

    private String desc;


    public static DeleteStatusEnum get(Integer value) {
        for (DeleteStatusEnum e : values()) {
            if (e.getCode().compareTo(value) == 0) {
                return e;
            }
        }
        return null;
    }

    public boolean eq(Integer code) {
        if (code == null) {
            return false;
        }
        return code.intValue() == this.getCode();
    }
}
