package com.jscxrz.zhddkk.websocket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.jscxrz.zhddkk.bean.ChatMessageBean;
import com.jscxrz.zhddkk.entity.WsChatlog;
import com.jscxrz.zhddkk.entity.WsCommon;
import com.jscxrz.zhddkk.entity.WsUser;
import com.jscxrz.zhddkk.listener.ApplicationContextRegister;
import com.jscxrz.zhddkk.service.WsService;

@ServerEndpoint("/zhddWebSocket/{user}/{pass}/{userAgent}")
public class ZhddWebSocket 
{
	private static final Log logger = LogFactory.getLog(ZhddWebSocket.class);
	
	private static Map<String,ZhddWebSocket> clients = new ConcurrentHashMap<String,ZhddWebSocket>();
		
	private Session session;
	
	private String user;
	
	private String pass;
	
	private String userAgent;
	
	private long loginTimes;
	
	private static int onLineCount = 0;
	
	@OnMessage
	public void onMessage(String message, Session session) throws IOException, InterruptedException
	{
		if (this.user == null || this.session==null) {
			return;
		}
		
		//time:2019-01-03 16:00:05;typeId:1;typeDesc:系统消息;from:admin;to:无名1;msg:hello
		//1:系统消息  2:在线消息 3:离线消息  4:广告消息
		SimpleDateFormat sdfx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		if (message.contains("msg:"))
		{
			String msgFrom = message.split("from:")[1].split(";")[0].trim();
			String msgTo = message.split("to:")[1].split(";")[0].trim();
			String typeId = message.split("typeId:")[1].split(";")[0].trim();
			//String typeDesc = message.split("typeDesc:")[1].split(";")[0].trim();
			
			String msgStr = null;
			try
			{
				msgStr = message.split("msg:")[1].trim();
			}
			catch (Exception e)
			{
				// 如果消息为空 直接退出
				return;
			}
			
			WsService wsService = getService();
			
			//对消息进行敏感字、脏话进行处理
			String msg = msgStr;
			WsCommon wsMgc = new WsCommon();
			wsMgc.setType("mgc");
			List<WsCommon> mgcList = wsService.queryCommon(wsMgc);
			WsCommon wsZh = new WsCommon();
			wsZh.setType("zh");
			List<WsCommon> zhList = wsService.queryCommon(wsZh);
			List<WsCommon> allList = new ArrayList<WsCommon>();
			allList.addAll(mgcList);
			allList.addAll(zhList);
			
			for (WsCommon wc : allList)
			{
				msg = msg.replaceAll(wc.getName(), "***");
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String curTime = sdf.format(new Date());
			
			if (typeId.equals("1"))
			{
				// 如果是系统消息
				ChatMessageBean chatBean = new ChatMessageBean(curTime,"1","系统消息",msgFrom,msgTo, msg);
				Session fromSession = querySession(msgFrom);
				fromSession.getBasicRemote().sendText(chatBean.toString());
			}
			else
			{
				// 如果用户不在线
				Session toSession = querySession(msgTo);
				if (null == toSession)
				{
					Session fromSession = querySession(msgFrom);
					ChatMessageBean chatBean = new ChatMessageBean(curTime,"3","离线消息",msgFrom,msgTo,msg);
					fromSession.getBasicRemote().sendText(chatBean.toString());
				}
				else
				{
					ChatMessageBean chatBean = new ChatMessageBean(curTime,"2","在线消息",msgFrom,msgTo,msg);
					toSession.getBasicRemote().sendText(chatBean.toString());
				}
				
				// 给admin发消息
				Session adminSession = querySession("admin");
				if (null != adminSession)
				{
					ChatMessageBean chatBean = new ChatMessageBean(curTime,"2","在线消息",msgFrom,msgTo,msg);
					adminSession.getBasicRemote().sendText(chatBean.toString());
				}
				
				WsChatlog wcl_1 = new WsChatlog();
				wcl_1.setTime(sdfx.format(new Date()));
				wcl_1.setUser(msgFrom);
				wcl_1.setToUser(msgTo);
				wcl_1.setMsg(msgStr);
				wsService.insertChatlog(wcl_1);
			}
		}
	}
	
	@javax.websocket.OnOpen
	public void OnOpen(@PathParam("user") String user,@PathParam("pass") String pass,@PathParam("userAgent") String userAgent,Session session)
	{
		WsUser wu = new WsUser();
		wu.setName(user);
		wu.setPassword(pass);
		WsService wsService = getService();
		List<WsUser> userList = wsService.queryWsUser(wu);
		if (null == userList || userList.size()==0) {
			System.out.println("用户:"+user+"登录失败!");
			this.session = null;
			this.user = null;
			this.pass = null;
			this.userAgent = null;
			OnClose();
			return;
		}
		
		this.pass = pass;
		//校验密码是否正确
		String dbPass = userList.get(0).getPassword();
		if (!this.pass.equals(dbPass)) {
			System.out.println("非法的用户连接socket!");
			OnClose();
		}
		
		this.session = session;
		this.user = user;
		this.userAgent = userAgent;
		this.loginTimes = System.currentTimeMillis();
		addOnLineCount();
		clients.put(user, this);

		SimpleDateFormat sdfx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		WsChatlog loginLog = new WsChatlog();
		loginLog.setTime(sdfx.format(new Date()));
		loginLog.setUser(this.user);
		loginLog.setToUser("");
		loginLog.setMsg("登录成功");
		loginLog.setRemark(this.userAgent);
		WsService ser = getService();
		ser.insertChatlog(loginLog);
		
		String curTime = sdfx.format(new Date());
		String msg = user + "已上线";
		logger.debug(msg);
		for (Entry<String,ZhddWebSocket> entry : clients.entrySet())
		{
			if (!entry.getKey().equals(user))
			{
				try {
					ChatMessageBean chatBean = new ChatMessageBean(curTime,"1","系统消息","admin",entry.getKey(),msg);
					entry.getValue().getSession().getBasicRemote().sendText(chatBean.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		// 更新用户状态以及登录时间
		WsUser updateWu = new WsUser();
		updateWu.setName(this.user);
		updateWu.setLastLoginTime(sdfx.format(new Date()));
		updateWu.setState("1"); 
		ser.updateWsUser(updateWu);
		
		//获取离线日志
		List<WsChatlog> historyLogs = ser.queryHistoryChatlog(user);
		if (null != historyLogs && historyLogs.size() > 0)
		{
			for (WsChatlog wcl : historyLogs)
			{
				String time = wcl.getTime();
				String sendmsg = wcl.getUser()+"-->我 " + wcl.getMsg();
				ChatMessageBean chatBean = new ChatMessageBean(time,"1","系统消息","admin",user,sendmsg);
				try 
				{
					this.session.getBasicRemote().sendText(chatBean.toString());
					Thread.sleep(50);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@javax.websocket.OnClose
	public void OnClose()
	{
		subOnLineCount();
		clients.remove(this.user);
		//System.out.println("用户" + user + "中断连接!");

		SimpleDateFormat sdfx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String curTime = sdfx.format(new Date());
		String msg =  user + "已下线!";
		logger.debug(msg);
		for (Entry<String,ZhddWebSocket> entry : clients.entrySet())
		{
			if (!entry.getKey().equals(user))
			{
				try {
					ChatMessageBean chatBean = new ChatMessageBean(curTime,"1","系统消息","admin",entry.getKey(),msg);
					entry.getValue().getSession().getBasicRemote().sendText(chatBean.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		// 修改在线状态为离线
		WsService ser = getService();  
		WsUser wu = new WsUser();
		wu.setName(this.user);
		wu.setState("0");
		wu.setLastLogoutTime(sdfx.format(new Date()));
		ser.updateWsUser(wu);
		
		// 记录登出日志
		WsChatlog loginLog = new WsChatlog();
		loginLog.setTime(sdfx.format(new Date()));
		loginLog.setUser(this.user);
		loginLog.setToUser("");
		loginLog.setMsg("退出服务器");
		ser.insertChatlog(loginLog);
	}
	
	@OnError
	public void onError(Throwable throwable)
	{
		System.out.println(this.user+"连接异常:" + throwable.getMessage());
		clients.remove(this.user);
	}
	
	public static synchronized void addOnLineCount()
	{
		ZhddWebSocket.onLineCount++;
	}
	
	public static synchronized void subOnLineCount(){
		ZhddWebSocket.onLineCount--;
	}
	
	public synchronized Session getSession()
	{
		return this.session;
	}
	
	public synchronized long getLoginTimes()
	{
		return this.loginTimes;
	}
	
	public static synchronized int getOnLineCount()
	{
		return ZhddWebSocket.onLineCount;
	}
	
	public static synchronized Map<String, ZhddWebSocket> getClients()
	{
		return ZhddWebSocket.clients;
	}
	
	public static Session querySession(String username)
	{
		Session session = null;
		for (Entry<String, ZhddWebSocket> entry : clients.entrySet())
		{
			if (entry.getKey().equals(username))
			{
				session = entry.getValue().getSession();
				break;
			}
		}

		return session;
	}
	
	private static WsService getService()
	{
		ApplicationContext act = ApplicationContextRegister.getApplicationContext();  
		WsService ser =act.getBean(WsService.class);
		return ser;
	}
}
