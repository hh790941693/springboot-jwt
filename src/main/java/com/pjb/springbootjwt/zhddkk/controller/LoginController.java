package com.pjb.springbootjwt.zhddkk.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.common.uploader.config.UploadConfig;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.bean.SessionInfoBean;
import com.pjb.springbootjwt.zhddkk.bean.SystemInfoBean;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.constants.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.constants.OperationEnum;
import com.pjb.springbootjwt.zhddkk.domain.*;
import com.pjb.springbootjwt.zhddkk.entity.WsOnlineInfo;
import com.pjb.springbootjwt.zhddkk.service.CacheService;
import com.pjb.springbootjwt.zhddkk.service.SysRoleService;
import com.pjb.springbootjwt.zhddkk.service.SysUserRoleService;
import com.pjb.springbootjwt.zhddkk.service.WsChatlogService;
import com.pjb.springbootjwt.zhddkk.service.WsCommonService;
import com.pjb.springbootjwt.zhddkk.service.WsUserProfileService;
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import com.pjb.springbootjwt.zhddkk.util.*;
import com.pjb.springbootjwt.zhddkk.websocket.WebSocketConfig;
import com.pjb.springbootjwt.zhddkk.websocket.ZhddWebSocket;
import com.wf.captcha.ArithmeticCaptcha;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    //登陆用户的redis缓存前缀
    private static final String REDIS_KEY_PREFIX = "ws_";

    private static final SimpleDateFormat SDF_STANDARD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    // 登陆成功后的页面前缀
    // 访问样例:http://127.0.0.1:8100?v=1
    // 目前可用值:
    //          v=1 -> wsclientIndex_v1.html 头、底、导航栏、iframe分离
    //          v=2 -> wsclientIndex_v2.html 头、底、导航栏、iframe都在一个页面
    //          v=3 -> wsclientIndex_v3.html 比v2更新一点的版本
    //          v=4 -> wsclientIndex_v4.html 采用jquery easyUI重新设计(默认)
    private static String HOME_PAGE_NAME = "wsclientIndex_v4";

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

    @Autowired
    private CacheService cacheService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 首页登录.
     *
     * @param model 模型
     * @param request 请求
     * @return 登陆首页
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.LOGIN, subModule = "", describe = "登陆首页页面")
    @RequestMapping({"", "/", "/index"})
    public String home(Model model, HttpServletRequest request, String errorMsg) {
        String version = request.getParameter("v");
        if (StringUtils.isNotBlank(version)) {
            switch(version){
                case "1":
                case "2":
                case "3":
                    HOME_PAGE_NAME = "wsclientIndex_v" +version;
                    break;
                default:
                    HOME_PAGE_NAME = "wsclientIndex_v4";
            }
        }

        // 如果用户已登陆过，则直接跳转登陆成功首页
        SessionInfoBean sessionInfoBean = SessionUtil.getSessionAttribute(request, CommonConstants.SESSION_INFO);
        if (null != sessionInfoBean) {
            return "ws/" + HOME_PAGE_NAME;
        }

        // 检查cookie
        Cookie[] cookies = request.getCookies();
        model.addAttribute(CommonConstants.S_USER, "");
        model.addAttribute(CommonConstants.S_PASS, "");
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CommonConstants.S_USER) && cookie.getMaxAge() != 0) {
                    model.addAttribute(CommonConstants.S_USER, cookie.getValue());
                } else if (cookie.getName().equals(CommonConstants.S_PASS) && cookie.getMaxAge() != 0) {
                    //对密码进行解密
                    String passDecrypt = SecurityAESUtil.decryptAES(cookie.getValue(), CommonConstants.AES_PASSWORD);
                    model.addAttribute(CommonConstants.S_PASS, passDecrypt);
                }
            }
        }

        if (StringUtils.isNotBlank(errorMsg)) {
            request.setAttribute("errorMsg", errorMsg);
        }
        return "ws/login";
    }

    /**
     * 登录按钮事件.
     * @param userName 用户名
     * @param password 密码
     * @param verifyCodeInput 验证码
     */
    @OperationLogAnnotation(type = OperationEnum.UPDATE, module = ModuleEnum.LOGIN, subModule = "", describe = "登录")
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    public void login(@RequestParam("user")String userName, @RequestParam("pass")String password,
                         @RequestParam("verifyCode")String verifyCodeInput,
                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取用户信息
        WsUsersDO curUserObj = wsUsersService.queryUserByName(userName);

        // 如果用户信息不存在,提示用户去注册
        if (null == curUserObj) {
            // 用户未注册
            request.setAttribute("user", userName);
            request.setAttribute("errorMsg", getLocaleMessage("login.err.user.not.exist", request));
            request.getRequestDispatcher("index").forward(request, response);
            //request.getRequestDispatcher("loginfail.page").forward(request, response);
            return;
        }

        // 是否被禁用   0:已禁用
        String isEnable = curUserObj.getEnable();
        if (isEnable.equals("0")) {
            // 此账号已被禁用
            request.setAttribute("user", userName);
            request.setAttribute("errorMsg", getLocaleMessage("login.err.user.disable", request));
            request.getRequestDispatcher("index").forward(request, response);
            return;
        }

        // 是否已登录
//        if (curUserObj.getState().equals("1")) {
//            // 如果已登录
//            request.setAttribute("user", user);
//            request.setAttribute("errorMsg", "当前用户已经登录了,请不要重复登录!");
//            request.getRequestDispatcher("index").forward(request, response);
//            return;
//        }

        //数据库明文密码
        String dbPassDecrypted = SecurityAESUtil.decryptAES(curUserObj.getPassword(), CommonConstants.AES_PASSWORD);
        // 如果密码不对
        if (!password.equals(dbPassDecrypted)) {
            request.setAttribute("user", userName);
            request.setAttribute("errorMsg", getLocaleMessage("login.err.password.wrong", request));
            request.getRequestDispatcher("index").forward(request, response);
            return;
        }

        // 校验验证码
        Cookie verifyCodeCookie = getCookieObj(request, CommonConstants.VERIFY_CODE);
        String verifyCode = null != verifyCodeCookie ? verifyCodeCookie.getValue() : "";
        if (StringUtils.isBlank(verifyCode)) {
            request.setAttribute("user", userName);
            request.setAttribute("errorMsg", getLocaleMessage("login.err.verifycode.invalid", request));
            request.getRequestDispatcher("index").forward(request, response);
            return;
        }
        if (!verifyCodeInput.equals(verifyCode)) {
            request.setAttribute("user", userName);
            request.setAttribute("errorMsg", getLocaleMessage("login.err.verifycode.wrong", request));
            request.getRequestDispatcher("index").forward(request, response);
            return;
        }

        // 客户端浏览器类型
        String userAgent = request.getHeader("User-Agent");
        logger.info("userAgent:{}", userAgent);
        String shortAgent = "unknown user agent";
        try {
            shortAgent = userAgent.split("\\(")[1].split("\\)")[0].replaceAll("\\(", "").replaceAll("\\)", "");
        } catch (Exception e) {
            logger.warn("获取客户端信息失败!" + e.getMessage());
        }

        // 获取session
        HttpSession session = request.getSession(true);
        // 设置session非活动失效时间(30分钟)
        session.setMaxInactiveInterval(CommonConstants.SESSION_INACTIVE_TIMEOUT);

        //记录cookie
        saveCookie(request, response, curUserObj);

        // 往session中存储用户信息
        SessionInfoBean sessionInfoBean = new SessionInfoBean(session.getId(),
                String.valueOf(curUserObj.getId()), userName, curUserObj.getPassword(), webSocketConfig.getAddress(),
                webSocketConfig.getPort(), curUserObj.getHeadImage(), shortAgent, String.valueOf(curUserObj.getRoleId()), curUserObj.getRoleName(), session.getMaxInactiveInterval());
        String jsonStr = JsonUtil.javaobject2Jsonstr(sessionInfoBean);
        JSONObject jsonObj = JsonUtil.javaobject2Jsonobject(sessionInfoBean);
        sessionInfoBean.setJsonStr(jsonStr);
        sessionInfoBean.setJsonObject(jsonObj);
        // 页面通过th:value="${session.sessionInfo.jsonStr}"来获取session信息
        session.setAttribute(CommonConstants.SESSION_INFO, sessionInfoBean);

        // 往redis中存储用户信息
        String redisKey = REDIS_KEY_PREFIX + userName;
        try {
            String redisValue = JsonUtil.javaobject2Jsonstr(curUserObj);
            logger.debug("设置redis缓存,key:" + redisKey + "  value:" + redisValue);
            //redisUtil.set(redisKey, redisValue);
        } catch (Exception e) {
            logger.debug("设置redis缓存失败,key:" + redisKey + " error:" + e.getMessage());
        }

        // 缓存常用表数据
        cacheService.cacheData();

        // 更新用户登录状态和时间
        curUserObj.setState("1");
        curUserObj.setLastLoginTime(SDF_STANDARD.format(new Date()));
        wsUsersService.updateById(curUserObj);

        response.sendRedirect("home.page");
    }

    /**
     * 登陆成功首页.
     *
     * @return 首页
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.REGISTER, subModule = "", describe = "登录成功页面")
    @RequestMapping(value = "home.page")
    public String wsclientIndex() {
        logger.debug("访问home.page");
        return "ws/" + HOME_PAGE_NAME;
    }

    /**
     * 登录失败页面.
     * @return 登陆失败页面
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.REGISTER, subModule = "", describe = "登录失败页面")
    @RequestMapping(value = "loginfail.page")
    public String loginfail() {
        logger.debug("访问loginfail.page");
        return "ws/loginfail";
    }

    /**
     * 注册页面.
     *
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.REGISTER, subModule = "", describe = "注册首页")
    @RequestMapping(value = "register.page")
    public String toRegister(Model model) {
        logger.debug("访问register.page");
        List<WsCommonDO> list = wsCommonService.selectList(new EntityWrapper<WsCommonDO>().eq("type", "zcwt"));
        model.addAttribute("questionList", list);
        return "ws/register";
    }

    /**
     * 注册按钮事件.
     *
     * @param model 模型
     * @return 注册
     */
    @OperationLogAnnotation(type = OperationEnum.INSERT, module = ModuleEnum.REGISTER, subModule = "", describe = "注册账号")
    @RequestMapping(value = "wsregister.do", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public Result<String> wsregister(Model model, @RequestParam("user")String user,
                             @RequestParam("pass")String pass,
                             @RequestParam("headImg")String headImg,
                             @RequestParam("confirmPass")String confirmPass,
                             @RequestParam("select1")String question1, @RequestParam("answer1")String answer1,
                             @RequestParam("select2")String question2, @RequestParam("answer2")String answer2,
                             @RequestParam("select3")String question3, @RequestParam("answer3")String answer3) {
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
        if (null != wsUsersDO) {
            // 如果已经注册
            model.addAttribute("user", user);
            model.addAttribute("detail", "当前用户已注册,请直接登录!");
            return Result.fail();
        }

        if (!pass.equals(confirmPass)) {
            // 如果密码不一致
            model.addAttribute("user", user);
            model.addAttribute("detail", "两次密码不一致!");
            return Result.ok();
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
        WsChatlogDO loginLog = new WsChatlogDO(sdfx.format(new Date()), "", user, "", "注册成功", "");
        wsChatlogService.insert(loginLog);

        //插入用户profile信息
        int userProfileCnt = wsUserProfileService.selectCount(new EntityWrapper<WsUserProfileDO>()
                .eq("user_id", insrtWu.getId()));
        if (userProfileCnt == 0) {
            WsUserProfileDO wsUserProfileDO = new WsUserProfileDO();
            wsUserProfileDO.setUserId(insrtWu.getId());
            wsUserProfileDO.setUserName(user);
            wsUserProfileDO.setCreateTime(new Date());
            if (StringUtils.isNotBlank(headImg)) {
                wsUserProfileDO.setImg(headImg);
            }
            wsUserProfileService.insert(wsUserProfileDO);
        }

        // 分配[普通用户]角色
        SysRoleDO sysRoleDO = sysRoleService.selectOne(new EntityWrapper<SysRoleDO>().eq("name", "普通用户"));
        if (null != sysRoleDO) {
            SysUserRoleDO sysUserRoleDO = new SysUserRoleDO();
            sysUserRoleDO.setUserId(insrtWu.getId());
            sysUserRoleDO.setUserName(insrtWu.getName());
            sysUserRoleDO.setRoleId(sysRoleDO.getId());
            sysUserRoleDO.setRoleName(sysRoleDO.getName());
            sysUserRoleService.insert(sysUserRoleDO);
        }

        model.addAttribute("user", user);
        model.addAttribute("pass", pass);
        return Result.ok();
    }

    /**
     * 查询用户注册的问题相关信息.
     * @return 字典
     */
    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.REGISTER, subModule = "", describe = "查询问题列表")
    @RequestMapping(value = "queryAllCommonData.do")
    @ResponseBody
    public Map<String, List<WsCommonDO>> queryAllCommonData() {
        logger.debug("访问queryAllCommonData.do");
        return buildCommonData();
    }

    /**
     * 忘记密码页面.
     * @return 忘记密码页面
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.FORGET_PASSWORD, subModule = "", describe = "忘记密码首页")
    @RequestMapping(value = "forgetPassword.page")
    public String forgetPassword(Model model, @RequestParam("user")String user) {
        logger.debug("访问forgetPassword.page");
        model.addAttribute("user", user);
        return "ws/forgetPassword";
    }

    /**
     * 更新密码事件.
     *
     */
    @OperationLogAnnotation(type = OperationEnum.UPDATE, module = ModuleEnum.FORGET_PASSWORD, subModule = "", describe = "更新密码")
    @RequestMapping(value = "updatePassword.do", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> updatePassword(@RequestParam("user")String user,
                                         @RequestParam("pass")String newPass,
                                         @RequestParam("confirmPass")String confirmPass,
                                         @RequestParam("select1")String question1, @RequestParam("answer1")String answer1,
                                         @RequestParam("select2")String question2, @RequestParam("answer2")String answer2,
                                         @RequestParam("select3")String question3, @RequestParam("answer3")String answer3) {
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
        if (null == wsUsersDO) {
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
     * 查询系统信息.
     * @return 系统信息
     */
    @ResponseBody
    @GetMapping("/querySystemInfo")
    public Result<SystemInfoBean> querySystemInfo() {
        SystemInfoBean systemInfoBean = new SystemInfoBean();
        systemInfoBean.setOsName(System.getProperty("os.name"));
        systemInfoBean.setJavaHome(System.getProperty("java.home"));
        systemInfoBean.setJavaVersion(System.getProperty("java.version"));
        systemInfoBean.setDbVersion("No Check");

        Properties props = System.getProperties();
        String osName = props.getProperty("os.name");
        boolean nginxPs = false;
        if (osName.contains("windows") || osName.contains("Windows")) {
            nginxPs = OsUtil.findWindowProcess("nginx.exe");
        } else {
            String result = ExcuteLinuxCmdUtil.executeLinuxCmd("ps -ef | grep nginx | grep -v grep");
            if (StringUtils.isNotBlank(result) && result.contains("nginx")) {
                nginxPs = true;
            }
        }
        systemInfoBean.setNginxFlag(nginxPs);

        String storePath = uploadConfig.getStorePath();
        File sf = new File(storePath);
        if (sf.isDirectory()) {
            systemInfoBean.setShareDir(storePath + ":共享目录正常");
        } else {
            systemInfoBean.setShareDir(storePath + ":共享目录不存在");
        }

        return Result.ok(systemInfoBean);
    }

    /**
     * 生成二维码并显示.
     *
     * @return 二维码base64字符串
     */
    @OperationLogAnnotation(type = OperationEnum.INSERT, module = ModuleEnum.OTHER, subModule = "", describe = "显示二维码")
    @RequestMapping(value = "showQRCode.do", method = RequestMethod.POST)
    @ResponseBody
    public String showQrCode(HttpServletRequest request) {
        String scheme = request.getScheme();

        String savePath = uploadConfig.getStorePath();
        String qrCodeFilename = "qrcode.png";
        String savePathAbs = savePath + File.separator + qrCodeFilename;
        System.out.println("二维码文件路径:" + savePathAbs);

        boolean isNeedGenerateCode = false;
        File qrCodeFile = new File(savePathAbs);
        if (!qrCodeFile.exists()) {
            isNeedGenerateCode = true;
        } else {
            long lastModified = qrCodeFile.lastModified();
            Date lastModifiedDate = new Date(lastModified);
            long diff = new Date().getTime() - lastModifiedDate.getTime();
            long minutesDiff = diff / (1000 * 60);
            System.out.println("距离上次生成时间有:" + minutesDiff + "分钟");
            if (minutesDiff >= 60) {
                //如果超过一个小时则可以重新生成二维码
                isNeedGenerateCode = true;
            }
        }

        if (isNeedGenerateCode) {
            String text = scheme + "://" + webSocketConfig.getAddress() + ":" + webSocketConfig.getPort();
            System.out.println("登录地址:" + text);
            try {
                String qrCodeFilePath = QRCodeUtil.generateQRCode(text, 200, 200, "png", savePathAbs);
                System.out.println("二维码文件新路径:" + qrCodeFilePath);
            } catch (Exception e) {
                System.out.println("生成二维码失败!" + e.getMessage());
                e.printStackTrace();
            }
        }
        return uploadConfig.getViewUrl() + "/" + qrCodeFilename;
    }

    /**
     * 获取用户的问题答案.
     * @param user 用户id
     * @return 用户信息
     */
    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.OTHER, subModule = "", describe = "获取用户密保信息")
    @RequestMapping(value = "getUserQuestion.json")
    @ResponseBody
    public Result<WsUsersDO> getUserQuestion(@RequestParam("user")String user) {
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
        if (null != wsUsersDO) {
            return Result.ok(wsUsersDO);
        } else {
            return Result.fail();
        }
    }

    /**
     * 检查某用户是否已经注册过.
     * 0:存在  1:不存在
     */
    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.REGISTER, subModule = "", describe = "检查用户是否已经注册")
    @RequestMapping(value = "checkUserRegisterStatus.json", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> checkUserRegisterStatus(@RequestParam("user") String user) {
        int userCount = wsUsersService.selectCount(new EntityWrapper<WsUsersDO>().eq("name", user));
        if (userCount > 0) {
            return Result.fail();
        }
        return Result.ok();
    }

    /**
     * 生成验证码.
     *
     * @param request 请求
     * @return 生成的验证码图片base64
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.LOGIN, subModule = "", describe = "获取验证码")
    @GetMapping("/generateVerifyCode.do")
    @ResponseBody
    public Result<String> getCode(HttpServletRequest request, HttpServletResponse response) {
        //算术验证码 数字加减乘除. 建议2位运算就行:captcha.setLen(2);
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 34);
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
        // 使用cookie存储验证码答案
        Cookie verifyCodeCookie = new Cookie(CommonConstants.VERIFY_CODE, verifyCode);
        verifyCodeCookie.setPath("/");
        verifyCodeCookie.setMaxAge(CommonConstants.COOKIE_TIMEOUT);
        response.addCookie(verifyCodeCookie);

        //SessionUtil.setSessionAttribute(request, CommonConstants.VERIFY_CODE, verifyCode);
        String base64String = captcha.toBase64("data:image/png;base64,");
        return Result.ok(base64String);
    }

    /**
     * 切换语言.
     * @param request 请求
     * @param response 响应
     * @param lang 语言
     * @return 重定向页面
     */
    @RequestMapping("/i18n")
    public String changeSessionLanauage(HttpServletRequest request, HttpServletResponse response, String lang) {
        String language = lang.split("_")[0];
        String country = lang.split("_")[1];
        Locale locale = new Locale(language, country);

        LocaleContextHolder.setLocale(locale);
        return "redirect:/";
    }

    /**
     * 获取聊天室用户信息.
     *
     * @return 聊天室用户信息
     */
    @RequestMapping(value = "getChatRoomInfo.json")
    @ResponseBody
    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.CHAT, subModule = "", describe = "获取房间人员列表")
    public Result<WsOnlineInfo> getChatRoomInfo(@RequestParam(value = "roomName") String roomName) {
        WsOnlineInfo woi = new WsOnlineInfo();
        woi.setCommonMap(buildCommonData());

        // 房间用户列表
        List<String> roomUserNameList = ZhddWebSocket.getRoomClientsUserList(roomName);
        List<WsUserProfileDO> roomUserProfileList = wsUserProfileService.selectList(new EntityWrapper<WsUserProfileDO>().in("user_name", roomUserNameList));
        woi.setRoomUserProfileList(roomUserProfileList);
        woi.setRoomUserCount(roomUserNameList.size());

        return Result.ok(woi);
    }

    /**
     * 校验用户密码正确性.
     * @param userName 用户名
     * @param password 密码(明文)
     *
     * @return 聊天室用户信息
     */
    @PostMapping(value = "verifyUser.do")
    @ResponseBody
    public Result<String> verifyUser(@RequestParam(value = "userName") String userName, @RequestParam(value = "password") String password) {
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", userName));
        if (null == wsUsersDO) {
            return Result.fail("用户不存在");
        }

        //数据库明文密码
        String dbPassDecrypted = SecurityAESUtil.decryptAES(wsUsersDO.getPassword(), CommonConstants.AES_PASSWORD);
        // 如果密码不对
        if (!password.equals(dbPassDecrypted)) {
            return Result.fail("密码不正确");
        }

        return Result.ok(wsUsersDO.getPassword());
    }

    /**
     * 构造常用语对象.
     * @return map
     */
    private Map<String, List<WsCommonDO>> buildCommonData() {
        Map<String, List<WsCommonDO>> commonMap = new HashMap<>();
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
            } else {
                List<WsCommonDO> tmpDicList = new ArrayList<>();
                tmpDicList.add(common);
                commonMap.put(type, tmpDicList);
            }
        }
        return commonMap;
    }

    /**
     * 根据messageId获取对应的国际化.
     * @param messageId 信息ID
     * @param request 请求
     * @return 国际化后的信息
     */
    private  String getLocaleMessage(String messageId, HttpServletRequest request) {
        Locale locale = LocaleContextHolder.getLocale();
        if (null == locale) {
            locale = Locale.SIMPLIFIED_CHINESE;
        }
        return messageSource.getMessage(messageId, null, locale);
    }

    /**
     * 保存cookie.
     * @param request 请求体
     * @param response 响应体
     * @param curUserObj 当前登陆用户
     */
    private void saveCookie(HttpServletRequest request, HttpServletResponse response, WsUsersDO curUserObj) {
        //记录cookie
        Cookie userIdCookie = new Cookie(CommonConstants.S_USER_ID, String.valueOf(curUserObj.getId()));
        userIdCookie.setPath("/");
        userIdCookie.setMaxAge(CommonConstants.COOKIE_TIMEOUT); //用户Id30分钟

        Cookie userCookie = new Cookie(CommonConstants.S_USER, curUserObj.getName());
        userCookie.setPath("/");
        userCookie.setMaxAge(CommonConstants.COOKIE_TIMEOUT); //用户名30分钟

        Cookie passCookie = new Cookie(CommonConstants.S_PASS, curUserObj.getPassword());
        passCookie.setPath("/");
        passCookie.setMaxAge(600); //密码10分钟

        Cookie webserveripCookie = new Cookie(CommonConstants.S_WEBSERVERIP, webSocketConfig.getAddress());
        webserveripCookie.setPath("/");
        webserveripCookie.setMaxAge(CommonConstants.COOKIE_TIMEOUT);

        Cookie webserverportCookie = new Cookie(CommonConstants.S_WEBSERVERPORT, webSocketConfig.getPort());
        webserverportCookie.setPath("/");
        webserverportCookie.setMaxAge(CommonConstants.COOKIE_TIMEOUT);

        response.addCookie(userIdCookie);
        response.addCookie(userCookie);
        response.addCookie(passCookie);
        response.addCookie(webserveripCookie);
        response.addCookie(webserverportCookie);
    }

    /**
     * 获取cookie对象.
     * @param request 请求对象
     * @param name    cookie对应的key
     * @return cookie对象
     */
    private static Cookie getCookieObj(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name) && cookie.getMaxAge() != 0) {
                    return cookie;
                }
            }
        }
        return null;
    }
}