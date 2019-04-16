package com.bkjk.influx.proxy.exception;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/3/4 14:32
 **/
public class InfluxDBProxyException extends RuntimeException {
    public InfluxDBProxyException() {
    }

    public InfluxDBProxyException(String message) {
        super(message);
    }

    public InfluxDBProxyException(String message, Throwable cause) {
        super(message, cause);
    }

    public InfluxDBProxyException(Throwable cause) {
        super(cause);
    }

    public static final InfluxDBProxyException create(String error){
        return new InfluxDBProxyException(error);
    }
}
