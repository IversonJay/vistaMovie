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
public class BaseMovie {
    private String mid;//ID
    private String mname;//电影名称
    private String synopsis;//简介
    private String performer;//演员
    private String openingDate;//上映时间
    private Integer cinemaCount;//上映的电影院数量
    private String hOFilmCode;//用来获取图片的ID
    private String director;//演员
    private String merge;
    private int runTime;
    
    
    private int type;
}
