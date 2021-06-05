package com.pjb.springbootjwt.shop.service.impl;

import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.shop.dao.SpMerchantApplyDao;
import com.pjb.springbootjwt.shop.domain.SpMerchantApplyDO;
import com.pjb.springbootjwt.shop.service.SpMerchantApplyService;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 申请成为商家表.
 */
@Service
public class SpMerchantApplyServiceImpl extends CoreServiceImpl<SpMerchantApplyDao, SpMerchantApplyDO> implements SpMerchantApplyService {

    private static final Logger logger = LoggerFactory.getLogger(SpMerchantApplyServiceImpl.class);
}
