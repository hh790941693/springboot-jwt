package com.pjb.springbootjwt.zhddkk.websocket;

import com.pjb.springbootjwt.zhddkk.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket配置.
 */
@Configuration
@ConfigurationProperties(prefix = "websocket.config")
public class WebSocketConfig {

    private String address;

    private String port;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Autowired
    public void setWsUsersService(WsUsersService wsUsersService) {
        ChatRoomWebSocket.wsUsersService = wsUsersService;
        ZhddWebSocket.wsUsersService = wsUsersService;
    }

    @Autowired
    public void setWsChatlogService(WsChatlogService wsChatlogService) {
        ZhddWebSocket.wsChatlogService = wsChatlogService;
    }

    @Autowired
    public void setWsCommonService(WsCommonService wsCommonService) {
        ZhddWebSocket.wsCommonService = wsCommonService;
    }

    @Autowired
    public void setWsUserProfileService(WsUserProfileService wsUserProfileService) {
        ZhddWebSocket.wsUserProfileService = wsUserProfileService;
    }

    @Autowired
    public void setWsChatRoomService(WsChatroomService wsChatroomService) {
        ChatRoomWebSocket.wsChatroomService = wsChatroomService;
    }

    @Autowired
    public void setWsChatRoomUsersService(WsChatroomUsersService wsChatroomUsersService) {
        ChatRoomWebSocket.wsChatroomUsersService = wsChatroomUsersService;
    }

    @Autowired
    public void setWsChatRoomUsersService(LoginService loginService) {
        ChatRoomWebSocket.loginService = loginService;
    }
}
