package com.pjb.springbootjwt.zhddkk.entity;

import com.pjb.springbootjwt.zhddkk.domain.WsCommonDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;

import java.util.List;
import java.util.Map;

public class WsOnlineInfo 
{
	// 在线人数
	private int onlineCount;
	
	// 离线人数
	private int offlineCount;
	
	// 当前登录的用户信息
	private WsUsersDO currentOnlineUserInfo;
	
	// 所有用户列表
	private List<WsUsersDO> userInfoList;
	
	// 在线用户列表
	private List<WsUsersDO> onlineUserList;
	
	// 离线用户列表
	private List<WsUsersDO> offlineUserList;
	
	// 好友列表
	private List<WsUsersDO> friendsList;
	
	// 字典列表
	//private List<WsDic> dicList;
	
	//private Map<String, List<WsDic>> dicMap;
	
	// 敏感词、脏话、常用语、注册问题
	private Map<String, List<WsCommonDO>> commonMap;

	public int getOnlineCount() {
		return onlineCount;
	}

	public void setOnlineCount(int onlineCount) {
		this.onlineCount = onlineCount;
	}

	public int getOfflineCount() {
		return offlineCount;
	}

	public void setOfflineCount(int offlineCount) {
		this.offlineCount = offlineCount;
	}

	public Map<String, List<WsCommonDO>> getCommonMap() {
		return commonMap;
	}

	public void setCommonMap(Map<String, List<WsCommonDO>> commonMap) {
		this.commonMap = commonMap;
	}

	public WsUsersDO getCurrentOnlineUserInfo() {
		return currentOnlineUserInfo;
	}

	public void setCurrentOnlineUserInfo(WsUsersDO currentOnlineUserInfo) {
		this.currentOnlineUserInfo = currentOnlineUserInfo;
	}

	public List<WsUsersDO> getUserInfoList() {
		return userInfoList;
	}

	public void setUserInfoList(List<WsUsersDO> userInfoList) {
		this.userInfoList = userInfoList;
	}

	public List<WsUsersDO> getOnlineUserList() {
		return onlineUserList;
	}

	public void setOnlineUserList(List<WsUsersDO> onlineUserList) {
		this.onlineUserList = onlineUserList;
	}

	public List<WsUsersDO> getOfflineUserList() {
		return offlineUserList;
	}

	public void setOfflineUserList(List<WsUsersDO> offlineUserList) {
		this.offlineUserList = offlineUserList;
	}

	public List<WsUsersDO> getFriendsList() {
		return friendsList;
	}

	public void setFriendsList(List<WsUsersDO> friendsList) {
		this.friendsList = friendsList;
	}
}
