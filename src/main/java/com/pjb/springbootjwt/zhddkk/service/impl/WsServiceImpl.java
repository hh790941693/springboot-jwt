package com.pjb.springbootjwt.zhddkk.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.pjb.springbootjwt.zhddkk.bean.JsonResult;
import com.pjb.springbootjwt.zhddkk.dao.WsDao;
import com.pjb.springbootjwt.zhddkk.domain.WsOperationLogDO;
import com.pjb.springbootjwt.zhddkk.entity.*;
import com.pjb.springbootjwt.zhddkk.service.WsService;
import com.pjb.springbootjwt.zhddkk.util.CommonUtil;
import com.pjb.springbootjwt.zhddkk.util.ServiceUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WsServiceImpl implements WsService {
	
	private static final Log logger = LogFactory.getLog(WsServiceImpl.class);

	@Autowired
	private WsDao wsDao;
	
	@Override
	public List<WsUser> queryWsUser(WsUser param) {

		return wsDao.queryWsUser(param);
	}

	@Override
	public int insertWsUser(WsUser param) {
		logger.debug("注册用户:"+param.toString());
		int res = wsDao.insertWsUser(param);
		if (res == 0)
		{
			System.out.println("用户"+param.getName()+"注册失败!");
		}
		return res;
	}

	@Override
	public int updateWsUser(WsUser param) {
		int res =  wsDao.updateWsUser(param);
		if (res == 0)
		{
			System.out.println("用户"+param.getName()+"更新失败!");
		}
		return res;
	}

	@Override
	public int insertChatlog(WsChatlog param) {
		
		return wsDao.insertChatlog(param);
	}

	@Override
	public List<WsChatlog> queryHistoryChatlog(String user) {
		WsUser wu = new WsUser();
		wu.setName(user);
		List<WsUser> list = wsDao.queryWsUser(wu);
		if (list != null && list.size() > 0)
		{
			String lastLogoutTime = list.get(0).getLastLogoutTime();
			if (!CommonUtil.validateEmpty(lastLogoutTime))
			{
				return wsDao.queryHistoryChatlog(user, lastLogoutTime);
			}
		}
		return null;
	}

	@Override
	public int queryWsUserCount(WsUser param) {
		
		return wsDao.queryWsUserCount(param);
	}

	@Override
	public List<WsUser> queryWsUserByPage(WsUser params) {

		return wsDao.queryWsUserByPage(params);
	}

	@Override
	public PageResponseEntity queryHistoryChatlogByPage(WsChatlog params) {
		
		int totalCount = queryHistoryChatlogCount(params);
		int totalPage = 0;
		int curPage = params.getCurPage();
		int numPerPage = params.getNumPerPage();
		if (totalCount % numPerPage != 0)
		{
			totalPage = totalCount/numPerPage + 1;
		}
		else
		{
			totalPage = totalCount / numPerPage;
		}
		
		int start = 0;
		int limit = numPerPage;
		if (curPage == 1)
		{
			start = 0;
		}
		else
		{
			start = (curPage-1) * numPerPage;
		}
		params.setStart(start);
		params.setLimit(limit);
		List<WsChatlog> list = wsDao.queryHistoryChatlogByPage(params);
		
		PageResponseEntity rqe = new PageResponseEntity();
		rqe.setTotalCount(totalCount);
		rqe.setTotalPage(totalPage);
		rqe.setList(list);
		
		// 已注册用户
		List<WsUser> userInfoList = wsDao.queryWsUser(new WsUser());
		List<String> userList = new ArrayList<String>();
		for (WsUser wu : userInfoList)
		{
			userList.add(wu.getName());
		}
		rqe.setParameter1(userList);
		
		return rqe;
	}

	@Override
	public int queryHistoryChatlogCount(WsChatlog log) {
		return wsDao.queryHistoryChatlogCount(log);
	}

	@Override
	public PageResponseEntity queryCommonByPage(WsCommon params) {
		int totalCount = queryCommonCount(params);
		int totalPage = 0;
		int curPage = params.getCurPage();
		int numPerPage = params.getNumPerPage();
		if (totalCount % numPerPage != 0)
		{
			totalPage = totalCount/numPerPage + 1;
		}
		else
		{
			totalPage = totalCount / numPerPage;
		}
		
		int start = 0;
		int limit = numPerPage;
		if (curPage == 1)
		{
			start = 0;
		}
		else
		{
			start = (curPage-1) * numPerPage;
		}
		params.setStart(start);
		params.setLimit(limit);
		List<WsCommon> list = wsDao.queryCommonByPage(params);
		
		PageResponseEntity rqe = new PageResponseEntity();
		rqe.setTotalCount(totalCount);
		rqe.setTotalPage(totalPage);
		rqe.setList(list);

	
		return rqe;
	}

	@Override
	public void insertCommon(WsCommon param) {
		
		wsDao.insertCommon(param);
	}

	@Override
	public void deleteCommon(WsCommon param) {
		
		wsDao.deleteCommon(param);
	}

	@Override
	public void updateCommon(WsCommon param) {

		wsDao.updateCommon(param);
	}

	@Override
	public int queryCommonCount(WsCommon param) {
		
		return wsDao.queryCommonCount(param);
	}

	@Override
	public List<WsCommon> queryCommon(WsCommon param) {
		
		return wsDao.queryCommon(param);
	}

	@Override
	public void insertMusic(WsFile wf) {
		int cnt = wsDao.insertMusic(wf);
		if (cnt > 0) {
			System.out.println("新增记录成功:"+wf.toString());
		}
	}

	@Override
	public void deleteMusic(int id) {
		List<WsFile> list = wsDao.queryMusic(new WsFile(id,null));
		int cnt = wsDao.deleteMusic(id);
		if (cnt>0) {
			if (list.size()>0) {
				String diskPath = list.get(0).getDiskPath();
				String filename = list.get(0).getFilename();
				
				String fileAbsPath = diskPath+File.separator+filename;
				File f = new File(fileAbsPath);
				if (f.isFile()) {
					if (f.delete()) {
						System.out.println("delete file:" + fileAbsPath);
					}
				}
			}
		}
	}

	@Override
	public List<WsFile> queryMusic(WsFile wf) {
		return wsDao.queryMusic(wf);
	}

	/**
	 * 添加广告
	 */
	@Override
	public void insertAds(WsAds wa) {
		int cnt = wsDao.insertAds(wa);
		if (cnt > 0) {
			System.out.println("新增广告成功:"+wa.toString());
		}
	}

	/**
	 * 查询广告个数
	 */
	@Override
	public int queryAdsCount(WsAds param) {
		return wsDao.queryAdsCount(param);
	}

	/**
	 * 分页显示广告列表
	 */
	@Override
	public List<WsAds> queryAdsByPage(int start,int limit) {
		List<WsAds> list = wsDao.queryAdsByPage(start, limit);
		if (null != list && list.size()>0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (WsAds wa : list) {
				String createTimeStr = sdf.format(wa.getCreateTime());
				wa.setCreateTimeStr(createTimeStr);
			}
		}
		
		return list;
	}

	@Override
	public void insertFriendsApply(WsFriendsApply wfa) {
		int cnt = wsDao.insertFriendsApply(wfa);
		if (cnt > 0) {
			System.out.println("新增好友申请记录成功:"+wfa.toString());
		}
	}

	@Override
	public void updateFrinedsApply(WsFriendsApply wfa) {
		int cnt = wsDao.updateFrinedsApply(wfa);
		if (cnt > 0) {
			System.out.println("更新好友申请记录成功:"+wfa.toString());
		}
	}

	@Override
	public int queryFriendsApplyCount(WsFriendsApply wfa) {
		return wsDao.queryFriendsApplyCount(wfa);
	}

	@Override
	public List<WsFriendsApply> queryFriendsApplyList(WsFriendsApply wfa) {
		List<WsFriendsApply> list = wsDao.queryFriendsApplyList(wfa);
		if (null != list && list.size()>0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (WsFriendsApply wfax : list) {
				String createTimeStr = sdf.format(wfax.getCreateTime());
				wfax.setCreateTimeStr(createTimeStr);
			}
		}
		return list;
	}

	@Override
	public void insertMyFriend(WsFriends wf) {
		int cnt = wsDao.insertMyFriend(wf);
		if (cnt > 0) {
			System.out.println("新增好友记录成功:"+wf.toString());
		}
	}

	@Override
	public int queryMyFriendsCount(WsFriends wf) {
		return wsDao.queryMyFriendsCount(wf);
	}

	@Override
	public List<WsFriends> queryMyFriendsList(WsFriends wf) {
		List<WsFriends> list = wsDao.queryMyFriendsList(wf);
		if (null != list && list.size()>0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (WsFriends wfx : list) {
				String createTimeStr = sdf.format(wfx.getCreateTime());
				wfx.setCreateTimeStr(createTimeStr);
			}
		}
		
		return list;
	}

	@Override
	public boolean existFriend(WsFriends wf) {
		int isExist = wsDao.existFriend(wf);
		return isExist > 0 ? true : false;
	}

	@Override
	public void insertUserProfile(WsUserProfile wup) {
		int cnt = wsDao.insertUserProfile(wup);
		if (cnt>0) {
			System.out.println("新增用户基本信息记录成功:"+wup.toString());
		}
	}

	@Override
	public void updateUserProfile(WsUserProfile wup) {
		int cnt = wsDao.updateUserProfile(wup);
		if (cnt>0) {
			System.out.println("更新用户基本信息记录成功:"+wup.toString());
		}
	}

	@Override
	public boolean existUserProfile(WsUserProfile wup) {
		int cnt = wsDao.existUserProfile(wup);
		return cnt > 0 ? true : false;
	}

	@Override
	public WsUserProfile selectOneUserProfile(WsUserProfile wup) {
		return wsDao.selectOneUserProfile(wup);
	}

	@Override
	public List<WsCircle> queryCircleByPage(WsCircle wc) {
		List<WsCircle> list = wsDao.queryCircleByPage(wc);
		if (null != list && list.size()>0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (WsCircle www : list) {
				www.setCreateTimeStr(sdf.format(www.getCreateTime()));
				//获取头像
				WsUserProfile wup = new WsUserProfile();
				wup.setUserName(www.getUserName());
				wup = wsDao.selectOneUserProfile(wup);
				if (null != wup) {
					www.setHeadImg(wup.getImg());
				}
			}
		}
		return list;
	}

	@Override
	public void insertCircle(WsCircle wc) {
		int cnt = wsDao.insertCircle(wc);
		if (cnt>0) {
			System.out.println("插入朋友圈记录成功:"+wc.toString());
		}
	}

	@Override
	public void updateCircle(WsCircle wc) {
		int cnt = wsDao.updateCircle(wc);
		if (cnt>0) {
			System.out.println("更新朋友圈点赞成功:"+wc.toString());
		}
	}

	@Override
	public List<WsCircleComment> queryCircleCommentList(WsCircleComment wcc) {
		List<WsCircleComment> list = wsDao.queryCircleCommentList(wcc);
		if (null != list && list.size()>0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (WsCircleComment www : list) {
				www.setCreateTimeStr(sdf.format(www.getCreateTime()));
			}
		}
		return list;
	}

	@Override
	public void insertCircleComment(WsCircleComment wcc) {
		int cnt = wsDao.insertCircleComment(wcc);
		if (cnt>0) {
			System.out.println("插入朋友圈评论成功:"+wcc.toString());
		}
	}

	@Override
	public void deleteCircle(WsCircle wc) {
		int cnt = wsDao.deleteCircle(wc);
		WsCircleComment wcc = new WsCircleComment();
		wcc.setCircleId(wc.getId());
		wsDao.deleteCircleComment(wcc);
		if (cnt>0) {
			System.out.println("删除朋友圈成功:"+wc.toString());
		}
	}

	@Override
	public void deleteCircleComment(WsCircleComment wcc) {
		int cnt = wsDao.deleteCircleComment(wcc);
		if (cnt>0) {
			System.out.println("删除朋友圈评论成功:"+wcc.toString());
		}
	}

	@Override
	public void deleteFriendsApply(WsFriendsApply wfa) {
		int cnt = wsDao.deleteFriendsApply(wfa);
		if (cnt>0) {
			System.out.println("删除好友申请成功:"+wfa.toString());
		}
	}

	@Override
	public void deleteMyFriend(WsFriends wf) {
		int cnt = wsDao.deleteMyFriend(wf);
		if (cnt>0) {
			System.out.println("删除好友成功:"+wf.toString());
		}
	}

	@Override
	public int queryMyApplyCount(WsFriendsApply wfa) {
		return wsDao.queryMyApplyCount(wfa);
	}

	@Override
	public List<WsFriendsApply> queryMyApplyList(WsFriendsApply wfa) {
		List<WsFriendsApply> list = wsDao.queryMyApplyList(wfa);
		if (null != list && list.size()>0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (WsFriendsApply wfax : list) {
				String createTimeStr = sdf.format(wfax.getCreateTime());
				wfax.setCreateTimeStr(createTimeStr);
			}
		}
		return list;
	}
	
	//------------------操作日志--------------------------------------------
	@Override
	public List<WsOperationLog> queryOperationLogList(WsOperationLog wol){
		return wsDao.queryOperationLogList(wol);
	}

	@Override
	public void insertOperationLog(WsOperationLog wol) {
		int cnt = wsDao.insertOperationLog(wol);
		if (cnt>0) {
			logger.info("新增操作日志成功:"+wol.getOperDescribe());
		}
	}

	@Override
	public PageResponseEntity queryOperationLogByPage(WsOperationLog params) {
		int totalCount = queryOperationLogCount(params);
		int totalPage = 0;
		int curPage = params.getCurPage();
		int numPerPage = params.getNumPerPage();
		if (totalCount % numPerPage != 0){
			totalPage = totalCount/numPerPage + 1;
		}
		else{
			totalPage = totalCount / numPerPage;
		}
		
		int start = 0;
		int limit = numPerPage;
		if (curPage == 0 || curPage == 1){
			start = 0;
		}
		else{
			start = (curPage-1) * numPerPage;
		}
		params.setStart(start);
		params.setLimit(limit);
		List<WsOperationLog> list = wsDao.queryOperationLogByPage(params);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (WsOperationLog wl : list) {
			wl.setAccessTimeText(sdf.format(wl.getAccessTime()));
		}

		PageResponseEntity rqe = new PageResponseEntity();
		rqe.setTotalCount(totalCount);
		rqe.setTotalPage(totalPage);
		rqe.setList(list);
		return rqe;
	}

	@Override
	public int queryOperationLogCount(WsOperationLog wol) {
		return wsDao.queryOperationLogCount(wol);
	}

	@Override
	public int queryCircleCount(WsCircle wc) {
		return wsDao.queryCircleCount(wc);
	}

	@Override
	public JsonResult queryOperationLogByPageByBootStrap(WsOperationLog params) {
		int totalCount = queryOperationLogCount(params);
		int curPage = params.getCurPage();
		int numPerPage = params.getNumPerPage();

		Page tmpPage = ServiceUtil.buildPage(curPage, numPerPage);
		params.setStart(tmpPage.getStart());
		params.setLimit(tmpPage.getLimit());
		
		List<WsOperationLog> list = wsDao.queryOperationLogByPage(params);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (WsOperationLog wl : list) {
			wl.setAccessTimeText(sdf.format(wl.getAccessTime()));
		}

		JsonResult jr = new JsonResult();
		jr.setTotal(totalCount);
		jr.setRows(list);
		return jr;
	}
}
