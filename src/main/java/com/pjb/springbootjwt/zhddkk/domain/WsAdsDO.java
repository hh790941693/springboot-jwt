package com.pjb.springbootjwt.zhddkk.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * 广告表
 */
 @TableName("ws_ads")
 @Data
public class WsAdsDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    //主键
    @TableId(type = IdType.AUTO)

    private Integer id;

    @NotBlank(message="标题不能为空")
    private String title;

    //内容
    @NotBlank(message="内容不能为空")
    private String content;

    //背景图片
    @NotBlank(message="背景图片不能为空")
    private String backImg;

    //接收人列表
    private String receiveList;

    //创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
}
