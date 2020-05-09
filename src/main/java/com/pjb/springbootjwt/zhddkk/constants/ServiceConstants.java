package com.pjb.springbootjwt.zhddkk.constants;

public interface ServiceConstants 
{
	// 第三方服务地址
	public static final String END_POINT = "http://172.27.151.50:8085/bms/services/ThirdBayonetService.ThirdBayonetServiceHttpSoap11Endpoint/";

	// 是否开启初始化接口连接  false:否  true:是 
	public static final boolean INIT_SWITCH = false;
	
	// 默认的sessionID  如果INIT_SWITCH为false时此配置项生效
	public static final String DEFAULT_SESSIONID = "0471cbabd8d7";
	
	// 初始化连接的服务IP地址
	public static final String LOGIN_URL = "172.27.151.50";
	
	// 初始化连接的服务用户名
	public static final String LOGIN_USER = "shaoyilan";
	
	// 初始化连接的服务用户密码
	public static final String LOGIN_PASS = "hik@12345";
	
	// 接口返回码  0表示正常
	public static final String NORMAL = "0";
	
	// 用来表示http地址中的&符号
	public static final String XML_SYMBOL_AMP = "XML_SYMBOL_AMP";
}
