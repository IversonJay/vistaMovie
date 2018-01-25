package com.lhh.vista.service.model;

import com.lhh.vista.common.model.BaseModelI;
import com.lhh.vista.common.model.BaseModelL;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by user on 2017/1/5.
 */
@Setter
@Getter
public class Collect extends BaseModelI {
    private Integer collectType;
    private String collectId;
    private String userId;
}
