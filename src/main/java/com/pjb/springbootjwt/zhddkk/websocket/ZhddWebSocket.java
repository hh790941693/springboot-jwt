package com.pjb.springbootjwt.zhddkk.websocket;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.zhddkk.bean.ChatMessageBean;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.domain.WsChatlogDO;
import com.pjb.springbootjwt.zhddkk.domain.WsCommonDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import com.pjb.springbootjwt.zhddkk.service.WsChatlogService;
import com.pjb.springbootjwt.zhddkk.service.WsCommonService;
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import com.pjb.springbootjwt.zhddkk.util.JsonUtil;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@ServerEndpoint(value = "/zhddWebSocket/{user}/{pass}/{userAgent}", configurator = HttpSessionConfigurator.class)
@Component
public class ZhddWebSocket {
    private static final Logger logger = LoggerFactory.getLogger(ZhddWebSocket.class);

    private static Map<String, ZhddWebSocket> clients = new ConcurrentHashMap<String, ZhddWebSocket>();

    private Session session;

    private String user;

    private String pass;

    private String userAgent;

    private long loginTimes;

    private static int onLineCount = 0;

    public static WsUsersService wsUsersService;

    public static WsChatlogService wsChatlogService;

    public static WsCommonService wsCommonService;

    /**
     * 接受消息.
     * @param message 消息
     * @param session 会话
     * @throws IOException 异常
     * @throws InterruptedException 异常
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException, InterruptedException {
        if (StringUtils.isBlank(message) || this.user == null || this.session == null) {
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
                Session adminSession = querySession(CommonConstants.ADMIN_USER);
                if (null != adminSession) {
                    ChatMessageBean chatBean = new ChatMessageBean(curTime, "2", "在线消息", msgFrom, msgTo, msg);
                    adminSession.getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
                }

                WsChatlogDO wcl1 = new WsChatlogDO(sdfx.format(new Date()), msgFrom, msgTo, msgStr,"");
                wsChatlogService.insert(wcl1);
            }
        }
    }

    /**
     * 连接服务器.
     * @param user 用户名
     * @param pass 密码
     * @param userAgent 浏览器类型
     * @param session 会话
     * @param endpointConfig endpointConfig
     */
    @javax.websocket.OnOpen
    public void OnOpen(@PathParam("user") String user,
                       @PathParam("pass") String pass,
                       @PathParam("userAgent") String userAgent,
                       Session session, EndpointConfig endpointConfig) {
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user).eq("password", pass));
        if (null == wsUsersDO) {
            logger.info("用户{}不存在或者密码错误", user);
            this.session = null;
            this.user = null;
            this.pass = null;
            this.userAgent = null;
            OnClose();
            return;
        }

        logger.info("用户:{} 已连接上服务器", user);
        this.session = session;
        this.user = user;
        this.userAgent = userAgent;
        this.loginTimes = System.currentTimeMillis();
        addOnLineCount();
        clients.put(user, this);

        SimpleDateFormat sdfx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        WsChatlogDO loginLog = new WsChatlogDO(sdfx.format(new Date()), this.user, "", "登录成功", this.userAgent);
        wsChatlogService.insert(loginLog);

        logger.debug(user + "已上线");
        for (Entry<String, ZhddWebSocket> entry : clients.entrySet()) {
            if (!entry.getKey().equals(user)) {
                try {
                    ChatMessageBean chatBean = new ChatMessageBean(sdfx.format(new Date()), "1", "系统消息",
                            CommonConstants.ADMIN_USER, entry.getKey(), user + "已上线");
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
                String sendmsg = wcl.getUser() + "-->我 " + wcl.getMsg();
                ChatMessageBean chatBean = new ChatMessageBean(time, "1", "系统消息", CommonConstants.ADMIN_USER, user, sendmsg);
                try {
                    this.session.getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
                    Thread.sleep(50);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 关闭.
     */
    @javax.websocket.OnClose
    public void OnClose() {
        logger.info("用户:{} 已断开服务器", this.user);
        subOnLineCount();
        clients.remove(this.user);
        SimpleDateFormat sdfx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String curTime = sdfx.format(new Date());
        String msg = user + "已下线!";
        logger.debug(msg);
        for (Entry<String, ZhddWebSocket> entry : clients.entrySet()) {
            if (!entry.getKey().equals(user)) {
                try {
                    ChatMessageBean chatBean = new ChatMessageBean(curTime, "1", "系统消息",
                            CommonConstants.ADMIN_USER, entry.getKey(), msg);
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
            WsChatlogDO logoutLog = new WsChatlogDO(sdfx.format(new Date()), this.user, "", "退出服务器", "");
            wsChatlogService.insert(logoutLog);
        }
    }

    /**
     * 连接异常.
     */
    @OnError
    public void onError(Throwable throwable) {
        logger.info("用户{}: 连接服务器异常", this.user);
        clients.remove(this.user);
    }

    public static synchronized void addOnLineCount() {
        ZhddWebSocket.onLineCount++;
    }

    public static synchronized void subOnLineCount() {
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

    /**
     * 查询会话信息.
     * @param username 用户id
     * @return 会话信息
     */
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
