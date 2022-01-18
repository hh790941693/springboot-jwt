package com.pjb.springbootjwt.zhddkk.dto;

import lombok.Data;

@Data
public class LoginHistoryDto {
    private String time;
    private String userId;
    private int loginCnt;
}
