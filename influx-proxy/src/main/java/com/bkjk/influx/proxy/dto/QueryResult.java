package com.bkjk.influx.proxy.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/2/27 10:26
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryResult {
    public QueryResult(List<Result> results) {
        this.results = results;
    }

    public QueryResult() {
    }

    private List<Result> results;
    private String error;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Result {
        private List<Series> series;
        private String error;
        @JsonProperty("statement_id")
        private long statementId;

    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Series {
        private String name;
        private Map<String, String> tags;
        private List<String> columns;
        private List<List<Object>> values;

    }

}
