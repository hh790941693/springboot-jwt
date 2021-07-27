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
 * 聊天室房间表.
 * 作者:admin
 * 邮箱:anonymous@anonymous.com
 * 创建时间:2021-07-27 09:42:44
 */
@TableName("ws_chatroom")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsChatroomDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    //
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id" , name = "")
    private Long id;
    //房间id
    @ApiModelProperty(value = "roomId" , name = "房间id")
    private String roomId;
    //房间名称
    @ApiModelProperty(value = "name" , name = "房间名称")
    private String name;
    //房间密码
    @ApiModelProperty(value = "password" , name = "房间密码")
    private String password;
    //房间描述
    @ApiModelProperty(value = "desc" , name = "房间描述")
    private String desc;
    //创建者id
    @ApiModelProperty(value = "createUserId" , name = "创建者id")
    private Long createUserId;
    //创建时间
    @ApiModelProperty(value = "createTime" , name = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    //更新时间
    @ApiModelProperty(value = "updateTime" , name = "更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    //状态 0:删除 1:正常 2:封锁
    @ApiModelProperty(value = "status" , name = "状态 0:删除 1:正常 2:封锁")
    private Integer status;
}
