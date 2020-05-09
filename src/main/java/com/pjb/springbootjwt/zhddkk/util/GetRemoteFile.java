package com.pjb.springbootjwt.zhddkk.util;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetRemoteFile{
	
	private static final String BASE_URL = "http://192.168.230.22:9998/";
	
	private static final String LOCAL_PATH = "d:\\temp\\";
	
	private boolean FileExist(String pathAndFile){//确定文件是否已经下载，但没有下载完成
	  File file = new File(pathAndFile);
	  if (file.exists())
	    return true;
	  else
	    return false;
	}
	private long FileSize(String pathAndFile){//确定已经下载了的文件大小
	  File file = new File(pathAndFile);
	  return file.length();
	}
	
	private void FileRename(String fName,String nName){//将下载完全的文件更名，去掉.tp名
	  File file = new File(fName);
	  file.renameTo(new File(nName));
	  file.delete();
	}
	
	private void downloadFile(String localFile) {
		  URL url = null;
		  HttpURLConnection urlc = null;
		  DataOutputStream dos = null;
		  BufferedInputStream bis = null;
		  FileOutputStream fos = null;
		  InputStream in = null;
		  String localFile_bak = localFile + ".tp";//未下载完文件加.bak扩展名，以便于区别
		  GetRemoteFile gco = new GetRemoteFile();
		  long fileSize = 0;
		  long start = System.currentTimeMillis();
		  int len = 0;
		  byte[] bt = new byte[1024];
		  RandomAccessFile raFile=null;
		  long totalSize=0;//要下载的文件总大小
		  try{
		    url = new URL(BASE_URL+localFile);      
		    urlc = (HttpURLConnection) url.openConnection();
		    in = urlc.getInputStream();
	        //String md5Hex = DigestUtils.md5Hex(in);
	        //System.out.println(md5Hex);
		    totalSize=Long.parseLong(urlc.getHeaderField("Content-Length"));

		    //urlc.disconnect();//先断开，下面再连接，否则下面会报已经连接的错误
		    urlc = (HttpURLConnection) url.openConnection();
		    //确定文件是否存在
		    if (gco.FileExist(LOCAL_PATH+localFile_bak)){//采用断点续传，这里的依据是看下载文件是否在本地有.tp有扩展名同名文件
		      System.out.println(localFile+"文件续传中...");
		      fileSize = gco.FileSize(LOCAL_PATH+localFile_bak); //取得文件在小，以便确定随机写入的位置
		      //System.out.println("fileSize:"+fileSize);
		      //设置User-Agent
		      //urlc.setRequestProperty("User-Agent","NetFox");
		      //设置断点续传的开始位置
		      urlc.setRequestProperty("RANGE", "bytes="+fileSize+"-");

		      //设置接受信息
		      urlc.setRequestProperty("Accept","image/gif,image/x-xbitmap,application/msword,*/*");        
		      raFile=new RandomAccessFile(LOCAL_PATH+localFile_bak,"rw");//随机方位读取
		      raFile.seek(fileSize);//定位指针到fileSize位置
		      bis = new BufferedInputStream(in);
		      int retryRead = 0;
		      while ((len = bis.read(bt)) > 0){//循环获取文件
		        raFile.write(bt, 0, len);
		        retryRead += len;
		      }
		      if (retryRead == totalSize) {
			      //重命名
			      gco.FileRename(LOCAL_PATH+localFile_bak, LOCAL_PATH+localFile);
			      System.out.println(localFile+"文件续传接收完毕！");
		      }
		    }
		    else{//采用原始下载
		      fos = new FileOutputStream(LOCAL_PATH+localFile_bak); //没有下载完毕就将文件的扩展名命名.bak
		      dos = new DataOutputStream(fos);
		      bis = new BufferedInputStream(in);
		      long totalRead = 0;
		      int i=0;
		      while ((len = bis.read(bt)) > 0){
		        dos.write(bt, 0, len);
		        totalRead += len;
		        i++;
		        long end = System.currentTimeMillis();
		        if (i%300 == 0 && totalSize != 0) {
		        	System.out.println("正在下载"+localFile+" 已下载:"+totalRead*100/totalSize+"% 用时:"+(end-start)/1000+"s");
		        }
		        if (totalRead == totalSize) {
		        	System.out.println("正在下载"+localFile+" 已下载:"+totalRead*100/totalSize+"% 用时:"+(end-start)/1000+"s");
		        }
		      }        
		    }
		    if(in!=null) {in.close();}
		    if(bis!=null) {bis.close();}
		    if(dos!=null) {dos.close();}
		    if(fos!=null) {fos.close();}
		    if(raFile!=null) {raFile.close();}
		    if (urlc!=null){urlc.disconnect();}
		    if(gco.FileSize(LOCAL_PATH+localFile_bak) == totalSize){
		      //下载完毕后，将文件重命名
		      gco.FileRename(LOCAL_PATH+localFile_bak,LOCAL_PATH+localFile);
		    }
		  }
		  catch (Exception e){
		    try{
		    	if(in!=null) {in.close();}
			    if(bis!=null) {bis.close();}
			    if(dos!=null) {dos.close();}
			    if(fos!=null) {fos.close();}
			    if(raFile!=null) {raFile.close();}
			    if (urlc!=null){urlc.disconnect();}
		    }
		    catch (IOException f){
		      f.printStackTrace();
		    }
		    e.printStackTrace();
		  }
	}
	
	public static void main(String[] args){
	  GetRemoteFile gco = new GetRemoteFile();
	  List<String> fileList = new ArrayList<String>();
	  fileList.add("apache-maven-3.6.0-bin.tar.gz");
	  fileList.add("apache-tomcat-8.5.37.tar.gz");
	  fileList.add("emby-server-deb_3.5.3.0_armhf.deb");
	  fileList.add("zabbix-release_4.0-2+stretch_all.deb");
	  fileList.add("index.html");
	  fileList.add("car.jpg");
	  
	  for (String f : fileList) {
		  gco.downloadFile(f);
	  }
	}
}
