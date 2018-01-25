package com.lhh.vista.service.dao;

import com.lhh.vista.common.service.BaseDao;
import com.lhh.vista.service.model.AppUser;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by soap on 2016/12/16.
 */
@Repository
public class AppUserDao extends BaseDao {
    public AppUser getByMemberId(String id) {
        return sqlSession.selectOne("AppUser.getByMemberId", id);
    }

    public AppUser getByMphone(String mphone) {
        return sqlSession.selectOne("AppUser.getByMphone", mphone);
    }

    public AppUser getByToken(String token) {
        return sqlSession.selectOne("AppUser.getByToken", token);
    }

    public void update(AppUser appUser) {
        sqlSession.update("AppUser.update", appUser);
    }

    public void insert(AppUser appUser) {
        sqlSession.insert("AppUser.insert", appUser);
    }

    public Long getNextCardId() {
        Map<String, Long> parameterMap = new HashMap<String, Long>();
        parameterMap.put("tvalue", -1l);
        sqlSession.selectOne("AppUser.GetNextCardId", parameterMap);
        return parameterMap.get("tvalue");
    }
}
