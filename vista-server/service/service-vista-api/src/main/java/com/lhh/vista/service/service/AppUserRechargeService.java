package com.lhh.vista.service.service;

import com.lhh.vista.service.model.AppUserRecharge;

/**
 * Created by liu on 2016/12/7.
 */

public interface AppUserRechargeService {

    AppUserRecharge appUserRecharge(String itemId, String userId);

    void finishUserRecharge(String userId, Integer orderId, Integer payType);

    void cancelOrder(String userId, Integer orderId);

    AppUserRecharge get(String orderId);
}
