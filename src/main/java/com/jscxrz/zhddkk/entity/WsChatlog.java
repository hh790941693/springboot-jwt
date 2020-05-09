package com.jscxrz.zhddkk.entity;


public class WsChatlog extends Page
{
	private String time;
	private String user;
	private String toUser;
	//private String fromUser;
	private String msg;
	private String remark;
	
	// 用户接受前台参数用
	private String beginTime; //查询起始时间
	private String endTime; //查询结束时间
	private String keyword; //关键词

	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getToUser() {
		return toUser;
	}
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	@Override
	public String toString() {
		return "WsChatlog [time=" + time + ", user=" + user + ", toUser=" + toUser + ", msg=" + msg + ", remark="
				+ remark + ", beginTime=" + beginTime + ", endTime=" + endTime + ", keyword=" + keyword + "]";
	}
}
