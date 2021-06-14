package com.pjb.springbootjwt.shop.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.shop.dto.GoodsDetailDTO;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.shop.dao.SpGoodsDao;
import com.pjb.springbootjwt.shop.domain.SpGoodsDO;
import com.pjb.springbootjwt.shop.service.SpGoodsService;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 商品表.
 */
@Service
public class SpGoodsServiceImpl extends CoreServiceImpl<SpGoodsDao, SpGoodsDO> implements SpGoodsService {

    private static final Logger logger = LoggerFactory.getLogger(SpGoodsServiceImpl.class);

    @Override
    public List<GoodsDetailDTO> queryCenterGoodsList(String loginUserId, Page<GoodsDetailDTO> page, Wrapper<GoodsDetailDTO> wrapper) {
        return this.baseMapper.queryCenterGoodsList(loginUserId, page, wrapper);
    }

    @Override
    public GoodsDetailDTO queryCenterGoodsDetail(String loginUserId, String goodsPkId) {
        return this.baseMapper.queryCenterGoodsDetail(loginUserId, goodsPkId);
    }
}
