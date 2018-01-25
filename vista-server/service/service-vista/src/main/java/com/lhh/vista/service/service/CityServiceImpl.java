package com.lhh.vista.service.service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.service.dao.CityDao;
import com.lhh.vista.service.dto.BaseCity;
import com.lhh.vista.service.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liu on 2016/12/7.
 */
@Service
public class CityServiceImpl implements CityService {
    @Autowired
    private CityDao cityDao;

    @Override
    public Integer save(City city) {
        if (city.getId() != null) {
            return cityDao.update(city);
        } else {
            return cityDao.create(city);
        }
    }

    @Override
    public void del(Integer id) {
        cityDao.del(id);
    }

    @Override
    public City find(Integer id) {
        return cityDao.find(id);
    }

    @Override
    public PagerResponse<City> getPager(PagerRequest pager) {
        return cityDao.getPager(pager);
    }

    @Override
    public List<BaseCity> getBaseAll(String name) {
        return cityDao.getBaseAll(name);
    }

    @Override
    public City getLatelyCity(Double lat, Double lon) {
        return cityDao.getLatelyCity(lat,lon);
    }

	@Override
	public City getCityByName(String name) {
		return cityDao.getCityByName(name);
	}
}
