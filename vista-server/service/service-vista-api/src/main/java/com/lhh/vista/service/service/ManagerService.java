package com.lhh.vista.service.service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.service.model.Manager;

/**
 * Created by liu on 2016/12/7.
 */
public interface ManagerService {
    Integer save(Manager manager);

    void del(Integer id);

    Manager find(Integer id);

    Manager findByUserName(String username);

    void changePwd(Integer id, String oldPwd, String newPwd);

    PagerResponse<Manager> getPager(PagerRequest pager, String username, Integer state);
}
