package com.bkjk.influx.proxy.handler;

import com.bkjk.influx.proxy.dto.BackendNode;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.reactive.function.client.WebClient;

@Data
@Builder
public class InfluxClient {
    private WebClient webClient;
    private BackendNode backendNode;
}
