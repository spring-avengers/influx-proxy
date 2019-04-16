package com.bkjk.influx.proxy.config;

import com.bkjk.influx.proxy.handler.InfluxProxyHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/1/25 19:40
 **/
@Configuration
public class Router {
    @Bean
    public RouterFunction<ServerResponse> routeCity(InfluxProxyHandler influxProxyHandler) {
        return RouterFunctions
                .route(RequestPredicates.path("/query")
                                .and(RequestPredicates.accept(MediaType.ALL)),
                        influxProxyHandler::query)
                .andRoute(RequestPredicates.POST("/write")
                                .and(RequestPredicates.accept(MediaType.ALL)),
                        influxProxyHandler::write)
                .andRoute(RequestPredicates.GET("/ping")
                                .and(RequestPredicates.accept(MediaType.ALL)),
                        influxProxyHandler::ping)
                .andRoute(RequestPredicates.path("/debug/{opt}")
                                .and(RequestPredicates.accept(MediaType.ALL)),
                        influxProxyHandler::debug)
                .andRoute(RequestPredicates.path("/debug")
                                .and(RequestPredicates.accept(MediaType.ALL)),
                        influxProxyHandler::debug)
                .andRoute(RequestPredicates.path("/refresh/allBackend")
                                .and(RequestPredicates.accept(MediaType.ALL)),
                        influxProxyHandler::refreshAllBackend)
                ;
    }
}
