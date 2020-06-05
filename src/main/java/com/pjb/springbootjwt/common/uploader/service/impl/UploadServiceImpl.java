package com.pjb.springbootjwt.common.uploader.service.impl;

import com.pjb.springbootjwt.common.uploader.config.UploadConfig;
import com.pjb.springbootjwt.common.uploader.service.UploadService;
import com.pjb.springbootjwt.zhddkk.domain.WsFileDO;
import com.pjb.springbootjwt.zhddkk.service.WsFileService;
import com.pjb.springbootjwt.zhddkk.util.MusicParserUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
    public String uploadFile(MultipartFile file, String folder, String userId) throws Exception {
        logger.info("进入上传文件业务层,目录名:{} 用户id:{}", folder, userId);
        if (StringUtils.isBlank(folder)){
            folder = TEMP_PATH;
        }
        String originFilename = file.getOriginalFilename();
        String newFileName = renameToUUID(file.getOriginalFilename());
        logger.info("newFileName:"+newFileName);
        String totalFolder = uploadConfig.getStorePath() + File.separator + folder + File.separator;
        logger.info("totalFolder："+totalFolder);
        File newFile = new File(totalFolder, newFileName);
        FileUtils.writeByteArrayToFile(newFile, file.getBytes());
        String viewUrl = uploadConfig.getViewUrl() + folder + "/" + newFileName;

        WsFileDO wsFileDO = new WsFileDO();
        wsFileDO.setUser(userId);
        wsFileDO.setFolder(folder);
        wsFileDO.setFilename(file.getOriginalFilename());
        wsFileDO.setDiskPath(uploadConfig.getStorePath() + File.separator + folder);
        wsFileDO.setUrl(viewUrl);
        wsFileDO.setFileSize(newFile.length());

        String trackLength = "";
        if (originFilename.endsWith("mp3")) {
            try {
                trackLength = MusicParserUtil.getMusicTrackTime(newFile.getAbsolutePath());
            }catch (Exception e){
                logger.info("获取音乐文件时长异常:"+e.getMessage());
            }
        }
        wsFileDO.setTrackLength(trackLength);
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
}
