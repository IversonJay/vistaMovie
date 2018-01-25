package com.lhh.vista.service.model;

import com.lhh.vista.common.model.BaseModelI;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/7.
 */
@Setter
@Getter
public class Ads extends BaseModelI {
    private String title;
    private String coverPath;
    private Integer isuse;
    private String targetUrl;
    private Integer type;
    private Integer count;
    private Integer id;

    public static final int ISUSE = 1;//已使用
    public static final int UNUSE = 0;//未使用
}
