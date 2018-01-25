package com.lhh.vista.web.dto;

import com.lhh.vista.common.model.BaseResult;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/21.
 */
@Setter
@Getter
public class AppConstantRes extends BaseResult {
    private String fmUrl;
    private String hbUrl;
    private String mpUrl;
    private String tiankuan;
}
