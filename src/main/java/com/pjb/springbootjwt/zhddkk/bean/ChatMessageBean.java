package com.pjb.springbootjwt.zhddkk.bean;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * 发送给客户端的聊天数据.
 */
@Data
public class ChatMessageBean {
    // 时间
    private String curTime;
    // 消息类型id
    private String msgTypeId;
    // 消息类型
    private String msgTypeDesc;
    // 发送方
    private String from;
    // 接收方
    private String to;
    // 消息内容
    private String msg;
    // 扩展数据
    Map<String, Object> extendMap = new HashMap<>();

    /**
     * 构造方法.
     * @param curTime 当前时间
     * @param msgTypeId 消息类型
     * @param msgTypeDesc 类型描述
     * @param from 发起方
     * @param to 接收方
     * @param msg 消息
     */
    public ChatMessageBean(String curTime, String msgTypeId, String msgTypeDesc, String from, String to, String msg) {
        super();
        this.curTime = curTime;
        this.msgTypeId = msgTypeId;
        this.msgTypeDesc = msgTypeDesc;
        this.from = from;
        this.to = to;
        this.msg = msg;
    }

    /**
     * 构造方法.
     * @param curTime 当前时间
     * @param msgTypeId 消息类型
     * @param msgTypeDesc 类型描述
     * @param from 发起方
     * @param to 接收方
     * @param msg 消息
     * @param extendMap 扩展参数
     */
    public ChatMessageBean(String curTime, String msgTypeId, String msgTypeDesc, String from, String to, String msg, Map<String, Object> extendMap) {
        super();
        this.curTime = curTime;
        this.msgTypeId = msgTypeId;
        this.msgTypeDesc = msgTypeDesc;
        this.from = from;
        this.to = to;
        this.msg = msg;
        this.extendMap = extendMap;
    }
}