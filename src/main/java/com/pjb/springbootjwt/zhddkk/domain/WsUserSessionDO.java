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
 * 用户会话表.
 * 作者:admin
 * 邮箱:547495788@qq.com
 * 创建时间:2021-08-07 17:38:43
 */
@TableName("ws_user_session")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsUserSessionDO implements Serializable {

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
    //SESSIONID
    @ApiModelProperty(value = "sessionId" , name = "SESSIONID")
    private String sessionId;
    //创建时间
    @ApiModelProperty(value = "createTime" , name = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    //更新时间
    @ApiModelProperty(value = "updateTime" , name = "更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
}
