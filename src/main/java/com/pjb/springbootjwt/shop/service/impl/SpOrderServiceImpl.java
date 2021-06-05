package com.pjb.springbootjwt.shop.service.impl;

import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.shop.dao.SpOrderDao;
import com.pjb.springbootjwt.shop.domain.SpOrderDO;
import com.pjb.springbootjwt.shop.service.SpOrderService;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 订单表.
 */
@Service
public class SpOrderServiceImpl extends CoreServiceImpl<SpOrderDao, SpOrderDO> implements SpOrderService {

    private static final Logger logger = LoggerFactory.getLogger(SpOrderServiceImpl.class);
}
