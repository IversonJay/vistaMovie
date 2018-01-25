package com.lhh.vista.service.service;

import com.lhh.vista.service.dto.AppUserLoginRes;
import com.lhh.vista.service.model.AppUser;

import java.util.Map;

/**
 * Created by soap on 2016/12/16.
 */
public interface AppUserService {
    //登录
    AppUserLoginRes login(String mphone, String passWord, String key);

    //登录
    AppUserLoginRes loginByToken(String token);

    //登录
    AppUser loginValidateToken(String token);
    
    void updateBaseInfo(AppUser appUser);

    //登录
    AppUser getByMemberId(String id);

    AppUser getByMphone(String mphone);

    void updateMphone(String id, String mphone);
}
