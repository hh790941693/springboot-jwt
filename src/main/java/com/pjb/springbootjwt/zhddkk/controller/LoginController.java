package com.pjb.springbootjwt.zhddkk.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
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
import com.pjb.springbootjwt.zhddkk.constants.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.constants.OperationEnum;
import com.pjb.springbootjwt.zhddkk.interceptor.WsInterceptor;
import com.pjb.springbootjwt.zhddkk.service.*;
import com.pjb.springbootjwt.zhddkk.util.*;
import com.pjb.springbootjwt.zhddkk.websocket.WebSocketConfig;
import com.pjb.springbootjwt.zhddkk.websocket.ZhddWebSocket;
import com.wf.captcha.ArithmeticCaptcha;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class LoginController {

    private static final String REDIS_KEY_PREFIX = "ws_"; //登陆用户的redis缓存前缀

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private static final Map<String,String> configMap = WsInterceptor.getConfigMap();

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

    @Autowired
    private MessageSource messageSource;

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

        // 如果用户已登陆过，则直接跳转登陆成功首页
        SessionInfoBean sessionInfoBean = (SessionInfoBean)request.getSession().getAttribute(CommonConstants.SESSION_INFO);
        if (null != sessionInfoBean){
            model.addAttribute(CommonConstants.SESSION_INFO, sessionInfoBean);
            return "ws/wsclientIndex";
        }

        // 检查cookie
        Cookie[] cookies = request.getCookies();
        Locale locale = null;
        if (null != cookies) {
            boolean isAdmin = false;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CommonConstants.S_USER) && cookie.getMaxAge() != 0) {
                    model.addAttribute(CommonConstants.S_USER, cookie.getValue());
                    if (cookie.getValue().equals(CommonConstants.ADMIN_USER)){
                        isAdmin = true;
                    }
                } else if (cookie.getName().equals(CommonConstants.S_PASS) && cookie.getMaxAge() != 0) {
                    if (isAdmin) {
                        //不保存admin密码
                        model.addAttribute(CommonConstants.S_PASS, "");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    } else {
                        //对密码进行解密
                        String passDecrypt = SecurityAESUtil.decryptAES(cookie.getValue(), CommonConstants.AES_PASSWORD);
                        model.addAttribute(CommonConstants.S_PASS, passDecrypt);
                    }
                } else if (cookie.getName().equals(CommonConstants.C_LANG) && cookie.getMaxAge() != 0) {
                    model.addAttribute(CommonConstants.C_LANG, cookie.getValue());
                    String language = cookie.getValue().split("_")[0];
                    String country = cookie.getValue().split("_")[1];
                    locale = new Locale(language, country);
                }
            }
        }

        if (null == locale){
            model.addAttribute(CommonConstants.C_LANG, CommonConstants.LANG_ZH);
            locale = new Locale("zh","CN");
        }

        request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
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
                         @RequestParam("verifyCode")String verifyCodeInput,
                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 检查当前用户是否已经登录过
        Map<String, ZhddWebSocket> socketMap = ZhddWebSocket.getClients();

        // 获取用户信息
        WsUsersDO curUserObj = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));

        // 如果用户信息不存在,提示用户去注册
        if (null == curUserObj){
            // 用户未注册
            request.setAttribute("user", user);
            request.setAttribute("detail", "当前用户未注册,请先注册!");
            request.getRequestDispatcher("loginfail.page").forward(request, response);
            return;
        }

        // 是否被禁用   0:已禁用
        String isEnable = curUserObj.getEnable();
        if (isEnable.equals("0")){
            // 此账号已被禁用
            request.setAttribute("user", user);
            request.setAttribute("detail", "当前用户已被禁用!");
            request.getRequestDispatcher("loginfail.page").forward(request, response);
            return;
        }

        // 是否已登录
        if (socketMap.containsKey(user)){
            // 如果已登录
            request.setAttribute("user", user);
            request.setAttribute("detail", "当前用户已经登录了,请不要重复登录!");
            request.getRequestDispatcher("loginfail.page").forward(request, response);
            return;
        }

        //数据库明文密码
        String dbPassDecrypted = SecurityAESUtil.decryptAES(curUserObj.getPassword(), CommonConstants.AES_PASSWORD);
        // 如果密码不对
        if (!pass.equals(dbPassDecrypted)){
            request.setAttribute("user", user);
            request.setAttribute("detail", "密码不对!");
            request.getRequestDispatcher("loginfail.page").forward(request, response);
            return;
        }

        // 校验验证码
        String verifyCode = (String)request.getSession().getAttribute(CommonConstants.VERIFY_CODE);
        if (!verifyCodeInput.equals(verifyCode)){
            request.setAttribute("user", user);
            request.setAttribute("detail", "验证码不对!");
            request.getRequestDispatcher("loginfail.page").forward(request, response);
            return;
        }

        // websocket聊天用的服务器ip和端口
        String webserverip = webSocketConfig.getAddress();
        String webserverPort = webSocketConfig.getPort();
        logger.info("webserverip:{} webserverPort:{}",webserverip, webserverPort);
        configMap.put(CommonConstants.S_WEBSERVERIP, webserverip);
        configMap.put(CommonConstants.S_WEBSERVERPORT, webserverPort);

        // 客户端浏览器类型
        String userAgent = request.getHeader("User-Agent");
        logger.info("userAgent:{}", userAgent);
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

        //session
        HttpSession httpSession = request.getSession();
        String sessionId = httpSession.getId();
        System.out.println("创建SESSION:" + sessionId);
        logger.info("创建SESSION: {}", sessionId);

        //记录cookie
        saveCookie(request, response, curUserObj, sessionId);

        // 设置session非活动失效时间
        httpSession.setMaxInactiveInterval(CommonConstants.SESSION_INACTIVE_TIMEOUT); //session不活动失效时间
        // 用户类型
        String userType = CommonConstants.USER_TYPE_COMMON;
        if (user.equals(CommonConstants.ADMIN_USER)) {
            userType = CommonConstants.USER_TYPE_MANAGER;
        }
        // 往session中存储用户信息
        SessionInfoBean sessionInfoBean = new SessionInfoBean(sessionId, user, curUserObj.getPassword(), webserverip, webserverPort, selfImg, shortAgent, userType);
        request.getSession().setAttribute(CommonConstants.SESSION_INFO, sessionInfoBean);
        model.addAttribute(CommonConstants.SESSION_INFO, sessionInfoBean);

        // 往redis中存储用户信息
        String redisKey = REDIS_KEY_PREFIX + user;
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

    /**
     * 登陆成功首页
     *
     * @param request
     * @param model
     * @return
     */
    @OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.REGISTER,subModule="",describe="登录成功页面")
    @RequestMapping(value = "wsclientIndex.page")
    public String wsclientIndex(HttpServletRequest request, Model model) {
        logger.debug("访问wsclientIndex.page");

        SessionInfoBean sessionInfoBean = (SessionInfoBean)request.getSession().getAttribute(CommonConstants.SESSION_INFO);
        model.addAttribute(CommonConstants.SESSION_INFO, sessionInfoBean);
        return "ws/wsclientIndex";
    }

    /**
     * 登录失败页面
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
     * 注册页面
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
            return CommonConstants.FAIL;
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
        return CommonConstants.SUCCESS;
    }

    /**
     * 查询用户注册的问题相关信息
     * @return
     */
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
     * 生成二维码并显示
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
            return CommonConstants.FAIL;
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

    /**
     * 生成验证码
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.LOGIN,subModule="",describe="获取验证码")
    @GetMapping("/generateVerifyCode.do")
    @ResponseBody
    public Result<String> getCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //算术验证码 数字加减乘除. 建议2位运算就行:captcha.setLen(2);
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 23);
        // 几位数运算，默认是两位
        captcha.setLen(2);

        // 中文验证码
        //ChineseCaptcha captcha = new ChineseCaptcha(120, 40);

        // 英文与数字验证码
        //SpecCaptcha captcha = new SpecCaptcha(120, 40);

        // 英文与数字动态验证码
        //GifCaptcha captcha = new GifCaptcha(120, 40);

        // 中文动态验证码
        //ChineseGifCaptcha captcha = new ChineseGifCaptcha(120, 40);

        // 获取运算的结果
        String verifyCode = captcha.text();
        request.getSession().setAttribute(CommonConstants.VERIFY_CODE, verifyCode);
        String base64String = captcha.toBase64("data:image/png;base64,");
        return Result.ok(base64String);
    }

    @RequestMapping("/i18n")
    public String changeSessionLanauage(HttpServletRequest request, HttpServletResponse response, String lang){

        String language = lang.split("_")[0];
        String country = lang.split("_")[1];
        Locale locale = new Locale(language, country);
        request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);

        // 设置cookie
        Cookie localeCookie = new Cookie(CommonConstants.C_LANG, lang);
        localeCookie.setPath("/");
        localeCookie.setMaxAge(CommonConstants.LOCALE_COOKIE_EXPIRE);
        response.addCookie(localeCookie);

        return "redirect:/";
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

    /**
     * 根据messageId获取对应的国际化
     * @param messageId
     * @param request
     * @return
     */
    private  String getLocaleMessage(String messageId, HttpServletRequest request){
        Locale locale = (Locale)request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
        return messageSource.getMessage(messageId, null, locale);
    }

    /**
     * 保存cookie
     * @param request 请求体
     * @param response 响应体
     * @param curUserObj 当前登陆用户
     * @param sessionId 会话id
     */
    private void saveCookie(HttpServletRequest request, HttpServletResponse response, WsUsersDO curUserObj, String sessionId){
        //记录cookie
        Cookie userCookie = new Cookie(CommonConstants.S_USER, curUserObj.getName());
        Cookie passCookie = new Cookie(CommonConstants.S_PASS, curUserObj.getPassword());
        Cookie webserveripCookie = new Cookie(CommonConstants.S_WEBSERVERIP, webSocketConfig.getAddress());
        Cookie webserverportCookie = new Cookie(CommonConstants.S_WEBSERVERPORT, webSocketConfig.getPort());
        // 客户端的JSESSIONID
        Cookie jsessionIdCookie = new Cookie(CommonConstants.JSESSIONID, sessionId);
        userCookie.setPath("/");
        userCookie.setMaxAge(CommonConstants.COOKIE_TIMEOUT);//用户名30分钟
        passCookie.setPath("/");
        passCookie.setMaxAge(600);//密码10分钟
        webserveripCookie.setPath("/");
        webserveripCookie.setMaxAge(CommonConstants.COOKIE_TIMEOUT);
        webserverportCookie.setPath("/");
        webserverportCookie.setMaxAge(CommonConstants.COOKIE_TIMEOUT);
        jsessionIdCookie.setPath("/");
        jsessionIdCookie.setMaxAge(CommonConstants.COOKIE_TIMEOUT); // 客户端的JSESSIONID保存30分钟
        response.addCookie(userCookie);
        response.addCookie(passCookie);
        response.addCookie(webserveripCookie);
        response.addCookie(webserverportCookie);
        response.addCookie(jsessionIdCookie);

        // 设置语言cookie
        Locale locale = (Locale)request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
        if (null != locale){
            Cookie localeCookie = new Cookie(CommonConstants.C_LANG, locale.toString());
            localeCookie.setPath("/");
            localeCookie.setMaxAge(CommonConstants.LOCALE_COOKIE_EXPIRE);
            response.addCookie(localeCookie);
        }
    }
}