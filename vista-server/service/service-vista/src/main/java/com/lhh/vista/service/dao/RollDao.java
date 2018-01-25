package com.lhh.vista.service.dao;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.service.BaseDao;
import com.lhh.vista.service.model.Roll;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RollDao extends BaseDao {

    public PagerResponse<Roll> getPager(PagerRequest pager) {
        return getPagerByCmd("Roll.getAll", pager, null);
    }

    public Roll find(Integer id) {
        return sqlSession.selectOne("Roll.getById", id);
    }

    public Integer create(Roll Roll) {
        sqlSession.insert("Roll.create", Roll);
        return Roll.getId();
    }

    public Integer update(Roll roll) {
        sqlSession.update("Roll.update", roll);
        return roll.getId();
    }

    public void del(Integer id) {
        sqlSession.delete("Roll.del", id);
    }

    public List<Roll> getBaseAllByType(Integer type) {
        return sqlSession.selectList("Roll.getBaseAllByType",type);
    }
}
