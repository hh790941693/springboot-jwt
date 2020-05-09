package com.jscxrz.zhddkk.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jscxrz.zhddkk.constants.CommonConstants;

public class DownloadFileServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;


	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	{
		System.out.println("DownloadFileServlet doGet called");
		String message = "下载失败";
		String filename = req.getParameter("filename");
		try {
			filename = new String(filename.getBytes("ISO8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String rootPath = this.getServletContext().getRealPath(CommonConstants.UPLOAD_PATH_SUFFIX);
		File rootPathFile = new File(rootPath);
		if (!rootPathFile.exists()){
			message = "文件不存在";
			return;
		}
		// 判断待下载文件是否存在
		String downfilePath = findFileDigui(rootPathFile, filename);
		if (null == downfilePath){
			message = "文件不存在";
			return;
		}
		
		File file = new File(downfilePath);
		if (!file.exists()){
			message = "文件不存在";
			return;
		}
		
		try {
			filename = URLEncoder.encode(filename,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		resp.setHeader("content-disposition", "attachment;filename=" + filename);
		FileInputStream in = null;
		OutputStream out = null;
		try {
			 in = new FileInputStream(downfilePath);
			 out = resp.getOutputStream();
			 byte buffer[] = new byte[1024];
			 int len = 0;
			 while ((len = in.read(buffer)) > 0){
				 out.write(buffer,0,len);
			 }
			 message = "下载成功";
		} catch (Exception e) {
			e.printStackTrace();
			message = "下载异常";
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
		
		req.setAttribute("result", message);
		System.out.println(message);
	}
	
	/**
	 * 递归寻找文件
	 * 
	 * @param rootDir
	 * @return
	 */
	private String findFileDigui(File rootDir, String filename){
		File[] fileList = rootDir.listFiles();
		for (File f : fileList){
			if (f.isFile()){
				if (f.getName().equals(filename)){
					return f.getAbsolutePath();
				}
			}else{
				findFileDigui(f, filename);
			}
		}
		
		return null;
	}
	

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	{
		System.out.println("DownloadFileServlet doPost called");
	}
	
}
