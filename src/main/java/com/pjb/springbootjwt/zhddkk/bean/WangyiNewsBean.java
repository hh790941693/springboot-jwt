package com.pjb.springbootjwt.zhddkk.bean;

public class WangyiNewsBean {
    private String newsType;
    private String docId;
    private String url;

    public WangyiNewsBean(String newsType, String docId, String url){
        this.newsType = newsType;
        this.docId = docId;
        this.url = url;
    }

    public String getNewsType() {
        return newsType;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
