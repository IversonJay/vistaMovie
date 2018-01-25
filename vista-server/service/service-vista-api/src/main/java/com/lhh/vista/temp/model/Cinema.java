package com.lhh.vista.temp.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by soap on 2016/12/10.
 */
@Setter
@Getter
public class Cinema {
    private String cid;
    private String cname;
    private String cadd;
    private String city;

    private Double lon;
    private Double lat;

    private String parkingInfo;
    private Integer minprice;
    
    private Integer loyaltyCode;
}
