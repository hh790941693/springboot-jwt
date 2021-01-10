package com.pjb.springbootjwt.zhddkk.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
 * 菜单表
 */
@TableName("sys_menu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysMenuDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    //菜单id
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id", name = "菜单id")
    private Integer id;
    //菜单名称
    @ApiModelProperty(value = "name", name = "菜单名称")
    private String name;
    //父菜单id
    @ApiModelProperty(value = "parentId", name = "父菜单id")
    private Integer parentId;
    //url
    @ApiModelProperty(value = "url", name = "url")
    private String url;
    //图标名称
    @ApiModelProperty(value = "icon", name = "图标名称")
    private String icon;
    //扩展字段1
    @ApiModelProperty(value = "extColumn1", name = "扩展字段1")
    private String extColumn1;
    //扩展字段2
    @ApiModelProperty(value = "extColumn2", name = "扩展字段2")
    private String extColumn2;
    //扩展字段3
    @ApiModelProperty(value = "extColumn3", name = "扩展字段3")
    private String extColumn3;
    //创建时间
    @ApiModelProperty(value = "createTime", name = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    //更新时间
    @ApiModelProperty(value = "updateTime", name = "更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    // 子菜单列表
    @TableField(exist = false)
    private List<SysMenuDO> childrenList = new ArrayList<SysMenuDO>();
}
