package com.bkjk.influx.proxy.common;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/1/25 18:51
 **/
public interface Constants {
    String DATABASE_URL = "%s/query?pretty=true&q=show databases";
    String RETENTION_URL = "%s/query?pretty=true&q=show retention POLICIES on %s";
    String CREATE_DATABASE_URL = "%s/query?pretty=true&q=CREATE DATABASE %s";
    String CREATE_RETENTION_URL = "create retention policy \"%s\"  on  \"%s\" duration %sd replication 1 %s";
    String UPDATE_RETENTION_URL = "ALTER RETENTION POLICY \"%s\" ON \"%s\" DURATION  %sd %s";
    String DELETE_RETENTION_URL = "DROP RETENTION POLICY \"%s\" ON \"%s\" ";
    String BASE_URL = "%s/query?pretty=true&db=%s";
}

