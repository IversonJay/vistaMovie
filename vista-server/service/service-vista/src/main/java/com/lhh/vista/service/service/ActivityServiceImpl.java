package com.lhh.vista.service.service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.service.dao.ActivityDao;
import com.lhh.vista.service.dto.BaseActivity;
import com.lhh.vista.service.model.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liu on 2016/12/7.
 */
@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityDao activityDao;

    @Override
    public Integer save(Activity activity) {
        if (activity.getId() != null) {
            return activityDao.update(activity);
        } else {
            return activityDao.create(activity);
        }
    }

    @Override
    public void del(Integer id) {
        activityDao.del(id);
    }

    @Override
    public Activity find(Integer id) {
        return activityDao.find(id);
    }

    @Override
    public PagerResponse<Activity> getPager(PagerRequest pager) {
        return activityDao.getPager(pager);
    }

    @Override
    public PagerResponse<BaseActivity> getBasePager(PagerRequest pager,int state,long stime) {
        Map<String,Object> param=new HashMap<>();
        param.put("state",state);
        param.put("stime",stime);
        return activityDao.getBasePager(pager,param);
    }
}
