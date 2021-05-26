package com.pjb.springbootjwt.common.uploader.controller;

import com.pjb.springbootjwt.common.uploader.service.UploadService;
import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.bean.FileUploadResultBean;
import com.pjb.springbootjwt.zhddkk.service.CacheService;
import com.pjb.springbootjwt.zhddkk.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.helper.StringUtil;
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

    @Autowired
    private CacheService cacheService;

    /**
     * 上传资源文件
     *
     * @param file   文件
     * @param folder 要存放的路径，如果为空，则默认存放temp文件夹
     * @return 上传文件的完整访问路径
     */
    @PostMapping("/uploadFile")
    public Result<String> upload(
            @RequestParam(required = true)MultipartFile file,
            @RequestParam(required = false)String folder,
            @RequestParam(required = false)String user
    ) {
        logger.info("进入上传文件控制层, 目录名:{}", folder);
        Map<String, Object> map = new HashMap<>();
        String url = "";
        String userName = SessionUtil.getSessionUserName();
        if (StringUtil.isBlank(userName)) {
            userName = user;
        }
        try {
            if (null != file){
                logger.info("开始上传文件");
                if (!file.isEmpty()){
                    logger.info("开始上传文件:{}", file.getOriginalFilename());
                    url = uploadService.uploadFile(file, folder, userName);
                    logger.info("返回的文件url:{}", url);
                    return Result.ok(url);
                }
            }
        } catch (Exception e) {
            logger.info("上传出现异常:{}",e.getMessage());
            e.printStackTrace();
        }
        return Result.fail();
    }

    /**
     * 上传音乐文件
     *
     * @param file   文件
     * @param folder 要存放的路径，如果为空，则默认存放temp文件夹
     * @return 上传文件的完整访问路径
     */
    @PostMapping("/uploadMusic")
    public Map<String, FileUploadResultBean> uploadMusic(
            @RequestParam(required = true)MultipartFile[] file,
            @RequestParam(required = false, defaultValue = "music")String folder
    ) {
        logger.info("进入上传文件控制层, 目录名:{}", folder);
        Map<String, FileUploadResultBean> map = new HashMap<>();
        String url = "";
        String userName = SessionUtil.getSessionUserName();
        try {
            if (null != file && file.length>0){
                logger.info("开始上传文件");
                for (int i=0;i<file.length;i++ ){
                    MultipartFile tempfile = file[i];
                    if (!tempfile.isEmpty()) {
                        String originFilename = tempfile.getOriginalFilename();
                        try {
                            logger.info("开始上传文件:{}", originFilename);
                            url = uploadService.uploadFile(tempfile, folder, userName);
                            logger.info("返回的文件url:{}", url);
                            FileUploadResultBean fileUploadResultBean = new FileUploadResultBean(originFilename, true, url);
                            map.put(originFilename, fileUploadResultBean);
                        }catch (Exception e){
                            logger.info("上传文件异常:{}",originFilename);
                            FileUploadResultBean fileUploadResultBean = new FileUploadResultBean(originFilename, false, "");
                            map.put(originFilename, fileUploadResultBean);
                        }
                    }
                }
                // 更新缓存
                cacheService.cacheUserFileData();
            }
        } catch (Exception e) {
            logger.info("上传出现异常:{}",e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 上传资源文件(供新的文本编辑器Froala使用)
     *
     * @param file   文件
     * @param folder 要存放的路径，如果为空，则默认存放temp文件夹
     * @return 上传文件的完整访问路径
     */
    @PostMapping("/uploadByFroala")
    public Map<String, Object> uploadByFroala(
            @RequestParam(required = true)MultipartFile file,
            @RequestParam(required = false)String folder
    ) {
        logger.info("进入上传文件控制层, 目录名:{}", folder);
        Map<String, Object> map = new HashMap<>();
        String url = "";
        String userName = SessionUtil.getSessionUserName();
        try {
            if (null != file){
                logger.info("开始上传文件");
                if (!file.isEmpty()){
                    logger.info("开始上传文件:{}", file.getOriginalFilename());
                    url = uploadService.uploadFile(file, folder, userName);
                    logger.info("返回的文件url:{}", url);
                    map.put("code", "1");
                    map.put("msg", "操作成功");
                    map.put("link", url);
                }
            }
        } catch (Exception e) {
            logger.info("上传出现异常:{}",e.getMessage());
            e.printStackTrace();
            map.put("code", "0");
            map.put("msg", "操作失败");
            map.put("link", "");
        }
        return map;
    }
}