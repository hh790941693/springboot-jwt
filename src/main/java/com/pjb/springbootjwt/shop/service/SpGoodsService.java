package com.pjb.springbootjwt.shop.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.shop.domain.SpGoodsDO;
import com.pjb.springbootjwt.shop.dto.SpGoodsDTO;
import com.pjb.springbootjwt.zhddkk.base.CoreService;

import java.util.List;

/**
 * 商品表.
 */
public interface SpGoodsService extends CoreService<SpGoodsDO> {
    List<SpGoodsDTO> queryCenterGoodsList(String loginUserId, Page<SpGoodsDTO> page, Wrapper<SpGoodsDTO> wrapper);

    SpGoodsDTO queryCenterGoodsDetail(String loginUserId, String goodsPkId);

    // 查询猜你喜欢商品列表
    List<SpGoodsDTO> queryMaybeLikeGoodsList(String goodsPkId);
}
