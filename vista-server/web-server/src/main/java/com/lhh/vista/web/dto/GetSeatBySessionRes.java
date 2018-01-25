package com.lhh.vista.web.dto;

import com.lhh.vista.customer.v2s.value.SeatLayoutData;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by soap on 2016/12/16.
 */
@Setter
@Getter
public class GetSeatBySessionRes {
    private String cid;
    private String mid;
    private String mname;
    private String cname;
    private String cadd;
    private String sname;
    private String stime;
    private Integer runTime;

    private SeatLayoutData seatLayoutData;
}
