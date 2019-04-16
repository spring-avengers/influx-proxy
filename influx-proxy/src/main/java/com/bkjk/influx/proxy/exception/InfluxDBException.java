package com.bkjk.influx.proxy.exception;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/3/4 14:32
 **/
public class InfluxDBException extends RuntimeException {
    public InfluxDBException() {
    }

    public InfluxDBException(String message) {
        super(message);
    }

    public InfluxDBException(String message, Throwable cause) {
        super(message, cause);
    }

    public InfluxDBException(Throwable cause) {
        super(cause);
    }

    public static final InfluxDBException create(String error){
        return new InfluxDBException(error);
    }
}
