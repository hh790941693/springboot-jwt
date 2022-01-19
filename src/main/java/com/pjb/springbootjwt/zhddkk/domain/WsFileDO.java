package com.pjb.springbootjwt.zhddkk.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;


/**
 * 文件表
 */
@TableName("ws_file")
@Data
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
    //文件类型
    @ApiModelProperty(value = "folder",name = "文件类型")
    private String folder;
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
    private Long fileSize;
    //未知
    @ApiModelProperty(value = "author",name = "未知")
    private String author;
    //文件时长
    @ApiModelProperty(value = "trackLength",name = "文件时长")
    private String trackLength;
    //创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "createTime",name = "")
    private Date createTime;
    //更新时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "updateTime",name = "")
    private Date updateTime;
    //0:无效 1:有效
    @ApiModelProperty(value = "status",name = "0:无效 1:有效")
    private Integer status;
    @ApiModelProperty(value = "accessStatus",name = "访问性 0:不可访问 1:可访问")
    private Integer accessStatus;

    // md5值
    private String md5;
}
