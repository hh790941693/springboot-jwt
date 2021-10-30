package com.pjb.springbootjwt.zhddkk.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 账号登录DTO.
 */
@Data
public class LoginDTO {

    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]*",message = "用户名必须是半角英文字")
    private String user;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "[a-zA-Z0-9_\\-/,&#$@\\*%+'!^?;.(){}\\[\\]~]*", message = "密码必须是半角英数字和特定符号")
    private String pass;

    @NotBlank(message = "验证码不能为空")
    private String verifyCode;
}
