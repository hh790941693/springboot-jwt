package com.pjb.springbootjwt.shop.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.shop.domain.*;
import com.pjb.springbootjwt.shop.dto.*;
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
    public Result<SpShoppingCenterDTO> queryGoodsList(SpGoodsDTO params){
        SpShoppingCenterDTO spShoppingCenterDTO = new SpShoppingCenterDTO();

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
        List<SpGoodsDTO> spGoodsList = spGoodsService.queryCenterGoodsList(SessionUtil.getSessionUserId(), page, wrapper);
        spShoppingCenterDTO.setGoodsList(spGoodsList);

        int shoppingCartNum = spShoppingCartService.selectCount(new EntityWrapper<SpShoppingCartDO>().eq("user_id", SessionUtil.getSessionUserId()));
        spShoppingCenterDTO.setShoppingCartNum(shoppingCartNum);

        int favoriteNum = spFavoriteService.selectCount(new EntityWrapper<SpFavoriteDO>().eq("user_id", SessionUtil.getSessionUserId()).eq("status", 1));
        spShoppingCenterDTO.setFavoriteNum(favoriteNum);

        int orderNum = spOrderService.selectCount(new EntityWrapper<SpOrderDO>().eq("order_user_id", SessionUtil.getSessionUserId()).isNull("parent_order_no"));
        spShoppingCenterDTO.setOrderNum(orderNum);

        return Result.ok(spShoppingCenterDTO);
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

    /**
     * 直接下单
     * @param goodsId 商品id
     * @param goodsCount 商品数量
     * @return
     */
    @PostMapping("/createOrder")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Result<String> createOrder(String goodsId, int goodsCount) {
        SpGoodsDO spGoodsDO = spGoodsService.selectOne(new EntityWrapper<SpGoodsDO>().eq("goods_id", goodsId));
        if (null == spGoodsDO) {
            return Result.fail("商品不存在");
        }

        // 主订单
        SpOrderDO mainOrder = new SpOrderDO();
        mainOrder.setOrderNo("porder_"+ UUID.randomUUID().toString().replaceAll("-", ""));
        mainOrder.setTotalPrice(spGoodsDO.getOriginalPrice().multiply(new BigDecimal(goodsCount)));
        mainOrder.setPayPrice(spGoodsDO.getSalePrice().multiply(new BigDecimal(goodsCount)));
        mainOrder.setOrderUserId(Long.valueOf(SessionUtil.getSessionUserId()));
        //支付状态 1:待支付 2:已支付
        mainOrder.setPayStatus(1);
        //物流状态 3:未发货 4:已发货
        mainOrder.setLogisticsStatus(3);
        //状态 1：待支付 2:已支付 3:待发货 4:已发货 5:已确认收货
        mainOrder.setStatus(1);
        mainOrder.setCreateTime(new Date());
        mainOrder.setUpdateTime(new Date());
        spOrderService.insert(mainOrder);

        // 各店铺订单
        SpOrderDO subOrder = new SpOrderDO();
        subOrder.setOrderNo("sorder_"+ UUID.randomUUID().toString().replaceAll("-", ""));
        subOrder.setParentOrderNo(mainOrder.getOrderNo());
        subOrder.setMerchantId(spGoodsDO.getMerchantId());
        subOrder.setGoodsId(goodsId);
        subOrder.setTotalPrice(spGoodsDO.getOriginalPrice().multiply(new BigDecimal(goodsCount)));
        subOrder.setPayPrice(spGoodsDO.getSalePrice().multiply(new BigDecimal(goodsCount)));
        subOrder.setOrderUserId(Long.valueOf(SessionUtil.getSessionUserId()));
        //支付状态 1:待支付 2:已支付
        subOrder.setPayStatus(1);
        //物流状态 3:未发货 4:已发货
        subOrder.setLogisticsStatus(3);
        //状态 1：待支付 2:已支付 3:待发货 4:已发货 5:已确认收货
        subOrder.setStatus(1);
        subOrder.setCreateTime(new Date());
        subOrder.setUpdateTime(new Date());
        spOrderService.insert(subOrder);

        // 各店铺订单商品详情
        SpOrderDetailDO spOrderDetailDO = new SpOrderDetailDO();
        spOrderDetailDO.setOrderNo(subOrder.getOrderNo());
        spOrderDetailDO.setGoodsId(goodsId);
        spOrderDetailDO.setGoodsCount(goodsCount);
        spOrderDetailDO.setGoodsOriginalPrice(spGoodsDO.getOriginalPrice());
        spOrderDetailDO.setGoodsSalePrice(spGoodsDO.getSalePrice());
        spOrderDetailDO.setMerchantId(spGoodsDO.getMerchantId());
        spOrderDetailDO.setCreateTime(new Date());
        spOrderDetailDO.setUpdateTime(new Date());
        spOrderDetailService.insert(spOrderDetailDO);

        // 更新商品库存
        spGoodsDO.setStockNum(spGoodsDO.getStockNum() - goodsCount);
        spGoodsDO.setSaleNumber(spGoodsDO.getSaleNumber() + goodsCount);
        spGoodsService.updateById(spGoodsDO);

        return Result.ok(mainOrder.getOrderNo());
    }

    /**
     * 购物车下单
     * @param goodsIdArr 商品id数组
     * @return
     */
    @PostMapping("/createCartOrder")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Result<String> createCartOrder(@RequestParam("goodsIdArr[]") String[] goodsIdArr){
        List<SpShoppingCartDTO> spShoppingCartDTOList = spShoppingCartService.queryShoppingCartList(SessionUtil.getSessionUserId());
        List<String> goodsIdList = new ArrayList<>(Arrays.asList(goodsIdArr));
        List<SpShoppingCartDTO> orderGoodsList = spShoppingCartDTOList.stream().filter(cart->goodsIdList.contains(cart.getGoodsId())).collect(Collectors.toList());
        if (null == orderGoodsList || orderGoodsList.size() == 0) {
            return Result.fail("请先选择要购买的商品");
        }
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
        //支付状态 1:待支付 2:已支付
        mainOrder.setPayStatus(1);
        //物流状态 3:未发货 4:已发货
        mainOrder.setLogisticsStatus(3);
        //状态 1：待支付 2:已支付 3:待发货 4:已发货 5:已确认收货
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
            //支付状态 1:待支付 2:已支付
            subOrder.setPayStatus(1);
            //物流状态 3:未发货 4:已发货
            subOrder.setLogisticsStatus(3);
            //状态 1：待支付 2:已支付 3:待发货 4:已发货 5:已确认收货
            subOrder.setStatus(1);
            subOrder.setCreateTime(new Date());
            subOrder.setUpdateTime(new Date());
            spOrderService.insert(subOrder);

            // 各店铺订单商品详情
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

            // 删除购物车商品
            spShoppingCartService.delete(new EntityWrapper<SpShoppingCartDO>().eq("goods_id", spShoppingCartDTO.getGoodsId()).eq("user_id", SessionUtil.getSessionUserId()));

            // 更新商品库存
            SpGoodsDO spGoodsDO = spGoodsService.selectOne(new EntityWrapper<SpGoodsDO>().eq("goods_id", spShoppingCartDTO.getGoodsId()));
            spGoodsDO.setStockNum(spGoodsDO.getStockNum() - spShoppingCartDTO.getGoodsCount());
            spGoodsDO.setSaleNumber(spGoodsDO.getSaleNumber() + spShoppingCartDTO.getGoodsCount());
            spGoodsService.updateById(spGoodsDO);
        }
        return Result.ok(mainOrder.getOrderNo());
    }

    /**
     * 支付页面.
     * @return
     */
    @RequestMapping("/spShoppingPay.page")
    public String spShoppingPay(Model model, String orderNo){
        model.addAttribute("orderNo", orderNo);
        return "shop/spShoppingCenter/spShoppingPay";
    }

    /**
     * 支付页面订单详情.
     * @param parentOrderNo
     * @return
     */
    @GetMapping("/queryOrderInfo")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Result<SpOrderDTO> queryOrderDetailList(String parentOrderNo) {
        SpOrderDO mainOrder = spOrderService.selectOne(new EntityWrapper<SpOrderDO>().eq("order_no", parentOrderNo));
        if (null == mainOrder) {
            return Result.fail();
        }
        SpOrderDTO spOrderDTO = new SpOrderDTO();
        spOrderDTO.setMainOrder(mainOrder);
        List<SpOrderDetailDTO> spOrderDetailList = spOrderDetailService.queryOrderDetailList(parentOrderNo);
        spOrderDTO.setSubOrderList(spOrderDetailList);

        return Result.ok(spOrderDTO);
    }

    /**
     * 订单支付.
     * @param parentOrderNo
     * @param payWay
     * @param payPrice
     * @return
     */
    @PostMapping("/pay")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Result<String> pay(String parentOrderNo, int payWay, String payPrice){
        SpOrderDO mainOrder = spOrderService.selectOne(new EntityWrapper<SpOrderDO>().eq("order_no", parentOrderNo));
        if (null == mainOrder) {
            return Result.fail("订单不存在");
        }

        // 检查订单是否已经取消  取消状态 5:未取消 6:已取消
        if (mainOrder.getCancelStatus().intValue() == 6 || mainOrder.getStatus().intValue() == 6) {
            return Result.fail("订单已取消,无法进行支付。");
        }

        // 检查是否已经支付  状态 1：待支付 2:已支付 3:待发货 4:已发货 6:已取消 9:已确认收货
        if (mainOrder.getPayStatus().intValue() != 1 || mainOrder.getStatus().intValue() != 1) {
            return Result.fail("无需重复支付。");
        }

        // 主订单
        //已支付
        mainOrder.setPayStatus(2);
        //支付方式
        mainOrder.setPayWay(payWay);
        //待发货
        mainOrder.setLogisticsStatus(3);
        //待发货
        mainOrder.setStatus(3);
        mainOrder.setUpdateTime(new Date());
        //支付人
        mainOrder.setPayUserId(Long.valueOf(SessionUtil.getSessionUserId()));
        //支付价格
        mainOrder.setPayPrice(new BigDecimal(payPrice));
        spOrderService.updateById(mainOrder);

        // 子订单
        List<SpOrderDO> subOrderList = spOrderService.selectList(new EntityWrapper<SpOrderDO>()
                .eq("parent_order_no", parentOrderNo).eq("status", 1));
        for (SpOrderDO subOrderDO : subOrderList) {
            //已支付
            subOrderDO.setPayStatus(2);
            //支付方式
            subOrderDO.setPayWay(payWay);
            //待发货
            subOrderDO.setLogisticsStatus(3);
            //待发货
            subOrderDO.setStatus(3);
            subOrderDO.setUpdateTime(new Date());
            //支付人
            subOrderDO.setPayUserId(Long.valueOf(SessionUtil.getSessionUserId()));
            spOrderService.updateById(subOrderDO);
        }

        return Result.ok();
    }

    /**
     * 我的订单页面.
     * @return
     */
    @RequestMapping("/spMyOrder.page")
    public String myOrder(){
        return "shop/spShoppingCenter/spMyOrder";
    }

    /**
     * 我的订单列表.
     * @return
     */
    @GetMapping("/queryMyOrderList")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Result<List<SpOrderDTO>> queryMyOrderList(){

        List<SpOrderDTO> resultList = new ArrayList<>();
        List<SpOrderDO> mainOrderList = spOrderService.selectList(new EntityWrapper<SpOrderDO>().eq("order_user_id", SessionUtil.getSessionUserId()).isNull("parent_order_no").orderBy("status"));
        for (SpOrderDO mainOrder : mainOrderList) {
            List<SpOrderDetailDTO> spOrderDetailList = spOrderDetailService.queryOrderDetailList(mainOrder.getOrderNo());

            SpOrderDTO spOrderDTO = new SpOrderDTO();
            spOrderDTO.setMainOrder(mainOrder);
            spOrderDTO.setSubOrderList(spOrderDetailList);
            resultList.add(spOrderDTO);
        }

        return Result.ok(resultList);
    }

    /**
     * 取消订单.
     * @return
     */
    @PostMapping("/cancelOrder")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Result<String> cancelOrder(String parentOrderNo){
        SpOrderDO mainOrder = spOrderService.selectOne(new EntityWrapper<SpOrderDO>().eq("order_no", parentOrderNo));
        if (null == mainOrder) {
            return Result.fail("订单不存在");
        }

        if (mainOrder.getStatus().intValue() == 6) {
            return Result.ok();
        }

        if (mainOrder.getPayStatus().intValue() != 1 || mainOrder.getStatus().intValue() != 1) {
            return Result.fail("当前订单已支付,无法取消。");
        }

        // 主订单
        mainOrder.setCancelStatus(6);
        mainOrder.setStatus(6);
        mainOrder.setUpdateTime(new Date());
        spOrderService.updateById(mainOrder);

        // 子订单
        List<SpOrderDO> subOrderList = spOrderService.selectList(new EntityWrapper<SpOrderDO>()
                .eq("parent_order_no", parentOrderNo));
        for (SpOrderDO subOrderDO : subOrderList) {
            subOrderDO.setCancelStatus(6);
            subOrderDO.setStatus(6);
            subOrderDO.setUpdateTime(new Date());
            spOrderService.updateById(subOrderDO);
        }

        return Result.ok();
    }
}
