package com.pjb.springbootjwt.zhddkk.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;


/**
 * 聊天记录表
 */
 @TableName("ws_chatlog")
public class WsChatlogDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    //
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "time",name = "")
    private String time;
    //
    @ApiModelProperty(value = "user",name = "")
    private String user;
    //
    @ApiModelProperty(value = "toUser",name = "")
    private String toUser;
    //
    @ApiModelProperty(value = "msg",name = "")
    private String msg;
    //
    @ApiModelProperty(value = "remark",name = "")
    private String remark;


    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }


    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }


    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getToUser() {
        return toUser;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }


    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

}
