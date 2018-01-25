package com.lhh.vista.service.service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.service.dto.BaseCity;
import com.lhh.vista.service.model.City;

import java.util.List;

/**
 * Created by liu on 2016/12/7.
 */

public interface CityService {
    Integer save(City city);

    void del(Integer id);

    City find(Integer id);

    PagerResponse<City> getPager(PagerRequest pager);

    List<BaseCity> getBaseAll(String name);

    City getLatelyCity(Double lat,Double lon);

	City getCityByName(String name);
}
