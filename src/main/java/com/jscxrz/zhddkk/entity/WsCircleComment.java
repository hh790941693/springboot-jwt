package com.jscxrz.zhddkk.entity;

import java.util.Date;

public class WsCircleComment {

	private Integer id;
	private Integer circleId;
	private String userName;
	private Integer userId;
	private String comment;
	private Date createTime;
	private String createTimeStr;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCircleId() {
		return circleId;
	}
	public void setCircleId(Integer circleId) {
		this.circleId = circleId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	@Override
	public String toString() {
		return "WsCircleComment [id=" + id + ", circleId=" + circleId + ", userName=" + userName + ", userId=" + userId
				+ ", comment=" + comment + ", createTime=" + createTime + "]";
	}
}
