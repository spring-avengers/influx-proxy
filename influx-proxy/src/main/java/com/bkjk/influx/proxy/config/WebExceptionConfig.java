package com.bkjk.influx.proxy.config;

import com.bkjk.influx.proxy.dto.QueryResult;
import com.bkjk.influx.proxy.exception.InfluxDBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/3/4 14:39
 **/
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnClass(WebFluxConfigurer.class)
@AutoConfigureBefore(WebFluxAutoConfiguration.class)
@EnableConfigurationProperties({ ServerProperties.class, ResourceProperties.class })
public class WebExceptionConfig {


    private final ServerProperties serverProperties;

    private final ApplicationContext applicationContext;

    private final ResourceProperties resourceProperties;

    private final List<ViewResolver> viewResolvers;

    private final ServerCodecConfigurer serverCodecConfigurer;

    public WebExceptionConfig(ServerProperties serverProperties,
                                         ResourceProperties resourceProperties,
                                         ObjectProvider<ViewResolver> viewResolversProvider,
                                         ServerCodecConfigurer serverCodecConfigurer,
                                         ApplicationContext applicationContext) {
        this.serverProperties = serverProperties;
        this.applicationContext = applicationContext;
        this.resourceProperties = resourceProperties;
        this.viewResolvers = viewResolversProvider.orderedStream()
                .collect(Collectors.toList());
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    @Bean
    public ErrorWebExceptionHandler errorWebExceptionHandler(
            ErrorAttributes errorAttributes) {
        InfluxErrorWebExceptionHandler exceptionHandler = new InfluxErrorWebExceptionHandler(
                errorAttributes, this.resourceProperties,
                this.serverProperties.getError(), this.applicationContext);
        exceptionHandler.setViewResolvers(this.viewResolvers);
        exceptionHandler.setMessageWriters(this.serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(this.serverCodecConfigurer.getReaders());
        return exceptionHandler;
    }

    @Slf4j
    public static final class InfluxErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

        /**
         * Create a new {@code DefaultErrorWebExceptionHandler} instance.
         *
         * @param errorAttributes    the error attributes
         * @param resourceProperties the resources configuration properties
         * @param errorProperties    the error configuration properties
         * @param applicationContext the current application context
         */
        public InfluxErrorWebExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
            super(errorAttributes, resourceProperties, errorProperties, applicationContext);
        }

        @Override
        protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
            if(this.getError(request)instanceof InfluxDBException){
                QueryResult queryResult=new QueryResult();
                QueryResult.Result result=new QueryResult.Result();
                result.setStatementId(0);
                result.setError(this.getError(request).getMessage());
                queryResult.setResults(Arrays.asList(result));
                return ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .syncBody(queryResult);
            }else{
                return super.renderErrorResponse(request);
            }
        }
    }

}
