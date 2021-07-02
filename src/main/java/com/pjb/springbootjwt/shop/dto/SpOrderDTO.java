package com.pjb.springbootjwt.shop.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import com.pjb.springbootjwt.shop.domain.SpOrderDO;

/**
 * 订单详情
 */
@Data
public class SpOrderDTO {

    // 主订单
    private SpOrderDO mainOrder;

    // 次订单列表
    private List<SpOrderDetailDTO> subOrderList = new ArrayList<>();
}
