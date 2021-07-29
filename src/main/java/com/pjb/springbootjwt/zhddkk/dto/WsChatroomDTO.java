package com.pjb.springbootjwt.zhddkk.dto;

import com.pjb.springbootjwt.zhddkk.domain.WsChatroomDO;
import lombok.Data;

@Data
public class WsChatroomDTO extends WsChatroomDO {

    private String managerName;

    private int onlineUserCount;

    private boolean isMyRoom;
}
