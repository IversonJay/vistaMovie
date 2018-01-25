package com.lhh.vista.service.dao;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.service.BaseDao;
import com.lhh.vista.service.dto.BaseActivity;
import com.lhh.vista.service.model.Activity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ActivityDao extends BaseDao {

    public PagerResponse<Activity> getPager(PagerRequest pager) {
        return getPagerByCmd("Activity.getAll", pager, null);
    }

    public PagerResponse<BaseActivity> getBasePager(PagerRequest pager,Map<String,Object> p) {
        return getPagerByCmd("Activity.getBaseAll", pager, p);
    }

    public Activity find(Integer id) {
        return sqlSession.selectOne("Activity.getById", id);
    }

    public Integer create(Activity Activity) {
        sqlSession.insert("Activity.create", Activity);
        return Activity.getId();
    }

    public Integer update(Activity activity) {
        sqlSession.update("Activity.update", activity);
        return activity.getId();
    }

    public void del(Integer id) {
        sqlSession.delete("Activity.del", id);
    }
}
