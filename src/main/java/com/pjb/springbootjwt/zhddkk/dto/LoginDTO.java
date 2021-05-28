package com.pjb.springbootjwt.zhddkk.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 账号登录DTO.
 */
@Data
public class LoginDTO {

    @NotBlank(message = "用户名不能为空")
    private String user;

    @NotBlank(message = "密码不能为空")
    private String pass;

    @NotBlank(message = "验证码不能为空")
    private String verifyCode;
}
