package com.pjb.springbootjwt.shop.service.impl;

import com.pjb.springbootjwt.shop.dto.SpMerchantDTO;
import com.pjb.springbootjwt.zhddkk.util.SessionUtil;
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

    @Override
    public SpMerchantDTO queryMerchantDetail(String merchantId) {
        String userId = SessionUtil.getSessionUserId();
        return this.baseMapper.queryMerchantDetail(merchantId, userId);
    }
}
