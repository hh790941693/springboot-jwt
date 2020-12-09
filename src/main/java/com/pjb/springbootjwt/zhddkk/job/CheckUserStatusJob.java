package com.pjb.springbootjwt.zhddkk.job;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import com.pjb.springbootjwt.zhddkk.websocket.ZhddWebSocket;
import java.util.List;
import java.util.Map;
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

        Map<String, ZhddWebSocket> clientsMap = ZhddWebSocket.getClients();
        logger.info("当前在线用户数:" + clientsMap.size());
        List<WsUsersDO> dbUserList = wsUsersService.selectList(new EntityWrapper<WsUsersDO>().eq("state", "1"));
        logger.info("数据库在线人数:" + dbUserList.size());

        if (clientsMap.size() > 0) {
            for (Map.Entry<String, ZhddWebSocket> entry : clientsMap.entrySet()) {
                WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", entry.getKey()));
                if (null != wsUsersDO && wsUsersDO.getState().equals("0")) {
                    logger.info("开始更新异常的用户状态为[在线],用户名:" + wsUsersDO.getName());
                    wsUsersDO.setState("1");
                    wsUsersService.updateById(wsUsersDO);
                }
            }
        }

        if (dbUserList.size() > 0) {
            for (WsUsersDO wsUsersDO : dbUserList) {
                if (!clientsMap.containsKey(wsUsersDO.getName())) {
                    logger.info("开始更新异常的用户状态为[离线],用户名:" + wsUsersDO.getName());
                    wsUsersDO.setState("0");
                    wsUsersService.updateById(wsUsersDO);
                }
            }
        }
    }
}
