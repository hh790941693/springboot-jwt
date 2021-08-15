package com.pjb.springbootjwt.zhddkk.constants;

/**
 * websocket聊天用的消息枚举类型.
 */
public enum ChatMsgTypeEnum {

    SYSTEM_MSG("系统消息", "1" ),
    CHAT_ONLINE_MSG("在线消息", "2"),
    CHAT_OFFLINE_MSG("离线消息", "3"),
    NOTICE_MSG("通知消息", "4"),
    STATUS_MSG("状态消息", "5"),
    ERROR_MSG("异常消息", "6");

    private String msgTypeDesc;
    private String msgTypeId;

    ChatMsgTypeEnum(String msgTypeDesc, String msgTypeId) {
        this.msgTypeId = msgTypeId;
        this.msgTypeDesc = msgTypeDesc;
    }

    public static String getMsgTypeDesc(String msgTypeId) {
        for (ChatMsgTypeEnum c : ChatMsgTypeEnum.values()) {
            if (c.getMsgTypeId().equals(msgTypeId)) {
                return c.msgTypeDesc;
            }
        }
        return "";
    }

    public String getMsgTypeDesc() {
        return msgTypeDesc;
    }

    public void setMsgTypeDesc(String msgTypeDesc) {
        this.msgTypeDesc = msgTypeDesc;
    }

    public String getMsgTypeId() {
        return msgTypeId;
    }

    public void setMsgTypeId(String msgTypeId) {
        this.msgTypeId = msgTypeId;
    }
}