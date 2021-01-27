package com.pjb.springbootjwt.zhddkk.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.common.uploader.config.UploadConfig;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.bean.FileUploadResultBean;
import com.pjb.springbootjwt.zhddkk.cache.CoreCache;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.domain.WsCommonDO;
import com.pjb.springbootjwt.zhddkk.domain.WsFileDO;
import com.pjb.springbootjwt.zhddkk.entity.PageResponseEntity;
import com.pjb.springbootjwt.zhddkk.constants.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.constants.OperationEnum;
import com.pjb.springbootjwt.zhddkk.service.WsFileService;
import com.pjb.springbootjwt.zhddkk.util.JsonUtil;
import com.pjb.springbootjwt.zhddkk.util.MusicParserUtil;
import com.pjb.springbootjwt.zhddkk.util.ServiceUtil;
import com.pjb.springbootjwt.zhddkk.websocket.WebSocketConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
    public String musicPlayer(Model model, @RequestParam(value = "user", required = false) String user) {
        model.addAttribute("user", user);
        return "music/musicPlayerVue";
    }
    
    /**
     * 音乐播放器简版.
     * 
     * @param model
     * @param user
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.MUSIC, subModule = "", describe = "简易音乐播放器首页")
    @RequestMapping("musicPlayerSimple.page")
    public String musicPlayerSimple(Model model, @RequestParam(value = "user", required = false) String user) {
        model.addAttribute("user", user);
        return "music/musicPlayerSimple";
    }
    
    /**
     * 上传文件首页.
     *
     * @param fileType
     * @return
     */
    @RequestMapping("upload.page")
    public String uploadFile(Model model, @RequestParam(value = "user", required = false) String user,
        @RequestParam(value = "fileType", required = false) String fileType) {
        model.addAttribute("user", user);
        model.addAttribute("fileType", fileType);
        return "music/upload";
    }
    
    /**
     * 上传文件结果.
     * 
     * @param model
     * @return
     */
    @RequestMapping("uploadResult.page")
    public String uploadFile(Model model) {
        return "music/uploadResult";
    }
    
    @OperationLogAnnotation(type = OperationEnum.DELETE, module = ModuleEnum.MUSIC, subModule = "", describe = "删除音乐文件")
    @RequestMapping("delFile.do")
    @ResponseBody
    @Transactional
    public Object delFile(HttpServletRequest request, @RequestParam(value = "id", required = true) int id) {
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
                return CommonConstants.SUCCESS;
            }
        }
        return CommonConstants.FAIL;
    }
    
    @RequestMapping("uploadFiles.do")
    @Deprecated
    public Object uploadFiles(@RequestParam MultipartFile[] files, @RequestParam("user") String user,
        @RequestParam("fileType") String fileType, HttpServletRequest request, Model model) {
        List<FileUploadResultBean> uploadResultList = new ArrayList<FileUploadResultBean>();
        
        if (files.length == 0) {
            return CommonConstants.FAIL;
        }
        
        String saveRootPath = request.getServletContext().getRealPath(CommonConstants.UPLOAD_PATH_SUFFIX); // 文件保存目录
        
        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            if (StringUtils.isBlank(filename)) {
                continue;
            }
            FileUploadResultBean fileBean = new FileUploadResultBean();
            fileBean.setFilename(filename);
            
            String fileTypeTmp = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
            if (!fileTypeTmp.equals(fileType)) {
                System.out.println(filename + "文件类型不是" + fileType);
                fileBean.setUploadFlag(false);
                uploadResultList.add(fileBean);
                continue;
            }
            
            String finalSavaPath = saveRootPath + File.separator + user;
            String storeResult = ServiceUtil.storeFile(request, finalSavaPath, file, filename);
            if (storeResult.equals(CommonConstants.SUCCESS)) {
                if (user != null) {
                    WsFileDO wf = new WsFileDO();
                    wf.setUser(user);
                    wf.setFolder("music");
                    wf.setFilename(filename);
                    wf.setDiskPath(finalSavaPath);
                    String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                        + request.getContextPath() + "/upload/" + user + "/" + filename;
                    wf.setUrl(url);
                    
                    File f = new File(finalSavaPath + File.separator + filename);
                    wf.setFileSize(f.length());
                    
                    String trackLength = MusicParserUtil.getMusicTrackTime(f.getAbsolutePath());
                    wf.setTrackLength(trackLength);
                    wsFileService.insert(wf);
                }
                fileBean.setUploadFlag(true);
            } else {
                fileBean.setUploadFlag(false);
            }
            uploadResultList.add(fileBean);
        }
        PageResponseEntity rqe = new PageResponseEntity();
        rqe.setTotalCount(uploadResultList.size());
        rqe.setTotalPage(100);
        rqe.setList(uploadResultList);
        Object object = JsonUtil.javaobject2Jsonobject(rqe);
        
        model.addAttribute("result", object);
        return "music/uploadResult";
    }
    
    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.MUSIC, subModule = "", describe = "显示音乐列表")
    @RequestMapping("showFiles.do")
    @ResponseBody
    // @Cacheable(value="musicList") 需要启动redis才可以
    public Result<List<WsFileDO>> showFiles(HttpServletRequest request,
        @RequestParam(value = "user", required = false) String user, @RequestParam("fileType") String fileType) {
        List<WsFileDO> fileList = CoreCache.getInstance().getUserFileList();
        if (null != fileList && fileList.size() > 0) {
            fileList = fileList.stream().filter(cache -> StringUtils.isNotBlank(cache.getUser()) && cache.getUser().equals(user)).filter(cache -> StringUtils.isNotBlank(cache.getFolder()) && cache.getFolder().equals(fileType)).sorted(Comparator.comparing(WsFileDO::getId)).collect(Collectors.toList());
        } else {
            fileList = wsFileService.selectList(new EntityWrapper<WsFileDO>().eq("user", user).eq("folder", fileType));
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
