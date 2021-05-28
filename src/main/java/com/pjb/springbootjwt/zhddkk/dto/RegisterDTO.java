package com.pjb.springbootjwt.zhddkk.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 账号注册DTO.
 */
@Data
public class RegisterDTO {

    @NotBlank(message = "用户名不能为空")
    private String user;

    @NotBlank(message = "密码不能为空")
    private String pass;

    @NotBlank(message = "请上传头像")
    private String headImg;

    @NotBlank(message = "确认密码不能为空")
    private String confirmPass;

    @NotBlank(message = "请选择问题1")
    private String question1;

    @NotBlank(message = "请填写问题1的答案")
    private String answer1;

    @NotBlank(message = "请选择问题2")
    private String question2;

    @NotBlank(message = "请填写问题2的答案")
    private String answer2;

    @NotBlank(message = "请选择问题3")
    private String question3;

    @NotBlank(message = "请填写问题3的答案")
    private String answer3;
}
