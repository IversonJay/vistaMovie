package com.lhh.vista.web.controller.manage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.lhh.vista.common.model.BaseResult;
import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.service.model.Manager;
import com.lhh.vista.service.service.ManagerService;
import com.lhh.vista.web.common.CommonData;

@Controller
@RequestMapping("m_manager")
public class MManagerController extends BaseController {
    @Autowired
    private ManagerService managerService;

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
            r.setResult(getContent("manage/edit_manager.ftl", null));
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
     * 跳转到用户修改密码界面
     *
     * @return
     */
    @RequestMapping("toChangePwd")
    @ResponseBody
    public ResponseResult<String> toChangePwd() {
        ResponseResult<String> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(getContent("manage/edit_changePwd.ftl", null));
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
     * 获取用户分页
     *
     * @return
     */
    @RequestMapping("getPager")
    @ResponseBody
    public ResponseResult<PagerResponse<Manager>> getPager(PagerRequest pager, String username, Integer locking) {
        ResponseResult<PagerResponse<Manager>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            if (Strings.isNullOrEmpty(username)) {
                username = null;
            }
            r.setResult(managerService.getPager(pager, username, locking));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 保存用户信息
     *
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResponseResult<Integer> save(Manager manageUsers) {
        ResponseResult<Integer> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(managerService.save(manageUsers));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }

    /**
     * 修改密码
     *
     * @return
     */
    @RequestMapping("changePwd")
    @ResponseBody
    public ResponseResult<Long> changePwd(String oldpwd, String password1, String password2) {
        ResponseResult<Long> r = new ResponseResult<>(StateTool.State.FAIL);
        Manager sysUser = (Manager) request.getSession().getAttribute(CommonData.LOGIN_USER);
        try {
            StateTool.checkState(!Strings.isNullOrEmpty(password1) && !Strings.isNullOrEmpty(password2) && password1.equals(password2), StateTool.State.FAIL);
            StateTool.checkState(sysUser != null, StateTool.State.FAIL);
            managerService.changePwd(sysUser.getId(), oldpwd, password1);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }

    /**
     * 删除用户信息
     *
     * @return
     */
    @RequestMapping("del")
    @ResponseBody
    public BaseResult del(Integer id) {
        BaseResult r = new BaseResult(StateTool.State.FAIL);
        try {
            managerService.del(id);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }

    /**
     * 获取单个用户信息
     *
     * @return
     */
    @RequestMapping("find")
    @ResponseBody
    public ResponseResult<Manager> find(Integer id) {
        ResponseResult<Manager> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(managerService.find(id));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }

    /**
     * 用户退出登录
     *
     * @return
     */
    @RequestMapping("quitLogin")
    public String quitLogin() {
        Manager sysUser = (Manager) request.getSession().getAttribute(CommonData.LOGIN_USER);
        if (sysUser != null) {
            request.getSession().removeAttribute(CommonData.LOGIN_USER);
        }
        return toView("m_common/toLogin", Skip.REDIRECT);
    }
}
