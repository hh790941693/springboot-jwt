package com.pjb.springbootjwt.shop.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.pjb.springbootjwt.shop.ShopUtil;
import com.pjb.springbootjwt.shop.domain.SpGoodsDO;
import com.pjb.springbootjwt.shop.service.SpGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.shop.domain.SpGoodsTypeDO;
import com.pjb.springbootjwt.shop.service.SpGoodsTypeService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 商品分类表.
 */
@Controller
@RequestMapping("/shop/spGoodsType")
public class SpGoodsTypeController extends AdminBaseController {

    private static final Logger logger = LoggerFactory.getLogger(SpGoodsTypeController.class);

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
	private SpGoodsTypeService spGoodsTypeService;

	@Autowired
	private SpGoodsService spGoodsService;

    /**
    * 跳转到商品分类表页面.
	*/
	@GetMapping()
	//@RequiresPermissions("shop:spGoodsType:spGoodsType")
    public String spGoodsType(){
	    return "shop/spGoodsType/spGoodsType";
	}

    /**
     * 获取商品分类表列表数据.
     */
	@ResponseBody
	@GetMapping("/list")
	//@RequiresPermissions("shop:spGoodsType:spGoodsType")
	public Result<Page<SpGoodsTypeDO>> list(SpGoodsTypeDO spGoodsTypeDto) {
        Wrapper<SpGoodsTypeDO> wrapper = new EntityWrapper<SpGoodsTypeDO>(spGoodsTypeDto);
        Page<SpGoodsTypeDO> page = spGoodsTypeService.selectPage(getPage(SpGoodsTypeDO.class), wrapper);
        return Result.ok(page);
	}

    /**
     * 跳转到商品分类表添加页面.
     */
	@GetMapping("/add")
	//@RequiresPermissions("shop:spGoodsType:add")
    public String add(Model model) {
		SpGoodsTypeDO spGoodsType = new SpGoodsTypeDO();
        model.addAttribute("spGoodsType", spGoodsType);
	    return "shop/spGoodsType/spGoodsTypeForm";
	}

    /**
     * 跳转到商品分类表编辑页面.
     * @param id 商品分类表ID
     * @param model 商品分类表实体
     */
	@GetMapping("/edit/{id}")
	//@RequiresPermissions("shop:spGoodsType:edit")
    public String edit(@PathVariable("id") Long id, Model model) {
		SpGoodsTypeDO spGoodsType = spGoodsTypeService.selectById(id);
		model.addAttribute("spGoodsType", spGoodsType);
	    return "shop/spGoodsType/spGoodsTypeForm";
	}
	
	/**
	 * 保存商品分类表.
	 */
	@ResponseBody
	@PostMapping("/save")
	@Transactional(rollbackFor = Exception.class)
	//@RequiresPermissions("shop:spGoodsType:add")
	public Result<String> save(SpGoodsTypeDO spGoodsType) {
		String goodsTypeId = ShopUtil.buildObjectId("gt_");
		spGoodsType.setTypeId(goodsTypeId);
		spGoodsType.setCreateTime(new Date());
		spGoodsType.setUpdateTime(new Date());
		spGoodsTypeService.insert(spGoodsType);
        return Result.ok();
	}

	/**
	 * 编辑商品分类表.
	 */
	@ResponseBody
	@RequestMapping("/update")
	//@RequiresPermissions("shop:spGoodsType:edit")
	public Result<String> update(SpGoodsTypeDO spGoodsType) {
		spGoodsTypeService.updateById(spGoodsType);
		return Result.ok();
	}
	
	/**
	 * 删除商品分类表.
	 */
	@PostMapping("/remove")
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	//@RequiresPermissions("shop:spGoodsType:remove")
	public Result<String> remove(Long id) {
		SpGoodsTypeDO spGoodsTypeDO = spGoodsTypeService.selectById(id);
		if (null == spGoodsTypeDO) {
			return Result.build(Result.CODE_FAIL, "记录不存在");
		}
		int goodsCnt = spGoodsService.selectCount(new EntityWrapper<SpGoodsDO>().eq("goods_type_id", spGoodsTypeDO.getTypeId()).eq("status", 1));
		if (goodsCnt > 0) {
			return Result.build(Result.CODE_FAIL, "尚有已上架的商品使用了该分类，请先下架对应商品后再进行操作！");
		}

		spGoodsTypeDO.setStatus(0);
		spGoodsTypeService.updateById(spGoodsTypeDO);
		return Result.ok();
	}
	
	/**
	 * 批量删除商品分类表.
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	//@RequiresPermissions("shop:spGoodsType:batchRemove")
	public Result<String> remove(@RequestParam("ids[]") Long[] ids) {
		spGoodsTypeService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}
}