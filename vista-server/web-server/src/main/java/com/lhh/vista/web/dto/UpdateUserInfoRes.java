package com.lhh.vista.web.dto;

import com.lhh.vista.common.model.BaseResult;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/25.
 */
@Setter
@Getter
public class UpdateUserInfoRes extends BaseResult {
    private String userHead;
    private String nickName;
    private String mphone;
    private String sex;
    private String qqmark;

    private Float balance;//余额
    private Float integral;//积分
    
    private String memberLevelName;//会员卡名称
    private String cardNumber;
    private Integer clubId;
    
    
    private String dcinema;
    private Integer loyaltyCode;
    
}
