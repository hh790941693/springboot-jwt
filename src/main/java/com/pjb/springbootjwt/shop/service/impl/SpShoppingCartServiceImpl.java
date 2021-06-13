package com.pjb.springbootjwt.shop.service.impl;

import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.shop.dao.SpShoppingCartDao;
import com.pjb.springbootjwt.shop.domain.SpShoppingCartDO;
import com.pjb.springbootjwt.shop.service.SpShoppingCartService;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 */
@Service
public class SpShoppingCartServiceImpl extends CoreServiceImpl<SpShoppingCartDao, SpShoppingCartDO> implements SpShoppingCartService {

    private static final Logger logger = LoggerFactory.getLogger(SpShoppingCartServiceImpl.class);
}
