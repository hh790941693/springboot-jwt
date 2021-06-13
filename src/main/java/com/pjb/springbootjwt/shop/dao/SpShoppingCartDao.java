package com.pjb.springbootjwt.shop.dao;

import com.pjb.springbootjwt.shop.domain.SpShoppingCartDO;
import com.pjb.springbootjwt.shop.dto.SpShoppingCartDTO;
import com.pjb.springbootjwt.zhddkk.base.BaseDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 */
@Repository
public interface SpShoppingCartDao extends BaseDao<SpShoppingCartDO> {
    List<SpShoppingCartDTO> queryShoppingCartList(@Param("loginUserId") String loginUserId);
}
