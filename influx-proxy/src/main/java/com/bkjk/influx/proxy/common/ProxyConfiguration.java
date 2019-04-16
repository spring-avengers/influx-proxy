package com.bkjk.influx.proxy.common;

import com.bkjk.influx.proxy.dto.BackendNode;
import com.bkjk.influx.proxy.dto.KeyMapping;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/3/1 12:55
 **/
@Configuration
@ConfigurationProperties(prefix = "proxy.config")
@Data
public class ProxyConfiguration {
    boolean enable;
    List<BackendNode> node;
    List<KeyMapping> mapping;
}
