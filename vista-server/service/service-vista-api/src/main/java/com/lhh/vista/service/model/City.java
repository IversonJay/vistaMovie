package com.lhh.vista.service.model;

import com.lhh.vista.common.model.BaseModelI;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/7.
 */
@Setter
@Getter
public class City extends BaseModelI {
    private String cityName;
    private String ename;
    private String pinyin;
    private Integer hot;
    private Double lat;//维度
    private Double lon;//经度

    public static final int HOT_YES = 1;
    public static final int HOT_NO = 2;
}
