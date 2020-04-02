package com.pjb.springbootjwt.common.sms.constants;

/**
 * 短信常量
 *
 */
public class SmsStaticParam {
    // 设置超时时间-可自行调整
    public static final String DEFAULT_CONNECT_TIMEOUT  = "sun.net.client.defaultConnectTimeout";
    public static final String DEFAULT_READ_TIMEOUT = "sun.net.client.defaultReadTimeout";
    public static final String CONNECT_TIMEOUT = "10000";
    public static final String READ_TIMEOUT = "10000";
    // 初始化ascClient需要的几个参数
    public static final String PRODUCT = "Dysmsapi";// 短信API产品名称（短信产品名固定，无需修改）
    public static final String DOMAIN = "dysmsapi.aliyuncs.com";// 短信API产品域名（接口地址固定，无需修改）
    // 替换成你的AK (产品密)
    public static final String ACCESS_KEY_Id = "LTAI4FtHkHhCgtCBu4vLScWf";// 你的accessKeyId,填你自己的 上文配置所得  自行配置
    public static final String ACCESS_KEY_SECRET = "w15jwjTF7v7JposcO1Jw1fooZDZmnM";// 你的accessKeySecret,填你自己的 上文配置所得 自行配置
    // 必填:短信签名-可在短信控制台中找到
    public static final String SIGN_NAME = "智咖智慧园区";// 阿里云配置你自己的短信签名填入
    // 注册验证短信模板
    public static final String REGISTER_VERIFY_CODE = "SMS_173160030"; // 阿里云配置你自己的短信模板填
    //忘记密码短信模板
    public static final String FORGET_PASSWD_CODE = "SMS_173160029";
    //区域id
    public static final String REGINID = "cn-hangzhou";
    //终端名称
    public static final String ENDPOINT = "cn-hangzhou";

    //--------其他参数---------
    //被禁用的手机号时间(小时)
    public static final Integer FORBIDDEN_HOUR = 2;
    //短信时间发送频率(秒) 即每60秒才可发送一条短信
    public static final Integer SEND_FERQUENCY = 60;
    //短信有效时间(秒)
    public static final Integer VALID_ALIVE_TIME = 300;
    //同一个手机号每天最多发送验证码数
    public static final Integer DAY_SEND_COUNT = 3;
    //同一个手机号每月最多发送验证码数
    public static final Integer MONTH_SEND_COUNT = 25;
    //注册或忘记密码时最大失败尝试次数,超过阈值后会被禁用2小时
    public static final Integer MAX_ERR_TRY_COUNT = 5;

    //注册用的验证码
    public static final String SMS_TYPE_REGISTER = "1";
    //忘记密码时用的验证码
    public static final String SMS_TYPE_FORGET_PASSWD = "2";
}
