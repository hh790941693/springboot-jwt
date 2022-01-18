package com.pjb.springbootjwt.zhddkk.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 账号登录DTO.
 */
@Data
public class LoginDTO implements Serializable {

    private static final long serialVersionUID = 6315426423563472002L;

    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]*",message = "用户名必须是半角英文字")
    private String user;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "[a-zA-Z0-9_\\-/,&#$@\\*%+'!^?;.(){}\\[\\]~]*", message = "密码必须是半角英数字和特定符号")
    private transient String pass;

    @NotBlank(message = "验证码不能为空")
    private String verifyCode;
}
