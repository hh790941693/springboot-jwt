package com.pjb.springbootjwt.zhddkk.bean;

public class WangyiNewsBean {
    private String newsType;
    private String docdId;
    private String url;

    public WangyiNewsBean(String newsType, String docdId, String url){
        this.newsType = newsType;
        this.docdId = docdId;
        this.url = url;
    }

    public String getNewsType() {
        return newsType;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
    }

    public String getDocdId() {
        return docdId;
    }

    public void setDocdId(String docdId) {
        this.docdId = docdId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
