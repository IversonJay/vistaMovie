package com.lhh.vista.temp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by soap on 2016/12/12.
 */
@Setter
@Getter
public class CinemaAndMovie {
    private String cid;
    private String cname;
    private String cadd;
    private Double lat;
    private Double lon;

    private List<CinemaMovie> movies;

}
