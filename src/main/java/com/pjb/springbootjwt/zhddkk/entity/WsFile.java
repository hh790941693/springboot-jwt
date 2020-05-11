package com.pjb.springbootjwt.zhddkk.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import java.util.Date;

@TableName("ws_file")
public class WsFile {

	private int id;
	
	private String user;

	public WsFile() {}
	
	public WsFile(int id, String user) {
		super();
		this.id = id;
		this.user = user;
	}

	private String folder;

	private String filename;
	
	private String diskPath;
	
	private String url;
	
	private String author;
	
	private long fileSize; 
	
	private String trackLength;
	
	private Date createTime;
	
	private Date updateTime;
	
	private byte status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getTrackLength() {
		return trackLength;
	}

	public void setTrackLength(String trackLength) {
		this.trackLength = trackLength;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDiskPath() {
		return diskPath;
	}

	public void setDiskPath(String diskPath) {
		this.diskPath = diskPath;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "WsFile [user=" + user + ", filename=" + filename + ", diskPath=" + diskPath + ", url=" + url
				+ ", author=" + author + ", fileSize=" + fileSize + ", trackLength=" + trackLength + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", status=" + status + "]";
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}
}
