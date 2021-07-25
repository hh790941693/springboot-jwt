package com.pjb.springbootjwt.zhddkk.constants;

/**
 * 常量.
 */
public class CommonConstants {
    public static final String UPLOAD_PATH_SUFFIX = "/WEB-INF/upload";
    public static final String UPLOAD_TEMP_SUFFIX = "/WEB-INF/temp";

    //session信息
    public static final String SESSION_INFO = "sessionInfo";

    //cookie中存储的KV键值对
    public static final String C_USER_ID = "userId";
    public static final String C_USER = "username";
    public static final String C_PASS = "pass";
    public static final String C_WEBSERVERIP = "webserverIp";
    public static final String C_WEBSERVERPORT = "webserverPort";
    public static final String C_IMG = "selfImg";           //个人头像
    public static final String C_USER_AGENT = "userAgent";  // 设备信息

    // cookie中存储的KV键值对
    public static final String C_LANG = "lang";  // 语言

    // 登陆验证码
    public static final String VERIFY_CODE = "verifyCode";

    //cookie过期时间(单位:秒) 30分钟
    public static final int COOKIE_TIMEOUT = 1800;

    //session不活动时的超时时间(单位:秒)  30分钟
    public static final int SESSION_INACTIVE_TIMEOUT = 1800;

    //加密秘钥
    public static final String AES_PASSWORD = "hch";

    // admin管理员账号
    public static final String ADMIN_USER = "admin";

    // cookie(locale)的过期时间(20年)
    public static final int LOCALE_COOKIE_EXPIRE = 20 * 365 * 24 * 60 * 60;

    //session过期相关配置
    public static final String SESSION_TIMEOUT_CODE = "-255";
    public static final String SESSION_TIMEOUT_REDIRECT_URL = "/";

    // 用户单日最大文件上传容量(KB) 默认50M
    public static final Long DAY_MAX_UPLOAD_FILE_SIZE = 50 * 1024 * 1024l;
}