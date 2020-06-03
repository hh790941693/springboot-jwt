package com.pjb.springbootjwt.zhddkk.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.bean.FileUploadResultBean;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.domain.WsFileDO;
import com.pjb.springbootjwt.zhddkk.entity.PageResponseEntity;
import com.pjb.springbootjwt.zhddkk.enumx.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.enumx.OperationEnum;
import com.pjb.springbootjwt.zhddkk.model.ExtReturn;
import com.pjb.springbootjwt.zhddkk.service.WsFileService;
import com.pjb.springbootjwt.zhddkk.util.CommonUtil;
import com.pjb.springbootjwt.zhddkk.util.JsonUtil;
import com.pjb.springbootjwt.zhddkk.util.MusicParserUtil;
import com.pjb.springbootjwt.zhddkk.util.ServiceUtil;
import com.pjb.springbootjwt.zhddkk.websocket.WebSocketConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件控制器 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("file")
public class FileOperationController 
{
	private Logger logger = LoggerFactory.getLogger(FileOperationController.class);

	@Autowired
	private WsFileService wsFileService;

	@Autowired
	private WebSocketConfig webSocketConfig;

	/**
	 * 音乐播放首页
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.MUSIC,subModule="",describe="音乐播放器首页")
	@RequestMapping("musicPlayer.page")
	public String musicPlayer(Model model,@RequestParam(value="user",required=false) String user)
	{
		model.addAttribute("user", user);
		return "music/musicPlayerVue";
	}
	
	/**
	 * 音乐播放器简版
	 * @param model
	 * @param user
	 * @return
	 */
	@RequestMapping("musicPlayerSimple.page")
	public String musicPlayerSimple(Model model,@RequestParam(value="user",required=false) String user)
	{
		model.addAttribute("user", user);
		return "music/musicPlayerSimple";
	}

	/**
	 * 上传文件首页
	 *
	 * @param fileType
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.MUSIC,subModule="",describe="上传文件首页")
	@RequestMapping("upload.page")
	public String uploadFile(Model model,@RequestParam(value="user",required=false) String user,@RequestParam(value="fileType",required=false) String fileType)
	{
		model.addAttribute("user", user);
		model.addAttribute("fileType", fileType);
		return "music/upload";
	}
	
	/**
	 * 上传文件结果
	 * @param model
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.MUSIC,subModule="",describe="上传文件结果首页")
	@RequestMapping("uploadResult.page")
	public String uploadFile(Model model)
	{
		return "music/uploadResult";
	}	
	
	@OperationLogAnnotation(type=OperationEnum.DELETE,module=ModuleEnum.MUSIC,subModule="",describe="删除文件")
	@RequestMapping("delFile.do")
	@ResponseBody
	@Transactional
	public Object delFile(HttpServletRequest request, @RequestParam(value="id",required=true) int id) {
		WsFileDO wsFileDO = wsFileService.selectById(id);
		if (null != wsFileDO) {
			boolean delFlag = wsFileService.deleteById(id);
			if (delFlag) {
				//删除原文件
				String diskPath = wsFileDO.getDiskPath();
				String url = wsFileDO.getUrl();
				String filename = url.substring(url.lastIndexOf("/")+1);
				File file = new File(diskPath+File.separator+filename);
				if (file.exists() && file.isFile()) {
					logger.info("删除文件:{}"+filename);
					file.delete();
				}
				return "success";
			}
		}
        return "failed";
	}
	
	@OperationLogAnnotation(type=OperationEnum.INSERT,module=ModuleEnum.MUSIC,subModule="",describe="上传文件")
	@RequestMapping("uploadFiles.do")
    @Deprecated
	public Object uploadFiles(@RequestParam MultipartFile[] files,@RequestParam("user")String user,@RequestParam("fileType")String fileType,HttpServletRequest request,Model model) {
        List<FileUploadResultBean> uploadResultList = new ArrayList<FileUploadResultBean>();
		
		if(files.length == 0){
        	return "failed";
        }
        
		String saveRootPath = request.getServletContext().getRealPath(CommonConstants.UPLOAD_PATH_SUFFIX);  //文件保存目录

        for (MultipartFile file:files){
	        String filename= file.getOriginalFilename();
	        if(StringUtils.isBlank(filename)){
	             continue;
	        }
	        FileUploadResultBean fileBean = new FileUploadResultBean();
	        fileBean.setFilename(filename);

	        String fileTypeTmp = filename.substring(filename.lastIndexOf(".")+1, filename.length());
			if (!fileTypeTmp.equals(fileType)) {
				System.out.println(filename+"文件类型不是"+fileType);
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
					String url = request.getScheme() + "://" + request.getServerName() + ":"+request.getServerPort()+request.getContextPath()+"/upload/"+user+"/"+filename;
					wf.setUrl(url);
					
					File f = new File(finalSavaPath + File.separator + filename);
					wf.setFileSize(f.length());
					
					String trackLength = MusicParserUtil.getMusicTrackTime(f.getAbsolutePath());
					wf.setTrackLength(trackLength);
					wsFileService.insert(wf);
				}
				fileBean.setUploadFlag(true);
	        }else {
				fileBean.setUploadFlag(false);
	        }
	        uploadResultList.add(fileBean);
         }
		PageResponseEntity rqe = new PageResponseEntity();
		rqe.setTotalCount(uploadResultList.size());
		rqe.setTotalPage(100);
		rqe.setList(uploadResultList);
	    Object object = JsonUtil.javaobject2Jsonobject(rqe);
		
	    model.addAttribute("result",object);
		return "music/uploadResult";
	}
	
	@OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.MUSIC,subModule="",describe="显示音乐列表")
	@RequestMapping("showFiles.do")
	@ResponseBody
	public Object showFiles(HttpServletRequest request, @RequestParam(value="user",required=false) String user, @RequestParam("fileType") String fileType) {
		List<WsFileDO> fileList = wsFileService.selectList(new EntityWrapper<WsFileDO>().eq("user", user).eq("folder", "music"));
		List<WsFileDO> finalList = new ArrayList<WsFileDO>();
		String webserverip = webSocketConfig.getAddress();

		List<WsFileDO> needBatchUpdateList = new ArrayList<>();
		for (WsFileDO wsFileDO : fileList) {
			String url = wsFileDO.getUrl();
			String oldIp = url.substring(url.indexOf("//")+2, url.lastIndexOf(":"));
			if (!oldIp.equals(webserverip)) {
				String newUrl = url.replace(oldIp, webserverip);
				wsFileDO.setUrl(newUrl);
				needBatchUpdateList.add(wsFileDO);
			}
		}
		if (needBatchUpdateList.size()>0){
			wsFileService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
		}

		for (WsFileDO wsFileDO : fileList) {
			String url = wsFileDO.getUrl();
			if (CommonUtil.testUrl(url)) {
				wsFileDO.setAccessStatus("1");
			}else{
				wsFileDO.setAccessStatus("0");
			}
			finalList.add(wsFileDO);
		}
		
		ExtReturn er = new ExtReturn();
		er.setDataList(finalList);
		er.setTotal(finalList.size());
		er.setSuccess(true);
		return JsonUtil.javaobject2Jsonobject(er);
	}
}
