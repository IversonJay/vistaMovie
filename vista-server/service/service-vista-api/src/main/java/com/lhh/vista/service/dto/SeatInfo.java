package com.lhh.vista.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by liu on 2016/12/23.
 */
@Setter
@Getter
@ToString
public class SeatInfo {
    private String areaCode;
    private Integer row;
    private Integer col;
    private Short areaNum;

    private String rowName;
    private String colName;
}