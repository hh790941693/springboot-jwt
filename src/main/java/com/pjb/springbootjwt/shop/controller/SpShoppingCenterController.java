package com.pjb.springbootjwt.shop.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.shop.dto.GoodsDetailDTO;
import com.pjb.springbootjwt.shop.service.SpGoodsService;
import com.pjb.springbootjwt.zhddkk.base.Result;
import org.apache.commons.lang.StringUtils;
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
    public Result<List<GoodsDetailDTO>> queryGoodsList(GoodsDetailDTO params){
        Page<GoodsDetailDTO> page = new Page<>(1, 100);
        Wrapper<GoodsDetailDTO> wrapper = new EntityWrapper<GoodsDetailDTO>();
        if (StringUtils.isNotBlank(params.getName())) {
            wrapper.like("t1.name", params.getName());
        }
        if (StringUtils.isNotBlank(params.getMerchantName())) {
            wrapper.like("t2.name", params.getMerchantName());
        }

        wrapper.ne("t1.status", 0);
        wrapper.orderBy("t1.create_time", false);
        List<GoodsDetailDTO> goodsDetailDTOS = spGoodsService.queryCenterGoodsList(page, wrapper);
        return Result.ok(goodsDetailDTOS);
    }
}
