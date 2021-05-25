package com.pjb.springbootjwt.zhddkk.interceptor;

import com.pjb.springbootjwt.zhddkk.bean.SessionInfoBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 接口日志拦截器.
 */
@Configuration
public class ActionLogInterceptor implements HandlerInterceptor {

    // 接口日志开关
    private boolean logSwitch = true;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o) {
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) {
        if (modelAndView != null) {
            httpServletRequest.setAttribute("viewName", modelAndView.getViewName());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        if (!logSwitch) {
            return;
        }

        String url = request.getRequestURI();
        if (url.contains("undefined")) {
            return;
        }

        System.out.println("-----------------------------------action请求-------------------------------------");
        String functionName =  handler.toString().trim();
        //去掉修饰符和参数
        int index = functionName.indexOf(" ");
        if (index != -1) {
            functionName = functionName.substring(index, functionName.length() - 1).trim();
        }
        index = functionName.indexOf(" ");
        if (index != -1) {
            functionName = functionName.substring(index + 1, functionName.length() - 1).trim();
        }
        index = functionName.indexOf("(");
        if (index != -1) {
            functionName = functionName.substring(0, index).trim();
        }

        StringBuilder paramsSb = new StringBuilder();
        Map parameterMap = request.getParameterMap();
        for (Object key:parameterMap.keySet()) {
            String name = (String) key;
            if (StringUtils.isNotBlank(name) && (name.equals("password") || name.equals("passwd") || name.equals("pass"))) {
                continue;
            }
            String value = request.getParameter(name);
            paramsSb.append(name).append(":").append(value).append(" ");
        }
        System.out.println("接口地址: " + url);
        System.out.println("调用方法: " + functionName);
        System.out.println("参数列表: " + paramsSb.toString());
        long startTime = (long)request.getAttribute("startTime");
        System.out.println("耗时    : " + (System.currentTimeMillis() - startTime) + "ms");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");
        System.out.println("操作时间: " + dateFormat.format(new Date()));
        SessionInfoBean sessionInfoBean = SessionUtil.getSessionAttribute(CommonConstants.SESSION_INFO);
        if (null != sessionInfoBean) {
            String user = sessionInfoBean.getUserName();
            System.out.println("操作用户: " + user);
        }
        String viewName = (String)request.getAttribute("viewName");
        if (StringUtils.isNotBlank(viewName)) {
            System.out.println("页面    : " + viewName);
        }
        System.out.println("-------------------------------------------------------------------------------");
    }
}
