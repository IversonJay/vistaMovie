package com.lhh.vista.temp.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2017/1/11.
 */
@Setter
@Getter
public class BaseCinema {
    private String cid;
    private String cname;
    private String cadd;

    private Double lon;
    private Double lat;
    
    //create by liuHj
    private Double distance;
    private Integer loyaltyCode;
}
