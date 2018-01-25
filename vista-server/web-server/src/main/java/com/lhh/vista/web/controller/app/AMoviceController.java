package com.lhh.vista.web.controller.app;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.service.model.City;
import com.lhh.vista.service.service.CityService;
import com.lhh.vista.temp.dto.BaseMovie;
import com.lhh.vista.temp.dto.CinemaMovie;
import com.lhh.vista.temp.model.Movie;
import com.lhh.vista.temp.service.MovieService;
import com.lhh.vista.temp.service.SessionService;
import com.lhh.vista.web.common.CommonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by soap on 2016/12/11.
 */

@Controller
@RequestMapping("a_movice")
public class AMoviceController extends BaseController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private SessionService sessionService;

    /**
     * 获取最近电影
     * @param 影院id
     * @return
     */
    @RequestMapping("getRecentBest")
    @ResponseBody
    public Object getRecentBest(PagerRequest request, String cid) {
        ResponseResult<PagerResponse<BaseMovie>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(movieService.getRecentBest(request, cid));
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
    @RequestMapping("getSoonCome")
    @ResponseBody
    public Object getSoonCome(PagerRequest request) {
        ResponseResult<PagerResponse<BaseMovie>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(movieService.getSoonCome(request));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }
    
    /**
     * 获取所有影片  type 0 热映  1 即将上映
     */
    @RequestMapping("getMovieAll")
    @ResponseBody
    public Object getMovieAll(PagerRequest request, String cid, String type) {
        ResponseResult<PagerResponse<BaseMovie>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(movieService.getMovieAll(request, cid, type));
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
    public Object getOne(String merge) {
        ResponseResult<Movie> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            Movie movie = movieService.find(merge);
            if(movie != null) movie.setMerge(merge);
            StateTool.checkState(movie != null, StateTool.State.FAIL);
            movie.setSessionNum(sessionService.getSessionByMid(merge));
            r.setResult(movie);
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
        if (city == null) {
            city = cityService.getLatelyCity(121.6707760000, 31.1506170000);
        }
        System.out.println("!!!!" + city.getCityName());
        return city;
    }
}
