package com.lhh.vista.web.controller.app;

import java.io.File;
import java.io.FileOutputStream;
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
import com.lhh.vista.customer.v2s.GetMemberTransactionRes;
import com.lhh.vista.customer.v2s.MemberValidateRes;
import com.lhh.vista.customer.v2s.RepeatCheckRes;
import com.lhh.vista.service.model.AppUser;
import com.lhh.vista.service.service.AppUserService;
import com.lhh.vista.web.common.CommonData;
import com.lhh.vista.web.dto.UpdateUserInfoRes;
import com.lhh.vista.web.sms.SmsUtil;

import sun.misc.BASE64Decoder;

/**
 * Created by soap on 2016/12/18.
 */
@Controller
@RequestMapping("a_user")
public class AUserController extends BaseController {
    @Autowired
    private SmsUtil smsUtil;
    @Autowired
    private AppUserService appUserService;

    @Autowired
    private VistaApi vistaApi;

    @RequestMapping("getUserInfo")
    @ResponseBody
    public UpdateUserInfoRes getUserInfo() {
        UpdateUserInfoRes baseResult = new UpdateUserInfoRes();
        baseResult.setState(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            user = appUserService.getByMemberId(user.getMemberId());

            MemberValidateRes validateRes = vistaApi.getUserApi().checkUser(user.getMemberId(), true);
            StateTool.checkState(validateRes != null && validateRes.getResult() == 0, StateTool.State.FAIL);

            baseResult.setUserHead(user.getUserHead());
            baseResult.setNickName(user.getNickName());
            baseResult.setMphone(user.getMphone());
            baseResult.setSex(user.getSex());
            baseResult.setDcinema(user.getDcinema());

            baseResult.setIntegral(validateRes.obtainIntegral());
            baseResult.setBalance(validateRes.obtainBalance());
            baseResult.setMemberLevelName(validateRes.showMemberLevelName());
            baseResult.setCardNumber(validateRes.showCardNumber());
            baseResult.setClubId(validateRes.showClubID());
            baseResult.setLoyaltyCode(validateRes.getMember().getPreferredComplex());

            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }

    @RequestMapping("updateUserInfo")
    @ResponseBody
    public UpdateUserInfoRes updateUserInfo(String headData, String nickName, String dcinema, String loyaltyCode, String sex) {
        UpdateUserInfoRes baseResult = new UpdateUserInfoRes();
        baseResult.setState(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            if (!Strings.isNullOrEmpty(nickName)) {
                AppUser temp = new AppUser();
                temp.setMemberId(user.getMemberId());
                temp.setNickName(nickName);

                appUserService.updateBaseInfo(temp);

                baseResult.setNickName(nickName);
            }

            if (!Strings.isNullOrEmpty(dcinema) && !Strings.isNullOrEmpty(loyaltyCode)) {
                AppUser temp = new AppUser();
                temp.setDcinema(dcinema);
                temp.setMemberId(user.getMemberId());
                
                //更新会员服务器上的影城信息
                vistaApi.getUserApi().update4Cinema(user.getMemberId(), user.getClubid(), Integer.parseInt(loyaltyCode));

                appUserService.updateBaseInfo(temp);
            }
            if (!Strings.isNullOrEmpty(sex)) {
                AppUser temp = new AppUser();
                temp.setSex(sex);
                temp.setMemberId(user.getMemberId());

                appUserService.updateBaseInfo(temp);
            }
            if (!Strings.isNullOrEmpty(headData)) {
                String fileName = UUID.randomUUID().toString().replace("-", "");

                String saveRealPath = System.getProperty("vistaapp-web.root") + CommonData.HEAD_FILE_PATH;
                File uploadDir = new File(saveRealPath);
                uploadDir.mkdirs();
                saveRealPath = saveRealPath + fileName + ".png";
                String savePath = CommonData.HEAD_FILE_PATH + fileName + ".png";
                generateImage(headData.substring(headData.indexOf(",") + 1), saveRealPath);

                AppUser temp = new AppUser();
                temp.setMemberId(user.getMemberId());
                temp.setUserHead(savePath);

                appUserService.updateBaseInfo(temp);

                baseResult.setUserHead(savePath);
            }

            System.out.println(user);
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }

    @RequestMapping("cm_send_code1")
    @ResponseBody
    public Object cm_send_code1() {
        BaseResult baseResult = new BaseResult(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            user = appUserService.getByMemberId(user.getMemberId());
            request.getSession().setAttribute(CommonData.USER_SEND_CODE, smsUtil.sendSms(user.getMphone()));
            request.getSession().setAttribute(CommonData.USER_SEND_CODE_TIME, System.currentTimeMillis());
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }

    @RequestMapping("cm_test_code1")
    @ResponseBody
    public Object cm_test_code1(String code) {
        BaseResult baseResult = new BaseResult(StateTool.State.FAIL);
        try {
            StateTool.checkState(!Strings.isNullOrEmpty(code), StateTool.State.FAIL);
            String scode = (String) request.getSession().getAttribute(CommonData.USER_SEND_CODE);
            StateTool.checkState(code.equals(scode), StateTool.State.FAIL);
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }

    @RequestMapping("cm_send_code2")
    @ResponseBody
    public Object cm_send_code2(String mphone) {
        BaseResult baseResult = new BaseResult(StateTool.State.FAIL);
        try {
            StateTool.checkState(!Strings.isNullOrEmpty(mphone), StateTool.State.FAIL);
            StateTool.checkState(appUserService.getByMphone(mphone) == null, StateTool.State.FAIL);
            //这里调用查重接口,看是否已经使用
            RepeatCheckRes res = vistaApi.getUserApi().repeatCheck(mphone);
            StateTool.checkState(res != null && res.getState() == 0 && !res.isUsed(), StateTool.State.FAIL);
            StateTool.checkState(appUserService.getByMphone(mphone) == null, StateTool.State.FAIL);


            request.getSession().setAttribute(CommonData.USER_SEND_MPHONE2, mphone);
            request.getSession().setAttribute(CommonData.USER_SEND_CODE2, smsUtil.sendSms(mphone));
            request.getSession().setAttribute(CommonData.USER_SEND_CODE_TIME2, System.currentTimeMillis());
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }

    @RequestMapping("cm_test_code2")
    @ResponseBody
    public Object cm_test_code2(String mphone, String code) {
        BaseResult baseResult = new BaseResult(StateTool.State.FAIL);
        try {
            StateTool.checkState(!Strings.isNullOrEmpty(code)
                    && !Strings.isNullOrEmpty(mphone), StateTool.State.FAIL);
            String scode = (String) request.getSession().getAttribute(CommonData.USER_SEND_CODE2);
            String smphone = (String) request.getSession().getAttribute(CommonData.USER_SEND_MPHONE2);
            StateTool.checkState(mphone.equals(smphone) && code.equals(scode), StateTool.State.FAIL);
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }

    @RequestMapping("cm_do")
    @ResponseBody
    public Object cm_do(String mphone, String code1, String code2) {
        BaseResult baseResult = new BaseResult(StateTool.State.FAIL);
        try {
            StateTool.checkState(!Strings.isNullOrEmpty(code1)
                    && !Strings.isNullOrEmpty(code2)
                    && !Strings.isNullOrEmpty(mphone), StateTool.State.FAIL);
            String scode1 = (String) request.getSession().getAttribute(CommonData.USER_SEND_CODE);
            String scode2 = (String) request.getSession().getAttribute(CommonData.USER_SEND_CODE2);
            String smphone2 = (String) request.getSession().getAttribute(CommonData.USER_SEND_MPHONE2);

            StateTool.checkState(mphone.equals(smphone2) && code1.equals(scode1) && code2.equals(scode2), StateTool.State.FAIL);

            request.getSession().setAttribute(CommonData.USER_SEND_CODE, null);
            request.getSession().setAttribute(CommonData.USER_SEND_CODE2, null);
            request.getSession().setAttribute(CommonData.USER_SEND_CODE_TIME, null);
            request.getSession().setAttribute(CommonData.USER_SEND_CODE_TIME2, null);
            request.getSession().setAttribute(CommonData.USER_SEND_MPHONE2, null);

            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            appUserService.updateMphone(user.getMemberId(), mphone);

            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }

    @RequestMapping("changePass")
    @ResponseBody
    public Object changePass(String oldPass, String newPass) {
        BaseResult baseResult = new BaseResult(StateTool.State.FAIL);
        try {
            StateTool.checkState(!Strings.isNullOrEmpty(oldPass)
                    && !Strings.isNullOrEmpty(newPass), StateTool.State.FAIL);

            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            AppUser appUser = appUserService.getByMemberId(user.getMemberId());
            MemberValidateRes res = vistaApi.getUserApi().validate(appUser.getMphone(), oldPass, false);
            StateTool.checkState(res != null && res.getResult() == 0, StateTool.State.FAIL);
            vistaApi.getUserApi().update(user.getMemberId(), null, newPass, null);

            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }
    
    
    @RequestMapping("createPin")
    @ResponseBody
    public Object createPin(String pin, String mphone, String code) {
        BaseResult baseResult = new BaseResult(StateTool.State.FAIL);
        try {
            StateTool.checkState(!Strings.isNullOrEmpty(pin), StateTool.State.FAIL);

        	
        	StateTool.checkState(!Strings.isNullOrEmpty(code)
                    && !Strings.isNullOrEmpty(mphone), StateTool.State.FAIL);
            String scode = (String) request.getSession().getAttribute(CommonData.USER_SEND_PIN_CODE);
            String smphone = (String) request.getSession().getAttribute(CommonData.USER_SEND_PIN_PHONE);
            StateTool.checkState(mphone.equals(smphone) && code.equals(scode), StateTool.State.FAIL);
            
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            AppUser appUser = appUserService.getByMemberId(user.getMemberId());
            appUser.setPin(pin);
            appUserService.updateBaseInfo(appUser);
            
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }


    public static boolean generateImage(String imgStr, String imgFilePath) {// 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] decodedBytes = decoder.decodeBuffer(imgStr); // 将字符串格式的image转为二进制流（biye[])的decodedBytes
            File mimg = new File(imgFilePath);
            mimg.deleteOnExit();
            mimg.createNewFile();
            FileOutputStream out = new FileOutputStream(mimg); // 新建一个文件输出器，并为它指定输出位置imgFilePath
            out.write(decodedBytes); // 利用文件输出器将二进制格式decodedBytes输出
            out.close(); // 关闭文件输出器
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @RequestMapping("getYueList")
    @ResponseBody
    public Object getYueList() {
        ResponseResult baseResult = new ResponseResult(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            GetMemberTransactionRes res = vistaApi.getUserApi().memberTransactions(user.getMemberId());
            StateTool.checkState(res != null && res.getResult() == 0, StateTool.State.FAIL);
            baseResult.setResult(res.obtainYueList());
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }

    @RequestMapping("getJinfenList")
    @ResponseBody
    public Object getJinfenList() {
        ResponseResult baseResult = new ResponseResult(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            GetMemberTransactionRes res = vistaApi.getUserApi().memberTransactions(user.getMemberId());
            StateTool.checkState(res != null && res.getResult() == 0, StateTool.State.FAIL);
            baseResult.setResult(res.obtainJifenList());
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }

}