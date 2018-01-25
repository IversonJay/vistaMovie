package com.lhh.vista.service.service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.service.model.Roll;

import java.util.List;

/**
 * Created by liu on 2016/12/7.
 */

public interface RollService {
    Integer save(Roll roll);

    void del(Integer id);

    Roll find(Integer id);

    PagerResponse<Roll> getPager(PagerRequest pager,Integer type);

    List<Roll> getBaseAllByType(Integer type);
}
