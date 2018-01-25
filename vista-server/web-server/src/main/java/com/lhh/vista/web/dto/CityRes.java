package com.lhh.vista.web.dto;

import com.lhh.vista.common.model.BaseResult;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/28.
 */
@Setter
@Getter
public class CityRes extends BaseResult {
    public CityRes(Integer state) {
        super(state);
    }

    public CityRes() {
        super();
    }

    private String cityName;

    private Integer cityId;
}
