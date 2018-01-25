package com.lhh.vista.service.model;

import com.lhh.vista.common.model.BaseModelI;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by soap on 2016/12/14.
 */
@Setter
@Getter
public class AppUserOrder extends BaseModelI {
    public static final int STATE_WAIT_PAY = 1;
    public static final int STATE_TIME_OUT = 2;
    public static final int STATE_ORDER_SUCCESS = 3;
    public static final int STATE_CANCEL = 4;

    private String userId;//用户ID
    private String orderId;//订单ID
    private String placeNames;//区域的中文名称,仅用来显示
    private String places;//区域
    private Integer concessionPrice;//卖品价格
    private Integer totalPrice;//总的价格
    private Integer ticketPrice;//票的价格
    private String cid;//电影院ID
    private String sid;//场次ID
    private String mid;//电影ID
    private Integer state;//订单状态
    private Long createTime;

    private String cname;
    private String cadd;
    private String mname;
    private String sname;
    private String stime;

    private String vistaBookingId;
    private String vistaBookingNumber;

    private String concessionNames;//卖品详情
    private String concessions;//卖品

    private int ticketCount;
    private String etime;
    private String mphone;
}
