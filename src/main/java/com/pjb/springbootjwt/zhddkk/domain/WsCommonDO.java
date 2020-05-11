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
 @TableName("ws_common")
public class WsCommonDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    //
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id",name = "")
    private Integer id;
    //
    @ApiModelProperty(value = "type",name = "")
    private String type;
    //
    @ApiModelProperty(value = "name",name = "")
    private String name;
    //
    @ApiModelProperty(value = "orderby",name = "")
    private Integer orderby;
    //
    @ApiModelProperty(value = "remark",name = "")
    private String remark;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void setOrderby(Integer orderby) {
        this.orderby = orderby;
    }

    public Integer getOrderby() {
        return orderby;
    }


    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

}
