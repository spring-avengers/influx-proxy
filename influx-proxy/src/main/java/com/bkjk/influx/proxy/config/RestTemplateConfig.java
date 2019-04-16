package com.bkjk.influx.proxy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/3/1 13:35
 **/
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
