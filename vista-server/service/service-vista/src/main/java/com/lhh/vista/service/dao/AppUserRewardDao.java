package com.lhh.vista.service.dao;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.service.BaseDao;
import com.lhh.vista.service.model.AppUserReward;
import com.lhh.vista.service.model.Roll;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liu on 2017/1/7.
 */
@Repository
public class AppUserRewardDao extends BaseDao {
    public Integer getAppUserRewardCount(Integer rtype, String userId) {
        Map<String, Object> p = new HashMap<>();
        p.put("rtype", rtype);
        p.put("userId", userId);
        return sqlSession.selectOne("AppUserReward.getAppUserRewardCount", p);
    }

    public Integer create(AppUserReward AppUserReward) {
        sqlSession.insert("AppUserReward.create", AppUserReward);
        return AppUserReward.getId();
    }

    public Integer update(AppUserReward appUserReward) {
        sqlSession.update("AppUserReward.update", appUserReward);
        return appUserReward.getId();
    }

    public AppUserReward find(Integer orderId) {
        return sqlSession.selectOne("AppUserReward.getById", orderId);
    }


    public AppUserReward getAppUserRewardToReward(Integer rtype, String userId) {
        Map<String, Object> p = new HashMap<>();
        p.put("rtype", rtype);
        p.put("userId", userId);
        return sqlSession.selectOne("AppUserReward.getAppUserRewardToReward", p);
    }

    public PagerResponse<AppUserReward> getPager(PagerRequest pager, String userId) {
        Map<String, Object> p = new HashMap<>();
        p.put("userId", userId);
        return getPagerByCmd("AppUserReward.getAll", pager, p);
    }


    public List<String> getNotUse(String userId) {
        Map<String, Object> p = new HashMap<>();
        p.put("userId", userId);
        return sqlSession.selectList("AppUserReward.getNotUse", p);
    }
}
