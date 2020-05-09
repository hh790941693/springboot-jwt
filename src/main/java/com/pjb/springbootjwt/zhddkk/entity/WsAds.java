package com.pjb.springbootjwt.zhddkk.entity;

import java.util.Date;

/**
 * 公告实体
 * 
 * @author hch
 *
 */
public class WsAds {
	private Integer id;
	private String title;
	private String content;
	private String receiveList;
	private Date createTime;
	private String createTimeStr;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReceiveList() {
		return receiveList;
	}
	public void setReceiveList(String receiveList) {
		this.receiveList = receiveList;
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
		return "WsAds [id=" + id + ", title=" + title + ", content=" + content + ", receiveList=" + receiveList
				+ ", createTime=" + createTime + ", createTimeStr=" + createTimeStr + "]";
	}
}
