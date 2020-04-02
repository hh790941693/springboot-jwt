package com.pjb.springbootjwt.ump.controller;

import com.pjb.springbootjwt.common.vo.Result;
import com.pjb.springbootjwt.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 需要token、role或Permission访问的接口
 */
@RestController
@RequestMapping("/authc")
@Api(tags = "需要token访问的接口")
public class AuthcController {

    private static Logger logger = LoggerFactory.getLogger(AuthcController.class);

    @GetMapping("/index")
    @RequiresAuthentication
    @ApiOperation("首页")
    public Result<String> index(){
        String userId = JwtUtils.getLoginUserId();
        logger.info("用户{}成功访问首页", userId);
        return Result.ok("用户"+userId+"正常访问index");
    }

    @RequiresRoles("admin")
    @GetMapping("/roleAdmin")
    @RequiresAuthentication
    @ApiOperation("需要admin角色访问")
    public Result<String> roleAdmin(){
        Subject subject = SecurityUtils.getSubject();
        String userId = JwtUtils.getLoginUserId();
        logger.info("用户{}有admin角色", userId);
        return Result.ok("用户"+userId+"具有admin角色");
    }

    @RequiresPermissions("add")
    @GetMapping("/permAdd")
    @RequiresAuthentication
    @ApiOperation("需要add权限访问")
    public Result<String> permAdd(){
        Subject subject = SecurityUtils.getSubject();
        String userId = JwtUtils.getLoginUserId();
        logger.info("用户{}有add权限", userId);
        return Result.ok("用户"+userId+"具有add权限");
    }

    /**
     * 获取业务数据
     * 需要token

     * @return
     */
    @GetMapping("/getMessage")
    @RequiresAuthentication
    @ApiOperation("访问业务数据")
    public Result<String> getMessage(){
        logger.info("进入访问业务数据控制层");
        String userId = JwtUtils.getLoginUserId();
        logger.info("当前登录用户id:{}", userId);
        return Result.ok("用户"+userId+"正常访问getMessage");
    }
}
