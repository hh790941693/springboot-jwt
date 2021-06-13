package com.pjb.springbootjwt.shop.service;

import com.pjb.springbootjwt.shop.domain.SpFavoriteDO;
import com.pjb.springbootjwt.shop.domain.SpGoodsDO;
import com.pjb.springbootjwt.shop.domain.SpMerchantDO;
import com.pjb.springbootjwt.zhddkk.base.CoreService;

import java.util.List;

/**
 * 收藏表，包括店铺、商品等。.
 */
public interface SpFavoriteService extends CoreService<SpFavoriteDO> {

    List<SpGoodsDO> queryFavoriteGoodsList(String loginUserId);

    List<SpMerchantDO> queryFavoriteMerchantList(String loginUserId);
}
