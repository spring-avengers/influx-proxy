package com.bkjk.influx.proxy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/3/1 13:35
 **/
@Configuration
public class RestTemplateConfig {

    private static final int DEFAULT_QUERY_TIMEOUT = 60_000;

    @Bean
    public RestTemplate restTemplate() {
        Logger logger= LoggerFactory.getLogger("RestTemplate");
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setReadTimeout(DEFAULT_QUERY_TIMEOUT);
        restTemplate.setRequestFactory(simpleClientHttpRequestFactory);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ClientHttpRequestInterceptor logClientHttpRequestInterceptor=new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                logger.info("{} {}",request.getMethodValue(),request.getURI());
                return execution.execute(request,body);
            }
        };
        ArrayList<ClientHttpRequestInterceptor> interceptors=new ArrayList<>();
        interceptors.add(logClientHttpRequestInterceptor);
        interceptors.addAll(restTemplate.getInterceptors());
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}
