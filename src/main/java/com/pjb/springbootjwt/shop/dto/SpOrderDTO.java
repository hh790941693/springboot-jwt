package com.pjb.springbootjwt.shop.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单详情
 */
@Data
public class SpOrderDTO {

    private String parentOrderNo;

    private BigDecimal totalOriginalPrice = new BigDecimal(0);

    private BigDecimal totalSalePrice = new BigDecimal(0);

    private List<SpOrderDetailDTO> goodsList = new ArrayList<>();
}
