package com.pjb.springbootjwt.zhddkk.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志监听器.
 * 动态注入web项目的安装路径
 */
public class ZhddLog4jListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(ZhddLog4jListener.class);

    public static final String log4jDirKey = "webAppRootPath";

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        System.getProperties().remove(log4jDirKey);
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        String contextPath = arg0.getServletContext().getRealPath("/");
        logger.debug("contextPath:" + contextPath);
        System.setProperty(log4jDirKey, contextPath);
    }
}
