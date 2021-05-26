package com.pjb.springbootjwt.zhddkk.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.common.uploader.config.UploadConfig;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.cache.CoreCache;
import com.pjb.springbootjwt.zhddkk.domain.WsFileDO;
import com.pjb.springbootjwt.zhddkk.constants.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.constants.OperationEnum;
import com.pjb.springbootjwt.zhddkk.service.WsFileService;
import com.pjb.springbootjwt.zhddkk.util.SessionUtil;
import com.pjb.springbootjwt.zhddkk.websocket.WebSocketConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 文件控制器.
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("file")
public class FileOperationController {
    private Logger logger = LoggerFactory.getLogger(FileOperationController.class);
    
    @Autowired
    private WsFileService wsFileService;
    
    @Autowired
    private WebSocketConfig webSocketConfig;
    
    @Autowired
    private UploadConfig uploadConfig;

    /**
     * 音乐播放首页.
     * 
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.MUSIC, subModule = "", describe = "音乐播放器首页")
    @RequestMapping("musicPlayer.page")
    public String musicPlayer() {
        return "music/musicPlayerVue";
    }
    
    /**
     * 音乐播放器简版.
     *
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.MUSIC, subModule = "", describe = "简易音乐播放器首页")
    @RequestMapping("musicPlayerSimple.page")
    public String musicPlayerSimple() {
        return "music/musicPlayerSimple";
    }

    @OperationLogAnnotation(type = OperationEnum.DELETE, module = ModuleEnum.MUSIC, subModule = "", describe = "删除音乐文件")
    @RequestMapping("delFile.do")
    @ResponseBody
    @Transactional
    public Result<String> delFile(@RequestParam(value = "id") int id) {
        WsFileDO wsFileDO = wsFileService.selectById(id);
        if (null != wsFileDO) {
            boolean delFlag = wsFileService.deleteById(id);
            if (delFlag) {
                // 删除原文件
                String diskPath = wsFileDO.getDiskPath();
                String dymicDiskPath = uploadConfig.getStorePath() + File.separator + wsFileDO.getFolder();
                logger.info("diskPath:" + diskPath);
                logger.info("dymicDiskPath:" + dymicDiskPath);
                String url = wsFileDO.getUrl();
                String filename = url.substring(url.lastIndexOf("/") + 1);
                File file = null;
                if (diskPath.equals(dymicDiskPath)) {
                    file = new File(diskPath + File.separator + filename);
                } else {
                    file = new File(dymicDiskPath + File.separator + filename);
                }
                if (null != file && file.exists() && file.isFile()) {
                    logger.info("删除文件:{} " + file.getAbsolutePath());
                    file.delete();
                }
                return Result.ok();
            }
        }
        return Result.fail();
    }

    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.MUSIC, subModule = "", describe = "显示音乐列表")
    @RequestMapping("showFiles.do")
    @ResponseBody
    // @Cacheable(value="musicList") 需要启动redis才可以
    public Result<List<WsFileDO>> showFiles(@RequestParam("fileType") String fileType) {
        String userName = SessionUtil.getSessionUserName();
        List<WsFileDO> fileList = CoreCache.getInstance().getUserFileList();
        if (null != fileList && fileList.size() > 0) {
            fileList = fileList.stream().filter(cache -> StringUtils.isNotBlank(cache.getUser()) && cache.getUser().equals(userName)).filter(cache -> StringUtils.isNotBlank(cache.getFolder()) && cache.getFolder().equals(fileType)).sorted(Comparator.comparing(WsFileDO::getId)).collect(Collectors.toList());
        } else {
            fileList = wsFileService.selectList(new EntityWrapper<WsFileDO>().eq("user", userName).eq("folder", fileType));
        }
        
        List<WsFileDO> needBatchUpdateList = new ArrayList<>();
        String webserverip = webSocketConfig.getAddress();
        for (WsFileDO wsFileDO : fileList) {
            String url = wsFileDO.getUrl();
            String oldIp = url.substring(url.indexOf("//") + 2, url.lastIndexOf(":"));
            if (!oldIp.equals(webserverip)) {
                String newUrl = url.replace(oldIp, webserverip);
                wsFileDO.setUrl(newUrl);
                needBatchUpdateList.add(wsFileDO);
            }
        }
        if (needBatchUpdateList.size() > 0) {
            wsFileService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
        }

        return Result.ok(fileList);
    }
}
