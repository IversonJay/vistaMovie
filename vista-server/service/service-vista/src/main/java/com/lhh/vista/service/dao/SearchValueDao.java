package com.lhh.vista.service.dao;

import com.lhh.vista.common.service.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SearchValueDao extends BaseDao {

    public void create(String name) {
        sqlSession.insert("SearchValue.create", name);
    }

    public void addCount(String name) {
        sqlSession.update("SearchValue.addCount", name);
    }

    public int getCount(String name) {
        return sqlSession.selectOne("SearchValue.getCount", name);
    }

    public List<String> getHots() {
        return sqlSession.selectList("SearchValue.getHots");
    }
}
