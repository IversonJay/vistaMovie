package com.lhh.vista.service.service;

import com.lhh.vista.service.model.SystemValue;

import java.util.List;

/**
 * Created by soap on 2016/12/18.
 */
public interface SystemValueService {
    void setValue(int key,String value);
    String getValue(int key);

    List<SystemValue> getList();
}
