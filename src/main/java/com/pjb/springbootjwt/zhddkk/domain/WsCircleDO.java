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
 @TableName("ws_circle")
public class WsCircleDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    //主键
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id",name = "主键")
    private Integer id;
    //用户名
    @ApiModelProperty(value = "userName",name = "用户名")
    private String userName;
    //用户id
    @ApiModelProperty(value = "userId",name = "用户id")
    private Integer userId;
    //内容
    @ApiModelProperty(value = "content",name = "内容")
    private String content;
    //点赞数
    @ApiModelProperty(value = "likeNum",name = "点赞数")
    private Integer likeNum;
    //图片1
    @ApiModelProperty(value = "pic1",name = "图片1")
    private String pic1;
    //图片2
    @ApiModelProperty(value = "pic2",name = "图片2")
    private String pic2;
    //图片3
    @ApiModelProperty(value = "pic3",name = "图片3")
    private String pic3;
    //图片4
    @ApiModelProperty(value = "pic4",name = "图片4")
    private String pic4;
    //图片5
    @ApiModelProperty(value = "pic5",name = "图片5")
    private String pic5;
    //图片6
    @ApiModelProperty(value = "pic6",name = "图片6")
    private String pic6;
    //发表时间
    @ApiModelProperty(value = "createTime",name = "发表时间")
    private Date createTime;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }


    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }


    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }


    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getLikeNum() {
        return likeNum;
    }


    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic1() {
        return pic1;
    }


    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getPic2() {
        return pic2;
    }


    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }

    public String getPic3() {
        return pic3;
    }


    public void setPic4(String pic4) {
        this.pic4 = pic4;
    }

    public String getPic4() {
        return pic4;
    }


    public void setPic5(String pic5) {
        this.pic5 = pic5;
    }

    public String getPic5() {
        return pic5;
    }


    public void setPic6(String pic6) {
        this.pic6 = pic6;
    }

    public String getPic6() {
        return pic6;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

}
