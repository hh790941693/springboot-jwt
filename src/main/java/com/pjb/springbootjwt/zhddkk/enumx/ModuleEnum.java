package com.pjb.springbootjwt.zhddkk.enumx;

public enum ModuleEnum {
	
	//-------------------客户端-------------------------------
	LOGIN("登录"),
	LOGOUT("退出"),
	REGISTER("注册"),
	FORGET_PASSWORD("忘记密码"),
	CHAT("聊天"),
	MUSIC("音乐播放器"),
	GAME("游戏娱乐"),
	FRIENDS("好友"),
	APPLY("申请记录"),
	CIRCLE("朋友圈"),
	SETTING("设置"),
	SIGN("签到"),
    FEED_BACK("反馈建议"),
	OTHER("其他"),
	
	//-------------------服务端------------------------------
	USER_MANAGE("用户管理"),
	CHAT_HISTORY_MANAGE("聊天记录管理"),
	OPERATION_LOG_MANAGE("操作日志记录管理"),
	CHAT_MONITOR("聊天监控"),
	CONFIGURATION("配置"),
	AD_PUBLISH("广告发布"),
	TWOD_GAME("2D画图");

	String value;
	ModuleEnum(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
