package com.pjb.springbootjwt.zhddkk.domain;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 定时任务表.
 */
 @TableName("ws_scheduled_cron")
 @Data
public class WsScheduledCronDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    //主键id
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "cronId", name = "主键id")
    private Integer cronId;
    //定时任务完整类名
    @ApiModelProperty(value = "cronKey", name = "定时任务完整类名")
    private String cronKey;
    //cron表达式
    @ApiModelProperty(value = "cronExpression", name = "cron表达式")
    private String cronExpression;
    //任务描述
    @ApiModelProperty(value = "taskExplain", name = "任务描述")
    private String taskExplain;
    //状态,1:正常;2:停用
    @ApiModelProperty(value = "status", name = "状态,1:正常;2:停用")
    private Integer status;
}
