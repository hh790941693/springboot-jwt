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

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 订单详情表.
 */
@TableName("sp_order_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpOrderDetailDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    //主键id
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id" , name = "主键id")
    private Long id;

    //订单号
    @ApiModelProperty(value = "orderNo" , name = "订单号")
    private String orderNo;

    //商品id
    @ApiModelProperty(value = "goodsId" , name = "商品id")
    private String goodsId;

    //商品数量
    @ApiModelProperty(value = "goodsCount" , name = "商品数量")
    private Integer goodsCount;

    //商品原价
    @ApiModelProperty(value = "goodsOriginalPrice" , name = "商品原价")
    private BigDecimal goodsOriginalPrice;

    //商品售价
    private BigDecimal goodsSalePrice;

    //商品所属的商家id
    @ApiModelProperty(value = "merchantId" , name = "商品所属的商家id")
    private String merchantId;

    //创建时间
    @ApiModelProperty(value = "createTime" , name = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    //更新时间
    @ApiModelProperty(value = "updateTime" , name = "更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

}
