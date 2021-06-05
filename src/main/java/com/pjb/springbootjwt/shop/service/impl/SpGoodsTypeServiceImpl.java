package com.pjb.springbootjwt.shop.service.impl;

import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.shop.dao.SpGoodsTypeDao;
import com.pjb.springbootjwt.shop.domain.SpGoodsTypeDO;
import com.pjb.springbootjwt.shop.service.SpGoodsTypeService;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 商品分类表.
 */
@Service
public class SpGoodsTypeServiceImpl extends CoreServiceImpl<SpGoodsTypeDao, SpGoodsTypeDO> implements SpGoodsTypeService {

    private static final Logger logger = LoggerFactory.getLogger(SpGoodsTypeServiceImpl.class);
}
