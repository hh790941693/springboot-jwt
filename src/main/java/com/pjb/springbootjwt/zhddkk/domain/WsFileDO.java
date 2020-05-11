package com.pjb.springbootjwt.zhddkk.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;


/**
 * 文件表
 */
 @TableName("ws_file")
public class WsFileDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    //
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id",name = "")
    private Integer id;
    //用户名
    @ApiModelProperty(value = "user",name = "用户名")
    private String user;
    //文件名
    @ApiModelProperty(value = "filename",name = "文件名")
    private String filename;
    //存储磁盘目录
    @ApiModelProperty(value = "diskPath",name = "存储磁盘目录")
    private String diskPath;
    //url
    @ApiModelProperty(value = "url",name = "url")
    private String url;
    //文件大小
    @ApiModelProperty(value = "fileSize",name = "文件大小")
    private Integer fileSize;
    //未知
    @ApiModelProperty(value = "author",name = "未知")
    private String author;
    //文件时长
    @ApiModelProperty(value = "trackLength",name = "文件时长")
    private String trackLength;
    //
    @ApiModelProperty(value = "createTime",name = "")
    private Date createTime;
    //
    @ApiModelProperty(value = "updateTime",name = "")
    private Date updateTime;
    //0:无效 1:有效
    @ApiModelProperty(value = "status",name = "0:无效 1:有效")
    private Integer status;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }


    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }


    public void setDiskPath(String diskPath) {
        this.diskPath = diskPath;
    }

    public String getDiskPath() {
        return diskPath;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }


    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getFileSize() {
        return fileSize;
    }


    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }


    public void setTrackLength(String trackLength) {
        this.trackLength = trackLength;
    }

    public String getTrackLength() {
        return trackLength;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }


    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }


    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

}
