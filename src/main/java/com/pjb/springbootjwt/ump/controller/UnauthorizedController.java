package com.pjb.springbootjwt.ump.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/unauthorized")
@Api(tags = "未授权接口")
public class UnauthorizedController {

    public String unauthorized(){
        return "unauthorized";
    }
}
