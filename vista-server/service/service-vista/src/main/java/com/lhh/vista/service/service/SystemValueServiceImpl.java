package com.lhh.vista.service.service;

import com.google.common.base.Strings;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.customer.VistaApi;
import com.lhh.vista.service.dao.SystemValueDao;
import com.lhh.vista.service.model.SystemValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by soap on 2016/12/18.
 */
@Service
public class SystemValueServiceImpl implements SystemValueService {
    @Autowired
    private SystemValueDao systemValueDao;
    @Autowired
    private VistaApi vistaApi;

    @Override
    @Transactional
    public void setValue(int key, String value) {
        systemValueDao.setValue(key, value);
        if (!Strings.isNullOrEmpty(value)) {
            switch (key) {
                case SystemValue.VISTA_SERVER_TOKEN:
                    vistaApi.setToken(value);
                    break;
                case SystemValue.VISTA_SERVER_URL:
                    vistaApi.setServer(value);
                    break;
                case SystemValue.VISTA_SERVER_CLIENT_CLASS:
                    vistaApi.setClientClass(value);
                    break;
                case SystemValue.VISTA_SERVER_CLIENT_ID:
                    vistaApi.setClientId(value);
                    break;
                case SystemValue.VISTA_SERVER_CLIENT_NAME:
                    vistaApi.setClientName(value);
                    break;
                case SystemValue.VISTA_SERVER_CLUB_ID:
                    int id = -1;
                    try {
                        id = Integer.parseInt(value);
                    } catch (Exception e) {

                    }
                    StateTool.checkState(id != -1, StateTool.State.FAIL);
                    vistaApi.setClubID(id);
                    break;
                case SystemValue.VISTA_SERVER_ZXCLUB_ID:
                    int zxid = -1;
                    try {
                    	zxid = Integer.parseInt(value);
                    } catch (Exception e) {

                    }
                    StateTool.checkState(zxid != -1, StateTool.State.FAIL);
                    vistaApi.setClubZXID(zxid);
                    break;
            }
        }
    }

    @Override
    public String getValue(int key) {
        return systemValueDao.getValue(key);
    }

    @Override
    public List<SystemValue> getList() {
        return systemValueDao.getAll();
    }
}
