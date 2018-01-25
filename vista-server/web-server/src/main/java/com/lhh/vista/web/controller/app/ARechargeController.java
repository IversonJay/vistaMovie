package com.lhh.vista.web.controller.app;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.lhh.vista.common.model.BaseResult;
import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.customer.VistaApi;
import com.lhh.vista.customer.v2s.MemberValidateRes;
import com.lhh.vista.service.model.AppUser;
import com.lhh.vista.service.model.AppUserRecharge;
import com.lhh.vista.service.service.AppUserRechargeService;
import com.lhh.vista.service.service.AppUserService;
import com.lhh.vista.temp.model.Concession;
import com.lhh.vista.temp.service.ConcessionService;
import com.lhh.vista.web.common.CommonData;

/**
 * Created by liu on 2017/1/5.
 */
@Controller
@RequestMapping("a_recharge")
public class ARechargeController extends BaseController {
    @Autowired
    private AppUserRechargeService appUserRechargeService;

    @Autowired
    private VistaApi vistaApi;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private ConcessionService concessionService;
    
    @RequestMapping("getChongzhiConcession")
    @ResponseBody
    public Object getChongzhiConcession() {
        ResponseResult<List<Concession>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            
            user = appUserService.getByMemberId(user.getMemberId());
            List<Concession> list = concessionService.selectCZList(user.getDcinema());
//            user = appUserService.getByMemberId(user.getMemberId());
//            GetConcessionListRes res = vistaApi.getOrderApi().getConcessionList(user.getDcinema());
//            List<Concession> list = new ArrayList<>();
//            if (res != null && res.getItems().size() > 0) {
//                for (GetConcessionListRes.Concession con : res.getItems()) {
//                    if (con.getDescription().indexOf("储值") > -1 || con.getDescription().indexOf("充值") > -1) {
//                        list.add(new Concession(con.getId(), con.getDescription(), con.getPrice()));
//                    }
//                }
//            }
//            
////            String orderId = ("member_pin_" + System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "")).substring(0, 32);          
////            MemberValidateRes memeberInfo = vistaApi.getUserApi().checkUser4PIN(user.getMemberId(), orderId);            
        
            r.setResult(list);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }
    
    /**
     * 
     * 充值现金前 ， 去会员服务器和本地服务器判定一下pin码是否存在
     * @return
     */
    @RequestMapping("getChongzhiPin")
    @ResponseBody
    public MemberValidateRes getChongzhiPin() {
        ResponseResult<List<Concession>> r = new ResponseResult<>(StateTool.State.FAIL);
        MemberValidateRes memeberInfo = null;
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            user = appUserService.getByMemberId(user.getMemberId());
            
            String orderId = ("member_pin_" + System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "")).substring(0, 32);          
            memeberInfo = vistaApi.getUserApi().checkUser4PIN(user.getMemberId(), orderId);
            
            //当会员服务器中没有存储pin码 从本地服务器获取
            if (Strings.isNullOrEmpty(memeberInfo.getMember().getPin())) {
            	if (!Strings.isNullOrEmpty(user.getPin())) {
            		memeberInfo.getMember().setPin(user.getPin());
            	} else {
            		memeberInfo.getMember().setPin("");
            	}
            }
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return memeberInfo;
    }
    
    
    @RequestMapping("validatePin")
    @ResponseBody
    public ResponseResult<Integer> validatePin(String pin) {
    	
        ResponseResult<Integer> r = new ResponseResult<>(StateTool.State.FAIL);
        MemberValidateRes memeberInfo = null;
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            user = appUserService.getByMemberId(user.getMemberId());
            
            String orderId = ("member_pin_" + System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "")).substring(0, 32);          
            memeberInfo = vistaApi.getUserApi().checkUser4PIN(user.getMemberId(), orderId);
            
            //当会员服务器中没有存储pin码 从本地服务器获取
            if (Strings.isNullOrEmpty(memeberInfo.getMember().getPin())) {
            	if (!Strings.isNullOrEmpty(user.getPin()) && user.getPin().equals(pin)) {
            		r.setState(1);
            	} else if (!Strings.isNullOrEmpty(user.getPin()) && !user.getPin().equals(pin)){
            		r.setState(0);
            	} else {
            		r.setState(-1);
            	}
            } else {
            	if (memeberInfo.getMember().getPin().equals(pin)) {
            		r.setState(1);
            	} else {
            		r.setState(0);
            	}
            }
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }
    

    @RequestMapping("create")
    @ResponseBody
    public Object createChongzhiOrder(String itemId) {
        ResponseResult<AppUserRecharge> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            AppUserRecharge appUserRecharge = appUserRechargeService.appUserRecharge(itemId, user.getMemberId());
            appUserRecharge.setUserId(null);
            appUserRecharge.setOrderStatus(null);
            r.setResult(appUserRecharge);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }

    @RequestMapping("complete")
    @ResponseBody
    public Object completeOrder(Integer orderId, Integer payType) {
        BaseResult baseResult = new BaseResult();
        baseResult.setState(StateTool.State.FAIL);
//        try {
//            StateTool.checkState(payType == 2 || payType == 3 || payType == 4, StateTool.State.FAIL);
//
//            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
//            StateTool.checkState(user != null, StateTool.State.FAIL);
//
        //          appUserRechargeService.finishUserRecharge(user.getMemberId(), orderId, payType);
//            baseResult.setState(StateTool.State.SUCCESS);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return baseResult;
    }

    @RequestMapping("cancel")
    @ResponseBody
    public Object cancel(Integer orderId) {
        BaseResult baseResult = new BaseResult();
        baseResult.setState(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            appUserRechargeService.cancelOrder(user.getMemberId(), orderId);
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }
}
