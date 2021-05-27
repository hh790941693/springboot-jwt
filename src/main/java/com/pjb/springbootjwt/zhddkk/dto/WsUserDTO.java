package com.pjb.springbootjwt.zhddkk.dto;

import javax.validation.constraints.NotBlank;

public class WsUserDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    @NotBlank
    private String address;
}
