package com.lhh.vista.service.service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.service.model.Collect;
import com.lhh.vista.temp.dto.BaseMovie;
import com.lhh.vista.temp.model.Cinema;

/**
 * Created by user on 2017/1/5.
 */
public interface CollectService {
    Integer add(String collectId, Integer collectType, String userId);

    void del(String collectId, Integer collectType, String userId);

    Collect find(String collectId, Integer collectType, String userId);

    PagerResponse<BaseMovie> getMoviePager(PagerRequest pager, String userId);

    PagerResponse<Cinema> getCinemaPager(PagerRequest pager, String userId);
}
