package com.pjb.springbootjwt.zhddkk.entity;

import java.util.Date;

/**
 * 好友申请
 * 
 * @author Administrator
 *
 */
@Deprecated
public class WsFriendsApply extends Page {

	private Integer id;
	private Integer fromId;
	private String fromName;
	private Integer toId;
	private String toName;
	private Integer processStatus;
	private Date createTime;
	private String createTimeStr;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getFromId() {
		return fromId;
	}
	public void setFromId(Integer fromId) {
		this.fromId = fromId;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public Integer getToId() {
		return toId;
	}
	public void setToId(Integer toId) {
		this.toId = toId;
	}
	public String getToName() {
		return toName;
	}
	public void setToName(String toName) {
		this.toName = toName;
	}
	public Integer getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
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
		return "WsFriendsApply [id=" + id + ", fromId=" + fromId + ", fromName=" + fromName + ", toId=" + toId
				+ ", toName=" + toName + ", processStatus=" + processStatus + ", createTime=" + createTime
				+ ", createTimeStr=" + createTimeStr + "]";
	}
}
