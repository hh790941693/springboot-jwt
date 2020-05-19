package com.pjb.springbootjwt.common.uploader.service.impl;

import com.pjb.springbootjwt.common.uploader.config.UploadConfig;
import com.pjb.springbootjwt.common.uploader.service.UploadService;
import com.pjb.springbootjwt.zhddkk.entity.WsFile;
import com.pjb.springbootjwt.zhddkk.service.WsService;
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
    private WsService wsService;

    @Override
    public String uploadFile(MultipartFile file, String folder, String userId) throws Exception {
        logger.info("进入上传文件业务层,目录名:{} 用户id:{}", folder, userId);
        if (StringUtils.isBlank(folder)){
            folder = TEMP_PATH;
        }
        String originFilename = file.getOriginalFilename();
        //String newFileName = renameToUUID(file.getOriginalFilename());
        //logger.info("newFileName:"+newFileName);
        String totalFolder = uploadConfig.getStorePath() + folder + File.separator;
        File newFile = new File(totalFolder, originFilename);
        FileUtils.writeByteArrayToFile(newFile, file.getBytes());
        String viewUrl = uploadConfig.getViewUrl() + folder + "/" + originFilename;

        WsFile wf = new WsFile();
        wf.setUser(userId);
        wf.setFolder(folder);
        wf.setFilename(file.getOriginalFilename());
        wf.setDiskPath(uploadConfig.getStorePath() + folder);
        wf.setUrl(viewUrl);
        wf.setFileSize(newFile.length());

        if (originFilename.endsWith("mp3")) {
            String trackLength = MusicParserUtil.getMusicTrackTime(newFile.getAbsolutePath());
            wf.setTrackLength(trackLength);
        }else{
            wf.setTrackLength("");
        }
        wsService.insertMusic(wf);

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
