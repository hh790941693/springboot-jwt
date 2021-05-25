package com.pjb.springbootjwt.zhddkk.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pjb.springbootjwt.zhddkk.bean.SessionInfoBean;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.util.JsonUtil;
import com.pjb.springbootjwt.zhddkk.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * 用户登录拦截器.
 */
@Configuration
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        String servletPath = httpServletRequest.getServletPath();
        if (servletPath.contains("undefined")) {
            return false;
        }
        boolean filter = (boolean)httpServletRequest.getAttribute("filter");
        if (!filter) {
            return true;
        }

        // 检查session
        SessionInfoBean sessionInfoBean = SessionUtil.getSessionAttribute(CommonConstants.SESSION_INFO);
        String sessionUser = sessionInfoBean == null ? "" : sessionInfoBean.getUserName();

        // 如果session信息存在,放行
        if (StringUtils.isNotBlank(sessionUser)) {
            return true;
        }

        // 拦截 返回到登录页面
        logger.info("session user:{}", sessionUser);
        logger.info("servletPath:{}", servletPath);

        String headerAccept = httpServletRequest.getHeader("accept");
        String headerXRequestedWidth = httpServletRequest.getHeader("X-Requested-With");

        logger.info("accept:{}", headerAccept);
        logger.info("X-Requested-With:{}", headerXRequestedWidth);

        if (!(headerAccept.contains("application/json")
                || (headerXRequestedWidth != null && headerXRequestedWidth.contains("XMLHttpRequest")))) {
            // http请求
            String contextPath = httpServletRequest.getContextPath();
            httpServletResponse.sendRedirect(contextPath + "/");
        } else {
            // ajax请求
            try {
                //这里并不是设置跳转页面，而是将重定向的地址发给前端，让前端执行重定向
                //设置跳转地址
                httpServletResponse.setHeader("redirectUrl", CommonConstants.SESSION_TIMEOUT_REDIRECT_URL);
                // 设置错误信息
                httpServletResponse.setHeader("errorMsg", CommonConstants.SESSION_TIMEOUT_MSG);
                httpServletResponse.setHeader("errorCode", CommonConstants.SESSION_TIMEOUT_CODE);
                httpServletResponse.flushBuffer();

                PrintWriter writer = httpServletResponse.getWriter();
                Map<String, String> map = new HashMap<>();
                map.put("redirectUrl", CommonConstants.SESSION_TIMEOUT_REDIRECT_URL);
                map.put("code", CommonConstants.SESSION_TIMEOUT_CODE);
                map.put("msg", CommonConstants.SESSION_TIMEOUT_MSG);
                // JSON格式返回给前端
                writer.write(JsonUtil.javaobject2Jsonstr(map));
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }
}