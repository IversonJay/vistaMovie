package com.lhh.vista.temp.model;


import lombok.Getter;
import lombok.Setter;

/**
 * Created by soap on 2016/12/11.
 */
@Setter
@Getter
public class Movie {
    private String mid;//ID
    private String mname;//电影名称
    private String mnamee;//电影英文名称
    private String synopsis;//简介
    private String details;//详情
    private String mtype;//电影类型
    private String director;//导演
    private String performer;//演员
    private Integer runTime;//时长
    private String openingDate;//上映时间
    private Integer cinemaCount;//上映的电影院数量
    private String hOFilmCode;//上映时间
    

    private Integer mstate;//电影的上映情况,即将上映,正在热映

    public static final int STATE_WEICHULI = 0;
    public static final int STATE_ZZRY = 1;
    public static final int STATE_JJSY = 2;


    //一下是扩展信息,和数据表无关的
    private Integer sessionNum;
    private String merge;//电影合并的标识
    private Integer idx;
}
