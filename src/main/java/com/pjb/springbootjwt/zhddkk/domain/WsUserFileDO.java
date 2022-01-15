package com.pjb.springbootjwt.zhddkk.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户文件表.
 * 作者:admin
 * 邮箱:anonymous@anonymous.com
 * 创建时间:2022-01-14 10:02:48
 */
@TableName("ws_user_file")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsUserFileDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    //主键
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id" , name = "主键")
    private Long id;
    //用户id
    @ApiModelProperty(value = "userId" , name = "用户id")
    private Long userId;
    //用户名
    @ApiModelProperty(value = "userName" , name = "用户名")
    private String userName;
    //文件id
    @ApiModelProperty(value = "fileId" , name = "文件id")
    private Long fileId;
    //
    @ApiModelProperty(value = "fileName" , name = "")
    private String fileName;
    //
    @ApiModelProperty(value = "fileUrl" , name = "")
    private String fileUrl;
    // 文件分类
    private String category;
    //创建时间
    @ApiModelProperty(value = "createTime" , name = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    //更新时间
    @ApiModelProperty(value = "updateTime" , name = "更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    //状态
    @ApiModelProperty(value = "status" , name = "状态")
    private Integer status;
    // url的连接性 0:连接失败 1:正常
    @TableField(exist = false)
    private Integer accessStatus;
}
