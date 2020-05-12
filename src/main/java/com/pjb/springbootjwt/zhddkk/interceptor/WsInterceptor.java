package com.pjb.springbootjwt.zhddkk.interceptor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pjb.springbootjwt.zhddkk.constants.ServiceConstants;
import com.pjb.springbootjwt.zhddkk.domain.WsDicDO;
import com.pjb.springbootjwt.zhddkk.service.WsDicService;
import com.pjb.springbootjwt.zhddkk.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 拦截器
 * 
 * 初始化数据用
 * 
 * @author Administrator
 *
 */
@Configuration
public class WsInterceptor extends HandlerInterceptorAdapter implements InitializingBean {

	private Logger logger = LoggerFactory.getLogger(WsInterceptor.class);

	@Autowired
	private WsDicService wsDicService;

	// 字典表
	public static List<WsDicDO> dicList = new ArrayList<WsDicDO>();
	
	public static Map<String,List<WsDicDO>> dicMap = new HashMap<String,List<WsDicDO>>();
	
	// config.properties中的记录
	public static Map<String, String> configMap = new HashMap<String, String>();
	
	public static List<WsDicDO> getDicList() {
		return dicList;
	}

	public static void setDicList(List<WsDicDO> dicList) {
		WsInterceptor.dicList = dicList;
	}

	public static Map<String, List<WsDicDO>> getDicMap() {
		return dicMap;
	}

	public static void setDicMap(Map<String, List<WsDicDO>> dicMap) {
		WsInterceptor.dicMap = dicMap;
	}

	public static Map<String, String> getConfigMap() {
		return configMap;
	}

	public static void setConfigMap(Map<String, String> configMap) {
		WsInterceptor.configMap = configMap;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("WsInterceptor call afterPropertiesSet()");
		loadData();
	}
	
	public boolean preHandle(HttpServletRequest request) {
		System.out.println("-------------------preHandle------------------");
		logger.info("WsInterceptor call preHandle()");
		return true;
	}
	
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
		System.out.println("-------------------postHandle------------------");
		logger.info("WsInterceptor call postHandle()");
	}
	
	public void afterCompletion(
			HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println("-------------------afterCompletion------------------");
		logger.info("WsInterceptor call afterCompletion()");
	}
	
	private void loadData() {
		dicMap.clear();
		dicList.clear();
		configMap.clear();
		try {
			loadDicData();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("初始化表dic数据失败!" + e.getMessage());
		}

		try {
			loadConfigPropertiesData();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("初始化config.properties失败!" + e.getMessage());
		}
	}
	
	private void loadDicData() {
		logger.info("call loadDicData");
		List<WsDicDO> dicListTmp = wsDicService.selectList(null);
		dicList.addAll(dicListTmp);
		for (WsDicDO dic : dicListTmp) {
			String type = dic.getType();
			String key = dic.getKey();
			String value = dic.getValue();
			
			if (CommonUtil.validateEmpty(type) || CommonUtil.validateEmpty(key)
					|| CommonUtil.validateEmpty(value)) {
				continue;
			}

			if (dicMap.containsKey(type)) {
				List<WsDicDO> tmpDicList = dicMap.get(type);
				tmpDicList.add(dic);
			} else {
				List<WsDicDO> tmpDicList = new ArrayList<WsDicDO>();
				tmpDicList.add(dic);
				dicMap.put(type, tmpDicList);
			}
		}
	}


	/**
	 * 加载config.properties中的配置项到内存中去
	 */
	private void loadConfigPropertiesData() {
		logger.info("call loadConfigPropertiesData");
		String configPath = null;
		try {
			configPath = this.getClass().getResource("../../../../config.properties").getPath();
		}catch (Exception e) {
			System.out.println("查找文件config.properties失败! 改从ServiceConstants中加载配置");
			
			loadDefaultConfigData();
			System.out.println("ServiceConstants配置:" + configMap);
			return;
		}
		
		if (null == configPath) {
			loadDefaultConfigData();
			return;
		}

		File f = new File(configPath);
		if (!f.exists()) {
			System.out.println("config.properties不存在! 改从ServiceConstants中加载配置");
			
			loadDefaultConfigData();
			System.out.println("ServiceConstants配置:" + configMap);
			return;
		}
		FileReader fr = null;
		BufferedReader br = null;
		try {

			fr = new FileReader(f);
			br = new BufferedReader(fr);
			String data = null;
			while ((data = br.readLine()) != null) {
				if (data.startsWith("#")){
					continue;
				}
				if (data.trim().equals("")){
					continue;
				}
				String key = data.split("=")[0].trim();
				String value = "";
				if (data.split("=").length >1) {
					value = data.split("=")[1].trim();
				}
				if (!configMap.containsKey(key)) {
					configMap.put(key, value);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if (fr != null) {
				try {
					fr.close();
				} catch (Exception e1) {
					
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (Exception e2) {
					
				}
			}
		}
	}
	
	private void loadDefaultConfigData() {
//		configMap.put("login.cmsUrl", ServiceConstants.LOGIN_URL);
//		configMap.put("login.userName", ServiceConstants.LOGIN_USER);
//		configMap.put("login.passwd", ServiceConstants.LOGIN_PASS);
//		configMap.put("service.endpoint", ServiceConstants.END_POINT);
//		configMap.put("service.default.sessionid", ServiceConstants.DEFAULT_SESSIONID);
//		configMap.put("login.switch", String.valueOf(ServiceConstants.INIT_SWITCH));
		configMap.put("version", "1.0");
	}
}