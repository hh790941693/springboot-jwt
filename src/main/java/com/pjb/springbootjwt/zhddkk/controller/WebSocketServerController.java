package com.pjb.springbootjwt.zhddkk.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.zhddkk.bean.ChatMessageBean;
import com.pjb.springbootjwt.zhddkk.bean.JsonResult;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.domain.*;
import com.pjb.springbootjwt.zhddkk.entity.PageResponseEntity;
import com.pjb.springbootjwt.zhddkk.interceptor.WsInterceptor;
import com.pjb.springbootjwt.zhddkk.service.*;
import com.pjb.springbootjwt.zhddkk.util.*;
import com.pjb.springbootjwt.zhddkk.websocket.ZhddWebSocket;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * websocket服务端控制器.
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("ws")
public class WebSocketServerController extends AdminBaseController {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerController.class);

    private static final Map<String, String> configMap = WsInterceptor.getConfigMap();

    @Autowired
    private WsOperLogService wsOperLogService;

    @Autowired
    private WsUsersService wsUsersService;

    @Autowired
    private WsAdsService wsAdsService;

    @Autowired
    private WsCommonService wsCommonService;

    @Autowired
    private WsOperationLogService wsOperationLogService;

    @Autowired
    private WsChatlogService wsChatlogService;

    @Autowired
    private WsSignService wsSignService;

    @Autowired
    private WsFileService wsFileService;

    /**
     * 让某用户下线.
     * @param user 用户id
     * @return 结果
     */
    @RequestMapping(value = "offlineUser.do")
    @ResponseBody
    public String offlineUser(@RequestParam("user") String user) {
        Map<String, Map<String,ZhddWebSocket>> socketMap = ZhddWebSocket.getClientsMap();
        for (Entry<String, Map<String, ZhddWebSocket>> entry : socketMap.entrySet()) {
            if (entry.getValue().containsKey(user)) {
                entry.getValue().remove(user);
                ZhddWebSocket.removeRoomUser(entry.getKey(), user);
            }
        }

        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
        if (null != wsUsersDO) {
            wsUsersDO.setState("0");
            wsUsersDO.setLastLogoutTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            wsUsersService.updateById(wsUsersDO);
            return CommonConstants.SUCCESS;
        }
        return CommonConstants.FAIL;
    }

    /**
     * 让某用户禁用/启用.
     * @param user 用户id
     * @return 结果
     */
    @RequestMapping(value = "enableUser.do")
    @ResponseBody
    public String enableUser(@RequestParam("user") String user, @RequestParam("enable") String enable) {
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
        if (null != wsUsersDO) {
            wsUsersDO.setEnable(enable);
            wsUsersService.updateById(wsUsersDO);
            return CommonConstants.SUCCESS;
        }
        return CommonConstants.FAIL;
    }

    /**
     * 让某用户禁言/开言.
     * @param user 用户id
     * @return 结果
     */
    @RequestMapping(value = "enableSpeak.do")
    @ResponseBody
    public String enableSpeak(@RequestParam("user") String user, @RequestParam("speak") String speak) {
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
        if (null != wsUsersDO) {
            wsUsersDO.setSpeak(speak);
            wsUsersService.updateById(wsUsersDO);
            return CommonConstants.SUCCESS;
        }
        return CommonConstants.FAIL;
    }

    /**
     * 给指定用户的发送信息.
     * @param user 用户id
     * @return 结果
     */
    @RequestMapping(value = "sendText.do")
    @ResponseBody
    public String sendText(@RequestParam("user") String user, @RequestParam("msg") String message) {
        String result = "offline";

        String msgFrom = message.split("from:")[1].split(";")[0].trim();
        String msgTo = message.split("to:")[1].split(";")[0].trim();
        String typeId = message.split("typeId:")[1].split(";")[0].trim();
        String typeDesc = message.split("typeDesc:")[1].split(";")[0].trim();
        String msgStr = message.split("msg:")[1].trim();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String curTime = sdf.format(new Date());
        ChatMessageBean chatBean = new ChatMessageBean(curTime, typeId, typeDesc, msgFrom, msgTo, msgStr);

        // 在线人数
        Map<String, Map<String, ZhddWebSocket>> socketMap = ZhddWebSocket.getClientsMap();
        for (Entry<String, Map<String, ZhddWebSocket>> entry : socketMap.entrySet()) {
            if (entry.getValue().containsKey(user)) {
                try {
                    entry.getValue().get(user).getSession().getBasicRemote().sendText(chatBean.toString());
                    result = CommonConstants.SUCCESS;
                } catch (IOException e) {
                    e.printStackTrace();
                    result = CommonConstants.FAIL;
                }
            }
        }

        return result;
    }


    /**
     * 聊天记录监控页面.
     *
     */
    @RequestMapping(value = "wsserverChartMonitor.page", method = RequestMethod.GET)
    public String chatLogMonitor(Model model) {
        logger.debug("访问wsserverChartMonitor.page");
        model.addAttribute("webserverip", configMap.get(CommonConstants.S_WEBSERVERIP));
        model.addAttribute("webserverport", configMap.get(CommonConstants.S_WEBSERVERPORT));
        return "ws/wsserverChartMonitor";
    }

    /**
     * 发布广告.
     *
     * @param adTitle 标题
     * @param adContent 内容
     * @param req 请求
     */
    @RequestMapping(value = "addAd.do", method = RequestMethod.POST)
    @ResponseBody
    public String addAd(@RequestParam("adTitle") String adTitle,
                        @RequestParam("adContent") String adContent,
                        HttpServletRequest req) {

        // 接收人列表
        List<String> receiveList = new ArrayList<>();
        boolean result = true;
        if (CommonUtil.validateEmpty(adTitle) && CommonUtil.validateEmpty(adContent)) {
            req.setAttribute("result", false);
            result = false;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String curTime = sdf.format(new Date());

            Map<String, Map<String, ZhddWebSocket>> socketMap = ZhddWebSocket.getClientsMap();
            for (Entry<String, Map<String, ZhddWebSocket>> outerEntry : socketMap.entrySet()) {
                Map<String, ZhddWebSocket> roomClientMap = outerEntry.getValue();
                for (Entry<String, ZhddWebSocket> innerEntry : outerEntry.getValue().entrySet()) {
                    if (innerEntry.getKey().equals(CommonConstants.ADMIN_USER)) {
                        continue;
                    }
                    try {
                        ChatMessageBean chatBean = new ChatMessageBean(curTime, "4", "广告消息",
                                CommonConstants.ADMIN_USER, innerEntry.getKey(), "title:" + adTitle + ";content:" + adContent);
                        innerEntry.getValue().getSession().getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
                        receiveList.add(innerEntry.getKey());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            // 插入广告记录
            WsAdsDO wsAdsDO = new WsAdsDO();
            wsAdsDO.setTitle(adTitle);
            wsAdsDO.setContent(adContent);
            wsAdsDO.setReceiveList(receiveList.toString());
            result = wsAdsService.insert(wsAdsDO);
        }
        if (result) {
            return CommonConstants.SUCCESS;
        } else {
            return CommonConstants.FAIL;
        }
    }

    /**
     * 分页查询公告列表.
     * true:存在  false:不存在
     */
    @RequestMapping(value = "getAdsListByPage.json", method = RequestMethod.GET)
    @ResponseBody
    public Object getAdsListByPage(@RequestParam("curPage") int curPage, @RequestParam("numPerPage") int numPerPage) {
        int totalCount = wsAdsService.selectCount(null);
        int totalPage;
        if (totalCount % numPerPage != 0) {
            totalPage = totalCount / numPerPage + 1;
        } else {
            totalPage = totalCount / numPerPage;
        }

        int start;
        if (curPage == 1) {
            start = 0;
        } else {
            start = (curPage - 1) * numPerPage;
        }
        List<WsAdsDO> adslist = wsAdsService.selectPage(new Page<>(start, numPerPage)).getRecords();
        PageResponseEntity rqe = new PageResponseEntity();
        rqe.setTotalCount(totalCount);
        rqe.setTotalPage(totalPage);
        rqe.setList(adslist);
        return JsonUtil.javaobject2Jsonobject(rqe);
    }

    /**
     * 分页查询用户信息.
     * true:存在  false:不存在
     */
    @RequestMapping(value = "getOnlineUsersByPage.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getOnlineUsersByPage() {
        int totalCount = wsUsersService.selectCount(null);
        int totalPage;
        int numPerPage = 15;
        int curPage = 1;
        if (totalCount % numPerPage != 0) {
            totalPage = totalCount / numPerPage + 1;
        } else {
            totalPage = totalCount / numPerPage;
        }

        int start;
        if (curPage == 1) {
            start = 0;
        } else {
            start = (curPage - 1) * numPerPage;
        }
        List<WsUsersDO> userList = wsUsersService.selectPage(new Page<>(start, numPerPage)).getRecords();
        PageResponseEntity rqe = new PageResponseEntity();
        rqe.setTotalCount(totalCount);
        rqe.setTotalPage(totalPage);
        rqe.setList(userList);
        rqe.setParameter1(0);

        return JsonUtil.javaobject2Jsonobject(rqe);
    }

    /**
     * 获取用户聊天记录  分页查询.
     * getChatLogByPage.json
     */
    @RequestMapping(value = "getChatLogByPage.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getChatLogByPage() {
        Page<WsChatlogDO> page = wsChatlogService.selectPage(new Page<>(1, 15), null);
        int totalCount = wsChatlogService.selectCount(null);
        PageResponseEntity rqe = new PageResponseEntity();
        rqe.setTotalCount(totalCount);
        rqe.setTotalPage(10);
        rqe.setList(page.getRecords());

        return rqe;
    }

    /**
     * 获取操作日志列表  分页查询.
     * getOperationLogByPage.json
     */
    @RequestMapping(value = "getOperationLogByPage.json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getOperationLogByPage() {
        Page<WsOperationLogDO> page = wsOperationLogService.selectPage(new Page<>(1, 15), null);
        int totalCount = wsOperationLogService.selectCount(null);
        PageResponseEntity rqe = new PageResponseEntity();
        rqe.setTotalCount(totalCount);
        rqe.setTotalPage(10);
        rqe.setList(page.getRecords());

        return rqe;
    }

    /**
     * 获取操作日志列表  分页查询.
     * getOperationLogByPage.json
     */
    @RequestMapping(value = "getOperationLogByPageByBootstrap.json", method = RequestMethod.POST)
    @ResponseBody
    public Object getOperationLogByPageByBootstrap() {
        Page<WsOperationLogDO> page = wsOperationLogService.selectPage(new Page<>(1, 15), null);
        int totalCount = wsOperationLogService.selectCount(null);
        JsonResult jr = new JsonResult();
        jr.setTotal(totalCount);
        jr.setRows(page.getRecords());
        return JsonUtil.javaobject2Jsonobject(jr);
    }

    /**
     * 清空操作日志.
     * @return 清理结果
     */
    @PostMapping("clearOperationLog.do")
    @ResponseBody
    public String clearOperationLog() {
        wsOperLogService.delete(null);
        return CommonConstants.SUCCESS;
    }

    /**
     * 敏感词/脏话  分页查询.
     * getChatLogByPage.json
     */
    @RequestMapping(value = "getCommonByPage.json", method = RequestMethod.POST,
            produces = "application/json")
    @ResponseBody
    public Object getCommonByPage(@RequestBody WsCommonDO params) {
        Page<WsCommonDO> page = wsCommonService.selectPage(
                new Page<WsCommonDO>(1, 15),
                new EntityWrapper<WsCommonDO>().eq("type", params.getType()));
        List<WsCommonDO> wsCommonList = new ArrayList<>();
        if (null != page) {
            wsCommonList = page.getRecords();
        }
        return wsCommonList;
    }

    @RequestMapping(value = "addCommonItem.do",
            method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String addCommonItem(@RequestBody WsCommonDO params) {
        wsCommonService.insert(params);
        return CommonConstants.SUCCESS;
    }

    @RequestMapping(value = "deleteCommonItem.do", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String deleteCommonItem(@RequestBody WsCommonDO params) {
        wsCommonService.deleteById(params.getId());
        return CommonConstants.SUCCESS;
    }

    @RequestMapping(value = "updateCommonItem.do", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String updateCommonItem(@RequestBody WsCommonDO params) {
        wsCommonService.updateById(params);
        return CommonConstants.SUCCESS;
    }

    /**
     * 导出用户信息.
     *
     * @param response 响应
     */
    @RequestMapping(value = "exportUser.do", method = RequestMethod.GET)
    public void exportUser(HttpServletResponse response) {
        logger.debug("开始导出用户信息");
        List<WsUsersDO> list = wsUsersService.selectList(null);
        if (null != list && list.size() > 0) {
            for (WsUsersDO wu : list) {
                String password = wu.getPassword();
                if (StringUtils.isNotEmpty(password)) {
                    String passwordDecode = SecurityAESUtil.decryptAES(password, CommonConstants.AES_PASSWORD);
                    wu.setPasswordDecode(passwordDecode);
                }
            }

            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String fileName = "wsUser".concat("_").concat(time).concat(".xls");
            ExcelUtil.exportExcel(list, "用户信息", "用户", WsUsersDO.class, fileName, response);
        } else {
            logger.debug("无需导出用户信息");
        }
        logger.debug("结束导出用户信息");
    }

    /**
     * 导出操作日志信息.
     *
     * @param response 响应
     */
    @RequestMapping(value = "exportOperateLog.do", method = RequestMethod.GET)
    public void exportOperateLog(String userName, String operModule, HttpServletResponse response) {
        logger.debug("开始导出操作日志");
        Wrapper<WsOperationLogDO> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(userName)) {
            wrapper.eq("user_name", userName);
        }
        if (StringUtils.isNotEmpty(operModule)) {
            wrapper.eq("oper_module", operModule);
        }

        List<WsOperationLogDO> list = wsOperationLogService.selectList(wrapper);
        if (null != list && list.size() > 0) {
            for (WsOperationLogDO woltmp : list) {
                if (woltmp.getOperResult().length() > 32000) {
                    woltmp.setOperResult(woltmp.getOperResult().substring(0, 32000));
                }
            }

            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String fileName = "wsOperationLog".concat("_").concat(time).concat(".xls");
            ExcelUtil.exportExcel(list, "操作日志", "操作日志", WsOperationLogDO.class, fileName, response);
        } else {
            logger.debug("无需导出操作日志");
        }
        logger.debug("结束导出操作日志");
    }

    /**
     * 领导驾驶舱.
     * @return 领导驾驶舱页面
     */
    @RequestMapping("/wsserverChart.page")
    public String wsserverChat() {
        return "ws/wsserverChart";
    }

    /**
     * 每日签到人数.
     * @return 签到人数信息
     */
    @RequestMapping("/querySignData.do")
    @ResponseBody
    public Map<String, Integer> querySignData() {
        Map<String, Integer> map = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        for (int i = 6; i >= 0; i--) {
            Date date = DateUtil.getBeforeByDayTime(new Date(), -i);
            Date dayBeginDate = DateUtil.dayBeginDate(date);
            Date dayEndDate = DateUtil.dayEndDate(date);
            String timeName = sdf.format(date);

            int count = wsSignService.selectCount(new EntityWrapper<WsSignDO>().ge("create_time", dayBeginDate)
                .le("create_time", dayEndDate));
            map.put(timeName, count);
        }

        return map;
    }

    /**
     * 每日在线人数.
     * @return 每日在线人数信息
     */
    @RequestMapping("/queryOnlineUserData.do")
    @ResponseBody
    public Map<String, Integer> queryOnlineUserData() {
        Map<String, Integer> map = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        SimpleDateFormat sdfx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 30; i >= 0; i--) {
            Date date = DateUtil.getBeforeByDayTime(new Date(), -i);
            Date dayBeginDate = DateUtil.dayBeginDate(date);
            Date dayEndDate = DateUtil.dayEndDate(date);
            String timeName = sdf.format(date);

            int count = wsChatlogService.selectCount(new EntityWrapper<WsChatlogDO>()
                    .eq("to_user", "")
                    .ge("time", sdfx.format(dayBeginDate))
                    .le("time", sdfx.format(dayEndDate)));
            map.put(timeName, count);
        }

        return map;
    }

    /**
     * 每日文件上传大小.
     * @return 每日文件上传大小信息
     */
    @RequestMapping("/queryUploadFileData.do")
    @ResponseBody
    public Map<String, Long> queryUploadFileData() {
        Map<String, Long> map = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        for (int i = 6; i >= 0; i--) {
            Date date = DateUtil.getBeforeByDayTime(new Date(), -i);
            Date dayBeginDate = DateUtil.dayBeginDate(date);
            Date dayEndDate = DateUtil.dayEndDate(date);
            String timeName = sdf.format(date);

            List<WsFileDO> wsFileList = wsFileService.selectList(new EntityWrapper<WsFileDO>()
                    .ge("create_time", dayBeginDate)
                    .le("create_time", dayEndDate));
            long fileSize = 0;
            for (WsFileDO wsFileDO : wsFileList) {
                fileSize += wsFileDO.getFileSize();
            }
            map.put(timeName, fileSize);
        }
        return map;
    }
}