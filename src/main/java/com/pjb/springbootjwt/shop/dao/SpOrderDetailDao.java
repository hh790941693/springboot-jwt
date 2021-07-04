package com.pjb.springbootjwt.shop.dao;

import com.pjb.springbootjwt.shop.domain.SpOrderDetailDO;
import com.pjb.springbootjwt.shop.dto.SpOrderDetailDTO;
import com.pjb.springbootjwt.zhddkk.base.BaseDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单详情表
 */
@Repository
public interface SpOrderDetailDao extends BaseDao<SpOrderDetailDO> {

    List<SpOrderDetailDTO> queryOrderDetailListByOrderNo(@Param("orderNo")String orderNo);
    List<SpOrderDetailDTO> queryOrderDetailListByParentOrderNo(@Param("parentOrderNo")String parentOrderNo);
}
