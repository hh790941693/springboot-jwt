package com.pjb.springbootjwt.shop.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.shop.domain.*;
import com.pjb.springbootjwt.shop.dto.SpGoodsDTO;
import com.pjb.springbootjwt.shop.dto.SpMerchantDTO;
import com.pjb.springbootjwt.shop.dto.SpFavoriteDTO;
import com.pjb.springbootjwt.shop.dto.SpShoppingCartDTO;
import com.pjb.springbootjwt.shop.service.*;
import com.pjb.springbootjwt.zhddkk.base.Result;
import org.apache.commons.lang.StringUtils;
import com.pjb.springbootjwt.zhddkk.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private SpFavoriteService spFavoriteService;

    @Autowired
    private SpShoppingCartService spShoppingCartService;

    @Autowired
    private SpGoodsTypeService spGoodsTypeService;

    @Autowired
    private SpOrderService spOrderService;

    @Autowired
    private SpOrderDetailService spOrderDetailService;

    @GetMapping()
    public String SpShoppingCenter(){
        return "shop/spShoppingCenter/spShoppingCenter";
    }

    /**
     * 查询所有商家的商品列表
     * @param params
     * @return
     */
    @RequestMapping("/queryGoodsList")
    @ResponseBody
    public Result<List<SpGoodsDTO>> queryGoodsList(SpGoodsDTO params){
        Page<SpGoodsDTO> page = new Page<>(1, 100);
        Wrapper<SpGoodsDTO> wrapper = new EntityWrapper<SpGoodsDTO>();
        if (StringUtils.isNotBlank(params.getName())) {
            wrapper.like("t1.name", params.getName());
        }
        if (StringUtils.isNotBlank(params.getMerchantName())) {
            wrapper.like("t2.name", params.getMerchantName());
        }
        if (StringUtils.isNotBlank(params.getGoodsTypeId())) {
            wrapper.like("t1.goods_type_id", params.getGoodsTypeId());
        }
        wrapper.eq("t1.status", 1);
        wrapper.ne("t2.status", 0);
        wrapper.orderBy("t1.sale_price", params.isPriceSort());
        wrapper.orderBy("t1.sale_number", params.isSaleNumberSort());
        List<SpGoodsDTO> spGoodsDTOS = spGoodsService.queryCenterGoodsList(SessionUtil.getSessionUserId(), page, wrapper);
        return Result.ok(spGoodsDTOS);
    }

    /**
     * 查询指定商家的商品列表
     * @param params
     * @return
     */
    @RequestMapping("/queryGoodsListByMerchantId")
    @ResponseBody
    public Result<List<SpGoodsDTO>> queryGoodsListByMerchantId(SpGoodsDTO params){
        Page<SpGoodsDTO> page = new Page<>(1, 100);
        Wrapper<SpGoodsDTO> wrapper = new EntityWrapper<SpGoodsDTO>();
        if (StringUtils.isNotBlank(params.getMerchantId())) {
            wrapper.like("t1.merchant_id", params.getMerchantId());
        }
        wrapper.ne("t1.status", 0);
        wrapper.ne("t2.status", 0);
        wrapper.orderBy("t1.sale_number", false);
        List<SpGoodsDTO> spGoodsDTOS = spGoodsService.queryCenterGoodsList(SessionUtil.getSessionUserId(), page, wrapper);
        return Result.ok(spGoodsDTOS);
    }

    /**
     * 跳转商品详情页
     * @param goodsPkId 商品主键id
     * @return
     */
    @RequestMapping("/goodsDetail.page")
    public String goodsDetailPage(Model model, String goodsPkId){
        model.addAttribute("goodsPkId", goodsPkId);
        return "shop/spShoppingCenter/spGoodsDetail";
    }

    /**
     * 查询商品详情
     * @param goodsPkId 商品主键id
     * @return
     */
    @RequestMapping("/goodsDetail")
    @ResponseBody
    public Result<SpGoodsDTO> goodsDetail(String goodsPkId){
        SpGoodsDTO spGoodsDTO = spGoodsService.queryCenterGoodsDetail(SessionUtil.getSessionUserId(), goodsPkId);
        return Result.ok(spGoodsDTO);
    }

    /**
     * 跳转商家详情页
     * @param merchantId 商家id
     * @return
     */
    @RequestMapping("/merchantDetail.page")
    public String merchantDetailPage(Model model, String merchantId){
        model.addAttribute("merchantId", merchantId);
        return "shop/spShoppingCenter/spMerchantDetail";
    }

    /**
     * 查询商家详情
     * @param merchantId 商家id
     * @return
     */
    @RequestMapping("/merchantDetail")
    @ResponseBody
    public Result<SpMerchantDTO> merchantDetail(String merchantId){
        SpMerchantDTO spMerchantDTO = spMerchantService.queryMerchantDetail(merchantId);
        return Result.ok(spMerchantDTO);
    }

    /**
     * 我的收藏页面
     * @return
     */
    @RequestMapping("/spMyFavorite.page")
    public String spMyFavorite(){
        return "shop/spShoppingCenter/spMyFavorite";
    }

    /**
     * 查询我的收藏列表
     * @return
     */
    @RequestMapping("/queryFavoriteSubjectList")
    @ResponseBody
    public Result<SpFavoriteDTO> queryFavoriteGoodsList(){
        List<SpGoodsDTO> goodsDoList = spFavoriteService.queryFavoriteGoodsList(SessionUtil.getSessionUserId());
        List<SpMerchantDO> merchantDoList = spFavoriteService.queryFavoriteMerchantList(SessionUtil.getSessionUserId());

        SpFavoriteDTO spFavoriteDTO = new SpFavoriteDTO();
        spFavoriteDTO.setGoodsList(goodsDoList);
        spFavoriteDTO.setMerchantList(merchantDoList);
        return Result.ok(spFavoriteDTO);
    }

    /**
     * 我的购物车页面
     * @return
     */
    @RequestMapping("/spMyShoppingCart.page")
    public String spMyShoppingCart(){
        return "shop/spShoppingCenter/spMyShoppingCart";
    }

    /**
     * 查询我的收藏列表
     * @return
     */
    @RequestMapping("/queryShoppingCartList")
    @ResponseBody
    public Result<List<SpShoppingCartDTO>> queryShoppingCartList(){
        List<SpShoppingCartDTO> spShoppingCartDTOList = spShoppingCartService.queryShoppingCartList(SessionUtil.getSessionUserId());
        return Result.ok(spShoppingCartDTOList);
    }

    @RequestMapping("/queryGoodsTypeList")
    @ResponseBody
    public Result<List<SpGoodsTypeDO>> queryGoodsTypeList() {
        List<SpGoodsTypeDO> list = spGoodsTypeService.selectList(new EntityWrapper<SpGoodsTypeDO>().ne("status", 0));
        return Result.ok(list);
    }

    /**
     * 购物车商品增加
     * @param cartPkgId
     * @return
     */
    @PostMapping("/addShoppingCartGoodsCount")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Result<String> addShoppingCartGoodsCount(String cartPkgId){
        SpShoppingCartDO spShoppingCartDO = spShoppingCartService.selectById(cartPkgId);
        spShoppingCartDO.setGoodsCount(spShoppingCartDO.getGoodsCount()+1);
        spShoppingCartService.updateById(spShoppingCartDO);
        return Result.ok();
    }

    /**
     * 购物车商品减少
     * @param cartPkgId
     * @return
     */
    @PostMapping("/minusShoppingCartGoodsCount")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Result<String> minusShoppingCartGoodsCount(String cartPkgId){
        SpShoppingCartDO spShoppingCartDO = spShoppingCartService.selectById(cartPkgId);
        if (spShoppingCartDO.getGoodsCount() <= 0 ) {
            return Result.fail("商品数量不能小于0");
        }
        spShoppingCartDO.setGoodsCount(spShoppingCartDO.getGoodsCount()-1);
        spShoppingCartService.updateById(spShoppingCartDO);
        return Result.ok();
    }

    @PostMapping("/createOrder")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Result<String> createOrder(@RequestParam("goodsIdArr[]") String[] goodsIdArr){
        List<SpShoppingCartDTO> spShoppingCartDTOList = spShoppingCartService.queryShoppingCartList(SessionUtil.getSessionUserId());
        List<String> goodsIdList = new ArrayList<>(Arrays.asList(goodsIdArr));
        List<SpShoppingCartDTO> orderGoodsList = spShoppingCartDTOList.stream().filter(cart->goodsIdList.contains(cart.getGoodsId())).collect(Collectors.toList());

        BigDecimal totalOriginalPrice = new BigDecimal(0);
        BigDecimal totalPayPrice = new BigDecimal(0);
        for (SpShoppingCartDTO spShoppingCartDTO : orderGoodsList) {
            BigDecimal tmpOriginalPrice = spShoppingCartDTO.getOriginalPrice().multiply(new BigDecimal(spShoppingCartDTO.getGoodsCount()));
            BigDecimal tmpSalePrice = spShoppingCartDTO.getSalePrice().multiply(new BigDecimal(spShoppingCartDTO.getGoodsCount()));

            totalOriginalPrice = totalOriginalPrice.add(tmpOriginalPrice);
            totalPayPrice = totalPayPrice.add(tmpSalePrice);
        }

        // 主订单
        SpOrderDO mainOrder = new SpOrderDO();
        mainOrder.setOrderNo("porder_"+ UUID.randomUUID().toString().replaceAll("-", ""));
        mainOrder.setTotalPrice(totalOriginalPrice);
        mainOrder.setPayPrice(totalPayPrice);
        mainOrder.setOrderUserId(Long.valueOf(SessionUtil.getSessionUserId()));
        mainOrder.setStatus(1);
        mainOrder.setCreateTime(new Date());
        mainOrder.setUpdateTime(new Date());
        spOrderService.insert(mainOrder);

        for (SpShoppingCartDTO spShoppingCartDTO : orderGoodsList) {
            // 各店铺订单
            SpOrderDO subOrder = new SpOrderDO();
            subOrder.setOrderNo("sorder_"+ UUID.randomUUID().toString().replaceAll("-", ""));
            subOrder.setParentOrderNo(mainOrder.getOrderNo());
            subOrder.setMerchantId(spShoppingCartDTO.getMerchantId());
            subOrder.setGoodsId(spShoppingCartDTO.getGoodsId());
            subOrder.setTotalPrice(spShoppingCartDTO.getOriginalPrice().multiply(new BigDecimal(spShoppingCartDTO.getGoodsCount())));
            subOrder.setPayPrice(spShoppingCartDTO.getSalePrice().multiply(new BigDecimal(spShoppingCartDTO.getGoodsCount())));
            subOrder.setOrderUserId(Long.valueOf(SessionUtil.getSessionUserId()));
            subOrder.setStatus(1);
            subOrder.setCreateTime(new Date());
            subOrder.setUpdateTime(new Date());
            spOrderService.insert(subOrder);

            // 订单商品详情
            SpOrderDetailDO spOrderDetailDO = new SpOrderDetailDO();
            spOrderDetailDO.setOrderNo(subOrder.getOrderNo());
            spOrderDetailDO.setGoodsId(spShoppingCartDTO.getGoodsId());
            spOrderDetailDO.setGoodsCount(spShoppingCartDTO.getGoodsCount());
            spOrderDetailDO.setGoodsOriginalPrice(spShoppingCartDTO.getOriginalPrice());
            spOrderDetailDO.setGoodsSalePrice(spShoppingCartDTO.getSalePrice());
            spOrderDetailDO.setMerchantId(spShoppingCartDTO.getMerchantId());
            spOrderDetailDO.setCreateTime(new Date());
            spOrderDetailDO.setUpdateTime(new Date());
            spOrderDetailService.insert(spOrderDetailDO);

            spShoppingCartService.delete(new EntityWrapper<SpShoppingCartDO>().eq("goods_id", spShoppingCartDTO.getGoodsId()).eq("user_id", SessionUtil.getSessionUserId()));
        }
        return Result.ok(mainOrder.getOrderNo());
    }
}
