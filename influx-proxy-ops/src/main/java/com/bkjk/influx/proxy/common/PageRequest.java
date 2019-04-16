/**
 * =============================================================
 * Copyright 2018 Lianjia Group All Rights Reserved
 * CompanyName: 上海链家有限公司
 * SystemName: 贝壳
 * ClassName: PageRequest
 * version: 1.0.0
 * date: 2019/3/14
 * author: Tyson
 * =============================================================
 */
package com.bkjk.influx.proxy.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Tyson
 * @version V1.0
 * @Description: TODO
 * @date 2019/3/14上午9:49
 */
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    private int current = 0;
    private int pageSize = 10;
    private Map<String, Object> param;
    private Map<String, Boolean> orderBy = new HashMap();

    public PageRequest() {
    }

    public int getOffset() {
        return this.current > 0 ? this.pageSize * this.current : 0;
    }

    public int getCurrent() {
        return this.current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Map<String, Object> getParam() {
        return this.param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public Map<String, Boolean> getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(Map<String, Boolean> orderBy) {
        this.orderBy = orderBy;
    }

    public void addOrderBy(String column, boolean asc) {
        this.orderBy.put(column, asc);
    }
}
