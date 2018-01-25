package com.lhh.vista.web.controller.manage;

import com.lhh.vista.common.model.BaseResult;
import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.service.model.Activity;
import com.lhh.vista.service.service.ActivityService;
import com.lhh.vista.web.dto.ActivityWithDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("m_activity")
public class MActivityController extends BaseController {
    @Autowired
    private ActivityService activityService;

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
            r.setResult(getContent("manage/edit_activity.ftl", null));
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
    public ResponseResult<PagerResponse<Activity>> getPager(PagerRequest pager) {
        ResponseResult<PagerResponse<Activity>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(activityService.getPager(pager));
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
    public ResponseResult<Integer> save(ActivityWithDateTime activity) {
        ResponseResult<Integer> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(activityService.save(activity.toActivity()));
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
            activityService.del(id);
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
    public ResponseResult<ActivityWithDateTime> find(Integer id) {
        ResponseResult<ActivityWithDateTime> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            System.out.println(activityService.find(id).getState());
            r.setResult(new ActivityWithDateTime(activityService.find(id)));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }
}
