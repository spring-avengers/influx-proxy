package com.bkjk.influx.proxy;

import org.junit.After;
import org.junit.Before;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = InfluxProxyApplication.class)
public class InfluxProxyApplicationTests {

    @Before
    public void before() {
    }

    @After
    public void after(){
    }



    public static void main(String[] args) throws IOException {
        HashSet<String> apps=new HashSet<>();
        apps.addAll(p("http://eureka.dev.bkjk.cn/eureka/apps"));
        apps.addAll(p("http://eureka.test.bkjk.cn/eureka/apps"));
        apps.addAll(p("http://eureka.stage.bkjk.cn/eureka/apps"));
        apps.stream().forEach(System.out::println);
    }

    public static final Set<String> p(String uri){
        WebClient client = WebClient.create();
        Mono<ResponseEntity<HashMap>> ret = client.get().uri(uri).header("Accept", "application/json").exchange().block().toEntity(HashMap.class);
        Map<String,List> group =(Map) ((List) ((Map) ret.block().getBody().get("applications")).get("application"))
                .stream()
                .map(s -> (Map) s)
                .collect(Collectors.groupingBy((a) -> ((Map) a).get("name")));
        return group.entrySet().stream().map(en->en.getKey().toLowerCase()).collect(Collectors.toSet());
    }
}

