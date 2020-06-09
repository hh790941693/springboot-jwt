package com.pjb.springbootjwt.zhddkk.job;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.zhddkk.domain.WsCircleDO;
import com.pjb.springbootjwt.zhddkk.domain.WsFeedbackDO;
import com.pjb.springbootjwt.zhddkk.domain.WsFileDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUserProfileDO;
import com.pjb.springbootjwt.zhddkk.service.WsCircleService;
import com.pjb.springbootjwt.zhddkk.service.WsFeedbackService;
import com.pjb.springbootjwt.zhddkk.service.WsFileService;
import com.pjb.springbootjwt.zhddkk.service.WsUserProfileService;
import com.pjb.springbootjwt.zhddkk.websocket.WebSocketConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

/***
 * 定时更新表ws_file中url的ip地址
 */
@Configuration
@EnableScheduling
public class WsFileAccessCheckJob {

    private static Logger logger = LoggerFactory.getLogger(WsFileAccessCheckJob.class);

    @Autowired
    private WsFileService wsFileService;

    @Autowired
    private WsCircleService wsCircleService;

    @Autowired
    private WsUserProfileService wsUserProfileService;

    @Autowired
    private WsFeedbackService wsFeedbackService;

    @Autowired
    private WebSocketConfig webSocketConfig;

    @Scheduled(cron="0 */1 * * * ?")
    public void cronJob(){
        logger.info("[定时任务]定时检查文件资源url的访问性");
        checkWsFile();
        checkWsCircle();
        checkWsUserProfile();
        checkWsFeedback();
    }

    private void checkWsFile(){
        logger.info("开始检查ws_file");
        List<WsFileDO> wsFileList = wsFileService.selectList(new EntityWrapper<WsFileDO>().isNotNull("url"));
        if (null == wsFileList || wsFileList.size() == 0){
            logger.info("没有需要检查的文件");
            return;
        }

        String webserverip = webSocketConfig.getAddress();
        List<WsFileDO> needBatchUpdateList = new ArrayList<>();
        for (WsFileDO wsFileDO : wsFileList){
            String url = wsFileDO.getUrl();
            if (StringUtils.isBlank(url)){
                continue;
            }
            if (!url.contains("//") && !url.contains(":")){
                continue;
            }
            String oldIp = url.substring(url.indexOf("//")+2, url.lastIndexOf(":"));
            if (!oldIp.equals(webserverip)) {
                String newUrl = url.replace(oldIp, webserverip);
                wsFileDO.setUrl(newUrl);
                needBatchUpdateList.add(wsFileDO);
            }
        }
        if (needBatchUpdateList.size()>0){
            logger.info("本次更新文件数:{}", needBatchUpdateList.size());
            wsFileService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
        }
    }

    private void checkWsCircle(){
        logger.info("开始检查ws_circle");
        List<WsCircleDO> wsCircleList = wsCircleService.selectList(new EntityWrapper<WsCircleDO>());
        if (null == wsCircleList || wsCircleList.size() == 0){
            logger.info("没有需要检查的文件");
            return;
        }

        String webserverip = webSocketConfig.getAddress();
        List<WsCircleDO> needBatchUpdateList = new ArrayList<>();
        for (WsCircleDO wsCircleDO : wsCircleList){
            String pic1 = wsCircleDO.getPic1();
            String pic2 = wsCircleDO.getPic2();
            String pic3 = wsCircleDO.getPic3();
            String pic4 = wsCircleDO.getPic4();
            if (StringUtils.isNotBlank(pic1) && pic1.contains("//") && pic1.contains(":")){
                String oldIp = pic1.substring(pic1.indexOf("//")+2, pic1.lastIndexOf(":"));
                if (!oldIp.equals(webserverip)) {
                    String newUrl = pic1.replace(oldIp, webserverip);
                    wsCircleDO.setPic1(newUrl);
                    needBatchUpdateList.add(wsCircleDO);
                }
            }
            if (StringUtils.isNotBlank(pic2) && pic2.contains("//") && pic2.contains(":")){
                String oldIp = pic2.substring(pic2.indexOf("//")+2, pic2.lastIndexOf(":"));
                if (!oldIp.equals(webserverip)) {
                    String newUrl = pic2.replace(oldIp, webserverip);
                    wsCircleDO.setPic2(newUrl);
                    needBatchUpdateList.add(wsCircleDO);
                }
            }
            if (StringUtils.isNotBlank(pic3) && pic3.contains("//") && pic3.contains(":")){
                String oldIp = pic3.substring(pic3.indexOf("//")+2, pic3.lastIndexOf(":"));
                if (!oldIp.equals(webserverip)) {
                    String newUrl = pic3.replace(oldIp, webserverip);
                    wsCircleDO.setPic3(newUrl);
                    needBatchUpdateList.add(wsCircleDO);
                }
            }
            if (StringUtils.isNotBlank(pic4) && pic4.contains("//") && pic4.contains(":")){
                String oldIp = pic4.substring(pic4.indexOf("//")+2, pic4.lastIndexOf(":"));
                if (!oldIp.equals(webserverip)) {
                    String newUrl = pic4.replace(oldIp, webserverip);
                    wsCircleDO.setPic4(newUrl);
                    needBatchUpdateList.add(wsCircleDO);
                }
            }
        }
        if (needBatchUpdateList.size()>0){
            logger.info("本次更新文件数:{}", needBatchUpdateList.size());
            wsCircleService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
        }
    }

    private void checkWsUserProfile(){
        logger.info("开始检查ws_user_profile");
        List<WsUserProfileDO> wsUserProfileList = wsUserProfileService.selectList(new EntityWrapper<WsUserProfileDO>().isNotNull("img"));
        if (null == wsUserProfileList || wsUserProfileList.size() == 0){
            logger.info("没有需要检查的文件");
            return;
        }

        String webserverip = webSocketConfig.getAddress();
        List<WsUserProfileDO> needBatchUpdateList = new ArrayList<>();
        for (WsUserProfileDO wsUserProfileDO : wsUserProfileList){
            String url = wsUserProfileDO.getImg();
            if (StringUtils.isBlank(url)){
                continue;
            }
            if (!url.contains("//") && !url.contains(":")){
                continue;
            }
            String oldIp = url.substring(url.indexOf("//")+2, url.lastIndexOf(":"));
            if (!oldIp.equals(webserverip)) {
                String newUrl = url.replace(oldIp, webserverip);
                wsUserProfileDO.setImg(newUrl);
                needBatchUpdateList.add(wsUserProfileDO);
            }
        }
        if (needBatchUpdateList.size()>0){
            logger.info("本次更新文件数:{}", needBatchUpdateList.size());
            wsUserProfileService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
        }
    }

    private void checkWsFeedback(){
        logger.info("开始检查ws_feedback");
        List<WsFeedbackDO> wsFeedbackList = wsFeedbackService.selectList(new EntityWrapper<WsFeedbackDO>().isNotNull("pic_url"));
        if (null == wsFeedbackList || wsFeedbackList.size() == 0){
            logger.info("没有需要检查的文件");
            return;
        }

        String webserverip = webSocketConfig.getAddress();
        List<WsFeedbackDO> needBatchUpdateList = new ArrayList<>();
        for (WsFeedbackDO wsFeedbackDO : wsFeedbackList){
            String url = wsFeedbackDO.getPicUrl();
            if (StringUtils.isBlank(url)){
                continue;
            }
            if (!url.contains("//") && !url.contains(":")){
                continue;
            }
            String oldIp = url.substring(url.indexOf("//")+2, url.lastIndexOf(":"));
            if (!oldIp.equals(webserverip)) {
                String newUrl = url.replace(oldIp, webserverip);
                wsFeedbackDO.setPicUrl(newUrl);
                needBatchUpdateList.add(wsFeedbackDO);
            }
        }
        if (needBatchUpdateList.size()>0){
            logger.info("本次更新文件数:{}", needBatchUpdateList.size());
            wsFeedbackService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
        }
    }
}
