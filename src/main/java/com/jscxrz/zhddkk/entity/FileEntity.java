package com.jscxrz.zhddkk.entity;

public class FileEntity {
	private String fileName;
	
	private String fileAbsName;
	
	private String author;
	
	private String modifyTime;
	
	private long fileSize; 
	
	private String trackLength;
	
	private String remark;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileAbsName() {
		return fileAbsName;
	}

	public void setFileAbsName(String fileAbsName) {
		this.fileAbsName = fileAbsName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
}
