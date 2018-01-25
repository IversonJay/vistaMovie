package com.lhh.vista.service.service;

import com.google.common.base.Strings;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.util.ValidateTool;
import com.lhh.vista.customer.VistaApi;
import com.lhh.vista.customer.v2s.MemberValidateRes;
import com.lhh.vista.customer.v2s.StateResult;
import com.lhh.vista.service.dao.AppUserDao;
import com.lhh.vista.service.dto.AppUserLoginRes;
import com.lhh.vista.service.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Created by soap on 2016/12/16.
 */
@Service
public class AppUserServiceImpl implements AppUserService {
    @Autowired
    private AppUserDao appUserDao;

    @Autowired
    private VistaApi vistaApi;
    
    @Autowired
    private SystemValueService systemValueService;


    @Override
    public AppUser getByMemberId(String id) {
        return appUserDao.getByMemberId(id);
    }

    @Override
    public AppUser getByMphone(String mphone) {
        return appUserDao.getByMphone(mphone);
    }

    @Override
    public AppUserLoginRes login(String mphone, String passWord, String key) {
        AppUserLoginRes appUserLoginRes = new AppUserLoginRes();

        String clubId = systemValueService.getValue(Integer.parseInt(key));
        MemberValidateRes res = vistaApi.getUserApi().validateByClubID(mphone, passWord, clubId, true);
        StateTool.checkState(res != null && res.getResult() == 0 && res.getMember() != null, StateTool.State.FAIL);

        //如果没有会员卡,则创建一个
        if (Strings.isNullOrEmpty(res.getMember().getCardNumber())) {
            Long id = appUserDao.getNextCardId();
            StateTool.checkState(id > 0, StateTool.State.FAIL);
            vistaApi.getUserApi().update(res.getMember().getMemberId(), null, null, mphone);
//            vistaApi.getUserApi().update(res.getMember().getMemberId(), null, null, "WEB" + id);
        }

        AppUser appUser = appUserDao.getByMemberId(res.getMember().getMemberId());
        if (appUser == null) {
            appUser = new AppUser();
            String nickName = "";
            if (Strings.isNullOrEmpty(nickName)) {
                nickName = mphone.substring(0, 3) + "****" + mphone.substring(7);
            }
            appUser.setNickName(nickName);
            String token = System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "");
            if (token.length() > 50) {
                token = token.substring(0, 50);
            }
            appUser.setMemberId(res.getMember().getMemberId());
            appUser.setClubid(res.getMember().getClubID());
            appUser.setMphone(mphone);
            appUser.setToken(token);
            appUserDao.insert(appUser);
        } else {
            String token = System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "");
            if (token.length() > 50) {
                token = token.substring(0, 50);
            }
            appUser.setToken(token);

            AppUser temp = new AppUser();
            temp.setToken(token);
            temp.setMemberId(appUser.getMemberId());
            temp.setClubid(res.getMember().getClubID());
            appUserDao.update(temp);
        }
        appUserLoginRes.setAppUser(appUser);
        appUserLoginRes.setBalance(res.obtainBalance());
        appUserLoginRes.setIntegral(res.obtainIntegral());
        appUserLoginRes.setUserName(res.getMember().getUserName());
        appUserLoginRes.setClubId(res.showClubID());
        appUserLoginRes.setLoyaltyCode(res.getMember().getPreferredComplex());
        return appUserLoginRes;
    }

    @Override
    public AppUserLoginRes loginByToken(String token) {
        AppUserLoginRes appUserLoginRes = new AppUserLoginRes();
        AppUser appUser = appUserDao.getByToken(token);
        
        MemberValidateRes res = vistaApi.getUserApi().checkUser(appUser.getMemberId(), true);

        StateTool.checkState(res.getResult() == 0, StateTool.State.FAIL);

        appUserLoginRes.setAppUser(appUser);
        appUserLoginRes.setBalance(res.obtainBalance());
        appUserLoginRes.setIntegral(res.obtainIntegral());
        appUserLoginRes.setUserName(res.getMember().getUserName());
        return appUserLoginRes;
    }

    @Override
    public void updateBaseInfo(AppUser appUser) {
        appUserDao.update(appUser);
    }

    @Override
    @Transactional
    public void updateMphone(String id, String mphone) {
        AppUser temp = new AppUser();
        temp.setMphone(mphone);
        temp.setMemberId(id);
        appUserDao.update(temp);

        StateResult stateResult = vistaApi.getUserApi().update(id, mphone, null, null);
        StateTool.checkState(stateResult != null && stateResult.getResult() == 0, StateTool.State.FAIL);
    }

	@Override
	public AppUser loginValidateToken(String token) {
		return appUserDao.getByToken(token);
	}
}
