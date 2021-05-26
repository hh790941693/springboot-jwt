package com.pjb.springbootjwt.zhddkk.util;

import com.pjb.springbootjwt.zhddkk.bean.SessionInfoBean;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtil {

    public static <T> T getSessionAttribute(String name) {
        HttpSession httpSession = getRequest().getSession(false);
        if (null != httpSession) {
            return (T)httpSession.getAttribute(name);
        }
        return null;
    }

    public static void setSessionAttribute(String name, Object value) {
        HttpSession httpSession = getRequest().getSession(false);
        if (null != httpSession) {
            httpSession.setAttribute(name, value);
        }
    }

    public static String getSessionUserId() {
        HttpSession httpSession = getRequest().getSession(false);
        if (null != httpSession) {
            SessionInfoBean sessionInfoBean = (SessionInfoBean)httpSession.getAttribute(CommonConstants.SESSION_INFO);
            if (null != sessionInfoBean) {
                return sessionInfoBean.getUserId();
            }
        }
        return null;
    }

    public static String getSessionUserName() {
        HttpSession httpSession = getRequest().getSession(false);
        if (null != httpSession) {
            SessionInfoBean sessionInfoBean = (SessionInfoBean)httpSession.getAttribute(CommonConstants.SESSION_INFO);
            if (null != sessionInfoBean) {
                return sessionInfoBean.getUserName();
            }
        }
        return null;
    }

    private static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        return requestAttributes == null ? null : requestAttributes.getRequest();
    }
}
