package com.pjb.springbootjwt.shop.dto;

import com.pjb.springbootjwt.shop.domain.SpGoodsDO;
import lombok.Data;

/**
 * 商品详情
 */
@Data
public class GoodsDetailDTO extends SpGoodsDO {

    // 店铺名称
    private String merchantName;
}
