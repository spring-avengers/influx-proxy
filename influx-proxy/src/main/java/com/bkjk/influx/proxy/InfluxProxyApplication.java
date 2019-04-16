package com.bkjk.influx.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InfluxProxyApplication {

    public static void main(String[] args) {
//        System.setProperty("io.netty.leakDetection.maxRecords", "100");
//        System.setProperty("io.netty.leakDetection.acquireAndReleaseOnly", "true");
//        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
        SpringApplication.run(InfluxProxyApplication.class, args);
    }
}

