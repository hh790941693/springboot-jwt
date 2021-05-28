package com.pjb.springbootjwt.zhddkk.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class WsUserDTO {

    private Integer id;

    @NotBlank(message = "姓名不能为空")
    @NotNull(message = "姓名不能为null")
    private String name;

    @NotBlank(message = "密码不能为空")
    @NotNull(message = "密码不能为null")
    private String password;

    @NotBlank(message = "地址不能为空")
    @NotNull(message = "地址不能为null")
    private String address;
}
