package com.lhh.vista.service.model;


import com.lhh.vista.common.model.BaseModelI;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户充值表
 * Created by liu on 2017/1/3.
 */
@Setter
@Getter
public class AppUserRecharge extends BaseModelI {

    public static final int STATE_WAIT_PAY = 1;
    public static final int STATE_TIME_OUT = 2;
    public static final int STATE_ORDER_SUCCESS = 3;
    public static final int STATE_CANCEL = 4;

    private Integer id;
    
    private String userId;//用户ID

    private String orderId;//订单ID

    private int rechargeAmount;//充值金额

    private Integer orderStatus;//订单状态

    private String bz;//备注（留作备用字段）

    private Long createTime;
}
