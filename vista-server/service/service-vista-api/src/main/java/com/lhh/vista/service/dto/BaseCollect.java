package com.lhh.vista.service.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by user on 2017/1/5.
 */
@Setter
@Getter
public class BaseCollect {
    private Integer id;
    private String collectName;
    private String collectType;
    private String collectId;
    private Integer userId;

}
