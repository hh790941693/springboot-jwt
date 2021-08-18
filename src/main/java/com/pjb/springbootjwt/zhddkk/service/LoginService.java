package com.pjb.springbootjwt.zhddkk.service;

import com.pjb.springbootjwt.zhddkk.dto.WsChatroomInfoDTO;

public interface LoginService {

    // 获取聊天室人员情况
    WsChatroomInfoDTO getChatRoomInfo(String roomId);
}
