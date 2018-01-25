package com.lhh.vista.customer.service;

import com.lhh.vista.customer.VistaApi;
import com.lhh.vista.customer.s2v.AddConcessionWarp;
import com.lhh.vista.customer.s2v.AddTicketWarp;
import com.lhh.vista.customer.s2v.CancelTicketWarp;
import com.lhh.vista.customer.s2v.CompleteOrderWarp;
import com.lhh.vista.customer.s2v.GetOrderWarp;
import com.lhh.vista.customer.s2v.MemberValidateWarp;
import com.lhh.vista.customer.s2v.RemoveConcessionWarp;
import com.lhh.vista.customer.v2s.CompleteOrderRes;
import com.lhh.vista.customer.v2s.GetConcessionListRes;
import com.lhh.vista.customer.v2s.OrderRes;
import com.lhh.vista.customer.v2s.StateResult;

/**
 * Created by liu on 2016/12/21.
 */
public class OrderApi {
    private VistaApi vistaApi;

    public OrderApi(VistaApi vistaApi) {
        this.vistaApi = vistaApi;
    }

    /**
     * 生成订单接口,传当前登录用户的ID和生成的订单号
     *
     * @param memberId
     * @param orderId
     * @return
     */
    public StateResult checkUser(String memberId, String orderId) {
        MemberValidateWarp memberwarp = new MemberValidateWarp();
        memberwarp.setMemberId(memberId);
        memberwarp.setClientName(vistaApi.getClientName());
        memberwarp.setClientId(vistaApi.getClientId());
        memberwarp.setClientClass(vistaApi.getClientClass());
        memberwarp.setSessionId(orderId);
        memberwarp.setReturnMember(false);
        String res = vistaApi.post(vistaApi.getServerOrder()+"/WSVistaWebClient/RESTLoyalty.svc/member/validate", memberwarp);
        System.out.println(res);
        StateResult r = null;
        try {
            r = vistaApi.getJsonUtil().readValue(res, StateResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 添加票接口
     *
     * @param addTicket
     * @return
     */
    public OrderRes addTicket(AddTicketWarp addTicket) {
        addTicket.setClientName(vistaApi.getClientName());
        addTicket.setClientId(vistaApi.getClientId());
        addTicket.setClientClass(vistaApi.getClientClass());
        System.out.print("order params:"+addTicket.toString());
        String res = vistaApi.post(vistaApi.getServerOrder()+"/WSVistaWebClient/RestTicketing.svc/order/tickets", addTicket);
        OrderRes r = null;
        try {
            r = vistaApi.getJsonUtil().readValue(res, OrderRes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    public OrderRes getOrder(String orderId) {
        GetOrderWarp warp = new GetOrderWarp();
        warp.setOrderId(orderId);
        warp.setClientName(vistaApi.getClientName());
        warp.setClientId(vistaApi.getClientId());
        warp.setClientClass(vistaApi.getClientClass());

        String res = vistaApi.post(vistaApi.getServerOrder()+"/WSVistaWebClient/RestTicketing.svc/order", warp);
        OrderRes r = null;
        try {
            r = vistaApi.getJsonUtil().readValue(res, OrderRes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 取消订单接口
     *
     * @param orderId
     */
    public void cancelOrder(String orderId) {
        CancelTicketWarp warp = new CancelTicketWarp();
        warp.setOrderId(orderId);
        warp.setClientName(vistaApi.getClientName());
        warp.setClientId(vistaApi.getClientId());
        warp.setClientClass(vistaApi.getClientClass());
        vistaApi.post(vistaApi.getServerOrder()+"/WSVistaWebClient/RESTTicketing.svc/order/cancel", warp);
    }

    /**
     * 完成订单接口
     *
     * @param warp
     * @return
     */
    public CompleteOrderRes completeOrder(CompleteOrderWarp warp) {
        warp.setClientName(vistaApi.getClientName());
        warp.setClientId(vistaApi.getClientId());
        warp.setClientClass(vistaApi.getClientClass());

        String res = vistaApi.post(vistaApi.getServerOrder()+"/WSVistaWebClient/RestTicketing.svc/order/payment", warp);
        CompleteOrderRes r = null;
        try {
            r = vistaApi.getJsonUtil().readValue(res, CompleteOrderRes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 添加卖品接口
     *
     * @param warp
     * @return
     */
    public StateResult addConcessionList(AddConcessionWarp warp) {
        warp.setClientName(vistaApi.getClientName());
        warp.setClientId(vistaApi.getClientId());
        warp.setClientClass(vistaApi.getClientClass());
        String res = vistaApi.post(vistaApi.getServerOrder()+"/WSVistaWebClient/RESTTicketing.svc/order/concessions", warp);
        StateResult r = null;
        try {
            r = vistaApi.getJsonUtil().readValue(res, StateResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 移出卖品接口,暂时不可用
     *
     * @param warp
     * @return
     */
    public StateResult removeConcessionList(RemoveConcessionWarp warp) {
        warp.setClientName(vistaApi.getClientName());
        warp.setClientId(vistaApi.getClientId());
        warp.setClientClass(vistaApi.getClientClass());
        String res = vistaApi.delete(vistaApi.getServerOrder()+"/WSVistaWebClient/RESTTicketing.svc/order/concessions", warp);
        StateResult r = null;
        try {
            r = vistaApi.getJsonUtil().readValue(res, StateResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 获取卖品列表
     */
    public GetConcessionListRes getConcessionList(String cid) {
        String res = vistaApi.get(vistaApi.getServerOrder()+"/WSVistaWebClient/RESTData.svc/concession-items?cinemaId=" + cid + "&clientId=" + vistaApi.getClientId());
        GetConcessionListRes r = null;
        try {
            r = vistaApi.getJsonUtil().readValue(res, GetConcessionListRes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //userSessionId 订单号 - 32位唯一 否 clientId 第三方登录信息 -  否 cinemaId
        return r;
    }
}
