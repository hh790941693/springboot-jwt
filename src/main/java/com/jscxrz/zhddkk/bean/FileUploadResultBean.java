package com.jscxrz.zhddkk.bean;

public class FileUploadResultBean {
	private String filename;
	private String uploadResult;
	private boolean uploadFlag;  //true:success  //false:failed
	private String failedReason;
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getUploadResult() {
		return uploadResult;
	}
	public void setUploadResult(String uploadResult) {
		this.uploadResult = uploadResult;
	}
	public boolean isUploadFlag() {
		return uploadFlag;
	}
	public void setUploadFlag(boolean uploadFlag) {
		this.uploadFlag = uploadFlag;
	}
	public String getFailedReason() {
		return failedReason;
	}
	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}
	@Override
	public String toString() {
		return "FileUploadResultBean [filename=" + filename + ", uploadResult=" + uploadResult + ", uploadFlag="
				+ uploadFlag + ", failedReason=" + failedReason + "]";
	}
}
