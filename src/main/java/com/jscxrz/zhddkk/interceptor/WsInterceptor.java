package com.jscxrz.zhddkk.interceptor;

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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jscxrz.zhddkk.constants.ServiceConstants;
import com.jscxrz.zhddkk.dao.WsDao;
import com.jscxrz.zhddkk.entity.WsDic;
import com.jscxrz.zhddkk.util.CommonUtil;

/**
 * 拦截器
 * 
 * 初始化数据用
 * 
 * @author Administrator
 *
 */
@Configuration
//@PropertySource("classpath:config.properties")
public class WsInterceptor extends HandlerInterceptorAdapter implements InitializingBean
{
	@Autowired
	private WsDao wsDao;
	
	//@Autowired
	//private Environment env;
	
	// 字典表
	public static List<WsDic> dicList = new ArrayList<WsDic>();
	
	public static Map<String,List<WsDic>> dicMap = new HashMap<String,List<WsDic>>();
	
	// config.properties中的记录
	public static Map<String, String> configMap = new HashMap<String, String>();
	
	public static List<WsDic> getDicList() {
		return dicList;
	}

	public static void setDicList(List<WsDic> dicList) {
		WsInterceptor.dicList = dicList;
	}

	public static Map<String, List<WsDic>> getDicMap() {
		return dicMap;
	}

	public static void setDicMap(Map<String, List<WsDic>> dicMap) {
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
		loadData();
	}
	
	public boolean preHandle(HttpServletRequest request)
	{
		System.out.println("-------------------preHandle------------------");
		return true;
	}
	
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
	{
		System.out.println("-------------------postHandle------------------");
		System.out.println("reload database data to memory!");
		//loadData();
	}
	
	public void afterCompletion(
			HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println("-------------------afterCompletion------------------");
	}
	
	private void loadData()
	{
		dicMap.clear();
		dicList.clear();
		configMap.clear();
		try
		{
			loadDicData();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("初始化表dic数据失败!" + e.getMessage());
		}

		
		try
		{
			loadConfigPropertiesData();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("初始化config.properties失败!" + e.getMessage());
		}
	}
	
	private void loadDicData()
	{	
		List<WsDic> dicListTmp = wsDao.queryDic(new WsDic());
		dicList.addAll(dicListTmp);

		for (WsDic dic : dicListTmp)
		{
			String type = dic.getType();
			String key = dic.getKey();
			String value = dic.getValue();
			
			if (CommonUtil.validateEmpty(type) || CommonUtil.validateEmpty(key)
					|| CommonUtil.validateEmpty(value))
			{
				continue;
			}

			if (dicMap.containsKey(type))
			{
				List<WsDic> tmpDicList = dicMap.get(type);
				tmpDicList.add(dic);
			}
			else
			{
				List<WsDic> tmpDicList = new ArrayList<WsDic>();
				tmpDicList.add(dic);
				dicMap.put(type, tmpDicList);
			}
		}
	}


	/**
	 * 加载config.properties中的配置项到内存中去
	 */
	private void loadConfigPropertiesData()
	{
		String configPath = null;
		try
		{
			configPath = this.getClass().getResource("../../../../config.properties").getPath();
		}
		catch (Exception e)
		{
			System.out.println("查找文件config.properties失败! 改从ServiceConstants中加载配置");
			
			loadDefaultConfigData();
			System.out.println("ServiceConstants配置:" + configMap);
			return;
		}
		
		if (null == configPath)
		{
			loadDefaultConfigData();
			return;
		}

		File f = new File(configPath);
		if (!f.exists())
		{
			System.out.println("config.properties不存在! 改从ServiceConstants中加载配置");
			
			loadDefaultConfigData();
			System.out.println("ServiceConstants配置:" + configMap);
			return;
		}
		FileReader fr = null;
		BufferedReader br = null;
		try 
		{

			fr = new FileReader(f);
			br = new BufferedReader(fr);
			String data = null;
			while ((data = br.readLine()) != null)
			{
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
				if (!configMap.containsKey(key))
				{
					configMap.put(key, value);
				}
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally{
			if (fr != null)
			{
				try
				{
					fr.close();
				}
				catch (Exception e1)
				{
					
				}
			}
			if (br != null)
			{
				try
				{
					br.close();
				}
				catch (Exception e2)
				{
					
				}
			}
		}
	}
	
	private void loadDefaultConfigData()
	{
		configMap.put("login.cmsUrl", ServiceConstants.LOGIN_URL);
		configMap.put("login.userName", ServiceConstants.LOGIN_USER);
		configMap.put("login.passwd", ServiceConstants.LOGIN_PASS);
		configMap.put("service.endpoint", ServiceConstants.END_POINT);
		configMap.put("service.default.sessionid", ServiceConstants.DEFAULT_SESSIONID);
		configMap.put("login.switch", String.valueOf(ServiceConstants.INIT_SWITCH));
	}
}