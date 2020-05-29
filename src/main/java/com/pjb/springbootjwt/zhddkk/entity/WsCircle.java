package com.pjb.springbootjwt.zhddkk.entity;

import java.util.Date;
import java.util.List;

@Deprecated
public class WsCircle extends Page {
	private Integer id;
	private String userName;
	private Integer userId;
	private String content;
	private Integer likeNum;
	private String pic1;
	private String pic2;
	private String pic3;
	private String pic4;
	private String pic5;
	private String pic6;
	private Date createTime;
	private String createTimeStr;
	//非表字段
	private String headImg;
	private List<WsCircleComment> commentList;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getLikeNum() {
		return likeNum;
	}
	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}
	public String getPic1() {
		return pic1;
	}
	public void setPic1(String pic1) {
		this.pic1 = pic1;
	}
	public String getPic2() {
		return pic2;
	}
	public void setPic2(String pic2) {
		this.pic2 = pic2;
	}
	public String getPic3() {
		return pic3;
	}
	public void setPic3(String pic3) {
		this.pic3 = pic3;
	}
	public String getPic4() {
		return pic4;
	}
	public void setPic4(String pic4) {
		this.pic4 = pic4;
	}
	public String getPic5() {
		return pic5;
	}
	public void setPic5(String pic5) {
		this.pic5 = pic5;
	}
	public String getPic6() {
		return pic6;
	}
	public void setPic6(String pic6) {
		this.pic6 = pic6;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public List<WsCircleComment> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<WsCircleComment> commentList) {
		this.commentList = commentList;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	@Override
	public String toString() {
		return "WsCircle [id=" + id + ", userName=" + userName + ", userId=" + userId + ", content=" + content
				+ ", likeNum=" + likeNum + ", pic1=" + pic1 + ", pic2=" + pic2 + ", pic3=" + pic3 + ", pic4=" + pic4
				+ ", pic5=" + pic5 + ", pic6=" + pic6 + ", createTime=" + createTime + ", commentList=" + commentList
				+ "]";
	}
}
