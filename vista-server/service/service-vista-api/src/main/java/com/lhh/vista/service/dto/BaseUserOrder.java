package com.lhh.vista.service.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/27.
 */
@Setter
@Getter
public class BaseUserOrder {
    private Integer id;
    private String placeNames;//区域的中文名称,仅用来显示
    private String concessionNames;//卖品的中文名称,仅用来显示
    private Integer totalPrice;//总的价格
    private Integer state;//订单状态
    private Long createTime;
    private Integer ticketCount;
    private String places;

    private String cname;
    private String mname;
    private String sname;
    private String stime;
    private String mphone;

    private String mid;
    private int type;
}
