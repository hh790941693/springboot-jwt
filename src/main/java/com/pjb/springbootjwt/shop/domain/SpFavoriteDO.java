package com.pjb.springbootjwt.shop.domain;

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

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 收藏表，包括店铺、商品等。.
 */
@TableName("sp_favorite")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpFavoriteDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    //主键
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id" , name = "主键")
    private Long id;

    //用户id
    @ApiModelProperty(value = "userId" , name = "用户id")
    private Long userId;

    //收藏物id
    @ApiModelProperty(value = "subjectId" , name = "收藏物id")
    private String subjectId;

    //收藏物类型 1:商品 2:店铺
    @ApiModelProperty(value = "subjectType" , name = "收藏物类型 1:商品 2:店铺")
    private Integer subjectType;

    //状态 1:收藏 2:取消收藏
    @ApiModelProperty(value = "status" , name = "状态 1:收藏 2:取消收藏")
    private Integer status;

    //创建时间
    @ApiModelProperty(value = "createTime" , name = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    //更新时间
    @ApiModelProperty(value = "updateTime" , name = "更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

}
