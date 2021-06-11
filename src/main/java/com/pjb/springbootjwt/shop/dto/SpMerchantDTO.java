package com.pjb.springbootjwt.shop.dto;

import com.pjb.springbootjwt.shop.domain.SpMerchantDO;
import lombok.Data;

/**
 * 店铺详情DTO.
 */
@Data
public class SpMerchantDTO extends SpMerchantDO {

    // 店铺收藏状态 1:已收藏 2:未收藏
    private int merchantFavoriteStatus;
}
