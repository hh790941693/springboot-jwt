package com.pjb.springbootjwt.zhddkk.websocket;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
* 用于从websocket中获取用户session.
*/
public class HttpSessionConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
//        HttpSession httpSession = (HttpSession) request.getHttpSession();
//        if (null != httpSession) {
//            sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
//        }
        // do nothing
        return;
    }
}
