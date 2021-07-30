package com.pjb.springbootjwt.zhddkk.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.bean.WangyiNewsBean;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.constants.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.constants.OperationEnum;
import com.pjb.springbootjwt.zhddkk.domain.*;
import com.pjb.springbootjwt.zhddkk.entity.WsOnlineInfo;
import com.pjb.springbootjwt.zhddkk.bean.LoadConfigFileBean;
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

    private static final Map<String, String> configMap = LoadConfigFileBean.getConfigMap();

    private static final Map<String, String> chatMappingMap = LoadConfigFileBean.getChatMappingMap();

    @Autowired
    private WsCircleService wsCircleService;

    @Autowired
    private WsCircleCommentService wsCircleCommentService;

    @Autowired
    private WsUsersService wsUsersService;

    @Autowired
    private WsUserProfileService wsUserProfileService;

    @Autowired
    private WsCommonService wsCommonService;

    /**
     * 退出.
     *
     */
    @OperationLogAnnotation(type = OperationEnum.UPDATE, module = ModuleEnum.LOGOUT, subModule = "", describe = "退出")
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> logout(HttpServletRequest request) {
        //清除session之前,先获取session中的数据
        String userId = SessionUtil.getSessionUserId();
        WsUsersDO wsUsersDO = wsUsersService.selectById(userId);

        // 销毁session
        HttpSession httpSession = request.getSession(false);
        if (null != httpSession) {
            httpSession.invalidate();
        }

        //聊天室移除人
        ZhddWebSocket.removeUserFromAllRoom(wsUsersDO.getName());

        wsUsersDO.setState("0");
        wsUsersDO.setLastLogoutTime(SDF_STANDARD.format(new Date()));
        wsUsersService.updateById(wsUsersDO);
        return Result.ok();
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
     * @return 添加朋友圈页面
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.CIRCLE, subModule = "", describe = "添加朋友圈首页")
    @RequestMapping(value = "wsclientAddCircle.page")
    public String wsclientAddCircle() {
        return "ws/wsclientAddCircle";
    }

    /**
     * 获取在线人数信息.
     *
     * @return 在线用户信息
     */
    @RequestMapping(value = "getOnlineInfo.json")
    @ResponseBody
    public Result<WsOnlineInfo> getOnlineInfo(@RequestParam(value = "roomName") String roomName) {
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
        String userId = SessionUtil.getSessionUserId();
        WsUsersDO currentOnlineUserInfo = wsUsersService.selectById(userId);
        WsUserProfileDO wsUserProfileDO = wsUserProfileService.selectOne(new EntityWrapper<WsUserProfileDO>()
                .eq("user_id", userId));
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
        List<WsUsersDO> friendsUserList = wsUsersService.queryMyFriendList(Integer.valueOf(userId));
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
        List<String> roomUserNameList = ZhddWebSocket.getRoomClientsUserList(roomName);
        List<WsUserProfileDO> roomUserProfileList = wsUserProfileService.selectList(new EntityWrapper<WsUserProfileDO>().in("user_name", roomUserNameList));
        woi.setRoomUserProfileList(roomUserProfileList);
        woi.setRoomUserCount(roomUserNameList.size());

        return Result.ok(woi);
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
    public Result<String> toComment(
                                    @RequestParam("circleId")Integer circleId,
                                    @RequestParam("comment")String comment) {
        WsCircleDO wsCircleDO = wsCircleService.selectById(circleId);
        if (null == wsCircleDO) {
            return Result.fail();
        }

        String userId = SessionUtil.getSessionUserId();
        WsUsersDO wsUsersDO = wsUsersService.selectById(userId);
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
    public Result<String> addCircle(
                            @RequestParam("content")String content,
                            @RequestParam(value = "circleImgFile1", required = false) String circleImgFile1,
                            @RequestParam(value = "circleImgFile2", required = false) String circleImgFile2,
                            @RequestParam(value = "circleImgFile3", required = false) String circleImgFile3,
                            @RequestParam(value = "circleImgFile4", required = false) String circleImgFile4) {
        String userId = SessionUtil.getSessionUserId();
        WsUsersDO wsUsersDO = wsUsersService.selectById(userId);
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
    public Result<String> toLike(@RequestParam("circleId")Integer circleId) {
        WsCircleDO wsCircleDO = wsCircleService.selectById(circleId);
        if (null == wsCircleDO) {
            return Result.fail();
        }
        String userId = SessionUtil.getSessionUserId();
        WsUsersDO wsUsersDO = wsUsersService.selectById(userId);
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
    public Result<String> toDislike(@RequestParam("circleId")Integer circleId) {
        WsCircleDO wsCircleDO = wsCircleService.selectById(circleId);
        if (null == wsCircleDO) {
            return Result.fail();
        }
        String userId = SessionUtil.getSessionUserId();
        WsUsersDO wsUsersDO = wsUsersService.selectById(userId);
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
     * 移除kookie.
     * @param request 请求
     * @param response 响应
     */
    @SuppressWarnings("unused")
    private void removeCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CommonConstants.C_USER) || cookie.getName().equals(CommonConstants.C_PASS)) {
                    if (cookie.getMaxAge() != 0) {
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    }
                }
            }
        }
    }

    /**
     * 智能助手.
     */
    @GetMapping("intelAssistant.page")
    String intelAssistantPage() {
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
     * 聊天室.
     */
    @GetMapping("wsSimpleChatRoom.page")
    String wsSimpleChatRoom(Model model) {
        return "ws/wsSimpleChatRoom";
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
