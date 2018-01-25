package com.lhh.vista.web.controller.app;

import com.google.common.base.Strings;
import com.lhh.vista.common.model.BaseResult;
import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.service.service.SearchValueService;
import com.lhh.vista.temp.dto.BaseMovie;
import com.lhh.vista.temp.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by liu on 2016/12/7.
 */

@Controller
@RequestMapping("a_search")
public class ASearchController extends BaseController {
    @Autowired
    private SearchValueService searchValueService;
    @Autowired
    private MovieService movieService;

    /**
     * 获取所有城市
     *
     * @return
     */
    @RequestMapping("getHots")
    @ResponseBody
    public Object getHots() {
        ResponseResult<List<String>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(searchValueService.getHots());
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }

    @RequestMapping("add")
    @ResponseBody
    public Object add(String s) {
        BaseResult r = new BaseResult(StateTool.State.FAIL);
        try {
            StateTool.checkState(!Strings.isNullOrEmpty(s), StateTool.State.FAIL);
            searchValueService.addSearchValue(s);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }

    @RequestMapping("s")
    @ResponseBody
    public Object s(String s) {
        ResponseResult<List<BaseMovie>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(movieService.search(s));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }
}
