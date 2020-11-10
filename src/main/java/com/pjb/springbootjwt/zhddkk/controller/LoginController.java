package com.pjb.springbootjwt.zhddkk.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.common.redis.RedisUtil;
import com.pjb.springbootjwt.common.uploader.config.UploadConfig;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.domain.WsUserProfileDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import com.pjb.springbootjwt.zhddkk.enumx.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.enumx.OperationEnum;
import com.pjb.springbootjwt.zhddkk.interceptor.WsInterceptor;
import com.pjb.springbootjwt.zhddkk.service.*;
import com.pjb.springbootjwt.zhddkk.util.JsonUtil;
import com.pjb.springbootjwt.zhddkk.util.SecurityAESUtil;
import com.pjb.springbootjwt.zhddkk.websocket.WebSocketConfig;
import com.pjb.springbootjwt.zhddkk.websocket.ZhddWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    private static final int COOKIE_TIMEOUT = 1800; //cookie过期时间 30分钟

    private static final String REDIS_KEY_PREFIX = "ws_"; //登陆用户的redis缓存前缀

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private static SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");

    private static Map<String,String> configMap = WsInterceptor.getConfigMap();

    @Autowired
    private WebSocketConfig webSocketConfig;

    @Autowired
    private WsUsersService wsUsersService;

    @Autowired
    private WsUserProfileService wsUserProfileService;

    /**
     * 首页登录
     *
     * @param model
     * @param request
     * @param response
     * @return
     */
    @OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.LOGIN,subModule="",describe="登陆首页页面")
    @RequestMapping({"","/","/index"})
    public String v_home(Model model, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            boolean isAdmin = false;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CommonConstants.S_USER) && cookie.getMaxAge() != 0) {
                    model.addAttribute(CommonConstants.S_USER, cookie.getValue());
                    if (cookie.getValue().equals("admin")){
                        isAdmin = true;
                    }
                }else if (cookie.getName().equals(CommonConstants.S_PASS) && cookie.getMaxAge() != 0) {
                    if (isAdmin){
                        //不保存admin密码
                        model.addAttribute(CommonConstants.S_PASS, "");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    }else {
                        //对密码进行解密
                        String passDecrypt = SecurityAESUtil.decryptAES(cookie.getValue(), CommonConstants.AES_PASSWORD);
                        model.addAttribute(CommonConstants.S_PASS, passDecrypt);
                    }
                }
            }
        }else{
            model.addAttribute(CommonConstants.S_USER, "");
            model.addAttribute(CommonConstants.S_PASS, "");
        }
        return "ws/login";
    }

    /**
     * 登录
     *
     * @param model
     * @return
     */
    @OperationLogAnnotation(type= OperationEnum.UPDATE,module= ModuleEnum.LOGIN,subModule="",describe="登录")
    @RequestMapping(value = "wslogin.do", method = RequestMethod.POST)
    public void wsclient(Model model, @RequestParam("user")String user, @RequestParam("pass")String pass,
                           HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 检查当前用户是否已经登录过
        Map<String, ZhddWebSocket> socketMap = ZhddWebSocket.getClients();

        // 是否已登录
        boolean isLogin = false;
        // 是否已注册  true:已注册
        boolean isRegister = false;
        // 是否被禁用   0:已禁用
        String isEnable = "1";
        String dbPass = "";          //数据库密文密码
        String dbPassDecrypted = ""; //数据库明文密码

        WsUsersDO curUserObj = null;
        List<WsUsersDO> userList = wsUsersService.selectList(null);
        for (WsUsersDO u : userList){
            if (u.getName().equals(user)) {
                curUserObj = u;
                isRegister = true;
                dbPass = u.getPassword();
                dbPassDecrypted = SecurityAESUtil.decryptAES(dbPass, CommonConstants.AES_PASSWORD);
                isEnable = u.getEnable();
                break;
            }
        }

        if (socketMap.containsKey(user)){
            isLogin = true;
        }

        if (!isRegister){
            // 用户未注册
            model.addAttribute("user", user);
            model.addAttribute("detail","当前用户未注册,请先注册!");
            response.sendRedirect("loginfail.page");
            return;
        }

        if (isEnable.equals("0")){
            // 此账号已被禁用
            model.addAttribute("user", user);
            model.addAttribute("detail","当前用户已被禁用!");
            response.sendRedirect("loginfail.page");
            return;
        }

        if (isLogin){
            // 如果已登录
            model.addAttribute("user", user);
            model.addAttribute("detail","当前用户已经登录了,请不要重复登录!");
            response.sendRedirect("loginfail.page");
            return;
        }

        // 如果密码不对
        if (!pass.equals(dbPassDecrypted)){
            model.addAttribute("user", user);
            model.addAttribute("detail","密码不对!");
            response.sendRedirect("loginfail.page");
            return;
        }

        String webserverip = webSocketConfig.getAddress();
        String webserverPort = webSocketConfig.getPort();
        logger.info("------------------配置信息------------------:"+configMap);
        logger.info("webserverip:{} webserverPort:{}",webserverip, webserverPort);

        String userAgent = request.getHeader("User-Agent");
        logger.info("userAgent:"+userAgent);
        String shortAgent = "unknown device";
        try {
            shortAgent = userAgent.split("\\(")[1].split("\\)")[0].replaceAll("\\(", "").replaceAll("\\)", "");
        }catch (Exception e) {
            logger.warn("获取客户端信息失败!"+e.getMessage());
        }

        //个人头像
        WsUserProfileDO wup = wsUserProfileService.selectOne(new EntityWrapper<WsUserProfileDO>().eq("user_name", user));
        String selfImg = "";
        if (null != wup) {
            selfImg = wup.getImg();
        }

        //记录cookie
        Cookie userCookie = new Cookie(CommonConstants.S_USER,user);
        Cookie passCookie = new Cookie(CommonConstants.S_PASS,dbPass);
        Cookie webserveripCookie = new Cookie(CommonConstants.S_WEBSERVERIP, webserverip);
        Cookie webserverportCookie = new Cookie(CommonConstants.S_WEBSERVERPORT, webserverPort);
        userCookie.setPath("/");
        userCookie.setMaxAge(COOKIE_TIMEOUT);//用户名30分钟
        passCookie.setPath("/");
        passCookie.setMaxAge(600);//密码10分钟
        webserveripCookie.setPath("/");
        webserveripCookie.setMaxAge(COOKIE_TIMEOUT);
        webserverportCookie.setPath("/");
        webserverportCookie.setMaxAge(COOKIE_TIMEOUT);
        response.addCookie(userCookie);
        response.addCookie(passCookie);
        response.addCookie(webserveripCookie);
        response.addCookie(webserverportCookie);

        //session
        //request.getSession().invalidate();
        String sessionId = request.getSession().getId();
        logger.info("sessionId: {}", sessionId);
        //session.setMaxInactiveInterval(CommonConstants.SESSION_TIMEOUT); //session不活动失效时间
        request.getSession().setAttribute(CommonConstants.S_USER, user);
        request.getSession().setAttribute(CommonConstants.S_PASS, dbPass);
        request.getSession().setAttribute(CommonConstants.S_WEBSERVERIP, webserverip);
        request.getSession().setAttribute(CommonConstants.S_WEBSERVERPORT, webserverPort);
        request.getSession().setAttribute(CommonConstants.S_IMG, selfImg);
        request.getSession().setAttribute(CommonConstants.S_USER_AGENT, shortAgent);

        String redisKey = REDIS_KEY_PREFIX+user;
        try {
            if (null != curUserObj) {
                curUserObj.setState("1");//在线
                String redisValue = JsonUtil.javaobject2Jsonstr(curUserObj);
                logger.debug("设置redis缓存,key:"+redisKey+"  value:"+redisValue);
                //redisUtil.set(redisKey, redisValue);
            }
        }catch (Exception e) {
            logger.debug("设置redis缓存失败,key:"+redisKey+" error:"+e.getMessage());
        }

        response.sendRedirect("wsclientIndex.page");
    }

    @OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.REGISTER,subModule="",describe="登录成功页面")
    @RequestMapping(value = "wsclientIndex.page")
    public String wsclientIndex(HttpServletRequest request, Model model) {
        logger.debug("访问wsclientIndex.page");
        String user = (String)request.getSession().getAttribute(CommonConstants.S_USER);
        String password = (String)request.getSession().getAttribute(CommonConstants.S_PASS);
        String webserverIp = (String)request.getSession().getAttribute(CommonConstants.S_WEBSERVERIP);
        String webserverPort = (String)request.getSession().getAttribute(CommonConstants.S_WEBSERVERPORT);
        String selfImg = (String)request.getSession().getAttribute(CommonConstants.S_IMG);
        String userAgent = (String)request.getSession().getAttribute(CommonConstants.S_USER_AGENT);

        model.addAttribute(CommonConstants.S_USER, user);
        model.addAttribute(CommonConstants.S_PASS, password);
        model.addAttribute(CommonConstants.S_WEBSERVERIP, webserverIp);
        model.addAttribute(CommonConstants.S_WEBSERVERPORT, webserverPort);
        model.addAttribute(CommonConstants.S_IMG, selfImg);
        model.addAttribute(CommonConstants.S_USER_AGENT, userAgent);
        return "ws/wsclientIndex";
    }

    @OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.REGISTER,subModule="",describe="登录失败页面")
    @RequestMapping(value = "loginfail.page")
    public String loginfail(Model model) {
        logger.debug("访问loginfail.page");
        return "ws/loginfail";
    }
}
