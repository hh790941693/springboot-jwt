package com.pjb.springbootjwt.zhddkk.entity;

import java.util.List;
import java.util.Map;

public class WsOnlineInfo 
{
	// 在线人数
	private int onlineCount;
	
	// 离线人数
	private int offlineCount;
	
	// 当前登录的用户信息
	private WsUser currentOnlineUserInfo;
	
	// 所有用户列表
	private List<WsUser> userInfoList;
	
	// 在线用户列表
	private List<WsUser> onlineUserList;
	
	// 离线用户列表
	private List<WsUser> offlineUserList;
	
	// 好友列表
	private List<WsUser> friendsList;
	
	// 字典列表
	//private List<WsDic> dicList;
	
	//private Map<String, List<WsDic>> dicMap;
	
	// 敏感词、脏话、常用语、注册问题
	private Map<String, List<WsCommon>> commonMap;

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

	public List<WsUser> getUserInfoList() {
		return userInfoList;
	}

	public void setUserInfoList(List<WsUser> userInfoList) {
		this.userInfoList = userInfoList;
	}

	public List<WsUser> getOnlineUserList() {
		return onlineUserList;
	}

	public void setOnlineUserList(List<WsUser> onlineUserList) {
		this.onlineUserList = onlineUserList;
	}

	public List<WsUser> getOfflineUserList() {
		return offlineUserList;
	}

	public void setOfflineUserList(List<WsUser> offlineUserList) {
		this.offlineUserList = offlineUserList;
	}


	public Map<String, List<WsCommon>> getCommonMap() {
		return commonMap;
	}

	public void setCommonMap(Map<String, List<WsCommon>> commonMap) {
		this.commonMap = commonMap;
	}

	public WsUser getCurrentOnlineUserInfo() {
		return currentOnlineUserInfo;
	}

	public void setCurrentOnlineUserInfo(WsUser currentOnlineUserInfo) {
		this.currentOnlineUserInfo = currentOnlineUserInfo;
	}

	public List<WsUser> getFriendsList() {
		return friendsList;
	}

	public void setFriendsList(List<WsUser> friendsList) {
		this.friendsList = friendsList;
	}

	@Override
	public String toString() {
		return "WsOnlineInfo [onlineCount=" + onlineCount + ", offlineCount=" + offlineCount
				+ ", currentOnlineUserInfo=" + currentOnlineUserInfo + ", userInfoList=" + userInfoList
				+ ", onlineUserList=" + onlineUserList + ", offlineUserList=" + offlineUserList + ", friendsList="
				+ friendsList + ", commonMap=" + commonMap + "]";
	}
}
