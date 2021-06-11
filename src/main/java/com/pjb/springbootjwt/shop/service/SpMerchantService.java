package com.pjb.springbootjwt.shop.service;

import com.pjb.springbootjwt.shop.domain.SpMerchantDO;
import com.pjb.springbootjwt.shop.dto.SpMerchantDTO;
import com.pjb.springbootjwt.zhddkk.base.CoreService;
import org.apache.ibatis.annotations.Param;

/**
 * 商家店铺表.
 */
public interface SpMerchantService extends CoreService<SpMerchantDO> {

    SpMerchantDTO queryMerchantDetail(String merchantId);
}
