package com.pjb.springbootjwt.zhddkk.controller;

import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.dto.WsUserDTO;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Validated
@RestController
@RequestMapping("/valid")
public class ValidateTestController {

    /**
     * 在Controller直接使用验证注解
     * 需要在类上添加@Validated
     */
    @GetMapping("/email")
    public Result<String> validEmail(@NotBlank(message = "邮箱不能为空") @Email(message = "邮箱格式不正确") String email) {
        return Result.ok();
    }

    /**
     * 在Controller直接使用验证注解
     * 需要在类上添加@Validated
     */
    @PostMapping("/userDTO")
    public Result<WsUserDTO> validUserDTO(@Validated @RequestBody WsUserDTO wsUserDTO) {
        return Result.ok(wsUserDTO);
    }
}
