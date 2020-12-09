package com.pjb.springbootjwt.zhddkk.bean;

import lombok.Data;

@Data
public class WangyiNewsBean {
    private String newsType;
    private String docId;
    private String url;

    /**
     * 构造方法.
     * @param newsType 新闻类型
     * @param docId 新闻id
     * @param url url
     */
    public WangyiNewsBean(String newsType, String docId, String url) {
        this.newsType = newsType;
        this.docId = docId;
        this.url = url;
    }
}
