package com.pjb.springbootjwt.zhddkk.websocket;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.zhddkk.bean.ChatMessageBean;
import com.pjb.springbootjwt.zhddkk.cache.CoreCache;
import com.pjb.springbootjwt.zhddkk.constants.ChatMsgTypeEnum;
import com.pjb.springbootjwt.zhddkk.domain.*;
import com.pjb.springbootjwt.zhddkk.dto.WsChatroomInfoDTO;
import com.pjb.springbootjwt.zhddkk.service.*;
import com.pjb.springbootjwt.zhddkk.util.JsonUtil;
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

/**
 * 聊天室.
 */
@ServerEndpoint(value = "/chatRoomWebSocket/{roomId}/{roomPass}/{userId}/{userPass}", configurator = HttpSessionConfigurator.class)
@Component
public class ChatRoomWebSocket {
    //日志
    private static final Logger logger = LoggerFactory.getLogger(ChatRoomWebSocket.class);

    private static final SimpleDateFormat SDF_HHMMSS = new SimpleDateFormat("HH:mm:ss");

    // 房间内各用户会话map       <房间号, <用户ID, Session>>
    private static final Map<String, Map<String, Session>> roomSessionsMap = new ConcurrentHashMap();

    // 房间内各用户正在输入状态map <房间号, List<用户名>>
    private static final Map<String, List<String>> roomInputingStatusMap = new ConcurrentHashMap();

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

    // 用户密码
    private String userPass;

    // 用户数据对象
    private WsUsersDO userData;

    // 通过WebSocketConfig注入
    public static WsUsersService wsUsersService;

    public static WsChatroomService wsChatroomService;

    public static WsChatroomUsersService wsChatroomUsersService;

    public static LoginService loginService;

    /**
     * 接收客户端发来的消息并转发到客户端.
     * @param message 消息 {"roomId":"002","from":"admin","to":"无名1","msgTypeId":2,"msgTypeDesc":"在线消息","msg":"hello"}
     * @param session 会话  msgTypeId: 1:广播消息  2:在线消息  4.通知消息  5.状态消息
     * @throws IOException 异常
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        if (StringUtils.isBlank(message)) {
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
            WsChatroomUsersDO roomUserInfo = wsChatroomUsersService.selectOne(new EntityWrapper<WsChatroomUsersDO>().eq("room_id", roomId).eq("user_name", msgFrom));
            if (null != roomUserInfo) {
                // 黑名单
                if (roomUserInfo.getBlackStatus().intValue() != 0) {
                    ChatMessageBean chatBean = new ChatMessageBean(curTime, ChatMsgTypeEnum.ERROR_MSG.getMsgTypeId(), ChatMsgTypeEnum.ERROR_MSG.getMsgTypeDesc(), "", "", "你已被拉黑名单了!", new HashMap<>());
                    session.getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
                    return;
                }
                // 禁言
                if (roomUserInfo.getBanStatus().intValue() != 0) {
                    ChatMessageBean chatBean = new ChatMessageBean(curTime, ChatMsgTypeEnum.ERROR_MSG.getMsgTypeId(), ChatMsgTypeEnum.ERROR_MSG.getMsgTypeDesc(), "", "", "你已被禁言了!", new HashMap<>());
                    session.getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
                    return;
                }
            }

            Map<String, Object> extendMap = new HashMap<>();
            extendMap.put("userProfile", CoreCache.getInstance().getUserProfile(msgFrom));
            ChatMessageBean chatBean = new ChatMessageBean(curTime, msgTypeId, ChatMsgTypeEnum.CHAT_ONLINE_MSG.getMsgTypeDesc(), msgFrom, msgTo, msg, extendMap);
            sendToAll(roomId, chatBean);
        } else if (msgTypeId.equals(ChatMsgTypeEnum.NOTICE_MSG.getMsgTypeId())){
            // 通知消息(群发)
            ChatMessageBean chatBean = new ChatMessageBean(curTime, msgTypeId, ChatMsgTypeEnum.NOTICE_MSG.getMsgTypeDesc(), "管理员", "", msg, new HashMap<>());
            sendToAll(roomId, chatBean);
        } else if (msgTypeId.equals(ChatMsgTypeEnum.STATUS_MSG.getMsgTypeId())){
            // 状态消息(群发)
            if (msg.contains("input:")) {
                String inputStatus = msg.split(":")[1];

                if (inputStatus.equals("1")) {
                    addRoomInputing(roomId, msgFrom);
                } else {
                    removeRoomInputing(roomId, msgFrom);
                }
                Map<String, Object> extendMap = new HashMap<>();
                List<String> roomInputingUserList = roomInputingStatusMap.get(roomId);
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
            logger.info("用户{}进入聊天室%s失败,原因是用户不存在或者密码错误", userId, roomId);
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
        if (StringUtils.isNotBlank(wsChatroomDO.getPassword()) && !wsChatroomDO.getPassword().equals("NO-PASSWORD")) {
            if (!roomPass.equals(wsChatroomDO.getPassword())) {
                throw new Exception("房间密码不正确");
            }
        }

        // 检查是否被拉黑名单了
        WsChatroomUsersDO roomUserInfo = wsChatroomUsersService.selectOne(new EntityWrapper<WsChatroomUsersDO>().eq("room_id", roomId).eq("user_id", userId));
        if (null != roomUserInfo) {
            if (roomUserInfo.getBlackStatus().intValue() != 0) {
                throw new Exception("你已被拉黑名单了");
            }
        }

        this.session = session;
        this.roomId = roomId;
        this.roomPass = roomPass;
        this.userId = userId;
        this.userName = wsUsersDO.getName();
        this.userPass = userPass;
        this.userData = wsUsersDO;

        // 更新房间人员信息
        addRoomUser(this.session, this.roomId, this.userId, this.userName);

        String enterMsg = String.format("%s 进入了聊天室：%s", this.userName, this.roomId);
        logger.info(enterMsg);

        // 群发广播消息(某某用户进入了聊天室)
        ChatMessageBean chatBean = new ChatMessageBean(SDF_HHMMSS.format(new Date()), ChatMsgTypeEnum.SYSTEM_MSG.getMsgTypeId(), ChatMsgTypeEnum.SYSTEM_MSG.getMsgTypeDesc(),
                "", "", enterMsg, getRoomOnlineInfo(this.roomId));
        sendToAll(roomId, chatBean);
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
        removeRoomUser(this.roomId, this.userId);
        removeRoomInputing(this.roomId, this.userName);

        String leaveMsg = String.format("%s 离开了聊天室：%s", this.userName, this.roomId);
        logger.info(leaveMsg);

        // 群发广播消息(某某用户离开了聊天室)
        ChatMessageBean chatBean = new ChatMessageBean(SDF_HHMMSS.format(new Date()), ChatMsgTypeEnum.SYSTEM_MSG.getMsgTypeId(), ChatMsgTypeEnum.SYSTEM_MSG.getMsgTypeDesc(),
                "", "", leaveMsg, getRoomOnlineInfo(this.roomId));
        sendToAll(this.roomId, chatBean);
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
        // 聊天室减人
        removeRoomUser(this.roomId, this.userId);
        // 清除输入状态
        removeRoomInputing(this.roomId, this.userName);
    }

    // 聊天室加人
    public synchronized void addRoomUser(Session session, String roomId, String userId, String userName) {
        Map<String, Session> roomsSessMap = new ConcurrentHashMap<>();
        if (roomSessionsMap.containsKey(roomId)) {
            roomsSessMap = roomSessionsMap.get(roomId);
        }
        roomsSessMap.put(userId, session);
        roomSessionsMap.put(roomId, roomsSessMap);

        // 记录到数据库去
        WsChatroomDO wsChatroomDO = wsChatroomService.selectOne(new EntityWrapper<WsChatroomDO>().eq("room_id", roomId));
        WsChatroomUsersDO wsChatroomUsersDO = wsChatroomUsersService.selectOne(new EntityWrapper<WsChatroomUsersDO>().eq("room_id", roomId).eq("user_id", userId));
        if (null == wsChatroomUsersDO) {
            wsChatroomUsersDO = new WsChatroomUsersDO();
            // 聊天室用户表新增记录
            wsChatroomUsersDO.setRoomId(roomId);
            wsChatroomUsersDO.setUserName(userName);
            wsChatroomUsersDO.setUserId(Long.valueOf(userId));
            wsChatroomUsersDO.setIsOwner(userId.equals(wsChatroomDO.getCreateUserId().toString()) ? 1 : 0);
            wsChatroomUsersDO.setIsManager(userId.equals(wsChatroomDO.getCreateUserId().toString()) ? 1 : 0);
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
    public synchronized void removeRoomUser(String roomId, String userId) {
        if (roomSessionsMap.containsKey(roomId)) {
            Map<String, Session> userSessionMap = roomSessionsMap.get(roomId);
            userSessionMap.remove(userId);
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
        if (roomInputingStatusMap.containsKey(roomId)) {
            List<String> inputingUserList = roomInputingStatusMap.get(roomId);
            inputingUserList.remove(userName);
        }
    }

    // 聊天室正在聊天加人
    public static synchronized void addRoomInputing(String roomId, String userName) {
        List<String> inputingUserList = new ArrayList<>();
        if (roomInputingStatusMap.containsKey(roomId)) {
            inputingUserList = roomInputingStatusMap.get(roomId);
            if (!inputingUserList.contains(userName)) {
                inputingUserList.add(0, userName);
            }
        } else {
            inputingUserList.add(0, userName);
            roomInputingStatusMap.put(roomId, inputingUserList);
        }
    }

    // 获取聊天室在线信息
    public static synchronized Map<String, Object> getRoomOnlineInfo (String roomId) {
        Map<String, Object> map = new HashMap<>();
        WsChatroomInfoDTO wciDTO = loginService.getChatRoomInfo(roomId);
        // 聊天室所有人
        map.put("chatroomAllUserList", wciDTO.getChatroomAllUserList());
        // 聊天室所有在线用户
        map.put("chatroomOnlineUserList", wciDTO.getChatroomOnlineUserList());
        // 聊天室所有管理员
        map.put("managerUserList", wciDTO.getManagerUserList());
        // 聊天室在线人数
        map.put("roomUserCount", wciDTO.getRoomUserCount());

        return map;
    }

    // 获取聊天室的所有用户会话(session)
    public static synchronized Map<String, Session> getRoomUserSessionsMap(String roomId) {
        if (roomSessionsMap.containsKey(roomId)) {
            return roomSessionsMap.get(roomId);
        }
        return new ConcurrentHashMap<>();
    }

    /**
     * 群发消息.
     * @param roomId 房间名称
     * @param chatMessageBean 消息对象
     */
    public static void sendToAll(String roomId, ChatMessageBean chatMessageBean) {
        Map<String, Session> roomClientMap = getRoomUserSessionsMap(roomId);
        for (Entry<String, Session> entry : roomClientMap.entrySet()) {
            try {
                entry.getValue().getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatMessageBean));
            } catch (Exception e) {

            }
        }
    }

    /**
     * 单发消息.
     * @param roomId 房间名称
     * @param userId 用户Id
     * @param chatMessageBean 消息对象
     */
    public static void sendToOne(String roomId, String userId, ChatMessageBean chatMessageBean) {
        Map<String, Session> roomClientMap = getRoomUserSessionsMap(roomId);
        for (Entry<String, Session> entry : roomClientMap.entrySet()) {
            if (entry.getKey().equals(userId)) {
                try {
                    entry.getValue().getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatMessageBean));
                } catch (Exception e) {

                }
                break;
            }
        }
    }
}