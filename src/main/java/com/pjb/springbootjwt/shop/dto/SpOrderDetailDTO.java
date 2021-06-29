package com.pjb.springbootjwt.shop.dto;

import com.pjb.springbootjwt.shop.domain.SpGoodsDO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SpOrderDetailDTO extends SpGoodsDO {
    // 订单号
    private String orderNo;

    // 原价
    private BigDecimal goodsOriginalPrice;

    // 售价
    private BigDecimal goodsSalePrice;

    // 商品数量
    private int goodsCount;

    // 商家名称
    private String merchantName;
}
