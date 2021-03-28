package com.pjb.springbootjwt.zhddkk.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.pjb.springbootjwt.zhddkk.bean.SessionInfoBean;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用户登录拦截器.
 */
@Configuration
public class LoginInterceptor implements HandlerInterceptor {
    private static final Log logger = LogFactory.getLog(LoginInterceptor.class);

    // 过滤的url
    private static final List<String> filterUrls = Arrays.asList("ws/", "ws");

    public boolean preHandle() {
        System.out.println("-------------------login preHandle------------------");
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
//        String uri = request.getRequestURI();
//        for (String filUrl : filterUrls) {
//            if (uri.endsWith(filUrl)) {
//                return;
//            }
//        }
//
//        SessionInfoBean sessionInfoBean = (SessionInfoBean) request.getSession().getAttribute(CommonConstants.SESSION_INFO);
//        String user = sessionInfoBean == null ? "" : sessionInfoBean.getUser();
//        String pass = sessionInfoBean == null ? "" : sessionInfoBean.getPassword();
//        String url = request.getRequestURL().toString();
//        logger.debug("用户" + user + "访问:" + url);
//
//        if (StringUtils.isEmpty(user) || StringUtils.isEmpty(pass)) {
//            System.out.println("非法访问:" + uri);
//            try {
//                String context = request.getContextPath();
//                request.getRequestDispatcher("/ws/login.page").forward(request, response);
//                //response.sendRedirect(context+"/ws/login.page");
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("跳转至首页失败:" + e.getMessage());
//            }
//        }
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }
}