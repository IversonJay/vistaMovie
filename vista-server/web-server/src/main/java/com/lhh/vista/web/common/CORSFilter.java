package com.lhh.vista.web.common;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by liu on 2016/12/23.
 */
public class CORSFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
//         response.setHeader("Access-Control-Allow-Origin", "http://vistacinemas.cn/");
//        response.setHeader("Access-Control-Allow-Origin", "*");
          response.setHeader("Access-Control-Allow-Origin", "*");
        // response.setHeader("Access-Control-Allow-Origin", "http://192.168.31.153:9911");
        //  response.setHeader("Access-Control-Allow-Origin", "http://192.168.0.115:9911");
        //response.setHeader("Access-Control-Allow-Origin", "http://192.168.0.106:9911");

        response.setHeader("-Allow-MeAccess-Controlthods", "POST, GET");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }
}