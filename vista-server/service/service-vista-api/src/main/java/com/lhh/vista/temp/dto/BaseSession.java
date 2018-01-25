package com.lhh.vista.temp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by soap on 2016/12/11.
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseSession {
    private String sid;//ID
    private String stime;//开始时间
    private Integer sprice;//价格
    private Integer originalPrice;
    private String stype;//场次类型
    private String screenName;//场次类型
    
    private String spriceDescr;
    private String originalPriceDescr;
    private Integer clubId;
    private Integer runTime;
    private String etime;
}
