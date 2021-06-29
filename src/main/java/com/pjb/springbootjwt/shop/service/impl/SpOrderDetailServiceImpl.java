package com.pjb.springbootjwt.shop.service.impl;

import com.pjb.springbootjwt.shop.dto.SpOrderDetailDTO;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.shop.dao.SpOrderDetailDao;
import com.pjb.springbootjwt.shop.domain.SpOrderDetailDO;
import com.pjb.springbootjwt.shop.service.SpOrderDetailService;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 订单详情表.
 */
@Service
public class SpOrderDetailServiceImpl extends CoreServiceImpl<SpOrderDetailDao, SpOrderDetailDO> implements SpOrderDetailService {

    private static final Logger logger = LoggerFactory.getLogger(SpOrderDetailServiceImpl.class);

    @Override
    public List<SpOrderDetailDTO> queryOrderDetailList(String parentOrderNo) {
        return this.baseMapper.queryOrderDetailList(parentOrderNo);
    }
}
