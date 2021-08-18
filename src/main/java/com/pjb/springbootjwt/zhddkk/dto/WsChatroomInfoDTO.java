package com.pjb.springbootjwt.zhddkk.dto;

import com.pjb.springbootjwt.zhddkk.domain.WsCommonDO;
import lombok.Data;
import java.util.List;

/**
 * 聊天室房间人员情况信息.
 */
@Data
public class WsChatroomInfoDTO {

    // 常用语
    private List<WsCommonDO> cyyList;
    // 房间在线用户数
    private int roomUserCount = 0;
    // 房间所有人
    private List<WsChatroomUsersDTO> chatroomAllUserList;
    // 房间在线人
    private List<WsChatroomUsersDTO> chatroomOnlineUserList;
    // 房间管理员用户列表
    private List<WsChatroomUsersDTO> managerUserList;
}
