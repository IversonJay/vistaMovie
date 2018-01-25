package com.lhh.vista.web.controller.manage;

import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.service.model.Manager;
import com.lhh.vista.service.service.SystemService;
import com.lhh.vista.web.common.CommonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by soap on 2016/9/27.
 */
@Controller
@RequestMapping("m_system")
public class MSystemController extends BaseController {
    @Autowired
    private SystemService systemService;

    @RequestMapping("toIndex")
    public String toIndex() {
        Manager user = (Manager) request.getSession().getAttribute(CommonData.LOGIN_USER);
        request.setAttribute("username", user.getUsername());
        return toView("manage/index");
    }

    /**
     * 获取用户分页
     *
     * @return
     */
    @RequestMapping("reloadD")
    @ResponseBody
    public ResponseResult<String> reloadD() {
        ResponseResult<String> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            systemService.initDb();
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 获取用户分页
     *
     * @return
     */
    @RequestMapping("reloadC")
    @ResponseBody
    public ResponseResult<String> reloadC() {
        ResponseResult<String> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            systemService.synVistaData();
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 获取用户分页
     *
     * @return
     */
    @RequestMapping("reloadT")
    @ResponseBody
    public ResponseResult<String> reloadT() {
        ResponseResult<String> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            systemService.synTicketsData();
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }
    
    @RequestMapping("reloadM")
    @ResponseBody
    public ResponseResult<String> reloadM() {
        ResponseResult<String> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            systemService.sysnMovieTypeData();
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }
    
}
