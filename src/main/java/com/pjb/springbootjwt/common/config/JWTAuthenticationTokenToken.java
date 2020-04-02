package com.pjb.springbootjwt.common.config;

import org.apache.shiro.authc.AuthenticationToken;

public class JWTAuthenticationTokenToken implements AuthenticationToken {

    private static final long serialVersionUID = 5320481558604267958L;

    private String token;

    public JWTAuthenticationTokenToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
