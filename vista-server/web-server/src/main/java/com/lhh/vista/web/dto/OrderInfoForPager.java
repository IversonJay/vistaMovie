package com.lhh.vista.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/26.
 */
@Setter
@Getter
public class OrderInfoForPager{
    private Integer orderId;

    private String cname;
    private String mname;
    private String sname;
    private String stime;
    private String splaceName;
    private Integer totalPrice;

}
