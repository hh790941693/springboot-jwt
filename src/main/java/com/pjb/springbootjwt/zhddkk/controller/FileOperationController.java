package com.pjb.springbootjwt.zhddkk.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.bean.FileUploadResultBean;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.domain.WsFileDO;
import com.pjb.springbootjwt.zhddkk.entity.PageResponseEntity;
import com.pjb.springbootjwt.zhddkk.entity.WsFile;
import com.pjb.springbootjwt.zhddkk.enumx.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.enumx.OperationEnum;
import com.pjb.springbootjwt.zhddkk.interceptor.WsInterceptor;
import com.pjb.springbootjwt.zhddkk.model.ExtReturn;
import com.pjb.springbootjwt.zhddkk.service.WsFileService;
import com.pjb.springbootjwt.zhddkk.service.WsService;
import com.pjb.springbootjwt.zhddkk.util.JsonUtil;
import com.pjb.springbootjwt.zhddkk.util.MusicParserUtil;
import com.pjb.springbootjwt.zhddkk.util.ServiceUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	private static Map<String,String> configMap = WsInterceptor.getConfigMap();
	
	@Autowired
	private WsService wsService;

	@Autowired
	private WsFileService wsFileService;
	
	/**
	 * 音乐播放首页
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.MUSIC,subModule="",describe="音乐播放器首页")
	@RequestMapping("musicPlayer.page")
	public String musicPlayer(Model model,@RequestParam(value="user",required=false) String user)
	{
		model.addAttribute("user", user);
		return "dealfile/musicPlayer";
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
		return "dealfile/musicPlayerSimple";
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
		return "dealfile/upload";
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
		return "dealfile/uploadResult";
	}	
	
	@OperationLogAnnotation(type=OperationEnum.DELETE,module=ModuleEnum.MUSIC,subModule="",describe="删除文件")
	@RequestMapping("delFile.do")
	@ResponseBody
	public Object delFile(HttpServletRequest request, @RequestParam(value="id",required=true) int id) {
		wsService.deleteMusic(id);
		return "success";
	}
	
	@OperationLogAnnotation(type=OperationEnum.INSERT,module=ModuleEnum.MUSIC,subModule="",describe="上传文件")
	@RequestMapping("uploadFiles.do")
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
				fileBean.setUploadResult("上传失败");
				fileBean.setFailedReason("文件类型不是mp3");
				uploadResultList.add(fileBean);
				continue;
			}
			
	        String finalSavaPath = saveRootPath + File.separator + user;
	        String storeResult = ServiceUtil.storeFile(request, finalSavaPath, file, filename);
	        if (storeResult.equals(CommonConstants.SUCCESS)) {
				if (user != null) {
					WsFile wf = new WsFile();
					wf.setUser(user);
					wf.setFilename(filename);
					wf.setDiskPath(finalSavaPath);
					String url = request.getScheme() + "://" + request.getServerName() + ":"+request.getServerPort()+request.getContextPath()+"/upload/"+user+"/"+filename;
					wf.setUrl(url);
					
					File f = new File(finalSavaPath + File.separator + filename);
					wf.setFileSize(f.length());
					
					String trackLength = MusicParserUtil.getMusicTrackTime(f.getAbsolutePath());
					wf.setTrackLength(trackLength);
					wsService.insertMusic(wf);
				}
				fileBean.setUploadFlag(true);
				fileBean.setUploadResult("上传成功");
	        }else {
				fileBean.setUploadFlag(false);
				fileBean.setUploadResult("上传失败");
	        }
	        uploadResultList.add(fileBean);
         }
		PageResponseEntity rqe = new PageResponseEntity();
		rqe.setTotalCount(uploadResultList.size());
		rqe.setTotalPage(100);
		rqe.setList(uploadResultList);
	    Object object = JsonUtil.javaobject2Jsonobject(rqe);
		
	    model.addAttribute("result",object);
		return "dealfile/uploadResult";
	}
	
	@OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.MUSIC,subModule="",describe="显示音乐列表")
	@RequestMapping("showFiles.do")
	@ResponseBody
	public Object showFiles(HttpServletRequest request, @RequestParam(value="user",required=false) String user, @RequestParam("fileType") final String fileType) {
		List<WsFileDO> mlist = wsFileService.selectList(new EntityWrapper<WsFileDO>().eq("user", user).eq("folder", "music"));
		
		String webserverip = ServiceUtil.getWebsocketIp(configMap);
		List<WsFileDO> finallist = new ArrayList<WsFileDO>();
		for (WsFileDO w : mlist) {
			File mf = new File(w.getDiskPath()+File.separator+w.getFilename());
			if (mf.isFile()) {
				String musicUrl = w.getUrl();
				String oldIp = musicUrl.substring(musicUrl.indexOf("//")+2, musicUrl.lastIndexOf(":"));
				if (!oldIp.equals(webserverip)) {
					String newMusicUrl = musicUrl.replace(oldIp, webserverip);
					w.setUrl(newMusicUrl);
				}
				finallist.add(w);
			}else {
				wsService.deleteMusic(w.getId());
			}
		}
		
		ExtReturn er = new ExtReturn();
		er.setDataList(finallist);
		er.setTotal(finallist.size());
		er.setSuccess(true);
		return JsonUtil.javaobject2Jsonobject(er);
	}
}
