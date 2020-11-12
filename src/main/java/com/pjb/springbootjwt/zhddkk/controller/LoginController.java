package com.pjb.springbootjwt.zhddkk.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.common.redis.RedisUtil;
import com.pjb.springbootjwt.common.uploader.config.UploadConfig;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.bean.SessionInfoBean;
import com.pjb.springbootjwt.zhddkk.bean.SystemInfoBean;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.domain.WsChatlogDO;
import com.pjb.springbootjwt.zhddkk.domain.WsCommonDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUserProfileDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import com.pjb.springbootjwt.zhddkk.enumx.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.enumx.OperationEnum;
import com.pjb.springbootjwt.zhddkk.interceptor.WsInterceptor;
import com.pjb.springbootjwt.zhddkk.service.*;
import com.pjb.springbootjwt.zhddkk.util.*;
import com.pjb.springbootjwt.zhddkk.websocket.WebSocketConfig;
import com.pjb.springbootjwt.zhddkk.websocket.ZhddWebSocket;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class LoginController {

    private static final int COOKIE_TIMEOUT = 1800; //cookie过期时间 30分钟

    private static final String REDIS_KEY_PREFIX = "ws_"; //登陆用户的redis缓存前缀

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private static Map<String,String> configMap = WsInterceptor.getConfigMap();

    @Autowired
    private WebSocketConfig webSocketConfig;

    @Autowired
    private WsUsersService wsUsersService;

    @Autowired
    private WsUserProfileService wsUserProfileService;

    @Autowired
    private UploadConfig uploadConfig;

    @Autowired
    private WsCommonService wsCommonService;

    @Autowired
    private WsChatlogService wsChatlogService;

    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;

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
        SessionInfoBean sessionInfoBean = (SessionInfoBean)request.getSession().getAttribute(CommonConstants.SESSION_INFO);
        if (null != sessionInfoBean){
            model.addAttribute(CommonConstants.S_USER, sessionInfoBean.getUser());
            model.addAttribute(CommonConstants.S_PASS, sessionInfoBean.getPassword());
            model.addAttribute(CommonConstants.S_WEBSERVERIP, sessionInfoBean.getWebserverIp());
            model.addAttribute(CommonConstants.S_WEBSERVERPORT, sessionInfoBean.getWebserverPort());
            model.addAttribute(CommonConstants.S_IMG, sessionInfoBean.getSelfImg());
            model.addAttribute(CommonConstants.S_USER_AGENT, sessionInfoBean.getUserAgent());
            return "ws/wsclientIndex";
        }
        return "ws/login";
    }

    /**
     * 登录按钮事件
     *
     * @param model
     * @return
     */
    @OperationLogAnnotation(type= OperationEnum.UPDATE,module= ModuleEnum.LOGIN,subModule="",describe="登录")
    @RequestMapping(value = "wslogin.do", method = RequestMethod.POST)
    public void wsclient(Model model, @RequestParam("user")String user, @RequestParam("pass")String pass,
                           HttpServletRequest request, HttpServletResponse response) throws Exception {
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
            request.setAttribute("user", user);
            request.setAttribute("detail", "当前用户未注册,请先注册!");
            request.getRequestDispatcher("loginfail.page").forward(request, response);
            return;
        }

        if (isEnable.equals("0")){
            // 此账号已被禁用
            request.setAttribute("user", user);
            request.setAttribute("detail", "当前用户已被禁用!");
            request.getRequestDispatcher("loginfail.page").forward(request, response);
            return;
        }

        if (isLogin){
            // 如果已登录
            request.setAttribute("user", user);
            request.setAttribute("detail", "当前用户已经登录了,请不要重复登录!");
            request.getRequestDispatcher("loginfail.page").forward(request, response);
            return;
        }

        // 如果密码不对
        if (!pass.equals(dbPassDecrypted)){
            request.setAttribute("user", user);
            request.setAttribute("detail", "密码不对!");
            request.getRequestDispatcher("loginfail.page").forward(request, response);
            return;
        }

        String webserverip = webSocketConfig.getAddress();
        String webserverPort = webSocketConfig.getPort();
        logger.info("webserverip:{} webserverPort:{}",webserverip, webserverPort);
        configMap.put(CommonConstants.S_WEBSERVERIP, webserverip);
        configMap.put(CommonConstants.S_WEBSERVERPORT, webserverPort);

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
        System.out.println("创建SESSION:" + sessionId);
        logger.info("创建SESSION: {}", sessionId);
        //session.setMaxInactiveInterval(CommonConstants.SESSION_TIMEOUT); //session不活动失效时间
        request.getSession().setAttribute(CommonConstants.S_USER, user);
        request.getSession().setAttribute(CommonConstants.S_PASS, dbPass);
        request.getSession().setAttribute(CommonConstants.S_WEBSERVERIP, webserverip);
        request.getSession().setAttribute(CommonConstants.S_WEBSERVERPORT, webserverPort);
        request.getSession().setAttribute(CommonConstants.S_IMG, selfImg);
        request.getSession().setAttribute(CommonConstants.S_USER_AGENT, shortAgent);

        SessionInfoBean sessionInfoBean = new SessionInfoBean(user, dbPass, webserverip, webserverPort, selfImg, shortAgent);
        request.getSession().setAttribute(CommonConstants.SESSION_INFO, sessionInfoBean);

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

        SessionInfoBean sessionInfoBean = (SessionInfoBean)request.getSession().getAttribute(CommonConstants.SESSION_INFO);
        model.addAttribute(CommonConstants.S_USER, sessionInfoBean.getUser());
        model.addAttribute(CommonConstants.S_PASS, sessionInfoBean.getPassword());
        model.addAttribute(CommonConstants.S_WEBSERVERIP, sessionInfoBean.getWebserverIp());
        model.addAttribute(CommonConstants.S_WEBSERVERPORT, sessionInfoBean.getWebserverPort());
        model.addAttribute(CommonConstants.S_IMG, sessionInfoBean.getSelfImg());
        model.addAttribute(CommonConstants.S_USER_AGENT, sessionInfoBean.getUserAgent());
        return "ws/wsclientIndex";
    }

    /**
     * 登录失败
     * @param model
     * @return
     */
    @OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.REGISTER,subModule="",describe="登录失败页面")
    @RequestMapping(value = "loginfail.page")
    public String loginfail(Model model) {
        logger.debug("访问loginfail.page");
        return "ws/loginfail";
    }


    /**
     * 注册按钮事件
     *
     */
    @OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.REGISTER,subModule="",describe="注册首页")
    @RequestMapping(value = "register.page")
    public String toRegister(Model model) {
        logger.debug("访问register.page");
        return "ws/register";
    }

    /**
     * 注册按钮事件
     *
     * @param model
     * @return
     */
    @OperationLogAnnotation(type=OperationEnum.INSERT,module=ModuleEnum.REGISTER,subModule="",describe="注册账号")
    @RequestMapping(value = "wsregister.do", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public String wsregister(Model model,@RequestParam("user")String user,
                             @RequestParam("pass")String pass,
                             @RequestParam("headImg")String headImg,
                             @RequestParam("confirmPass")String confirmPass,
                             @RequestParam("select1")String question1,@RequestParam("answer1")String answer1,
                             @RequestParam("select2")String question2,@RequestParam("answer2")String answer2,
                             @RequestParam("select3")String question3,@RequestParam("answer3")String answer3) {
        // 是否已注册过  true:已注册
        boolean isUserExist = false;

        List<WsUsersDO> userList = wsUsersService.selectList(null);
        for (WsUsersDO u : userList) {
            if (u.getName().equals(user)) {
                isUserExist = true;
                break;
            }
        }
        if (isUserExist){
            // 如果已经注册
            model.addAttribute("user", user);
            model.addAttribute("detail","当前用户已注册,请直接登录!");
            return "failed";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 插入用户账号
        WsUsersDO insrtWu = new WsUsersDO();
        insrtWu.setName(user);
        //对密码进行加密
        String encryptPass = SecurityAESUtil.encryptAES(pass, CommonConstants.AES_PASSWORD);
        insrtWu.setPassword(encryptPass);
        insrtWu.setRegisterTime(sdf.format(new Date()));
        insrtWu.setLastLoginTime(sdf.format(new Date()));
        insrtWu.setQuestion1(question1);
        insrtWu.setAnswer1(answer1);
        insrtWu.setQuestion2(question2);
        insrtWu.setAnswer2(answer2);
        insrtWu.setQuestion3(question3);
        insrtWu.setAnswer3(answer3);
        wsUsersService.insert(insrtWu);

        //插入注册日志
        SimpleDateFormat sdfx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        WsChatlogDO loginLog = new WsChatlogDO();
        loginLog.setTime(sdfx.format(new Date()));
        loginLog.setUser(user);
        loginLog.setToUser("");
        loginLog.setMsg("注册成功");
        wsChatlogService.insert(loginLog);

        //插入用户profile信息
        int userProfileCnt = wsUserProfileService.selectCount(new EntityWrapper<WsUserProfileDO>().eq("user_id", insrtWu.getId()));
        if (userProfileCnt == 0){
            WsUserProfileDO wsUserProfileDO = new WsUserProfileDO();
            wsUserProfileDO.setUserId(insrtWu.getId());
            wsUserProfileDO.setUserName(user);
            wsUserProfileDO.setCreateTime(new Date());
            if (StringUtils.isNotBlank(headImg)){
                wsUserProfileDO.setImg(headImg);
            }
            wsUserProfileService.insert(wsUserProfileDO);
        }

        model.addAttribute("user", user);
        model.addAttribute("pass", pass);
        return "success";
    }

    @OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.REGISTER,subModule="",describe="查询问题列表")
    @RequestMapping(value = "queryAllCommonData.do")
    @ResponseBody
    public Map<String, List<WsCommonDO>> queryAllCommonData() {
        logger.debug("访问queryAllCommonData.do");
        Map<String, List<WsCommonDO>> map = buildCommonData();
        return map;
    }

    /**
     * 忘记密码页面
     * @return
     */
    @OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.FORGET_PASSWORD,subModule="",describe="忘记密码首页")
    @RequestMapping(value = "forgetPassword.page")
    public String forgetPassword(Model model,@RequestParam("user")String user) {
        logger.debug("访问forgetPassword.page");
        model.addAttribute("user", user);
        return "ws/forgetPassword";
    }

    /**
     * 更新密码事件
     *
     */
    @OperationLogAnnotation(type=OperationEnum.UPDATE,module=ModuleEnum.FORGET_PASSWORD,subModule="",describe="更新密码")
    @RequestMapping(value = "updatePassword.do",method=RequestMethod.POST)
    @ResponseBody
    public Result<String> updatePassword(Model model,@RequestParam("user")String user,
                                         @RequestParam("pass")String newPass,
                                         @RequestParam("confirmPass")String confirmPass,
                                         @RequestParam("select1")String question1,@RequestParam("answer1")String answer1,
                                         @RequestParam("select2")String question2,@RequestParam("answer2")String answer2,
                                         @RequestParam("select3")String question3,@RequestParam("answer3")String answer3) {
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
        if (null == wsUsersDO){
            return Result.fail();
        }

        if (wsUsersDO.getQuestion1().equals(question1) && wsUsersDO.getAnswer1().equals(answer1)
                && wsUsersDO.getQuestion2().equals(question2) && wsUsersDO.getAnswer2().equals(answer2)
                && wsUsersDO.getQuestion3().equals(question3) && wsUsersDO.getAnswer3().equals(answer3)
                && newPass.equals(confirmPass)) {
            //对新密码进行加密
            String newPassEncrypt = SecurityAESUtil.encryptAES(newPass, CommonConstants.AES_PASSWORD);
            wsUsersDO.setPassword(newPassEncrypt);
            boolean updateFlag = wsUsersService.updateById(wsUsersDO);
            if (updateFlag) {
                return Result.ok();
            }
        }

        return Result.fail();
    }

    /**
     * 查询系统信息
     * @return
     */
    @ResponseBody
    @GetMapping("/querySystemInfo")
    public Result<SystemInfoBean> querySystemInfo(){
        SystemInfoBean systemInfoBean = new SystemInfoBean();
        systemInfoBean.setOsName(System.getProperty("os.name"));
        systemInfoBean.setJavaHome(System.getProperty("java.home"));
        systemInfoBean.setJavaVersion(System.getProperty("java.version"));
        systemInfoBean.setDbVersion(OsUtil.queryMysqlVersion());

        Properties props = System.getProperties();
        String osName = props.getProperty("os.name");
        boolean nginxPs = false;
        if (osName.contains("windows") || osName.contains("Windows")) {
            nginxPs = OsUtil.findWindowProcess("nginx.exe");
        }else{
            String result = ExcuteLinuxCmdUtil.executeLinuxCmd("ps -ef | grep nginx | grep -v grep");
            System.out.println(result);
            if (result.contains("nginx")){
                nginxPs = true;
            }
        }
        systemInfoBean.setNginxFlag(nginxPs);

        String storePath = uploadConfig.getStorePath();
        File sf = new File(storePath);
        if (sf.isDirectory()){
            systemInfoBean.setShareDir(storePath+":共享目录正常");
        }else{
            systemInfoBean.setShareDir(storePath+":共享目录不存在");
        }

        return Result.ok(systemInfoBean);
    }

    /**
     * 生成二维码并显示出来
     *
     * @return
     */
    @OperationLogAnnotation(type=OperationEnum.INSERT,module=ModuleEnum.OTHER,subModule="",describe="显示二维码")
    @RequestMapping(value = "showQRCode.do", method = RequestMethod.POST)
    @ResponseBody
    public String showQRCode(HttpServletRequest request)
    {
        ServletContext sc = request.getServletContext();
        String contextPathx = sc.getContextPath();
        String scheme = request.getScheme();

        String savePath = uploadConfig.getStorePath();
        String qrCodeFilename = "qrcode.png";
        String savePathAbs = savePath + File.separator + qrCodeFilename;
        System.out.println("二维码文件路径:"+savePathAbs);

        boolean isNeedGenerateCode = false;
        File qrCodeFile = new File(savePathAbs);
        if (!qrCodeFile.exists()) {
            isNeedGenerateCode = true;
        }else {
            long lastModified = qrCodeFile.lastModified();
            Date lastModifiedDate = new Date(lastModified);
            long diff = new Date().getTime()-lastModifiedDate.getTime();
            long minutesDiff = diff / (1000*60);
            System.out.println("距离上次生成时间有:"+minutesDiff+"分钟");
            if (minutesDiff >= 60) {
                //如果超过一个小时则可以重新生成二维码
                isNeedGenerateCode = true;
            }
        }

        if (isNeedGenerateCode) {
            String text = scheme + "://" + serverAddress + ":" + serverPort;
            System.out.println("登录地址:"+text);
            String qrCodeFilePath = null;
            try {
                qrCodeFilePath = QRCodeUtil.generateQRCode(text, 200, 200, "png", savePathAbs);
                System.out.println("二维码文件新路径:"+qrCodeFilePath);
            } catch (Exception e) {
                System.out.println("生成二维码失败!"+e.getMessage());
                e.printStackTrace();
            }
        }
        return uploadConfig.getViewUrl()+"/"+qrCodeFilename;
    }

    /**
     * 获取用户的问题答案
     * getUserQuestion
     */
    @OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.OTHER,subModule="",describe="获取用户密保信息")
    @RequestMapping(value = "getUserQuestion.json")
    @ResponseBody
    public Object getUserQuestion(@RequestParam("user")String user) {
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
        if (null != wsUsersDO) {
            return JsonUtil.javaobject2Jsonobject(wsUsersDO);
        }else{
            return "failed";
        }
    }

    /**
     * 检查某用户是否已经注册过
     *
     * 0:存在  1:不存在
     */
    @OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.REGISTER,subModule="",describe="检查用户是否已经注册")
    @RequestMapping(value="checkUserRegisterStatus.json",method=RequestMethod.POST)
    @ResponseBody
    public Result<String> checkUserRegisterStatus(@RequestParam("user") String user) {
        int userCount = wsUsersService.selectCount(new EntityWrapper<WsUsersDO>().eq("name", user));
        if (userCount>0){
            return Result.fail();
        }

        return Result.ok();
    }

    private Map<String,List<WsCommonDO>> buildCommonData() {
        Map<String,List<WsCommonDO>> commonMap = new HashMap<String,List<WsCommonDO>>();
        List<WsCommonDO> commonList = wsCommonService.selectList(null);

        for (WsCommonDO common : commonList) {
            String type = common.getType();
            String name = common.getName();

            if (CommonUtil.validateEmpty(type) || CommonUtil.validateEmpty(name)) {
                continue;
            }

            if (commonMap.containsKey(type)) {
                List<WsCommonDO> tmpCommonList = commonMap.get(type);
                tmpCommonList.add(common);
            }else {
                List<WsCommonDO> tmpDicList = new ArrayList<WsCommonDO>();
                tmpDicList.add(common);
                commonMap.put(type, tmpDicList);
            }
        }

        return commonMap;
    }
}
