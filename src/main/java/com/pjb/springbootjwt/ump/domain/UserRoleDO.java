package com.pjb.springbootjwt.ump.domain;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

@TableName("t_user_role")
@Data
public class UserRoleDO {
    private String id;
    private String userId;
    private String username;
    private String roleId;
    private String roleName;
    private Date createTime;
}
