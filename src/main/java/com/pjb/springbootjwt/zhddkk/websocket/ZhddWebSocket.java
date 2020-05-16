package com.pjb.springbootjwt.zhddkk.websocket;

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

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.zhddkk.domain.WsChatlogDO;
import com.pjb.springbootjwt.zhddkk.domain.WsCommonDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import com.pjb.springbootjwt.zhddkk.service.WsChatlogService;
import com.pjb.springbootjwt.zhddkk.service.WsCommonService;
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import com.pjb.springbootjwt.zhddkk.util.JsonUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.pjb.springbootjwt.zhddkk.bean.ChatMessageBean;
import com.pjb.springbootjwt.zhddkk.listener.ApplicationContextRegister;
import org.springframework.stereotype.Component;

@ServerEndpoint("/zhddWebSocket/{user}/{pass}/{userAgent}")
@Component
public class ZhddWebSocket 
{
	private static final Logger logger = LoggerFactory.getLogger(ZhddWebSocket.class);
	
	private static Map<String,ZhddWebSocket> clients = new ConcurrentHashMap<String,ZhddWebSocket>();
		
	private Session session;
	
	private String user;
	
	private String pass;
	
	private String userAgent;
	
	private long loginTimes;
	
	private static int onLineCount = 0;

	private static WsUsersService wsUsersService;

	private static WsChatlogService wsChatlogService;

	private static WsCommonService wsCommonService;

	static {
		ApplicationContext act = ApplicationContextRegister.getApplicationContext();
        wsUsersService = act.getBean(WsUsersService.class);
        wsChatlogService = act.getBean(WsChatlogService.class);
		wsCommonService = act.getBean(WsCommonService.class);
	}
	
	@OnMessage
	public void onMessage(String message, Session session) throws IOException, InterruptedException {
		if (this.user == null || this.session==null) {
			return;
		}

		JSONObject jsonObject = JsonUtil.jsonstr2Jsonobject(message);
		//time:2019-01-03 16:00:05;typeId:1;typeDesc:系统消息;from:admin;to:无名1;msg:hello
		//1:系统消息  2:在线消息 3:离线消息  4:广告消息
		SimpleDateFormat sdfx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		if (message.contains("msg")) {
			String msgFrom = jsonObject.getString("from");
			String msgTo = jsonObject.getString("to");
			String typeId = jsonObject.getString("typeId");
			String typeDesc = jsonObject.getString("typeDesc");

			String msgStr = null;
			try {
				msgStr = jsonObject.getString("msg");
			} catch (Exception e) {
				// 如果消息为空 直接退出
				return;
			}

			//对消息进行敏感字、脏话进行处理
			String msg = msgStr;
			List<WsCommonDO> mgcList = wsCommonService.selectList(new EntityWrapper<WsCommonDO>()
				.eq("type", "mgc"));

			List<WsCommonDO> zhList = wsCommonService.selectList(new EntityWrapper<WsCommonDO>()
					.eq("type", "zh"));
			List<WsCommonDO> allList = new ArrayList<WsCommonDO>();
			allList.addAll(mgcList);
			allList.addAll(zhList);

			for (WsCommonDO wc : allList) {
				msg = msg.replaceAll(wc.getName(), "***");
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String curTime = sdf.format(new Date());

			if (typeId.equals("1")) {
				// 如果是系统消息
				ChatMessageBean chatBean = new ChatMessageBean(curTime, "1", "系统消息", msgFrom, msgTo, msg);
				Session fromSession = querySession(msgFrom);
				fromSession.getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
			} else {
				// 如果用户不在线
				Session toSession = querySession(msgTo);
				if (null == toSession) {
					Session fromSession = querySession(msgFrom);
					ChatMessageBean chatBean = new ChatMessageBean(curTime, "3", "离线消息", msgFrom, msgTo, msg);
					fromSession.getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
				} else {
					ChatMessageBean chatBean = new ChatMessageBean(curTime, "2", "在线消息", msgFrom, msgTo, msg);
					toSession.getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
				}

				// 给admin发消息
				Session adminSession = querySession("admin");
				if (null != adminSession) {
					ChatMessageBean chatBean = new ChatMessageBean(curTime, "2", "在线消息", msgFrom, msgTo, msg);
					adminSession.getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
				}

				WsChatlogDO wcl_1 = new WsChatlogDO();
				wcl_1.setTime(sdfx.format(new Date()));
				wcl_1.setUser(msgFrom);
				wcl_1.setToUser(msgTo);
				wcl_1.setMsg(msgStr);
				wsChatlogService.insert(wcl_1);
			}
		}
	}
	
	@javax.websocket.OnOpen
	public void OnOpen(@PathParam("user") String user,@PathParam("pass") String pass,@PathParam("userAgent") String userAgent,Session session) {
        logger.info("用户连接 user:{}", user);
		WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user).eq("password", pass));
		if (null == wsUsersDO) {
			System.out.println("用户:"+user+"登录失败!");
			logger.info("用户{}不存在或者密码错误", user);
			this.session = null;
			this.user = null;
			this.pass = null;
			this.userAgent = null;
			OnClose();
			return;
		}

		this.session = session;
		this.user = user;
		this.userAgent = userAgent;
		this.loginTimes = System.currentTimeMillis();
		addOnLineCount();
		clients.put(user, this);

		SimpleDateFormat sdfx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		WsChatlogDO loginLog = new WsChatlogDO();
		loginLog.setTime(sdfx.format(new Date()));
		loginLog.setUser(this.user);
		loginLog.setToUser("");
		loginLog.setMsg("登录成功");
		loginLog.setRemark(this.userAgent);
		wsChatlogService.insert(loginLog);
		
		String curTime = sdfx.format(new Date());
		String msg = user + "已上线";
		logger.debug(msg);
		for (Entry<String,ZhddWebSocket> entry : clients.entrySet()) {
			if (!entry.getKey().equals(user)) {
				try {
					ChatMessageBean chatBean = new ChatMessageBean(curTime,"1","系统消息","admin",entry.getKey(),msg);
					entry.getValue().getSession().getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		// 更新用户状态以及登录时间
        wsUsersDO.setName(this.user);
        wsUsersDO.setLastLoginTime(sdfx.format(new Date()));
        wsUsersDO.setState("1");
        wsUsersService.updateById(wsUsersDO);
		
		//获取离线日志
        List<WsChatlogDO> chatLogHistoryList = wsChatlogService.selectList(new EntityWrapper<WsChatlogDO>().eq("to_user", user)
                .gt("time", wsUsersDO.getLastLogoutTime()));
		if (null != chatLogHistoryList && chatLogHistoryList.size() > 0) {
			for (WsChatlogDO wcl : chatLogHistoryList) {
				String time = wcl.getTime();
				String sendmsg = wcl.getUser()+"-->我 " + wcl.getMsg();
				ChatMessageBean chatBean = new ChatMessageBean(time,"1","系统消息","admin",user,sendmsg);
				try {
					this.session.getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
					Thread.sleep(50);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@javax.websocket.OnClose
	public void OnClose() {
		subOnLineCount();
		clients.remove(this.user);
		SimpleDateFormat sdfx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String curTime = sdfx.format(new Date());
		String msg =  user + "已下线!";
		logger.debug(msg);
		for (Entry<String,ZhddWebSocket> entry : clients.entrySet()) {
			if (!entry.getKey().equals(user)) {
				try {
					ChatMessageBean chatBean = new ChatMessageBean(curTime,"1","系统消息","admin",entry.getKey(),msg);
					entry.getValue().getSession().getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		// 修改在线状态为离线
		WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", this.user));
		if (null != wsUsersDO) {
            wsUsersDO.setState("0");
            wsUsersDO.setLastLogoutTime(sdfx.format(new Date()));
            wsUsersService.updateById(wsUsersDO);

            // 记录登出日志
            WsChatlogDO logoutLog = new WsChatlogDO();
            logoutLog.setTime(sdfx.format(new Date()));
            logoutLog.setUser(this.user);
            logoutLog.setToUser("");
            logoutLog.setMsg("退出服务器");
            wsChatlogService.insert(logoutLog);
        }
	}
	
	@OnError
	public void onError(Throwable throwable) {
		System.out.println(this.user+"连接异常:" + throwable.getMessage());
		logger.info("用户{}连接异常",this.user);
		clients.remove(this.user);
	}
	
	public static synchronized void addOnLineCount() {
		ZhddWebSocket.onLineCount++;
	}
	
	public static synchronized void subOnLineCount(){
		ZhddWebSocket.onLineCount--;
	}
	
	public synchronized Session getSession() {
		return this.session;
	}
	
	public synchronized long getLoginTimes() {
		return this.loginTimes;
	}
	
	public static synchronized int getOnLineCount() {
		return ZhddWebSocket.onLineCount;
	}
	
	public static synchronized Map<String, ZhddWebSocket> getClients() {
		return ZhddWebSocket.clients;
	}
	
	public static Session querySession(String username) {
		Session session = null;
		for (Entry<String, ZhddWebSocket> entry : clients.entrySet()) {
			if (entry.getKey().equals(username)) {
				session = entry.getValue().getSession();
				break;
			}
		}

		return session;
	}
}
