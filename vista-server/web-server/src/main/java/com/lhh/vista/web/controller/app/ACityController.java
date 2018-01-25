package com.lhh.vista.web.controller.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.service.dto.BaseCity;
import com.lhh.vista.service.model.City;
import com.lhh.vista.service.service.CityService;
import com.lhh.vista.web.common.CommonData;
import com.lhh.vista.web.dto.CityRes;

/**
 * Created by liu on 2016/12/7.
 */
class BaseCityList {
	private List<BaseCity> cityList;
	private String leter;
	public List<BaseCity> getCityList() {
		return cityList;
	}
	public void setCityList(List<BaseCity> cityList) {
		this.cityList = cityList;
	}
	public String getLeter() {
		return leter;
	}
	public void setLeter(String leter) {
		this.leter = leter;
	}
}
@Controller
@RequestMapping("a_city")
public class ACityController extends BaseController {
    @Autowired
    private CityService cityService;
    
    /**
     * 获取所有城市
     * @author LiuHJ  修改返回格式
     * @return
     */
    @RequestMapping("getAll")
    @ResponseBody
    public Object getAll(String name) {
    	List<BaseCityList> bigList = new ArrayList<BaseCityList>();
        ResponseResult<List<BaseCityList>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
        	BaseCityList baseCityList = new BaseCityList();
        	List<BaseCity> cityList = cityService.getBaseAll(name);
        	Map<String, List<BaseCity>> map = new TreeMap<String, List<BaseCity>>();
        	for(BaseCity city : cityList) {
        		if (map.containsKey(city.getPinyin())) {
        			map.get(city.getPinyin()).add(city);
        		} else {
        			List<BaseCity> cityListSplitByPy = new ArrayList<BaseCity>();
        			cityListSplitByPy.add(city);
        			map.put(city.getPinyin(), cityListSplitByPy);
        		}
        	}
        	
        	for (Entry<String, List<BaseCity>> entry : map.entrySet()) {
        		BaseCityList list = new BaseCityList();
        		list.setLeter(entry.getKey());
        		list.setCityList(entry.getValue());
        		bigList.add(list);
            }    	
            r.setResult(bigList);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }

//    @RequestMapping("submitLocation")
//    @ResponseBody
//    public Object submitLocation(Double lat, Double lon) {
//        CityRes r = new CityRes(StateTool.State.FAIL);
//        try {
//            StateTool.checkState(lat != null && lon != null, StateTool.State.FAIL);
//            City city = cityService.getLatelyCity(lat, lon);
//            request.getSession().setAttribute(CommonData.CURRENT_CITY, city);
//
//            r.setState(StateTool.State.SUCCESS);
//            r.setCityName(city.getCityName());
//        } catch (StateTool.StateException e) {
//            r.setState(e.getState());
//            e.printStackTrace();
//        }
//        return r;
//    }
    
    /**
     * @author LiuHJ  此接口暂时废弃
     * @param name
     * @return
     */
    @RequestMapping("submitLocation")
    @ResponseBody
    public Object submitLocation(String name) {
        CityRes r = new CityRes(StateTool.State.FAIL);
        try {
            StateTool.checkState(name != null, StateTool.State.FAIL);
            City city = cityService.getCityByName(name);
            request.getSession().setAttribute(CommonData.CURRENT_CITY, city);
            
            if (city == null) {
            	r.setState(StateTool.State.FAIL);
                r.setCityName("无此城市信息");
            }
            r.setState(StateTool.State.SUCCESS);
            r.setCityName(city.getCityName());
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }

    @RequestMapping("getCity")
    @ResponseBody
    public Object getCity(Double lat, Double lon) {
        CityRes r = new CityRes(StateTool.State.FAIL);
        try {
            StateTool.checkState(lat != null && lon != null, StateTool.State.FAIL);
            City city = cityService.getLatelyCity(lat, lon);
            r.setState(StateTool.State.SUCCESS);
            r.setCityName(city.getCityName());
            r.setCityId(city.getId());
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }

    @RequestMapping("submitCity")
    @ResponseBody
    public Object submitCity(Integer id) {
        CityRes r = new CityRes(StateTool.State.FAIL);
        try {
            StateTool.checkState(id != null, StateTool.State.FAIL);
            City city = cityService.find(id);
            StateTool.checkState(city != null, StateTool.State.FAIL);
            request.getSession().setAttribute(CommonData.CURRENT_CITY, city);
            r.setCityName(city.getCityName());
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }
}
