package com.pjb.springbootjwt.ump.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author jinbin
 * @date 2018-07-08 20:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class UserDO implements Serializable {
    String id;
    String username;
    String password;
    Date createTime;
    Date updateTime;

    @TableField(exist = false)
    private Set<RoleDO> roleList;
}
