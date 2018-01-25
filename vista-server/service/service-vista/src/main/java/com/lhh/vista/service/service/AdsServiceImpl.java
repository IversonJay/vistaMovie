package com.lhh.vista.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.service.dao.AdsDao;
import com.lhh.vista.service.dao.RollDao;
import com.lhh.vista.service.model.Ads;
import com.lhh.vista.service.model.Roll;

/**
 * Created by liu on 2016/12/7.
 */
@Service
public class AdsServiceImpl implements AdsService {
    @Autowired
    private RollDao rollDao;
    @Autowired
    private AdsDao adsDao;

    @Override
    public Integer save(Ads ads) {
    	if (ads.getIsuse() == 1) {
    		adsDao.resetUse();
    	}
        if (ads.getId() != null) {
            return adsDao.update(ads);
        } else {
        	ads.setCount(0);
            return adsDao.create(ads);
        }
    }

    @Override
    public void del(Integer id) {
        adsDao.del(id);
    }

    @Override
    public Ads find(Integer id) {
        return adsDao.find(id);
    }

    @Override
    public PagerResponse<Ads> getPager(PagerRequest pager,Integer type) {
        return adsDao.getPager(pager);
    }

	@Override
	public Ads getAdsAll() {
		return adsDao.getAdsAll();
	}

	@Override
	public int addCount(Integer id) {
		return adsDao.addCount(id);
	}

}
