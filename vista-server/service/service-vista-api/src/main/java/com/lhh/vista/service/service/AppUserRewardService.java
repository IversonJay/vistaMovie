package com.lhh.vista.service.service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.service.model.AppUserReward;

import java.util.List;

/**
 * Created by liu on 2017/1/7.
 */
public interface AppUserRewardService {
    /**
     * 获取用户尚未使用的奖励
     */
    List<String> getAppUserNotUseReward(String userId);
    /**
     * 获取某一个抽奖的抽奖次数
     *
     * @param type
     */
    Integer getAppUserRewardCount(Integer type, String userId);

    /**
     * 尝试增加抽奖次数
     *
     * @param type
     */
    Integer addRewardCount(Integer type, String userId);

    /**
     * 抽奖
     *
     * @param type
     * @param userId
     * @param ids    返回奖品ID
     */
    String reward(Integer type, String userId, List<String> ids);

    /**
     * 兑换
     */
    String exchange(Integer oid, String userId);

    PagerResponse<AppUserReward> getPager(PagerRequest pagerRequest, String memberId);
}
