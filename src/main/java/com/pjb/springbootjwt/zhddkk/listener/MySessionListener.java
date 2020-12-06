package com.pjb.springbootjwt.zhddkk.listener;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Component
public class MySessionListener implements HttpSessionListener{

	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		System.out.println("session created........id:"+session.getId()+" createTime:"+SDF.format(new Date()));
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        System.out.println("session destroyed........id:"+session.getId()+" createTime:"+SDF.format(new Date()));
	}
}
