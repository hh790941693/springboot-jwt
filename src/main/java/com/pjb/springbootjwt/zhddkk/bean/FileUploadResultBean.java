package com.pjb.springbootjwt.zhddkk.bean;

import lombok.Data;

@Data
public class FileUploadResultBean {
    private String filename;
    private boolean uploadFlag;
    private String url;

    public FileUploadResultBean(){

    }

    /**
     * 构造方法.
     * @param filename 文件名
     * @param uploadFlag 上传flag
     * @param url 文件url
     */
    public FileUploadResultBean(String filename, boolean uploadFlag, String url) {
        this.filename = filename;
        this.uploadFlag = uploadFlag;
        this.url = url;
    }
}
