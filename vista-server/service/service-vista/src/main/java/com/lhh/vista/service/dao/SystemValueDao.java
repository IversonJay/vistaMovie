package com.lhh.vista.service.dao;

import com.lhh.vista.common.service.BaseDao;
import com.lhh.vista.service.model.SystemValue;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by soap on 2016/12/18.
 */
@Repository
public class SystemValueDao extends BaseDao {

    private void create(SystemValue sv) {
        sqlSession.insert("SystemValue.create", sv);
    }

    private void update(SystemValue sv) {
        sqlSession.update("SystemValue.update", sv);
    }

    private SystemValue getByKey(Integer key) {
        return sqlSession.selectOne("SystemValue.getByKey", key);
    }


    public void setValue(int key, String value) {
        SystemValue sv = getByKey(key);
        if (sv != null) {
            sv.setValue(value);
            update(sv);
        } else {
            sv = new SystemValue();
            sv.setKey(key);
            sv.setValue(value);
            create(sv);
        }
    }

    public String getValue(int key) {
        SystemValue sv = getByKey(key);
        if (sv == null) {
            if (key == SystemValue.VISTA_HAIBAO_PIC_URL) {
                return "";
            }
            if (key == SystemValue.VISTA_ACTIVITY_DAZHGUYANPAN) {
                return "999999";
            }
            //这里应该加载默认值
            return null;
        }
        return sv.getValue();
    }

    public List<SystemValue> getAll() {
        return sqlSession.selectList("SystemValue.getAll");
    }
}
