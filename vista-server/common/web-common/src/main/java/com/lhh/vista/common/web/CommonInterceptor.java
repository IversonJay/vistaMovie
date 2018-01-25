package com.lhh.vista.common.web;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author 刘一先
 *
 * 通用拦截器，拦截所有请求，并进行注入
 *
 */
public class CommonInterceptor implements HandlerInterceptor{
	
	public void afterCompletion(HttpServletRequest request,HttpServletResponse response, Object object, Exception arg3)throws Exception {
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response,Object object, ModelAndView mav) throws Exception {
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object object) throws Exception {
		if(object!=null&&object instanceof HandlerMethod&&((HandlerMethod)object).getBean() instanceof BaseController){
			BaseController controller =(BaseController) ((HandlerMethod)object).getBean();
			controller.setRequest(request);
			controller.setResponse(response);
		}
		return true;
	}
}
