package com.jscxrz.zhddkk.listener;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jscxrz.zhddkk.entity.WsUser;
import com.jscxrz.zhddkk.service.WsService;
import com.jscxrz.zhddkk.websocket.ZhddWebSocket;

/**
 * tomcat启动后更新用户表ws_user 更新state字段为0
 * 
 * @author Administrator
 *
 */
public class ModifyUserStatusListener implements ServletContextListener
{
	private WsService wsService;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
	     WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext());  
	     wsService = (WsService) springContext.getBean("wsServiceImpl"); 
	     updateUser();
	}
	
	private void updateUser() {
		Map<String,ZhddWebSocket> clientsMap = ZhddWebSocket.getClients();
		
		List<WsUser> dbUserList = wsService.queryWsUser(new WsUser());
		if (dbUserList != null && dbUserList.size() > 0)
		{
			for (WsUser wu: dbUserList)
			{
				if (wu.getState().equals("1"))
				{
					if (!clientsMap.containsKey(wu.getName()))
					{
						wu.setState("0");
						wsService.updateWsUser(wu);
					}
				}
			}
		}
	}

}
