package com.pjb.springbootjwt.zhddkk.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;


/**
 * 
 */
 @TableName("ws_circle_comment")
public class WsCircleCommentDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    //主键
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id",name = "主键")
    private Integer id;
    //朋友圈消息id
    @ApiModelProperty(value = "circleId",name = "朋友圈消息id")
    private Integer circleId;
    //评论人id
    @ApiModelProperty(value = "userId",name = "评论人id")
    private Integer userId;
    //评论人姓名
    @ApiModelProperty(value = "userName",name = "评论人姓名")
    private String userName;
    //评论内容
    @ApiModelProperty(value = "comment",name = "评论内容")
    private String comment;
    //评论时间
    @ApiModelProperty(value = "createTime",name = "评论时间")
    private Date createTime;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    public void setCircleId(Integer circleId) {
        this.circleId = circleId;
    }

    public Integer getCircleId() {
        return circleId;
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


    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

}
