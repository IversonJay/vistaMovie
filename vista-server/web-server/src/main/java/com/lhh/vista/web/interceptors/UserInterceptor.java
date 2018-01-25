package com.lhh.vista.web.interceptors;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lhh.vista.service.dto.AppUserLoginRes;
import com.lhh.vista.service.model.AppUser;
import com.lhh.vista.service.service.AppUserService;
import com.lhh.vista.web.common.CommonData;
import com.lhh.vista.web.controller.app.ACollectController;
import com.lhh.vista.web.controller.app.AOrderController;
import com.lhh.vista.web.controller.app.ARechargeController;
import com.lhh.vista.web.controller.app.ARewardController;
import com.lhh.vista.web.controller.app.AUserController;
import com.lhh.vista.web.controller.app.AVoucherController;

import net.sf.json.JSONObject;

/**
 * @author Soap
 *         <p>
 *         通用拦截器，拦截所有请求，并进行注入
 */
public class UserInterceptor implements HandlerInterceptor {
    @Autowired
    private AppUserService appUserService;
    
    public static Set<String> userSet = new HashSet();

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception arg3) throws Exception {
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView mav) throws Exception {

    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        if (object instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) object;
            if (handlerMethod.getBean() instanceof ACollectController || handlerMethod.getBean() instanceof ARewardController || handlerMethod.getBean() instanceof AUserController || handlerMethod.getBean() instanceof AOrderController || handlerMethod.getBean() instanceof ARechargeController || handlerMethod.getBean() instanceof AVoucherController) {
            	AppUser appUser = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            	
            	AppUserLoginRes appUserLoginRes = null;
            	AppUser appUserByToken = null;
            	String token = request.getParameter("token");
            	try {
//	            	appUserLoginRes = appUserService.loginByToken(token);
//	                appUserByToken = appUserLoginRes.getAppUser();
            		appUserByToken = appUserService.loginValidateToken(token);
            	} catch (Exception e) {
            		System.out.println("exception。。。。。。。。。。。。。。。。。。。。。");
                }
            	
                if (appUser != null) {
                	UserInterceptor.userSet.add(appUser.getMemberId());
                	if (appUserByToken == null) { //被人顶了
                		returnErrorMessage(response);
                        return false;
                	}
                    return true;
                }
              
                if (appUserByToken != null) { //session过期
                	UserInterceptor.userSet.add(appUserByToken.getMemberId());
                    request.getSession().setAttribute(CommonData.APP_LOGIN_USER, appUserByToken);
                    return true;
                }
                returnErrorMessage1(response);
                return false;
            }
            return true;
        }

    	
    	return true;
    }
    
    
    private void returnErrorMessage(HttpServletResponse response) throws IOException {
    	response.setCharacterEncoding("UTF-8");    
    	response.setContentType("application/json; charset=utf-8");  
    	PrintWriter out = null ;  
    	try{  
    	    JSONObject res = new JSONObject();  
    	    res.put("state","-2");  
    	    res.put("result","多点登陆");  
    	    out = response.getWriter();  
    	    out.append(res.toString());  
    	}  
    	catch (Exception e){  
    	    e.printStackTrace();  
    	    response.sendError(500);  
    	}  
    }
    
    
    private void returnErrorMessage1(HttpServletResponse response) throws IOException {
    	response.setCharacterEncoding("UTF-8");    
    	response.setContentType("application/json; charset=utf-8");  
    	PrintWriter out = null ;  
    	try{  
    	    JSONObject res = new JSONObject();  
    	    res.put("state","-3");  
    	    res.put("result","长时间未登录 + 被别人顶了");  
    	    out = response.getWriter();  
    	    out.append(res.toString());  
    	}  
    	catch (Exception e){  
    	    e.printStackTrace();  
    	    response.sendError(500);  
    	}  
    }
}
