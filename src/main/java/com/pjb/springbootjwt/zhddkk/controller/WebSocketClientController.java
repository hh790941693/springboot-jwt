package com.pjb.springbootjwt.zhddkk.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.common.redis.RedisUtil;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.bean.SessionInfoBean;
import com.pjb.springbootjwt.zhddkk.bean.WangyiNewsBean;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.constants.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.constants.OperationEnum;
import com.pjb.springbootjwt.zhddkk.domain.*;
import com.pjb.springbootjwt.zhddkk.entity.PageResponseEntity;
import com.pjb.springbootjwt.zhddkk.entity.WsOnlineInfo;
import com.pjb.springbootjwt.zhddkk.interceptor.WsInterceptor;
import com.pjb.springbootjwt.zhddkk.service.*;
import com.pjb.springbootjwt.zhddkk.util.*;
import com.pjb.springbootjwt.zhddkk.websocket.ZhddWebSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * webSocket控制器.
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("ws")
public class WebSocketClientController extends AdminBaseController {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketClientController.class);

    private static final SimpleDateFormat SDF_STANDARD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Map<String, String> configMap = WsInterceptor.getConfigMap();

    private static final Map<String, String> chatMappingMap = WsInterceptor.getChatMappingMap();

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WsCircleService wsCircleService;

    @Autowired
    private WsCircleCommentService wsCircleCommentService;

    @Autowired
    private WsUsersService wsUsersService;

    @Autowired
    private WsUserProfileService wsUserProfileService;

    @Autowired
    private WsFriendsService wsFriendsService;

    @Autowired
    private WsFriendsApplyService wsFriendsApplyService;

    @Autowired
    private WsCommonService wsCommonService;

    /**
     * 退出.
     *
     */
    @OperationLogAnnotation(type = OperationEnum.UPDATE, module = ModuleEnum.LOGOUT, subModule = "", describe = "退出")
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public String logout(@RequestParam("user")String user, HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        System.out.println("退出前SESSION:" + httpSession.getId());
        httpSession.invalidate();
        ZhddWebSocket.removeUserFromAllRoom(user);

        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
        if (null != wsUsersDO) {
            wsUsersDO.setState("0");
            wsUsersDO.setLastLogoutTime(SDF_STANDARD.format(new Date()));
            wsUsersService.updateById(wsUsersDO);
        }
        return CommonConstants.SUCCESS;
    }

    /**
     *聊天页面.
     *
     * @return 聊天页面
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.CHAT, subModule = "", describe = "聊天首页")
    @RequestMapping(value = "wsclientChat.page")
    public String wsclientChat() {
        logger.debug("访问wsclientChat.page");
        return "ws/wsclientChat";
    }

    /**
     * 头部页面.
     *
     * @return header页面
     */
    @RequestMapping(value = "header.page")
    public String headerPage() {
        logger.debug("访问header.page");
        return "ws/header";
    }

    /**
     * 底部页面.
     *
     * @return 底部页面
     */
    @RequestMapping(value = "footer.page")
    public String footerPage() {
        logger.debug("访问footer.page");
        return "ws/footer";
    }

    /**
     * 客户端左侧导航栏页面.
     *
     * @return 客户端左侧导航栏页面
     */
    @RequestMapping(value = "clientLeftNavi.page")
    public String clientLeftNavi() {
        logger.debug("访问clientLeftNavi.page");
        return "ws/clientLeftNavi";
    }

    /**
     * 服务端左侧导航栏页面.
     *
     * @return 服务端左侧导航栏页面
     */
    @RequestMapping(value = "serverLeftNavi.page")
    public String serverLeftNavi() {
        logger.debug("访问serverLeftNavi.page");
        return "ws/serverLeftNavi";
    }

    /**
     * 朋友圈首页.
     *
     * @return 朋友圈首页
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.CIRCLE, subModule = "", describe = "朋友圈首页")
    @RequestMapping(value = "wsclientCircle.page")
    public String clientCircle() {
        logger.debug("访问wsclientCircle.page");
        return "ws/wsclientCircleVue";
    }

    /**
     * 添加朋友圈.
     *
     * @param model 模型
     * @param user 用户id
     * @return 添加朋友圈页面
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.CIRCLE, subModule = "", describe = "添加朋友圈首页")
    @RequestMapping(value = "wsclientAddCircle.page")
    public String wsclientAddCircle(Model model, @RequestParam("user")String user) {
        logger.debug("访问wsclientAddCircle.page");
        model.addAttribute("user", user);
        return "ws/wsclientAddCircle";
    }

    /**
     * 获取在线人数信息.
     *
     * @return 在线用户信息
     */
    @RequestMapping(value = "getOnlineInfo.json")
    @ResponseBody
    public Result<WsOnlineInfo> getOnlineInfo(@RequestParam(value = "roomName") String roomName, @RequestParam(value = "user") String user) {
        if (StringUtils.isBlank(user)) {
            return Result.fail(new WsOnlineInfo());
        }
        Map<String, Session> roomClientMap = ZhddWebSocket.getRoomClientsSessionMap(roomName);

        //所有用户
        List<WsUsersDO> allUserList = wsUsersService.selectList(new EntityWrapper<WsUsersDO>()
                .ne("name", CommonConstants.ADMIN_USER));
        //在线用户
        List<WsUsersDO> onlineUserList = new ArrayList<>();
        //离线用户
        List<WsUsersDO> offlineUserList = new ArrayList<WsUsersDO>();

        for (WsUsersDO wu : allUserList) {
            if (roomClientMap.containsKey(wu.getName())) {
                wu.setState("1");
                onlineUserList.add(wu);
            } else {
                wu.setState("0");
                offlineUserList.add(wu);
            }
        }

        //当前用户信息
        WsUsersDO currentOnlineUserInfo = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
        WsUserProfileDO wsUserProfileDO = wsUserProfileService.selectOne(new EntityWrapper<WsUserProfileDO>()
                .eq("user_id", currentOnlineUserInfo.getId()));
        if (null != wsUserProfileDO) {
            currentOnlineUserInfo.setHeadImage(wsUserProfileDO.getImg());
        }

        WsOnlineInfo woi = new WsOnlineInfo();
        woi.setOfflineCount(offlineUserList.size());
        woi.setOnlineCount(onlineUserList.size());
        Collections.reverse(allUserList);
        woi.setUserInfoList(allUserList);
        woi.setOnlineUserList(onlineUserList);
        woi.setOfflineUserList(offlineUserList);
        //我的好友列表
        List<WsUsersDO> friendsUserList = wsUsersService.queryMyFriendList(currentOnlineUserInfo.getId());
        woi.setFriendsList(friendsUserList);
        List<WsUsersDO> onineFriendList = friendsUserList.stream().filter(wu -> wu != null
                && wu.getState().equals("1")).collect(Collectors.toList());
        woi.setOnlineFriendCount(onineFriendList.size());
        List<WsUsersDO> offLineFriendList = friendsUserList.stream().filter(wu -> wu != null
                && wu.getState().equals("0")).collect(Collectors.toList());
        woi.setOfflineFriendCount(offLineFriendList.size());
        woi.setCommonMap(buildCommonData());
        woi.setCurrentOnlineUserInfo(currentOnlineUserInfo);

        // 房间用户列表
        List<String> roomUserList = ZhddWebSocket.getRoomClientsUserList(roomName);
        woi.setRoomUserList(roomUserList);
        woi.setRoomUserCount(roomUserList.size());

        return Result.ok(woi);
    }

    /**
     * 分页显示所有用户信息.
     * true:存在  false:不存在
     */
    @RequestMapping(value = "showAllUser.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @Deprecated
    public Object getOnlineUsersByPage(@RequestBody WsUsersDO params) {
        int totalCount = wsUsersService.selectCount(new EntityWrapper<WsUsersDO>().ne("name", CommonConstants.ADMIN_USER));
        int numPerPage = 15;
        int curPage = 1;
        String curUser = params.getName();
        int totalPage;
        if (totalCount % numPerPage != 0) {
            totalPage = totalCount / numPerPage + 1;
        } else {
            totalPage = totalCount / numPerPage;
        }
        if (totalPage == 0) {
            totalPage = 1;
        }

        Page<WsUsersDO> queryPage = new Page<WsUsersDO>(curPage, numPerPage);
        List<WsUsersDO> userlist = new ArrayList<>();
        Page<WsUsersDO> userPage = wsUsersService.selectPage(queryPage,
                    new EntityWrapper<WsUsersDO>().ne("name", CommonConstants.ADMIN_USER).orderBy("last_login_time", false));

        if (null != userPage) {
            userlist = userPage.getRecords();
        }
        if (null != userlist && userlist.size() > 0) {
            for (WsUsersDO wu : userlist) {
                if (wu.getName().equals(curUser)) {
                    continue;
                }
                wu.setIsFriend(0);

                int isMyFriend = wsFriendsService.selectCount(new EntityWrapper<WsFriendsDO>()
                    .eq("uname", curUser).eq("fname", wu.getName()));
                if (isMyFriend > 0) {
                    wu.setIsFriend(3); //已是好友
                } else {
                    // 0:不是  1:申请中 2:被拒绝 3:申请成功
                    WsFriendsApplyDO wfa = new WsFriendsApplyDO();
                    wfa.setFromName(curUser);
                    wfa.setToName(wu.getName());
                    List<WsFriendsApplyDO> applyList = new ArrayList<>();
                    Page<WsFriendsApplyDO> wsFriendsApplyDoPage = wsFriendsApplyService.selectPage(new Page<>(curPage,
                                    numPerPage),
                        new EntityWrapper<WsFriendsApplyDO>().eq("from_name", curUser)
                        .eq("to_name", wu.getName()));
                    if (null != wsFriendsApplyDoPage) {
                        applyList = wsFriendsApplyDoPage.getRecords();
                    }
                    //List<WsFriendsApply> applyList = wsService.queryFriendsApplyList(wfa);
                    if (null == applyList || applyList.size() == 0) {
                        wu.setIsFriend(0); //去申请
                    } else if (applyList.size() == 1) {
                        Integer processStatus = applyList.get(0).getProcessStatus();
                        wu.setIsFriend(processStatus); // 1:申请中 2:被拒绝 3:申请成功
                    } else {
                        // 过滤掉被驳回的记录
                        for (WsFriendsApplyDO temp :  applyList) {
                            if (temp.getProcessStatus() == 2) {
                                continue;
                            }
                            wu.setIsFriend(temp.getProcessStatus());
                        }
                    }
                }
            }
        }
        PageResponseEntity rqe = new PageResponseEntity();
        rqe.setTotalCount(totalCount);
        rqe.setTotalPage(totalPage);
        rqe.setList(userlist);
        rqe.setParameter1(0);
        return JsonUtil.javaobject2Jsonobject(rqe);
    }

    /**
     *分页 获取好友申请列表.
     * true:存在  false:不存在
     */
    @RequestMapping(value = "getFriendsApplyList.json", method = RequestMethod.GET)
    @ResponseBody
    public Object getFriendsApplyList(@RequestParam("curPage") int curPage,
                                      @RequestParam("numPerPage") int numPerPage,
                                      @RequestParam("curUser") String curUser) {
        int totalCount = wsFriendsApplyService.selectCount(new EntityWrapper<WsFriendsApplyDO>().eq("to_name", curUser));
        int totalPage;
        if (totalCount % numPerPage != 0) {
            totalPage = totalCount / numPerPage + 1;
        } else {
            totalPage = totalCount / numPerPage;
        }
        if (totalPage == 0) {
            totalPage = 1;
        }

        List<WsFriendsApplyDO> userlist = new ArrayList<>();
        Page<WsFriendsApplyDO> wsFriendsApplyDoPage = wsFriendsApplyService.selectPage(
                new Page<WsFriendsApplyDO>(curPage, numPerPage),
                new EntityWrapper<WsFriendsApplyDO>().eq("to_name", curUser));
        if (null != wsFriendsApplyDoPage) {
            userlist = wsFriendsApplyDoPage.getRecords();
        }

        PageResponseEntity rqe = new PageResponseEntity();
        rqe.setTotalCount(totalCount);
        rqe.setTotalPage(totalPage);
        rqe.setList(userlist);
        rqe.setParameter1(0);

        return JsonUtil.javaobject2Jsonobject(rqe);
    }

    /**
     *分页 我的申请记录.
     * true:存在  false:不存在
     */
    @RequestMapping(value = "getMyApplyList.json", method = RequestMethod.GET)
    @ResponseBody
    public Object getMyApplyList(@RequestParam("curPage") int curPage,
                                 @RequestParam("numPerPage") int numPerPage,
                                 @RequestParam("curUser") String curUser) {
        int totalCount = wsFriendsApplyService.selectCount(new EntityWrapper<WsFriendsApplyDO>().eq("to_name", curUser));
        int totalPage;
        if (totalCount % numPerPage != 0) {
            totalPage = totalCount / numPerPage + 1;
        } else {
            totalPage = totalCount / numPerPage;
        }
        if (totalPage == 0) {
            totalPage = 1;
        }

        List<WsFriendsApplyDO> userlist = new ArrayList<>();
        Page<WsFriendsApplyDO> wsFriendsApplyDoPage = wsFriendsApplyService.selectPage(
                new Page<WsFriendsApplyDO>(curPage, numPerPage),
                 new EntityWrapper<WsFriendsApplyDO>().eq("from_name", curUser));
        if (null != wsFriendsApplyDoPage) {
            userlist = wsFriendsApplyDoPage.getRecords();
        }

        PageResponseEntity rqe = new PageResponseEntity();
        rqe.setTotalCount(totalCount);
        rqe.setTotalPage(totalPage);
        rqe.setList(userlist);
        rqe.setParameter1(0);

        return JsonUtil.javaobject2Jsonobject(rqe);
    }

    /**
     * 添加好友申请.
     *
     */
    @RequestMapping(value = "addFriend.do", method = RequestMethod.POST)
    @ResponseBody
    public String addFriend(@RequestParam("fromUserName")String fromUserName,
                                         @RequestParam("toUserId")Integer toUserId) {
        Integer fromUserId = querySpecityUserName(fromUserName).getId();
        String toUserName = querySpecityUserId(toUserId).getName();

        logger.info(fromUserName + "申请添加" + toUserName + "为好友");
        int existCount = wsFriendsService.selectCount(new EntityWrapper<WsFriendsDO>().eq("uname", fromUserName)
                .eq("fname", toUserName));
        if (existCount <= 0) {
            WsFriendsApplyDO wfa = new WsFriendsApplyDO();
            wfa.setFromId(fromUserId);
            wfa.setFromName(fromUserName);
            wfa.setToId(toUserId);
            wfa.setToName(toUserName);
            wfa.setProcessStatus(1);
            wsFriendsApplyService.insert(wfa);
        } else {
            logger.info(toUserName + "已是你的好友了,无需再次申请");
        }
        return CommonConstants.SUCCESS;
    }

    /**
     * 同意好友申请.
     *
     */
    @RequestMapping(value = "agreeFriend.do", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public String agreeFriend(@RequestParam("recordId")Integer recordId) {
        WsFriendsApplyDO wfa = wsFriendsApplyService.selectById(recordId);
        if (null == wfa) {
            return CommonConstants.FAIL;
        }
        if (wfa.getProcessStatus().intValue() != 1) {
            return CommonConstants.FAIL;
        }

        wfa.setProcessStatus(3);
        boolean updateWfaFlag = wsFriendsApplyService.updateById(wfa);
        if (updateWfaFlag) {
            WsFriendsDO wf1 = new WsFriendsDO();
            wf1.setUid(wfa.getFromId());
            wf1.setUname(wfa.getFromName());
            wf1.setFid(wfa.getToId());
            wf1.setFname(wfa.getToName());
            int isExist1 = wsFriendsService.selectCount(new EntityWrapper<WsFriendsDO>()
                .eq("uname", wfa.getFromName()).eq("fname", wfa.getToName()));
            if (isExist1 <= 0) {
                wsFriendsService.insert(wf1);
            }

            WsFriendsDO wf2 = new WsFriendsDO();
            wf2.setUid(wfa.getToId());
            wf2.setUname(wfa.getToName());
            wf2.setFid(wfa.getFromId());
            wf2.setFname(wfa.getFromName());
            int isExist2 = wsFriendsService.selectCount(new EntityWrapper<WsFriendsDO>()
                    .eq("uname", wfa.getToName()).eq("fname", wfa.getFromName()));
            if (isExist2 <= 0) {
                wsFriendsService.insert(wf2);
            }
        }
        return CommonConstants.SUCCESS;
    }

    /**
     * 拒绝好友申请.
     *
     */
    @RequestMapping(value = "deagreeFriend.do", method = RequestMethod.POST)
    @ResponseBody
    public String deagreeFriend(@RequestParam("recordId")Integer recordId) {
        WsFriendsApplyDO wfa = wsFriendsApplyService.selectById(recordId);
        if (null == wfa) {
            return CommonConstants.FAIL;
        }
        if (wfa.getProcessStatus().intValue() != 1) {
            return CommonConstants.FAIL;
        }

        wfa.setProcessStatus(2);
        boolean updateFlag = wsFriendsApplyService.updateById(wfa);
        if (updateFlag) {
            return CommonConstants.SUCCESS;
        }

        return CommonConstants.FAIL;
    }

    /**
     * 删除好友.
     *
     */
    @RequestMapping(value = "deleteFriend.do", method = RequestMethod.POST)
    @ResponseBody
    public String deleteFriend(@RequestParam("id")Integer id) {
        WsFriendsDO wsFriendsDO = wsFriendsService.selectById(id);
        if (null == wsFriendsDO) {
            return CommonConstants.FAIL;
        }

        String uname = wsFriendsDO.getUname();
        String fname = wsFriendsDO.getFname();

        logger.info("uname:" + uname + "   fname:" + fname);
        if (StringUtils.isNotEmpty(uname) && StringUtils.isNotEmpty(fname)) {
            wsFriendsService.delete(new EntityWrapper<WsFriendsDO>().eq("uname", uname).eq("fname", fname));
            wsFriendsService.delete(new EntityWrapper<WsFriendsDO>().eq("uname", fname).eq("fname", uname));

            wsFriendsApplyService.delete(new EntityWrapper<WsFriendsApplyDO>().eq("from_name", fname)
                    .eq("to_name", uname));
            wsFriendsApplyService.delete(new EntityWrapper<WsFriendsApplyDO>().eq("from_name", uname)
                    .eq("to_name", fname));
        }

        return CommonConstants.SUCCESS;
    }

    /**
     * 查看朋友圈列表.
     *
     */
    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.CIRCLE, subModule = "", describe = "查看朋友圈列表")
    @PostMapping(value = "queryCircleByPage.do")
    @ResponseBody
    public Result<Page<WsCircleDO>> queryCircleList(int curPage, int numPerPage) {
        Page<WsCircleDO> qryPage = new Page<WsCircleDO>(curPage, numPerPage);

        Page<WsCircleDO> page = wsCircleService.selectPage(qryPage, new EntityWrapper<WsCircleDO>().orderBy("create_time", false));
        List<WsCircleDO> circleList = page.getRecords();
        Map<Integer, String> headImgMap = new HashMap<>();
        if (null != circleList && circleList.size() > 0) {
            for (WsCircleDO wc : circleList) {
                if (!headImgMap.containsKey(wc.getUserId())) {
                    WsUserProfileDO wsUserProfileDO = wsUserProfileService.selectOne(new EntityWrapper<WsUserProfileDO>()
                            .eq("user_id", wc.getUserId()));
                    if (null != wsUserProfileDO) {
                        headImgMap.put(wc.getUserId(), wsUserProfileDO.getImg());
                    }
                }

                //头像
                wc.setHeadImg(headImgMap.get(wc.getUserId()));
                List<WsCircleCommentDO> commentList = wsCircleCommentService.selectList(new EntityWrapper<WsCircleCommentDO>()
                        .eq("circle_id", wc.getId())
                        .orderBy("create_time", false));
                if (null == commentList) {
                    wc.setCommentList(new ArrayList<>());
                } else {
                    wc.setCommentList(commentList);
                }
            }
        }

        return Result.ok(page);
    }

    /**
     * 评论朋友圈.
     *
     */
    @OperationLogAnnotation(type = OperationEnum.INSERT, module = ModuleEnum.CIRCLE, subModule = "", describe = "评论朋友圈")
    @RequestMapping(value = "toComment.do", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> toComment(@RequestParam("user")String user,
                                    @RequestParam("circleId")Integer circleId,
                                    @RequestParam("comment")String comment) {
        WsCircleDO wsCircleDO = wsCircleService.selectById(circleId);
        if (null == wsCircleDO) {
            return Result.fail();
        }

        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
        if (null == wsUsersDO) {
            return Result.fail();
        }

        WsCircleCommentDO wcc = new WsCircleCommentDO();
        wcc.setCircleId(circleId);
        wcc.setUserId(wsUsersDO.getId());
        wcc.setUserName(wsUsersDO.getName());
        wcc.setComment(comment);
        wcc.setCreateTime(new Date());
        boolean insertFlag = wsCircleCommentService.insert(wcc);
        if (insertFlag) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 新增朋友圈.
     *
     */
    @OperationLogAnnotation(type = OperationEnum.INSERT, module = ModuleEnum.CIRCLE, subModule = "", describe = "新增朋友圈")
    @RequestMapping(value = "addCircle.do", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public Result<String> addCircle(@RequestParam("user")String user,
                            @RequestParam("content")String content,
                            @RequestParam(value = "circleImgFile1", required = false) String circleImgFile1,
                            @RequestParam(value = "circleImgFile2", required = false) String circleImgFile2,
                            @RequestParam(value = "circleImgFile3", required = false) String circleImgFile3,
                            @RequestParam(value = "circleImgFile4", required = false) String circleImgFile4) {
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
        if (null == wsUsersDO) {
            return Result.fail();
        }
        //增加积分
        Date dayStart = TimeUtil.getDayStart(new Date());
        Date dayEnd = TimeUtil.getDayEnd(new Date());
        int circleCnt = wsCircleService.selectCount(new EntityWrapper<WsCircleDO>().eq("user_id", wsUsersDO.getId())
                .ge("create_time", dayStart).le("create_time", dayEnd));
        if (circleCnt == 0) {
            wsUsersDO.setCoinNum(wsUsersDO.getCoinNum() + 15);
            wsUsersService.updateById(wsUsersDO);
        }

        WsCircleDO wc = new WsCircleDO();
        wc.setUserName(wsUsersDO.getName());
        wc.setUserId(wsUsersDO.getId());
        wc.setContent(content);
        wc.setCreateTime(new Date());
        wc.setLikeNum(0);
        wc.setPic1(circleImgFile1);
        wc.setPic2(circleImgFile2);
        wc.setPic3(circleImgFile3);
        wc.setPic4(circleImgFile4);
        boolean insertFlag = wsCircleService.insert(wc);
        if (insertFlag) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 点赞朋友圈.
     *
     */
    @OperationLogAnnotation(type = OperationEnum.UPDATE, module = ModuleEnum.CIRCLE, subModule = "", describe = "点赞朋友圈")
    @RequestMapping(value = "toLike.do", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> toLike(@RequestParam("user")String user, @RequestParam("circleId")Integer circleId) {
        WsCircleDO wsCircleDO = wsCircleService.selectById(circleId);
        if (null == wsCircleDO) {
            return Result.fail();
        }
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
        if (null == wsUsersDO) {
            return Result.fail();
        }
        int likeNum = wsCircleDO.getLikeNum();
        wsCircleDO.setLikeNum(likeNum + 1);
        boolean updateFlag = wsCircleService.updateById(wsCircleDO);
        if (updateFlag) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 点踩朋友圈.
     *
     */
    @OperationLogAnnotation(type = OperationEnum.UPDATE, module = ModuleEnum.CIRCLE, subModule = "", describe = "点赞朋友圈")
    @RequestMapping(value = "toDislike.do", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> toDislike(@RequestParam("user")String user, @RequestParam("circleId")Integer circleId) {
        WsCircleDO wsCircleDO = wsCircleService.selectById(circleId);
        if (null == wsCircleDO) {
            return Result.fail();
        }
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
        if (null == wsUsersDO) {
            return Result.fail();
        }
        int dislikeNum = wsCircleDO.getDislikeNum();
        wsCircleDO.setDislikeNum(dislikeNum + 1);
        boolean updateFlag = wsCircleService.updateById(wsCircleDO);
        if (updateFlag) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 删除朋友圈.
     *
     */
    @OperationLogAnnotation(type = OperationEnum.DELETE, module = ModuleEnum.CIRCLE, subModule = "", describe = "删除朋友圈")
    @RequestMapping(value = "toDeleteCircle.do", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> toDeleteCircle(@RequestParam("id")Integer id) {
        WsCircleDO wsCircleDO = wsCircleService.selectById(id);
        if (null == wsCircleDO) {
            return Result.fail();
        }
        boolean deleteFlag = wsCircleService.deleteById(id);
        if (deleteFlag) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 删除评论.
     *
     */
    @OperationLogAnnotation(type = OperationEnum.DELETE, module = ModuleEnum.CIRCLE, subModule = "", describe = "删除朋友圈评论")
    @RequestMapping(value = "toDeleteComment.do", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> toDeleteComment(@RequestParam("id")Integer id) {
        WsCircleCommentDO wsCircleCommentDO = wsCircleCommentService.selectById(id);
        if (null == wsCircleCommentDO) {
            return Result.fail();
        }
        boolean deleteFlag = wsCircleCommentService.deleteById(id);
        if (deleteFlag) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 获取我的好友列表.
     * true:存在  false:不存在
     */
    @RequestMapping(value = "getMyFriendsList.json", method = RequestMethod.GET)
    @ResponseBody
    public Object getMyFriendsList(@RequestParam("curPage") int curPage,
                                   @RequestParam("numPerPage") int numPerPage,
                                   @RequestParam("curUser") String curUser) {
        int totalCount = wsFriendsService.selectCount(new EntityWrapper<WsFriendsDO>().eq("uname", curUser));
        int totalPage;
        if (totalCount % numPerPage != 0) {
            totalPage = totalCount / numPerPage + 1;
        } else {
            totalPage = totalCount / numPerPage;
        }
        if (totalPage == 0) {
            totalPage = 1;
        }

        List<WsFriendsDO> userlist = new ArrayList<>();
        Page<WsFriendsDO> friendsPage = wsFriendsService.selectPage(new Page<>(curPage, numPerPage),
                    new EntityWrapper<WsFriendsDO>().eq("uname", curUser).orderBy("create_time", false));
        if (null != friendsPage) {
            userlist = friendsPage.getRecords();
        }
        PageResponseEntity rqe = new PageResponseEntity();
        rqe.setTotalCount(totalCount);
        rqe.setTotalPage(totalPage);
        rqe.setList(userlist);
        rqe.setParameter1(0);

        return JsonUtil.javaobject2Jsonobject(rqe);
    }

    /**
     * 设置个人信息.
     *
     */
    @RequestMapping(value = "setPersonInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public String setPersonInfo(@RequestParam(value = "userName") String userName,
                                @RequestParam(value = "realName", required = false) String realName,
                                @RequestParam(value = "headImg", required = false) String headImg,
                                @RequestParam(value = "sign", required = false) String sign,
                                @RequestParam(value = "age", required = false) Integer age,
                                @RequestParam(value = "sex", required = false) Integer sex,
                                @RequestParam(value = "sexText", required = false) String sexText,
                                @RequestParam(value = "tel", required = false) String tel,
                                @RequestParam(value = "address", required = false) String address,
                                @RequestParam(value = "profession", required = false) Integer profession,
                                @RequestParam(value = "professionText", required = false) String professionText,
                                @RequestParam(value = "hobby", required = false) Integer hobby,
                                @RequestParam(value = "hobbyText", required = false) String hobbyText
    ) {
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", userName));
        try {
            // 检查表中是否有个人信息记录
            WsUserProfileDO wsUserProfileDO = wsUserProfileService.selectOne(new EntityWrapper<WsUserProfileDO>()
                    .eq("user_id", wsUsersDO.getId()));
            if (null == wsUserProfileDO) {
                logger.info("插入个人信息");
                WsUserProfileDO wup = new WsUserProfileDO();
                wup.setUserId(wsUsersDO.getId());
                wup.setUserName(userName);
                wup.setRealName(realName);
                wup.setImg(headImg);
                wup.setSign(sign);
                wup.setAge(age);
                wup.setSex(sex);
                wup.setSexText(sexText);
                wup.setTel(tel);
                wup.setAddress(address);
                wup.setProfession(profession);
                wup.setProfessionText(professionText);
                wup.setHobby(hobby);
                wup.setHobbyText(hobbyText);
                wsUserProfileService.insert(wup);
            } else {
                logger.info("更新个人信息");
                wsUserProfileDO.setUserName(userName);
                wsUserProfileDO.setRealName(realName);
                wsUserProfileDO.setImg(headImg);
                wsUserProfileDO.setSign(sign);
                wsUserProfileDO.setAge(age);
                wsUserProfileDO.setSex(sex);
                wsUserProfileDO.setSexText(sexText);
                wsUserProfileDO.setTel(tel);
                wsUserProfileDO.setAddress(address);
                wsUserProfileDO.setProfession(profession);
                wsUserProfileDO.setProfessionText(professionText);
                wsUserProfileDO.setHobby(hobby);
                wsUserProfileDO.setHobbyText(hobbyText);
                wsUserProfileService.updateById(wsUserProfileDO);
            }
        } catch (Exception e) {
            logger.error("更新个人信息失败!" + e.getMessage());
            return CommonConstants.FAIL;
        }
        return CommonConstants.SUCCESS;
    }

    /**
     * 查询个人信息.
     *
     * @return 用户信息
     */
    @RequestMapping(value = "queryPersonInfo.json", method = RequestMethod.POST)
    @ResponseBody
    public Object queryPersonInfo(@RequestParam("user") String user) {
        WsUserProfileDO wsUserProfileDO = wsUserProfileService.selectOne(new EntityWrapper<WsUserProfileDO>()
                    .eq("user_name", user));
        if (null != wsUserProfileDO) {
            return JsonUtil.javaobject2Jsonobject(wsUserProfileDO);
        }

        return CommonConstants.FAIL;
    }

    /**
     * 查询软件版本.
     *
     * @return 软件版本
     */
    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.OTHER, subModule = "", describe = "查询软件版本")
    @RequestMapping(value = "queryVersion.json", method = RequestMethod.GET)
    @ResponseBody
    public Object queryVersion() {
        return configMap.get("version");
    }

    /**
     * 检查版本更新.
     * @return 版本更新结果
     */
    @OperationLogAnnotation(type = OperationEnum.UPDATE, module = ModuleEnum.OTHER, subModule = "", describe = "检查版本更新")
    @RequestMapping(value = "checkUpdate.do", method = RequestMethod.GET)
    @ResponseBody
    public Object checkUpdate(@RequestParam("version")String curVersion, @RequestParam("cmd")String cmd) {
        String shellRootPath = "/root/update/checkupdate.sh";

        try {
            Process ps = Runtime.getRuntime().exec(shellRootPath + " " + curVersion + " " + cmd);
            ps.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            System.out.println(result);
            return result;
        } catch (Exception e) {
            logger.error("检查/升级版本失败:" + e.getMessage());
            return "操作失败!可能当前系统是非linux系统";
        }
    }

    private Map<String, List<WsCommonDO>> buildCommonData() {
        Map<String, List<WsCommonDO>> commonMap = new HashMap<>();
        List<WsCommonDO> commonList = wsCommonService.selectList(null);

        for (WsCommonDO common : commonList) {
            String type = common.getType();
            String name = common.getName();

            if (CommonUtil.validateEmpty(type) || CommonUtil.validateEmpty(name)) {
                continue;
            }

            if (commonMap.containsKey(type)) {
                List<WsCommonDO> tmpCommonList = commonMap.get(type);
                tmpCommonList.add(common);
            } else {
                List<WsCommonDO> tmpDicList = new ArrayList<>();
                tmpDicList.add(common);
                commonMap.put(type, tmpDicList);
            }
        }

        return commonMap;
    }

    /**
     * 根据用户名称查询用户信息.
     *
     * @param name 用户名称
     * @return 用户信息
     */
    private WsUsersDO querySpecityUserName(String name) {
        return wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", name));
    }

    /**
     * 根据用户名称查询用户id.
     *
     * @return 用户信息
     */
    private WsUsersDO querySpecityUserId(Integer id) {
        return wsUsersService.selectById(id);
    }

    /**
     * 移除kookie.
     * @param request 请求
     * @param response 响应
     */
    @SuppressWarnings("unused")
    private void removeCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CommonConstants.S_USER) || cookie.getName().equals(CommonConstants.S_PASS)) {
                    if (cookie.getMaxAge() != 0) {
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    }
                }
            }
        }
    }

    /**
     * 获取好友列表列表数据.
     */
    @ResponseBody
    @GetMapping("/myFriendsList")
    public Result<Page<WsFriendsDO>> list(WsFriendsDO wsFriendsDto) {
        Wrapper<WsFriendsDO> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotBlank(wsFriendsDto.getUname())) {
            wrapper.eq("uname", wsFriendsDto.getUname());
        }
        Page<WsFriendsDO> qryPage = getPage(WsFriendsDO.class);
        Page<WsFriendsDO> page = wsFriendsService.selectPage(qryPage, wrapper);
        return Result.ok(page);
    }

    /**
     * 查询会话信息.
     * @param user 用户id
     * @param request 请求
     * @return 会话信息
     */
    @ResponseBody
    @GetMapping("/querySessionData")
    public String querySessionData(String user, HttpServletRequest request) {
        logger.info("查询session数据, user:{}", user);
        Object sessionUser = "";
        Object sessionPass = "";
        try {
            SessionInfoBean sessionInfoBean = (SessionInfoBean) request.getSession().getAttribute(CommonConstants.SESSION_INFO);
            sessionUser = sessionInfoBean == null ? "" : sessionInfoBean.getUser();
            sessionPass = sessionInfoBean == null ? "" : sessionInfoBean.getPassword();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("sessionUser:" + sessionUser);
        logger.info("sessionPass:" + sessionPass);

        return sessionUser + ":" + sessionPass;
    }

    /**
     * 智能助手.
     */
    @GetMapping("intelAssistant.page")
    String intelAssistantPage(Model model, String user) {
        model.addAttribute("user", user);
        return "ws/intelAssistant";
    }

    /**
     * 简易聊天室.
     */
    @GetMapping("wsSimpleChat.page")
    String wsSimpleChat(Model model) {
        return "ws/wsSimpleChat";
    }

    /**
     * 与机器人聊天.
     *
     * @param text 人类聊天内容
     * @return 回复内容
     */
    @ResponseBody
    @GetMapping("/robotChat")
    public String robotChat(String text) throws InterruptedException {
        for (Map.Entry<String, String> entry: chatMappingMap.entrySet()) {
            String key = entry.getKey();
            if (key.contains(text) || text.contains(key)) {
                long sleepTimes = (long) (Math.random() * 150 + 20);
                System.out.println(sleepTimes);
                Thread.sleep(sleepTimes);
                return entry.getValue();
            }
        }
        return "主人,我没听懂";
    }

    /**
     * 网易新闻.
     * @return 新闻页面
     */
    @RequestMapping("/wangyiNews.page")
    public String wangyiNews() {
        return "ws/wangyiNews";
    }

    /**
     * 网易: https://3g.163.com
     * 新闻：/touch/reconstruct/article/list/BBM54PGAwangning/0-10.html
     * 娱乐：/touch/reconstruct/article/list/BA10TA81wangning/0-10.html
     * 体育：/touch/reconstruct/article/list/BA8E6OEOwangning/0-10.html
     * 财经：/touch/reconstruct/article/list/BA8EE5GMwangning/0-10.html
     * 军事：/touch/reconstruct/article/list/BAI67OGGwangning/0-10.html
     * 科技：/touch/reconstruct/article/list/BA8D4A3Rwangning/0-10.html
     * 手机：/touch/reconstruct/article/list/BAI6I0O5wangning/0-10.html
     * 数码：/touch/reconstruct/article/list/BAI6JOD9wangning/0-10.html
     * 时尚：/touch/reconstruct/article/list/BA8F6ICNwangning/0-10.html
     * 游戏：/touch/reconstruct/article/list/BAI6RHDKwangning/0-10.html
     * 教育：/touch/reconstruct/article/list/BA8FF5PRwangning/0-10.html
     * 健康：/touch/reconstruct/article/list/BDC4QSV3wangning/0-10.html
     * 旅游：/touch/reconstruct/article/list/BEO4GINLwangning/0-10.html
     * 视频：/touch/nc/api/video/recommend/Video_Recom/0-10.do?callback=videoList
     * @return url地址
     */
    @RequestMapping("/queryWangyiNewsType")
    @ResponseBody
    public Result<List<WangyiNewsBean>> queryWangyiNewsType() {
        String baseUrl = "https://3g.163.com";
        String branchUrlFormat = baseUrl + "/touch/reconstruct/article/list/%s";
        List<WangyiNewsBean> list = new ArrayList<>();
        WangyiNewsBean wnb1 = new WangyiNewsBean("新闻", "BBM54PGAwangning", String.format(branchUrlFormat, "BBM54PGAwangning"));
        WangyiNewsBean wnb2 = new WangyiNewsBean("娱乐", "BA10TA81wangning", String.format(branchUrlFormat, "BA10TA81wangning"));
        WangyiNewsBean wnb3 = new WangyiNewsBean("体育", "BA8E6OEOwangning", String.format(branchUrlFormat, "BA8E6OEOwangning"));
        WangyiNewsBean wnb4 = new WangyiNewsBean("财经", "BA8EE5GMwangning", String.format(branchUrlFormat, "BA8EE5GMwangning"));
        WangyiNewsBean wnb5 = new WangyiNewsBean("军事", "BAI67OGGwangning", String.format(branchUrlFormat, "BAI67OGGwangning"));
        WangyiNewsBean wnb6 = new WangyiNewsBean("科技", "BA8D4A3Rwangning", String.format(branchUrlFormat, "BA8D4A3Rwangning"));
        WangyiNewsBean wnb7 = new WangyiNewsBean("手机", "BAI6I0O5wangning", String.format(branchUrlFormat, "BAI6I0O5wangning"));
        WangyiNewsBean wnb8 = new WangyiNewsBean("数码", "BAI6JOD9wangning", String.format(branchUrlFormat, "BAI6JOD9wangning"));
        WangyiNewsBean wnb9 = new WangyiNewsBean("时尚", "BA8F6ICNwangning", String.format(branchUrlFormat, "BA8F6ICNwangning"));
        WangyiNewsBean wnb10 = new WangyiNewsBean("游戏", "BAI6RHDKwangning", String.format(branchUrlFormat, "BAI6RHDKwangning"));
        WangyiNewsBean wnb11 = new WangyiNewsBean("教育", "BA8FF5PRwangning", String.format(branchUrlFormat, "BA8FF5PRwangning"));
        WangyiNewsBean wnb12 = new WangyiNewsBean("健康", "BDC4QSV3wangning", String.format(branchUrlFormat, "BDC4QSV3wangning"));
        WangyiNewsBean wnb13 = new WangyiNewsBean("旅游", "BEO4GINLwangning", String.format(branchUrlFormat, "BEO4GINLwangning"));
        list.add(wnb1);
        list.add(wnb2);
        list.add(wnb3);
        list.add(wnb4);
        list.add(wnb5);
        list.add(wnb6);
        list.add(wnb7);
        list.add(wnb8);
        list.add(wnb9);
        list.add(wnb10);
        list.add(wnb11);
        list.add(wnb12);
        list.add(wnb13);

        return Result.ok(list);
    }
}
