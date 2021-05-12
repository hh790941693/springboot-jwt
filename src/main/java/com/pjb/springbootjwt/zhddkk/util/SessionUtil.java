package com.pjb.springbootjwt.zhddkk.util;

import com.pjb.springbootjwt.zhddkk.bean.SessionInfoBean;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtil {

    public static SessionInfoBean getSessionInfo(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if (null != httpSession) {
            return (SessionInfoBean)httpSession.getAttribute(CommonConstants.SESSION_INFO);
        }

        return null;
    }
}
