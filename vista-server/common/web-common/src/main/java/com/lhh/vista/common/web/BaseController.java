package com.lhh.vista.common.web;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.common.base.Strings;
import freemarker.template.Template;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    protected Log log = LogFactory.getLog(getClass());
    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;
    public String getContent(String templateName, Map<String, Object> model) throws Exception{
        Map<String, Object> models=new HashMap<>();
        models.put("appName",request.getContextPath());
        if(model!=null){
            models.putAll(model);
        }
        Template t = freemarkerConfigurer.getConfiguration().getTemplate(templateName);
        return FreeMarkerTemplateUtils.processTemplateIntoString(t, models);
    }
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public class Skip {
        public static final int NORMAL = 0;
        public static final int FORWARD = 1;
        public static final int REDIRECT = 2;
    }

    public String toView(String page) {
        return toView(page, Skip.NORMAL);
    }

    public String toView(String page, int skip) {
        switch (skip) {
            case Skip.NORMAL:
                return "/" + page;
            case Skip.FORWARD:
                return "forward:/" + page;
            case Skip.REDIRECT:
                return "redirect:/" + page;
            default:
                return "";
        }
    }
}
