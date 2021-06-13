package com.pjb.springbootjwt.shop.dto;

import com.pjb.springbootjwt.shop.domain.SpGoodsDO;
import lombok.Data;

/**
 * 购物车.
 */
@Data
public class SpShoppingCartDTO extends SpGoodsDO {

    private long shoppingCartPkId;

    // 商品数量
    private int goodsCount;
}
