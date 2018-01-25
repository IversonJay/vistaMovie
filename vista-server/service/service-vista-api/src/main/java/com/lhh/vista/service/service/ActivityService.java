package com.lhh.vista.service.service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.service.dto.BaseActivity;
import com.lhh.vista.service.model.Activity;

/**
 * Created by liu on 2016/12/7.
 */

public interface ActivityService {
    Integer save(Activity activity);

    void del(Integer id);

    Activity find(Integer id);

    PagerResponse<Activity> getPager(PagerRequest pager);

    PagerResponse<BaseActivity> getBasePager(PagerRequest pagerr,int state,long stime);
}
