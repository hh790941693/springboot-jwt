package com.pjb.springbootjwt.zhddkk.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.common.uploader.config.UploadConfig;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.domain.WsFileDO;
import com.pjb.springbootjwt.zhddkk.constants.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.constants.OperationEnum;
import com.pjb.springbootjwt.zhddkk.domain.WsUserFileDO;
import com.pjb.springbootjwt.zhddkk.service.CacheService;
import com.pjb.springbootjwt.zhddkk.service.WsFileService;
import com.pjb.springbootjwt.zhddkk.service.WsUserFileService;
import com.pjb.springbootjwt.zhddkk.util.SessionUtil;
import com.pjb.springbootjwt.zhddkk.websocket.WebSocketConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
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
public class FileOperationController extends AdminBaseController {
    private Logger logger = LoggerFactory.getLogger(FileOperationController.class);
    
    @Autowired
    private WsFileService wsFileService;
    
    @Autowired
    private WebSocketConfig webSocketConfig;
    
    @Autowired
    private UploadConfig uploadConfig;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private WsUserFileService wsUserFileService;

    /**
     * 音乐播放首页.
     * 
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.MUSIC, subModule = "", describe = "音乐播放器首页")
    @RequestMapping("musicPlayer.page")
    public String musicPlayer(Model model) {
        String userId = SessionUtil.getSessionUserId();
        if (StringUtils.isNotBlank(userId)) {
            List<WsUserFileDO> list = wsUserFileService.selectList(new EntityWrapper<WsUserFileDO>().eq("user_id", userId).isNotNull("category"));
            if (null != list) {
                List<String> categoryList = list.stream().map(WsUserFileDO::getCategory).distinct().collect(Collectors.toList());
                if (null != categoryList && categoryList.size() > 0) {
                    model.addAttribute("categoryList", categoryList);
                }
            }
        }
        return "music/musicPlayerVue";
    }

    /**
     * 音乐绑定页面
     *
     * @return
     */
    @RequestMapping("musicAdd.page")
    public String musicAdd() {
        return "music/musicAdd";
    }

    /**
     * 文件管理页面
     *
     * @return
     */
    @RequestMapping("fileManage.page")
    public String fileManage() {
        return "music/fileManage";
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

    /**
     * 音乐播放列表
     * @param fileType
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.MUSIC, subModule = "", describe = "显示音乐列表")
    @RequestMapping("showFiles.do")
    @ResponseBody
    // @Cacheable(value="musicList") 需要启动redis才可以
    public Result<List<WsUserFileDO>> showFiles(@RequestParam("fileType") String fileType) {
        String userName = SessionUtil.getSessionUserName();
        String userId = SessionUtil.getSessionUserId();

        Page<WsUserFileDO> page = new Page<>(1, 9999);
        List<WsUserFileDO> fileList = wsUserFileService.selectUserFileList(page, new EntityWrapper<WsUserFileDO>().eq("t1.user_id", userId).eq("t1.status", 1));

        return Result.ok(fileList);
    }

    /**
     * 绑定音乐->用户未添加的音乐
     * @param wsFileDO
     * @return
     */
    @RequestMapping("listAllMusic.do")
    @ResponseBody
    public Result<Page<WsFileDO>> showUserFileList(WsFileDO wsFileDO) {
        String userId = SessionUtil.getSessionUserId();
        List<WsUserFileDO> wsUserFileDOList = wsUserFileService.selectList(new EntityWrapper<WsUserFileDO>().eq("user_id", userId));
        List<Long> ignoreFileIdList = wsUserFileDOList.stream().map(WsUserFileDO::getFileId).collect(Collectors.toList());
        Page<WsFileDO> page = wsFileService.selectPage(getPage(WsFileDO.class), new EntityWrapper<WsFileDO>().eq("folder", "music").notIn("id", ignoreFileIdList));
        return Result.ok(page);
    }

    /**
     * 绑定音乐->用户已添加的音乐
     * @param wsUserFileDO
     * @return
     */
    @RequestMapping("listUserFile.do")
    @ResponseBody
    public Result<Page<WsUserFileDO>> showUserFileList(WsUserFileDO wsUserFileDO) {
        Page<WsUserFileDO> page = getPage(WsUserFileDO.class);
        String userId = SessionUtil.getSessionUserId();
        List<WsUserFileDO> fileList = wsUserFileService.selectUserFileList(page, new EntityWrapper<WsUserFileDO>().eq("t1.user_id", userId).eq("t1.status", 1));
        page.setRecords(fileList);
        return Result.ok(page);
    }

    /**
     * 绑定音乐->批量添加->添加到用户的文件列表
     * @param ids
     * @return
     */
    @RequestMapping("batchAddMusic.do")
    @ResponseBody
    public Result<String> batchAddMusic(@RequestParam("ids[]") Integer[] ids) {
        List<WsFileDO> wsFileDOList = wsFileService.selectList(new EntityWrapper<WsFileDO>().in("id", ids));
        List<WsUserFileDO> wsUserFileDOList = new ArrayList<>();
        if (null != wsFileDOList && wsFileDOList.size() > 0) {
            wsFileDOList.forEach(fl->{
                WsUserFileDO wuf = new WsUserFileDO();
                wuf.setFileId(Long.valueOf(fl.getId()));
                wuf.setFileName(fl.getFilename());
                wuf.setFileUrl(fl.getUrl());
                wuf.setCreateTime(new Date());
                wuf.setUpdateTime(new Date());
                wuf.setUserId(Long.valueOf(SessionUtil.getSessionUserId()));
                wuf.setUserName(SessionUtil.getSessionUserName());
                wsUserFileDOList.add(wuf);
            });

            List<WsUserFileDO> delWsUserFileDOList = wsUserFileService.selectList(new EntityWrapper<WsUserFileDO>().in("file_id", ids));
            if (null != delWsUserFileDOList && delWsUserFileDOList.size() > 0) {
                wsUserFileService.deleteBatchIds(delWsUserFileDOList.stream().map(WsUserFileDO::getId).collect(Collectors.toList()));
            }
            wsUserFileService.insertBatch(wsUserFileDOList, wsUserFileDOList.size());
        }
        return Result.ok();
    }

    /**
     * 绑定音乐->批量删除->从用户文件列表移除文件
     * @param ids
     * @return
     */
    @PostMapping("/batchRemoveMusic.do")
    @ResponseBody
    public Result<String> batchRemove(@RequestParam("ids[]") Long[] ids) {
        wsUserFileService.deleteBatchIds(Arrays.asList(ids));
        return Result.ok();
    }

    /**
     * 绑定音乐->批量删除->从用户文件列表移除文件
     * @param ids
     * @return
     */
    @PostMapping("/bindCategory.do")
    @ResponseBody
    public Result<String> bindCategory(@RequestParam("ids[]") Long[] ids, String category) {
        List<WsUserFileDO> wsUserFileDOList = wsUserFileService.selectList(new EntityWrapper<WsUserFileDO>().in("id",Arrays.asList(ids)));
        wsUserFileDOList.forEach(uf->uf.setCategory(category));
        wsUserFileService.updateBatchById(wsUserFileDOList, wsUserFileDOList.size());
        return Result.ok();
    }

    /**
     * 文件管理->文件列表
     * @param wsFileDO
     * @return
     */
    @RequestMapping("listFile.do")
    @ResponseBody
    public Result<Page<WsFileDO>> listFile(WsFileDO wsFileDO) {
        Wrapper<WsFileDO> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotBlank(wsFileDO.getFolder())) {
            wrapper.eq("folder", wsFileDO.getFolder());
        }
        Page<WsFileDO> page = wsFileService.selectPage(getPage(WsFileDO.class), wrapper);
        return Result.ok(page);
    }

    /**
     * 文件管理->删除文件(包括删除本地物理文件)
     * @param id
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.DELETE, module = ModuleEnum.MUSIC, subModule = "", describe = "删除音乐文件")
    @RequestMapping("delFile.do")
    @ResponseBody
    @Transactional
    public Result<String> delFile(@RequestParam(value = "id") int id) {
        Integer[] ids = new Integer[1];
        ids[0] = id;
        return batchDelFile(ids);
    }

    /**
     * 文件管理->批量删除文件(包括删除本地物理文件)
     * @param ids
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.DELETE, module = ModuleEnum.MUSIC, subModule = "", describe = "批量删除音乐文件")
    @RequestMapping("batchDelFile.do")
    @ResponseBody
    @Transactional
    public Result<String> batchDelFile(@RequestParam("ids[]") Integer[] ids) {
        List<WsFileDO> wsFileDOList = wsFileService.selectBatchIds(Arrays.asList(ids));
        int totalNum = ids.length;
        int failedNum = 0;
        for (WsFileDO wsFileDO : wsFileDOList) {
            int id = wsFileDO.getId();
            boolean delFlag = wsFileService.deleteById(id);
            wsUserFileService.delete(new EntityWrapper<WsUserFileDO>().eq("file_id", wsFileDO.getId()));
            if (delFlag) {
                // 删除原文件
                String diskPath = wsFileDO.getDiskPath();
                String dymicDiskPath = uploadConfig.getStorePath() + File.separator + wsFileDO.getFolder();
                logger.info("diskPath:{}  dymicDiskPath:{}", diskPath, dymicDiskPath);
                String url = wsFileDO.getUrl();
                String filename = url.substring(url.lastIndexOf("/") + 1);
                File file = null;
                if (diskPath.equals(dymicDiskPath)) {
                    file = new File(diskPath + File.separator + filename);
                } else {
                    file = new File(dymicDiskPath + File.separator + filename);
                }
                if (null != file && file.exists() && file.isFile()) {
                    logger.info("删除文件:{} {} ", id, file.getAbsolutePath());
                    try {
                        file.delete();
                    } catch (Exception e) {
                        failedNum++;
                        logger.error("删除文件失败:{} {}",id, file.getAbsolutePath());
                    }
                }
            } else {
                failedNum++;
            }
        }
        cacheService.cacheUserFileData();
        String resultMsg = "";
        if (failedNum > 0) {
            resultMsg = "总计:" + totalNum + " 删除失败:" + failedNum;
        } else {
            resultMsg = "总计:" + totalNum + " 删除成功";
        }
        return Result.ok(resultMsg);
    }
}
