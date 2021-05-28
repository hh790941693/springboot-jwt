package com.pjb.springbootjwt.zhddkk.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LoginDTO {

    @NotBlank(message = "用户名不能为空")
    @NotNull(message = "用户名不能为null")
    private String user;

    @NotBlank(message = "密码不能为空")
    @NotNull(message = "密码不能为null")
    private String pass;

    @NotBlank(message = "验证码不能为空")
    @NotNull(message = "验证码不能为null")
    private String verifyCode;
}
