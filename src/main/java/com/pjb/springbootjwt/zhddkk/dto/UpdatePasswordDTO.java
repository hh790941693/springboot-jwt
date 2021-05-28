package com.pjb.springbootjwt.zhddkk.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改密码DTO.
 */
@Data
public class UpdatePasswordDTO {

    @NotBlank(message = "用户名不能为空")
    private String user;

    @NotBlank(message = "旧密码不能为空")
    private String oldPass;

    @NotBlank(message = "新密码不能为空")
    private String newPass;

    @NotBlank(message = "确认密码不能为空")
    private String confirmPass;
}
