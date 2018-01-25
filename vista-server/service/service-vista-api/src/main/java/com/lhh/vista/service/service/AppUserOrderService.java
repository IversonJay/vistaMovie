package com.lhh.vista.service.service;

import java.util.List;

import com.lhh.vista.service.model.AppUserOrder;

/**
 * Created by liu on 2016/12/21.
 */
public interface AppUserOrderService {
    void createOrder();

    List<AppUserOrder>  getOrderByStime(String queryTime);
}
