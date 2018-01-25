package com.lhh.vista.web.controller.manage;

import com.lhh.vista.common.util.DateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.service.model.Manager;
import com.lhh.vista.service.service.ManagerService;
import com.lhh.vista.web.common.CommonData;
import com.lhh.vista.web.common.IPTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by soap on 2016/9/27.
 */
@Controller
@RequestMapping("m_common")
public class MCommonController extends BaseController {
    @Autowired
    private ManagerService managerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("toLogin")
    public String toLogin() {
        return toView("manage/login");
    }

    /**
     * 登陆
     */
    @RequestMapping("login")
    public String login(String username, String password) {
        Manager manager = managerService.findByUserName(username);
        if (manager != null && passwordEncoder.matches(password, manager.getPassword())&&manager.getState()==Manager.STATE_NORMAL) {
            Manager tempUser = new Manager();
            tempUser.setId(manager.getId());
            tempUser.setLastLoginTime(DateTool.getNowTime());
            tempUser.setLastLoginIp(IPTool.getIpAddress(request));

            manager.setLastLoginIp(tempUser.getLastLoginIp());
            manager.setLastLoginTime(tempUser.getLastLoginTime());

            request.getSession().setAttribute(CommonData.LOGIN_USER, manager);
            managerService.save(tempUser);

            return toView("m_system/toIndex", Skip.REDIRECT);
        }
        return toView("m_common/toLogin", Skip.REDIRECT);
    }
}
