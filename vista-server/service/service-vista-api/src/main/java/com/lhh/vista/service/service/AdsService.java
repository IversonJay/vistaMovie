package com.lhh.vista.service.service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.service.model.Ads;
import com.lhh.vista.service.model.Roll;

import java.util.List;

/**
 * Created by liu on 2016/12/7.
 */

public interface AdsService {
    Integer save(Ads ads);

    void del(Integer id);

    Ads find(Integer id);

    PagerResponse<Ads> getPager(PagerRequest pager,Integer type);

	Ads getAdsAll();

	int addCount(Integer id);
}
