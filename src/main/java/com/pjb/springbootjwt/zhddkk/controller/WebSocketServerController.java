package com.pjb.springbootjwt.zhddkk.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.common.uploader.config.UploadConfig;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.bean.ChatMessageBean;
import com.pjb.springbootjwt.zhddkk.bean.JsonResult;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;

import com.pjb.springbootjwt.zhddkk.domain.*;
import com.pjb.springbootjwt.zhddkk.entity.PageResponseEntity;
import com.pjb.springbootjwt.zhddkk.enumx.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.enumx.OperationEnum;
import com.pjb.springbootjwt.zhddkk.interceptor.WsInterceptor;
import com.pjb.springbootjwt.zhddkk.service.*;
import com.pjb.springbootjwt.zhddkk.util.*;
import com.pjb.springbootjwt.zhddkk.websocket.ZhddWebSocket;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
	private static final Logger logger = LoggerFactory.getLogger(WebSocketServerController.class);
	
	private static Map<String,String> configMap = WsInterceptor.getConfigMap();

	@Autowired
	private WsOperLogService wsOperLogService;

	@Value("${server.address}")
	private String serverAddress;

	@Value("${server.port}")
	private String serverPort;

	@Autowired
	private UploadConfig uploadConfig;

	@Autowired
	private WsUsersService wsUsersService;

	@Autowired
	private WsAdsService wsAdsService;

	@Autowired
	private WsCommonService wsCommonService;

	@Autowired
	private WsOperationLogService wsOperationLogService;

	@Autowired
	private WsChatlogService wsChatlogService;

	@Autowired
	private WsSignService wsSignService;

	@Autowired
	private WsFileService wsFileService;

	/**
	 * 让某用户下线
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "offlineUser.do")
	@ResponseBody
	public String offlineUser(@RequestParam("user") String user)
	{
		Map<String, ZhddWebSocket> socketMap = ZhddWebSocket.getClients();
		for (Entry<String,ZhddWebSocket> entry : socketMap.entrySet()) {
			if (entry.getKey().equals(user)) {
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
		
		WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
		if (null != wsUsersDO) {
			wsUsersDO.setState("0");
			wsUsersService.updateById(wsUsersDO);
			return "success";
		}
		return "failed";
	}
	
	/**
	 * 让某用户禁用/启用
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "enableUser.do")
	@ResponseBody
	public String enableUser(@RequestParam("user") String user,@RequestParam("enable") String enable)
	{
		WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
		if (null != wsUsersDO) {
			wsUsersDO.setEnable(enable);
			wsUsersService.updateById(wsUsersDO);
			return "success";
		}
		return "failed";
	}
	
	/**
	 * 让某用户禁言/开言
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "enableSpeak.do")
	@ResponseBody
	public String enableSpeak(@RequestParam("user") String user,@RequestParam("speak") String speak)
	{
		WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
		if (null != wsUsersDO) {
			wsUsersDO.setSpeak(speak);
			wsUsersService.updateById(wsUsersDO);
			return "success";
		}
		return "failed";
	}
	
	/**
	 * 给指定用户的发送信息
	 * @param user
	 * @return
	 */
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
	@RequestMapping(value = "wsserverChartLog.page", method = RequestMethod.GET)
	public String chatLogManage(Model model)
	{
		logger.debug("访问wsserverChartLog.page");
		List<WsUsersDO> userList = wsUsersService.selectList(null);
		List<String> list = new ArrayList<String>();
		for (WsUsersDO wu : userList) {
			list.add(wu.getName());
		}
		model.addAttribute("users", JSONArray.fromObject(list));
		return "ws/wsserverChartLog";
	}
	
	/**
	 * 操作日志管理页面
	 * 
	 */
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
		
		List<WsUsersDO> userList = wsUsersService.selectList(null);
		List<String> list = new ArrayList<String>();
		for (WsUsersDO wu : userList){
			list.add(wu.getName());
		}
		model.addAttribute("users", JSONArray.fromObject(list));
		return "ws/wsserverOperationLog";
	}
	
	/**
	 * 操作日志管理页面
	 * 
	 */
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

		List<WsUsersDO> userList = wsUsersService.selectList(null);
		List<String> list = new ArrayList<String>();
		for (WsUsersDO wu : userList){
			list.add(wu.getName());
		}
		model.addAttribute("users", JSONArray.fromObject(list));
		return "ws/wsserverOperationLogByBootstrap";
	}
	
	/**
	 * 聊天记录监控页面
	 * 
	 */
	@RequestMapping(value = "wsserverChartMonitor.page", method = RequestMethod.GET)
	public String chatLogMonitor(Model model,HttpServletRequest request)
	{
		logger.debug("访问wsserverChartMonitor.page");
		model.addAttribute("webserverip", configMap.get("webserver.ip"));
		int serverPort = request.getServerPort();
		model.addAttribute("webserverport", serverPort);
		return "ws/wsserverChartMonitor";
	}
	
	
	/**
	 * 常用字典项配置页面
	 * 
	 */
	@RequestMapping(value = "wsserverCommon.page", method = RequestMethod.GET)
	public String commonManager(Model model,@RequestParam("type") String type)
	{
		logger.debug("访问wsserverCommon.page,type="+type);
		model.addAttribute("type", type);
		String title="";
		if (type.equals("mgc")) {
			title="敏感词";
		}else if (type.equals("zh")){
			title="脏话";
		}else if (type.equals("cyy")){
			title="常用语";
		}else if (type.equals("zcwt")){
			title="注册问题";
		}
		model.addAttribute("title", title);
		return "ws/wsserverCommon";
	}
	
	/**
	 * 广告首页
	 * 
	 */
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
			for (Entry<String,ZhddWebSocket> entry : socketMap.entrySet()) {
				if (entry.getKey().equals("admin")) {
					continue;
				}
	
				try {
					ChatMessageBean chatBean = new ChatMessageBean(curTime,"4","广告消息","admin",entry.getKey(), "title:"+adTitle+";content:"+adContent);
					entry.getValue().getSession().getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
					receiveList.add(entry.getKey());
				} catch (IOException e) {
					res = false;
					e.printStackTrace();
				}
			}
			
			// 插入广告记录
			WsAdsDO wsAdsDO = new WsAdsDO();
			wsAdsDO.setTitle(adTitle);
			wsAdsDO.setContent(adContent);
			wsAdsDO.setReceiveList(receiveList.toString());
			res = wsAdsService.insert(wsAdsDO);
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
	@RequestMapping(value="getAdsListByPage.json",method=RequestMethod.GET)
	@ResponseBody
	public Object getAdsListByPage(@RequestParam("curPage") int curPage, @RequestParam("numPerPage") int numPerPage)
	{
		int totalCount = wsAdsService.selectCount(null);
		int totalPage = 0;
		if (totalCount % numPerPage != 0) {
			totalPage = totalCount/numPerPage + 1;
		}else{
			totalPage = totalCount / numPerPage;
		}
		
		int start = 0;
		int limit = numPerPage;
		if (curPage == 1){
			start = 0;
		}else{
			start = (curPage-1) * numPerPage;
		}
		List<WsAdsDO> adslist = wsAdsService.selectPage(new Page<>(start, limit)).getRecords();
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
	@RequestMapping(value="getOnlineUsersByPage.json",method=RequestMethod.POST,produces="application/json")
	@ResponseBody
	public Object getOnlineUsersByPage(@RequestBody WsUsersDO params) {
		int totalCount = wsUsersService.selectCount(null);
		int totalPage = 0;
		int numPerPage = 15;
		int curPage = 1;
		if (totalCount % numPerPage != 0){
			totalPage = totalCount/numPerPage + 1;
		}else{
			totalPage = totalCount / numPerPage;
		}
		
		int start = 0;
		int limit = numPerPage;
		if (curPage == 1){
			start = 0;
		}else{
			start = (curPage-1) * numPerPage;
		}
		List<WsUsersDO> userList = wsUsersService.selectPage(new Page<>(start, limit)).getRecords();
		PageResponseEntity rqe = new PageResponseEntity();
		rqe.setTotalCount(totalCount);
		rqe.setTotalPage(totalPage);
		rqe.setList(userList);
		rqe.setParameter1(ZhddWebSocket.getClients().size());
		
		return JsonUtil.javaobject2Jsonobject(rqe);
	}
	
	/**
	 * 获取用户聊天记录  分页查询
	 * 
	 * getChatLogByPage.json
	 */
	@RequestMapping(value = "getChatLogByPage.json", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public Object getChatLogByPage(@RequestBody WsChatlogDO params)
	{
		Page<WsChatlogDO> page = wsChatlogService.selectPage(new Page<>(1,15), null);
		int totalCount = wsChatlogService.selectCount(null);
		PageResponseEntity rqe = new PageResponseEntity();
		rqe.setTotalCount(totalCount);
		rqe.setTotalPage(10);
		rqe.setList(page.getRecords());

		return rqe;
	}
	
	/**
	 * 获取操作日志列表  分页查询
	 * 
	 * getOperationLogByPage.json
	 */
	@RequestMapping(value = "getOperationLogByPage.json", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public Object getOperationLogByPage(@RequestBody WsOperationLogDO params)
	{
		Page<WsOperationLogDO> page = wsOperationLogService.selectPage(new Page<>(1, 15), null);
		int totalCount = wsOperationLogService.selectCount(null);
		PageResponseEntity rqe = new PageResponseEntity();
		rqe.setTotalCount(totalCount);
		rqe.setTotalPage(10);
		rqe.setList(page.getRecords());

		return rqe;
	}
	
	/**
	 * 获取操作日志列表  分页查询
	 * 
	 * getOperationLogByPage.json
	 */
	@RequestMapping(value = "getOperationLogByPageByBootstrap.json", method = RequestMethod.POST)
	@ResponseBody
	public Object getOperationLogByPageByBootstrap(WsOperationLogDO params)
	{
		Page<WsOperationLogDO> page = wsOperationLogService.selectPage(new Page<>(1, 15), null);
		int totalCount = wsOperationLogService.selectCount(null);
		JsonResult jr = new JsonResult();
		jr.setTotal(totalCount);
		jr.setRows(page.getRecords());
		return JsonUtil.javaobject2Jsonobject(jr);
	}


	/**
	 * 清空操作日志
	 * @return
	 */
	@PostMapping("clearOperationLog.do")
	@ResponseBody
	public String clearOperationLog(){
		wsOperLogService.delete(null);
		return "success";
	}

	/**
	 * 敏感词/脏话  分页查询
	 * 
	 * getChatLogByPage.json
	 */
	@RequestMapping(value = "getCommonByPage.json", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public Object getCommonByPage(@RequestBody WsCommonDO params)
	{
		Page<WsCommonDO> page = wsCommonService.selectPage(new Page<WsCommonDO>(1,15), new EntityWrapper<WsCommonDO>().eq("type", params.getType()));
		List<WsCommonDO> wsCommonList = new ArrayList<>();
		if (null != page){
			wsCommonList = page.getRecords();
		}
		return wsCommonList;
	}

	@RequestMapping(value = "addCommonItem.do", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public String addCommonItem(@RequestBody WsCommonDO params)
	{
		wsCommonService.insert(params);
		return "success";
	}

	@RequestMapping(value = "deleteCommonItem.do", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public String deleteCommonItem(@RequestBody WsCommonDO params)
	{
		wsCommonService.deleteById(params.getId());
		return "success";
	}

	@RequestMapping(value = "updateCommonItem.do", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public String updateCommonItem(@RequestBody WsCommonDO params)
	{
		wsCommonService.updateById(params);
		return "success";
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
	 * 导出用户信息
	 * 
	 * @param response
	 */
	@RequestMapping(value = "exportUser.do", method = RequestMethod.GET)
	public void exportUser(HttpServletResponse response){
		logger.debug("开始导出用户信息");
		List<WsUsersDO> list = wsUsersService.selectList(null);
		if (null != list && list.size()>0) {
			for (WsUsersDO wu : list) {
				String password = wu.getPassword();
				if (StringUtils.isNotEmpty(password)) {
					String passwordDecode = SecurityAESUtil.decryptAES(password, CommonConstants.AES_PASSWORD);
					wu.setPasswordDecode(passwordDecode);
				}
			}
			
			String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String fileName = "wsUser".concat("_").concat(time).concat(".xls");
			ExcelUtil.exportExcel(list, "用户信息", "用户", WsUsersDO.class, fileName, response);
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
	@RequestMapping(value = "exportOperateLog.do", method = RequestMethod.GET)
	public void exportOperateLog(String userName, String operModule, HttpServletResponse response){
		logger.debug("开始导出操作日志");
		Wrapper<WsOperationLogDO> wrapper = new EntityWrapper<>();
		if (StringUtils.isNotEmpty(userName)) {
			wrapper.eq("user_name", userName);
		}
		if (StringUtils.isNotEmpty(operModule)) {
			wrapper.eq("oper_module", operModule);
		}

		List<WsOperationLogDO> list = wsOperationLogService.selectList(wrapper);
		if (null != list && list.size()>0) {
			for (WsOperationLogDO woltmp : list) {
				if (woltmp.getOperResult().length()>32000) {
					woltmp.setOperResult(woltmp.getOperResult().substring(0, 32000));
				}
			}
			
			String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String fileName = "wsOperationLog".concat("_").concat(time).concat(".xls");
			ExcelUtil.exportExcel(list, "操作日志", "操作日志", WsOperationLogDO.class, fileName, response);
		}else {
			logger.debug("无需导出操作日志");
		}
		logger.debug("结束导出操作日志");
	}

	/**
	 * 领导驾驶舱
	 * @return
	 */
	@RequestMapping("/wsserverChart.page")
	public String wsserverChat(){
		return "ws/wsserverChart";
	}

    /**
     * 每日签到人数
     * @return
     */
	@RequestMapping("/querySignData.do")
    @ResponseBody
	public Map<String, Integer> querySignData(){
	    Map<String, Integer> map = new LinkedHashMap<>();
	    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        for (int i=6;i>=0;i--){
            Date date = DateUtil.getBeforeByDayTime(new Date(), -i);
            Date dayBeginDate = DateUtil.dayBeginDate(date);
            Date dayEndDate = DateUtil.dayEndDate(date);
            String timeName = sdf.format(date);

            int count = wsSignService.selectCount(new EntityWrapper<WsSignDO>().ge("create_time", dayBeginDate)
                .le("create_time", dayEndDate));
            map.put(timeName, count);
        }

	    return map;
    }

    /**
     * 每日在线人数
     * @return
     */
    @RequestMapping("/queryOnlineUserData.do")
    @ResponseBody
    public Map<String, Integer> queryOnlineUserData(){
        Map<String, Integer> map = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        SimpleDateFormat sdfx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i=30;i>=0;i--){
            Date date = DateUtil.getBeforeByDayTime(new Date(), -i);
            Date dayBeginDate = DateUtil.dayBeginDate(date);
            Date dayEndDate = DateUtil.dayEndDate(date);
            String timeName = sdf.format(date);

            int count = wsChatlogService.selectCount(new EntityWrapper<WsChatlogDO>()
                    .eq("to_user", "")
                    .ge("time", sdfx.format(dayBeginDate))
                    .le("time", sdfx.format(dayEndDate)));
            map.put(timeName, count);
        }

        return map;
    }

    /**
     * 每日文件上传大小
     * @return
     */
    @RequestMapping("/queryUploadFileData.do")
    @ResponseBody
    public Map<String, Long> queryUploadFileData(){
        Map<String, Long> map = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        SimpleDateFormat sdfx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i=6;i>=0;i--){
            Date date = DateUtil.getBeforeByDayTime(new Date(), -i);
            Date dayBeginDate = DateUtil.dayBeginDate(date);
            Date dayEndDate = DateUtil.dayEndDate(date);
            String timeName = sdf.format(date);

            List<WsFileDO> wsFileList = wsFileService.selectList(new EntityWrapper<WsFileDO>()
                    .ge("create_time", dayBeginDate)
                    .le("create_time", dayEndDate));
            long fileSize = 0;
            for (WsFileDO wsFileDO : wsFileList){
                fileSize += wsFileDO.getFileSize();
            }
            map.put(timeName, fileSize);
        }

        return map;
    }
}
