package com.pjb.springbootjwt.zhddkk.bean;

public class ChatMessageBean {
	private String curTime;
	private String typeId;
	private String typeDesc;
	private String from;
	private String to;
	private String msg;
	
	public ChatMessageBean(String curTime, String typeId, String typeDesc, String from, String to, String msg) {
		super();
		this.curTime = curTime;
		this.typeId = typeId;
		this.typeDesc = typeDesc;
		this.from = from;
		this.to = to;
		this.msg = msg;
	}
	public String getCurTime() {
		return curTime;
	}
	public void setCurTime(String curTime) {
		this.curTime = curTime;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("curTime:"+getCurTime()).append(";");
		sb.append("typeId:"+getTypeId()).append(";");
		sb.append("typeDesc:"+getTypeDesc()).append(";");
		sb.append("from:"+getFrom()).append(";");
		sb.append("to:"+getTo()).append(";");
		sb.append("msg:"+getMsg());
		
		return sb.toString();
	}
}
