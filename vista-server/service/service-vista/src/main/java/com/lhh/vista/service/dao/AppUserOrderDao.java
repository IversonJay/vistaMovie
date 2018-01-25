package com.lhh.vista.service.dao;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.service.BaseDao;
import com.lhh.vista.service.dto.BaseUserOrder;
import com.lhh.vista.service.model.AppUserOrder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AppUserOrderDao extends BaseDao {

    public PagerResponse<BaseUserOrder> getPager(PagerRequest pager, Map<String, Object> param) {
        return getPagerByCmd("AppUserOrder.getAll", pager, param);
    }

    public AppUserOrder find(Integer id) {
        return sqlSession.selectOne("AppUserOrder.getById", id);
    }

    public AppUserOrder find(String orderId) {
        return sqlSession.selectOne("AppUserOrder.getByOrderId", orderId);
    }

    public Integer create(AppUserOrder appUserOrder) {
        sqlSession.insert("AppUserOrder.create", appUserOrder);
        return appUserOrder.getId();
    }

    public Integer update(AppUserOrder appUserOrder) {
        sqlSession.update("AppUserOrder.update", appUserOrder);
        return appUserOrder.getId();
    }

    public void del(Integer id) {
        sqlSession.delete("AppUserOrder.del", id);
    }

	public void update(Map<String, Object> map) {
		sqlSession.update("AppUserOrder.updateByMap", map);
	}

	public List<AppUserOrder> selectOrderByStime(String queryTime) {
		return sqlSession.selectList("AppUserOrder.selectOrderByStime", queryTime);
	}
}
