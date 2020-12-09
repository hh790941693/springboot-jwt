package com.pjb.springbootjwt.zhddkk.entity;

import java.util.List;
import lombok.Data;

@Data
public class PageResponseEntity {
    private int totalPage;
    private int totalCount;
    private List<?> list;
    private Object parameter1 = "";
    private Object parameter2 = "";
    private Object parameter3 = "";
}
