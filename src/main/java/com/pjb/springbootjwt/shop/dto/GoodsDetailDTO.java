package com.pjb.springbootjwt.shop.dto;

import com.pjb.springbootjwt.shop.domain.SpGoodsDO;
import lombok.Data;

/**
 * 商品详情DTO.
 */
@Data
public class GoodsDetailDTO extends SpGoodsDO {

    // 店铺名称
    private String merchantName;

    // 商品收藏状态
    private int goodsFavoriteStatus;

    // 店铺收藏状态
    private int merchantFavoriteStatus;

    // 价格排序(默认true:升序)
    private boolean priceSort = true;

    // 销量排序(默认false:降序)
    private boolean saleNumberSort = false;
}
