package com.lhh.vista.service.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/27.
 */
@Setter
@Getter
public class BaseOrderForPay {
    private Integer id;
    private String orderId;//区域的中文名称,仅用来显示
    private Integer totalPrice;//总的价格
    private String mname;
    private String cid;

    private Long createTime;
}
