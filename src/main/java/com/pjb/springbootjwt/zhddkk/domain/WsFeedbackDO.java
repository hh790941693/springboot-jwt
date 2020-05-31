package com.pjb.springbootjwt.zhddkk.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;


/**
 * 问题反馈
 */
 @TableName("ws_feedback")
public class WsFeedbackDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    //
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id",name = "")
    private Integer id;
    //用户id
    @ApiModelProperty(value = "userId",name = "用户id")
    private Integer userId;
    //用户名称
    @ApiModelProperty(value = "userName",name = "用户名称")
    private String userName;
    //反馈类型 1:建议 2:问题
    @ApiModelProperty(value = "type",name = "反馈类型 1:建议 2:问题")
    private String type;
    //标题
    @ApiModelProperty(value = "title",name = "标题")
    private String title;
    //问题描述
    @ApiModelProperty(value = "content",name = "问题描述")
    private String content;
    //图片url
    @ApiModelProperty(value = "picUrl",name = "图片url")
    private String picUrl;
    //答复内容
    @ApiModelProperty(value = "replyContent",name = "答复内容")
    private String replyContent;
    //答复时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "replyTime",name = "答复时间")
    private Date replyTime;
    //创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "createTime",name = "创建时间")
    private Date createTime;
    //更新时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "updateTime",name = "更新时间")
    private Date updateTime;
    //状态 0:已删除 1:待答复 2:已答复
    @ApiModelProperty(value = "status",name = "状态 0:已删除 1:待答复 2:已答复")
    private Integer status;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }


    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }


    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getReplyContent() {
        return replyContent;
    }


    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public Date getReplyTime() {
        return replyTime;
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
