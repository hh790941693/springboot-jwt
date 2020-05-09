package com.pjb.springbootjwt.zhddkk.entity;

import java.util.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 用户账号表
 * 
 * @author Administrator
 *
 */
public class WsUser extends Page
{
	public WsUser() {}
	
	public WsUser(String name) {
		this.name = name;
	}
	
	private Integer id;
	
	// 姓名
	@Excel(name = "姓名",orderNum = "1",width=10)
	private String name;
	
	// 密码(密文)
	private String password;
	
	// 密码(明文) 
	@Excel(name = "密码",orderNum = "2",width=10)
	private String passwordDecode;
	
	// 注册时间
	@Excel(name = "注册时间",orderNum = "3",width=20)
	private String registerTime;
	
	// 是否在线  0:下线   1:在线
	@Excel(name = "在线状态",orderNum = "4",width=10,replace={"离线_0","在线_1"})
	private String state;
	
	// 最近一次登录时间
	@Excel(name = "最近登陆时间",orderNum = "5",width=20)
	private String lastLoginTime;
	
	// 最近一次离线时间
	private String LastLogoutTime;
	
	// 账号是否被禁用     0:禁用    1:可用
	@Excel(name = "禁用状态",orderNum = "6",width=10,replace={"禁用_0","正常_1"})
	private String enable;
	
	// 是否禁言   0:禁言   1:没有禁言
	@Excel(name = "禁言状态",orderNum = "7",width=10,replace={"禁言_0","正常_1"})
	private String speak;

	@Excel(name = "问题1",orderNum = "8",width=25)
	private String question1;
	
	@Excel(name = "问题2",orderNum = "10",width=25)
	private String question2;
	
	@Excel(name = "问题3",orderNum = "12",width=25)
	private String question3;
	
	@Excel(name = "回答1",orderNum = "9",width=15)
	private String answer1;
	
	@Excel(name = "回答2",orderNum = "11",width=15)
	private String answer2;
	
	@Excel(name = "回答3",orderNum = "13",width=15)
	private String answer3;
	
	private Date createTime;
	
	private String createTimeStr;
	
	// 非表字段  是否是好友 0:不是  1:申请中 2:被拒绝 3:申请成功
	private Integer isFriend;
			
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}	
	
	public String getQuestion1() {
		return question1;
	}

	public void setQuestion1(String question1) {
		this.question1 = question1;
	}

	public String getQuestion2() {
		return question2;
	}

	public void setQuestion2(String question2) {
		this.question2 = question2;
	}

	public String getQuestion3() {
		return question3;
	}

	public void setQuestion3(String question3) {
		this.question3 = question3;
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public String getAnswer3() {
		return answer3;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}
	
	public String getLastLogoutTime() {
		return LastLogoutTime;
	}

	public void setLastLogoutTime(String lastLogoutTime) {
		LastLogoutTime = lastLogoutTime;
	}
	
	public String getSpeak() {
		return speak;
	}

	public void setSpeak(String speak) {
		this.speak = speak;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	
	public Integer getIsFriend() {
		return isFriend;
	}

	public void setIsFriend(Integer isFriend) {
		this.isFriend = isFriend;
	}

	public String getPasswordDecode() {
		return passwordDecode;
	}

	public void setPasswordDecode(String passwordDecode) {
		this.passwordDecode = passwordDecode;
	}

	@Override
	public String toString() {
		return "WsUser [id=" + id + ", name=" + name + ", password=" + password + ", registerTime=" + registerTime
				+ ", state=" + state + ", lastLoginTime=" + lastLoginTime + ", LastLogoutTime=" + LastLogoutTime
				+ ", enable=" + enable + ", speak=" + speak + ", question1=" + question1 + ", question2=" + question2
				+ ", question3=" + question3 + ", answer1=" + answer1 + ", answer2=" + answer2 + ", answer3=" + answer3
				+ ", createTime=" + createTime + ", createTimeStr=" + createTimeStr + ", isFriend=" + isFriend + "]";
	}
}
