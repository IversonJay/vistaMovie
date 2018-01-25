package com.lhh.vista.service.dto;

import com.lhh.vista.common.model.BaseModelI;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/7.
 */
@Setter
@Getter
public class BaseCity {
    private Integer id;
    private String cityName;
    private Double lat;//维度
    private Double lon;//经度
    private String pinyin;
    private Integer hot;
}
