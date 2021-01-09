package com.pjb.springbootjwt.zhddkk.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 用户与角色关系表
 */
@TableName("sys_user_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserRoleDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    //
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id", name = "")
    private Integer id;
    //用户id
    @ApiModelProperty(value = "userId", name = "用户id")
    private Integer userId;
    //用户名称
    @ApiModelProperty(value = "userName", name = "用户名称")
    private String userName;
    //角色id
    @ApiModelProperty(value = "roleId", name = "角色id")
    private Integer roleId;
    //角色名称
    @ApiModelProperty(value = "roleName", name = "角色名称")
    private String roleName;
    //创建时间
    @ApiModelProperty(value = "createTime", name = "创建时间")
    private Date createTime;
    //更新时间
    @ApiModelProperty(value = "updateTime", name = "更新时间")
    private Date updateTime;
}
