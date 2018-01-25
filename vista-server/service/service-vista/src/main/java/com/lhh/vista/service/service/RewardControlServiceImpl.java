package com.lhh.vista.service.service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.service.dao.RewardControlDao;
import com.lhh.vista.service.model.RewardControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liu on 2017/1/4.
 */
@Service
public class RewardControlServiceImpl implements RewardControlService {

    @Autowired
    private RewardControlDao rewardControlDao;

    @Override
    public List<RewardControl> getRewardIdsByType(Integer type) {
        return rewardControlDao.getRewardIdsByType(type);
    }


    @Override
    public void update(RewardControl rewardControl) {
        StateTool.checkState(rewardControlDao.find(rewardControl.getRtype(), rewardControl.getRid()) != null, -1);
        rewardControlDao.update(rewardControl);
    }

    @Override
    public void create(RewardControl rewardControl) {
        StateTool.checkState(rewardControlDao.find(rewardControl.getRtype(), rewardControl.getRid()) == null, -1);
        rewardControlDao.create(rewardControl);
    }


    @Override
    public void del(Integer type, String rid) {
        rewardControlDao.del(type, rid);
    }

    @Override
    public PagerResponse<RewardControl> getPager(PagerRequest pager, Integer rtype) {
        return rewardControlDao.getPager(pager, rtype);
    }
}
