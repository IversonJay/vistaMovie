package com.lhh.vista.temp.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by soap on 2016/12/12.
 */
@Setter
@Getter
public class CinemaMovie {
    private String mid;//ID
    private String mname;//电影名称
    private String mtype;//电影类型
    private Integer runTime;//时长
    private String hOFilmCode;//上映时间
}
