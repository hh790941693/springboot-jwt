package com.pjb.springbootjwt.shop.service;

import com.pjb.springbootjwt.shop.domain.SpGoodsDO;
import com.pjb.springbootjwt.shop.dto.GoodsDetailDTO;
import com.pjb.springbootjwt.zhddkk.base.CoreService;

import java.util.List;

/**
 * 商品表.
 */
public interface SpGoodsService extends CoreService<SpGoodsDO> {
    List<GoodsDetailDTO> queryCenterGoodsList();
}
