package com.pjb.springbootjwt.zhddkk.constants;

/**
 * websocket聊天用的消息枚举类型.
 */
public enum ChatMsgTypeEnum {

    SYSTEM_MSG("系统消息", "1" ),
    CHAT_ONLINE_MSG("在线消息", "2"),
    CHAT_OFFLINE_MSG("离线消息", "3"),
    NOTICE_MSG("通知消息", "4"),
    STATUS_MSG("状态消息", "5");

    private String desc;
    private String typeId;

    ChatMsgTypeEnum(String desc, String typeId) {
        this.typeId = typeId;
        this.desc = desc;
    }

    public static String getDesc(String typeId) {
        for (ChatMsgTypeEnum c : ChatMsgTypeEnum.values()) {
            if (c.getTypeId().equals(typeId)) {
                return c.desc;
            }
        }
        return "";
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}