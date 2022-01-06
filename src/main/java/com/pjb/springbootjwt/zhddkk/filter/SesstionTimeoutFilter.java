package com.pjb.springbootjwt.zhddkk.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.util.RedisTemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * session超时与否、存在与否过滤器
 */
@WebFilter(filterName = "sesstionTimeoutFilter", urlPatterns = {"/*"})
public class SesstionTimeoutFilter implements Filter {

    @Autowired
    private RedisTemplateUtil redisUtil;

    private static final Logger logger = LoggerFactory.getLogger(SesstionTimeoutFilter.class);

    // 忽略的URL前缀列表
    private static final List<String> IGNORE_URL_PREFIX_LIST = new ArrayList<>(Arrays.asList(
            "/js/",
            "/css/",
            "/json/",
            "/img/",
            "/canvas/",
            "/zhddWebSocket/",
            "/chatRoomWebSocket/",
            "/game/",
            "/upload/app",
            "/email",
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

        String uri = httpServletRequest.getRequestURI();
        String sessionId = httpServletRequest.getRequestedSessionId() == null ? "" : httpServletRequest.getRequestedSessionId();
        String lockKey = "REQ_LOCK_KEY_" + sessionId + ":" + uri;

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
        if (redisUtil.setNx(lockKey, "ing", 10)) {
            try {
                logger.info("已创建key:" + lockKey);
                httpServletRequest.setAttribute("filter", true);
                filterChain.doFilter(servletRequest, servletResponse);
            }catch (Exception e) {

            } finally {
                logger.info("移除key:" + lockKey);
                redisUtil.delete(lockKey);
            }
        } else {
            logger.info("key已被占用:" + lockKey);
            try {
                Result<String> result = Result.build(Integer.valueOf(CommonConstants.FREQUENCY_REQUEST_CODE), "请求过于频繁");
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                httpServletResponse.getOutputStream().write(JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void destroy() {
        logger.info("============SesstionTimeoutFilter destroy()============");
    }
}
