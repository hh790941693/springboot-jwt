package com.pjb.springbootjwt.shop.dao;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.pjb.springbootjwt.shop.domain.SpGoodsDO;
import com.pjb.springbootjwt.shop.dto.SpGoodsDTO;
import com.pjb.springbootjwt.zhddkk.base.BaseDao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品表
 */
@Repository
public interface SpGoodsDao extends BaseDao<SpGoodsDO> {
    // 查询商品详情列表
    List<SpGoodsDTO> queryCenterGoodsList(@Param("loginUserId")String loginUserId, RowBounds rowBounds, @Param("ew") Wrapper<SpGoodsDTO> wrapper);

    // 查询商品详情
    SpGoodsDTO queryCenterGoodsDetail(@Param("loginUserId")String loginUserId, @Param("goodsPkId")String goodsPkId);

    // 查询猜你喜欢商品列表
    List<SpGoodsDTO> queryMaybeLikeGoodsList(@Param("goodsPkId")String goodsPkId);
}
