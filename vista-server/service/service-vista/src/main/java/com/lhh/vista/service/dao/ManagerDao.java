package com.lhh.vista.service.dao;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.service.BaseDao;
import com.lhh.vista.service.model.Manager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ManagerDao extends BaseDao {

    public PagerResponse<Manager> getPager(PagerRequest pager, Map<String, Object> param) {
        return getPagerByCmd("Manager.getAll", pager, param);
    }

    public Manager find(Integer id) {
        return sqlSession.selectOne("Manager.getById", id);
    }

    public Manager findByUserName(String username) {
        return sqlSession.selectOne("Manager.getByUserName", username);
    }

    public Integer create(Manager Manager) {
        sqlSession.insert("Manager.create", Manager);
        return Manager.getId();
    }

    public Integer update(Manager manager) {
        sqlSession.update("Manager.update", manager);
        return manager.getId();
    }

    public void del(Integer id) {
        sqlSession.delete("Manager.del", id);
    }

    public List<Manager> getAll() {
        return sqlSession.selectList("Manager.getAll");
    }
}
