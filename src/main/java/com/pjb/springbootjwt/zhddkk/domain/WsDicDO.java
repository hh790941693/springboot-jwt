package com.pjb.springbootjwt.zhddkk.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;


/**
 * 字典表
 */
 @TableName("ws_dic")
public class WsDicDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    //
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "type",name = "")
    private String type;
    //
    @ApiModelProperty(value = "key",name = "")
    private String key;
    //
    @ApiModelProperty(value = "value",name = "")
    private String value;
    //
    @ApiModelProperty(value = "sort",name = "")
    private Integer sort;
    //
    @ApiModelProperty(value = "remark",name = "")
    private String remark;


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }


    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }


    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getSort() {
        return sort;
    }


    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

}
