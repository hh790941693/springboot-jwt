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
 * 商品分类表.
 */
@TableName("sp_goods_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpGoodsTypeDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    //主键id
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id" , name = "主键id")
    private Long id;

    //商品分类id
    @ApiModelProperty(value = "typeId" , name = "商品分类id")
    private String typeId;

    //分类名称
    @ApiModelProperty(value = "name" , name = "分类名称")
    private String name;

    //分类图片
    @ApiModelProperty(value = "image" , name = "分类图片")
    private String image;

    //分类描述
    @ApiModelProperty(value = "desc" , name = "分类描述")
    private String desc;

    //状态 0:禁用 1:启用
    @ApiModelProperty(value = "status" , name = "状态 0:禁用 1:启用")
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
