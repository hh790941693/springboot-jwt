package com.pjb.springbootjwt.zhddkk.job;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import com.pjb.springbootjwt.zhddkk.websocket.ZhddWebSocket;
import java.util.List;
import java.util.Map;
import javax.websocket.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 定时检查用户状态.
 */
@Configuration
@EnableScheduling
public class CheckUserStatusJob {

    private static final Logger logger = LoggerFactory.getLogger(CheckUserStatusJob.class);

    @Autowired
    private WsUsersService wsUsersService;

    /**
     * 定时任务入口.
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void cronJob() {
        logger.info("[定时任务]定时检查用户的在线/离线状态");

        Map<String, Map<String, Session>> clientsMap = ZhddWebSocket.getClientsMap();
        logger.info("当前在线用户数:" + clientsMap.size());
        List<WsUsersDO> dbUserList = wsUsersService.selectList(new EntityWrapper<WsUsersDO>().eq("state", "1"));
        logger.info("数据库在线人数:" + dbUserList.size());
        for (WsUsersDO wsUsersDO : dbUserList) {
            boolean userOnline = false;
            for (Map.Entry<String, Map<String, Session>> outEntry : clientsMap.entrySet()) {
                for (Map.Entry<String, Session> innerEntry : outEntry.getValue().entrySet()) {
                    if (innerEntry.getKey().equals(wsUsersDO.getName())) {
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
