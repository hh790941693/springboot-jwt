package com.pjb.springbootjwt.zhddkk.filter;

import com.pjb.springbootjwt.zhddkk.bean.SessionInfoBean;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.util.JsonUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter(filterName = "sesstionTimeoutFilter", urlPatterns = {"/*"})
public class SesstionTimeoutFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(SesstionTimeoutFilter.class);

    // 忽略的URL地址列表
    private static final List<String> IGNORE_URL_LIST = new ArrayList<>(Arrays.asList(
            "",
            "/",
            "/index",
            "/wslogin.do",
            "/canvas/snow.page",
            "/querySystemInfo",
            "/register.page",
            "/forgetPassword.page",
            "/canvas/canvasIndex.page",
            "/queryAllCommonData.do",
            "/favicon.ico",
            "/getUserQuestion.json",
            "/showQRCode.do",
            "/checkUserRegisterStatus.json",
            "/updatePassword.do",
            "/loginfail.page",
            "/sessionInfo",
            "/sessionInfo.page",
            "/generateVerifyCode.do",
            "/upload/app",
            "/wsregister.do",
            "/getChatRoomInfo.json"));

    // 忽略的URL前缀列表
    private static final List<String> IGNORE_URL_PREFIX_LIST = new ArrayList<>(Arrays.asList(
            "/js/",
            "/css/",
            "/json/",
            "/img/",
            "/canvas/",
            "/zhddWebSocket/",
            "/game/",
            "/i18n"));

    // 忽略的URL后缀列表
    private static final List<String> IGNORE_URL_SUFFIX_LIST = new ArrayList<>(Arrays.asList(
            ".js",
            ".css",
            ".jpg",
            ".jpeg",
            ".png",
            ".mp3"));

    @Override
    public void init(FilterConfig filterConfig) {
        logger.info("============SesstionTimeoutFilter init()============");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String contextPath = httpServletRequest.getContextPath();

        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Headers",
                "User-Agent,Origin,Cache-Control,Content-type,Date,Server,withCredentials,AccessToken,username,offlineticket,Authorization");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        SessionInfoBean sessionInfoBean = (SessionInfoBean) httpServletRequest.getSession().getAttribute(CommonConstants.SESSION_INFO);
        String user = sessionInfoBean == null ? "" : sessionInfoBean.getUser();

        String servletPath = httpServletRequest.getServletPath();
        // 是否要过滤 true:过滤 false:不过滤
        boolean filterFlag = false;
        if (IGNORE_URL_LIST.contains(servletPath) || StringUtils.isNotBlank(user)) {
            filterFlag = true;
        }
        if (!filterFlag) {
            for (String prefix : IGNORE_URL_PREFIX_LIST) {
                if (servletPath.startsWith(prefix)) {
                    filterFlag = true;
                    break;
                }
            }

            if (!filterFlag) {
                for (String suffix : IGNORE_URL_SUFFIX_LIST) {
                    if (servletPath.endsWith(suffix)) {
                        filterFlag = true;
                        break;
                    }
                }
            }
        }

        if (filterFlag) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 拦截 返回到登录页面
        logger.info("session user:" + user);
        logger.info("servletPath:" + servletPath);

        String headerAccept = httpServletRequest.getHeader("accept");
        String headerXRequestedWidth = httpServletRequest.getHeader("X-Requested-With");

        logger.info("accept:" + headerAccept);
        logger.info("X-Requested-With:" + headerXRequestedWidth);
        // JSP格式返回
        if (!(headerAccept.indexOf("application/json") > -1 || (headerXRequestedWidth != null && headerXRequestedWidth.indexOf(
                "XMLHttpRequest") > -1))) {
            // http请求
            httpServletResponse.sendRedirect(contextPath + "/");
        } else {
            // ajax请求
            try {
                Map<String, String> map = new HashMap<>();
                map.put("redirectUrl", CommonConstants.SESSION_TIMEOUT_REDIRECT_URL);
                map.put("code", CommonConstants.SESSION_TIMEOUT_CODE);
                map.put("msg", CommonConstants.SESSION_TIMEOUT_MSG);

                //这里并不是设置跳转页面，而是将重定向的地址发给前端，让前端执行重定向
                //设置跳转地址
                httpServletResponse.setHeader("redirectUrl", CommonConstants.SESSION_TIMEOUT_REDIRECT_URL);
                // 设置错误信息
                httpServletResponse.setHeader("errorMsg", CommonConstants.SESSION_TIMEOUT_MSG);
                httpServletResponse.setHeader("errorCode", CommonConstants.SESSION_TIMEOUT_CODE);
                httpServletResponse.flushBuffer();

                PrintWriter writer = httpServletResponse.getWriter();
                // JSON格式返回
                writer.write(JsonUtil.javaobject2Jsonstr(map));
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void destroy() {
        logger.info("============SesstionTimeoutFilter destroy()============");
    }
}
