package com.lhh.vista.web.controller.manage;

import com.lhh.vista.common.model.BaseResult;
import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.service.model.RewardControl;
import com.lhh.vista.service.service.RewardControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("m_rewardControl")
public class MRewardController extends BaseController {
    @Autowired
    private RewardControlService rewardControlService;

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
            r.setResult(getContent("manage/edit_reward.ftl", null));
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
    public ResponseResult<PagerResponse<RewardControl>> getPager(PagerRequest pager, Integer type) {
        ResponseResult<PagerResponse<RewardControl>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(rewardControlService.getPager(pager, type));
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
    @RequestMapping("create")
    @ResponseBody
    public ResponseResult<Integer> create(RewardControl rewardControl) {
        ResponseResult<Integer> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            rewardControlService.create(rewardControl);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }

    @RequestMapping("update")
    @ResponseBody
    public ResponseResult<Integer> update(RewardControl rewardControl) {
        ResponseResult<Integer> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            rewardControlService.update(rewardControl);
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
    public BaseResult del(Integer type, String rid) {
        BaseResult r = new BaseResult(StateTool.State.FAIL);
        try {
            rewardControlService.del(type, rid);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }

}
