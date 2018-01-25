package com.lhh.vista.web.controller.app;

import com.lhh.vista.common.model.BaseResult;
import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.service.model.AppUser;
import com.lhh.vista.service.service.CollectService;
import com.lhh.vista.temp.dto.BaseMovie;
import com.lhh.vista.temp.model.Cinema;
import com.lhh.vista.web.common.CommonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("a_collect")
public class ACollectController extends BaseController {
    @Autowired
    private CollectService collectService;

    /**
     * 获取分页
     *
     * @return
     */
    @RequestMapping("getMoviePager")
    @ResponseBody
    public Object getMoviePager(PagerRequest pager) {
        ResponseResult<PagerResponse<BaseMovie>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            r.setResult(collectService.getMoviePager(pager, user.getMemberId()));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }

    @RequestMapping("getCinemaPager")
    @ResponseBody
    public Object getCinemaPager(PagerRequest pager) {
        ResponseResult<PagerResponse<Cinema>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            r.setResult(collectService.getCinemaPager(pager, user.getMemberId()));
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
    @RequestMapping("add")
    @ResponseBody
    public ResponseResult<Integer> add(Integer collectType, String collectId) {
        ResponseResult<Integer> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            StateTool.checkState(collectType == 1 || collectType == 2, StateTool.State.FAIL);
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            r.setResult(collectService.add(collectId, collectType, user.getMemberId()));
            r.setState(StateTool.State.SUCCESS);
        } catch (Exception se) {
            r.setState(StateTool.State.FAIL);
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
    public BaseResult del(Integer collectType, String collectId) {
        BaseResult r = new BaseResult(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            collectService.del(collectId, collectType, user.getMemberId());
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
    @RequestMapping("check")
    @ResponseBody
    public BaseResult check(Integer collectType, String collectId) {
        BaseResult r = new BaseResult(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            if (collectService.find(collectId, collectType, user.getMemberId()) != null) {
                r.setState(1);
            } else {
                r.setState(2);
            }
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }
}
