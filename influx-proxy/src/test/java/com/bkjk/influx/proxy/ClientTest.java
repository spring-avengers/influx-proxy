package com.bkjk.influx.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.ChannelOption;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import static com.bkjk.influx.proxy.service.BackendService.logRequest;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/2/25 18:38
 **/
public class ClientTest {
    public static void main(String[] args) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setSerializationInclusion(NON_NULL);
//        InfluxDB influxdb = InfluxDBFactory.connect("http://172.29.64.250:18086");
//        QueryResult result = influxdb.query(new Query("select * from health limit 1", "micrometerDb"));
//        System.out.println(objectMapper.writeValueAsString(result));
//        influxdb.close();

        TcpClient tcpClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8086")
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .filter(logRequest())
                .build();
        System.out.println(webClient.get().uri(uriBuilder -> uriBuilder
                .path("query")
                .queryParam("db", "micrometerDb")
                .queryParam("q", "select * from cpu")
                .build()
        )
                .exchange()
                .flatMap(clientResponse -> clientResponse.toEntity(String.class))
                .block().getBody());
    }
}
