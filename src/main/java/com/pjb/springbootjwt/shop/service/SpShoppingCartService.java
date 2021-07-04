package com.pjb.springbootjwt.shop.service;

import com.pjb.springbootjwt.shop.domain.SpShoppingCartDO;
import com.pjb.springbootjwt.shop.dto.SpShoppingCartDTO;
import com.pjb.springbootjwt.zhddkk.base.CoreService;

import java.util.List;

/**
 * .
 */
public interface SpShoppingCartService extends CoreService<SpShoppingCartDO> {

    // 我的购物车所有商品列表
    List<SpShoppingCartDTO> queryShoppingCartList(String loginUserId);

    // 我的购物车部分商品列表
    List<SpShoppingCartDTO> queryShoppingCartListByGoodsIds(String loginUserId, List<String> goodsIdList);
}
