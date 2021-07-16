package com.pjb.springbootjwt.zhddkk.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class WebUtil {

    public static final String REQUEST_TOKEN = "token";

    /**
     * 表单请求是否是重复提交.
     * true:重复请求  false：不重复请求
     * @param request
     * @return
     */
    public static boolean isRepeatSubmit(HttpServletRequest request) {
        String clientToken = request.getParameter(REQUEST_TOKEN);
        if (StringUtils.isBlank(clientToken)) {
            return true;
        }
        String serverToken = (String)request.getSession(false).getAttribute(REQUEST_TOKEN);
        if (StringUtils.isBlank(serverToken)) {
            return true;
        }
        if (!clientToken.equals(serverToken)) {
            return true;
        }

        return false;
    }

    public static String generateAccessToken() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return UUID.randomUUID().toString() + "-" + timestamp;
    }
}
