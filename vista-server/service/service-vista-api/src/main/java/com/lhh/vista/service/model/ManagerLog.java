package com.lhh.vista.service.model;

import com.lhh.vista.common.model.BaseModelL;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/7.
 * 管理员日志表,用来记录所有操作日志
 */
@Setter
@Getter
public class ManagerLog extends BaseModelL {
    private String username;
    private String action;
    private String param;
    private String time;
}
