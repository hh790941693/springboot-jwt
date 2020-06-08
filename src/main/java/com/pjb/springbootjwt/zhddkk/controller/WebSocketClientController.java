package com.pjb.springbootjwt.zhddkk.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.common.redis.RedisUtil;
import com.pjb.springbootjwt.common.uploader.config.UploadConfig;
import com.pjb.springbootjwt.util.HttpClientUtils;
import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.bean.SystemInfoBean;
import com.pjb.springbootjwt.zhddkk.bean.WangyiNewsBean;
import com.pjb.springbootjwt.zhddkk.domain.*;

import com.pjb.springbootjwt.zhddkk.entity.PageResponseEntity;
import com.pjb.springbootjwt.zhddkk.entity.WsOnlineInfo;
import com.pjb.springbootjwt.zhddkk.service.*;
import com.pjb.springbootjwt.zhddkk.util.*;
import com.pjb.springbootjwt.zhddkk.websocket.WebSocketConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.enumx.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.enumx.OperationEnum;
import com.pjb.springbootjwt.zhddkk.interceptor.WsInterceptor;
import com.pjb.springbootjwt.zhddkk.websocket.ZhddWebSocket;

/**
 * webSocket控制器
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("ws")
public class WebSocketClientController extends AdminBaseController
{
	private static final int COOKIE_TIMEOUT = 1800; //cookie过期时间 30分钟

	private static final String REDIS_KEY_PREFIX = "ws_"; //登陆用户的redis缓存前缀
	
	private static final Logger logger = LoggerFactory.getLogger(WebSocketClientController.class);
	
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");
	
	private static Map<String,String> configMap = WsInterceptor.getConfigMap();

	@Autowired
	private WebSocketConfig webSocketConfig;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WsCircleService wsCircleService;

    @Autowired
    private WsCircleCommentService wsCircleCommentService;

    @Autowired
    private WsUsersService wsUsersService;

    @Autowired
    private WsUserProfileService wsUserProfileService;

    @Autowired
    private WsFriendsService wsFriendsService;

    @Autowired
    private WsFriendsApplyService wsFriendsApplyService;

    @Autowired
    private WsChatlogService wsChatlogService;

    @Autowired
    private WsCommonService wsCommonService;

    @Autowired
    private UploadConfig uploadConfig;
	
	/**
	 *客户端登录首页
	 * 
	 * @param model
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.LOGIN,subModule="",describe="登录首页")
	@RequestMapping(value = {"","/","login.page"}, method = RequestMethod.GET)
	public String login(Model model,@RequestParam(value="user",required=false)String user,HttpServletRequest request)
	{
		logger.debug("访问login.page,user:"+user);
		if (StringUtils.isNotEmpty(user)) {
			WsUsersDO curUser = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
			if (null != curUser) {
				String redisKey = REDIS_KEY_PREFIX+user;
				try {
					curUser.setState("0");//离线
					String redisValue = JsonUtil.javaobject2Jsonstr(curUser);
					logger.debug("设置redis缓存,key:{}"+redisKey+"  value:"+redisValue);
					//redisUtil.set(redisKey, redisValue);
				}catch (Exception e) {
					logger.debug("设置redis缓存失败,key:"+redisKey+" error:"+e.getMessage());
				}
			}
		}
		
		if (StringUtils.isNotBlank(user)) {
			model.addAttribute(CommonConstants.S_USER, user);
		}else {
			Cookie[] cookies = request.getCookies();
			if (null != cookies) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals(CommonConstants.S_USER) && cookie.getMaxAge() != 0) {
						model.addAttribute(CommonConstants.S_USER, cookie.getValue());
					}
					
					if (cookie.getName().equals(CommonConstants.S_PASS) && cookie.getMaxAge() != 0) {
						//对密码进行解密
						String passDecrypt = SecurityAESUtil.decryptAES(cookie.getValue(), CommonConstants.AES_PASSWORD);
						model.addAttribute(CommonConstants.S_PASS, passDecrypt);
					}			
				}
			}
		}

		return "ws/login";
	}


	/**
	 * 登录
	 * 
	 * @param model
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.UPDATE,module=ModuleEnum.LOGIN,subModule="",describe="登录")
	@RequestMapping(value = "wslogin.do", method = RequestMethod.POST)
	public String wsclient(Model model,@RequestParam("user")String user,@RequestParam("pass")String pass,
							HttpServletRequest request, HttpServletResponse response) {
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
			return "ws/loginfail";
		}

		if (isEnable.equals("0")){
			// 此账号已被禁用
			model.addAttribute("user", user);
			model.addAttribute("detail","当前用户已被禁用!");
			return "ws/loginfail";
		}
		
		if (isLogin){
			// 如果已登录
			model.addAttribute("user", user);
			model.addAttribute("detail","当前用户已经登录了,请不要重复登录!");
			return "ws/loginfail";
		}

		// 如果密码不对
		if (!pass.equals(dbPassDecrypted)){
			model.addAttribute("user", user);
			model.addAttribute("detail","密码不对!");
			return "ws/loginfail";
		}

		String webserverip = webSocketConfig.getAddress();
		String webserverPort = webSocketConfig.getPort();
		logger.info("------------------配置信息------------------:"+configMap);
		logger.info("webserverip:"+webserverip);
		logger.info("webserverPort:"+webserverPort);

		String userAgent = request.getHeader("User-Agent");
		logger.info("user:"+user+"  userAgent:"+userAgent);
		String shortAgent = "unknown device";
		try {
			shortAgent = userAgent.split("\\(")[1].split("\\)")[0].replaceAll("\\(", "").replaceAll("\\)", "");
		}catch (Exception e) {
			logger.warn("获取客户端信息失败!"+e.getMessage());
		}
		
		model.addAttribute(CommonConstants.S_USER, user);
		model.addAttribute(CommonConstants.S_PASS, dbPass);
		model.addAttribute(CommonConstants.S_WEBSERVERIP, webserverip);
		model.addAttribute(CommonConstants.S_WEBSERVERPORT, webserverPort);
		model.addAttribute(CommonConstants.S_USER_AGENT, shortAgent);
		
		//个人头像
		WsUserProfileDO wup = wsUserProfileService.selectOne(new EntityWrapper<WsUserProfileDO>().eq("user_name", user));
		String selfImg = "";
		if (null != wup) {
			selfImg = wup.getImg();
			model.addAttribute(CommonConstants.S_IMG, selfImg);
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
		request.getSession().setMaxInactiveInterval(CommonConstants.SESSION_TIMEOUT); //session不活动失效时间
		request.getSession().setAttribute(CommonConstants.S_USER+"_"+user, user);
		request.getSession().setAttribute(CommonConstants.S_USER, user);
		request.getSession().setAttribute(CommonConstants.S_PASS+"_"+user, dbPass);
		request.getSession().setAttribute(CommonConstants.S_WEBSERVERIP, webserverip);
		request.getSession().setAttribute(CommonConstants.S_WEBSERVERPORT, webserverPort);
		request.getSession().setAttribute(CommonConstants.S_IMG+"_"+user, selfImg);
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

		return "ws/wsclientIndex";
	}

	/**
	 * 注册
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
	
	/**
	 * 退出
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.UPDATE,module=ModuleEnum.LOGOUT,subModule="",describe="退出")
	@RequestMapping(value = "logout.do",method=RequestMethod.POST)
	@ResponseBody
	public String logout(@RequestParam("user")String user,HttpServletRequest request) {
		String sessionUser = (String)request.getSession().getAttribute(CommonConstants.S_USER+"_"+user);
		System.out.println("清理session缓存:"+sessionUser);

		if (StringUtils.isBlank(sessionUser)){
			return "success";
		}

		if (user.equals(sessionUser)) {
			removeSessionAttributes(user, request);
			//removeCookies(request,response);
			
			String redisKey = REDIS_KEY_PREFIX+user;
			String redisValue = SDF.format(new Date()).concat("_").concat("0");
			try {
				logger.debug("设置redis缓存,key:"+redisKey+"  value:"+redisValue);
                //redisUtil.set(redisKey, redisValue);
			}catch (Exception e) {
				logger.debug("设置redis缓存失败,key:"+redisKey+" error:"+e.getMessage());
			}
			return "success";
		}
		return "failed";
	}
	
	
	/**
	 * 在登录页面点击注册时调用此方法
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.REGISTER,subModule="",describe="注册首页")
	@RequestMapping(value = "register.page")
	public String toRegister(Model model) {
		logger.debug("访问register.page");
		return "ws/register";
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
	 * 忘记密码
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
	    *聊天页面
	 * 
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.CHAT,subModule="",describe="聊天首页")
	@RequestMapping(value = "wsclientChat.page")
	public String wsclientChat() {
		logger.debug("访问wsclientChat.page");
		return "ws/wsclientChat";
	}
	
	/**
	 * 添加好友页面
	 *
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.FRIENDS,subModule="",describe="添加好友首页")
	@RequestMapping(value = "addFriends.page")
	public String addFriends() {
		logger.debug("访问addFriends.page");
		return "ws/addFriends";
	}

	/**
	 * 头部页面
	 *
	 * @return
	 */
	@RequestMapping(value = "header.page")
	public String headerPage() {
		logger.debug("访问header.page");
		return "ws/header";
	}

	/**
	 * 底部页面
	 *
	 * @return
	 */
	@RequestMapping(value = "footer.page")
	public String footerPage() {
		logger.debug("访问footer.page");
		return "ws/footer";
	}

	/**
	 * 客户端左侧导航栏页面
	 *
	 * @return
	 */
	@RequestMapping(value = "clientLeftNavi.page")
	public String clientLeftNavi() {
		logger.debug("访问clientLeftNavi.page");
		return "ws/clientLeftNavi";
	}

	/**
	 * 服务端左侧导航栏页面
	 *
	 * @return
	 */
	@RequestMapping(value = "serverLeftNavi.page")
	public String serverLeftNavi() {
		logger.debug("访问serverLeftNavi.page");
		return "ws/serverLeftNavi";
	}
	
	/**
	 * 好友列表
	 *
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.FRIENDS,subModule="",describe="好友列表首页")
	@RequestMapping(value = "friendsList.page")
	public String friendsList() {
		logger.debug("访问friendsList.page");
		return "ws/friendsList";
	}	
	
	/**
	 * 我的申请好友页面
	 *
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.FRIENDS,subModule="",describe="我的申请首页")
	@RequestMapping(value = "myApply.page")
	public String myApply() {
		logger.debug("访问myApply.page");
		return "ws/myApply";
	}
	
	/**
	 * 好友申请列表
	 *
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.FRIENDS,subModule="",describe="我好友申请首页")
	@RequestMapping(value = "friendsApply.page")
	public String friendsApply() {
		logger.debug("访问friendsApply.page");
		return "ws/friendsApply";
	}

	/**
	 * 朋友圈首页
	 *
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.CIRCLE,subModule="",describe="朋友圈首页")
	@RequestMapping(value = "wsclientCircle.page")
	public String clientCircle() {
		logger.debug("访问wsclientCircle.page");
		return "ws/wsclientCircleVue";
	}
	
	/**
	 * 添加朋友圈
	 * 
	 * @param model
	 * @param user
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.CIRCLE,subModule="",describe="添加朋友圈首页")
	@RequestMapping(value = "wsclientAddCircle.page")
	public String wsclientAddCircle(Model model,@RequestParam("user")String user) {
		logger.debug("访问wsclientAddCircle.page");
		model.addAttribute("user", user);
		return "ws/wsclientAddCircle";
	}

	/**
	 * 设置个人信息
	 *
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.SETTING,subModule="",describe="用户信息设置首页")
	@RequestMapping(value = "setPersonalInfo.page")
	public String setPersonalInfo(Model model,@RequestParam("user")String user) {
		logger.debug("访问setPersonalInfo.page");
		model.addAttribute("user", user);
		return "ws/setPersonalInfo";
	}
	
	/**
	 * 显示用户信息
	 * 
	 * @param model
	 * @param user
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.SETTING,subModule="",describe="显示个用户信息首页")
	@RequestMapping(value = "showPersonalInfo.page")
	public String showPersonalInfo(Model model,@RequestParam("user")String user) {
		logger.debug("访问showPersonalInfo.page");
		model.addAttribute("user", user);
		return "ws/showPersonalInfo";
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
	 * 更新密码
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
	 * 获取在线人数信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "getOnlineInfo.json")
	@ResponseBody
	public Result<WsOnlineInfo> getOnlineInfo(@RequestParam(value="user",required=true)String user) {
		if (StringUtils.isBlank(user)){
			return Result.fail(new WsOnlineInfo());
		}
		Map<String, ZhddWebSocket> socketMap = ZhddWebSocket.getClients();
		//所有用户
		List<WsUsersDO> allUserList = wsUsersService.selectList(new EntityWrapper<WsUsersDO>().ne("name", "admin"));
		//在线用户
		List<WsUsersDO> onlineUserList = new ArrayList<WsUsersDO>();
		//离线用户
		List<WsUsersDO> offlineUserList = new ArrayList<WsUsersDO>();
		//当前用户信息
		WsUsersDO currentOnlineUserInfo = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
		WsUserProfileDO wsUserProfileDO = wsUserProfileService.selectOne(new EntityWrapper<WsUserProfileDO>().eq("user_id", currentOnlineUserInfo.getId()));
		if (null != wsUserProfileDO) {
            currentOnlineUserInfo.setHeadImage(wsUserProfileDO.getImg());
        }
		//我的好友列表
		List<WsUsersDO> friendsUserList = wsUsersService.queryMyFriendList(currentOnlineUserInfo.getId());

		for (WsUsersDO wu : allUserList) {
			if (socketMap.containsKey(wu.getName())) {
				wu.setState("1");
				onlineUserList.add(wu);
			}else {
				wu.setState("0");
				offlineUserList.add(wu);
			}
		}
		
		WsOnlineInfo woi = new WsOnlineInfo();
		woi.setOfflineCount(offlineUserList.size());
		woi.setOnlineCount(onlineUserList.size());
		Collections.reverse(allUserList);
		woi.setUserInfoList(allUserList);
		woi.setOnlineUserList(onlineUserList);
		woi.setOfflineUserList(offlineUserList);
		woi.setFriendsList(friendsUserList);
		woi.setCommonMap(buildCommonData());
		woi.setCurrentOnlineUserInfo(currentOnlineUserInfo);

		return Result.ok(woi);
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
	 * 分页显示所有用户信息
	 * 
	 * true:存在  false:不存在
	 */
	@OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.FRIENDS,subModule="",describe="显示所有用户信息")
	@RequestMapping(value="showAllUser.json",method=RequestMethod.POST,produces="application/json")
	@ResponseBody
	@Deprecated
	public Object getOnlineUsersByPage(@RequestBody WsUsersDO params) {
        int totalCount = wsUsersService.selectCount(new EntityWrapper<WsUsersDO>().ne("name", "admin"));
		int numPerPage = 15;
		int curPage = 1;
		String curUser = params.getName();
		int totalPage = 1;
		if (totalCount % numPerPage != 0){
			totalPage = totalCount/numPerPage+1;
		}else{
			totalPage = totalCount/numPerPage;
		}
		if (totalPage == 0) {
			totalPage = 1;
		}

        Page<WsUsersDO> queryPage = new Page<WsUsersDO>(curPage, numPerPage);
		List<WsUsersDO> userlist = new ArrayList<>();
		Page<WsUsersDO> userPage = wsUsersService.selectPage(queryPage,
                    new EntityWrapper<WsUsersDO>().ne("name", "admin").orderBy("last_login_time", false));

		if (null != userPage){
            userlist = userPage.getRecords();
        }
		if (null != userlist && userlist.size()>0) {
			for (WsUsersDO wu : userlist) {
				if (wu.getName().equals(curUser)) {
					continue;
				}
				wu.setIsFriend(0);

				int isMyFriend = wsFriendsService.selectCount(new EntityWrapper<WsFriendsDO>()
                    .eq("uname", curUser).eq("fname", wu.getName()));
				if (isMyFriend>0) {
					wu.setIsFriend(3);//已是好友
				}else {
					// 0:不是  1:申请中 2:被拒绝 3:申请成功
					WsFriendsApplyDO wfa = new WsFriendsApplyDO();
					wfa.setFromName(curUser);
					wfa.setToName(wu.getName());
					List<WsFriendsApplyDO> applyList = new ArrayList<>();
					Page<WsFriendsApplyDO> wsFriendsApplyDOPage = wsFriendsApplyService.selectPage(new Page<>(curPage, numPerPage),
                        new EntityWrapper<WsFriendsApplyDO>().eq("from_name", curUser)
                        .eq("to_name", wu.getName()));
					if (null != wsFriendsApplyDOPage){
                        applyList = wsFriendsApplyDOPage.getRecords();
                    }
					//List<WsFriendsApply> applyList = wsService.queryFriendsApplyList(wfa);
					if (null == applyList || applyList.size() == 0) {
						wu.setIsFriend(0);//去申请
					}else if (applyList.size() == 1) {
						Integer processStatus = applyList.get(0).getProcessStatus();
						wu.setIsFriend(processStatus);// 1:申请中 2:被拒绝 3:申请成功
					}else if (applyList.size() > 1) {
						// 过滤掉被驳回的记录
						for (WsFriendsApplyDO temp :  applyList) {
							if (temp.getProcessStatus() == 2) {
								continue;
							}
							wu.setIsFriend(temp.getProcessStatus());
						}
					}
				}
			}
		}
		PageResponseEntity rqe = new PageResponseEntity();
		rqe.setTotalCount(totalCount);
		rqe.setTotalPage(totalPage);
		rqe.setList(userlist);
		rqe.setParameter1(ZhddWebSocket.getClients().size());
		return JsonUtil.javaobject2Jsonobject(rqe);
	}
	
	/**
	 *分页 获取好友申请列表
	 * 
	 * true:存在  false:不存在
	 */
	@OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.FRIENDS,subModule="",describe="好友申请列表")
	@RequestMapping(value="getFriendsApplyList.json",method=RequestMethod.GET)
	@ResponseBody
	public Object getFriendsApplyList(@RequestParam("curPage") int curPage, @RequestParam("numPerPage") int numPerPage,@RequestParam("curUser") String curUser) {
		int totalCount = wsFriendsApplyService.selectCount(new EntityWrapper<WsFriendsApplyDO>().eq("to_name", curUser));
		int totalPage = 1;
		if (totalCount % numPerPage != 0) {
			totalPage = totalCount/numPerPage + 1;
		} else {
			totalPage = totalCount / numPerPage;
		}
		if (totalPage == 0) {
			totalPage = 1;
		}
		
		int start = 0;
		int limit = numPerPage;
		if (curPage == 1) {
			start = 0;
		} else {
			start = (curPage-1) * numPerPage;
		}

        List<WsFriendsApplyDO> userlist = new ArrayList<>();
        Page<WsFriendsApplyDO> wsFriendsApplyDOPage = wsFriendsApplyService.selectPage(new Page<WsFriendsApplyDO>(curPage, numPerPage),
                new EntityWrapper<WsFriendsApplyDO>().eq("to_name", curUser));
        if (null != wsFriendsApplyDOPage){
            userlist = wsFriendsApplyDOPage.getRecords();
        }

		PageResponseEntity rqe = new PageResponseEntity();
		rqe.setTotalCount(totalCount);
		rqe.setTotalPage(totalPage);
		rqe.setList(userlist);
		rqe.setParameter1(ZhddWebSocket.getClients().size());
		
		return JsonUtil.javaobject2Jsonobject(rqe);
	}
	
	/**
	 *分页 我的申请记录
	 * 
	 * true:存在  false:不存在
	 */
	@OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.FRIENDS,subModule="",describe="我的申请列表")
	@RequestMapping(value="getMyApplyList.json",method=RequestMethod.GET)
	@ResponseBody
	public Object getMyApplyList(@RequestParam("curPage") int curPage, @RequestParam("numPerPage") int numPerPage,@RequestParam("curUser") String curUser) {
		int totalCount = wsFriendsApplyService.selectCount(new EntityWrapper<WsFriendsApplyDO>().eq("to_name", curUser));
		int totalPage = 1;
		if (totalCount % numPerPage != 0) {
			totalPage = totalCount/numPerPage + 1;
		} else {
			totalPage = totalCount / numPerPage;
		}
		if (totalPage == 0) {
			totalPage = 1;
		}
		
		int start = 0;
		int limit = numPerPage;
		if (curPage == 1) {
			start = 0;
		} else {
			start = (curPage-1) * numPerPage;
		}

        List<WsFriendsApplyDO> userlist = new ArrayList<>();
        Page<WsFriendsApplyDO> wsFriendsApplyDOPage = wsFriendsApplyService.selectPage(new Page<WsFriendsApplyDO>(curPage, numPerPage),
                new EntityWrapper<WsFriendsApplyDO>().eq("from_name", curUser));
        if (null != wsFriendsApplyDOPage){
            userlist = wsFriendsApplyDOPage.getRecords();
        }

		PageResponseEntity rqe = new PageResponseEntity();
		rqe.setTotalCount(totalCount);
		rqe.setTotalPage(totalPage);
		rqe.setList(userlist);
		rqe.setParameter1(ZhddWebSocket.getClients().size());

		return JsonUtil.javaobject2Jsonobject(rqe);
	}
	
	/**
	 * 添加好友申请
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.INSERT,module=ModuleEnum.FRIENDS,subModule="",describe="添加好友")
	@RequestMapping(value = "addFriend.do",method=RequestMethod.POST)
	@ResponseBody
	public String addFriend(Model model,@RequestParam("fromUserName")String fromUserName,
										@RequestParam("toUserId")Integer toUserId) {
		Integer fromUserId = querySpecityUserName(fromUserName).getId();
		String toUserName = querySpecityUserId(toUserId).getName();
		
		logger.info(fromUserName+"申请添加"+toUserName+"为好友");
		int existCount = wsFriendsService.selectCount(new EntityWrapper<WsFriendsDO>().eq("uname", fromUserName)
				.eq("fname", toUserName));
		if (existCount<=0) {
			WsFriendsApplyDO wfa = new WsFriendsApplyDO();
			wfa.setFromId(fromUserId);
			wfa.setFromName(fromUserName);
			wfa.setToId(toUserId);
			wfa.setToName(toUserName);
			wfa.setProcessStatus(1);
			wsFriendsApplyService.insert(wfa);
		}else{
			logger.info(toUserName+"已是你的好友了,无需再次申请");
		}
		return "success";
	}
	
	/**
	 * 同意好友申请
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.UPDATE,module=ModuleEnum.FRIENDS,subModule="",describe="同意好友申请")
	@RequestMapping(value = "agreeFriend.do",method=RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String agreeFriend(@RequestParam("recordId")Integer recordId) {
		WsFriendsApplyDO wfa = wsFriendsApplyService.selectById(recordId);
		if (null == wfa){
			return "failed";
		}
		if (wfa.getProcessStatus().intValue() != 1){
			return "failed";
		}

		wfa.setProcessStatus(3);
		boolean updateWfaFlag = wsFriendsApplyService.updateById(wfa);
		if (updateWfaFlag) {
			WsFriendsDO wf1 = new WsFriendsDO();
			wf1.setUid(wfa.getFromId());
			wf1.setUname(wfa.getFromName());
			wf1.setFid(wfa.getToId());
			wf1.setFname(wfa.getToName());
			int isExist1 = wsFriendsService.selectCount(new EntityWrapper<WsFriendsDO>()
				.eq("uname", wfa.getFromName()).eq("fname", wfa.getToName()));
			if (isExist1<=0) {
			    wsFriendsService.insert(wf1);
			}

            WsFriendsDO wf2 = new WsFriendsDO();
			wf2.setUid(wfa.getToId());
			wf2.setUname(wfa.getToName());
			wf2.setFid(wfa.getFromId());
			wf2.setFname(wfa.getFromName());
			int isExist2 = wsFriendsService.selectCount(new EntityWrapper<WsFriendsDO>()
					.eq("uname", wfa.getToName()).eq("fname", wfa.getFromName()));
			if (isExist2<=0) {
                wsFriendsService.insert(wf2);
			}
		}
		return "success";
	}
	
	/**
	 * 拒绝好友申请
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.UPDATE,module=ModuleEnum.FRIENDS,subModule="",describe="拒绝好友申请")
	@RequestMapping(value = "deagreeFriend.do",method=RequestMethod.POST)
	@ResponseBody
	public String deagreeFriend(Model model,@RequestParam("recordId")Integer recordId) {
		WsFriendsApplyDO wfa = wsFriendsApplyService.selectById(recordId);
		if (null == wfa){
			return "failed";
		}
		if (wfa.getProcessStatus().intValue() != 1){
			return "failed";
		}

		wfa.setProcessStatus(2);
		boolean updateFlag = wsFriendsApplyService.updateById(wfa);
		if (updateFlag){
			return "success";
		}

		return "failed";
	}
	
	/**
	 * 删除好友
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.DELETE,module=ModuleEnum.FRIENDS,subModule="",describe="删除好友")
	@RequestMapping(value = "deleteFriend.do",method=RequestMethod.POST)
	@ResponseBody
	public String deleteFriend(@RequestParam("id")Integer id) {
		WsFriendsDO wsFriendsDO = wsFriendsService.selectById(id);
		if (null == wsFriendsDO){
			return "failed";
		}
		
		String uname = wsFriendsDO.getUname();
		String fname = wsFriendsDO.getFname();

		logger.info("uname:"+uname+"   fname:"+fname);
		if (StringUtils.isNotEmpty(uname) && StringUtils.isNotEmpty(fname)) {
			wsFriendsService.delete(new EntityWrapper<WsFriendsDO>().eq("uname", uname).eq("fname", fname));
			wsFriendsService.delete(new EntityWrapper<WsFriendsDO>().eq("uname", fname).eq("fname", uname));

			wsFriendsApplyService.delete(new EntityWrapper<WsFriendsApplyDO>().eq("from_name", fname)
					.eq("to_name", uname));
			wsFriendsApplyService.delete(new EntityWrapper<WsFriendsApplyDO>().eq("from_name", uname)
					.eq("to_name", fname));
		}
		
		return "success";
	}
	
	/**
	 * 查看朋友圈列表
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.CIRCLE,subModule="",describe="查看朋友圈列表")
	@PostMapping(value = "queryCircleByPage.do")
	@ResponseBody
	public Result<Page<WsCircleDO>> queryCircleList(int curPage, int numPerPage) {
		Page<WsCircleDO> qryPage = new Page<WsCircleDO>(curPage, numPerPage);

		Page<WsCircleDO> page = wsCircleService.selectPage(qryPage, new EntityWrapper<WsCircleDO>().orderBy("create_time",false));
        List<WsCircleDO> circleList = page.getRecords();
		Map<Integer, String> headImgMap = new HashMap<>();
		if (null != circleList && circleList.size()>0) {
			for (WsCircleDO wc : circleList) {
				if (!headImgMap.containsKey(wc.getUserId())){
					WsUserProfileDO wsUserProfileDO = wsUserProfileService.selectOne(new EntityWrapper<WsUserProfileDO>().eq("user_id", wc.getUserId()));
					if (null != wsUserProfileDO) {
						headImgMap.put(wc.getUserId(), wsUserProfileDO.getImg());
					}
				}

				//头像
				wc.setHeadImg(headImgMap.get(wc.getUserId()));
				List<WsCircleCommentDO> commentList = wsCircleCommentService.selectList(new EntityWrapper<WsCircleCommentDO>().eq("circle_id", wc.getId())
						.orderBy("create_time", false));
				if (null == commentList) {
					wc.setCommentList(new ArrayList<WsCircleCommentDO>());
				}else {
					wc.setCommentList(commentList);
				}
			}
		}

		return Result.ok(page);
	}
	
	/**
	 * 评论朋友圈
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.INSERT,module=ModuleEnum.CIRCLE,subModule="",describe="评论朋友圈")
	@RequestMapping(value = "toComment.do",method=RequestMethod.POST)
	@ResponseBody
	public Result<String> toComment(@RequestParam("user")String user,@RequestParam("circleId")Integer circleId,@RequestParam("comment")String comment) {
		WsCircleDO wsCircleDO = wsCircleService.selectById(circleId);
		if (null == wsCircleDO){
			return Result.fail();
		}

		WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
		if (null == wsUsersDO){
			return Result.fail();
		}

		WsCircleCommentDO wcc = new WsCircleCommentDO();
		wcc.setCircleId(circleId);
		wcc.setUserId(wsUsersDO.getId());
		wcc.setUserName(wsUsersDO.getName());
		wcc.setComment(comment);
		wcc.setCreateTime(new Date());
		boolean insertFlag = wsCircleCommentService.insert(wcc);
		if (insertFlag){
			return Result.ok();
		}
		return Result.fail();
	}
	
	/**
	 * 新增朋友圈
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.INSERT,module=ModuleEnum.CIRCLE,subModule="",describe="新增朋友圈")
	@RequestMapping(value = "addCircle.do",method=RequestMethod.POST)
	@ResponseBody
    @Transactional
	public Result<String> addCircle(@RequestParam("user")String user,
							@RequestParam("content")String content,
							@RequestParam(value="circleImgFile1",required=false) String circleImgFile1,
							@RequestParam(value="circleImgFile2",required=false) String circleImgFile2,
							@RequestParam(value="circleImgFile3",required=false) String circleImgFile3,
							@RequestParam(value="circleImgFile4",required=false) String circleImgFile4) {
		WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
		if (null == wsUsersDO){
			return Result.fail();
		}
		//增加积分
		Date dayStart = TimeUtil.getDayStart(new Date());
		Date dayEnd = TimeUtil.getDayEnd(new Date());
		int circleCnt = wsCircleService.selectCount(new EntityWrapper<WsCircleDO>().eq("user_id", wsUsersDO.getId())
				.ge("create_time", dayStart).le("create_time", dayEnd));
		if (circleCnt == 0){
			wsUsersDO.setCoinNum(wsUsersDO.getCoinNum()+15);
			wsUsersService.updateById(wsUsersDO);
		}

		WsCircleDO wc = new WsCircleDO();
		wc.setUserName(wsUsersDO.getName());
		wc.setUserId(wsUsersDO.getId());
		wc.setContent(content);
		wc.setCreateTime(new Date());
		wc.setLikeNum(0);
		wc.setPic1(circleImgFile1);
		wc.setPic2(circleImgFile2);
		wc.setPic3(circleImgFile3);
		wc.setPic4(circleImgFile4);
		boolean insertFlag = wsCircleService.insert(wc);
		if (insertFlag){
			return Result.ok();
		}
		return Result.fail();
	}
	
	
	/**
	 * 点赞朋友圈
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.UPDATE,module=ModuleEnum.CIRCLE,subModule="",describe="点赞朋友圈")
	@RequestMapping(value = "toLike.do",method=RequestMethod.POST)
	@ResponseBody
	public Result<String> toLike(@RequestParam("user")String user,@RequestParam("circleId")Integer circleId) {
		WsCircleDO wsCircleDO = wsCircleService.selectById(circleId);
		if (null == wsCircleDO){
			return Result.fail();
		}
		WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
		if (null == wsUsersDO){
			return Result.fail();
		}
		int likeNum = wsCircleDO.getLikeNum();
		wsCircleDO.setLikeNum(likeNum+1);
		boolean updateFlag = wsCircleService.updateById(wsCircleDO);
		if (updateFlag){
			return Result.ok();
		}
		return Result.fail();
	}
	
	/**
	 * 删除朋友圈
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.DELETE,module=ModuleEnum.CIRCLE,subModule="",describe="删除朋友圈")
	@RequestMapping(value = "toDeleteCircle.do",method=RequestMethod.POST)
	@ResponseBody
	public Result<String> toDeleteCircle(@RequestParam("id")Integer id) {
		WsCircleDO wsCircleDO = wsCircleService.selectById(id);
		if (null == wsCircleDO){
			return Result.fail();
		}
		boolean deleteFlag = wsCircleService.deleteById(id);
		if (deleteFlag){
			return Result.ok();
		}
		return Result.fail();
	}
	
	/**
	 * 删除评论
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.DELETE,module=ModuleEnum.CIRCLE,subModule="",describe="删除朋友圈评论")
	@RequestMapping(value = "toDeleteComment.do",method=RequestMethod.POST)
	@ResponseBody
	public Result<String> toDeleteComment(@RequestParam("id")Integer id) {
		WsCircleCommentDO wsCircleCommentDO = wsCircleCommentService.selectById(id);
		if (null == wsCircleCommentDO){
			return Result.fail();
		}
		boolean deleteFlag = wsCircleCommentService.deleteById(id);
		if (deleteFlag){
			return Result.ok();
		}
		return Result.fail();
	}	
	
	/**
	 * 获取我的好友列表
	 * 
	 * true:存在  false:不存在
	 */
	@OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.FRIENDS,subModule="",describe="获取我的好友列表")
	@RequestMapping(value="getMyFriendsList.json",method=RequestMethod.GET)
	@ResponseBody
	public Object getMyFriendsList(@RequestParam("curPage") int curPage, @RequestParam("numPerPage") int numPerPage,@RequestParam("curUser") String curUser) {
		int totalCount = wsFriendsService.selectCount(new EntityWrapper<WsFriendsDO>().eq("uname", curUser));
		int totalPage = 1;
		if (totalCount % numPerPage != 0) {
			totalPage = totalCount/numPerPage+1;
		}else {
			totalPage = totalCount/numPerPage;
		}
		if (totalPage == 0) {
			totalPage = 1;
		}
		
		int start = 0;
		int limit = numPerPage;
		if (curPage == 1) {
			start = 0;
		} else {
			start = (curPage-1) * numPerPage;
		}

        List<WsFriendsDO> userlist = new ArrayList<>();
		Page<WsFriendsDO> friendsPage = wsFriendsService.selectPage(new Page<>(curPage, numPerPage),
                    new EntityWrapper<WsFriendsDO>().eq("uname", curUser).orderBy("create_time",false));
		if (null != friendsPage){
            userlist = friendsPage.getRecords();
        }
		PageResponseEntity rqe = new PageResponseEntity();
		rqe.setTotalCount(totalCount);
		rqe.setTotalPage(totalPage);
		rqe.setList(userlist);
		rqe.setParameter1(ZhddWebSocket.getClients().size());
		
		return JsonUtil.javaobject2Jsonobject(rqe);
	}

	/**
	 * 设置个人信息
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.UPDATE,module=ModuleEnum.SETTING,subModule="",describe="设置个人信息")
	@RequestMapping(value = "setPersonInfo.do",method=RequestMethod.POST)
	@ResponseBody
	public String setPersonInfo(@RequestParam(value="userName",required=true) String userName,
								@RequestParam(value="realName",required=false) String realName,
								@RequestParam(value="headImg",required=false) String headImg,
								@RequestParam(value="sign",required=false) String sign,
								@RequestParam(value="age",required=false) Integer age,
								@RequestParam(value="sex",required=false) Integer sex,
								@RequestParam(value="sexText",required=false) String sexText,
								@RequestParam(value="tel",required=false) String tel,
								@RequestParam(value="address",required=false) String address,
								@RequestParam(value="profession",required=false) Integer profession,
								@RequestParam(value="professionText",required=false) String professionText,
								@RequestParam(value="hobby",required=false) Integer hobby,
								@RequestParam(value="hobbyText",required=false) String hobbyText
	) {
		WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", userName));
		try {
			// 检查表中是否有个人信息记录
			WsUserProfileDO wsUserProfileDO = wsUserProfileService.selectOne(new EntityWrapper<WsUserProfileDO>().eq("user_id", wsUsersDO.getId()));
			if (null == wsUserProfileDO) {
				logger.info("插入个人信息");
				WsUserProfileDO wup = new WsUserProfileDO();
				wup.setUserId(wsUsersDO.getId());
				wup.setUserName(userName);
				wup.setRealName(realName);
				wup.setImg(headImg);
				wup.setSign(sign);
				wup.setAge(age);
				wup.setSex(sex);
				wup.setSexText(sexText);
				wup.setTel(tel);
				wup.setAddress(address);
				wup.setProfession(profession);
				wup.setProfessionText(professionText);
				wup.setHobby(hobby);
				wup.setHobbyText(hobbyText);
				wsUserProfileService.insert(wup);
			}else {
				logger.info("更新个人信息");
				wsUserProfileDO.setUserName(userName);
				wsUserProfileDO.setRealName(realName);
				wsUserProfileDO.setImg(headImg);
				wsUserProfileDO.setSign(sign);
				wsUserProfileDO.setAge(age);
				wsUserProfileDO.setSex(sex);
				wsUserProfileDO.setSexText(sexText);
				wsUserProfileDO.setTel(tel);
				wsUserProfileDO.setAddress(address);
				wsUserProfileDO.setProfession(profession);
				wsUserProfileDO.setProfessionText(professionText);
				wsUserProfileDO.setHobby(hobby);
				wsUserProfileDO.setHobbyText(hobbyText);
				wsUserProfileService.updateById(wsUserProfileDO);
			}
		}catch (Exception e) {
			logger.error("更新个人信息失败!"+e.getMessage());
			return "failed";
		}
		return "success";
	}
	
	/**
	 * 查询个人信息
	 *
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.SETTING,subModule="",describe="查询个人信息")
	@RequestMapping(value="queryPersonInfo.json",method=RequestMethod.POST)
	@ResponseBody
	public Object queryPersonInfo(@RequestParam("user") String user) {
		WsUserProfileDO wsUserProfileDO = wsUserProfileService.selectOne(new EntityWrapper<WsUserProfileDO>()
					.eq("user_name", user));
		if (null != wsUserProfileDO) {
			return JsonUtil.javaobject2Jsonobject(wsUserProfileDO);
		}

		return "failed";
	}

	/**
	 * 查询软件版本
	 *
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.OTHER,subModule="",describe="查询软件版本")
	@RequestMapping(value="queryVersion.json",method=RequestMethod.GET)
	@ResponseBody
	public Object queryVersion() {
		return configMap.get("version");
	}
	
	/**
	 * 检查版本更新
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.UPDATE,module=ModuleEnum.OTHER,subModule="",describe="检查版本更新")
	@RequestMapping(value = "checkUpdate.do",method=RequestMethod.GET)
	@ResponseBody
	public Object checkUpdate(@RequestParam("version")String curVersion,@RequestParam("cmd")String cmd) {
		String shellRootPath = "/root/update/checkupdate.sh";
		
		String result = "";
	    try { 
	        Process ps = Runtime.getRuntime().exec(shellRootPath+" "+curVersion+" "+cmd); 
	        ps.waitFor(); 
	    
	        BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream())); 
	        StringBuffer sb = new StringBuffer(); 
	        String line; 
	        while ((line = br.readLine()) != null) { 
	          sb.append(line).append("\n"); 
	        } 
	        result = sb.toString(); 
	        System.out.println(result);
	        return result;
	      }  
	      catch (Exception e) {
	    	logger.error("检查/升级版本失败:"+e.getMessage());
	        return "操作失败!可能当前系统是非linux系统"; 
	      }
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
	 * 根据用户名称查询用户信息
	 * 
	 * @param name
	 * @return
	 */
	private WsUsersDO querySpecityUserName(String name) {
	    return wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", name));
	}
	
	/**
	 * 根据用户名称查询用户id
	 *
	 * @return
	 */
	private WsUsersDO querySpecityUserId(Integer id) {
	    return wsUsersService.selectById(id);
	}
	
	/**
	 * 移除session中的变量
	 * @param request
	 */
	private void removeSessionAttributes(String user, HttpServletRequest request) {
		HttpSession hs = request.getSession();
		hs.removeAttribute(CommonConstants.S_USER+"_"+user);
		hs.removeAttribute(CommonConstants.S_PASS+"_"+user);
		hs.removeAttribute(CommonConstants.S_WEBSERVERIP);
		hs.removeAttribute(CommonConstants.S_WEBSERVERPORT);
		hs.removeAttribute(CommonConstants.S_IMG+"_"+user);
	}
	
	/**
	 * 移除kookie
	 * @param request
	 */
	@SuppressWarnings("unused")
	private void removeCookies(HttpServletRequest request,HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(CommonConstants.S_USER) || cookie.getName().equals(CommonConstants.S_PASS)) {
					if (cookie.getMaxAge() != 0) {
						cookie.setMaxAge(0);
						response.addCookie(cookie);
					}
				}		
			}
		}
	}



	/**
	 * 跳转到好友列表页面
	 */
	@GetMapping("myFriends.page")
	String myFriendsPage(Model model, String user){
		model.addAttribute("user", user);
		return "ws/myFriends";
	}

	/**
	 * 获取好友列表列表数据
	 */
	@ResponseBody
	@GetMapping("/myFriendsList")
	public Result<Page<WsFriendsDO>> list(WsFriendsDO wsFriendsDTO){
		Wrapper<WsFriendsDO> wrapper = new EntityWrapper<WsFriendsDO>();
		if (StringUtils.isNotBlank(wsFriendsDTO.getUname())){
			wrapper.eq("uname", wsFriendsDTO.getUname());
		}
		Page<WsFriendsDO> qryPage = getPage(WsFriendsDO.class);
		Page<WsFriendsDO> page = wsFriendsService.selectPage(qryPage, wrapper);
		return Result.ok(page);
	}

	@ResponseBody
	@GetMapping("/querySessionData")
	public String querySessionData(String user, HttpServletRequest request){
		logger.info("查询session数据, user:{}"+user);
		HttpSession httpSession = request.getSession();
		String sessionUser = "";
		String sessionPass = "";
		try {
			sessionUser = (String) httpSession.getAttribute(CommonConstants.S_USER+"_"+user);
			sessionPass = (String) httpSession.getAttribute(CommonConstants.S_PASS+"_"+user);
		}catch (Exception e){
			e.printStackTrace();
		}
		logger.info("sessionUser:"+sessionUser);
		logger.info("sessionPass:"+sessionPass);

		return sessionUser+":"+sessionPass;
	}

	//查询系统信息
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
	 * 网易新闻
	 * @return
	 */
	@RequestMapping("/wangyiNews.page")
	public String wangyiNews() {
		return "ws/wangyiNews";
	}

	/**
	 * 网易: https://3g.163.com
	 * 新闻：/touch/reconstruct/article/list/BBM54PGAwangning/0-10.html
	 * 娱乐：/touch/reconstruct/article/list/BA10TA81wangning/0-10.html
	 * 体育：/touch/reconstruct/article/list/BA8E6OEOwangning/0-10.html
	 * 财经：/touch/reconstruct/article/list/BA8EE5GMwangning/0-10.html
	 * 军事：/touch/reconstruct/article/list/BAI67OGGwangning/0-10.html
	 * 科技：/touch/reconstruct/article/list/BA8D4A3Rwangning/0-10.html
	 * 手机：/touch/reconstruct/article/list/BAI6I0O5wangning/0-10.html
	 * 数码：/touch/reconstruct/article/list/BAI6JOD9wangning/0-10.html
	 * 时尚：/touch/reconstruct/article/list/BA8F6ICNwangning/0-10.html
	 * 游戏：/touch/reconstruct/article/list/BAI6RHDKwangning/0-10.html
	 * 教育：/touch/reconstruct/article/list/BA8FF5PRwangning/0-10.html
	 * 健康：/touch/reconstruct/article/list/BDC4QSV3wangning/0-10.html
	 * 旅游：/touch/reconstruct/article/list/BEO4GINLwangning/0-10.html
	 * 视频：/touch/nc/api/video/recommend/Video_Recom/0-10.do?callback=videoList
	 * @return
	 */
	@RequestMapping("/queryWangyiNewsType")
	@ResponseBody
	public Result<List<WangyiNewsBean>> queryWangyiNewsType(){
		String baseUrl = "https://3g.163.com";
		String branchUrlFormat = baseUrl+"/touch/reconstruct/article/list/%swangning";
		List<WangyiNewsBean> list = new ArrayList<>();
		WangyiNewsBean wyBean1 = new WangyiNewsBean("新闻", "BBM54PGA", String.format(branchUrlFormat, "BBM54PGA"));
		WangyiNewsBean wyBean2 = new WangyiNewsBean("娱乐", "BA10TA81", String.format(branchUrlFormat, "BA10TA81"));
		WangyiNewsBean wyBean3 = new WangyiNewsBean("体育", "BA8E6OEO", String.format(branchUrlFormat, "BA8E6OEO"));
		WangyiNewsBean wyBean4 = new WangyiNewsBean("财经", "BA8EE5GM", String.format(branchUrlFormat, "BA8EE5GM"));
		WangyiNewsBean wyBean5 = new WangyiNewsBean("军事", "BAI67OGG", String.format(branchUrlFormat, "BAI67OGG"));
		WangyiNewsBean wyBean6 = new WangyiNewsBean("科技", "BA8D4A3R", String.format(branchUrlFormat, "BA8D4A3R"));
		WangyiNewsBean wyBean7 = new WangyiNewsBean("手机", "BAI6I0O5", String.format(branchUrlFormat, "BAI6I0O5"));
		WangyiNewsBean wyBean8 = new WangyiNewsBean("数码", "BAI6JOD9", String.format(branchUrlFormat, "BAI6JOD9"));
		WangyiNewsBean wyBean9 = new WangyiNewsBean("时尚", "BA8F6ICN", String.format(branchUrlFormat, "BA8F6ICN"));
		WangyiNewsBean wyBean10 = new WangyiNewsBean("游戏", "BAI6RHDK", String.format(branchUrlFormat, "BAI6RHDK"));
		WangyiNewsBean wyBean11 = new WangyiNewsBean("教育", "BA8FF5PR", String.format(branchUrlFormat, "BA8FF5PR"));
		WangyiNewsBean wyBean12 = new WangyiNewsBean("健康", "BDC4QSV3", String.format(branchUrlFormat, "BDC4QSV3"));
		WangyiNewsBean wyBean13 = new WangyiNewsBean("旅游", "BEO4GINL", String.format(branchUrlFormat, "BEO4GINL"));
		WangyiNewsBean wyBean14 = new WangyiNewsBean("视频", "", "https://3g.163.com/touch/nc/api/video/recommend/Video_Recom/0-10.do?callback=videoList");
		list.add(wyBean1);
		list.add(wyBean2);
		list.add(wyBean3);
		list.add(wyBean4);
		list.add(wyBean5);
		list.add(wyBean6);
		list.add(wyBean7);
		list.add(wyBean8);
		list.add(wyBean9);
		list.add(wyBean10);
		list.add(wyBean11);
		list.add(wyBean12);
		list.add(wyBean13);
		list.add(wyBean14);

		return Result.ok(list);
	}

	@RequestMapping("/queryWangyiNews")
	@ResponseBody
	@CrossOrigin(value = "*")
	public Object queryWangyiNewsType(String url){
		Object result = HttpClientUtils.sendGetRequestV2(url+"/0-99.html", new LinkedHashMap<>(), Object.class);
		System.out.println(result);
		return result;
	}
}
