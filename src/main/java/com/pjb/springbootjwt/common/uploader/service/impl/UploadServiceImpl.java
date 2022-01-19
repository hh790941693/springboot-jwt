package com.pjb.springbootjwt.common.uploader.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.common.uploader.config.UploadConfig;
import com.pjb.springbootjwt.common.uploader.service.UploadService;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.domain.WsFileDO;
import com.pjb.springbootjwt.zhddkk.service.WsFileService;
import com.pjb.springbootjwt.zhddkk.util.CommonUtil;
import com.pjb.springbootjwt.zhddkk.util.SessionUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {
    
    private static Logger logger = LoggerFactory.getLogger(UploadService.class);
    
    private static String TEMP_PATH = "temp";
    
    @Autowired
    private UploadConfig uploadConfig;
    
    @Autowired
    private WsFileService wsFileService;
    
    @Override
    public String uploadFile(MultipartFile file, String folder, String userName)
        throws Exception {
        logger.info("进入上传文件业务层,目录名:{} 用户:{}", folder, userName);
        if (StringUtils.isBlank(folder)) {
            folder = TEMP_PATH;
        }

        // 检查文件上传是否重复
        String md5 = CommonUtil.getMD5(file.getBytes());
        if (StringUtils.isNotBlank(md5)) {
            List<WsFileDO> conflictFileList = wsFileService.selectList(new EntityWrapper<WsFileDO>().eq("md5", md5));
            if (null != conflictFileList && conflictFileList.size() > 0) {
                logger.info("{}上传重复,md5:{}", file.getOriginalFilename(), md5);
                return conflictFileList.get(0).getUrl();
            }
        }

        String originalFilename = file.getOriginalFilename();
        String newFileName = renameToUUID(originalFilename);
        logger.info("newFileName:" + newFileName);
        String totalFolder = uploadConfig.getStorePath() + File.separator + folder + File.separator;
        logger.info("totalFolder：" + totalFolder);
        File newFile = new File(totalFolder, newFileName);
        FileUtils.writeByteArrayToFile(newFile, file.getBytes());

        String viewUrl = uploadConfig.getViewUrl() + folder + "/" + newFileName;
        WsFileDO wsFileDO = new WsFileDO();
        wsFileDO.setUser(userName);
        wsFileDO.setFolder(folder);
        wsFileDO.setFilename(file.getOriginalFilename());
        if (uploadConfig.getStorePath().endsWith(File.separator)) {
            wsFileDO.setDiskPath(uploadConfig.getStorePath() + folder);
        } else {
            wsFileDO.setDiskPath(uploadConfig.getStorePath() + File.separator + folder);
        }
        wsFileDO.setUrl(viewUrl);
        wsFileDO.setFileSize(newFile.length());

        // if (originFilename.endsWith("mp3")) {
        // try {
        // trackLength = MusicParserUtil.getMusicTrackTime(newFile.getAbsolutePath());
        // }catch (Exception e){
        // logger.info("获取音乐文件时长异常:"+e.getMessage());
        // }
        // }
        wsFileDO.setTrackLength("00:00");
        wsFileDO.setMd5(md5);
        wsFileService.insert(wsFileDO);
        
        return viewUrl;
    }

    /**
     * 重命名文件为UUID + 后缀
     *
     * @param fileName 原始文件名
     */
    private static String renameToUUID(String fileName) {
        if (fileName.lastIndexOf(".") > 0) {
            return UUID.randomUUID() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return UUID.randomUUID().toString();
        }
    }

    /**
     * 检查用户上传容量是否超出
     * @param
     * @return
     * @throws Exception
     */
    @Override
    public boolean checkUploadCapacity() throws Exception {
        String sessionUserName = SessionUtil.getSessionUserName();
        boolean canUploadFlag = true;
        if (StringUtils.isNotBlank(sessionUserName) && !sessionUserName.equals(CommonConstants.ADMIN_USER)) {
            Long fileTotalSize = wsFileService.queryUserTodayFileSize(sessionUserName);
            if (null != fileTotalSize) {
                if (fileTotalSize > CommonConstants.DAY_MAX_UPLOAD_FILE_SIZE) {
                    canUploadFlag = false;
                }
            }
        }
        return canUploadFlag;
    }
}
