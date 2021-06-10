package com.pjb.springbootjwt.shop.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.shop.domain.SpMerchantDO;
import com.pjb.springbootjwt.shop.dto.GoodsDetailDTO;
import com.pjb.springbootjwt.shop.service.SpGoodsService;
import com.pjb.springbootjwt.shop.service.SpMerchantService;
import com.pjb.springbootjwt.zhddkk.base.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    private SpMerchantService spMerchantService;

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
        wrapper.ne("t2.status", 0);
        wrapper.orderBy("t1.sale_price", params.isPriceSort());
        wrapper.orderBy("t1.sale_number", params.isSaleNumberSort());
        List<GoodsDetailDTO> goodsDetailDTOS = spGoodsService.queryCenterGoodsList(page, wrapper);
        return Result.ok(goodsDetailDTOS);
    }

    /**
     * 跳转商品详情页
     * @param goodsId 商品id
     * @return
     */
    @RequestMapping("/goodsDetail.page")
    public String goodsDetailPage(Model model, String goodsId){
        model.addAttribute("goodsId", goodsId);
        return "shop/spShoppingCenter/spGoodsDetail";
    }

    /**
     * 查询商品详情
     * @param goodsId 商品id
     * @return
     */
    @RequestMapping("/goodsDetail")
    @ResponseBody
    public Result<GoodsDetailDTO> goodsDetail(String goodsId){
        GoodsDetailDTO goodsDetailDTO = spGoodsService.queryCenterGoodsDetail(goodsId);
        return Result.ok(goodsDetailDTO);
    }

    /**
     * 跳转商家详情页
     * @param merchantId 商家id
     * @return
     */
    @RequestMapping("/merchantDetail.page")
    public String merchantDetail(Model model, String merchantId){
        SpMerchantDO spMerchantDO = spMerchantService.selectOne(new EntityWrapper<SpMerchantDO>().eq("merchant_id", merchantId));
        model.addAttribute("spMerchant", spMerchantDO);
        return "shop/spShoppingCenter/spMerchantDetail";
    }
}
