package com.pjb.springbootjwt.zhddkk.cmdlinerunner;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import com.pjb.springbootjwt.zhddkk.websocket.ZhddWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.List;
import java.util.Map;

/**
 * tomcat启动后更新用户表ws_user 更新state字段为0.
 * 
 * @author Administrator
 *
 */
@Component
public class UpdateUserStatusRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(UpdateUserStatusRunner.class);

    @Autowired
    private WsUsersService wsUsersService;

    @Override
    public void run(String... args) {
        logger.info("进入了UpdateUserStatusRunner");
        updateUser();
    }
    
    private void updateUser() {
        logger.info("开始检查在线用户状态");
        Map<String, Map<String, Session>> clientsMap = ZhddWebSocket.getClientsMap();
        for (Map.Entry<String, Map<String, Session>> entry : clientsMap.entrySet()) {
            logger.info("房间号:{},用户数:{}", entry.getKey(), entry.getValue().size());
        }

        List<WsUsersDO> dbUserList = wsUsersService.selectList(new EntityWrapper<WsUsersDO>().eq("state", "1"));
        logger.info("数据库在线人数:" + dbUserList.size());
        for (WsUsersDO wsUsersDO : dbUserList) {
            boolean isUserOnline = false;
            for (Map.Entry<String, Map<String, Session>> outEntry : clientsMap.entrySet()) {
                for (Map.Entry<String, Session> innerEntry : outEntry.getValue().entrySet()) {
                    if (innerEntry.getKey().equals(wsUsersDO.getName())) {
                        isUserOnline = true;
                        break;
                    }
                }
            }
            if (!isUserOnline && !wsUsersDO.getState().equals("0")) {
                wsUsersDO.setState("0");
                wsUsersService.updateById(wsUsersDO);
            }
        }
    }
}
