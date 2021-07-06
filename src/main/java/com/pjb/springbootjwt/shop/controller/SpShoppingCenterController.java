package com.pjb.springbootjwt.shop.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.shop.ShopUtil;
import com.pjb.springbootjwt.shop.constants.SpConstants;
import com.pjb.springbootjwt.shop.domain.*;
import com.pjb.springbootjwt.shop.dto.*;
import com.pjb.springbootjwt.shop.service.*;
import com.pjb.springbootjwt.zhddkk.base.Result;
import org.apache.commons.lang.StringUtils;
import com.pjb.springbootjwt.zhddkk.util.SessionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 超市购物中心.
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
     * 购物首页商品列表
     * @param params 查询条件
     * @return
     */
    @RequestMapping("/queryGoodsList")
    @ResponseBody
    public Result<SpShoppingCenterDTO> queryGoodsList(SpGoodsDTO params){
        SpShoppingCenterDTO spShoppingCenterDTO = new SpShoppingCenterDTO();

        Page<SpGoodsDTO> page = new Page<>(1, 800);
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
        wrapper.eq("t1.status", 1);  //商品状态 0:已删除 1:已上架 2:已下架
        wrapper.ne("t2.status", 0);  //店铺状态 0:已关闭 1:营业中 2:暂停营业 3:已打烊
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
        Page<SpGoodsDTO> page = new Page<>(1, 800);
        Wrapper<SpGoodsDTO> wrapper = new EntityWrapper<SpGoodsDTO>();
        if (StringUtils.isNotBlank(params.getMerchantId())) {
            wrapper.like("t1.merchant_id", params.getMerchantId());
        }
        wrapper.eq("t1.status", 1); //商品状态 0:已删除 1:已上架 2:已下架
        wrapper.ne("t2.status", 0); //店铺状态 0:已关闭 1:营业中 2:暂停营业 3:已打烊
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
     * 我的收藏列表
     * @return
     */
    @RequestMapping("/queryFavoriteSubjectList")
    @ResponseBody
    public Result<SpFavoriteDTO> queryFavoriteSubjectList(){
        // 收藏的商品列表
        List<SpGoodsDTO> goodsDoList = spFavoriteService.queryFavoriteGoodsList(SessionUtil.getSessionUserId());
        // 收藏的店铺列表
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
     * 购物车列表
     * @return
     */
    @RequestMapping("/queryShoppingCartList")
    @ResponseBody
    public Result<List<SpShoppingCartDTO>> queryShoppingCartList(){
        List<SpShoppingCartDTO> spShoppingCartDTOList = spShoppingCartService.queryShoppingCartList(SessionUtil.getSessionUserId());
        return Result.ok(spShoppingCartDTOList);
    }

    /**
     * 查询商品类型列表
     * @return
     */
    @RequestMapping("/queryGoodsTypeList")
    @ResponseBody
    public Result<List<SpGoodsTypeDO>> queryGoodsTypeList() {
        List<SpGoodsTypeDO> list = spGoodsTypeService.selectList(new EntityWrapper<SpGoodsTypeDO>().ne("status", 0));
        return Result.ok(list);
    }

    /**
     * 购物车添加商品
     * @param cartPkgId 购物车表主键id
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
     * 购物车减少商品
     * @param cartPkgId 购物车表主键id
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
        if (goodsCount <= 0) {
            return Result.fail("入参有误");
        }
        if (goodsCount > spGoodsDO.getStockNum()) {
            return Result.fail("商品数量超过了库存数"+spGoodsDO.getStockNum());
        }

        // 主订单
        SpOrderDO mainOrder = new SpOrderDO();
        String mainOrderNo = ShopUtil.buildOrderNo("p", SessionUtil.getSessionUserId());
        mainOrder.setOrderNo(mainOrderNo);
        mainOrder.setTotalPrice(spGoodsDO.getOriginalPrice().multiply(new BigDecimal(goodsCount)));
        mainOrder.setPayPrice(spGoodsDO.getSalePrice().multiply(new BigDecimal(goodsCount)));
        mainOrder.setOrderUserId(Long.valueOf(SessionUtil.getSessionUserId()));
        //支付状态 1:待支付 2:已支付
        mainOrder.setPayStatus(SpConstants.OrderStatusEnum.WAIT_PAY.getCode());
        //物流状态 3:待发货 4:已发货
        mainOrder.setLogisticsStatus(SpConstants.OrderStatusEnum.WAIT_DELIVERY.getCode());
        //状态 1：待支付 2:已支付 3:待发货 4:已发货 5:已确认收货
        mainOrder.setStatus(SpConstants.OrderStatusEnum.WAIT_PAY.getCode());
        mainOrder.setOrderTime(new Date());
        mainOrder.setCreateTime(new Date());
        mainOrder.setUpdateTime(new Date());
        spOrderService.insert(mainOrder);

        // 各店铺订单
        SpOrderDO subOrder = new SpOrderDO();
        String subOrderNo = ShopUtil.buildOrderNo("s", SessionUtil.getSessionUserId());
        subOrder.setOrderNo(subOrderNo);
        subOrder.setParentOrderNo(mainOrder.getOrderNo());
        subOrder.setMerchantId(spGoodsDO.getMerchantId());
        //subOrder.setGoodsId(goodsId);
        subOrder.setTotalPrice(spGoodsDO.getOriginalPrice().multiply(new BigDecimal(goodsCount)));
        subOrder.setPayPrice(spGoodsDO.getSalePrice().multiply(new BigDecimal(goodsCount)));
        subOrder.setOrderUserId(Long.valueOf(SessionUtil.getSessionUserId()));
        //支付状态 1:待支付 2:已支付
        subOrder.setPayStatus(SpConstants.OrderStatusEnum.WAIT_PAY.getCode());
        //物流状态 3:待发货 4:已发货
        subOrder.setLogisticsStatus(SpConstants.OrderStatusEnum.WAIT_DELIVERY.getCode());
        //状态 1：待支付 2:已支付 3:待发货 4:已发货 5:已确认收货
        subOrder.setStatus(SpConstants.OrderStatusEnum.WAIT_PAY.getCode());
        subOrder.setOrderTime(new Date());
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

        // 更新商品库存和销量
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
        List<String> goodsIdList = new ArrayList<>(Arrays.asList(goodsIdArr));

        List<SpShoppingCartDTO> spShoppingCartDTOList = spShoppingCartService.queryShoppingCartListByGoodsIds(SessionUtil.getSessionUserId(), goodsIdList);
        if (null == spShoppingCartDTOList || spShoppingCartDTOList.size() == 0) {
            return Result.fail("请先选择要购买的商品");
        }

        // 计算订单总价格
        BigDecimal totalOriginalPrice = new BigDecimal(0);
        BigDecimal totalPayPrice = new BigDecimal(0);
        for (SpShoppingCartDTO spShoppingCartDTO : spShoppingCartDTOList) {
            BigDecimal tmpOriginalPrice = spShoppingCartDTO.getOriginalPrice().multiply(new BigDecimal(spShoppingCartDTO.getGoodsCount()));
            BigDecimal tmpSalePrice = spShoppingCartDTO.getSalePrice().multiply(new BigDecimal(spShoppingCartDTO.getGoodsCount()));

            totalOriginalPrice = totalOriginalPrice.add(tmpOriginalPrice);
            totalPayPrice = totalPayPrice.add(tmpSalePrice);
        }

        // 主订单
        SpOrderDO mainOrder = new SpOrderDO();
        String mainOrderNo = ShopUtil.buildOrderNo("p", SessionUtil.getSessionUserId());
        mainOrder.setOrderNo(mainOrderNo);
        mainOrder.setTotalPrice(totalOriginalPrice);
        mainOrder.setPayPrice(totalPayPrice);
        mainOrder.setOrderUserId(Long.valueOf(SessionUtil.getSessionUserId()));
        //支付状态 1:待支付 2:已支付
        mainOrder.setPayStatus(SpConstants.OrderStatusEnum.WAIT_PAY.getCode());
        //物流状态 3:待发货 4:已发货
        mainOrder.setLogisticsStatus(SpConstants.OrderStatusEnum.WAIT_DELIVERY.getCode());
        //状态 1：待支付 2:已支付 3:待发货 4:已发货 5:已确认收货
        mainOrder.setStatus(SpConstants.OrderStatusEnum.WAIT_PAY.getCode());
        mainOrder.setOrderTime(new Date());
        mainOrder.setCreateTime(new Date());
        mainOrder.setUpdateTime(new Date());
        spOrderService.insert(mainOrder);

        //找出涉及到的店铺id
        List<String> merchantIdList = spShoppingCartDTOList.stream().map(SpShoppingCartDTO::getMerchantId).distinct().collect(Collectors.toList());

        // 为各店铺生成子订单
        for (String merchantId : merchantIdList) {
            // 店铺涉及到的商品列表
            List<SpShoppingCartDTO> subSpShoppingCartDTOList = spShoppingCartDTOList.stream().filter(cart->cart.getMerchantId().equals(merchantId)).collect(Collectors.toList());

            //计算各店铺的商品总价格
            BigDecimal tmpOriginalPrice = new BigDecimal(0);
            BigDecimal tmpSalePrice = new BigDecimal(0);
            for (SpShoppingCartDTO subCart : subSpShoppingCartDTOList) {
                tmpOriginalPrice = tmpOriginalPrice.add(subCart.getOriginalPrice().multiply(new BigDecimal(subCart.getGoodsCount())));
                tmpSalePrice =tmpSalePrice.add(subCart.getSalePrice().multiply(new BigDecimal(subCart.getGoodsCount())));
            }

            SpOrderDO subOrder = new SpOrderDO();
            String subOrderNo = ShopUtil.buildOrderNo("s", SessionUtil.getSessionUserId());
            subOrder.setOrderNo(subOrderNo);
            subOrder.setParentOrderNo(mainOrder.getOrderNo());
            subOrder.setMerchantId(merchantId);
            subOrder.setTotalPrice(tmpOriginalPrice);
            subOrder.setPayPrice(tmpSalePrice);
            subOrder.setOrderUserId(Long.valueOf(SessionUtil.getSessionUserId()));
            //支付状态 1:待支付 2:已支付
            subOrder.setPayStatus(SpConstants.OrderStatusEnum.WAIT_PAY.getCode());
            //物流状态 3:待发货 4:已发货
            subOrder.setLogisticsStatus(SpConstants.OrderStatusEnum.WAIT_DELIVERY.getCode());
            //状态 1：待支付 2:已支付 3:待发货 4:已发货 5:已确认收货
            subOrder.setStatus(SpConstants.OrderStatusEnum.WAIT_PAY.getCode());
            subOrder.setOrderTime(new Date());
            subOrder.setCreateTime(new Date());
            subOrder.setUpdateTime(new Date());
            spOrderService.insert(subOrder);

            // 各店铺订单商品详情
            List<SpOrderDetailDO> insertOrderDetailList = new ArrayList<>();
            for (SpShoppingCartDTO subCart : subSpShoppingCartDTOList) {
                SpOrderDetailDO spOrderDetailDO = new SpOrderDetailDO();
                spOrderDetailDO.setOrderNo(subOrder.getOrderNo());
                spOrderDetailDO.setGoodsId(subCart.getGoodsId());
                spOrderDetailDO.setGoodsCount(subCart.getGoodsCount());
                spOrderDetailDO.setGoodsOriginalPrice(subCart.getOriginalPrice());
                spOrderDetailDO.setGoodsSalePrice(subCart.getSalePrice());
                spOrderDetailDO.setMerchantId(merchantId);
                spOrderDetailDO.setCreateTime(new Date());
                spOrderDetailDO.setUpdateTime(new Date());
                insertOrderDetailList.add(spOrderDetailDO);
            }
            spOrderDetailService.insertBatch(insertOrderDetailList);
        }

        // 删除购物车商品
        spShoppingCartService.delete(new EntityWrapper<SpShoppingCartDO>().in("goods_id", goodsIdList));

        // 更新商品库存和销量
        List<SpGoodsDO> updateGoodsList = new ArrayList<>();
        for (SpShoppingCartDTO spShoppingCartDO : spShoppingCartDTOList) {
            SpGoodsDO updateGoods = new SpGoodsDO();
            BeanUtils.copyProperties(spShoppingCartDO, updateGoods);
            updateGoods.setStockNum(updateGoods.getStockNum() - spShoppingCartDO.getGoodsCount());
            updateGoods.setSaleNumber(updateGoods.getSaleNumber() + spShoppingCartDO.getGoodsCount());
            updateGoods.setUpdateTime(new Date());
            updateGoodsList.add(updateGoods);
        }
        spGoodsService.updateBatchById(updateGoodsList);

        return Result.ok(mainOrder.getOrderNo());
    }

    /**
     * 支付页面.
     * @param orderNo 主订单号
     * @return
     */
    @RequestMapping("/spShoppingPay.page")
    public String spShoppingPay(Model model, String orderNo){
        model.addAttribute("orderNo", orderNo);
        return "shop/spShoppingCenter/spShoppingPay";
    }

    /**
     * 支付页面订单详情.
     * @param parentOrderNo 主订单号
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

        List<SpOrderDO> subOrderList = spOrderService.selectList(new EntityWrapper<SpOrderDO>().eq("parent_order_no", mainOrder.getOrderNo()));
        List<SpSubOrderDTO> subOrderListReturnList = new ArrayList<>();
        for (SpOrderDO subOrder : subOrderList) {
            SpSubOrderDTO spSubOrderDTO = new SpSubOrderDTO();
            List<SpOrderDetailDTO> spOrderDetailList = spOrderDetailService.queryOrderDetailListByOrderNo(subOrder.getOrderNo());
            subOrder.setMerchantName(spOrderDetailList.get(0).getMerchantName());
            spSubOrderDTO.setSubOrder(subOrder);
            spSubOrderDTO.setSubOrderGoodsList(spOrderDetailList);
            subOrderListReturnList.add(spSubOrderDTO);
        }
        spOrderDTO.setSubOrderList(subOrderListReturnList);
        return Result.ok(spOrderDTO);
    }

    /**
     * 订单支付.
     * @param parentOrderNo 主订单号
     * @param payWay        支付方式
     * @param payPrice      支付价格
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
        if (mainOrder.getCancelStatus().intValue() == SpConstants.OrderStatusEnum.CANCEL_OK.getCode() || mainOrder.getStatus().intValue() == SpConstants.OrderStatusEnum.CANCEL_OK.getCode()) {
            return Result.fail("订单已取消,无法进行支付。");
        }

        // 检查是否已经支付  状态 1：待支付 2:已支付 3:待发货 4:已发货 6:已取消 9:已确认收货
        if (mainOrder.getPayStatus().intValue() != SpConstants.OrderStatusEnum.WAIT_PAY.getCode() || mainOrder.getStatus().intValue() != SpConstants.OrderStatusEnum.WAIT_PAY.getCode()) {
            return Result.fail("无需重复支付。");
        }

        // 主订单
        //已支付
        mainOrder.setPayStatus(SpConstants.OrderStatusEnum.PAY_OK.getCode());
        //支付方式
        mainOrder.setPayWay(payWay);
        //待发货
        mainOrder.setLogisticsStatus(SpConstants.OrderStatusEnum.WAIT_DELIVERY.getCode());
        //待发货
        mainOrder.setStatus(SpConstants.OrderStatusEnum.WAIT_DELIVERY.getCode());
        mainOrder.setPayTime(new Date());
        mainOrder.setUpdateTime(new Date());
        //支付人
        mainOrder.setPayUserId(Long.valueOf(SessionUtil.getSessionUserId()));
        //支付价格
        mainOrder.setPayPrice(new BigDecimal(payPrice));
        spOrderService.updateById(mainOrder);

        // 子订单
        List<SpOrderDO> subOrderList = spOrderService.selectList(new EntityWrapper<SpOrderDO>()
                .eq("parent_order_no", parentOrderNo).eq("status", SpConstants.OrderStatusEnum.WAIT_PAY.getCode()));
        for (SpOrderDO subOrderDO : subOrderList) {
            //已支付
            subOrderDO.setPayStatus(SpConstants.OrderStatusEnum.PAY_OK.getCode());
            //支付方式
            subOrderDO.setPayWay(payWay);
            //待发货
            subOrderDO.setLogisticsStatus(SpConstants.OrderStatusEnum.WAIT_DELIVERY.getCode());
            //待发货
            subOrderDO.setStatus(SpConstants.OrderStatusEnum.WAIT_DELIVERY.getCode());
            subOrderDO.setPayTime(new Date());
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
     * @param status   订单状态
     * @param orderNo  主订单号
     * @return
     */
    @GetMapping("/queryMyOrderList")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Result<List<SpOrderDTO>> queryMyOrderList(String status, String orderNo){

        List<SpOrderDTO> resultList = new ArrayList<>();
        Wrapper<SpOrderDO> wrapper = new EntityWrapper<SpOrderDO>().eq("order_user_id", SessionUtil.getSessionUserId()).isNull("parent_order_no");
        if (StringUtils.isNotBlank(status)) {
            wrapper.eq("status", status);
        }
        if (StringUtils.isNotBlank(orderNo)) {
            wrapper.eq("order_no", orderNo);
        }
        wrapper.orderBy("create_time", false);
        List<SpOrderDO> mainOrderList = spOrderService.selectList(wrapper);
        for (SpOrderDO mainOrder : mainOrderList) {
            SpOrderDTO spOrderDTO = new SpOrderDTO();
            spOrderDTO.setMainOrder(mainOrder);

            List<SpOrderDO> subOrderList = spOrderService.selectList(new EntityWrapper<SpOrderDO>().eq("parent_order_no", mainOrder.getOrderNo()));
            List<SpSubOrderDTO> subOrderListReturnList = new ArrayList<>();
            for (SpOrderDO subOrder : subOrderList) {
                SpSubOrderDTO spSubOrderDTO = new SpSubOrderDTO();
                List<SpOrderDetailDTO> spOrderDetailList = spOrderDetailService.queryOrderDetailListByOrderNo(subOrder.getOrderNo());
                subOrder.setMerchantName(spOrderDetailList.get(0).getMerchantName());
                spSubOrderDTO.setSubOrder(subOrder);
                spSubOrderDTO.setSubOrderGoodsList(spOrderDetailList);
                subOrderListReturnList.add(spSubOrderDTO);
            }
            spOrderDTO.setSubOrderList(subOrderListReturnList);

            resultList.add(spOrderDTO);
        }
        return Result.ok(resultList);
    }

    /**
     * 取消订单.
     * @param parentOrderNo 主订单号
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

        // 订单已是取消状态
        if (mainOrder.getStatus().intValue() == SpConstants.OrderStatusEnum.CANCEL_OK.getCode()) {
            return Result.ok();
        }

        // 订单是已支付状态
        if (mainOrder.getPayStatus().intValue() != SpConstants.OrderStatusEnum.WAIT_PAY.getCode() || mainOrder.getStatus().intValue() != SpConstants.OrderStatusEnum.WAIT_PAY.getCode()) {
            return Result.fail("当前订单已支付,无法取消。");
        }

        // 主订单
        mainOrder.setCancelStatus(SpConstants.OrderStatusEnum.CANCEL_OK.getCode());
        mainOrder.setStatus(SpConstants.OrderStatusEnum.CANCEL_OK.getCode());
        mainOrder.setCancelTime(new Date());
        mainOrder.setUpdateTime(new Date());
        spOrderService.updateById(mainOrder);

        // 子订单
        List<SpOrderDO> subOrderList = spOrderService.selectList(new EntityWrapper<SpOrderDO>()
                .eq("parent_order_no", parentOrderNo));
        for (SpOrderDO subOrderDO : subOrderList) {
            subOrderDO.setCancelStatus(SpConstants.OrderStatusEnum.CANCEL_OK.getCode());
            subOrderDO.setStatus(SpConstants.OrderStatusEnum.CANCEL_OK.getCode());
            subOrderDO.setCancelTime(new Date());
            subOrderDO.setUpdateTime(new Date());
            spOrderService.updateById(subOrderDO);
        }

        //商品对应的库存要增加,销量减少
        List<SpOrderDetailDTO> spOrderDetailList = spOrderDetailService.queryOrderDetailListByParentOrderNo(mainOrder.getOrderNo());
        List<SpGoodsDO> batchUpdateList = new ArrayList<>();
        for (SpOrderDetailDTO spOrderDetailDTO : spOrderDetailList) {
            SpGoodsDO spGoodsDO = spGoodsService.selectOne(new EntityWrapper<SpGoodsDO>().eq("goods_id", spOrderDetailDTO.getGoodsId()));
            if (null != spGoodsDO) {
                spGoodsDO.setStockNum(spGoodsDO.getStockNum() + spOrderDetailDTO.getGoodsCount());
                spGoodsDO.setSaleNumber(spGoodsDO.getSaleNumber() - spOrderDetailDTO.getGoodsCount());
                batchUpdateList.add(spGoodsDO);
            }
        }
        if (batchUpdateList.size() > 0) {
            spGoodsService.updateBatchById(batchUpdateList);
        }
        return Result.ok();
    }

    /**
     * 商家订单页面.
     * @return
     */
    @RequestMapping("/spMerchantOrder.page")
    @Transactional(rollbackFor = Exception.class)
    public String spMerchantOrder() {
        return "shop/spShoppingCenter/spMerchantOrder";
    }

    /**
     * 商家订单列表.
     * @param status   订单状态
     * @param orderNo  次订单号
     * @return
     */
    @GetMapping("/queryMerchantOrderList")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Result<List<SpSubOrderDTO>> queryMerchantOrderList(String status, String orderNo){
        List<SpSubOrderDTO> resultList = new ArrayList<>();
        SpMerchantDO spMerchantDO = spMerchantService.selectOne(new EntityWrapper<SpMerchantDO>().eq("user_id", SessionUtil.getSessionUserId()));
        // 该用户没有店铺
        if (spMerchantDO == null) {
            return Result.fail();
        }
        // 店铺已关闭
        if (spMerchantDO.getStatus().intValue() == 0) {
            return Result.fail();
        }
        Wrapper<SpOrderDO> wrapper = new EntityWrapper<SpOrderDO>().eq("merchant_id", spMerchantDO.getMerchantId());
        if (StringUtils.isNotBlank(status)) {
            wrapper.eq("status", status);
        }
        if (StringUtils.isNotBlank(orderNo)) {
            wrapper.eq("order_no", orderNo);
        }
        wrapper.orderBy("create_time", false);
        List<SpOrderDO> subOrderList = spOrderService.selectList(wrapper);
        for (SpOrderDO subOrder : subOrderList) {
            SpSubOrderDTO spSubOrderDTO = new SpSubOrderDTO();
            List<SpOrderDetailDTO> spOrderDetailList = spOrderDetailService.queryOrderDetailListByOrderNo(subOrder.getOrderNo());
            subOrder.setMerchantName(spOrderDetailList.get(0).getMerchantName());
            spSubOrderDTO.setSubOrder(subOrder);
            spSubOrderDTO.setSubOrderGoodsList(spOrderDetailList);
            resultList.add(spSubOrderDTO);
        }
        return Result.ok(resultList);
    }

    /**
     * 商家发货
     * @param orderNo 次订单号
     * @return
     */
    @PostMapping("/deliverOrder")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deliverOrder(String orderNo) {

        SpMerchantDO spMerchantDO = spMerchantService.selectOne(new EntityWrapper<SpMerchantDO>().eq("user_id", SessionUtil.getSessionUserId()));
        // 该用户没有店铺
        if (spMerchantDO == null) {
            return Result.fail("该用户尚无店铺");
        }

        SpOrderDO spOrderDO = spOrderService.selectOne(new EntityWrapper<SpOrderDO>().eq("order_no", orderNo).eq("merchant_id", spMerchantDO.getMerchantId()));
        if (null == spOrderDO) {
            return Result.fail("订单不存在");
        }

        //当前订单不是待发货状态
        if (spOrderDO.getStatus().intValue() != SpConstants.OrderStatusEnum.WAIT_DELIVERY.getCode()) {
            return Result.fail("订单状态不正确");
        }

        // 订单状态改为已发货
        spOrderDO.setStatus(SpConstants.OrderStatusEnum.DELIVERY_OK.getCode());
        spOrderDO.setDeliverTime(new Date());
        spOrderService.updateById(spOrderDO);

        return Result.ok();
    }

    /**
     * 买家确认收货
     * @param orderNo 次订单号
     * @return
     */
    @PostMapping("/confirmOrder")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Result<String> confirmOrder(String orderNo) {
        SpOrderDO spOrderDO = spOrderService.selectOne(new EntityWrapper<SpOrderDO>().eq("order_no", orderNo).eq("order_user_id", SessionUtil.getSessionUserId()));
        if (null == spOrderDO) {
            return Result.fail("订单不存在");
        }

        if (StringUtils.isBlank(spOrderDO.getParentOrderNo())) {
            return Result.fail("订单不存在");
        }
        //当前订单不是已发货状态
        if (spOrderDO.getStatus().intValue() != SpConstants.OrderStatusEnum.DELIVERY_OK.getCode()) {
            return Result.fail("订单状态不正确");
        }

        // 订单状态改为已确认收货
        spOrderDO.setStatus(SpConstants.OrderStatusEnum.CONFIRM_OK.getCode());
        spOrderDO.setConfirmTime(new Date());
        spOrderService.updateById(spOrderDO);

        return Result.ok();
    }
}
