package com.pjb.springbootjwt.zhddkk.job;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.common.uploader.config.UploadConfig;
import com.pjb.springbootjwt.shop.domain.SpGoodsDO;
import com.pjb.springbootjwt.shop.domain.SpGoodsTypeDO;
import com.pjb.springbootjwt.shop.domain.SpMerchantApplyDO;
import com.pjb.springbootjwt.shop.domain.SpMerchantDO;
import com.pjb.springbootjwt.shop.service.SpGoodsService;
import com.pjb.springbootjwt.shop.service.SpGoodsTypeService;
import com.pjb.springbootjwt.shop.service.SpMerchantApplyService;
import com.pjb.springbootjwt.shop.service.SpMerchantService;
import com.pjb.springbootjwt.zhddkk.domain.*;
import com.pjb.springbootjwt.zhddkk.service.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 定时更新表中url的ip地址.
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
    private WsAdsService wsAdsService;

    @Autowired
    private SpGoodsTypeService spGoodsTypeService;

    @Autowired
    private SpGoodsService spGoodsService;

    @Autowired
    private SpMerchantService spMerchantService;

    @Autowired
    private SpMerchantApplyService spMerchantApplyService;

    @Autowired
    private UploadConfig uploadConfig;

    /**
     * 定时任务入口.
     */
    @Scheduled(cron = "0 */2 * * * ?")
    public void cronJob1() {
        logger.info("[定时任务1]定时检查文件资源url的访问性");
        checkWsAds();
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

    private void checkWsAds() {
        logger.info("开始检查ws_ads");
        List<WsAdsDO> srcList = wsAdsService.selectList(new EntityWrapper<WsAdsDO>().isNotNull("back_img"));
        if (null == srcList || srcList.size() == 0) {
            logger.info("没有需要检查的文件");
            return;
        }

        String viewUrl = uploadConfig.getViewUrl();
        String ip = viewUrl.substring(viewUrl.indexOf("//") + 2, viewUrl.lastIndexOf(":"));
        String port = viewUrl.substring(viewUrl.lastIndexOf(":")+1).replace("/", "");
        String ipPort = ip + ":" + port;

        List<WsAdsDO> needBatchUpdateList = new ArrayList<>();
        List<WsAdsDO> targetList = srcList.stream().filter(record-> StringUtils.isNotBlank(record.getBackImg()) && !record.getBackImg().contains(ipPort)).collect(Collectors.toList());
        for (WsAdsDO record : targetList) {
            String newValue = record.getBackImg().replaceAll("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}:[0-9]{1,5}", ipPort);
            record.setBackImg(newValue);
            needBatchUpdateList.add(record);
        }

        if (needBatchUpdateList.size() > 0) {
            logger.info("本次更新文件数:{}", needBatchUpdateList.size());
            wsAdsService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
        }
    }

    private void checkWsFile() {
        logger.info("开始检查ws_file");
        List<WsFileDO> srcList = wsFileService.selectList(new EntityWrapper<WsFileDO>().isNotNull("url"));
        if (null == srcList || srcList.size() == 0) {
            logger.info("没有需要检查的文件");
            return;
        }

        String viewUrl = uploadConfig.getViewUrl();
        String ip = viewUrl.substring(viewUrl.indexOf("//") + 2, viewUrl.lastIndexOf(":"));
        String port = viewUrl.substring(viewUrl.lastIndexOf(":")+1).replace("/", "");
        String ipPort = ip + ":" + port;

        List<WsFileDO> needBatchUpdateList = new ArrayList<>();
        List<WsFileDO> targetList = srcList.stream().filter(record-> StringUtils.isNotBlank(record.getUrl()) && !record.getUrl().contains(ipPort)).collect(Collectors.toList());
        for (WsFileDO record : targetList) {
            String newValue = record.getUrl().replaceAll("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}:[0-9]{1,5}", ipPort);
            record.setUrl(newValue);
            needBatchUpdateList.add(record);
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

        String viewUrl = uploadConfig.getViewUrl();
        String webserverip = viewUrl.substring(viewUrl.indexOf("//") + 2, viewUrl.lastIndexOf(":"));
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
        List<WsUserProfileDO> srcList = wsUserProfileService.selectList(
                new EntityWrapper<WsUserProfileDO>().isNotNull("img"));
        if (null == srcList || srcList.size() == 0) {
            logger.info("没有需要检查的文件");
            return;
        }

        String viewUrl = uploadConfig.getViewUrl();
        String ip = viewUrl.substring(viewUrl.indexOf("//") + 2, viewUrl.lastIndexOf(":"));
        String port = viewUrl.substring(viewUrl.lastIndexOf(":")+1).replace("/", "");
        String ipPort = ip + ":" + port;

        List<WsUserProfileDO> needBatchUpdateList = new ArrayList<>();
        List<WsUserProfileDO> targetList = srcList.stream().filter(record-> StringUtils.isNotBlank(record.getImg()) && !record.getImg().contains(ipPort)).collect(Collectors.toList());
        for (WsUserProfileDO record : targetList) {
            String newValue = record.getImg().replaceAll("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}:[0-9]{1,5}", ipPort);
            record.setImg(newValue);
            needBatchUpdateList.add(record);
        }

        if (needBatchUpdateList.size() > 0) {
            logger.info("本次更新文件数:{}", needBatchUpdateList.size());
            wsUserProfileService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
        }
    }

    private void checkWsFeedback() {
        logger.info("开始检查ws_feedback");
        List<WsFeedbackDO> srcList = wsFeedbackService.selectList(new EntityWrapper<WsFeedbackDO>().isNotNull("pic_url"));
        if (null == srcList || srcList.size() == 0) {
            logger.info("没有需要检查的文件");
            return;
        }

        String viewUrl = uploadConfig.getViewUrl();
        String ip = viewUrl.substring(viewUrl.indexOf("//") + 2, viewUrl.lastIndexOf(":"));
        String port = viewUrl.substring(viewUrl.lastIndexOf(":")+1).replace("/", "");
        String ipPort = ip + ":" + port;

        List<WsFeedbackDO> needBatchUpdateList = new ArrayList<>();
        List<WsFeedbackDO> targetList = srcList.stream().filter(record-> StringUtils.isNotBlank(record.getPicUrl()) && !record.getPicUrl().contains(ipPort)).collect(Collectors.toList());
        for (WsFeedbackDO record : targetList) {
            String newValue = record.getPicUrl().replaceAll("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}:[0-9]{1,5}", ipPort);
            record.setPicUrl(newValue);
            needBatchUpdateList.add(record);
        }

        if (needBatchUpdateList.size() > 0) {
            logger.info("本次更新文件数:{}", needBatchUpdateList.size());
            wsFeedbackService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
        }
    }

    private void checkMerchantApply() {
        logger.info("开始检查sp_merchant_apply");
        List<SpMerchantApplyDO> srcList = spMerchantApplyService.selectList(new EntityWrapper<SpMerchantApplyDO>().isNotNull("image"));
        if (null == srcList || srcList.size() == 0) {
            logger.info("没有需要检查的文件");
            return;
        }

        String viewUrl = uploadConfig.getViewUrl();
        String ip = viewUrl.substring(viewUrl.indexOf("//") + 2, viewUrl.lastIndexOf(":"));
        String port = viewUrl.substring(viewUrl.lastIndexOf(":")+1).replace("/", "");
        String ipPort = ip + ":" + port;

        List<SpMerchantApplyDO> needBatchUpdateList = new ArrayList<>();
        List<SpMerchantApplyDO> targetList = srcList.stream().filter(record-> StringUtils.isNotBlank(record.getImage()) && !record.getImage().contains(ipPort)).collect(Collectors.toList());
        for (SpMerchantApplyDO record : targetList) {
            String newValue = record.getImage().replaceAll("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}:[0-9]{1,5}", ipPort);
            record.setImage(newValue);
            needBatchUpdateList.add(record);
        }

        if (needBatchUpdateList.size() > 0) {
            logger.info("本次更新文件数:{}", needBatchUpdateList.size());
            spMerchantApplyService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
        }
    }

    private void checkMerchant() {
        logger.info("开始检查sp_merchant");
        List<SpMerchantDO> srcList = spMerchantService.selectList(new EntityWrapper<SpMerchantDO>().isNotNull("image"));
        if (null == srcList || srcList.size() == 0) {
            logger.info("没有需要检查的文件");
            return;
        }

        String viewUrl = uploadConfig.getViewUrl();
        String ip = viewUrl.substring(viewUrl.indexOf("//") + 2, viewUrl.lastIndexOf(":"));
        String port = viewUrl.substring(viewUrl.lastIndexOf(":")+1).replace("/", "");
        String ipPort = ip + ":" + port;

        List<SpMerchantDO> needBatchUpdateList = new ArrayList<>();
        List<SpMerchantDO> targetList = srcList.stream().filter(record-> StringUtils.isNotBlank(record.getImage()) && !record.getImage().contains(ipPort)).collect(Collectors.toList());
        for (SpMerchantDO record : targetList) {
            String newValue = record.getImage().replaceAll("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}:[0-9]{1,5}", ipPort);
            record.setImage(newValue);
            needBatchUpdateList.add(record);
        }

        if (needBatchUpdateList.size() > 0) {
            logger.info("本次更新文件数:{}", needBatchUpdateList.size());
            spMerchantService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
        }
    }

    private void checkGoodsType() {
        logger.info("开始检查sp_goods_type");
        List<SpGoodsTypeDO> srcList = spGoodsTypeService.selectList(new EntityWrapper<SpGoodsTypeDO>().isNotNull("image"));
        if (null == srcList || srcList.size() == 0) {
            logger.info("没有需要检查的文件");
            return;
        }

        String viewUrl = uploadConfig.getViewUrl();
        String ip = viewUrl.substring(viewUrl.indexOf("//") + 2, viewUrl.lastIndexOf(":"));
        String port = viewUrl.substring(viewUrl.lastIndexOf(":")+1).replace("/", "");
        String ipPort = ip + ":" + port;

        List<SpGoodsTypeDO> needBatchUpdateList = new ArrayList<>();
        List<SpGoodsTypeDO> targetList = srcList.stream().filter(record-> StringUtils.isNotBlank(record.getImage()) && !record.getImage().contains(ipPort)).collect(Collectors.toList());
        for (SpGoodsTypeDO record : targetList) {
            String newValue = record.getImage().replaceAll("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}:[0-9]{1,5}", ipPort);
            record.setImage(newValue);
            needBatchUpdateList.add(record);
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

        String viewUrl = uploadConfig.getViewUrl();
        String webserverip = viewUrl.substring(viewUrl.indexOf("//") + 2, viewUrl.lastIndexOf(":"));
        List<SpGoodsDO> needBatchUpdateList = new ArrayList<>();
        for (SpGoodsDO spGoodsDO : list) {
            String backImage = spGoodsDO.getBackImage();
            boolean isNeedUpdate = false;
            if (StringUtils.isNotBlank(backImage)) {
                if (backImage.contains("//") && backImage.contains(":")) {
                    String oldIp = backImage.substring(backImage.indexOf("//") + 2, backImage.lastIndexOf(":"));
                    if (!oldIp.equals(webserverip)) {
                        String newUrl = backImage.replace(oldIp, webserverip);
                        spGoodsDO.setBackImage(newUrl);
                        isNeedUpdate = true;
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
                        isNeedUpdate = true;
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
                        isNeedUpdate = true;
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
                        isNeedUpdate = true;
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
                        isNeedUpdate = true;
                    }
                }
            }
            if (isNeedUpdate){
                needBatchUpdateList.add(spGoodsDO);
            }
        }
        if (needBatchUpdateList.size() > 0) {
            logger.info("本次更新文件数:{}", needBatchUpdateList.size());
            spGoodsService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
        }
    }
}
