package com.pjb.springbootjwt.zhddkk.filter;

import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebFilter(filterName = "sesstionTimeoutFilter",urlPatterns = {"/*"})
public class SesstionTimeoutFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(SesstionTimeoutFilter.class);

    private static final List<String> IGNORE_URL_LIST = Arrays.asList("", "/", "/index", "/wslogin.do", "/canvas/snow.page");

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
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "User-Agent,Origin,Cache-Control,Content-type,Date,Server,withCredentials,AccessToken,username,offlineticket,Authorization");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        String user = (String) httpServletRequest.getSession().getAttribute(CommonConstants.S_USER);
        logger.info("user:" + user + ", servletPath:" + servletPath);

        if (IGNORE_URL_LIST.contains(servletPath) || servletPath.startsWith("/zhddWebSocket/")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (servletPath.endsWith(".js") || servletPath.endsWith(".css")
                || servletPath.endsWith(".jpg") || servletPath.endsWith(".jpeg")
                || servletPath.endsWith(".png") || StringUtils.isNotBlank(user)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 拦截 返回到登录页面
        logger.info("session user:" + user);
        // JSP格式返回
        if (!(httpServletRequest.getHeader("accept").indexOf("application/json") > -1 || (httpServletRequest
                .getHeader("X-Requested-With") != null && httpServletRequest.getHeader("X-Requested-With").indexOf(
                "XMLHttpRequest") > -1))) {

            // 如果不是异步请求
            httpServletResponse.sendRedirect(contextPath + "/");
        } else { // JSON格式返回
            try {
                Map<String, String> map = new HashMap<>();
                map.put("resultCode", "-1");
                map.put("resultMsg", "session invalid");
                PrintWriter writer = httpServletResponse.getWriter();
                writer.write(JsonUtil.javaobject2Jsonstr(map));
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void destroy() {
        logger.info("============SesstionTimeoutFilter destroy()");
    }
}
