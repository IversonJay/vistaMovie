package com.lhh.vista.service.dao;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.service.BaseDao;
import com.lhh.vista.service.dto.BaseCollect;
import com.lhh.vista.service.model.Collect;
import org.springframework.stereotype.Repository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by user on 2017/1/5.
 */
@Repository
public class CollectDao extends BaseDao {

    public PagerResponse<Collect> getPager(PagerRequest pager, int collectType, String userId) {
        Map<String, Object> p = new HashMap<>();
        p.put("collectType", collectType);
        p.put("userId", userId);
        return getPagerByCmd("Collect.getAll", pager, p);
    }

    public Collect find(String collectId, Integer collectType, String userId) {
        Map<String, Object> p = new HashMap<>();
        p.put("collectId", collectId);
        p.put("collectType", collectType);
        p.put("userId", userId);
        return sqlSession.selectOne("Collect.get", p);
    }

    public Integer create(Collect Collect) {
        sqlSession.insert("Collect.create", Collect);
        return Collect.getId();
    }

    public Integer update(Collect collect) {
        sqlSession.update("Collect.update", collect);
        return collect.getId();
    }

    public void del(String collectId, Integer collectType, String userId) {
        Map<String, Object> p = new HashMap<>();
        p.put("collectId", collectId);
        p.put("collectType", collectType);
        p.put("userId", userId);
        sqlSession.delete("Collect.del", p);
    }

    public List<BaseCollect> getBaseAll() {
        return sqlSession.selectList("Collect.getBaseAll");
    }

}
