package com.lhh.vista.service.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/7.
 * APP用户表
 */
@Setter
@Getter
public class AppUser {
    private String memberId;
    private String mphone;//用户昵称
    private String nickName;//用户昵称
    private String userHead;//用户头像
    private String token;//用户token
    private String sex;//用户性别
    private Integer clubid;

    private String dcinema;//用户默认影院

    private String qqmark;//qq标记
    private String pin;
}
