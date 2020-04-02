package com.pjb.springbootjwt.common.uploader.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "upload.config")
@Component
public class UploadConfig {

    //文件在服务器的保存目录 例如:/home
    private String storePath;
    //文件的访问路径 例如:http://127.0.0.1/files/aaa.txt
    private String viewUrl;

    public String getStorePath() {
        return storePath;
    }

    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }

    @Override
    public String toString() {
        return "UploadConfig{" +
                "storePath='" + storePath + '\'' +
                ", viewUrl='" + viewUrl + '\'' +
                '}';
    }
}

