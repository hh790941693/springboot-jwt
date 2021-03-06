package com.pjb.springbootjwt.shop.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.pjb.springbootjwt.shop.constants.SpConstants;
import com.pjb.springbootjwt.shop.domain.SpGoodsDO;
import com.pjb.springbootjwt.shop.domain.SpOrderDO;
import com.pjb.springbootjwt.shop.service.SpGoodsService;
import com.pjb.springbootjwt.shop.service.SpOrderService;
import com.pjb.springbootjwt.zhddkk.util.SessionUtil;
import com.pjb.springbootjwt.zhddkk.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.shop.domain.SpMerchantDO;
import com.pjb.springbootjwt.shop.service.SpMerchantService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 商家店铺表.
 */
@Controller
@RequestMapping("/shop/spMerchant")
public class SpMerchantController extends AdminBaseController {

    private static final Logger logger = LoggerFactory.getLogger(SpMerchantController.class);

	/**
    * binder.
	* @param binder binder
	*/
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @Autowired
	private SpMerchantService spMerchantService;

	@Autowired
	private SpOrderService spOrderService;

	@Autowired
	private SpGoodsService spGoodsService;

    /**
    * 跳转到商家店铺表页面.
	*/
	@GetMapping()
	//@RequiresPermissions("shop:spMerchant:spMerchant")
    public String spMerchant(){
	    return "shop/spMerchant/spMerchant";
	}

    /**
     * 获取商家店铺表列表数据.
     */
	@ResponseBody
	@GetMapping("/list")
	//@RequiresPermissions("shop:spMerchant:spMerchant")
	public Result<Page<SpMerchantDO>> list(SpMerchantDO spMerchantDto) {
        Wrapper<SpMerchantDO> wrapper = new EntityWrapper<SpMerchantDO>(spMerchantDto);
        Page<SpMerchantDO> page = spMerchantService.selectPage(getPage(SpMerchantDO.class), wrapper);
        return Result.ok(page);
	}

    /**
     * 跳转到商家店铺表添加页面.
     */
	@GetMapping("/add")
	//@RequiresPermissions("shop:spMerchant:add")
    public String add(Model model) {
		SpMerchantDO spMerchant = new SpMerchantDO();
        model.addAttribute("spMerchant", spMerchant);
	    return "shop/spMerchant/spMerchantForm";
	}

    /**
     * 跳转到商家店铺表编辑页面.
     * @param id 商家店铺表ID
     * @param model 商家店铺表实体
     */
	@GetMapping("/edit/{id}")
	//@RequiresPermissions("shop:spMerchant:edit")
    public String edit(@PathVariable("id") Long id, Model model) {
		SpMerchantDO spMerchant = spMerchantService.selectById(id);
		model.addAttribute("spMerchant", spMerchant);
	    return "shop/spMerchant/spMerchantForm";
	}
	
	/**
	 * 保存商家店铺表.
	 */
	@ResponseBody
	@PostMapping("/save")
	//@RequiresPermissions("shop:spMerchant:add")
	public Result<String> save(SpMerchantDO spMerchant) {
		spMerchantService.insert(spMerchant);
        return Result.ok();
	}

	/**
	 * 编辑商家店铺表.
	 */
	@ResponseBody
	@RequestMapping("/update")
	//@RequiresPermissions("shop:spMerchant:edit")
	public Result<String> update(SpMerchantDO spMerchant, HttpServletRequest request) {
		if (WebUtil.isRepeatSubmit(request)){
			return Result.fail("请不要重复提交请求");
		}
		spMerchantService.updateById(spMerchant);
		request.getSession(false).removeAttribute(WebUtil.REQUEST_TOKEN);
		return Result.ok();
	}
	
	/**
	 * 删除商家店铺表.
	 */
	@PostMapping("/remove")
	@ResponseBody
	//@RequiresPermissions("shop:spMerchant:remove")
	public Result<String> remove(Long id) {
		spMerchantService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 批量删除商家店铺表.
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	//@RequiresPermissions("shop:spMerchant:batchRemove")
	public Result<String> remove(@RequestParam("ids[]") Long[] ids) {
		spMerchantService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}

	/**
	 * 跳转到我的店铺页面.
	 */
	@GetMapping("/myMerchant")
	//@RequiresPermissions("shop:spMerchant:spMerchant")
	public String myMerchant(Model model, HttpServletRequest request, HttpServletResponse response){
		SpMerchantDO spMerchantDO = spMerchantService.selectOne(new EntityWrapper<SpMerchantDO>().eq("user_id", SessionUtil.getSessionUserId()));

		model.addAttribute("spMerchant", spMerchantDO);
		// 订单个数
		int orderNum = spOrderService.selectCount(new EntityWrapper<SpOrderDO>().eq("merchant_id", spMerchantDO.getMerchantId()));
		// 商品数量
		int goodsNum = spGoodsService.selectCount(new EntityWrapper<SpGoodsDO>().eq("merchant_id", spMerchantDO.getMerchantId()).eq("status", 1));
		model.addAttribute("orderNum", orderNum);
		model.addAttribute("goodsNum", goodsNum);
		String requestToken = WebUtil.generateAccessToken();
		request.getSession(false).setAttribute(WebUtil.REQUEST_TOKEN, requestToken);
		return "shop/spMerchant/myMerchant";
	}
}