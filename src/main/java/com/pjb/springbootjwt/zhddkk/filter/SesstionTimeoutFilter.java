package com.pjb.springbootjwt.zhddkk.filter;

import java.io.IOException;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * session超时与否、存在与否过滤器
 */
@WebFilter(filterName = "sesstionTimeoutFilter", urlPatterns = {"/*"})
public class SesstionTimeoutFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(SesstionTimeoutFilter.class);

    // 忽略的URL前缀列表
    private static final List<String> IGNORE_URL_PREFIX_LIST = new ArrayList<>(Arrays.asList(
            "/js/",
            "/css/",
            "/json/",
            "/img/",
            "/canvas/",
            "/zhddWebSocket/",
            "/game/",
            "/upload/app",
            "/i18n"));

    // 忽略的URL后缀列表
    private static final List<String> IGNORE_URL_SUFFIX_LIST = new ArrayList<>(Arrays.asList(
            ".js",
            ".css",
            ".jpg",
            ".jpeg",
            ".png",
            ".mp3",
            ".ico",
            ".properties"));

    @Override
    public void init(FilterConfig filterConfig) {
        logger.info("============SesstionTimeoutFilter init()============");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Headers",
                "User-Agent,Origin,Cache-Control,Content-type,Date,Server,withCredentials,AccessToken,username,offlineticket,Authorization");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        httpServletRequest.setCharacterEncoding("UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");

        String servletPath = httpServletRequest.getServletPath();
        // 如果是忽略的前缀url,放行
        for (String prefix : IGNORE_URL_PREFIX_LIST) {
            if (servletPath.startsWith(prefix)) {
                httpServletRequest.setAttribute("filter", false);
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        // 如果是忽略的后缀url,放行
        for (String suffix : IGNORE_URL_SUFFIX_LIST) {
            if (servletPath.endsWith(suffix)) {
                httpServletRequest.setAttribute("filter", false);
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        httpServletRequest.setAttribute("filter", true);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        logger.info("============SesstionTimeoutFilter destroy()============");
    }
}
