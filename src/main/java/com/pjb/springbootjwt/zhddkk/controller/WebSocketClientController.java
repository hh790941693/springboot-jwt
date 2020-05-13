package com.pjb.springbootjwt.zhddkk.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.common.redis.RedisUtil;
import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.domain.*;
import com.pjb.springbootjwt.zhddkk.entity.*;
import com.pjb.springbootjwt.zhddkk.service.*;
import com.pjb.springbootjwt.zhddkk.websocket.WebSocketConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.pjb.springbootjwt.zhddkk.util.CommonUtil;
import com.pjb.springbootjwt.zhddkk.util.JsonUtil;
import com.pjb.springbootjwt.zhddkk.util.SecurityAESUtil;
import com.pjb.springbootjwt.zhddkk.websocket.ZhddWebSocket;

/**
 * webSocket控制器
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("ws")
public class WebSocketClientController
{
	private static final int COOKIE_TIMEOUT = 1800; //cookie过期时间 30分钟
	
	private static final int SESSION_TIMEOUT = 600; //session不活动时的超时时间  10分钟
	
	private static final String REDIS_KEY_PREFIX = "ws_"; //登陆用户的redis缓存前缀
	
	private static final Log logger = LogFactory.getLog(WebSocketClientController.class);
	
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");
	
	private static Map<String,String> configMap = WsInterceptor.getConfigMap();

	@Autowired
	private WebSocketConfig webSocketConfig;

	@Autowired
	private WsService wsService;

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
    private WsDicService wsDicService;

    @Autowired
    private WsFriendsService wsFriendsService;

    @Autowired
    private WsFriendsApplyService wsFriendsApplyService;

    @Autowired
    private WsChatlogService wsChatlogService;
	
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
		request.getSession().setMaxInactiveInterval(SESSION_TIMEOUT); //session不活动失效时间
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
		
		if (user.equals("admin")) {
			return "ws/wsserverIndex";
		}else{
			return "ws/wsclientIndex";
		}
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
	public String wsregister(Model model,@RequestParam("user")String user,@RequestParam("pass")String pass,
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
		String sessionUser = (String)request.getSession().getAttribute(CommonConstants.S_USER);
		System.out.println("清理session缓存:"+sessionUser);

		if (StringUtils.isBlank(sessionUser)){
			return "success";
		}

		if (user.equals(sessionUser)) {
			removeSessionAttributes(request);
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
	public Map<String, List<WsCommon>> queryAllCommonData() {
		logger.debug("访问queryAllCommonData.do");
		Map<String, List<WsCommon>> map = buildCommonData();
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
		return "ws/wsclientCircle";
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
	public String updatePassword(Model model,@RequestParam("user")String user,
			@RequestParam("pass")String newPass,
			@RequestParam("confirmPass")String confirmPass,
			@RequestParam("select1")String question1,@RequestParam("answer1")String answer1,
			@RequestParam("select2")String question2,@RequestParam("answer2")String answer2,
			@RequestParam("select3")String question3,@RequestParam("answer3")String answer3) {
		String title="更新密码";
		String result = "failed";

		WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
		if (null != wsUsersDO) {
			if (wsUsersDO.getQuestion1().equals(question1) && wsUsersDO.getAnswer1().equals(answer1)
					&& wsUsersDO.getQuestion2().equals(question2) && wsUsersDO.getAnswer2().equals(answer2)
					&& wsUsersDO.getQuestion3().equals(question3) && wsUsersDO.getAnswer3().equals(answer3)
					&& newPass.equals(confirmPass)) {
				//对新密码进行加密
				String newPassEncrypt = SecurityAESUtil.encryptAES(newPass, CommonConstants.AES_PASSWORD);
				wsUsersDO.setPassword(newPassEncrypt);
				boolean updateFlag = wsUsersService.updateById(wsUsersDO);
				if (updateFlag) {
					result = "success";
				}
			}
		}
		
		model.addAttribute("title",title);
		model.addAttribute("result",result);
		return result;
	}
	
	/**
	 * 获取在线人数信息
	 * 
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.CHAT,subModule="",describe="获取在线人数信息")
	@RequestMapping(value = "getOnlineInfo.json")
	@ResponseBody
	public Object getOnlineInfo(@RequestParam(value="user",required=false)String user) {
		Map<String, ZhddWebSocket> socketMap = ZhddWebSocket.getClients();
		//List<WsUser> allUserListTmp = wsService.queryWsUser(new WsUser());
		List<WsUsersDO> allUserListTmp = wsUsersService.selectList(null);
		List<WsUsersDO> allUserList = new ArrayList<WsUsersDO>();
		List<WsUsersDO> onlineUserList = new ArrayList<WsUsersDO>();
		List<WsUsersDO> offlineUserList = new ArrayList<WsUsersDO>();
		List<WsUsersDO> friendsUserList = new ArrayList<WsUsersDO>();//好友列表
		WsUsersDO currentOnlineUserInfo = new WsUsersDO();
		for (WsUsersDO wu : allUserListTmp) {
			if (wu.getName().equals("admin")) {
				continue;
			}
			if (StringUtils.isNotBlank(user) && wu.getName().equals(user)) {
				currentOnlineUserInfo = wu;
			}

			if (socketMap.containsKey(wu.getName())) {
				onlineUserList.add(wu);
			}else {
				wu.setState("0");
				offlineUserList.add(wu);
			}
			
			if (!user.equals(wu.getName())) {
				//检查wu.getName()是否是user的好友
				List<WsFriendsDO> friendsList = wsFriendsService.selectList(new EntityWrapper<WsFriendsDO>()
						.eq("uname", user).eq("fname", wu.getName()));
				if (friendsList.size()>0) {
					friendsUserList.add(wu);
				}
			}
			
			allUserList.add(wu);
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

		return JsonUtil.javaobject2Jsonobject(woi);
	}
	
	/**
	 * 检查某用户是否已经注册过
	 * 
	 * true:存在  false:不存在
	 */
	@OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.REGISTER,subModule="",describe="检查用户是否已经注册")
	@RequestMapping(value="checkUserRegisterStatus.json",method=RequestMethod.POST)
	@ResponseBody
	public Object checkUserRegisterStatus(@RequestParam("user") String user) {
		String result = "{\"result\":\"false\"}";
		int userCount = wsUsersService.selectCount(new EntityWrapper<WsUsersDO>().eq("name", user));
		if (userCount>0){
			result = "{\"result\":\"true\"}";
		}

		return JsonUtil.jsonstr2Jsonobject(result);
	}
	
	/**
	 * 分页显示所有用户信息
	 * 
	 * true:存在  false:不存在
	 */
	@OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.FRIENDS,subModule="",describe="显示所有用户信息")
	@RequestMapping(value="showAllUser.json",method=RequestMethod.POST,produces="application/json")
	@ResponseBody
	public Object getOnlineUsersByPage(@RequestBody WsUser params) {
		int totalCount = wsService.queryWsUserCount(params);
		int numPerPage = params.getNumPerPage();
		int curPage = params.getCurPage();
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
		
		int start = 0;
		int limit = numPerPage;
		if (curPage == 1){
			start = 0;
		}else{
			start = (curPage-1)*numPerPage;
		}
		params.setStart(start);
		params.setLimit(limit);
		List<WsUser> userlist = wsService.queryWsUserByPage(params);
		if (null != userlist && userlist.size()>0) {
			for (WsUser wu : userlist) {
				if (wu.getName().equals(curUser)) {
					continue;
				}
				wu.setIsFriend(0);
				
				WsFriends wf = new WsFriends();
				wf.setUname(curUser);
				wf.setFname(wu.getName());
				List<WsFriends> isMyFriend = wsService.queryMyFriendsList(wf);
				if (null != isMyFriend && isMyFriend.size()>0) {
					wu.setIsFriend(3);//已是好友
				}else {
					// 0:不是  1:申请中 2:被拒绝 3:申请成功
					WsFriendsApply wfa = new WsFriendsApply();
					wfa.setFromName(curUser);
					wfa.setToName(wu.getName());
					wfa.setStart(0);
					wfa.setLimit(10);
	
					List<WsFriendsApply> applyList = wsService.queryFriendsApplyList(wfa);
					if (null == applyList || applyList.size() == 0) {
						wu.setIsFriend(0);//去申请
					}else if (applyList.size() == 1) {
						Integer processStatus = applyList.get(0).getProcessStatus();
						wu.setIsFriend(processStatus);// 1:申请中 2:被拒绝 3:申请成功
					}else if (applyList.size() > 1) {
						// 过滤掉被驳回的记录
						for (WsFriendsApply temp :  applyList) {
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

		WsFriendsApply wfa = new WsFriendsApply();
		wfa.setToName(curUser);
		wfa.setStart(start);
		wfa.setLimit(limit);
		List<WsFriendsApply> userlist = wsService.queryFriendsApplyList(wfa);
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

		WsFriendsApply wfa = new WsFriendsApply();
		wfa.setFromName(curUser);
		wfa.setStart(start);
		wfa.setLimit(limit);
		List<WsFriendsApply> userlist = wsService.queryMyApplyList(wfa);
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
		WsFriends wf = new WsFriends();
		wf.setUname(fromUserName);
		wf.setFname(toUserName);
		int existCount = wsFriendsService.selectCount(new EntityWrapper<WsFriendsDO>().eq("uname", fromUserName)
				.eq("fname", toUserName));
		if (existCount<=0) {
			WsFriendsApply wfa = new WsFriendsApply();
			wfa.setFromId(fromUserId);
			wfa.setFromName(fromUserName);
			wfa.setToId(toUserId);
			wfa.setToName(toUserName);
			wfa.setProcessStatus(1);
			wsService.insertFriendsApply(wfa);
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
			WsFriends wf1 = new WsFriends();
			wf1.setUid(wfa.getFromId());
			wf1.setUname(wfa.getFromName());
			wf1.setFid(wfa.getToId());
			wf1.setFname(wfa.getToName());
			int isExist1 = wsFriendsService.selectCount(new EntityWrapper<WsFriendsDO>()
				.eq("uname", wfa.getFromName()).eq("fname", wfa.getToName()));
			if (isExist1<=0) {
				wsService.insertMyFriend(wf1);
			}

			WsFriends wf2 = new WsFriends();
			wf2.setUid(wfa.getToId());
			wf2.setUname(wfa.getToName());
			wf2.setFid(wfa.getFromId());
			wf2.setFname(wfa.getFromName());
			int isExist2 = wsFriendsService.selectCount(new EntityWrapper<WsFriendsDO>()
					.eq("uname", wfa.getToName()).eq("fname", wfa.getFromName()));
			if (isExist2<=0) {
				wsService.insertMyFriend(wf2);
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
		Page<WsCircleDO> page = new Page<WsCircleDO>(curPage, numPerPage);
		List<WsCircleDO> circleList = wsCircleService.selectList(new EntityWrapper<WsCircleDO>().orderBy("create_time",false));
		page.setRecords(circleList);
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
//		int totalCount = wsService.queryCircleCount(new WsCircle());
//		int totalPage = 1;
//		if (totalCount % numPerPage != 0){
//			totalPage = totalCount/numPerPage + 1;
//		}
//		else{
//			totalPage = totalCount/numPerPage;
//		}
//		if (totalPage == 0) {
//			totalPage = 1;
//		}
//
//		int start = 0;
//		int limit = numPerPage;
//		if (curPage == 1){
//			start = 0;
//		}
//		else{
//			start = (curPage-1) * numPerPage;
//		}
//		WsCircle queryCircle = new WsCircle();
//		queryCircle.setStart(start);
//		queryCircle.setLimit(limit);
//		List<WsCircle> circleList = wsService.queryCircleByPage(queryCircle);
//		if (null != circleList && circleList.size()>0) {
//			for (WsCircle wc : circleList) {
//				WsCircleComment queryCommentCond = new WsCircleComment();
//				queryCommentCond.setCircleId(wc.getId());
//				List<WsCircleComment> commentList = wsService.queryCircleCommentList(queryCommentCond);
//				if (null == commentList) {
//					wc.setCommentList(new ArrayList<WsCircleComment>());
//				}else {
//					wc.setCommentList(commentList);
//				}
//			}
//		}
//
//		PageResponseEntity rqe = new PageResponseEntity();
//		rqe.setTotalCount(circleList.size());
//		rqe.setTotalPage(totalPage);
//		rqe.setList(circleList);
//		Object object = JsonUtil.javaobject2Jsonobject(rqe);
//		return object;
	}
	
	/**
	 * 评论朋友圈
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.INSERT,module=ModuleEnum.CIRCLE,subModule="",describe="评论朋友圈")
	@RequestMapping(value = "toComment.do",method=RequestMethod.POST)
	@ResponseBody
	public Object toComment(@RequestParam("user")String user,@RequestParam("circleId")Integer circleId,@RequestParam("comment")String comment) {
		WsCircleDO wsCircleDO = wsCircleService.selectById(circleId);
		if (null == wsCircleDO){
			return "failed";
		}

		WsCircleComment wcc = new WsCircleComment();
		wcc.setCircleId(circleId);
		Integer userId = querySpecityUserName(user).getId();
		wcc.setUserId(userId);
		wcc.setUserName(user);
		wcc.setComment(comment);
		wcc.setCreateTime(new Date());
		wsService.insertCircleComment(wcc);
		return "success";
	}
	
	/**
	 * 新增朋友圈
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.INSERT,module=ModuleEnum.CIRCLE,subModule="",describe="新增朋友圈")
	@RequestMapping(value = "addCircle.do",method=RequestMethod.POST)
	@ResponseBody
	public Object addCircle(@RequestParam("user")String user,
							@RequestParam("content")String content,
							@RequestParam(value="circleImgFile1",required=false) String circleImgFile1,
							@RequestParam(value="circleImgFile2",required=false) String circleImgFile2,
							@RequestParam(value="circleImgFile3",required=false) String circleImgFile3,
							@RequestParam(value="circleImgFile4",required=false) String circleImgFile4) {
		try {
			WsUsersDO wsUser = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
			if (null == wsUser){
				return "failed";
			}
			WsCircleDO wc = new WsCircleDO();
			wc.setUserName(user);
			wc.setUserId(wsUser.getId());
			wc.setContent(content);
			wc.setCreateTime(new Date());
			wc.setLikeNum(0);
			wc.setPic1(circleImgFile1);
			wc.setPic2(circleImgFile2);
			wc.setPic3(circleImgFile3);
			wc.setPic4(circleImgFile4);
			wsCircleService.insert(wc);
		}catch (Exception e) {
			System.out.println("新增朋友失败:"+e.getMessage());
			return "failed";
		}
		
		return "success";
	}
	
	
	/**
	 * 点赞朋友圈
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.UPDATE,module=ModuleEnum.CIRCLE,subModule="",describe="点赞朋友圈")
	@RequestMapping(value = "toLike.do",method=RequestMethod.POST)
	@ResponseBody
	public Object toLike(@RequestParam("user")String user,@RequestParam("circleId")Integer circleId) {
		WsCircleDO wsCircleDO = wsCircleService.selectById(circleId);
		if (null == wsCircleDO){
			return "failed";
		}
		int likeNum = wsCircleDO.getLikeNum();
		wsCircleDO.setLikeNum(likeNum+1);
		wsCircleService.updateById(wsCircleDO);
		return "success";
	}
	
	/**
	 * 删除朋友圈
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.DELETE,module=ModuleEnum.CIRCLE,subModule="",describe="删除朋友圈")
	@RequestMapping(value = "toDeleteCircle.do",method=RequestMethod.POST)
	@ResponseBody
	public Object toDeleteCircle(@RequestParam("id")Integer id) {
		WsCircleDO wsCircleDO = wsCircleService.selectById(id);
		if (null == wsCircleDO){
			return "failed";
		}
		wsCircleService.deleteById(id);
		return "success";
	}
	
	/**
	 * 删除评论
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.DELETE,module=ModuleEnum.CIRCLE,subModule="",describe="删除朋友圈评论")
	@RequestMapping(value = "toDeleteComment.do",method=RequestMethod.POST)
	@ResponseBody
	public Object toDeleteComment(@RequestParam("id")Integer id) {
		WsCircleCommentDO wsCircleCommentDO = wsCircleCommentService.selectById(id);
		if (null == wsCircleCommentDO){
			return "failed";
		}
		wsCircleCommentService.deleteById(id);
		return "success";
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

		WsFriends wf = new WsFriends();
		wf.setUname(curUser);
		wf.setStart(start);
		wf.setLimit(limit);
		List<WsFriends> userlist = wsService.queryMyFriendsList(wf);
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
	
	
	private Map<String,List<WsCommon>> buildCommonData() {
		Map<String,List<WsCommon>> commonMap = new HashMap<String,List<WsCommon>>();
		List<WsCommon> commonList = wsService.queryCommon(new WsCommon());

		for (WsCommon common : commonList) {
			String type = common.getType();
			String name = common.getName();
			
			if (CommonUtil.validateEmpty(type) || CommonUtil.validateEmpty(name)) {
				continue;
			}

			if (commonMap.containsKey(type)) {
				List<WsCommon> tmpCommonList = commonMap.get(type);
				tmpCommonList.add(common);
			}else {
				List<WsCommon> tmpDicList = new ArrayList<WsCommon>();
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
	private WsUser querySpecityUserName(String name) {
		WsUser wu = new WsUser();
		wu.setName(name);
		List<WsUser> wuList = wsService.queryWsUser(wu);
		if (wuList.size() > 0) {
			return wuList.get(0);
		}
		return null;
	}
	
	/**
	 * 根据用户名称查询用户id
	 *
	 * @return
	 */
	private WsUser querySpecityUserId(Integer id) {
		WsUser wu = new WsUser();
		wu.setId(id);
		List<WsUser> wuList = wsService.queryWsUser(wu);
		if (wuList.size() > 0) {
			return wuList.get(0);
		}
		return null;
	}
	
	/**
	 * 移除session中的变量
	 * @param request
	 */
	private void removeSessionAttributes(HttpServletRequest request) {
		HttpSession hs = request.getSession();
		hs.removeAttribute(CommonConstants.S_USER);
		hs.removeAttribute(CommonConstants.S_PASS);
		hs.removeAttribute(CommonConstants.S_WEBSERVERIP);
		hs.removeAttribute(CommonConstants.S_WEBSERVERPORT);
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
}
