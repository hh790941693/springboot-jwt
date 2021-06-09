package com.pjb.springbootjwt.shop.dao;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.pjb.springbootjwt.shop.domain.SpGoodsDO;
import com.pjb.springbootjwt.shop.dto.GoodsDetailDTO;
import com.pjb.springbootjwt.zhddkk.base.BaseDao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品表
 */
@Repository
public interface SpGoodsDao extends BaseDao<SpGoodsDO> {
    List<GoodsDetailDTO> queryCenterGoodsList(RowBounds rowBounds, @Param("ew") Wrapper<GoodsDetailDTO> wrapper);
}
