package com.pjb.springbootjwt.shop.dao;

import com.pjb.springbootjwt.shop.domain.SpFavoriteDO;
import com.pjb.springbootjwt.shop.dto.GoodsDetailDTO;
import com.pjb.springbootjwt.shop.domain.SpMerchantDO;
import com.pjb.springbootjwt.zhddkk.base.BaseDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 收藏表，包括店铺、商品等。
 */
@Repository
public interface SpFavoriteDao extends BaseDao<SpFavoriteDO> {

    List<GoodsDetailDTO> queryFavoriteGoodsList(@Param("loginUserId")String loginUserId);

    List<SpMerchantDO> queryFavoriteMerchantList(@Param("loginUserId")String loginUserId);
}
