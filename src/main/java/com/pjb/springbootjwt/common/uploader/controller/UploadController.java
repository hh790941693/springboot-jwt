package com.pjb.springbootjwt.common.uploader.controller;

import com.pjb.springbootjwt.common.uploader.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/upload/app")
@Slf4j
public class UploadController {

    private static Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private UploadService uploadService;

    /**
     * 上传资源文件
     *
     * @param files   文件
     * @param folder 要存放的路径，如果为空，则默认存放temp文件夹
     * @param userId 用戶ID
     * @return 上传文件的完整访问路径
     */
    @PostMapping
    public Map<String, Object> upload(
            @RequestParam(required = true)MultipartFile[] files,
            @RequestParam(required = false)String folder,
            @RequestParam(required = false)String userId
    ) {
        logger.info("进入上传文件控制层, 目录名:{} 用户ID:{}", folder, userId);
        Map<String, Object> map = new HashMap<>();
        String url = "";
        try {
            if (null != files && files.length>0){
                logger.info("开始上传文件");
                for (int i=0;i<files.length;i++ ){
                    MultipartFile file = files[i];
                    if (!file.isEmpty()){
                        logger.info("开始上传文件:{}", file.getOriginalFilename());
                        url = uploadService.uploadFile(file, folder, userId);
                        logger.info("返回的文件url:{}", url);

                        map.put("code", "1");
                        map.put("msg", "success");
                        map.put("data", url);
                    }
                }
            }
        } catch (Exception e) {
            logger.info("上传出现异常:{}",e.getMessage());
            e.printStackTrace();
            map.put("code", "0");
            map.put("msg", "error");
            map.put("data", "");
        }
        return map;
    }
}