package com.pjb.springbootjwt.shop.dao;

import com.pjb.springbootjwt.shop.domain.SpFavoriteDO;
import com.pjb.springbootjwt.zhddkk.base.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * 收藏表，包括店铺、商品等。
 */
@Repository
public interface SpFavoriteDao extends BaseDao<SpFavoriteDO> {

}
