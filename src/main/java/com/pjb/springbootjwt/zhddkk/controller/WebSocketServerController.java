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
     * @param startDay 起始时间  yyyy-MM-dd
     * @param endDay   结束时间  yyyy-MM-dd
     * @return 签到人数信息
     */
    @RequestMapping("/querySignData.do")
    @ResponseBody
    public Map<String, Integer> querySignData(String startDay, String endDay) {
        Map<String, Integer> map = new LinkedHashMap<>();
        SimpleDateFormat sdfx = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfy = new SimpleDateFormat("MM-dd");
        Date beginDate = null;
        Date endDate = null;
        try {
            beginDate = sdfx.parse(startDay);
            endDate = sdfx.parse(endDay);
        } catch (Exception e) {

        }

        Date startDate = beginDate;
        do {
            Date dayBeginDate = DateUtil.dayBeginDate(startDate);
            Date dayEndDate = DateUtil.dayEndDate(startDate);
            int count = wsSignService.selectCount(new EntityWrapper<WsSignDO>().ge("create_time", dayBeginDate)
                    .le("create_time", dayEndDate));

            String timeName = sdfy.format(startDate);
            map.put(timeName, count);
            startDate = DateUtil.getBeforeByDayTime(startDate, 1);
        } while (startDate.compareTo(endDate) <= 0);

        return map;
    }

    /**
     * 每日在线人数.
     * @return 每日在线人数信息
     */
    @RequestMapping("/queryOnlineUserData.do")
    @ResponseBody
    public Map<String, Integer> queryOnlineUserData(String startDay, String endDay) {

        SimpleDateFormat sdfx = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = null;
        Date endDate = null;
        try {
            beginDate = sdfx.parse(startDay);
            endDate = sdfx.parse(endDay);
        } catch (Exception e) {

        }

        Map<String, Integer> map = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<LoginHistoryDto> loginList = wsOperationLogService.queryOnlineUserData();
        Map<String, List<LoginHistoryDto>> loginMap = loginList.stream().collect(Collectors.groupingBy(LoginHistoryDto::getTime));

        Date startDate = beginDate;
        do {
            String timeName = sdf.format(startDate);
            int userNum = 0;
            if (loginMap.containsKey(timeName)) {
                userNum = loginMap.get(timeName).size();
            }
            map.put(timeName.substring(5), userNum);
            startDate = DateUtil.getBeforeByDayTime(startDate, 1);
        } while (startDate.compareTo(endDate) <= 0);

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
    public Map<String, Long> queryUploadFileData(String startDay, String endDay) {
        SimpleDateFormat sdfx = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = null;
        Date endDate = null;
        try {
            beginDate = sdfx.parse(startDay);
            endDate = sdfx.parse(endDay);
        } catch (Exception e) {

        }

        Map<String, Long> map = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");

        Date startDate = beginDate;
        do {
            Date dayBeginDate = DateUtil.dayBeginDate(startDate);
            Date dayEndDate = DateUtil.dayEndDate(startDate);
            String timeName = sdf.format(startDate);
            List<WsFileDO> wsFileList = wsFileService.selectList(new EntityWrapper<WsFileDO>()
                    .ge("create_time", dayBeginDate)
                    .le("create_time", dayEndDate));
            long fileSize = wsFileList.stream().map(WsFileDO::getFileSize).reduce(0l, Long::sum);
            map.put(timeName, fileSize);

            startDate = DateUtil.getBeforeByDayTime(startDate, 1);
        } while (startDate.compareTo(endDate) <= 0);

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