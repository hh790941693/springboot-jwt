package com.pjb.springbootjwt.zhddkk.bean;

public class FileUploadResultBean {
	private String filename;
	private boolean uploadFlag;
	private String url;

	public FileUploadResultBean(){

	}

	public FileUploadResultBean(String filename, boolean uploadFlag, String url){
		this.filename = filename;
		this.uploadFlag = uploadFlag;
		this.url = url;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public boolean isUploadFlag() {
		return uploadFlag;
	}

	public void setUploadFlag(boolean uploadFlag) {
		this.uploadFlag = uploadFlag;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "FileUploadResultBean{" +
				"filename='" + filename + '\'' +
				", uploadFlag=" + uploadFlag +
				", url='" + url + '\'' +
				'}';
	}
}
