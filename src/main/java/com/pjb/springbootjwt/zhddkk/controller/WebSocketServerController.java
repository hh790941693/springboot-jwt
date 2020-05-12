package com.pjb.springbootjwt.zhddkk.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.bean.ChatMessageBean;
import com.pjb.springbootjwt.zhddkk.bean.JsonResult;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.entity.*;
import com.pjb.springbootjwt.zhddkk.enumx.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.enumx.OperationEnum;
import com.pjb.springbootjwt.zhddkk.interceptor.WsInterceptor;
import com.pjb.springbootjwt.zhddkk.service.WsOperLogService;
import com.pjb.springbootjwt.zhddkk.service.WsService;
import com.pjb.springbootjwt.zhddkk.util.*;
import com.pjb.springbootjwt.zhddkk.websocket.ZhddWebSocket;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONArray;

/**
 * websocket服务端控制器
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("ws")
public class WebSocketServerController extends AdminBaseController
{
	private static final Log logger = LogFactory.getLog(WebSocketServerController.class);
	
	private static Map<String,String> configMap = WsInterceptor.getConfigMap();
	
	@Autowired
	private WsService wsService;

	@Autowired
	private WsOperLogService wsOperLogService;
	
	/**
	 * 让某用户下线
	 * @param user
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.UPDATE,module=ModuleEnum.USER_MANAGE,subModule="",describe="使用户下线")
	@RequestMapping(value = "offlineUser.do")
	@ResponseBody
	public String offlineUser(@RequestParam("user") String user)
	{
		Map<String, ZhddWebSocket> socketMap = ZhddWebSocket.getClients();
		for (Entry<String,ZhddWebSocket> entry : socketMap.entrySet())
		{
			if (entry.getKey().equals(user))
			{
				try {
					entry.getValue().getSession().close();
					ZhddWebSocket.getClients().remove(user);
					break;
				} catch (IOException e) {
					e.printStackTrace();
					return "failed";
				}
			}
		}
		
		WsUser updateWu2 = new WsUser();
		updateWu2.setName(user);
		updateWu2.setState("0");
		wsService.updateWsUser(updateWu2);

		return "success";
	}
	
	/**
	 * 让某用户禁用/启用
	 * @param user
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.UPDATE,module=ModuleEnum.USER_MANAGE,subModule="",describe="用户的禁用/启用")
	@RequestMapping(value = "enableUser.do")
	@ResponseBody
	public String enableUser(@RequestParam("user") String user,@RequestParam("enable") String enable)
	{
		WsUser wu = new WsUser();
		wu.setName(user);
		wu.setEnable(enable);
		wsService.updateWsUser(wu);
		return "success";
	}
	
	/**
	 * 让某用户禁言/开言
	 * @param user
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.UPDATE,module=ModuleEnum.USER_MANAGE,subModule="",describe="用户的禁言/开言")
	@RequestMapping(value = "enableSpeak.do")
	@ResponseBody
	public String enableSpeak(@RequestParam("user") String user,@RequestParam("speak") String speak)
	{
		WsUser wu = new WsUser();
		wu.setName(user);
		wu.setSpeak(speak);
		wsService.updateWsUser(wu);
		return "success";
	}
	
	/**
	 * 给指定用户的发送信息
	 * @param user
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.INSERT,module=ModuleEnum.USER_MANAGE,subModule="",describe=" 给用户发送信息")
	@RequestMapping(value = "sendText.do")
	@ResponseBody
	public String sendText(@RequestParam("user") String user, @RequestParam("msg") String message)
	{
		String result = "offline";
		
		String msgFrom = message.split("from:")[1].split(";")[0].trim();
		String msgTo = message.split("to:")[1].split(";")[0].trim();
		String typeId = message.split("typeId:")[1].split(";")[0].trim();
		String typeDesc = message.split("typeDesc:")[1].split(";")[0].trim();
		String msgStr = message.split("msg:")[1].trim();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String curTime = sdf.format(new Date());
		ChatMessageBean chatBean = new ChatMessageBean(curTime, typeId, typeDesc, msgFrom, msgTo, msgStr);

		// 在线人数
		Map<String, ZhddWebSocket> socketMap = ZhddWebSocket.getClients();
		if (socketMap.containsKey(user))
		{
			try {
				socketMap.get(user).getSession().getBasicRemote().sendText(chatBean.toString());
			} catch (IOException e) {
				e.printStackTrace();
				return "failed";
			}
			result = "success";
		}

		return result;
	}
	
	/**
	 * 用户管理页面
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.USER_MANAGE,subModule="",describe="用户管理首页")
	@RequestMapping(value = "wsserverUser.page", method = RequestMethod.GET)
	public String userManage(Model model)
	{
		logger.debug("访问wsserverUser.page");
		return "ws/wsserverUser";
	}
	
	/**
	 * 聊天记录管理页面
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.CHAT_HISTORY_MANAGE,subModule="",describe="聊天记录管理首页")
	@RequestMapping(value = "wsserverChartLog.page", method = RequestMethod.GET)
	public String chatLogManage(Model model)
	{
		logger.debug("访问wsserverChartLog.page");
		List<WsUser> userInfoList = wsService.queryWsUser(new WsUser());
		List<String> userList = new ArrayList<String>();
		for (WsUser wu : userInfoList)
		{
			userList.add(wu.getName());
		}
		model.addAttribute("users", JSONArray.fromObject(userList));
		return "ws/wsserverChartLog";
	}
	
	/**
	 * 操作日志管理页面
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.OPERATION_LOG_MANAGE,subModule="",describe="操作日志记录管理首页")
	@RequestMapping(value = "wsserverOperationLog.page", method = RequestMethod.GET)
	public String operationLog(Model model)
	{
		logger.debug("访问wsserverOperationLog.page");
		
		//模块列表
		ModuleEnum[] values = ModuleEnum.values();
		List<String> moduleList = new ArrayList<String>();
		for (ModuleEnum me : values) {
			moduleList.add(me.getValue());
		}
		model.addAttribute("modules", JSONArray.fromObject(moduleList));
		
		List<WsUser> userInfoList = wsService.queryWsUser(new WsUser());
		List<String> userList = new ArrayList<String>();
		for (WsUser wu : userInfoList){
			userList.add(wu.getName());
		}
		model.addAttribute("users", JSONArray.fromObject(userList));
		return "ws/wsserverOperationLog";
	}
	
	/**
	 * 操作日志管理页面
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.OPERATION_LOG_MANAGE,subModule="",describe="操作日志记录管理首页2")
	@RequestMapping(value = "wsserverOperationLogByBootstrap.page", method = RequestMethod.GET)
	public String operationLogByBootstrap(Model model)
	{
		logger.debug("访问wsserverOperationLogByBootstrap.page");
		
		//模块列表
		ModuleEnum[] values = ModuleEnum.values();
		List<String> moduleList = new ArrayList<String>();
		for (ModuleEnum me : values) {
			moduleList.add(me.getValue());
		}
		model.addAttribute("modules", JSONArray.fromObject(moduleList));
		
		List<WsUser> userInfoList = wsService.queryWsUser(new WsUser());
		List<String> userList = new ArrayList<String>();
		for (WsUser wu : userInfoList){
			userList.add(wu.getName());
		}
		model.addAttribute("users", JSONArray.fromObject(userList));
		return "ws/wsserverOperationLogByBootstrap";
	}
	
	/**
	 * 聊天记录监控页面
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.USER_MANAGE,subModule="",describe="聊天记录监控首页")
	@RequestMapping(value = "wsserverChartLogMonitor.page", method = RequestMethod.GET)
	public String chatLogMonitor(Model model,HttpServletRequest request)
	{
		logger.debug("访问wsserverChartLogMonitor.page");
		model.addAttribute("webserverip", configMap.get("webserver.ip"));
		//model.addAttribute("webserverport", configMap.get("webserver.port"));
		int serverPort = request.getServerPort();
		model.addAttribute("webserverport", serverPort);
		return "ws/wsserverChartLogMonitor";
	}
	
	
	/**
	 * 常用字典项配置页面
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.CONFIGURATION,subModule="",describe="常用字典项配置首页")
	@RequestMapping(value = "wsserverCommon.page", method = RequestMethod.GET)
	public String commonManager(Model model,@RequestParam("type") String type)
	{
		logger.debug("访问wsserverCommon.page,type="+type);
		model.addAttribute("type", type);
		String title="";
		if (type.equals("mgc"))
		{
			title="敏感词";
		}
		else if (type.equals("zh"))
		{
			title="脏话";
		}
		else if (type.equals("cyy"))
		{
			title="常用语";
		}
		else if (type.equals("zcwt"))
		{
			title="注册问题";
		}
		model.addAttribute("title", title);
		return "ws/wsserverCommon";
	}
	
	/**
	 * 广告首页
	 * 
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.AD_PUBLISH,subModule="",describe="广告首页")
	@RequestMapping(value = "ad.page")
	public String adIndex()
	{
		logger.debug("访问ad.page");
		return "ws/wsserverAd";
	}
	
	/**
	 * 添加广告
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.AD_PUBLISH,subModule="",describe="添加广告首页")
	@RequestMapping(value = "addAd.page")
	public String addAd()
	{
		logger.debug("访问addAd.page");
		return "ws/wsserverAddAd";
	}	

	/**
	 * 发布广告
	 * 
	 * @param adTitle
	 * @param adContent
	 * @param req
	 * @param resp
	 */
	@OperationLogAnnotation(type=OperationEnum.INSERT,module=ModuleEnum.AD_PUBLISH,subModule="",describe="发布广告")
	@RequestMapping(value="addAd.do",method=RequestMethod.POST)
	@ResponseBody
	public String addAd(@RequestParam(value="adTitle",required=true) String adTitle,@RequestParam(value="adContent",required=true) String adContent,HttpServletRequest req,HttpServletResponse resp) {
		
		// 接收人列表
		List<String> receiveList = new ArrayList<String>();
		boolean res = true;
		if (CommonUtil.validateEmpty(adTitle) && CommonUtil.validateEmpty(adContent)) {
			req.setAttribute("result", false);
			res = false;
		}else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String curTime = sdf.format(new Date());
			
			Map<String, ZhddWebSocket> socketMap = ZhddWebSocket.getClients();
			for (Entry<String,ZhddWebSocket> entry : socketMap.entrySet())
			{
				if (entry.getKey().equals("admin")) {
					continue;
				}
	
				try {
					ChatMessageBean chatBean = new ChatMessageBean(curTime,"4","广告消息","admin",entry.getKey(), "title:"+adTitle+";content:"+adContent);
					entry.getValue().getSession().getBasicRemote().sendText(chatBean.toString());
					receiveList.add(entry.getKey());
				} catch (IOException e) {
					res = false;
					e.printStackTrace();
				}
			}
			
			// 插入广告记录
			WsAds wa = new WsAds();
			wa.setTitle(adTitle);
			wa.setContent(adContent);
			wa.setReceiveList(receiveList.toString());
			wsService.insertAds(wa);
		}
		if (res) {
			return "success";
		}else {
			return "failed";
		}
	}
	
	/**
	 * 分页查询公告列表
	 * 
	 * true:存在  false:不存在
	 */
	@OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.AD_PUBLISH,subModule="",describe="显示广告列表")
	@RequestMapping(value="getAdsListByPage.json",method=RequestMethod.GET)
	@ResponseBody
	public Object getAdsListByPage(@RequestParam("curPage") int curPage, @RequestParam("numPerPage") int numPerPage)
	{
		int totalCount = wsService.queryAdsCount(new WsAds());
		int totalPage = 0;
		if (totalCount % numPerPage != 0)
		{
			totalPage = totalCount/numPerPage + 1;
		}
		else
		{
			totalPage = totalCount / numPerPage;
		}
		
		int start = 0;
		int limit = numPerPage;
		if (curPage == 1)
		{
			start = 0;
		}
		else
		{
			start = (curPage-1) * numPerPage;
		}
		List<WsAds> adslist = wsService.queryAdsByPage(start, limit);
		
		PageResponseEntity rqe = new PageResponseEntity();
		rqe.setTotalCount(totalCount);
		rqe.setTotalPage(totalPage);
		rqe.setList(adslist);
		return JsonUtil.javaobject2Jsonobject(rqe);
	}
	
	
	/**
	 * 分页查询用户信息
	 * 
	 * true:存在  false:不存在
	 */
	@OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.USER_MANAGE,subModule="",describe="查询用户列表")
	@RequestMapping(value="getOnlineUsersByPage.json",method=RequestMethod.POST,produces="application/json")
	@ResponseBody
	public Object getOnlineUsersByPage(@RequestBody WsUser params)
	{
		int totalCount = wsService.queryWsUserCount(params);
		int totalPage = 0;
		int numPerPage = params.getNumPerPage();
		int curPage = params.getCurPage();
		if (totalCount % numPerPage != 0){
			totalPage = totalCount/numPerPage + 1;
		}
		else{
			totalPage = totalCount / numPerPage;
		}
		
		int start = 0;
		int limit = numPerPage;
		if (curPage == 1){
			start = 0;
		}
		else{
			start = (curPage-1) * numPerPage;
		}
		params.setStart(start);
		params.setLimit(limit);
		List<WsUser> userlist = wsService.queryWsUserByPage(params);
		
		PageResponseEntity rqe = new PageResponseEntity();
		rqe.setTotalCount(totalCount);
		rqe.setTotalPage(totalPage);
		rqe.setList(userlist);
		rqe.setParameter1(ZhddWebSocket.getClients().size());
		
		return JsonUtil.javaobject2Jsonobject(rqe);
	}
	
	/**
	 * 获取用户聊天记录  分页查询
	 * 
	 * getChatLogByPage.json
	 */
	@OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.CHAT_HISTORY_MANAGE,subModule="",describe="用户聊天记录列表")
	@RequestMapping(value = "getChatLogByPage.json", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public Object getChatLogByPage(@RequestBody WsChatlog params)
	{
		return wsService.queryHistoryChatlogByPage(params);
	}
	
	/**
	 * 获取操作日志列表  分页查询
	 * 
	 * getOperationLogByPage.json
	 */
	@RequestMapping(value = "getOperationLogByPage.json", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public Object getOperationLogByPage(@RequestBody WsOperationLog params)
	{
		return wsService.queryOperationLogByPage(params);
	}
	
	/**
	 * 获取操作日志列表  分页查询
	 * 
	 * getOperationLogByPage.json
	 */
	@RequestMapping(value = "getOperationLogByPageByBootstrap.json", method = RequestMethod.POST)
	@ResponseBody
	public Object getOperationLogByPageByBootstrap(WsOperationLog params)
	{
		JsonResult jr = wsService.queryOperationLogByPageByBootStrap(params);
		return JsonUtil.javaobject2Jsonobject(jr);
	}
	
	
	/**
	 * 敏感词/脏话  分页查询
	 * 
	 * getChatLogByPage.json
	 */
	@OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.CONFIGURATION,subModule="",describe="常用配置列表")
	@RequestMapping(value = "getCommonByPage.json", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public Object getCommonByPage(@RequestBody WsCommon params)
	{
		return wsService.queryCommonByPage(params);
	}
	
	@OperationLogAnnotation(type=OperationEnum.INSERT,module=ModuleEnum.CONFIGURATION,subModule="",describe="添加配置")
	@RequestMapping(value = "addCommonItem.do", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public String addCommonItem(@RequestBody WsCommon params)
	{
		wsService.insertCommon(params);
		return "success";
	}
	
	@OperationLogAnnotation(type=OperationEnum.DELETE,module=ModuleEnum.CONFIGURATION,subModule="",describe="删除配置")
	@RequestMapping(value = "deleteCommonItem.do", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public String deleteCommonItem(@RequestBody WsCommon params)
	{
		wsService.deleteCommon(params);
		return "success";
	}
	
	@OperationLogAnnotation(type=OperationEnum.UPDATE,module=ModuleEnum.CONFIGURATION,subModule="",describe="更新配置")
	@RequestMapping(value = "updateCommonItem.do", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public String updateCommonItem(@RequestBody WsCommon params)
	{
		wsService.updateCommon(params);
		return "success";
	}
	
	/**
	 * 生成二维码并显示出来
	 *
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.INSERT,module=ModuleEnum.OTHER,subModule="",describe="显示二维码")
	@RequestMapping(value = "showQRCode.do", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public String showQRCode(HttpServletRequest request)
	{
		ServletContext sc = request.getServletContext();
		String contextPathx = sc.getContextPath();
		String scheme = request.getScheme();
		
		String serverIp = ServiceUtil.getWebsocketIp(configMap);
		//String serverPort = configMap.get("webserver.port");
		int serverPort = request.getServerPort();
		
		String savePath = request.getServletContext().getRealPath(CommonConstants.WEBINF_IMGES);
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
			String text = scheme + "://" + serverIp + ":" + serverPort + contextPathx+"/ws/login.page";
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
		return scheme + "://" + serverIp + ":" + serverPort + "/"+contextPathx+"/img/"+qrCodeFilename;
	}
	
	/**
	 * 导出用户信息
	 * 
	 * @param response
	 */
	@OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.USER_MANAGE,subModule="",describe="导出用户信息")
	@RequestMapping(value = "exportUser.do", method = RequestMethod.GET)
	public void exportUser(HttpServletResponse response){
		logger.debug("开始导出用户信息");
		List<WsUser> list = wsService.queryWsUser(new WsUser());
		if (null != list && list.size()>0) {
			for (WsUser wu : list) {
				String password = wu.getPassword();
				if (StringUtils.isNotEmpty(password)) {
					String passwordDecode = SecurityAESUtil.decryptAES(password, CommonConstants.AES_PASSWORD);
					wu.setPasswordDecode(passwordDecode);
				}
			}
			
			String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String fileName = "wsUser".concat("_").concat(time).concat(".xls");
			ExcelUtil.exportExcel(list, "用户信息", "用户", WsUser.class, fileName, response);
		}else {
			logger.debug("无需导出用户信息");
		}
		logger.debug("结束导出用户信息");
	}
	
	/**
	 * 导出操作日志信息
	 * 
	 * @param response
	 */
	@OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.OPERATION_LOG_MANAGE,subModule="",describe="导出操作日志")
	@RequestMapping(value = "exportOperateLog.do", method = RequestMethod.GET)
	public void exportOperateLog(String userName, String operModule, HttpServletResponse response){
		logger.debug("开始导出操作日志");
		WsOperationLog wol = new WsOperationLog();
		if (StringUtils.isNotEmpty(userName)) {
			wol.setUserName(userName);
		}
		if (StringUtils.isNotEmpty(operModule)) {
			wol.setOperModule(operModule);
		}
		
		List<WsOperationLog> list = wsService.queryOperationLogList(wol);
		if (null != list && list.size()>0) {
			for (WsOperationLog woltmp : list) {
				if (woltmp.getOperResult().length()>32000) {
					woltmp.setOperResult(woltmp.getOperResult().substring(0, 32000));
				}
			}
			
			String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String fileName = "wsOperationLog".concat("_").concat(time).concat(".xls");
			ExcelUtil.exportExcel(list, "操作日志", "操作日志", WsOperationLog.class, fileName, response);
		}else {
			logger.debug("无需导出操作日志");
		}
		logger.debug("结束导出操作日志");
	}
}
