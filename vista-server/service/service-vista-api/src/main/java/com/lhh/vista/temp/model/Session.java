package com.lhh.vista.temp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by soap on 2016/12/11.
 */
@Setter
@Getter
@ToString
public class Session {
    private String sid;//场次ID
    private String cid;//电影院ID
    private String mid;//电影ID
    private String stime;//开始时间
    private Integer sprice;//价格
    private Integer originalPrice;
    private String stype;//场次类型
    private String screenName;//影厅名
}
