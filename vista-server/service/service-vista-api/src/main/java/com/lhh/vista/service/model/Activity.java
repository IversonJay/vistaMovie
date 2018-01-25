package com.lhh.vista.service.model;

import com.lhh.vista.common.model.BaseModelI;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/8.
 */
@Setter
@Getter
public class Activity extends BaseModelI {
    private String aname;//活动名称
    private String acover;//活动封面
    private Long stime;//开始时间
    private Long etime;//结束时间
    private String content;//活动内容

    private Integer state;//活动状态

    public static final int STATE_NORMAL = 1;//正常状态
    public static final int STATE_CLOSE = -1;//关闭状态
}
