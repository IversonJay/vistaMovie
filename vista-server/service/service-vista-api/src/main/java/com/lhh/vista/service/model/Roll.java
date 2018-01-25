package com.lhh.vista.service.model;

import com.lhh.vista.common.model.BaseModelI;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/7.
 */
@Setter
@Getter
public class Roll extends BaseModelI {
    private String title;
    private Integer type;
    private String coverPath;
    private String targetId;
    private String targetUrl;

    public static final int TYPE_RYYP = 1;//热映影片
    public static final int TYPE_JJSY = 2;//即将上映
    public static final int TYPE_HD = 3;//活动
    public static final int TYPE_YY = 4;//影院
}
