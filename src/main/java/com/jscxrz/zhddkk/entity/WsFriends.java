package com.jscxrz.zhddkk.entity;

import java.util.Date;

/**
 * 我的好友列表
 * 
 * @author Administrator
 *
 */
public class WsFriends extends Page{
	private Integer id;
	private Integer uid;
	private String uname;
	private Integer fid;
	private String fname;
	private Date createTime;
	private String createTimeStr;
	private String remark;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "WsFriends [id=" + id + ", uid=" + uid + ", uname=" + uname + ", fid=" + fid + ", fname=" + fname
				+ ", createTime=" + createTime + ", createTimeStr=" + createTimeStr + ", remark=" + remark + "]";
	}

}
