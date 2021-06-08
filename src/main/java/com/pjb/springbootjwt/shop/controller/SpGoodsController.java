package com.pjb.springbootjwt.shop.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.pjb.springbootjwt.shop.domain.SpGoodsDO;
import com.pjb.springbootjwt.shop.service.SpGoodsService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 商品表.
 */
@Controller
@RequestMapping("/shop/spGoods")
public class SpGoodsController extends AdminBaseController {

    private static final Logger logger = LoggerFactory.getLogger(SpGoodsController.class);

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
	private SpGoodsService spGoodsService;

    /**
    * 跳转到商品表页面.
	*/
	@GetMapping()
	//@RequiresPermissions("shop:spGoods:spGoods")
    public String spGoods(){
	    return "shop/spGoods/spGoods";
	}

    /**
     * 获取商品表列表数据.
     */
	@ResponseBody
	@GetMapping("/list")
	//@RequiresPermissions("shop:spGoods:spGoods")
	public Result<Page<SpGoodsDO>> list(SpGoodsDO spGoodsDto) {
        Wrapper<SpGoodsDO> wrapper = new EntityWrapper<SpGoodsDO>(spGoodsDto);
        Page<SpGoodsDO> page = spGoodsService.selectPage(getPage(SpGoodsDO.class), wrapper);
        return Result.ok(page);
	}

    /**
     * 跳转到商品表添加页面.
     */
	@GetMapping("/add")
	//@RequiresPermissions("shop:spGoods:add")
    public String add(Model model) {
		SpGoodsDO spGoods = new SpGoodsDO();
        model.addAttribute("spGoods", spGoods);
	    return "shop/spGoods/spGoodsForm";
	}

    /**
     * 跳转到商品表编辑页面.
     * @param id 商品表ID
     * @param model 商品表实体
     */
	@GetMapping("/edit/{id}")
	//@RequiresPermissions("shop:spGoods:edit")
    public String edit(@PathVariable("id") Long id, Model model) {
		SpGoodsDO spGoods = spGoodsService.selectById(id);
		model.addAttribute("spGoods", spGoods);
	    return "shop/spGoods/spGoodsForm";
	}
	
	/**
	 * 保存商品表.
	 */
	@ResponseBody
	@PostMapping("/save")
	//@RequiresPermissions("shop:spGoods:add")
	public Result<String> save(SpGoodsDO spGoods) {
		spGoodsService.insert(spGoods);
        return Result.ok();
	}

	/**
	 * 编辑商品表.
	 */
	@ResponseBody
	@RequestMapping("/update")
	//@RequiresPermissions("shop:spGoods:edit")
	public Result<String> update(SpGoodsDO spGoods) {
		spGoodsService.updateById(spGoods);
		return Result.ok();
	}
	
	/**
	 * 删除商品表.
	 */
	@PostMapping("/remove")
	@ResponseBody
	//@RequiresPermissions("shop:spGoods:remove")
	public Result<String> remove(Long id) {
		spGoodsService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 批量删除商品表.
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	//@RequiresPermissions("shop:spGoods:batchRemove")
	public Result<String> remove(@RequestParam("ids[]") Long[] ids) {
		spGoodsService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}
}