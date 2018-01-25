package com.lhh.vista.common.model;

public class BaseResult {
    private Integer state;

    public void setState(Integer state) {
        this.state = state;
    }

    public BaseResult(Integer state) {
        this.state = state;
    }

    public BaseResult() {
    }

    public Integer getState() {
        return state;
    }
}
