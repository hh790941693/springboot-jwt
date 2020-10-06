package com.pjb.springbootjwt.zhddkk.filter;

import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "testFilter",urlPatterns = {"/*"})
public class SesstionTimeoutFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(SesstionTimeoutFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("============SesstionTimeoutFilter init()");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String servletPath = httpServletRequest.getServletPath();
        String contextPath = httpServletRequest.getContextPath();

        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Headers","User-Agent,Origin,Cache-Control,Content-type,Date,Server,withCredentials,AccessToken,username,offlineticket,Authorization");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        String user = (String)httpServletRequest.getSession().getAttribute(CommonConstants.S_USER);
        if (!(servletPath.endsWith(".js") || servletPath.endsWith(".css")
                || servletPath.endsWith(".jpg")|| servletPath.endsWith(".jpeg")
                || servletPath.endsWith(".png"))) {
            logger.info("session user:" + user);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        logger.info("============SesstionTimeoutFilter destroy()");
    }
}
