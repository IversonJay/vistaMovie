package com.lhh.vista.service.service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.service.dao.RollDao;
import com.lhh.vista.service.model.Roll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liu on 2016/12/7.
 */
@Service
public class RollServiceImpl implements RollService {
    @Autowired
    private RollDao rollDao;

    @Override
    public Integer save(Roll roll) {
        if (roll.getId() != null) {
            return rollDao.update(roll);
        } else {
            return rollDao.create(roll);
        }
    }

    @Override
    public void del(Integer id) {
        rollDao.del(id);
    }

    @Override
    public Roll find(Integer id) {
        return rollDao.find(id);
    }

    @Override
    public PagerResponse<Roll> getPager(PagerRequest pager,Integer type) {
        return rollDao.getPager(pager);
    }

    @Override
    public List<Roll> getBaseAllByType(Integer type) {
        return rollDao.getBaseAllByType(type);
    }
}
