package com.pjb.springbootjwt.zhddkk.bean;

import lombok.Data;
import net.sf.json.JSONObject;

/**
 * session信息.
 */
@Data
public class SessionInfoBean {

    // 会话ID
    private String id;

    // 用户id
    private String userId;

    // 用户名
    private String userName;

    // 用户密码(密文)
    private String password;

    // 服务端ip
    private String webserverIp;

    // 服务端口
    private String webserverPort;

    // 头像
    private String selfImg;

    // 客户端类型
    private String userAgent;

    // 角色id
    private String roleId;

    // 角色名
    private String roleName;

    // 会话最大失效时间
    private int maxInactiveInterval;

    private JSONObject jsonObject;

    private String jsonStr;
}
