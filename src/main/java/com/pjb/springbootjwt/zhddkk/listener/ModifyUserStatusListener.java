package com.pjb.springbootjwt.zhddkk.listener;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import com.pjb.springbootjwt.zhddkk.websocket.ZhddWebSocket;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * tomcat启动后更新用户表ws_user 更新state字段为0
 * 
 * @author Administrator
 *
 */
@Component
public class ModifyUserStatusListener implements ServletContextListener, CommandLineRunner
{
	private WsUsersService wsUsersService;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("run.....");
		updateUser();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
	     WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext());  
	     wsUsersService = (WsUsersService) springContext.getBean("wsUsersServiceImpl");
	     //updateUser();
	}
	
	private void updateUser() {
		Map<String,ZhddWebSocket> clientsMap = ZhddWebSocket.getClients();
		List<WsUsersDO> dbUserList = wsUsersService.selectList(new EntityWrapper<WsUsersDO>().eq("state","1"));
		if (dbUserList != null && dbUserList.size() > 0) {
			for (WsUsersDO wu: dbUserList) {
				if (!clientsMap.containsKey(wu.getName())) {
					wu.setState("0");
					wsUsersService.updateById(wu);
				}
			}
		}
	}
}
