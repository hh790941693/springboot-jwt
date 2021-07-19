package com.pjb.springbootjwt.zhddkk.job;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.shop.domain.SpGoodsDO;
import com.pjb.springbootjwt.shop.domain.SpGoodsTypeDO;
import com.pjb.springbootjwt.shop.domain.SpMerchantApplyDO;
import com.pjb.springbootjwt.shop.domain.SpMerchantDO;
import com.pjb.springbootjwt.shop.service.SpGoodsService;
import com.pjb.springbootjwt.shop.service.SpGoodsTypeService;
import com.pjb.springbootjwt.shop.service.SpMerchantApplyService;
import com.pjb.springbootjwt.shop.service.SpMerchantService;
import com.pjb.springbootjwt.zhddkk.domain.WsCircleDO;
import com.pjb.springbootjwt.zhddkk.domain.WsFeedbackDO;
import com.pjb.springbootjwt.zhddkk.domain.WsFileDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUserProfileDO;
import com.pjb.springbootjwt.zhddkk.service.WsCircleService;
import com.pjb.springbootjwt.zhddkk.service.WsFeedbackService;
import com.pjb.springbootjwt.zhddkk.service.WsFileService;
import com.pjb.springbootjwt.zhddkk.service.WsUserProfileService;
import com.pjb.springbootjwt.zhddkk.util.CommonUtil;
import com.pjb.springbootjwt.zhddkk.websocket.WebSocketConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 定时更新表ws_file中url的ip地址.
 */
@Configuration
@EnableScheduling
public class WsFileAccessCheckJob {

    private static final Logger logger = LoggerFactory.getLogger(WsFileAccessCheckJob.class);

    @Autowired
    private WsFileService wsFileService;

    @Autowired
    private WsCircleService wsCircleService;

    @Autowired
    private WsUserProfileService wsUserProfileService;

    @Autowired
    private WsFeedbackService wsFeedbackService;

    @Autowired
    private SpGoodsTypeService spGoodsTypeService;

    @Autowired
    private SpGoodsService spGoodsService;

    @Autowired
    private SpMerchantService spMerchantService;

    @Autowired
    private SpMerchantApplyService spMerchantApplyService;

    @Autowired
    private WebSocketConfig webSocketConfig;

    /**
     * 定时任务入口.
     */
    @Scheduled(cron = "0 */2 * * * ?")
    public void cronJob1() {
        logger.info("[定时任务1]定时检查文件资源url的访问性");
        checkWsFile();
        checkWsCircle();
        checkWsUserProfile();
        checkWsFeedback();
    }

    /**
     * 定时任务入口.
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void cronJob2() {
        logger.info("[定时任务2]定时检查文件资源url的访问性");
        checkMerchantApply();
        checkMerchant();
        checkGoodsType();
        checkGoods();
    }

    private void checkWsFile() {
        logger.info("开始检查ws_file");
        List<WsFileDO> wsFileList = wsFileService.selectList(new EntityWrapper<WsFileDO>().isNotNull("url"));
        if (null == wsFileList || wsFileList.size() == 0) {
            logger.info("没有需要检查的文件");
            return;
        }

        String webserverip = webSocketConfig.getAddress();
        List<WsFileDO> needBatchUpdateList = new ArrayList<>();
        for (WsFileDO wsFileDO : wsFileList) {
            boolean needUpdate = false;
            String url = wsFileDO.getUrl();
            int accessStatus =  wsFileDO.getAccessStatus();
            if (StringUtils.isBlank(url)) {
                continue;
            }
            if (!url.contains("//") && !url.contains(":")) {
                continue;
            }
            String oldIp = url.substring(url.indexOf("//") + 2, url.lastIndexOf(":"));
            if (!oldIp.equals(webserverip)) {
                String newUrl = url.replace(oldIp, webserverip);
                wsFileDO.setUrl(newUrl);
                needUpdate = true;
            }

            if (CommonUtil.testUrl(url)) {
                wsFileDO.setAccessStatus(1);
                if (accessStatus != 1) {
                    needUpdate = true;
                }
            } else {
                wsFileDO.setAccessStatus(0);
                if (accessStatus != 0) {
                    needUpdate = true;
                }
            }
            if (needUpdate) {
                needBatchUpdateList.add(wsFileDO);
            }
        }

        if (needBatchUpdateList.size() > 0) {
            logger.info("本次更新文件数:{}", needBatchUpdateList.size());
            wsFileService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
        }
    }

    private void checkWsCircle() {
        logger.info("开始检查ws_circle");
        List<WsCircleDO> wsCircleList = wsCircleService.selectList(new EntityWrapper<WsCircleDO>());
        if (null == wsCircleList || wsCircleList.size() == 0) {
            logger.info("没有需要检查的文件");
            return;
        }

        String webserverip = webSocketConfig.getAddress();
        List<WsCircleDO> needBatchUpdateList = new ArrayList<>();
        for (WsCircleDO wsCircleDO : wsCircleList) {
            List<String> picList = new ArrayList<>(Arrays.asList(wsCircleDO.getPic1(), wsCircleDO.getPic2(), wsCircleDO.getPic3(), wsCircleDO.getPic4()));
            int picIndex = 0;
            for (String pic : picList) {
                if (StringUtils.isNotBlank(pic) && pic.contains("//") && pic.contains(":")) {
                    String oldIp = pic.substring(pic.indexOf("//") + 2, pic.lastIndexOf(":"));
                    if (!oldIp.equals(webserverip)) {
                        String newUrl = pic.replace(oldIp, webserverip);
                        if (picIndex == 0) {
                            wsCircleDO.setPic1(newUrl);
                        } else if (picIndex == 1) {
                            wsCircleDO.setPic2(newUrl);
                        } else if (picIndex == 2) {
                            wsCircleDO.setPic3(newUrl);
                        } else if (picIndex == 3) {
                            wsCircleDO.setPic4(newUrl);
                        }
                        needBatchUpdateList.add(wsCircleDO);
                    }
                }
                picIndex++;
            }
        }
        if (needBatchUpdateList.size() > 0) {
            logger.info("本次更新文件数:{}", needBatchUpdateList.size());
            wsCircleService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
        }
    }

    private void checkWsUserProfile() {
        logger.info("开始检查ws_user_profile");
        List<WsUserProfileDO> wsUserProfileList = wsUserProfileService.selectList(
                new EntityWrapper<WsUserProfileDO>().isNotNull("img"));
        if (null == wsUserProfileList || wsUserProfileList.size() == 0) {
            logger.info("没有需要检查的文件");
            return;
        }

        String webserverip = webSocketConfig.getAddress();
        List<WsUserProfileDO> needBatchUpdateList = new ArrayList<>();
        for (WsUserProfileDO wsUserProfileDO : wsUserProfileList) {
            String url = wsUserProfileDO.getImg();
            if (StringUtils.isBlank(url)) {
                continue;
            }
            if (!url.contains("//") && !url.contains(":")) {
                continue;
            }
            String oldIp = url.substring(url.indexOf("//") + 2, url.lastIndexOf(":"));
            if (!oldIp.equals(webserverip)) {
                String newUrl = url.replace(oldIp, webserverip);
                wsUserProfileDO.setImg(newUrl);
                needBatchUpdateList.add(wsUserProfileDO);
            }
        }
        if (needBatchUpdateList.size() > 0) {
            logger.info("本次更新文件数:{}", needBatchUpdateList.size());
            wsUserProfileService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
        }
    }

    private void checkWsFeedback() {
        logger.info("开始检查ws_feedback");
        List<WsFeedbackDO> wsFeedbackList = wsFeedbackService.selectList(new EntityWrapper<WsFeedbackDO>().isNotNull("pic_url"));
        if (null == wsFeedbackList || wsFeedbackList.size() == 0) {
            logger.info("没有需要检查的文件");
            return;
        }

        String webserverip = webSocketConfig.getAddress();
        List<WsFeedbackDO> needBatchUpdateList = new ArrayList<>();
        for (WsFeedbackDO wsFeedbackDO : wsFeedbackList) {
            String url = wsFeedbackDO.getPicUrl();
            if (StringUtils.isBlank(url)) {
                continue;
            }
            if (!url.contains("//") && !url.contains(":")) {
                continue;
            }
            String oldIp = url.substring(url.indexOf("//") + 2, url.lastIndexOf(":"));
            if (!oldIp.equals(webserverip)) {
                String newUrl = url.replace(oldIp, webserverip);
                wsFeedbackDO.setPicUrl(newUrl);
                needBatchUpdateList.add(wsFeedbackDO);
            }
        }
        if (needBatchUpdateList.size() > 0) {
            logger.info("本次更新文件数:{}", needBatchUpdateList.size());
            wsFeedbackService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
        }
    }

    private void checkMerchantApply() {
        logger.info("开始检查sp_merchant_apply");
        List<SpMerchantApplyDO> list = spMerchantApplyService.selectList(new EntityWrapper<SpMerchantApplyDO>().isNotNull("image"));
        if (null == list || list.size() == 0) {
            logger.info("没有需要检查的文件");
            return;
        }

        String webserverip = webSocketConfig.getAddress();
        List<SpMerchantApplyDO> needBatchUpdateList = new ArrayList<>();
        for (SpMerchantApplyDO spMerchantApplyDO : list) {
            String oldUrl = spMerchantApplyDO.getImage();
            if (StringUtils.isBlank(oldUrl)) {
                continue;
            }
            if (!oldUrl.contains("//") && !oldUrl.contains(":")) {
                continue;
            }
            String oldIp = oldUrl.substring(oldUrl.indexOf("//") + 2, oldUrl.lastIndexOf(":"));
            if (!oldIp.equals(webserverip)) {
                String newUrl = oldUrl.replace(oldIp, webserverip);
                spMerchantApplyDO.setImage(newUrl);
                needBatchUpdateList.add(spMerchantApplyDO);
            }
        }
        if (needBatchUpdateList.size() > 0) {
            logger.info("本次更新文件数:{}", needBatchUpdateList.size());
            spMerchantApplyService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
        }
    }

    private void checkMerchant() {
        logger.info("开始检查sp_merchant");
        List<SpMerchantDO> list = spMerchantService.selectList(new EntityWrapper<SpMerchantDO>().isNotNull("image"));
        if (null == list || list.size() == 0) {
            logger.info("没有需要检查的文件");
            return;
        }

        String webserverip = webSocketConfig.getAddress();
        List<SpMerchantDO> needBatchUpdateList = new ArrayList<>();
        for (SpMerchantDO spMerchantDO : list) {
            String oldUrl = spMerchantDO.getImage();
            if (StringUtils.isBlank(oldUrl)) {
                continue;
            }
            if (!oldUrl.contains("//") && !oldUrl.contains(":")) {
                continue;
            }
            String oldIp = oldUrl.substring(oldUrl.indexOf("//") + 2, oldUrl.lastIndexOf(":"));
            if (!oldIp.equals(webserverip)) {
                String newUrl = oldUrl.replace(oldIp, webserverip);
                spMerchantDO.setImage(newUrl);
                needBatchUpdateList.add(spMerchantDO);
            }
        }
        if (needBatchUpdateList.size() > 0) {
            logger.info("本次更新文件数:{}", needBatchUpdateList.size());
            spMerchantService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
        }
    }

    private void checkGoodsType() {
        logger.info("开始检查sp_goods_type");
        List<SpGoodsTypeDO> list = spGoodsTypeService.selectList(new EntityWrapper<SpGoodsTypeDO>().isNotNull("image"));
        if (null == list || list.size() == 0) {
            logger.info("没有需要检查的文件");
            return;
        }

        String webserverip = webSocketConfig.getAddress();
        List<SpGoodsTypeDO> needBatchUpdateList = new ArrayList<>();
        for (SpGoodsTypeDO spGoodsTypeDO : list) {
            String oldUrl = spGoodsTypeDO.getImage();
            if (StringUtils.isBlank(oldUrl)) {
                continue;
            }
            if (!oldUrl.contains("//") && !oldUrl.contains(":")) {
                continue;
            }
            String oldIp = oldUrl.substring(oldUrl.indexOf("//") + 2, oldUrl.lastIndexOf(":"));
            if (!oldIp.equals(webserverip)) {
                String newUrl = oldUrl.replace(oldIp, webserverip);
                spGoodsTypeDO.setImage(newUrl);
                needBatchUpdateList.add(spGoodsTypeDO);
            }
        }
        if (needBatchUpdateList.size() > 0) {
            logger.info("本次更新文件数:{}", needBatchUpdateList.size());
            spGoodsTypeService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
        }
    }

    private void checkGoods() {
        logger.info("开始检查sp_goods");
        List<SpGoodsDO> list = spGoodsService.selectList(null);
        if (null == list || list.size() == 0) {
            logger.info("没有需要检查的文件");
            return;
        }

        String webserverip = webSocketConfig.getAddress();
        List<SpGoodsDO> needBatchUpdateList = new ArrayList<>();
        for (SpGoodsDO spGoodsDO : list) {

            String backImage = spGoodsDO.getBackImage();
            if (StringUtils.isNotBlank(backImage)) {
                if (backImage.contains("//") && backImage.contains(":")) {

                    String oldIp = backImage.substring(backImage.indexOf("//") + 2, backImage.lastIndexOf(":"));
                    if (!oldIp.equals(webserverip)) {
                        String newUrl = backImage.replace(oldIp, webserverip);
                        spGoodsDO.setBackImage(newUrl);

                    }
                }
            }

            String image1 = spGoodsDO.getImage1();
            if (StringUtils.isNotBlank(image1)) {
                if (image1.contains("//") && image1.contains(":")) {

                    String oldIp = image1.substring(image1.indexOf("//") + 2, image1.lastIndexOf(":"));
                    if (!oldIp.equals(webserverip)) {
                        String newUrl = image1.replace(oldIp, webserverip);
                        spGoodsDO.setImage1(newUrl);

                    }
                }
            }

            String image2 = spGoodsDO.getImage2();
            if (StringUtils.isNotBlank(image2)) {
                if (image2.contains("//") && image2.contains(":")) {

                    String oldIp = image2.substring(image2.indexOf("//") + 2, image2.lastIndexOf(":"));
                    if (!oldIp.equals(webserverip)) {
                        String newUrl = image2.replace(oldIp, webserverip);
                        spGoodsDO.setImage2(newUrl);

                    }
                }
            }

            String image3 = spGoodsDO.getImage3();
            if (StringUtils.isNotBlank(image3)) {
                if (image3.contains("//") && image3.contains(":")) {

                    String oldIp = image3.substring(image3.indexOf("//") + 2, image3.lastIndexOf(":"));
                    if (!oldIp.equals(webserverip)) {
                        String newUrl = image3.replace(oldIp, webserverip);
                        spGoodsDO.setImage3(newUrl);

                    }
                }
            }

            String image4 = spGoodsDO.getImage4();
            if (StringUtils.isNotBlank(image4)) {
                if (image4.contains("//") && image4.contains(":")) {

                    String oldIp = image4.substring(image4.indexOf("//") + 2, image4.lastIndexOf(":"));
                    if (!oldIp.equals(webserverip)) {
                        String newUrl = image4.replace(oldIp, webserverip);
                        spGoodsDO.setImage4(newUrl);

                    }
                }
            }

            needBatchUpdateList.add(spGoodsDO);
        }
        if (needBatchUpdateList.size() > 0) {
            logger.info("本次更新文件数:{}", needBatchUpdateList.size());
            spGoodsService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
        }
    }
}
