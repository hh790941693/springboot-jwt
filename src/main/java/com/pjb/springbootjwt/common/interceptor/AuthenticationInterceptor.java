package com.pjb.springbootjwt.common.interceptor;

import com.pjb.springbootjwt.common.annotation.NeedRefreashToken;
import com.pjb.springbootjwt.common.exception.ApplicationException;
import com.pjb.springbootjwt.common.utils.BuildResponseUtil;
import com.pjb.springbootjwt.ump.domain.UserDO;
import com.pjb.springbootjwt.ump.service.UserService;
import com.pjb.springbootjwt.util.JwtUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


/**
 * @author jinbin
 * @date 2018-07-08 20:41
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        //logger.info("进入到拦截器AuthenticationInterceptor.preHandle");
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod)object;
        Method method = handlerMethod.getMethod();
        String methodName = method.getName();
        //logger.info("方法名:{}", methodName);
        if (method.isAnnotationPresent(RequiresAuthentication.class)) {
            logger.info("当前请求需要token");
            String token = httpServletRequest.getHeader("Authorization");
            if (StringUtils.isEmpty(token)) {
                BuildResponseUtil.buildRes("404", "无token，不允许访问", httpServletResponse);
                return false;
            }
            String userId = JwtUtils.getUserId(token);
            UserDO user = userService.findUserById(userId);
            if (user == null) {
                BuildResponseUtil.buildRes("405", "用户不存在", httpServletResponse);
                return false;
            }

            // 正常请求token 执行认证
            try {
                JwtUtils.verifyToken(token, user.getId(), user.getUsername() + user.getPassword());
            }catch (ApplicationException e){
                logger.info("token认证失败:{}", e.getMessage());
                BuildResponseUtil.buildRes(e.getCode(), e.getDesc(), httpServletResponse);
                return false;
            }
        } else if(method.isAnnotationPresent(NeedRefreashToken.class)) {
            //刷新token请求 执行认证
            logger.info("当前请求需要refreashToken认证");
            NeedRefreashToken needRefreashToken = method.getAnnotation(NeedRefreashToken.class);
            if (!needRefreashToken.required()) {
                return true;
            }

            String token = httpServletRequest.getHeader("Authorization");
            // 执行认证
            if (StringUtils.isEmpty(token)) {
                BuildResponseUtil.buildRes("404", "无token，不允许访问", httpServletResponse);
                return false;
            }
            String userId = JwtUtils.getUserId(token);
            UserDO user = userService.findUserById(userId);
            // 刷新token
            try {
                JwtUtils.verifyRefreashToken(token, user.getId(), user.getId() + user.getPassword());
            }catch (ApplicationException e){
                logger.info("refreashToken认证失败:{}", e.getMessage());
                BuildResponseUtil.buildRes(e.getCode(), e.getDesc(), httpServletResponse);
                return false;
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
