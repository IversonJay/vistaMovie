package com.lhh.vista.service.service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.service.dto.BaseCity;
import com.lhh.vista.service.model.RewardControl;

import java.util.List;

/**
 * Created by liu on 2017/1/4.
 */
public interface RewardControlService {
    List<RewardControl> getRewardIdsByType(Integer type);

    void update(RewardControl rewardControl);

    void create(RewardControl rewardControl);

    void del(Integer type,String rid);

    PagerResponse<RewardControl> getPager(PagerRequest pager, Integer rtype);
}
