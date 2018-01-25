package com.lhh.vista.web.controller.app;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.customer.VistaApi;
import com.lhh.vista.customer.s2v.RewardWarp;
import com.lhh.vista.customer.v2s.GetMemberItemRes;
import com.lhh.vista.customer.v2s.MemberValidateRes;
import com.lhh.vista.customer.v2s.StateResult;
import com.lhh.vista.customer.v2s.value.RewardRes;
import com.lhh.vista.service.dto.BaseActivity;
import com.lhh.vista.service.model.*;
import com.lhh.vista.service.service.ActivityService;
import com.lhh.vista.service.service.AppUserRewardService;
import com.lhh.vista.service.service.RewardControlService;
import com.lhh.vista.service.service.SystemValueService;
import com.lhh.vista.temp.dto.BaseMovie;
import com.lhh.vista.util.PrizeUtil;
import com.lhh.vista.web.common.CommonData;
import com.lhh.vista.web.dto.ActivityWithDateTime;
import com.lhh.vista.web.dto.AppConstantRes;
import com.lhh.vista.web.dto.RotateItem;
import com.lhh.vista.web.dto.StringRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by liu on 2016/12/7.
 */

@Controller
@RequestMapping("a_reward")
public class ARewardController extends BaseController {
    @Autowired
    private VistaApi vistaApi;

    @Autowired
    private RewardControlService rewardControlService;

    @Autowired
    private AppUserRewardService appUserRewardService;

    @Autowired
    private SystemValueService systemValueService;
    
    
    @RequestMapping("list")
    @ResponseBody
    public Object getRewardList() {
    	AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
    	String sessionId = UUID.randomUUID().toString().replace("-", "").substring(0, 32);  
    	MemberValidateRes memberValidateRes = vistaApi.getUserApi().rewardValidate(user.getMemberId(), true, sessionId);
    	RewardWarp rewardWarp = new RewardWarp();
    	rewardWarp.setGetAdvanceBookings(true);
    	rewardWarp.setGetAdvanceSeatingRecognitions(true);
    	rewardWarp.setGetConcessions(true);
    	rewardWarp.setGetDiscounts(true);
    	rewardWarp.setGetTicketTypes(true);
    	rewardWarp.setSupressSelectedSessionDateTimeFilter(true);
    	rewardWarp.setUserSessionId(sessionId);
    	rewardWarp.setOptionalClientName(vistaApi.getClientName());
    	rewardWarp.setOptionalClientId(vistaApi.getClientId());
    	rewardWarp.setOptionalClientClass(vistaApi.getClientClass());
    	String res = vistaApi.post(vistaApi.getServerUser() + "/WSVistaWebClient/RESTLoyalty.svc/member/items", rewardWarp);
		System.out.println(res);
		RewardRes r = null;
		try {
			r = vistaApi.getJsonUtil().readValue(res, RewardRes.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
    }

    /**
     * 获取所有城市
     *
     * @return
     */
    @RequestMapping("getPager")
    @ResponseBody
    public Object getPager(PagerRequest pagerRequest) {
        ResponseResult<PagerResponse<AppUserReward>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            r.setResult(appUserRewardService.getPager(pagerRequest, user.getMemberId()));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 获取单个信息
     *
     * @return
     */
    @RequestMapping("getRotateItem")
    @ResponseBody
    public Object getRotateItem() {
        ResponseResult<List<RotateItem>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);

            List<RotateItem> rotateItems = new ArrayList<>();
            GetMemberItemRes getMemberItemRes = vistaApi.getUserApi().memberItem(user.getMemberId(), true);
            StateTool.checkState(getMemberItemRes != null, StateTool.State.FAIL);
            List<String> used = appUserRewardService.getAppUserNotUseReward(user.getMemberId());
            List<GetMemberItemRes.Item> memberItems = getMemberItemRes.toList(used);

            List<RewardControl> rcs = rewardControlService.getRewardIdsByType(RewardControl.TYPE_DZP);
            List<String> xx = new ArrayList<>();
            for (RewardControl rc : rcs) {
                xx.add(rc.getRid());
            }
            rotateItems.add(new RotateItem("感谢参与", "-9999"));
            for (GetMemberItemRes.Item item : memberItems) {
                if (rotateItems.size() > 8) {
                    break;
                }
                if (rotateItems.size() == 4) {
                    rotateItems.add(new RotateItem("感谢参与", "-9999"));
                }
                if (xx.contains(item.getRecognitionID())) {
                    rotateItems.add(new RotateItem(item.getName(), item.getRecognitionID()));
                }
            }
            if (rotateItems.size() < 8) {
                int c = 8 - rotateItems.size();
                for (int i = 0; i < c; i++) {
                    rotateItems.add(new RotateItem("感谢参与", "-9999"));
                }
            }
            r.setResult(rotateItems);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }

    @RequestMapping("getRotateCount")
    @ResponseBody
    public Object getRotateCount() {
        ResponseResult<Integer> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            r.setResult(appUserRewardService.getAppUserRewardCount(AppUserReward.TYPE_DZP, user.getMemberId()));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }

    @RequestMapping("getRotateGuize")
    @ResponseBody
    public Object getRotateGuize() {
        StringRes res = new StringRes();
        res.setState(StateTool.State.FAIL);
        try {
            res.setR(systemValueService.getValue(SystemValue.VISTA_ACTIVITY_DAZHGUYANPAN_GUIZE));
            res.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @RequestMapping("rotateAddCount")
    @ResponseBody
    public Object rotateAddCount() {
        ResponseResult<Integer> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            appUserRewardService.addRewardCount(AppUserReward.TYPE_DZP, user.getMemberId());
            r.setResult(appUserRewardService.getAppUserRewardCount(AppUserReward.TYPE_DZP, user.getMemberId()));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }

    @RequestMapping("getRotateResult")
    @ResponseBody
    public Object getRotateResult(@RequestBody List<String> ids) {
        ResponseResult<String> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);

            r.setResult(appUserRewardService.reward(AppUserReward.TYPE_DZP, user.getMemberId(), ids));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }

    @RequestMapping("exchange")
    @ResponseBody
    public Object exchange(Integer id) {
        ResponseResult<String> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            r.setResult(  appUserRewardService.exchange(id, user.getMemberId()));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }
}
