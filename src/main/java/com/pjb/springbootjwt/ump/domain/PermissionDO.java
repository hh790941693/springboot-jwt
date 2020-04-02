package com.pjb.springbootjwt.ump.domain;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_permission")
public class PermissionDO {
    private String id;
    private String permissionName;
    private Date createTime;
}
