package com.pjb.springbootjwt.zhddkk.websocket;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.zhddkk.bean.ChatMessageBean;
import com.pjb.springbootjwt.zhddkk.cache.CoreCache;
import com.pjb.springbootjwt.zhddkk.constants.ChatMsgTypeEnum;
import com.pjb.springbootjwt.zhddkk.domain.WsChatlogDO;
import com.pjb.springbootjwt.zhddkk.domain.WsCommonDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUserProfileDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import com.pjb.springbootjwt.zhddkk.service.WsChatlogService;
import com.pjb.springbootjwt.zhddkk.service.WsCommonService;
import com.pjb.springbootjwt.zhddkk.service.WsUserProfileService;
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import com.pjb.springbootjwt.zhddkk.util.JsonUtil;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.websocket.EndpointConfig;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.pjb.springbootjwt.zhddkk.util.UnicodeUtil;
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
    private static final Map<String, Map<String, Session>> clientsMap = new ConcurrentHashMap();

    // <房间号, List<用户名>>
    private static final  Map<String, List<String>> roomInputingUserMap = new ConcurrentHashMap();

    private Session session;

    // 房间号
    private String roomName;

    // 用户
    private String user;

    // 密码
    private String pass;

    // 客户端类型
    private String userAgent;

    // 通过WebSocketConfig注入
    public static WsUsersService wsUsersService;

    public static WsChatlogService wsChatlogService;

    public static WsCommonService wsCommonService;

    public static WsUserProfileService wsUserProfileService;

    /**
     * 转发消息.
     * @param message 消息 {"roomName":"002","from":"admin","to":"无名1","msgTypeId":2,"msgTypeDesc":"在线消息","msg":"hello"}
     * @param session 会话  msgTypeId: 1:广播消息  2:在线消息  4.通知消息  5.状态消息
     * @throws IOException 异常
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        if (StringUtils.isBlank(message) || this.user == null || this.session == null) {
            return;
        }

        if (!message.contains("msg")) {
            return;
        }

        JSONObject jsonObject = JsonUtil.jsonstr2Jsonobject(message);
        String roomName = jsonObject.getString("roomName");
        String msgFrom = jsonObject.getString("from");
        String msgTo = jsonObject.getString("to");
        String msgTypeId = jsonObject.getString("msgTypeId");
        //String msgTypeDesc = jsonObject.getString("msgTypeDesc");
        String msgStr = jsonObject.getString("msg");

        //对消息进行敏感字、脏话进行处理
        String msg = msgStr;
        List<WsCommonDO> commonList = CoreCache.getInstance().getCommonList();
        if (null == commonList) {
            commonList = wsCommonService.selectList(null);
        }
        List<WsCommonDO> allList = commonList.stream().filter(c->c.getType().equals("mgc") || c.getType().equals("zh")).collect(Collectors.toList());
        for (WsCommonDO wc : allList) {
            msg = msg.replaceAll(wc.getName(), "***");
        }

        String curTime = SDF_HHMMSS.format(new Date());
        if (msgTypeId.equals(ChatMsgTypeEnum.SYSTEM_MSG.getMsgTypeId())) {
            // 系统消息(暂时没用到)
            ChatMessageBean chatBean = new ChatMessageBean(curTime, msgTypeId, ChatMsgTypeEnum.SYSTEM_MSG.getMsgTypeDesc(), msgFrom, msgTo, msg, new HashMap<>());
            session.getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
        } else if (msgTypeId.equals(ChatMsgTypeEnum.CHAT_ONLINE_MSG.getMsgTypeId())){
            // 聊天消息(群发)
            // 发送方信息
            WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", msgFrom));
            if (null != wsUsersDO) {
                // 禁用
                if (wsUsersDO.getEnable().equals("0")) {
                    ChatMessageBean chatBean = new ChatMessageBean(curTime, ChatMsgTypeEnum.SYSTEM_MSG.getMsgTypeId(), ChatMsgTypeEnum.SYSTEM_MSG.getMsgTypeDesc(), "", "", "你的账号已被禁用了!", new HashMap<>());
                    session.getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
                    return;
                }
                // 禁言
                if (wsUsersDO.getSpeak().equals("0")) {
                    ChatMessageBean chatBean = new ChatMessageBean(curTime, ChatMsgTypeEnum.SYSTEM_MSG.getMsgTypeId(), ChatMsgTypeEnum.SYSTEM_MSG.getMsgTypeDesc(), "", "", "你已被禁言了!", new HashMap<>());
                    session.getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
                    return;
                }
            }
            Map<String, Object> extendMap = new HashMap<>();
            WsUserProfileDO wsUserProfileDO = CoreCache.getInstance().getUserProfile(msgFrom);
            if (null == wsUserProfileDO) {
                List<WsUserProfileDO> userProfileList = wsUserProfileService.selectList(null);
                for (WsUserProfileDO wupd : userProfileList) {
                    if (wupd.getUserName().equals(msgFrom)) {
                        wsUserProfileDO = wupd;
                        break;
                    }
                }
            }
            extendMap.put("userProfile", wsUserProfileDO);
            ChatMessageBean chatBean = new ChatMessageBean(curTime, msgTypeId, ChatMsgTypeEnum.CHAT_ONLINE_MSG.getMsgTypeDesc(), msgFrom, msgTo, msg, extendMap);
            sendToAll(roomName, chatBean);

            // 记录聊天日志
            WsChatlogDO wsChatlogDO = new WsChatlogDO(SDF_STANDARD.format(new Date()), roomName, msgFrom, msgTo, UnicodeUtil.string2Unicode(msgStr),"");
            wsChatlogService.insert(wsChatlogDO);
        } else if (msgTypeId.equals(ChatMsgTypeEnum.NOTICE_MSG.getMsgTypeId())){
            // 通知消息(群发)
            ChatMessageBean chatBean = new ChatMessageBean(curTime, msgTypeId, ChatMsgTypeEnum.NOTICE_MSG.getMsgTypeDesc(), "管理员", "", msg, new HashMap<>());
            sendToAll(roomName, chatBean);
        } else if (msgTypeId.equals(ChatMsgTypeEnum.STATUS_MSG.getMsgTypeId())){
            // 状态消息(群发)
            if (msg.contains("input:")) {
                String inputStatus = msg.split(":")[1];
                List<String> roomInputingUserList = roomInputingUserMap.get(roomName);
                if (null == roomInputingUserList) {
                    roomInputingUserList = new ArrayList<>();
                }

                if (inputStatus.equals("1")) {
                    if (!roomInputingUserList.contains(msgFrom)) {
                        roomInputingUserList.add(0, msgFrom);
                    }
                } else {
                    roomInputingUserList.remove(msgFrom);
                }
                roomInputingUserMap.put(roomName, roomInputingUserList);
                Map<String, Object> extendMap = new HashMap<>();
                extendMap.put("inputingUserList", roomInputingUserList);

                ChatMessageBean chatBean = new ChatMessageBean(curTime, msgTypeId, ChatMsgTypeEnum.STATUS_MSG.getMsgTypeDesc(), msgFrom, "", msg, extendMap);
                sendToAll(roomName, chatBean);
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
                       Session session, EndpointConfig endpointConfig) throws Exception {
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user).eq("password", pass));
        if (null == wsUsersDO) {
            logger.info("用户{}不存在或者密码错误", user);
            this.session = null;
            this.roomName = null;
            this.user = null;
            this.pass = null;
            this.userAgent = null;
            throw new Exception("用户不存在或密码错误");
        }
        this.session = session;
        this.roomName = roomName;
        this.user = user;
        this.pass = pass;
        this.userAgent = userAgent;

        String enterMsgFormat = "%s(%s) 进入了聊天室：%s";
        String enterMsg = String.format(enterMsgFormat, this.user, this.userAgent, this.roomName);
        logger.info(enterMsg);

        // 更新房间人员信息
        addRoomUser(this.roomName, this.user);

        // 登录日志
        WsChatlogDO loginLog = new WsChatlogDO(SDF_STANDARD.format(new Date()), this.roomName, this.user, "", "进入了聊天室", this.userAgent);
        wsChatlogService.insert(loginLog);

        // 广播消息
        try {
            ChatMessageBean chatBean = new ChatMessageBean(SDF_HHMMSS.format(new Date()), ChatMsgTypeEnum.SYSTEM_MSG.getMsgTypeId(), ChatMsgTypeEnum.SYSTEM_MSG.getMsgTypeDesc(),
                    "", "", enterMsg, getRoomOnlineInfo(this.roomName));
            sendToAll(roomName, chatBean);
        } catch (Exception e) {
           e.printStackTrace();
        }

        //获取离线日志
        List<WsChatlogDO> chatLogHistoryList = wsChatlogService.selectList(new EntityWrapper<WsChatlogDO>().eq("room_name", this.roomName)
                .eq("to_user", this.user)
                .gt("time", wsUsersDO.getLastLogoutTime()));
        if (null != chatLogHistoryList && chatLogHistoryList.size() > 0) {
            for (WsChatlogDO wcl : chatLogHistoryList) {
                String time = wcl.getTime();
                if (time.length() >= 18) {
                    time = time.substring(11);
                }

                // 发送方相关信息
                Map<String, Object> extendMap = new HashMap<>();
                WsUserProfileDO wsUserProfileDO = CoreCache.getInstance().getUserProfile(wcl.getUser());
                if (null == wsUserProfileDO) {
                    List<WsUserProfileDO> userProfileList = wsUserProfileService.selectList(null);
                    for (WsUserProfileDO wupd : userProfileList) {
                        if (wupd.getUserName().equals(wcl.getUser())) {
                            wsUserProfileDO = wupd;
                            break;
                        }
                    }
                }
                extendMap.put("userProfile", wsUserProfileDO);
                try {
                    ChatMessageBean offlineChatBean = new ChatMessageBean(time, ChatMsgTypeEnum.CHAT_OFFLINE_MSG.getMsgTypeId(), ChatMsgTypeEnum.CHAT_OFFLINE_MSG.getMsgTypeDesc(), wcl.getUser(), wcl.getToUser(), UnicodeUtil.unicode2String(wcl.getMsg()), extendMap);
                    this.session.getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(offlineChatBean));
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
        if (StringUtils.isBlank(this.user)) {
            return;
        }
        // 删除房间相关人信息
        removeRoomUser(this.roomName, this.user);
        removeRoomInputing(this.roomName, this.user);

        String leaveMsgFormat = "%s(%s) 离开了聊天室：%s";
        String leaveMsg = String.format(leaveMsgFormat, this.user, this.userAgent, this.roomName);
        logger.info(leaveMsg);

        // 广播消息
        ChatMessageBean chatBean = new ChatMessageBean(SDF_HHMMSS.format(new Date()), ChatMsgTypeEnum.SYSTEM_MSG.getMsgTypeId(), ChatMsgTypeEnum.SYSTEM_MSG.getMsgTypeDesc(),
                "", "", leaveMsg, getRoomOnlineInfo(this.roomName));
        try {
            sendToAll(this.roomName, chatBean);
        } catch (Exception e) {

        }

        // 记录登出日志
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", this.user));
        if (null != wsUsersDO) {
            // 记录登出日志
            WsChatlogDO logoutLog = new WsChatlogDO(SDF_STANDARD.format(new Date()), this.roomName, this.user, "", "离开了聊天室", "");
            wsChatlogService.insert(logoutLog);
        }
    }

    /**
     * 连接聊天室异常.
     */
    @OnError
    public void onError(Throwable throwable) {
        if (StringUtils.isBlank(this.user)) {
            return;
        }

        logger.info("用户{}: 进入聊天室:{} 失败", this.user, this.roomName);
        throwable.printStackTrace();
        removeRoomUser(this.roomName, this.user);
        removeRoomInputing(this.roomName, this.user);
    }

    // 聊天室加人
    public synchronized void addRoomUser(String roomName, String user) {
        Map<String, Session> roomMap = new ConcurrentHashMap<>();
        if (clientsMap.containsKey(roomName)) {
            roomMap = clientsMap.get(roomName);
        }
        roomMap.put(user, this.session);
        clientsMap.put(roomName, roomMap);
    }

    // 聊天室减人
    public static synchronized void removeRoomUser(String roomName, String user) {
        if (clientsMap.containsKey(roomName)) {
            Map<String, Session> userMap = clientsMap.get(roomName);
            userMap.remove(user);
        }
    }

    // 聊天室正在聊天减人
    public static synchronized void removeRoomInputing(String roomName, String user) {
        if (roomInputingUserMap.containsKey(roomName)) {
            List<String> inputingUserList = roomInputingUserMap.get(roomName);
            inputingUserList.remove(user);
        }
    }

    // 删除用户的所有聊天室
    public static void removeUserFromAllRoom(String user) {
        for (Entry<String, Map<String, Session>> entry : clientsMap.entrySet()) {
            Map<String, Session> roomMap = entry.getValue();
            roomMap.remove(user);
        }
    }

    // 获取聊天室的人数
    public static synchronized int getRoomOnLineCount(String roomName) {
        Map<String, Session> map = getRoomClientsSessionMap(roomName);
        return map != null ? map.size() : 0;
    }

    // 获取聊天室在线信息
    public static synchronized Map<String, Object> getRoomOnlineInfo (String roomName) {
        Map<String, Object> map = new HashMap<>();
        map.put("roomCount", getRoomOnLineCount(roomName));
        List<String> roomUserNameList = getRoomClientsUserList(roomName);
        List<WsUserProfileDO> roomUserProfileList = wsUserProfileService.selectList(new EntityWrapper<WsUserProfileDO>().in("user_name", roomUserNameList));
        map.put("userProfileList", roomUserProfileList);

        return map;
    }

    // 获取聊天室的所有用户(session)
    public static synchronized Map<String, Session> getRoomClientsSessionMap(String roomName) {
        if (clientsMap.containsKey(roomName)) {
            return clientsMap.get(roomName);
        }
        return new ConcurrentHashMap<>();
    }

    // 获取聊天室的所有用户(用户名)
    public static synchronized List<String> getRoomClientsUserList(String roomName) {
        List<String> roomUserList = new ArrayList<>();
        Map<String, Session> roomClientMap = getRoomClientsSessionMap(roomName);
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

    /**
     * 群发消息.
     * @param roomName 房间名称
     * @param chatMessageBean 消息对象
     */
    public static void sendToAll(String roomName, ChatMessageBean chatMessageBean) {
        Map<String, Session> roomClientMap = getRoomClientsSessionMap(roomName);
        for (Entry<String, Session> entry : roomClientMap.entrySet()) {
            try {
                entry.getValue().getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatMessageBean));
            } catch (Exception e) {

            }
        }
    }

    /**
     * 单发消息.
     * @param roomName 房间名称
     * @param toUser 接收人
     * @param chatMessageBean 消息对象
     */
    public static void sendToOne(String roomName, String toUser, ChatMessageBean chatMessageBean) throws IOException {
        Session toSession = queryRoomSession(roomName, toUser);
        if (null != toSession) {
            toSession.getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatMessageBean));
        }
    }
}
