package com.pjb.springbootjwt.zhddkk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import lombok.Data;

import java.util.Date;

@Data
public class WsChatroomUsersDTO extends WsUsersDO {
    // 在线状态 0:离线 1:在线
    private Integer status;

    //是否是群主  0:不是 1:是
    private Integer isOwner;

    // 是否是管理员
    private Integer isManager;

    // 禁言状态 0:未禁言 1:已禁言
    private Integer banStatus;

    // 黑名单状态 0:不是 1:是
    private Integer blackStatus;

    // 真实姓名
    private String realName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
}
