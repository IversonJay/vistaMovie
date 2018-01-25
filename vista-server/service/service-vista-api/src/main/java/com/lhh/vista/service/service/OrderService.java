package com.lhh.vista.service.service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.service.dto.*;
import com.lhh.vista.service.model.AppUserOrder;

import java.util.List;
import java.util.Map;

/**
 * Created by liu on 2016/12/23.
 */
public interface OrderService {
    List<Concession> getConcession(String userId,Integer orderId);

    /**
     * 设置卖品
     * @param cons
     * @param orderId
     */
    void setConcession(List<Concession> cons,String userId,  Integer orderId, String mphone);

    /**
     * 先验证票类信息,看看用户可以买几个类型的票
     *
     * @param memberId
     */
    Map<String, List<UserTicket>> checkTicket(List<String> areaList, String sid, String memberId);

    /**
     * 创建订单的方法
     *
     * @param seatInfoList
     * @param memberId
     */
    Integer createOrder(List<SeatInfo> seatInfoList, String sid, String payment, String memberId, String mphone);


    PagerResponse<BaseUserOrder> getUserOrder(PagerRequest pagerRequest, String memberId,Integer otype);

    AppUserOrder getOrder(String userId, Integer orderId);

    BaseOrderForPay getOrderForPay(String userId, Integer orderId);

    AppUserOrder completeOrder(String userId, Integer orderId, Integer payType,String pin);

    void cancelOrder(String userId, Integer orderId);

    AppUserOrder getOrder(String orderId);

	AppUserOrder getOrderById(Integer orderId);

	AppUserOrder completeExchangeOrder(String memberId, Integer oid, List<String> codes, String payment);

	AppUserOrder completeVoucherOrder(String memberId, Integer oid, List<String> codes);

}
