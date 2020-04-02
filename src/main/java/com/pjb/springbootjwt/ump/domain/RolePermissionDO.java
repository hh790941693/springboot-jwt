package com.pjb.springbootjwt.ump.domain;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@TableName("t_role_permission")
@Data
public class RolePermissionDO {
    private String id;
    private String roleId;
    private String roleName;
    private String permissionId;
    private String permissionName;
}
