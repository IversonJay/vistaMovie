package com.lhh.vista.web.controller.manage;

import com.google.common.base.Strings;
import com.lhh.vista.common.model.BaseResult;
import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.service.model.City;
import com.lhh.vista.service.service.CityService;
import com.lhh.vista.web.common.CommonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("m_city")
public class MCityController extends BaseController {
    @Autowired
    private CityService cityService;

    /**
     * 跳转到用户管理页面
     *
     * @return
     */
    @RequestMapping("toEdit")
    @ResponseBody
    public ResponseResult<String> toPage() {
        ResponseResult<String> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(getContent("manage/edit_city.ftl", null));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        } catch (Exception e) {
            r.setState(StateTool.State.FAIL);
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 获取分页
     *
     * @return
     */
    @RequestMapping("getPager")
    @ResponseBody
    public ResponseResult<PagerResponse<City>> getPager(PagerRequest pager) {
        ResponseResult<PagerResponse<City>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(cityService.getPager(pager));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 保存信息
     *
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResponseResult<Integer> save(City city) {
        ResponseResult<Integer> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(cityService.save(city));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping("del")
    @ResponseBody
    public BaseResult del(Integer id) {
        BaseResult r = new BaseResult(StateTool.State.FAIL);
        try {
            cityService.del(id);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }

    /**
     * 获取单个信息
     *
     * @return
     */
    @RequestMapping("find")
    @ResponseBody
    public ResponseResult<City> find(Integer id) {
        ResponseResult<City> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(cityService.find(id));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }
}
