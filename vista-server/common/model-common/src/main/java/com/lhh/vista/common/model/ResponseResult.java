package com.lhh.vista.common.model;

/**
 * Created by liu on 2016/12/7.
 */
public class ResponseResult<T> extends BaseResult {
    private T result;
    public T getResult() {
        return result;
    }
    public void setResult(T result) {
        this.result = result;
    }
    public ResponseResult(Integer state) {
        super(state);
    }
}
