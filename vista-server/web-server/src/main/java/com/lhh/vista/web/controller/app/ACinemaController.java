package com.lhh.vista.web.controller.app;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.service.model.City;
import com.lhh.vista.service.service.CityService;
import com.lhh.vista.temp.dto.BaseCinema;
import com.lhh.vista.temp.dto.CinemaAndMovie;
import com.lhh.vista.temp.dto.CinemaWithSession;
import com.lhh.vista.temp.model.Cinema;
import com.lhh.vista.temp.service.CinemaService;
import com.lhh.vista.web.common.CommonData;
import com.lhh.vista.web.pay.weixin.LocationUtils;

/**
 * Created by soap on 2016/12/11.
 */

@Controller
@RequestMapping("a_cinema")
public class ACinemaController extends BaseController {

    @Autowired
    private CinemaService cinemaService;

    /**
     * 获取所有城市
     *
     * @return
     */
    @RequestMapping("getPager")
    @ResponseBody
    public Object getPager(PagerRequest request, Integer orderBy) {
        ResponseResult<PagerResponse<Cinema>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            City city = getCity();
            r.setResult(cinemaService.getPager(request, city.getEname(), orderBy));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }


    /**
     * 获取所有城市
     *
     * @return
     */
    @RequestMapping("getOne")
    @ResponseBody
    public Object getOne(String id) {
        ResponseResult<CinemaAndMovie> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(cinemaService.findCinemaWithMovie(id));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 获取所有城市影院
     * @author LiuHJ
     * @param 经纬度
     * @return
     */
    @RequestMapping("getBaseAll")
    @ResponseBody
    public Object getBaseAll(String name, Double lng, Double lat, String cname) {
        ResponseResult<List<BaseCinema>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
        	StateTool.checkState(name != null, StateTool.State.FAIL);
            City city = cityService.getCityByName(name);
            request.getSession().setAttribute(CommonData.CURRENT_CITY, city);
        	
//            City city = getCity();
            if (city == null ) {
            	ResponseResult<String> result = new ResponseResult<>(StateTool.State.FAIL);
            	result.setResult("该城市暂无影院");
            	return result;
            }
            //计算距离
            List<BaseCinema> cinemaList = cinemaService.getBaseCinema(city.getEname(), cname);
            for(BaseCinema cinema : cinemaList) {
            	double distance = LocationUtils.getDistance(lng, lat, cinema.getLon(), cinema.getLat()) / 1000 ;
            	BigDecimal disFormate = new BigDecimal(distance);
            	cinema.setDistance(disFormate.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            } 
            
            //排序
            Collections.sort(cinemaList, new Comparator<BaseCinema>() {
            	 
                public int compare(BaseCinema o1, BaseCinema o2) {    
                    // 升序
                    return o1.getDistance().compareTo(o2.getDistance());
                    // 降序
                    // return o2.getDistance().compareTo(o1.getDistance());
                }
            });
            
//            r.setResult(cinemaService.getBaseCinema(city.getEname()));
            r.setResult(cinemaList);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }

    @RequestMapping("getCinemaWithSession")
    @ResponseBody
    public Object getCinemaWithSession(String mid, String date) {
        ResponseResult<List<CinemaWithSession>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            City city = getCity();
            r.setResult(cinemaService.getCinemaWithSession(mid, date, city.getEname()));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }

    @Autowired
    private CityService cityService;

    private City getCity() {
        City city = (City) super.request.getSession().getAttribute(CommonData.CURRENT_CITY);
//        if (city == null) {
//            city = cityService.getLatelyCity(121.6707760000, 31.1506170000);
//            request.getSession().setAttribute(CommonData.CURRENT_CITY, city);
//        }
        return city;
    }
}
