package com.lhh.vista.service.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/7.
 * 基础活动信息表
 */
@Setter
@Getter
public class BaseActivity {
    private Integer id;
    private String aname;//活动名称
    private String acover;//活动封面
    private Long etime;//结束时间
}
