package com.pjb.springbootjwt.shop.service.impl;

import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.shop.dao.SpFavoriteDao;
import com.pjb.springbootjwt.shop.domain.SpFavoriteDO;
import com.pjb.springbootjwt.shop.service.SpFavoriteService;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 收藏表，包括店铺、商品等。.
 */
@Service
public class SpFavoriteServiceImpl extends CoreServiceImpl<SpFavoriteDao, SpFavoriteDO> implements SpFavoriteService {

    private static final Logger logger = LoggerFactory.getLogger(SpFavoriteServiceImpl.class);
}
