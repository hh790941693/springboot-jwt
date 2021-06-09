package com.pjb.springbootjwt.shop.controller;

import com.pjb.springbootjwt.shop.dto.GoodsDetailDTO;
import com.pjb.springbootjwt.shop.service.SpGoodsService;
import com.pjb.springbootjwt.zhddkk.base.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 超市购物
 */
@Controller
@RequestMapping("/shop/shoppingCenter")
public class SpShoppingCenterController {

    @Autowired
    private SpGoodsService spGoodsService;

    @GetMapping()
    public String SpShoppingCenter(){
        return "shop/spShoppingCenter/spShoppingCenter";
    }

    @RequestMapping("/queryGoodsList")
    @ResponseBody
    public Result<List<GoodsDetailDTO>> queryGoodsList(){
        List<GoodsDetailDTO> goodsDetailDTOS = spGoodsService.queryCenterGoodsList();
        return Result.ok(goodsDetailDTOS);
    }
}
