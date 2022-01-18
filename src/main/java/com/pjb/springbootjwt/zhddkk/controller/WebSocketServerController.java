package com.pjb.springbootjwt.zhddkk.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.zhddkk.domain.*;
import com.pjb.springbootjwt.zhddkk.dto.LoginHistoryDto;
import com.pjb.springbootjwt.zhddkk.service.*;
import com.pjb.springbootjwt.zhddkk.util.*;
import com.pjb.springbootjwt.zhddkk.websocket.WebSocketConfig;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    @Autowired
    private WsSignService wsSignService;

    @Autowired
    private WsFileService wsFileService;

    @Autowired
    private WsOperationLogService wsOperationLogService;

    @Autowired
    private WebSocketConfig webSocketConfig;

    /**
     * 聊天记录监控页面.
     *
     */
    @RequestMapping(value = "wsserverChartMonitor.page", method = RequestMethod.GET)
    public String chatLogMonitor(Model model) {
        logger.debug("访问wsserverChartMonitor.page");
        model.addAttribute("webserverip", webSocketConfig.getAddress());
        model.addAttribute("webserverport", webSocketConfig.getPort());
        return "ws/wsserverChartMonitor";
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
        for (int i = 30; i >= 0; i--) {
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<LoginHistoryDto> loginList = wsOperationLogService.queryOnlineUserData();
        Map<String, List<LoginHistoryDto>> loginMap = loginList.stream().collect(Collectors.groupingBy(LoginHistoryDto::getTime));

        for (int i = 30; i >= 0; i--) {
            Date date = DateUtil.getBeforeByDayTime(new Date(), -i);
            String timeName = sdf.format(date);
            int userNum = 0;
            if (loginMap.containsKey(timeName)) {
                userNum = loginMap.get(timeName).size();
            }
            map.put(timeName.substring(5), userNum);
        }
        return map;
    }

    static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * 每日文件上传大小.
     * @return 每日文件上传累计大小信息
     */
    @RequestMapping("/queryUploadFileData.do")
    @ResponseBody
    public Map<String, Long> queryUploadFileData() {
        Map<String, Long> map = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        for (int i = 30; i >= 0; i--) {
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

    /**
     * 用户累计上传文件大小数据.
     * @return 用户累计上传文件大小数据
     */
    @RequestMapping("/queryEachUserUploadFileSizeData.do")
    @ResponseBody
    public LinkedHashMap<String, Object> queryEachUserUploadFileSizeData() {
        List<LinkedHashMap<String, String>> list = wsFileService.queryEachUserUploadFileSizeData();
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        for (Map<String, String> map : list) {
            Object value= map.get("file_size");

            resultMap.put(map.get("user_name"), value);
        }
        return resultMap;
    }
}