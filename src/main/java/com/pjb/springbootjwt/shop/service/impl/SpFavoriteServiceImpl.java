package com.pjb.springbootjwt.shop.service.impl;

import com.pjb.springbootjwt.shop.dto.SpGoodsDTO;
import com.pjb.springbootjwt.shop.domain.SpMerchantDO;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.shop.dao.SpFavoriteDao;
import com.pjb.springbootjwt.shop.domain.SpFavoriteDO;
import com.pjb.springbootjwt.shop.service.SpFavoriteService;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 收藏表，包括店铺、商品等。.
 */
@Service
public class SpFavoriteServiceImpl extends CoreServiceImpl<SpFavoriteDao, SpFavoriteDO> implements SpFavoriteService {

    private static final Logger logger = LoggerFactory.getLogger(SpFavoriteServiceImpl.class);

    @Override
    public List<SpGoodsDTO> queryFavoriteGoodsList(String loginUserId) {
        return this.baseMapper.queryFavoriteGoodsList(loginUserId);
    }

    @Override
    public List<SpMerchantDO> queryFavoriteMerchantList(String loginUserId) {
        return this.baseMapper.queryFavoriteMerchantList(loginUserId);
    }
}
