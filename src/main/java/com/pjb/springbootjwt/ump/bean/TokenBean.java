package com.pjb.springbootjwt.ump.bean;

import java.io.Serializable;

public class TokenBean implements Serializable {

    private static final long serialVersionUID = 2552429215424205489L;

    private String id;
    private String token;
    private Long tokenExpire;
    private String refreashToken;
    private Long refreashTokenExpire;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getTokenExpire() {
        return tokenExpire;
    }

    public void setTokenExpire(Long tokenExpire) {
        this.tokenExpire = tokenExpire;
    }

    public String getRefreashToken() {
        return refreashToken;
    }

    public void setRefreashToken(String refreashToken) {
        this.refreashToken = refreashToken;
    }

    public Long getRefreashTokenExpire() {
        return refreashTokenExpire;
    }

    public void setRefreashTokenExpire(Long refreashTokenExpire) {
        this.refreashTokenExpire = refreashTokenExpire;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TokenBean{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", tokenExpire=" + tokenExpire +
                ", refreashToken='" + refreashToken + '\'' +
                ", refreashTokenExpire=" + refreashTokenExpire +
                '}';
    }
}
