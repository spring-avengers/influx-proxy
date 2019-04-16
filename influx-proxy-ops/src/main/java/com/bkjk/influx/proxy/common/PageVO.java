package com.bkjk.influx.proxy.common;

import com.bkjk.influx.proxy.common.PageRequest;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


public class PageVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private final long total;
    private final List<T> rows;
    private final int page;
    private final int size;

    public PageVO(List<T> rows, PageRequest pageRequest, long total) {
        this.total = total;
        this.rows = rows;
        this.page = pageRequest.getCurrent();
        this.size = pageRequest.getPageSize();
    }

    public PageVO(List<T> rows, int page, int size, long total) {
        this.total = total;
        this.rows = rows;
        this.page = page;
        this.size = size;
    }

    public long getTotal() {
        return total;
    }

    public List<T> getRows() {
        return Collections.unmodifiableList(rows);
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public boolean hasRows() {
        return rows != null && rows.size() > 0;
    }
}
