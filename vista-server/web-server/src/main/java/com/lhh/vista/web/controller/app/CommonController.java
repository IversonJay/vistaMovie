package com.lhh.vista.web.controller.app;

import com.google.common.base.Strings;
import com.lhh.vista.common.model.BaseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.customer.VistaApi;
import com.lhh.vista.customer.v2s.RepeatCheckRes;
import com.lhh.vista.customer.v2s.StateResult;
import com.lhh.vista.service.dto.AppUserLoginRes;
import com.lhh.vista.service.model.AppUser;
import com.lhh.vista.service.model.SystemValue;
import com.lhh.vista.service.service.AppUserService;
import com.lhh.vista.service.service.SystemValueService;
import com.lhh.vista.temp.model.Cinema;
import com.lhh.vista.temp.service.CinemaService;
import com.lhh.vista.web.common.CommonData;
import com.lhh.vista.web.dto.AppConstantRes;
import com.lhh.vista.web.dto.LoginRes;
import com.lhh.vista.web.dto.StringRes;
import com.lhh.vista.web.sms.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by soap on 2016/11/26.
 */
@Controller
@RequestMapping("a_common")
public class CommonController extends BaseController {

    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private SystemValueService systemValueService;
    
    @Autowired
    private CinemaService cinemaService;

    @Autowired
    private VistaApi vistaApi;

    @RequestMapping("reg_do")
    @ResponseBody
    public Object reg_do(String mphone, String password, String code, String key) {
        LoginRes baseResult = new LoginRes();
        try {
            StateTool.checkState(!Strings.isNullOrEmpty(code)
                    && !Strings.isNullOrEmpty(mphone)
                    && !Strings.isNullOrEmpty(password), StateTool.State.FAIL);
            String scode = (String) request.getSession().getAttribute(CommonData.REG_CODE);
            String smphone = (String) request.getSession().getAttribute(CommonData.REG_MPHONE);

            StateTool.checkState(mphone.equals(smphone) && code.equals(scode), StateTool.State.FAIL);

            request.getSession().setAttribute(CommonData.REG_CODE, null);
            request.getSession().setAttribute(CommonData.REG_MPHONE, null);

            StateResult res = vistaApi.getUserApi().create(mphone, password);

            StateTool.checkState(res != null && res.getResult() == 0, StateTool.State.FAIL);

            AppUserLoginRes appUserLoginRes = appUserService.login(mphone, password, key);
            AppUser user = appUserLoginRes.getAppUser();
            StateTool.checkState(user != null, StateTool.State.FAIL);

            request.getSession().setAttribute(CommonData.APP_LOGIN_USER, user);

            baseResult.setToken(user.getToken());
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }

    @RequestMapping("test_code")
    @ResponseBody
    public Object test_code(String mphone, String code) {
        BaseResult baseResult = new BaseResult(StateTool.State.FAIL);
        try {
            StateTool.checkState(!Strings.isNullOrEmpty(code)
                    && !Strings.isNullOrEmpty(mphone), StateTool.State.FAIL);
            String scode = (String) request.getSession().getAttribute(CommonData.REG_CODE);
            String smphone = (String) request.getSession().getAttribute(CommonData.REG_MPHONE);

            StateTool.checkState(mphone.equals(smphone) && code.equals(scode), StateTool.State.FAIL);
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }

    @RequestMapping("reg_send_code")
    @ResponseBody
    public Object reg_send_code(String mphone) {
        BaseResult baseResult = new BaseResult(StateTool.State.FAIL);
        try {
            StateTool.checkState(!Strings.isNullOrEmpty(mphone), StateTool.State.FAIL);
            //这里调用查重接口,看是否已经使用
            RepeatCheckRes res = vistaApi.getUserApi().repeatCheck(mphone);
            StateTool.checkState(res != null && res.getState() == 0 && !res.isUsed(), StateTool.State.FAIL);
            request.getSession().setAttribute(CommonData.REG_CODE, smsUtil.sendSms(mphone));
//            request.getSession().setAttribute(CommonData.REG_CODE, "888888");
            request.getSession().setAttribute(CommonData.REG_MPHONE, mphone);

            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }

    @RequestMapping("login")
    @ResponseBody
    public Object login(String mphone, String pass, String key) {
        LoginRes baseResult = new LoginRes();
        baseResult.setState(StateTool.State.FAIL);
        try {
        	//
        	long a=System.currentTimeMillis();
            AppUserLoginRes appUserLoginRes = appUserService.login(mphone, pass, key);
            System.out.println("\r<br>调用登录接口执行耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
            AppUser user = appUserLoginRes.getAppUser();
            StateTool.checkState(user != null, StateTool.State.FAIL);

            request.getSession().setAttribute(CommonData.APP_LOGIN_USER, user);

            baseResult.setToken(user.getToken());
            baseResult.setNickName(user.getNickName());
            baseResult.setUserHead(user.getUserHead());
            baseResult.setDcinema(user.getDcinema());
            baseResult.setBalance(appUserLoginRes.getBalance());
            baseResult.setIntegral(appUserLoginRes.getIntegral());
            baseResult.setMphone(appUserLoginRes.getUserName());
            baseResult.setClubId(appUserLoginRes.getClubId());
            baseResult.setLoyaltyCode(appUserLoginRes.getLoyaltyCode());
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }
    
    
    
    @RequestMapping("logout")
    @ResponseBody
    public Object logout() {
        request.getSession().removeAttribute(CommonData.APP_LOGIN_USER);
        BaseResult baseResult = new BaseResult(StateTool.State.SUCCESS);
        return baseResult;
    }

    @RequestMapping("loginByToken")
    @ResponseBody
    public LoginRes loginByToken(String token) {
        LoginRes baseResult = new LoginRes();
        baseResult.setState(StateTool.State.FAIL);
        try {
            AppUserLoginRes appUserLoginRes = appUserService.loginByToken(token);
            AppUser user = appUserLoginRes.getAppUser();
            StateTool.checkState(user != null, StateTool.State.FAIL);

            request.getSession().setAttribute(CommonData.APP_LOGIN_USER, appUserLoginRes.getAppUser());

            baseResult.setToken(user.getToken());
            baseResult.setNickName(user.getNickName());
            baseResult.setUserHead(user.getUserHead());
            baseResult.setBalance(appUserLoginRes.getBalance());
            baseResult.setIntegral(appUserLoginRes.getIntegral());
            baseResult.setMphone(appUserLoginRes.getUserName());
            baseResult.setDcinema(user.getDcinema());
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }

    @RequestMapping("getAppConstant")
    @ResponseBody
    public Object getAppConstant() {
        AppConstantRes res = new AppConstantRes();
        res.setState(StateTool.State.FAIL);
        try {
            res.setHbUrl(systemValueService.getValue(SystemValue.VISTA_HAIBAO_PIC_URL));
            res.setFmUrl(systemValueService.getValue(SystemValue.VISTA_FENGMIAN_PIC_URL));
            res.setMpUrl(systemValueService.getValue(SystemValue.VISTA_MAIPIN_PIC_URL));
            res.setTiankuan(systemValueService.getValue(SystemValue.VISTA_TIAOKUAN));
            res.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    @RequestMapping("fp_send_code")
    @ResponseBody
    public Object fp_send_code(String mphone) {
        BaseResult baseResult = new BaseResult(StateTool.State.FAIL);
        try {
            StateTool.checkState(!Strings.isNullOrEmpty(mphone), StateTool.State.FAIL);

            //StateTool.checkState(appUserService.getByMphone(mphone) != null, StateTool.State.FAIL);

            //这里调用查重接口,看是否已经使用
            RepeatCheckRes res = vistaApi.getUserApi().repeatCheck(mphone);
            RepeatCheckRes resZX = vistaApi.getUserApi().repeatCheckZX(mphone);
            StateTool.checkState(((res != null && res.getState() == 0 && res.isUsed()) || (resZX != null && resZX.getState() == 0 && resZX.isUsed())), StateTool.State.FAIL);

            request.getSession().setAttribute(CommonData.USER_FG_SEND_MPHONE, mphone);
            request.getSession().setAttribute(CommonData.USER_FG_SEND_CODE, smsUtil.sendSms(mphone));
//            request.getSession().setAttribute(CommonData.USER_FG_SEND_CODE, "888888");
            request.getSession().setAttribute(CommonData.USER_FG_SEND_CODE_TIME, System.currentTimeMillis());
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }
    
    @RequestMapping("pin_send_code")
    @ResponseBody
    public Object pin_send_code(String mphone) {
    	BaseResult baseResult = new BaseResult(StateTool.State.FAIL);
    	try {
            StateTool.checkState(!Strings.isNullOrEmpty(mphone), StateTool.State.FAIL);

            StateTool.checkState(appUserService.getByMphone(mphone) != null, StateTool.State.FAIL);

            //这里调用查重接口,看是否已经使用
            RepeatCheckRes res = vistaApi.getUserApi().repeatCheck(mphone);
            StateTool.checkState(res != null && res.getState() == 0 && res.isUsed(), StateTool.State.FAIL);

            request.getSession().setAttribute(CommonData.USER_SEND_PIN_PHONE, mphone);
            request.getSession().setAttribute(CommonData.USER_SEND_PIN_CODE, smsUtil.sendSms(mphone));
//            request.getSession().setAttribute(CommonData.USER_SEND_PIN_CODE, "888888");
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    	
    }

    @RequestMapping("fp_test_code")
    @ResponseBody
    public Object fp_test_code(String mphone, String code) {
        BaseResult baseResult = new BaseResult(StateTool.State.FAIL);
        try {
            StateTool.checkState(!Strings.isNullOrEmpty(code)
                    && !Strings.isNullOrEmpty(mphone), StateTool.State.FAIL);
            String scode = (String) request.getSession().getAttribute(CommonData.USER_FG_SEND_CODE);
            String smphone = (String) request.getSession().getAttribute(CommonData.USER_FG_SEND_MPHONE);
            StateTool.checkState(mphone.equals(smphone) && code.equals(scode), StateTool.State.FAIL);
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }

    @RequestMapping("fp_do")
    @ResponseBody
    public Object fp_do(String mphone, String password, String code) {
        BaseResult baseResult = new BaseResult(StateTool.State.FAIL);
        try {
            StateTool.checkState(!Strings.isNullOrEmpty(code)
                    && !Strings.isNullOrEmpty(mphone)
                    && !Strings.isNullOrEmpty(password), StateTool.State.FAIL);
            String scode = (String) request.getSession().getAttribute(CommonData.USER_FG_SEND_CODE);
            String smphone = (String) request.getSession().getAttribute(CommonData.USER_FG_SEND_MPHONE);

            StateTool.checkState(mphone.equals(smphone) && code.equals(scode), StateTool.State.FAIL);

            request.getSession().setAttribute(CommonData.USER_FG_SEND_CODE, null);
            request.getSession().setAttribute(CommonData.USER_FG_SEND_MPHONE, null);
            AppUser appUser = appUserService.getByMphone(mphone);
            StateTool.checkState(appUser != null, StateTool.State.FAIL);
            Cinema cinema = null;
            if (!Strings.isNullOrEmpty(appUser.getDcinema())) {
            	cinema = cinemaService.find(appUser.getDcinema());            	
            }
            String clubId = systemValueService.getValue(SystemValue.VISTA_SERVER_CLUB_ID);
            String clubZxId = systemValueService.getValue(SystemValue.VISTA_SERVER_ZXCLUB_ID);
            StateResult stateResult = vistaApi.getUserApi().update4Pass(appUser.getMemberId(), null, password, null, cinema.getLoyaltyCode(), clubId);
            StateResult zxStateResult = vistaApi.getUserApi().update4Pass(appUser.getMemberId(), null, password, null, cinema.getLoyaltyCode(), clubZxId);
            StateTool.checkState(((stateResult != null && stateResult.getResult() == 0) || (zxStateResult != null && zxStateResult.getResult() == 0)), StateTool.State.FAIL);

            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }


    @RequestMapping("getGuanyuwomen")
    @ResponseBody
    public Object getGuanyuwomen() {
        StringRes res = new StringRes();
        res.setState(StateTool.State.FAIL);
        try {
            request.getSession().setAttribute("rr", "rr");
            res.setR(systemValueService.getValue(SystemValue.VISTA_GUANYUWOMEN));
            res.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
