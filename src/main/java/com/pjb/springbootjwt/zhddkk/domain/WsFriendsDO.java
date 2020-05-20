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
 * 好友列表
 */
 @TableName("ws_friends")
public class WsFriendsDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    //主键
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id",name = "主键")
    private Integer id;
    //用户id
    @ApiModelProperty(value = "uid",name = "用户id")
    private Integer uid;
    //用户姓名
    @ApiModelProperty(value = "uname",name = "用户姓名")
    private String uname;
    //好友id
    @ApiModelProperty(value = "fid",name = "好友id")
    private Integer fid;
    //好友姓名
    @ApiModelProperty(value = "fname",name = "好友姓名")
    private String fname;
    //添加时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "createTime",name = "添加时间")
    private Date createTime;
    //备注
    @ApiModelProperty(value = "remark",name = "备注")
    private String remark;

    //在线状态
    @TableField(exist = false)
    private String state;

    //头像
    @TableField(exist = false)
    private String headImage;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getUid() {
        return uid;
    }


    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUname() {
        return uname;
    }


    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public Integer getFid() {
        return fid;
    }


    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFname() {
        return fname;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }


    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }
}
