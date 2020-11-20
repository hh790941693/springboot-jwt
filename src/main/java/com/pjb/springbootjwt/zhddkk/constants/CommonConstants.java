package com.pjb.springbootjwt.zhddkk.constants;

public class CommonConstants 
{
	public static final String SESSION_USER = "USER";
	public static final String UPLOAD_PATH_SUFFIX = "/WEB-INF/upload";
	public static final String UPLOAD_TEMP_SUFFIX = "/WEB-INF/temp";

	// session信息
	public static final String SESSION_INFO = "sessionInfo";
	// cookie中存储session的id名称
	public static final String JSESSIONID = "JSESSIONID";

	// session中存储的KV键值对
	public static final String S_USER = "user";
	public static final String S_PASS = "pass";
	public static final String S_WEBSERVERIP = "webserverip";
	public static final String S_WEBSERVERPORT = "webserverport";
	public static final String S_IMG = "selfimg";           //个人头像
	public static final String S_USER_AGENT = "useragent"; //设备信息

	//cookie过期时间(单位:秒) 30分钟
	public static final int COOKIE_TIMEOUT = 1800;

	//session不活动时的超时时间(单位:秒)  10分钟
	public static final int SESSION_INACTIVE_TIMEOUT = 600;

	// 请求结果常量
	public static final String SUCCESS = "success";
	public static final String FAIL = "failed";
	
	//加密秘钥
	public static final String AES_PASSWORD = "hch";

	// admin管理员账号密码
	public static final String ADMIN_USER = "admin";
	public static final String ADMIN_PASSWORD = "admin";
}
