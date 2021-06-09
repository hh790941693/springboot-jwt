package com.pjb.springbootjwt.shop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品详情
 */
public class GoodsDetailDTO {
    //商品id
    private String goodsId;

    //商品名称
    private String name;

    //商品简介
    private String brief;

    //商品生产地
    private String place;

    //商品类型id
    private String goodsTypeId;

    //归属店铺id
    private String merchantId;

    //库存数
    private Integer stockNum;

    //原价
    private BigDecimal originalPrice;

    //售价
    private BigDecimal salePrice;

    //商品单位
    private String unitName;

    //商品图片1
    private String image1;
    //商品图片2
    private String image2;
    //商品图片3
    private String image3;
    //商品图片4
    private String image4;

    //状态 0:未上架 1:已上架
    private Integer status;

    //创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    //更新时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
}
