package com.pjb.springbootjwt.shop.dao;

import com.pjb.springbootjwt.shop.domain.SpMerchantDO;
import com.pjb.springbootjwt.shop.dto.SpMerchantDTO;
import com.pjb.springbootjwt.zhddkk.base.BaseDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 商家店铺表
 */
@Repository
public interface SpMerchantDao extends BaseDao<SpMerchantDO> {

    /**
     * 查询店铺详情
     * @param merchantId 店铺id
     * @return
     */
    SpMerchantDTO queryMerchantDetail(@Param("merchantId")String merchantId, @Param("loginUserId")String loginUserId);
}
