package com.pjb.springbootjwt.zhddkk.websocket;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.zhddkk.bean.ChatMessageBean;
import com.pjb.springbootjwt.zhddkk.cache.CoreCache;
import com.pjb.springbootjwt.zhddkk.constants.ChatMsgTypeEnum;
import com.pjb.springbootjwt.zhddkk.domain.*;
import com.pjb.springbootjwt.zhddkk.service.*;
import com.pjb.springbootjwt.zhddkk.util.JsonUtil;
import com.pjb.springbootjwt.zhddkk.util.UnicodeUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.EndpointConfig;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ServerEndpoint(value = "/chatRoomWebSocket/{roomId}/{roomPass}/{userId}/{userPass}", configurator = HttpSessionConfigurator.class)
@Component
public class ChatRoomWebSocket {
    private static final Logger logger = LoggerFactory.getLogger(ChatRoomWebSocket.class);

    private static final SimpleDateFormat SDF_STANDARD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final SimpleDateFormat SDF_HHMMSS = new SimpleDateFormat("HH:mm:ss");

    // <房间号, <用戶ID,用户session>>
    private static final Map<String, Map<String, Session>> clientsMap = new ConcurrentHashMap();

    // <房间号, List<用户名>>
    private static final Map<String, List<String>> roomInputingUserMap = new ConcurrentHashMap();

    // 当前会话
    private Session session;

    // 房间号
    private String roomId;

    // 房间密码
    private String roomPass;

    // 用户id
    private String userId;

    // 用户名称
    private String userName;

    // 密码
    private String userPass;

    private WsUsersDO userData;

    // 通过WebSocketConfig注入
    public static WsUsersService wsUsersService;

    public static WsChatlogService wsChatlogService;

    public static WsUserProfileService wsUserProfileService;

    public static WsChatroomService wsChatroomService;

    public static WsChatroomUsersService wsChatroomUsersService;

    /**
     * 转发消息.
     * @param message 消息 {"roomName":"002","from":"admin","to":"无名1","msgTypeId":2,"msgTypeDesc":"在线消息","msg":"hello"}
     * @param session 会话  msgTypeId: 1:广播消息  2:在线消息  4.通知消息  5.状态消息
     * @throws IOException 异常
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        if (StringUtils.isBlank(message) || this.userName == null || this.session == null) {
            return;
        }

        if (!message.contains("msg")) {
            return;
        }

        JSONObject jsonObject = JsonUtil.jsonstr2Jsonobject(message);
        String roomId = jsonObject.getString("roomId");
        String msgFrom = jsonObject.getString("from");
        String msgTo = jsonObject.getString("to");
        String msgTypeId = jsonObject.getString("msgTypeId");
        String msgStr = jsonObject.getString("msg");

        //对消息进行敏感字、脏话进行处理
        String msg = msgStr;
        List<WsCommonDO> commonList = CoreCache.getInstance().getCommonList();
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
            extendMap.put("userProfile", CoreCache.getInstance().getUserProfile(msgFrom));
            ChatMessageBean chatBean = new ChatMessageBean(curTime, msgTypeId, ChatMsgTypeEnum.CHAT_ONLINE_MSG.getMsgTypeDesc(), msgFrom, msgTo, msg, extendMap);
            sendToAll(roomId, chatBean);

            // 记录聊天日志
            WsChatlogDO wsChatlogDO = new WsChatlogDO(SDF_STANDARD.format(new Date()), roomId, msgFrom, msgTo, UnicodeUtil.string2Unicode(msgStr),"");
            wsChatlogService.insert(wsChatlogDO);
        } else if (msgTypeId.equals(ChatMsgTypeEnum.NOTICE_MSG.getMsgTypeId())){
            // 通知消息(群发)
            ChatMessageBean chatBean = new ChatMessageBean(curTime, msgTypeId, ChatMsgTypeEnum.NOTICE_MSG.getMsgTypeDesc(), "管理员", "", msg, new HashMap<>());
            sendToAll(roomId, chatBean);
        } else if (msgTypeId.equals(ChatMsgTypeEnum.STATUS_MSG.getMsgTypeId())){
            // 状态消息(群发)
            if (msg.contains("input:")) {
                String inputStatus = msg.split(":")[1];
                List<String> roomInputingUserList = roomInputingUserMap.get(roomId);
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
                roomInputingUserMap.put(roomId, roomInputingUserList);
                Map<String, Object> extendMap = new HashMap<>();
                extendMap.put("inputingUserList", roomInputingUserList);

                ChatMessageBean chatBean = new ChatMessageBean(curTime, msgTypeId, ChatMsgTypeEnum.STATUS_MSG.getMsgTypeDesc(), msgFrom, "", msg, extendMap);
                sendToAll(roomId, chatBean);
            }
        }
    }

    /**
     * 进入聊天室.
     * @param roomId 房间号
     * @param roomPass 房间密码
     * @param userId 用户ID
     * @param userPass 密码
     * @param session 会话
     * @param endpointConfig endpointConfig
     */
    @javax.websocket.OnOpen
    public void OnOpen(@PathParam("roomId") String roomId,
                       @PathParam("roomPass") String roomPass,
                       @PathParam("userId") String userId,
                       @PathParam("userPass") String userPass,
                       Session session, EndpointConfig endpointConfig) throws Exception {

        // 检查用户是否合法
        WsUsersDO wsUsersDO = wsUsersService.selectById(userId);
        if (null == wsUsersDO || !wsUsersDO.getPassword().equals(userPass)) {
            logger.info("用户{}进入聊天室%s失败,原因是用户不存在或者密码错误", userName, roomId);
            this.session = null;
            this.roomId = null;
            this.roomPass = null;
            this.userId = null;
            this.userName = null;
            this.userPass = null;
            this.userData = null;
            throw new Exception("用户不存在或密码错误");
        }

        // 检查房间号是否需要密码
        WsChatroomDO wsChatroomDO = wsChatroomService.selectOne(new EntityWrapper<WsChatroomDO>().eq("room_id", roomId));
        if (null == wsChatroomDO || wsChatroomDO.getStatus().intValue() != 1) {
            throw new Exception("聊天室不存在");
        }
        if (StringUtils.isNotBlank(wsChatroomDO.getPassword())) {
            if (!roomPass.equals(wsChatroomDO.getPassword())) {
                throw new Exception("房间密码不正确");
            }
        }

        this.session = session;
        this.roomId = roomId;
        this.roomPass = roomPass;
        this.userId = userId;
        this.userName = wsUsersDO.getName();
        this.userPass = userPass;
        this.userData = wsUsersDO;

        String enterMsgFormat = "%s 进入了聊天室：%s";
        String enterMsg = String.format(enterMsgFormat, this.userName, this.roomId);
        logger.info(enterMsg);

        // 更新房间人员信息
        addRoomUser();

        // 登录日志
        WsChatlogDO loginLog = new WsChatlogDO(SDF_STANDARD.format(new Date()), this.roomId, this.userName, "", "进入了聊天室", "");
        wsChatlogService.insert(loginLog);

        // 广播消息
        try {
            ChatMessageBean chatBean = new ChatMessageBean(SDF_HHMMSS.format(new Date()), ChatMsgTypeEnum.SYSTEM_MSG.getMsgTypeId(), ChatMsgTypeEnum.SYSTEM_MSG.getMsgTypeDesc(),
                    "", "", enterMsg, getRoomOnlineInfo(this.roomId));
            sendToAll(roomId, chatBean);
        } catch (Exception e) {
           e.printStackTrace();
        }

        //获取离线日志
        List<WsChatlogDO> chatLogHistoryList = wsChatlogService.selectList(new EntityWrapper<WsChatlogDO>().eq("room_name", this.roomId)
                .eq("to_user", this.userName)
                .gt("time", wsUsersDO.getLastLogoutTime()));
        if (null != chatLogHistoryList && chatLogHistoryList.size() > 0) {
            for (WsChatlogDO wcl : chatLogHistoryList) {
                String time = wcl.getTime();
                if (time.length() >= 18) {
                    time = time.substring(11);
                }

                // 发送方相关信息
                Map<String, Object> extendMap = new HashMap<>();
                extendMap.put("userProfile", CoreCache.getInstance().getUserProfile(wcl.getUser()));
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
        if (StringUtils.isBlank(this.userName)) {
            return;
        }
        // 删除房间相关人信息
        removeRoomUser();
        removeRoomInputing(this.roomId, this.userName);

        String leaveMsgFormat = "%s 离开了聊天室：%s";
        String leaveMsg = String.format(leaveMsgFormat, this.userName, this.roomId);
        logger.info(leaveMsg);

        // 广播消息
        ChatMessageBean chatBean = new ChatMessageBean(SDF_HHMMSS.format(new Date()), ChatMsgTypeEnum.SYSTEM_MSG.getMsgTypeId(), ChatMsgTypeEnum.SYSTEM_MSG.getMsgTypeDesc(),
                "", "", leaveMsg, getRoomOnlineInfo(this.roomId));
        try {
            sendToAll(this.roomId, chatBean);
        } catch (Exception e) {

        }

        // 记录登出日志
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", this.userName));
        if (null != wsUsersDO) {
            // 记录登出日志
            WsChatlogDO logoutLog = new WsChatlogDO(SDF_STANDARD.format(new Date()), this.roomId, this.userName, "", "离开了聊天室", "");
            wsChatlogService.insert(logoutLog);
        }
    }

    /**
     * 连接聊天室异常.
     */
    @OnError
    public void onError(Throwable throwable) {
        if (StringUtils.isBlank(this.userName)) {
            return;
        }

        logger.info("用户{}: 进入聊天室:{} 失败", this.userName, this.roomId);
        throwable.printStackTrace();
        removeRoomUser();
        removeRoomInputing(this.roomId, this.userName);
    }

    // 聊天室加人
    public synchronized void addRoomUser() {
        Map<String, Session> roomMap = new ConcurrentHashMap<>();
        if (clientsMap.containsKey(this.roomId)) {
            roomMap = clientsMap.get(this.roomId);
        }
        roomMap.put(this.userId, this.session);
        clientsMap.put(this.roomId, roomMap);

        // 记录到数据库去
        WsChatroomDO wsChatroomDO = wsChatroomService.selectOne(new EntityWrapper<WsChatroomDO>().eq("room_id", this.roomId));
        WsChatroomUsersDO wsChatroomUsersDO = wsChatroomUsersService.selectOne(new EntityWrapper<WsChatroomUsersDO>().eq("room_id", this.roomId).eq("user_id", this.userId));
        if (null == wsChatroomUsersDO) {
            wsChatroomUsersDO = new WsChatroomUsersDO();
            // 聊天室用户表新增记录
            wsChatroomUsersDO.setRoomId(this.roomId);
            wsChatroomUsersDO.setUserName(this.userName);
            wsChatroomUsersDO.setUserId(Long.valueOf(this.userId));
            wsChatroomUsersDO.setIsManager(this.userId.equals(wsChatroomDO.getCreateUserId().toString()) ? 1 : 0);
            wsChatroomUsersDO.setCreateTime(new Date());
            wsChatroomUsersDO.setUpdateTime(new Date());
            wsChatroomUsersDO.setStatus(1);
            wsChatroomUsersService.insert(wsChatroomUsersDO);
        } else {
            // 更新状态为在线
            if (wsChatroomUsersDO.getStatus().intValue() != 1) {
                wsChatroomUsersDO.setStatus(1);
                wsChatroomUsersDO.setUpdateTime(new Date());
                wsChatroomUsersService.updateById(wsChatroomUsersDO);
            }
        }
    }

    // 聊天室减人
    public synchronized void removeRoomUser() {
        if (clientsMap.containsKey(this.roomId)) {
            Map<String, Session> userMap = clientsMap.get(this.roomId);
            userMap.remove(this.userId);
        }

        WsChatroomUsersDO wsChatroomUsersDO = wsChatroomUsersService.selectOne(new EntityWrapper<WsChatroomUsersDO>().eq("room_id", this.roomId).eq("user_id", this.userId));
        if (null != wsChatroomUsersDO) {
            if (wsChatroomUsersDO.getStatus().intValue() != 0) {
                wsChatroomUsersDO.setStatus(0);
                wsChatroomUsersService.updateById(wsChatroomUsersDO);
            }
        }
    }

    // 聊天室正在聊天减人
    public static synchronized void removeRoomInputing(String roomId, String userName) {
        if (roomInputingUserMap.containsKey(roomId)) {
            List<String> inputingUserList = roomInputingUserMap.get(roomId);
            inputingUserList.remove(userName);
        }
    }

    // 获取聊天室的人数
    public static synchronized int getRoomOnLineCount(String roomId) {
        Map<String, Session> map = getRoomClientsSessionMap(roomId);
        return map != null ? map.size() : 0;
    }

    // 获取聊天室在线信息
    public static synchronized Map<String, Object> getRoomOnlineInfo (String roomId) {
        Map<String, Object> map = new HashMap<>();
        map.put("roomCount", getRoomOnLineCount(roomId));
        List<String> roomUserNameList = getRoomClientsUserList(roomId);
        List<WsUserProfileDO> roomUserProfileList = wsUserProfileService.selectList(new EntityWrapper<WsUserProfileDO>().in("user_name", roomUserNameList));
        map.put("userProfileList", roomUserProfileList);

        return map;
    }

    // 获取聊天室的所有用户(session)
    public static synchronized Map<String, Session> getRoomClientsSessionMap(String roomId) {
        if (clientsMap.containsKey(roomId)) {
            return clientsMap.get(roomId);
        }
        return new ConcurrentHashMap<>();
    }

    // 获取聊天室的所有用户(用户名)
    public static synchronized List<String> getRoomClientsUserList(String roomId) {
        List<String> roomUserList = new ArrayList<>();
        Map<String, Session> roomClientMap = getRoomClientsSessionMap(roomId);
        for (Entry<String, Session> entry : roomClientMap.entrySet()) {
            roomUserList.add(entry.getKey());
        }
        return roomUserList;
    }

    /**
     * 群发消息.
     * @param roomId 房间名称
     * @param chatMessageBean 消息对象
     */
    public static void sendToAll(String roomId, ChatMessageBean chatMessageBean) {
        Map<String, Session> roomClientMap = getRoomClientsSessionMap(roomId);
        for (Entry<String, Session> entry : roomClientMap.entrySet()) {
            try {
                entry.getValue().getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatMessageBean));
            } catch (Exception e) {

            }
        }
    }
}
