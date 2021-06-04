package com.pjb.springbootjwt.ump.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenBean implements Serializable {

    private static final long serialVersionUID = 2552429215424205489L;

    private String userId;
    private String token;
    private Long tokenExpire;
    private String refreashToken;
    private Long refreashTokenExpire;
}
