package com.pjb.springbootjwt.shop.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.shop.domain.SpGoodsDO;
import com.pjb.springbootjwt.shop.dto.GoodsDetailDTO;
import com.pjb.springbootjwt.zhddkk.base.CoreService;
import com.pjb.springbootjwt.zhddkk.domain.WsFriendsDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品表.
 */
public interface SpGoodsService extends CoreService<SpGoodsDO> {
    List<GoodsDetailDTO> queryCenterGoodsList(Page<GoodsDetailDTO> page, Wrapper<GoodsDetailDTO> wrapper);

    GoodsDetailDTO queryCenterGoodsDetail(String goodsId);
}
