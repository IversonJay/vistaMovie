package com.lhh.vista.service.dao;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.service.BaseDao;
import com.lhh.vista.service.dto.BaseCity;
import com.lhh.vista.service.model.City;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CityDao extends BaseDao {

    public PagerResponse<City> getPager(PagerRequest pager) {
        return getPagerByCmd("City.getAll", pager, null);
    }

    public City find(Integer id) {
        return sqlSession.selectOne("City.getById", id);
    }

    public Integer create(City City) {
        sqlSession.insert("City.create", City);
        return City.getId();
    }

    public Integer update(City city) {
        sqlSession.update("City.update", city);
        return city.getId();
    }

    public void del(Integer id) {
        sqlSession.delete("City.del", id);
    }

    public List<BaseCity> getBaseAll(String name) {
    	Map<String, String> map = new HashMap<>();
    	map.put("name", name);
        return sqlSession.selectList("City.getBaseCityAll", map);
    }


    public City getLatelyCity(Double lat, Double lon) {
        Map<String, Object> param = new HashMap<>();
        param.put("lat", lat);
        param.put("lon", lon);
        return sqlSession.selectOne("City.getLatelyCity", param);
    }

	public City getCityByName(String name) {
		return sqlSession.selectOne("City.getCityByName", name);
	}

}
