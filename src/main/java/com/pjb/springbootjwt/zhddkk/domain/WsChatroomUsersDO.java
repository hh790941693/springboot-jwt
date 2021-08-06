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
 * 聊天室人员信息.
 * 作者:admin
 * 邮箱:anonymous@anonymous.com
 * 创建时间:2021-07-27 09:42:47
 */
@TableName("ws_chatroom_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsChatroomUsersDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    //
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id" , name = "")
    private Long id;
    //房间号
    @ApiModelProperty(value = "roomId" , name = "房间号")
    private String roomId;
    //用户id
    @ApiModelProperty(value = "userId" , name = "用户id")
    private Long userId;
    //用户名
    @ApiModelProperty(value = "userName" , name = "用户名")
    private String userName;

    //是否是群主  0:不是 1:是
    private Integer isOwner;
    //是否是管理员 0:不是 1:是
    @ApiModelProperty(value = "isManager" , name = "是否是管理员 0:不是 1:是")
    private Integer isManager;
    // 禁言状态 0:未禁言 1:已禁言
    private Integer banStatus;
    // 黑名单状态 0:不是 1:是
    private Integer blackStatus;
    //创建时间
    @ApiModelProperty(value = "createTime" , name = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    //更新时间
    @ApiModelProperty(value = "updateTime" , name = "更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    // 状态 0:离线 1:在线
    private Integer status;
}
