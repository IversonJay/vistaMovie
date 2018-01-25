package com.lhh.vista.service.model;

import com.lhh.vista.common.model.BaseModelI;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2017/1/7.
 */
@Setter
@Getter
public class AppUserReward extends BaseModelI {
    private String userId;
    private String buyOrderId;//对换抽奖时候的订单号
    private Long buyTime;//对换抽奖的时间
    private String resOrderId;//对换结果时候的订单号
    private Long resTime;//对换结果的时间
    private String resId;//结果ID
    private String resVistaId;//用来关联的ID
    private Integer resSequence;//这个是用来兑换的字段,member/item里面的
    private String resName;//结果ID
    private Integer used;//是否已经使用
    private Integer rtype;//类型

    private String vistaBookingId;


    public static final int USED = 1;
    public static final int NOUSE = 0;

    public static final int TYPE_DZP = 1;
}
