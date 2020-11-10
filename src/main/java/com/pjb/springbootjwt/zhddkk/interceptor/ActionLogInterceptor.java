package com.pjb.springbootjwt.zhddkk.interceptor;

import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 日志打印
 * @author zgn
 * @date 2018年7月12日
 */
@Configuration
public class ActionLogInterceptor implements HandlerInterceptor {

    private boolean logSwitch = true;
    private long startTime = 0;
    private String viewName = "";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o) throws Exception {
        startTime = System.currentTimeMillis();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null){
            viewName = modelAndView.getViewName();
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {
        if (!logSwitch){
            return;
        }

        String url = request.getRequestURI();
        if (!url.startsWith("/zhddkk") && !url.startsWith("/ws") && !url.startsWith("/file")){
            return;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");
        System.out.println("-----------------------------------action请求-------------------------------------");
        String functionName = "";
        if (handler != null){
            functionName =  handler.toString().trim();
            //去掉修饰符和参数
            int index = functionName.indexOf(" ");
            if(index != -1) {
                functionName = functionName.substring(index,functionName.length()-1).trim();
            }
            index = functionName.indexOf(" ");
            if(index != -1) {
                functionName = functionName.substring(index+1,functionName.length()-1).trim();
            }
            index = functionName.indexOf("(");
            if(index != -1) {
                functionName = functionName.substring(0,index).trim();
            }
        }

        StringBuilder paramsSb = new StringBuilder();
        Map parameterMap = request.getParameterMap();
        for (Object key:parameterMap.keySet()){
            String name = (String) key;
            if (StringUtils.isNotBlank(name) && (name.equals("password") || name.equals("passwd"))){
                continue;
            }
            String value = request.getParameter(name);
            paramsSb.append(name).append(":").append(value).append(" ");
        }
        System.out.println("接口地址: "+ url);
        System.out.println("调用方法: "+ functionName);
        System.out.println("参数列表: " + paramsSb.toString());
        System.out.println("耗时    : " + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println("操作时间: "+ dateFormat.format(new Date()));
        String user = (String)request.getSession(false).getAttribute(CommonConstants.S_USER);
        if (StringUtils.isNotBlank(user)){
            System.out.println("操作用户: " + user);
        }
        if (StringUtils.isNotBlank(viewName)){
            System.out.println("页面    : "+ viewName);
        }
        System.out.println("-------------------------------------------------------------------------------");
    }
}
