package com.lhh.vista.temp.model;


import lombok.Getter;
import lombok.Setter;

/**
 * Created by soap on 2016/12/13.
 */
@Setter
@Getter
public class Ticket {
    private String cid;//电影院ID
    private String sid;//场次ID
    private String area;//区域类别编码
    private String desc;// 票类名称
    private Integer price;// 单价
    private Integer memberPrice;//会员价
    private Integer salePrice;//折扣价
    private Integer zxPrice;//尊享价
    private Integer dhqPrice;//预留字段
}
