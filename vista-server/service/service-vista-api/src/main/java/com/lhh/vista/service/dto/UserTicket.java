package com.lhh.vista.service.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/26.
 */
@Setter
@Getter
public class UserTicket {
    private String area;
    private String ticketCode;
    private String ticketName;
    private Integer price;
}
