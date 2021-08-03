package com.pjb.springbootjwt.zhddkk.dto;

import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import lombok.Data;

@Data
public class WsChatroomUsersDTO extends WsUsersDO {
    // 在线状态 0:离线 1:在线
    private Integer status;

    // 是否是管理员
    private Integer isManager;

    private String realName;
}
