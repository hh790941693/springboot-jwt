package com.pjb.springbootjwt.ump.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@TableName("t_role")
@Data
public class RoleDO {
    private String id;
    private String rolename;
    private Date createTime;

    @TableField(exist = false)
    private Set<PermissionDO> permissionList;
}
