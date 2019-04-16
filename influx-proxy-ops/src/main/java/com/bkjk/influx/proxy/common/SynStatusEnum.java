package com.bkjk.influx.proxy.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyson
 * @version V1.0
 * @Description: TODO
 * @date 2019/3/20上午11:16
 */
@Getter
@AllArgsConstructor
public enum SynStatusEnum {

    /**
     * 同步状态 0- 未同步
     */
    NOT_SYN(0, "未同步"),

    /**
     * 同步状态 1- 同步
     */
    IS_SYN(1, "同步");


    private Integer code;

    private String desc;


    public static SynStatusEnum get(Integer value) {
        for (SynStatusEnum e : values()) {
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
