package com.pjb.springbootjwt.zhddkk.entity;

import com.pjb.springbootjwt.zhddkk.domain.WsChatroomUsersDO;
import com.pjb.springbootjwt.zhddkk.domain.WsCommonDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUserProfileDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class WsOnlineInfo {
    // 在线人数
    private int onlineCount;
    // 离线人数
    private int offlineCount;
    // 当前登录的用户信息
    private WsUsersDO currentOnlineUserInfo;
    // 所有用户列表
    private List<WsUsersDO> userInfoList;
    // 在线用户列表
    private List<WsUsersDO> onlineUserList;
    // 离线用户列表
    private List<WsUsersDO> offlineUserList;
    // 好友列表
    private List<WsUsersDO> friendsList;
    //好友在线数
    private int onlineFriendCount = 0;
    //好友离线数
    private int offlineFriendCount = 0;
    // 敏感词、脏话、常用语、注册问题
    private Map<String, List<WsCommonDO>> commonMap;

    // 房间用户详情列表
    private List<WsUserProfileDO> roomUserProfileList;
    // 房间用户数
    private int roomUserCount = 0;
    // 房间管理员用户列表
    private List<WsChatroomUsersDO> managerUserList;
}
