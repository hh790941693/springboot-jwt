package com.pjb.springbootjwt.zhddkk.interceptor;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pjb.springbootjwt.zhddkk.bean.SessionInfoBean;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.druid.util.StringUtils;


/**
 * 用户登录拦截器
 * 
 * 
 * @author Administrator
 *
 */
@Configuration
public class LoginInterceptor extends HandlerInterceptorAdapter implements InitializingBean
{
    private static final Log logger = LogFactory.getLog(LoginInterceptor.class);

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    // 过滤的url
    private static List<String> filterUrls = Arrays.asList("ws/","ws");

    public boolean preHandle(HttpServletRequest request)
    {
        System.out.println("-------------------login preHandle------------------");
        return true;
    }

    @SuppressWarnings("unused")
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        String uri = request.getRequestURI();
        for (String filUrl : filterUrls) {
            if (uri.endsWith(filUrl)){
                return;
            }
        }

        SessionInfoBean sessionInfoBean = (SessionInfoBean)request.getSession().getAttribute(CommonConstants.SESSION_INFO);
        String user = sessionInfoBean == null ? "" : sessionInfoBean.getUser();
        String pass = sessionInfoBean == null ? "" : sessionInfoBean.getPassword();
        String url = request.getRequestURL().toString();
        logger.debug("用户"+user+"访问:"+url);

        if (StringUtils.isEmpty(user) || StringUtils.isEmpty(pass)) {
            System.out.println("非法访问:"+uri);
            try {
                String context = request.getContextPath();
                request.getRequestDispatcher("/ws/login.page").forward(request, response);
                //response.sendRedirect(context+"/ws/login.page");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("跳转至首页失败:"+e.getMessage());
            }
        }
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

}