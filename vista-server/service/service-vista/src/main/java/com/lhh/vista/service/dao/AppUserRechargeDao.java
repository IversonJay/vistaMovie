package com.lhh.vista.service.dao;

import com.lhh.vista.common.service.BaseDao;
import com.lhh.vista.service.model.AppUserOrder;
import com.lhh.vista.service.model.AppUserRecharge;
import org.springframework.stereotype.Repository;

@Repository
public class AppUserRechargeDao extends BaseDao {

    public Integer create(AppUserRecharge appUserRecharge) {
        sqlSession.insert("AppUserRecharge.create", appUserRecharge);
        return appUserRecharge.getId();
    }

    public Integer update(AppUserRecharge appUserRecharge) {
        sqlSession.insert("AppUserRecharge.update", appUserRecharge);
        return appUserRecharge.getId();
    }

    public AppUserRecharge find(Integer id) {
        return sqlSession.selectOne("AppUserRecharge.getById", id);
    }

    public AppUserRecharge find(String orderId) {
        return sqlSession.selectOne("AppUserRecharge.getByOrderId", orderId);
    }
}
