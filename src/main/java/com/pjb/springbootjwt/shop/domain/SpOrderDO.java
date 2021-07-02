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
 * 订单表.
 */
@TableName("sp_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpOrderDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    //主键id
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id" , name = "主键id")
    private Long id;

    //订单号
    @ApiModelProperty(value = "orderNo" , name = "订单号")
    private String orderNo;

    //父订单号
    @ApiModelProperty(value = "parentOrderNo" , name = "父订单号")
    private String parentOrderNo;

    //店铺id
    private String merchantId;

    //商品id
    private String goodsId;

    //总价
    @ApiModelProperty(value = "totalPrice" , name = "总价")
    private BigDecimal totalPrice;

    //支付价格
    @ApiModelProperty(value = "payPrice" , name = "支付价格")
    private BigDecimal payPrice;

    //下单用户id
    @ApiModelProperty(value = "orderUserId" , name = "下单用户id")
    private Long orderUserId;

    //支付用户id
    @ApiModelProperty(value = "payUserId" , name = "支付用户id")
    private Long payUserId;

    // 支付状态 1：待支付 2:已支付
    private Integer payStatus;

    // 物流状态 3:待发货 4:已发货
    private Integer logisticsStatus;

    // 取消状态 5:未取消 6:已取消
    private Integer cancelStatus;

    // 支付方式 1:微信 2:支付宝 3:银行卡 4:现金 9:其他
    private Integer payWay;

    //状态 1：待支付 2:已支付 3:待发货 4:已发货 6:已取消 9:已确认收货
    @ApiModelProperty(value = "status" , name = "状态 1：待支付 2:已支付 3:待发货 4:已发货 6:已取消 9:已确认收货")
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
