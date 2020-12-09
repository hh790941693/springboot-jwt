package com.pjb.springbootjwt.zhddkk.bean;

import lombok.Data;

@Data
public class ChatMessageBean {
    private String curTime;
    private String typeId;
    private String typeDesc;
    private String from;
    private String to;
    private String msg;

    /**
     * 构造方法.
     * @param curTime 当前时间
     * @param typeId 消息类型
     * @param typeDesc 类型描述
     * @param from 发起方
     * @param to 接收方
     * @param msg 消息
     */
    public ChatMessageBean(String curTime, String typeId, String typeDesc, String from, String to, String msg) {
        super();
        this.curTime = curTime;
        this.typeId = typeId;
        this.typeDesc = typeDesc;
        this.from = from;
        this.to = to;
        this.msg = msg;
    }
}