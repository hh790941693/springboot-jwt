package com.pjb.springbootjwt.zhddkk.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 角色表
 */
@TableName("sys_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    //角色id
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id", name = "角色id")
    private Integer id;
    //角色名
    @ApiModelProperty(value = "name", name = "角色名")
    private String name;
    //创建时间
    @ApiModelProperty(value = "createTime", name = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    //更新时间
    @ApiModelProperty(value = "updateTime", name = "更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    @TableField(exist = false)
    private String roleNameLike;
}
