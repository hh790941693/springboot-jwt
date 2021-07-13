package com.pjb.springbootjwt.zhddkk.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 * </pre>
 * <small> 2018年3月23日 | Aron</small>
 */
@TableName("sys_task")
@Data
public class SysTaskDO implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    // 任务名
    private String jobName;
    // 任务分组
    private String jobGroup;
    // 任务执行时调用哪个类的方法 包名+类名
    private String beanClass;
    // cron表达式
    private String cronExpression;
    // 任务调用的方法名
    private String methodName;
    // 方法参数(json串) 比如{"name":"xxx","age":22}
    private String parameters;
    // 任务状态 0:停止 1:运行中
    private String jobStatus;
    // 任务描述
    private String description;
    // 任务是否有状态
    private String isConcurrent;
    // 创建时间
    private Date createDate;
    // 更新时间
    private Date updateDate;
}
