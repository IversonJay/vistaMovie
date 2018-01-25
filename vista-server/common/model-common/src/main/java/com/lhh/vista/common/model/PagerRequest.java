package com.lhh.vista.common.model;

public class PagerRequest {
    private Integer page = 1;// 页数
    private Integer rows = 10;// 每页显示多少条
    private String sort = "id";// 排序
    private String order = "desc";// 排序方式
    private String orderEx = null;

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderEx() {
        return orderEx;
    }

    public void setOrderEx(String orderEx) {
        this.orderEx = orderEx;
    }
}