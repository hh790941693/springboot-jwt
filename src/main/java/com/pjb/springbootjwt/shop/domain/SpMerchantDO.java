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
 * 商家店铺表.
 */
@TableName("sp_merchant")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpMerchantDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    //主键id
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id" , name = "主键id")
    private Long id;

    //商家id
    @ApiModelProperty(value = "userId" , name = "商家id")
    private Long userId;

    //店铺id
    @ApiModelProperty(value = "merchantId" , name = "店铺id")
    private String merchantId;

    //店铺名称
    @ApiModelProperty(value = "name" , name = "店铺名称")
    private String name;

    //店铺地址
    @ApiModelProperty(value = "address" , name = "店铺地址")
    private String address;

    //店铺图片
    @ApiModelProperty(value = "image" , name = "店铺图片")
    private String image;

    //店铺描述
    @ApiModelProperty(value = "desc" , name = "店铺描述")
    private String desc;

    //联系电话
    @ApiModelProperty(value = "contact" , name = "联系电话")
    private String contact;

    //店铺状态 0:已关闭 1:营业中 2:暂停营业 3:已打烊
    @ApiModelProperty(value = "status" , name = "店铺状态 0:已关闭 1:营业中 2:暂停营业 3:已打烊")
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
