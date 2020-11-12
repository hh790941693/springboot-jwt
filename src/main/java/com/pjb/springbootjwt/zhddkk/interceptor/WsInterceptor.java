package com.pjb.springbootjwt.zhddkk.interceptor;

import java.io.*;
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
import com.pjb.springbootjwt.zhddkk.util.UnicodeUtil;
import org.apache.commons.lang3.StringUtils;
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

	public static Map<String, String> chatMappingMap = new HashMap<>();
	
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

	public static Map<String, String> getChatMappingMap() {
		return chatMappingMap;
	}

	public static void setChatMappingMap(Map<String, String> chatMappingMap) {
		WsInterceptor.chatMappingMap = chatMappingMap;
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
		chatMappingMap.clear();
		try {
			loadDicData();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("初始化表dic数据失败!" + e.getMessage());
		}

		try {
			loadConfigPropertiesData("config.properties", configMap);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("初始化config.properties失败!" + e.getMessage());
		}

		try {
			loadConfigPropertiesData("chatmapping.properties", chatMappingMap);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("初始化chatmapping.properties失败!" + e.getMessage());
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
	 * 加载指定的配置文件内容到内存中去
	 */
	private void loadConfigPropertiesData(String filename, Map<String, String> map) {
		if (StringUtils.isBlank(filename)){
			return;
		}
		logger.info("call load data from:"+filename);

		InputStream stream = null;
		InputStreamReader reader = null;
		BufferedReader br = null;
		try {
			stream = getClass().getClassLoader().getResourceAsStream(filename);
			reader = new InputStreamReader(stream,"UTF-8");
			br = new BufferedReader(reader);
			String data = null;
			while ((data = br.readLine()) != null) {
				if (data.startsWith("#")) {
					continue;
				}
				if (data.trim().equals("")) {
					continue;
				}
				String key = data.split("=")[0].trim();
				key = UnicodeUtil.unicode2String(key);
				String value = "";
				if (data.split("=").length > 1) {
					value = data.split("=")[1].trim();
					value = UnicodeUtil.unicode2String(value);
				}
				if (!map.containsKey(key)) {
					map.put(key, value);
				}
			}
		} catch (Exception e) {
			logger.info("查找文件"+filename+"失败! 改从ServiceConstants中加载配置");
			System.out.println("查找文件"+filename+"失败! 改从ServiceConstants中加载配置");

			loadDefaultConfigData();
			return;
		}finally{
			if (br != null) {
				try {
					br.close();
				} catch (Exception e2) {
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e1) {
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e3) {
				}
			}
		}
		System.out.println(filename+"配置:" + map);
	}

	private void loadDefaultConfigData() {
		// 版本号
		configMap.put("author", "huangchaohui");
		configMap.put("contact", "547495788@qq.com");
		configMap.put("address", "Flower Gaden#5,Freedom street,Da Ye,Huang Shi,Wu Han,Hu Bei province");
		configMap.put("copyRight", "All Rights reserved@2018-2035");
		configMap.put("version", "1.0-Snapshot");
	}
}