package com.pjb.springbootjwt.zhddkk.entity;

import lombok.Data;

@Data
public class FileEntity {
    private String fileName;

    private String fileAbsName;

    private String author;

    private String modifyTime;

    private long fileSize;

    private String trackLength;

    private String remark;
}
