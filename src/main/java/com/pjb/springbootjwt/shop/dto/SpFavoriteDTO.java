package com.pjb.springbootjwt.shop.dto;

import com.pjb.springbootjwt.shop.domain.SpGoodsDO;
import com.pjb.springbootjwt.shop.domain.SpMerchantDO;
import lombok.Data;

import java.util.List;

@Data
public class SpFavoriteDTO {

    private List<SpGoodsDO> goodsList;

    private List<SpMerchantDO> merchantList;
}