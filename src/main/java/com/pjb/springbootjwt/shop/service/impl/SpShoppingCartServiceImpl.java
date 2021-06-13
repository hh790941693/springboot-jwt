package com.pjb.springbootjwt.shop.service.impl;

import com.pjb.springbootjwt.shop.dto.SpShoppingCartDTO;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.shop.dao.SpShoppingCartDao;
import com.pjb.springbootjwt.shop.domain.SpShoppingCartDO;
import com.pjb.springbootjwt.shop.service.SpShoppingCartService;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * .
 */
@Service
public class SpShoppingCartServiceImpl extends CoreServiceImpl<SpShoppingCartDao, SpShoppingCartDO> implements SpShoppingCartService {

    private static final Logger logger = LoggerFactory.getLogger(SpShoppingCartServiceImpl.class);

    @Override
    public List<SpShoppingCartDTO> queryShoppingCartList(String loginUserId) {
        return this.baseMapper.queryShoppingCartList(loginUserId);
    }
}
