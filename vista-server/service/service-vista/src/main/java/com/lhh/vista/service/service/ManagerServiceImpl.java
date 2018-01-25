package com.lhh.vista.service.service;

import com.google.common.base.Strings;
import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.service.dao.ManagerDao;
import com.lhh.vista.service.model.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ManagerServiceImpl implements ManagerService {
    @Autowired
    private ManagerDao managerDao;
    /**
     * 密码加密
     */
    @Autowired
    private PasswordEncoder passwordEncoder;


    public PagerResponse<Manager> getPager(PagerRequest pager, String username, Integer locking) {
        Map<String, Object> param = new HashMap<>();
        return managerDao.getPager(pager, param);
    }

    public Manager find(Integer id) {
        Manager manager= managerDao.find(id);
        manager.setPassword(null);
        return manager;
    }

    @Override
    public Manager findByUserName(String username) {
        return managerDao.findByUserName(username);
    }

    public void changePwd(Integer id, String oldPwd, String newPwd) {
        Manager manager = managerDao.find(id);
        StateTool.checkState(manager != null, StateTool.State.FAIL);
        StateTool.checkState(passwordEncoder.matches(oldPwd, manager.getPassword()), StateTool.State.FAIL);
        manager.setPassword(passwordEncoder.encode(newPwd));
        managerDao.update(manager);
    }

    public Integer save(Manager manager) {
        if (manager.getId() != null) {
            if (manager.getPassword() != null && !Strings.isNullOrEmpty(manager.getPassword().trim())) {
                manager.setPassword(passwordEncoder.encode(manager.getPassword()));
            } else {
                manager.setPassword(null);
            }
            return managerDao.update(manager);
        } else {
            manager.setPassword(passwordEncoder.encode(manager.getPassword()));
            return managerDao.create(manager);
        }
    }

    public void del(Integer id) {
        managerDao.del(id);
    }
}
