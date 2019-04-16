package com.bkjk.influx.proxy.config;

import com.bkjk.influx.proxy.common.micrometer.MyRegistryCustomizer;
import com.bkjk.influx.proxy.common.micrometer.PlatformTag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/3/28 14:35
 **/
@Configuration
public class MicrometerConfig {

    @Bean
    public PlatformTag platformTag(){
        return new PlatformTag();
    }
    @Bean
    public MyRegistryCustomizer myRegistryCustomizer(PlatformTag platformTag){
        return new MyRegistryCustomizer(platformTag);
    }
}
