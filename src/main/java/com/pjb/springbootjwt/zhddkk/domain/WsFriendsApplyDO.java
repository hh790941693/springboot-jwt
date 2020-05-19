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
 * 好友申请表
 */
 @TableName("ws_friends_apply")
public class WsFriendsApplyDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    //主键
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id",name = "主键")
    private Integer id;
    //申请人id
    @ApiModelProperty(value = "fromId",name = "申请人id")
    private Integer fromId;
    //申请人姓名
    @ApiModelProperty(value = "fromName",name = "申请人姓名")
    private String fromName;
    //好友id
    @ApiModelProperty(value = "toId",name = "好友id")
    private Integer toId;
    //好友姓名
    @ApiModelProperty(value = "toName",name = "好友姓名")
    private String toName;
    //申请状态 1:申请中 2:被拒绝 3:申请成功
    @ApiModelProperty(value = "processStatus",name = "申请状态 1:申请中 2:被拒绝 3:申请成功")
    private Integer processStatus;
    //创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "createTime",name = "创建时间")
    private Date createTime;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public Integer getFromId() {
        return fromId;
    }


    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromName() {
        return fromName;
    }


    public void setToId(Integer toId) {
        this.toId = toId;
    }

    public Integer getToId() {
        return toId;
    }


    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToName() {
        return toName;
    }


    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }

    public Integer getProcessStatus() {
        return processStatus;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

}
