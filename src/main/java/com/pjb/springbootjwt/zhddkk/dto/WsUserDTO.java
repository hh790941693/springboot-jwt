package com.pjb.springbootjwt.zhddkk.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class WsUserDTO {

    private Integer id;

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String password;

    @NotBlank
    @NotNull
    private String address;
}
