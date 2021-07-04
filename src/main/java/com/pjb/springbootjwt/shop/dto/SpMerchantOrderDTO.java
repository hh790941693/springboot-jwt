package com.pjb.springbootjwt.shop.dto;

import com.pjb.springbootjwt.shop.domain.SpOrderDO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SpMerchantOrderDTO {

    // 次订单
    private SpOrderDO subOrder;

    // 次订单商品列表
    private List<SpOrderDetailDTO> subOrderList = new ArrayList<>();
}
