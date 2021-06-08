package com.pjb.springbootjwt.shop.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
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
	    return "shop/spGoodsType/spGoodsTypeAdd";
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
	    return "shop/spGoodsType/spGoodsTypeEdit";
	}
	
	/**
	 * 保存商品分类表.
	 */
	@ResponseBody
	@PostMapping("/save")
	@Transactional(rollbackFor = Exception.class)
	//@RequiresPermissions("shop:spGoodsType:add")
	public Result<String> save(SpGoodsTypeDO spGoodsType) {
		String goodsTypeId = "gt_" + UUID.randomUUID().toString().replaceAll("-", "");
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
	//@RequiresPermissions("shop:spGoodsType:remove")
	public Result<String> remove(Long id) {
		spGoodsTypeService.deleteById(id);
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