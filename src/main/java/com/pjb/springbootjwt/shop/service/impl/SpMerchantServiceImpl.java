package com.pjb.springbootjwt.shop.service.impl;

import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.shop.dao.SpMerchantDao;
import com.pjb.springbootjwt.shop.domain.SpMerchantDO;
import com.pjb.springbootjwt.shop.service.SpMerchantService;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 商家店铺表.
 */
@Service
public class SpMerchantServiceImpl extends CoreServiceImpl<SpMerchantDao, SpMerchantDO> implements SpMerchantService {

    private static final Logger logger = LoggerFactory.getLogger(SpMerchantServiceImpl.class);
}
