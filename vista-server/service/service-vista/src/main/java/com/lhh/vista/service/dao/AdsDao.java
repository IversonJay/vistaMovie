package com.lhh.vista.service.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.service.BaseDao;
import com.lhh.vista.service.model.Ads;
import com.lhh.vista.service.model.Roll;

@Repository
public class AdsDao extends BaseDao {

    public PagerResponse<Ads> getPager(PagerRequest pager) {
        return getPagerByCmd("Ads.getAll", pager, null);
    }

    public Ads find(Integer id) {
        return sqlSession.selectOne("Ads.getById", id);
    }

    public Integer create(Ads ads) {
        sqlSession.insert("Ads.create", ads);
        return ads.getId();
    }

    public Integer update(Ads ads) {
        sqlSession.update("Ads.update", ads);
        return ads.getId();
    }

    public void del(Integer id) {
        sqlSession.delete("Ads.del", id);
    }

	public Ads getAdsAll() {
		 return sqlSession.selectOne("Ads.getAdsAll");
	}

	public void resetUse() {
		 sqlSession.update("Ads.resetUse");
	}

	public int addCount(Integer id) {
		return sqlSession.update("Ads.addCount", id);
	}
}
