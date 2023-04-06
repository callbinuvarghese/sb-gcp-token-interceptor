package com.binu.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class EmployeeLoginInterceptor extends HandlerInterceptorAdapter {

    Logger logger = LoggerFactory.getLogger(EmployeeLoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        logger.info("====== Employee Login PreHandle Interceptor to do authentication logic ======");
        //we can returns true or false
        // depending on our logic to allow user to access rest controller api
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        logger.info("====== Employee Login PostHandle Interceptor ======");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) {
        logger.info("====== Employee Login AfterCompletion Interceptor ======");
    }
}
