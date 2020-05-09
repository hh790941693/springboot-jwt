package com.pjb.springbootjwt.zhddkk.dao;


import com.pjb.springbootjwt.zhddkk.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WsDao
{
	public List<WsUser> queryWsUser(WsUser param);
	
	public List<WsUser> queryWsUserByPage(WsUser params);
	
	public int queryWsUserCount(WsUser param);
	
	public int insertWsUser(WsUser param);
	
	public int updateWsUser(WsUser param);
	
	public int insertChatlog(WsChatlog param);
	
	// 获取用户的离线日志
	public List<WsChatlog> queryHistoryChatlog(@Param("user") String user, @Param("lastLogoutTime") String lastLogoutTime);
	
	public List<WsChatlog> queryHistoryChatlogByPage(WsChatlog chatlog);
	
	public int queryHistoryChatlogCount(WsChatlog log);
	
	public List<WsDic> queryDic(WsDic param);
	
	//------------------脏话、敏感词 增删改查----------------
	public List<WsCommon> queryCommonByPage(WsCommon param);
	
	public List<WsCommon> queryCommon(WsCommon param);
	
	public void insertCommon(WsCommon param);
	
	public void deleteCommon(WsCommon param);
	
	public void updateCommon(WsCommon param);
	
	public int queryCommonCount(WsCommon param);
	
	
	// -----------------上次音乐文件--------------------------------
	public int insertMusic(WsFile wf);
	
	public int deleteMusic(@Param("id") int id);
	
	public List<WsFile> queryMusic(WsFile wf);
	
	//------------------添加广告------------------------------------
	public int insertAds(WsAds wa);
	public int queryAdsCount(WsAds param);
	public List<WsAds> queryAdsByPage(@Param("start") int start, @Param("limit") int limit);
	
	//--------------------添加好友申请--------------------------------
	public int insertFriendsApply(WsFriendsApply wfa); //新增申请
	public int updateFrinedsApply(WsFriendsApply wfa); //审核申请
	public int deleteFriendsApply(WsFriendsApply wfa); //删除申请
	public int queryFriendsApplyCount(WsFriendsApply wfa);
	public List<WsFriendsApply> queryFriendsApplyList(WsFriendsApply wfa); //查询申请列表
	
	public int queryMyApplyCount(WsFriendsApply wfa);//我的申请
	public List<WsFriendsApply> queryMyApplyList(WsFriendsApply wfa); //我的申请列表
	
	//--------------------我的好友列表-------------------------------
	public int insertMyFriend(WsFriends wf);  //添加好友
	public int deleteMyFriend(WsFriends wf);  //删除好友
	public int existFriend(WsFriends wf); //判断是否存在某个好友
	public int queryMyFriendsCount(WsFriends wf);
	public List<WsFriends> queryMyFriendsList(WsFriends wf); //查询好友列表
	
	//--------------------用户基本信息设置------------------------------------
	public int insertUserProfile(WsUserProfile wup); //插入记录
	public int updateUserProfile(WsUserProfile wup); //更新记录
	public int existUserProfile(WsUserProfile wup); //记录是否存在
	public WsUserProfile selectOneUserProfile(WsUserProfile wup); //查询一条记录
	
	//------------------朋友圈表---------------------------------------------
	public int queryCircleCount(WsCircle wc);
	public List<WsCircle> queryCircleByPage(WsCircle wc);
	public int insertCircle(WsCircle wc);
	public int updateCircle(WsCircle wc);
	public int deleteCircle(WsCircle wc);
	
	//-------------------朋友圈评论表---------------------------------------
	public List<WsCircleComment> queryCircleCommentList(WsCircleComment wcc);
	public int insertCircleComment(WsCircleComment wcc);
	public int deleteCircleComment(WsCircleComment wcc);
	
	//------------------操作日志--------------------------------------------
	public List<WsOperationLog> queryOperationLogList(WsOperationLog wol);
	public int insertOperationLog(WsOperationLog wol);
	public List<WsOperationLog> queryOperationLogByPage(WsOperationLog wol);
	public int queryOperationLogCount(WsOperationLog wol);
}
