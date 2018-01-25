package com.lhh.vista.service.model;

import com.lhh.vista.common.model.BaseModelI;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/7.
 * 管理员表
 */
@Setter
@Getter
public class Manager extends BaseModelI {
    private String username;
    private String password;
    private Integer role;//1管理员,2其他维护者
    private Integer state;//1正常,-1锁定
    private String lastLoginIp;
    private String lastLoginTime;

    public static final int STATE_LOCK = -1;
    public static final int STATE_NORMAL = 1;

    public static final int ROLE_MANAGE = 1;
    public static final int ROLE_OTHER = 2;
}
