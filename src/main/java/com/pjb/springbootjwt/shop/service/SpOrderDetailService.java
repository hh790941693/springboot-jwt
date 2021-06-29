package com.pjb.springbootjwt.shop.service;

import com.pjb.springbootjwt.shop.domain.SpOrderDetailDO;
import com.pjb.springbootjwt.shop.dto.SpOrderDetailDTO;
import com.pjb.springbootjwt.zhddkk.base.CoreService;

import java.util.List;

/**
 * 订单详情表.
 */
public interface SpOrderDetailService extends CoreService<SpOrderDetailDO> {

    List<SpOrderDetailDTO> queryOrderDetailList(String parentOrderNo);
}
