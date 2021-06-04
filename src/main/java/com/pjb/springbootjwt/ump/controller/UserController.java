package com.pjb.springbootjwt.ump.controller;

import com.pjb.springbootjwt.common.annotation.NeedRefreashToken;
import com.pjb.springbootjwt.common.redis.RedisUtil;
import com.pjb.springbootjwt.common.vo.Result;
import com.pjb.springbootjwt.ump.bean.TokenBean;
import com.pjb.springbootjwt.ump.constant.Constant;
import com.pjb.springbootjwt.ump.domain.UserDO;
import com.pjb.springbootjwt.ump.service.UserService;
import com.pjb.springbootjwt.util.EhcacheUtils;
import com.pjb.springbootjwt.util.JwtUtils;
import io.jsonwebtoken.Jwt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jinbin
 * @date 2018-07-08 20:45
 */
@Controller
@RequestMapping("/api/user")
@Api(tags = "用户操作接口")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    RedisUtil redisUtil;

    @ApiOperation("去登陆")
    @GetMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    /**
     * 登录
     * 无需token
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @ApiOperation("登陆")
    @PostMapping("/login")
    @ResponseBody
    public Result<TokenBean> login(
            @ApiParam(name = "username", value = "用户名", required = true) @RequestParam(value = "username") String username,
            @ApiParam(name = "password", value = "密码", required = true) @RequestParam(value = "password") String password
    ){
        logger.info("进入登录控制层,用户:{}",username);

//        Subject subject = SecurityUtils.getSubject();
//        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
//        try {
//            //会去调用CustomRealm.doGetAuthenticationInfo方法
//            subject.login(usernamePasswordToken);
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//            return Result.build(900, "用户名或者密码错误", new TokenBean());
//        }

        logger.info("进入登录控制层,用户:{}",username);
        UserDO userInfo = userService.findByUsername(username);
        if(userInfo == null){
            return Result.build(901,"用户不存在", new TokenBean());
        }

        if (!userInfo.getPassword().equals(password)){
            return Result.build(902,"密码错误", new TokenBean());
        }
        String userId = userInfo.getId();

        TokenBean tokenBean = new TokenBean();
        String token = JwtUtils.createToken(userId);
        String refreashToken = JwtUtils.createRefreashToken(userId);

        tokenBean.setUserId(userId);
        tokenBean.setToken(token);
        tokenBean.setRefreashToken(refreashToken);
        tokenBean.setTokenExpire(System.currentTimeMillis()+JwtUtils.TOKEN_EXPIRE);
        tokenBean.setRefreashTokenExpire(System.currentTimeMillis()+JwtUtils.REFREASH_TOKEN_EXPIRE);

        return Result.ok(tokenBean);
    }

    /**
     * 刷新token
     * head中需要携带refreashToken
     * Authorization:xxxxx.xx.xx
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @NeedRefreashToken
    @GetMapping("/refreashToken")
    @ResponseBody
    public Result<String> refreashToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        logger.info("进入刷新缓存控制层");
        try {
            String token = httpServletRequest.getHeader("Authorization");// 从 http 请求头中取出 token
            String userId = JwtUtils.getUserId(token);
            //校验token
            JwtUtils.verifyToken(token, userId);

            //验证通过的话,创建新的token返回给前端
            String newToken = JwtUtils.createToken(userId);
            return Result.ok(newToken);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("刷新token异常:{}", e.getMessage());
        }

        return Result.fail();
    }
}