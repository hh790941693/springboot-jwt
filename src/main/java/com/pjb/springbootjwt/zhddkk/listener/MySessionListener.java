package com.pjb.springbootjwt.zhddkk.listener;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;

/**
 * session监听器.
 */
@Component
public class MySessionListener implements HttpSessionListener {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        System.out.println("session created........id:" + session.getId() + " createTime:" + SDF.format(new Date()));
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        System.out.println("session destroyed........id:" + session.getId() + " createTime:" + SDF.format(new Date()));
    }
}
