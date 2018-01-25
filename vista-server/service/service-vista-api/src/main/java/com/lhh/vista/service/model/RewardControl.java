package com.lhh.vista.service.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2017/1/4.
 */
@Setter
@Getter
public class RewardControl {
    private String rid;//奖励ID
    private Integer rtype;//奖励类型
    private Integer rchance;//奖励概率
    private String remark;//奖励概率

    public static final int TYPE_DZP = 1;
}
