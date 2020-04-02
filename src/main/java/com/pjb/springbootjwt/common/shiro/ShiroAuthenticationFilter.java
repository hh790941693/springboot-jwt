package com.pjb.springbootjwt.common.shiro;

import com.pjb.springbootjwt.common.exception.ApplicationException;
import com.pjb.springbootjwt.common.utils.BuildResponseUtil;
import com.pjb.springbootjwt.common.utils.SpringContextHolder;
import com.pjb.springbootjwt.ump.domain.UserDO;
import com.pjb.springbootjwt.ump.service.UserService;
import com.pjb.springbootjwt.util.JwtUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

public class ShiroAuthenticationFilter extends BasicHttpAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(ShiroAuthenticationFilter.class);

    /**
     * 判断是否为登录请求
     */
    @Override
    protected boolean isLoginAttempt(String authzHeader) {
        return StringUtils.isNotBlank(authzHeader);
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response){
        return false;//已经在isAccessAllowed登录过了，不执行父类的登录操作（token不同）
    }

    /**
     * 这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 注解 @RequiresAuthentication
     * 缺点：不能够对GET,POST等请求进行分别过滤鉴权
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        logger.info("进入过滤器ShiroAuthenticationFilter.isAccessAllowed");
        if (isLoginAttempt(request, response)) {
            //header带有Authorization的才会进来
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String authorization = httpServletRequest.getHeader("Authorization");
            String userId = JwtUtils.getUserId(authorization);
            if (StringUtils.isBlank(userId)){
                return false;
            }
            UserDO userDO = SpringContextHolder.getBean(UserService.class).selectById(userId);
            if (null == userDO){
                return false;
            }
            logger.info("校验token");
            try {
                JwtUtils.verifyToken(authorization, userId, userDO.getUsername() + userDO.getPassword());
            }catch (ApplicationException e){
                logger.info("验证token失败:{}", e.getMessage());
                getSubject(request, response).logout();
                BuildResponseUtil.buildRes(e.getCode(), e.getDesc(), (HttpServletResponse)response);
                return false;
            }

            try {
                logger.info("调用getSubject().login()");
                //ShiroAuthenticationTokenToken jwtAuthenticationTokenToken = new ShiroAuthenticationTokenToken(authorization);
                UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userDO.getUsername(), userDO.getPassword());
                getSubject(request, response).login(usernamePasswordToken);
            }catch (Exception e){
                e.printStackTrace();
                BuildResponseUtil.buildRes("412", "调用getSubject().login()异常", (HttpServletResponse)response);
                return false;
            }
        }
        return true;
    }

    /**
     * 跨域处理
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        logger.info("进入过滤器ShiroAuthenticationFilter.preHandle");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

}