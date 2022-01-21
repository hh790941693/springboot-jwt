package com.pjb.springbootjwt.zhddkk.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.bean.SessionInfoBean;
import com.pjb.springbootjwt.zhddkk.cache.CoreCache;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.domain.WsUserSessionDO;
import com.pjb.springbootjwt.zhddkk.util.JsonUtil;
import com.pjb.springbootjwt.zhddkk.util.RedisTemplateUtil;
import com.pjb.springbootjwt.zhddkk.util.SessionUtil;
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
            "/login",
            "/login.do",
            "/index",
            "/exception.page",
            "/redirect",
            "/valid",
            "/verifyUser.do",
            "/register.page",
            "/forgetPassword.page",
            "/getUserQuestion.json",
            "/showQRCode.do",
            "/checkUserRegisterStatus.json",
            "/updatePassword.do",
            "/upload/app",
            "/register.do",
            "/getChatRoomInfo.json",
            "/js/",
            "/css/",
            "/json/",
            "/img/",
            "/i18n",
            "/canvas/",
            "/zhddWebSocket/",
            "/chatRoomWebSocket/",
            "/game/",
            "/upload/app",
            "/email",
            "/generateVerifyCode.do"));

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

        // 如果是http://ip:port或者http://ip:port/,放行
        String servletPath = httpServletRequest.getServletPath();
        if (servletPath.equals("") || servletPath.equals("/")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 如果是忽略的前缀url,放行
        for (String prefix : IGNORE_URL_PREFIX_LIST) {
            if (servletPath.startsWith(prefix)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        // 如果是忽略的后缀url,放行
        for (String suffix : IGNORE_URL_SUFFIX_LIST) {
            if (servletPath.endsWith(suffix)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        String lockKey = "REQ_LOCK_KEY_" + sessionId + "_" + uri;
        if (redisUtil.setNx(lockKey, "ing", 10)) {
            try {
                logger.info("已创建key:" + lockKey);

                // 检查session
                SessionInfoBean sessionInfoBean = SessionUtil.getSessionAttribute(CommonConstants.SESSION_INFO);

                String redirectUrl = "/exception.page?redirectName=sessionTimeout";
                String resultCode = CommonConstants.SESSION_TIMEOUT_CODE;
                // 如果session信息存在,放行
                if (null != sessionInfoBean) {
                    //从缓存中获取SESSION数据
                    List<WsUserSessionDO> userSessionList = CoreCache.getInstance().getUserSessionList();
                    WsUserSessionDO wsUserSessionDO = userSessionList.stream().filter(obj->obj.getUserId().toString().equals(sessionInfoBean.getUserId())).findAny().orElse(null);
                    if (null != wsUserSessionDO && !wsUserSessionDO.getSessionId().equals(httpServletRequest.getSession().getId())) {
                        // 如果用户重复登陆，则需要重定向到登陆页面
                        redirectUrl = "/exception.page?redirectName=conflictLogin";
                        resultCode = CommonConstants.CONFLICT_LOGIN_CODE;
                    } else {
                        filterChain.doFilter(servletRequest, servletResponse);
                        return;
                    }
                }

                // 拦截 返回到登录页面
                logger.info("session user:{}", null != sessionInfoBean ? sessionInfoBean.getUserName() : "");
                logger.info("servletPath:{}", servletPath);

                String headerAccept = httpServletRequest.getHeader("accept");
                String headerXRequestedWidth = httpServletRequest.getHeader("X-Requested-With");

                logger.info("accept:{}", headerAccept);
                logger.info("X-Requested-With:{}", headerXRequestedWidth);

                if (!(headerAccept.contains("application/json")
                        || (headerXRequestedWidth != null && headerXRequestedWidth.contains("XMLHttpRequest")))) {
                    // http请求
                    logger.info("****************** HTTP请求 ***********************");
                    String contextPath = httpServletRequest.getContextPath();
                    httpServletResponse.sendRedirect(contextPath + redirectUrl);
                } else {
                    // ajax请求
                    try {
                        //这里并不是设置跳转页面，而是将重定向的地址发给前端，让前端执行重定向
                        logger.info("****************** AJAX请求 ***********************");
                        //设置跳转地址
                        httpServletResponse.setHeader("redirectUrl", redirectUrl);
                        // 设置错误信息
                        httpServletResponse.setHeader("errorCode", resultCode);
                        httpServletResponse.flushBuffer();

                        PrintWriter writer = httpServletResponse.getWriter();
                        Map<String, String> map = new HashMap<>();
                        map.put("redirectUrl", redirectUrl);
                        map.put("code", resultCode);

                        // JSON格式返回给前端
                        writer.write(JsonUtil.javaobject2Jsonstr(map));
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {

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
