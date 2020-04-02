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
        //检查缓存中是否有token
        //Object cacheToken = EhcacheUtils.get(EhcacheUtils.default_cache_name, TOKEN_PREFIX+userInfo.getId());
        Object cacheToken = redisUtil.get(Constant.TOKEN_PREFIX+userId);

        TokenBean tokenBean = new TokenBean();
        if (null != cacheToken){
            logger.info("直接从缓存中取出token返回");
            tokenBean = (TokenBean)cacheToken;
            //检查refreashToken是否已经过期
            Long refreashTokenExpire = tokenBean.getRefreashTokenExpire();
            if (refreashTokenExpire< System.currentTimeMillis()){
                logger.info("缓存中的refreashToken已经过期,重新生成token");
                String token = JwtUtils.createToken(userId, userInfo.getUsername() + userInfo.getPassword(), JwtUtils.TOKEN_EXPIRE);
                String refreashToken = JwtUtils.createToken(userId, userId + userInfo.getPassword(), JwtUtils.REFREASH_TOKEN_EXPIRE);

                tokenBean.setId(userId);
                tokenBean.setToken(token);
                tokenBean.setRefreashToken(refreashToken);
                tokenBean.setTokenExpire(System.currentTimeMillis()+JwtUtils.TOKEN_EXPIRE);
                tokenBean.setRefreashTokenExpire(System.currentTimeMillis()+JwtUtils.REFREASH_TOKEN_EXPIRE);

                //将token存入缓存
                EhcacheUtils.put(EhcacheUtils.default_cache_name, Constant.TOKEN_PREFIX+userId, tokenBean);

                //将token存入redis
                redisUtil.set(Constant.TOKEN_PREFIX+userId, tokenBean);
            }else{
                logger.info("缓存中的refreashToken没有过期");
            }
        }else {
            logger.info("第一次登录,生成token并存入缓存");
            String token = JwtUtils.createToken(userId, userInfo.getUsername() + userInfo.getPassword(), JwtUtils.TOKEN_EXPIRE);
            String refreashToken = JwtUtils.createToken(userId, userId + userInfo.getPassword(), JwtUtils.REFREASH_TOKEN_EXPIRE);

            tokenBean.setId(userId);
            tokenBean.setToken(token);
            tokenBean.setRefreashToken(refreashToken);
            tokenBean.setTokenExpire(System.currentTimeMillis()+JwtUtils.TOKEN_EXPIRE);
            tokenBean.setRefreashTokenExpire(System.currentTimeMillis()+JwtUtils.REFREASH_TOKEN_EXPIRE);

            //将token存入缓存
            EhcacheUtils.put(EhcacheUtils.default_cache_name, Constant.TOKEN_PREFIX+userId, tokenBean);

            //将token存入redis
            redisUtil.set(Constant.TOKEN_PREFIX+userId, tokenBean);
        }
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
            UserDO userInfo = userService.findUserById(userId);
            String newToken = JwtUtils.createToken(userId, userInfo.getUsername()+userInfo.getPassword(), JwtUtils.TOKEN_EXPIRE);

            Object cacheToken = EhcacheUtils.get(EhcacheUtils.default_cache_name, Constant.TOKEN_PREFIX+userId);
            if (null != cacheToken){
                TokenBean tokenBean = (TokenBean) cacheToken;
                tokenBean.setToken(newToken);
            }
            EhcacheUtils.remove(EhcacheUtils.default_cache_name, Constant.TOKEN_PREFIX+userId);

            //从redis读取缓存
            Object redisToken = redisUtil.get(Constant.TOKEN_PREFIX+userId);
            if (null != redisToken){
                TokenBean tokenBean = (TokenBean)redisToken;
                tokenBean.setToken(newToken);
                //更新缓存token
                EhcacheUtils.put(EhcacheUtils.default_cache_name, Constant.TOKEN_PREFIX+userId, tokenBean);

                //更新redis
                redisUtil.set(Constant.TOKEN_PREFIX+userId, tokenBean);
            }

            return Result.ok(newToken);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("刷新token异常:{}", e.getMessage());
        }

        return Result.fail();
    }
}