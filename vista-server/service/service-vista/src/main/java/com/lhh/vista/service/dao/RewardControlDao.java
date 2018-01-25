package com.lhh.vista.service.dao;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.service.BaseDao;
import com.lhh.vista.service.model.RewardControl;
import com.lhh.vista.service.model.Manager;
import com.lhh.vista.service.model.RewardControl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liu on 2017/1/4.
 */
@Repository
public class RewardControlDao extends BaseDao {
    public List<RewardControl> getRewardIdsByType(Integer type) {
        return sqlSession.selectList("RewardControl.getRewardIdsByType", type);
    }

    public PagerResponse<RewardControl> getPager(PagerRequest pager, Integer rtype) {
        Map<String, Object> param = new HashMap<>();
        param.put("rtype", rtype);
        return getPagerByCmd("RewardControl.getAll", pager, rtype);
    }

    public void create(RewardControl rewardControl) {
        sqlSession.insert("RewardControl.create", rewardControl);
    }

    public void update(RewardControl rewardControl) {
        sqlSession.update("RewardControl.update", rewardControl);
    }

    public RewardControl find(Integer type, String rid) {
        Map<String, Object> param = new HashMap<>();
        param.put("rtype", type);
        param.put("rid", rid);
        return sqlSession.selectOne("RewardControl.get", param);
    }

    public void del(Integer type, String rid) {
        Map<String, Object> param = new HashMap<>();
        param.put("rtype", type);
        param.put("rid", rid);
        sqlSession.delete("RewardControl.del", param);
    }
}
