package com.lhh.vista.web.controller.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.service.model.Ads;
import com.lhh.vista.service.model.Roll;
import com.lhh.vista.service.service.AdsService;
import com.lhh.vista.service.service.RollService;

/**
 * Created by liu on 2016/12/7.
 */

@Controller
@RequestMapping("a_roll")
public class ARollController extends BaseController {
    @Autowired
    private RollService rollService;
    @Autowired
    private AdsService adsService;

    /**
     * 获取所有城市
     *
     * @return
     */
    @RequestMapping("getAll")
    @ResponseBody
    public Object getAll(Integer type) {
        ResponseResult<List<Roll>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            StateTool.checkState(type != null, StateTool.State.FAIL);
            r.setResult(rollService.getBaseAllByType(type));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }
    
    @RequestMapping("getAds")
    @ResponseBody
    public Object getAds() {
        ResponseResult<Ads> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
        	Ads ads = adsService.getAdsAll();
            r.setResult(ads);
            if (ads == null) 
            	r.setState(0);
            else 
            	r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }
    
    @RequestMapping("ac")
    @ResponseBody
    public Object addCount(Integer id) {
        ResponseResult<String> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
        	int updateNum = adsService.addCount(id);
        	if (updateNum != 0) {
        		r.setResult("success");
        	} else {
        		r.setResult("error");        		
        	}
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }
}
