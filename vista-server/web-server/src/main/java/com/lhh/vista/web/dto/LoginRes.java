package com.lhh.vista.web.dto;

import com.lhh.vista.common.model.BaseResult;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by soap on 2016/11/26.
 */
@Setter
@Getter
public class LoginRes extends BaseResult {
    private String mphone;
    private String nickName;
    private String userHead;//用户头像
    private String dcinema;
    private String token;

    private Float balance;//余额
    private Float integral;//积分
    
    private Integer clubId;
    private Integer loyaltyCode;
}
