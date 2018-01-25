package com.lhh.vista.web.interceptors;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhh.vista.common.model.BaseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.service.model.Manager;
import com.lhh.vista.web.common.CommonData;
import com.lhh.vista.web.controller.manage.MCommonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Soap
 *         <p>
 *         通用拦截器，拦截所有请求，并进行注入
 */
public class ManageInterceptor implements HandlerInterceptor {
    @Autowired
    private ObjectMapper jsonUtil;

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception arg3) throws Exception {
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView mav) throws Exception {

    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        if (object instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) object;
            if (handlerMethod.getBean() instanceof MCommonController) {
                return true;
            }
        }
        Manager user = (Manager) request.getSession().getAttribute(CommonData.LOGIN_USER);
        if (user == null) {
            BaseResult r = new BaseResult();
            r.setState(StateTool.State.FAIL);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(jsonUtil.writeValueAsString(r));
            return false;
        }
        return true;
    }
}
