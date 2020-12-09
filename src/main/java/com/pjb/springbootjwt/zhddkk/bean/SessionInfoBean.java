package com.pjb.springbootjwt.zhddkk.bean;

import lombok.Data;
import net.sf.json.JSONObject;

/**
 * session信息.
 */
@Data
public class SessionInfoBean {

    private String id;

    private String user;

    private String password;

    private String webserverIp;

    private String webserverPort;

    private String selfImg;

    private String userAgent;

    private String userType;

    private JSONObject jsonObject;

    private String jsonStr;

    public SessionInfoBean(){

    }

    /**
     * 构造方法.
     * @param id  会话id
     * @param user 用户名
     * @param password 密码密文
     * @param webserverIp 服务器ip
     * @param webserverPort 端口
     * @param selfImg 头像url
     * @param userAgent 浏览器类型
     * @param userType 用户类型
     */
    public SessionInfoBean(String id, String user, String password, String webserverIp,
                           String webserverPort, String selfImg, String userAgent, String userType) {
        this.id = id;
        this.user = user;
        this.password = password;
        this.webserverIp = webserverIp;
        this.webserverPort = webserverPort;
        this.selfImg = selfImg;
        this.userAgent = userAgent;
        this.userType = userType;
    }
}
