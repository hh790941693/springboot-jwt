package com.pjb.springbootjwt.zhddkk.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtil {

    public static <T> T getSessionAttribute(HttpServletRequest request, String name) {
        HttpSession httpSession = request.getSession(false);
        if (null != httpSession) {
            return (T)httpSession.getAttribute(name);
        }
        return null;
    }

    public static void setSessionAttribute(HttpServletRequest request, String name, Object value) {
        HttpSession httpSession = request.getSession(false);
        if (null != httpSession) {
            httpSession.setAttribute(name, value);
        }
    }
}
