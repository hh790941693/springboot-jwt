package com.pjb.springbootjwt.ump.controller;

import com.pjb.springbootjwt.common.vo.Result;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    @RequiresAuthentication
    public Result<String> test(){
        return Result.ok("test");
    }
}
