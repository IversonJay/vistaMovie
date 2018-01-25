package com.lhh.vista.service.dto;

import com.lhh.vista.service.model.AppUser;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/29.
 */
@Setter
@Getter
public class AppUserLoginRes {
    private AppUser appUser;

    private Float balance;//余额
    private Float integral;//积分
    private String userName;//手机号
    private Integer clubId;

    private Integer loyaltyCode;
}
