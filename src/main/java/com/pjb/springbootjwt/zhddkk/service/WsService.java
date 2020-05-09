package com.pjb.springbootjwt.zhddkk.service;

import com.pjb.springbootjwt.zhddkk.bean.JsonResult;
import com.pjb.springbootjwt.zhddkk.entity.*;

import java.util.List;


public interface WsService 
{
	public List<WsUser> queryWsUser(WsUser param);
	
	public int queryWsUserCount(WsUser param);
	
	public List<WsUser> queryWsUserByPage(WsUser params);
	
	public int insertWsUser(WsUser param);
	
	public int updateWsUser(WsUser param);
	
	public int insertChatlog(WsChatlog param);
	
	// 获取用户的离线日志
	public List<WsChatlog> queryHistoryChatlog(String user);
	
	public PageResponseEntity queryHistoryChatlogByPage(WsChatlog log);
	
	public int queryHistoryChatlogCount(WsChatlog log);
	
	//----------------------------------------------------------------
	public List<WsCommon> queryCommon(WsCommon param);
	
	public PageResponseEntity queryCommonByPage(WsCommon param);
	
	public void insertCommon(WsCommon param);
	
	public void deleteCommon(WsCommon param);
	
	public void updateCommon(WsCommon param);
	
	public int queryCommonCount(WsCommon param);
	
	
	// -----------------上次音乐文件--------------------------------
	public void insertMusic(WsFile wf);
	
	public void deleteMusic(int id);
	
	public List<WsFile> queryMusic(WsFile wf);
	
	//------------------添加广告------------------------------------
	public void insertAds(WsAds wa);
	public int queryAdsCount(WsAds param);
	public List<WsAds> queryAdsByPage(int start, int limit);
	
	//--------------------添加好友申请--------------------------------
	public void insertFriendsApply(WsFriendsApply wfa); //新增申请
	public void updateFrinedsApply(WsFriendsApply wfa); //审核申请
	public void deleteFriendsApply(WsFriendsApply wfa); //删除申请
	public int queryFriendsApplyCount(WsFriendsApply wfa);
	public List<WsFriendsApply> queryFriendsApplyList(WsFriendsApply wfa); //查询申请列表
	
	public int queryMyApplyCount(WsFriendsApply wfa);//我的申请
	public List<WsFriendsApply> queryMyApplyList(WsFriendsApply wfa);
	
	
	//--------------------我的好友列表-------------------------------
	public void insertMyFriend(WsFriends wf);  //添加好友
	public void deleteMyFriend(WsFriends wf);  //删除好友
	public boolean existFriend(WsFriends wf); //判断是否存在某个好友
	public int queryMyFriendsCount(WsFriends wf);
	public List<WsFriends> queryMyFriendsList(WsFriends wf); //查询好友列表
	
	
	//--------------------用户基本信息设置------------------------------------
	public void insertUserProfile(WsUserProfile wup); //插入记录
	public void updateUserProfile(WsUserProfile wup); //更新记录
	public boolean existUserProfile(WsUserProfile wup); //记录是否存在
	public WsUserProfile selectOneUserProfile(WsUserProfile wup); //查询一条记录
	
	
	//------------------朋友圈表---------------------------------------------
	public int queryCircleCount(WsCircle wc);
	public List<WsCircle> queryCircleByPage(WsCircle wc);
	public void insertCircle(WsCircle wc);
	public void updateCircle(WsCircle wc);
	public void deleteCircle(WsCircle wc);
	
	//-------------------朋友圈评论表---------------------------------------
	public List<WsCircleComment> queryCircleCommentList(WsCircleComment wcc);
	public void insertCircleComment(WsCircleComment wcc);
	public void deleteCircleComment(WsCircleComment wcc);
	
	//------------------操作日志--------------------------------------------
	public List<WsOperationLog> queryOperationLogList(WsOperationLog wol);
	public PageResponseEntity queryOperationLogByPage(WsOperationLog wol);
	public JsonResult queryOperationLogByPageByBootStrap(WsOperationLog wol);
	public int queryOperationLogCount(WsOperationLog wol);
	public void insertOperationLog(WsOperationLog wol);
}
