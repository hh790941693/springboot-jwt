package com.pjb.springbootjwt.zhddkk.entity;

import lombok.Data;

@Data
public class PageQueryEntity {
    private int curPage;
    private int numPerPage;
    private Object params;
}
