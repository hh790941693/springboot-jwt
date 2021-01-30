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
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
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

@ServerEndpoint(value = "/zhddWebSocket/{roomName}/{user}/{pass}/{userAgent}", configurator = HttpSessionConfigurator.class)
@Component
public class ZhddWebSocket {
    private static final Logger logger = LoggerFactory.getLogger(ZhddWebSocket.class);

    private static final SimpleDateFormat SDF_STANDARD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final SimpleDateFormat SDF_HHMMSS = new SimpleDateFormat("HH:mm:ss");

    // <房间号, <用戶名,用户session>>
    private static Map<String, Map<String, Session>> clientsMap = new ConcurrentHashMap();

    private Session session;

    private String roomName;

    private String user;

    private String pass;

    private String userAgent;

    private static Map<String, Integer> onlineCountMap = new ConcurrentHashMap();

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

        if (message.contains("msg")) {
            String roomName = jsonObject.getString("roomName");
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

            String curTime = SDF_HHMMSS.format(new Date());
            if (typeId.equals("1")) {
                // 如果是系统消息
                ChatMessageBean chatBean = new ChatMessageBean(curTime, "1", "系统消息", msgFrom, msgTo, msg);
                Session fromSession = queryRoomSession(roomName, msgFrom);
                if (null != fromSession) {
                    fromSession.getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
                }
            } else {
                // 如果用户不在线
                Map<String, Session> roomClientMap = getRoomClientsMap(roomName);
                for (Entry<String, Session> entry : roomClientMap.entrySet()) {
                    ChatMessageBean chatBean = new ChatMessageBean(curTime, "2", "在线消息", msgFrom, msgTo, msg);
                    entry.getValue().getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
                }

                WsChatlogDO wcl1 = new WsChatlogDO(curTime, roomName, msgFrom, msgTo, msgStr,"");
                wsChatlogService.insert(wcl1);
            }
        }
    }

    /**
     * 进入聊天室.
     * @param roomName 房间名称
     * @param user 用户名
     * @param pass 密码
     * @param userAgent 浏览器类型
     * @param session 会话
     * @param endpointConfig endpointConfig
     */
    @javax.websocket.OnOpen
    public void OnOpen(@PathParam("roomName") String roomName,
                       @PathParam("user") String user,
                       @PathParam("pass") String pass,
                       @PathParam("userAgent") String userAgent,
                       Session session, EndpointConfig endpointConfig) {
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user).eq("password", pass));
        if (null == wsUsersDO) {
            logger.info("用户{}不存在或者密码错误", user);
            this.session = null;
            this.roomName = null;
            this.user = null;
            this.pass = null;
            this.userAgent = null;
            OnClose();
            return;
        }
        this.session = session;
        this.roomName = roomName;
        this.user = user;
        this.pass = pass;
        this.userAgent = userAgent;

        String enterMsgFormat = "%s 进入了聊天室：%s";
        String enterMsg = String.format(enterMsgFormat, this.user, this.roomName);
        logger.info(enterMsg);

        // 更新房间人员信息
        addRoomUser(roomName, user);

        // 更新房间在线人数
        addOnLineCount(roomName);

        // 登录日志
        WsChatlogDO loginLog = new WsChatlogDO(SDF_STANDARD.format(new Date()), roomName, this.user, "", "进入了聊天室", this.userAgent);
        wsChatlogService.insert(loginLog);

        // 广播消息
        Map<String, Session> roomClientMap = ZhddWebSocket.getRoomClientsMap(roomName);
        for (Entry<String, Session> entry : roomClientMap.entrySet()) {
            if (!entry.getKey().equals(this.user)) {
                try {
                    ChatMessageBean chatBean = new ChatMessageBean(SDF_HHMMSS.format(new Date()), "1", "系统消息",
                            "", "", enterMsg);
                    entry.getValue().getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //获取离线日志
        List<WsChatlogDO> chatLogHistoryList = wsChatlogService.selectList(new EntityWrapper<WsChatlogDO>().eq("room_name", roomName)
                .eq("to_user", user)
                .gt("time", wsUsersDO.getLastLogoutTime()));
        if (null != chatLogHistoryList && chatLogHistoryList.size() > 0) {
            for (WsChatlogDO wcl : chatLogHistoryList) {
                String time = wcl.getTime();
                if (time.length() >= 18) {
                    time = time.substring(11);
                }
                //String sendmsg = wcl.getUser() + "-->我 " + wcl.getMsg();
                String sendmsg = wcl.getMsg();
                ChatMessageBean chatBean = new ChatMessageBean(time, "1", "离线消息", wcl.getUser(), "我", sendmsg);
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
     * 聊天室关闭.
     */
    @javax.websocket.OnClose
    public void OnClose() {
        // 在线人数-1
        subOnLineCount(roomName);
        // 删除房间相关人信息
        removeRoomUser(roomName, this.user);

        String leaveMsgFormat = "%s 离开了聊天室：%s";
        String leaveMsg = String.format(leaveMsgFormat, this.user, this.roomName);
        logger.info(leaveMsg);

        // 广播消息
        Map<String, Session> roomClientMap = clientsMap.get(roomName);
        for (Entry<String, Session> entry : roomClientMap.entrySet()) {
            if (!entry.getKey().equals(user)) {
                try {
                    ChatMessageBean chatBean = new ChatMessageBean(SDF_HHMMSS.format(new Date()), "1", "系统消息",
                            "", "", leaveMsg);
                    entry.getValue().getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 记录登出日志
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", this.user));
        if (null != wsUsersDO) {
            // 记录登出日志
            WsChatlogDO logoutLog = new WsChatlogDO(SDF_STANDARD.format(new Date()), roomName, this.user, "", "离开了聊天室", "");
            wsChatlogService.insert(logoutLog);
        }
    }

    /**
     * 连接聊天室异常.
     */
    @OnError
    public void onError(Throwable throwable) {
        logger.info("用户{}: 进入聊天室:{} 失败", this.user, this.roomName);
        removeRoomUser(roomName, this.user);
    }

    public static synchronized void addOnLineCount(String roomName) {
        if (onlineCountMap.containsKey(roomName)) {
            int onlineCount = onlineCountMap.get(roomName);
            onlineCountMap.put(roomName, onlineCount + 1);
        } else {
            onlineCountMap.put(roomName, 1);
        }
    }

    public static synchronized void subOnLineCount(String roomName) {
        if (onlineCountMap.containsKey(roomName)) {
            int onlineCount = onlineCountMap.get(roomName);
            onlineCountMap.put(roomName, onlineCount - 1);
        } else {
            onlineCountMap.put(roomName, 0);
        }
    }

    public synchronized void addRoomUser(String roomName, String user) {
        if (clientsMap.containsKey(roomName)) {
            Map<String, Session> roomMap = clientsMap.get(roomName);
            roomMap.put(user, this.session);
        } else {
            Map<String, Session> userMap = new HashMap<>();
            userMap.put(user, this.session);
            clientsMap.put(roomName, userMap);
        }
    }

    public static synchronized void removeRoomUser(String roomName, String user) {
        if (clientsMap.containsKey(roomName)) {
            Map<String, Session> userMap = clientsMap.get(roomName);
            if (userMap.containsKey(user)) {
                userMap.remove(user);
                try {
                    userMap.get(user).close();
                } catch (Exception e) {
                    // do nothing
                }
            }
        }
    }

    public static void removeAllRoomUser(String user) {
        for (Entry<String, Map<String, Session>> entry : clientsMap.entrySet()) {
            Map<String, Session> roomMap = entry.getValue();
            if (roomMap.containsKey(user)) {
                roomMap.remove(user);
                subOnLineCount(entry.getKey());
                try {
                    roomMap.get(user).close();
                } catch (Exception e) {
                    continue;
                }
            }
        }
    }

    public static synchronized int getOnLineCount(String roomName) {
        int onlineCount = 0;
        if (onlineCountMap.containsKey(roomName)) {
            onlineCount = onlineCountMap.get(roomName);
        }
        return onlineCount;
    }

    public static synchronized Map<String, Session> getRoomClientsMap(String roomName) {
        if (clientsMap.containsKey(roomName)) {
            return clientsMap.get(roomName);
        }
        return new ConcurrentHashMap<String, Session>();
    }

    public static synchronized List<String> getRoomClientsList(String roomName) {
        List<String> roomUserList = new ArrayList<>();
        Map<String, Session> roomClientMap = getRoomClientsMap(roomName);
        for (Entry<String, Session> entry : roomClientMap.entrySet()) {
            roomUserList.add(entry.getKey());
        }
        return roomUserList;
    }

    public static Map<String, Map<String, Session>> getClientsMap() {
        return clientsMap;
    }

    public synchronized Session getSession() {
        return this.session;
    }

    /**
     * 查询会话信息.
     * @param username 用户id
     * @return 会话信息
     */
    public static Session queryRoomSession(String roomName, String username) {
        Map<String, Session> roomClientMap = clientsMap.get(roomName);
        if (null != roomClientMap) {
            if (roomClientMap.containsKey(username)) {
                return roomClientMap.get(username);
            }
        }
        return null;
    }
}
