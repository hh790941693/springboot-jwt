package com.jscxrz.zhddkk.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jscxrz.zhddkk.constants.CommonConstants;
import com.jscxrz.zhddkk.dao.WsDao;
import com.jscxrz.zhddkk.entity.WsFile;
import com.jscxrz.zhddkk.util.MusicParserUtil;

public class UploadFileServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	private WsDao wsDao;
	
	public void init(ServletConfig config) throws ServletException {

		super.init(config);

		WebApplicationContext  applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());

		wsDao = (WsDao)applicationContext.getBean(WsDao.class);
		}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	{
		System.out.println("UploadFileServlet doGet called");
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	{
		System.out.println("UploadFileServlet doPost called");
		
		String savePath = this.getServletContext().getRealPath(CommonConstants.UPLOAD_PATH_SUFFIX);  //文件保存目录
		String tempPath = this.getServletContext().getRealPath(CommonConstants.UPLOAD_TEMP_SUFFIX);    //文件临时存放目录
		File savefile = new File(savePath);
		File tempFile = new File(tempPath);
		if (!savefile.exists() && !savefile.isDirectory()){
			savefile.mkdirs();
		}
		if (!tempFile.exists() && !tempFile.isDirectory()){
			tempFile.mkdirs();
		}
		
		String message = "文件上传失败";
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024*100); //缓冲区大小100KB
		factory.setRepository(tempFile);  //设置临时保存目录
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8"); //解决上传文件名的中文乱码问题
		upload.setFileSizeMax(1024*1024*1000);  //最大上传文件大小 1MB
		upload.setSizeMax(1024*1024*1000);  //设置上传文件总量的最大值
		if (!ServletFileUpload.isMultipartContent(req)){
			message =  "文件不合法";
			return;
		}
		
		// 文件上传速度
		upload.setProgressListener(new ProgressListener(){
			@Override
			public void update(long byteRead, long contentLength, int arg2) {
				//float totalLen = contentLength/1024/1024;
				//float readLen = byteRead/1024/1024;
				//System.out.println("文件大小为:"+ totalLen + " M,当前已处理"+readLen+" M");
			}
		});
		
		List<FileItem> list;
		InputStream in = null;
		FileOutputStream out = null;
		String user = null;
		String fileType = null;
		try {
			list = upload.parseRequest(req);
			for (FileItem item : list){
				String filename = item.getName();
				try{
					if (item.isFormField()){
						String name = item.getFieldName();
						String value = null;
						try {
							value = item.getString("UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						//System.out.println(name + " = " + value);
						if (name.equals("user")) {
							user = value;
						}else if (name.equals("fileType")) {
							fileType = value;
						}
					}else{
						if (filename == null || filename.trim().equals("")){
							continue;
						}
						System.out.println("待上传文件:" + filename);


						filename = filename.substring(filename.lastIndexOf("\\")+1);
						if (fileType != null) {
							// 检查文件类型是否正确
							String fileTypeTmp = filename.substring(filename.lastIndexOf(".")+1, filename.length());
							if (!fileTypeTmp.equals(fileType)) {
								System.out.println(filename+"文件类型不符,当前支持类型为"+fileType);
								continue;
							}
						}

						String finalSavaPath = null;
						if (user == null) {
							out = new FileOutputStream(savePath + File.separator + filename);
						}else {
							finalSavaPath = savePath + File.separator + user;
							File ftmp = new File(finalSavaPath);
							if (!ftmp.exists() && !ftmp.isDirectory()) {
								ftmp.mkdirs();
							}
							out = new FileOutputStream(finalSavaPath + File.separator + filename);
						}
						
						in = item.getInputStream();
						byte buffer[] = new byte[1024];
						int len = 0;
						while ((len=in.read(buffer))>0){
							out.write(buffer, 0, len);
						}
						item.delete();
						message = filename + "上传成功";
						
						if (user != null && finalSavaPath != null) {
							WsFile wf = new WsFile();
							wf.setUser(user);
							wf.setFilename(filename);
							wf.setDiskPath(finalSavaPath);
							String url = req.getScheme() + "://" + req.getServerName() + ":"+req.getServerPort()+req.getContextPath()+"/upload/"+user+"/"+filename;
							wf.setUrl(url);
							
							File f = new File(finalSavaPath + File.separator + filename);
							wf.setFileSize(f.length());
							
							String trackLength = MusicParserUtil.getMusicTrackTime(f.getAbsolutePath());
							wf.setTrackLength(trackLength);
							wsDao.insertMusic(wf);
						}
					}
				}catch (Exception e){
					message = filename + "上传失败";
				}
			}
		} catch (FileUploadException e) {
			message = e.getMessage();
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
			message = e.getMessage();
		}finally{
			if (null != in){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
					message = "关闭文件流in失败";
				}
			}
			if (null != out){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
					message = "关闭文件流out失败";
				}
			}
		}

		System.out.println(message);
		req.setAttribute("result",message);
		try {
			req.getRequestDispatcher("/uploadResult.page").forward(req, resp);
		} catch (Exception e) {
			System.out.println("跳转至文件上传结果页面失败!"+e.getMessage());
			e.printStackTrace();
		} 
	}
}
