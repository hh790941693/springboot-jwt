package com.pjb.springbootjwt.zhddkk.listener;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * session创建与销毁监听器.
 */
@Component
public class SessionListener implements HttpSessionListener {

    private static final Logger logger = LoggerFactory.getLogger(SessionListener.class);

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        String strFormat1 = "★★★★★★★★★★★★★Session Created★★★★★★★★★★★★★\n"
                          + "          id: %s\n"
                          + "          create: %s\n"
                          + "★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★";
        String strFormat2 = "★★★★★★★ session created: %s create time: %s ★★★★★★★";
        String str1 = String.format(strFormat1, session.getId(), SDF.format(new Date()));
        String str2 = String.format(strFormat2, session.getId(), SDF.format(new Date()));
        System.out.println(str1);
        logger.info(str2);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        String strFormat1 = "★★★★★★★★★★★★★Session Destroyed★★★★★★★★★★★★★\n"
                + "          id: %s\n"
                + "          destroyed: %s\n"
                + "★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★";
        String strFormat2 = "★★★★★★★ session destroyed: %s destroyed time: %s ★★★★★★★";
        String str1 = String.format(strFormat1, session.getId(), SDF.format(new Date()));
        String str2 = String.format(strFormat2, session.getId(), SDF.format(new Date()));
        System.out.println(str1);
        logger.info(str2);
    }
}
