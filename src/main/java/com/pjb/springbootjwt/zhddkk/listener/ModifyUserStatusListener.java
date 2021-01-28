package com.pjb.springbootjwt.zhddkk.listener;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import com.pjb.springbootjwt.zhddkk.websocket.ZhddWebSocket;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * tomcat启动后更新用户表ws_user 更新state字段为0.
 * 
 * @author Administrator
 *
 */
@Component
public class ModifyUserStatusListener implements ServletContextListener, CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ModifyUserStatusListener.class);

    private WsUsersService wsUsersService;

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    @Override
    public void run(String... args) {
        logger.info("进入ModifyUserStatusListener");
        updateUser();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext());
        wsUsersService = (WsUsersService) springContext.getBean("wsUsersServiceImpl");
    }

    private void updateUser() {
        logger.info("开始检查在线用户状态");
        Map<String, Map<String, ZhddWebSocket>> clientsMap = ZhddWebSocket.getClientsMap();
        logger.info("当前在线用户数:" + clientsMap.size());
        List<WsUsersDO> dbUserList = wsUsersService.selectList(new EntityWrapper<WsUsersDO>().eq("state", "1"));
        logger.info("数据库在线人数:" + dbUserList.size());
        for (WsUsersDO wsUsersDO : dbUserList) {
            boolean userOnline = false;
            for (Map.Entry<String, Map<String, ZhddWebSocket>> outEntry : clientsMap.entrySet()) {
                for (Map.Entry<String, ZhddWebSocket> innerEntry : outEntry.getValue().entrySet()) {
                    if (innerEntry.getValue().getUser().equals(wsUsersDO.getName())) {
                        userOnline = true;
                        break;
                    }
                }
            }
            if (!userOnline) {
                wsUsersDO.setState("0");
                wsUsersService.updateById(wsUsersDO);
            }
        }
    }
}
