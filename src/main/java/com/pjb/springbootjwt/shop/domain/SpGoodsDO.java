package com.pjb.springbootjwt.shop.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 商品表.
 */
@TableName("sp_goods")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpGoodsDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    //主键id
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id" , name = "主键id")
    private Long id;

    //商品id
    @ApiModelProperty(value = "goodsId" , name = "商品id")
    private String goodsId;

    //商品名称
    @ApiModelProperty(value = "name" , name = "商品名称")
    private String name;

    //商品简介
    @ApiModelProperty(value = "brief" , name = "商品简介")
    private String brief;

    //商品生产地
    @ApiModelProperty(value = "place" , name = "商品生产地")
    private String place;

    //商品类型id
    @ApiModelProperty(value = "goodsTypeId" , name = "商品类型id")
    private String goodsTypeId;

    //归属店铺id
    @ApiModelProperty(value = "merchantId" , name = "归属店铺id")
    private String merchantId;

    //库存数
    @ApiModelProperty(value = "stockNum" , name = "库存数")
    private Integer stockNum;

    //原价
    @ApiModelProperty(value = "originalPrice" , name = "原价")
    private BigDecimal originalPrice;

    //售价
    @ApiModelProperty(value = "salePrice" , name = "售价")
    private BigDecimal salePrice;

    //商品单位
    @ApiModelProperty(value = "unitName" , name = "商品单位")
    private String unitName;

    //商品封面图片
    private String backImage;

    //商品图片1
    private String image1;
    //商品图片2
    private String image2;
    //商品图片3
    private String image3;
    //商品图片4
    private String image4;

    //状态 0:未上架 1:已上架
    @ApiModelProperty(value = "status" , name = "状态 0:未上架 1:已上架")
    private Integer status;

    //创建时间
    @ApiModelProperty(value = "createTime" , name = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    //更新时间
    @ApiModelProperty(value = "updateTime" , name = "更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    // ------------非表字段---------------------------
    // 商品类型列表
    @TableField(exist = false)
    private List<SpGoodsTypeDO> spGoodsTypeList = new ArrayList<>();
}
