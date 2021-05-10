package com.pjb.springbootjwt.zhddkk.bean;

import lombok.Data;
import net.sf.json.JSONObject;

/**
 * session信息.
 */
@Data
public class SessionInfoBean {

    private String id;

    // 用户id
    private String userId;

    // 用户名
    private String user;

    private String password;

    private String webserverIp;

    private String webserverPort;

    private String selfImg;

    private String userAgent;

    private String roleId;

    private String roleName;

    private int maxInactiveInterval;

    private JSONObject jsonObject;

    private String jsonStr;

    public SessionInfoBean(){

    }

    /**
     * 构造方法.
     * @param id  会话id
     * @param userId 用户id
     * @param user 用户名
     * @param password 密码密文
     * @param webserverIp 服务器ip
     * @param webserverPort 端口
     * @param selfImg 头像url
     * @param userAgent 浏览器类型
     * @param roleId 用户角色id
     * @param roleName 角色名
     * @param maxInactiveInterval session超时时间(秒)
     */
    public SessionInfoBean(String id, String userId, String user, String password, String webserverIp,
                           String webserverPort, String selfImg, String userAgent, String roleId, String roleName, int maxInactiveInterval) {
        this.id = id;
        this.userId = userId;
        this.user = user;
        this.password = password;
        this.webserverIp = webserverIp;
        this.webserverPort = webserverPort;
        this.selfImg = selfImg;
        this.userAgent = userAgent;
        this.roleId = roleId;
        this.roleName = roleName;
        this.maxInactiveInterval = maxInactiveInterval;
    }
}
