package com.pjb.springbootjwt.shop.dto;

import lombok.Data;
import java.util.List;


@Data
public class SpShoppingCenterDTO {

    // 收藏数
    private int favoriteNum;

    // 购物车商品数
    private int shoppingCartNum;

    // 我的订单数
    private int orderNum;

    // 超市商品列表
    private List<SpGoodsDTO> goodsList;
}
