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
import com.pjb.springbootjwt.shop.domain.SpOrderDO;
import com.pjb.springbootjwt.shop.service.SpOrderService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 订单表.
 */
@Controller
@RequestMapping("/shop/spOrder")
public class SpOrderController extends AdminBaseController {

    private static final Logger logger = LoggerFactory.getLogger(SpOrderController.class);

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
	private SpOrderService spOrderService;

    /**
    * 跳转到订单表页面.
	*/
	@GetMapping()
	//@RequiresPermissions("shop:spOrder:spOrder")
    public String spOrder(){
	    return "shop/spOrder/spOrder";
	}

    /**
     * 获取订单表列表数据.
     */
	@ResponseBody
	@GetMapping("/list")
	//@RequiresPermissions("shop:spOrder:spOrder")
	public Result<Page<SpOrderDO>> list(SpOrderDO spOrderDto) {
        Wrapper<SpOrderDO> wrapper = new EntityWrapper<SpOrderDO>(spOrderDto);
        Page<SpOrderDO> page = spOrderService.selectPage(getPage(SpOrderDO.class), wrapper);
        return Result.ok(page);
	}

    /**
     * 跳转到订单表添加页面.
     */
	@GetMapping("/add")
	//@RequiresPermissions("shop:spOrder:add")
    public String add(Model model) {
		SpOrderDO spOrder = new SpOrderDO();
        model.addAttribute("spOrder", spOrder);
	    return "shop/spOrder/spOrderForm";
	}

    /**
     * 跳转到订单表编辑页面.
     * @param id 订单表ID
     * @param model 订单表实体
     */
	@GetMapping("/edit/{id}")
	//@RequiresPermissions("shop:spOrder:edit")
    public String edit(@PathVariable("id") Long id, Model model) {
		SpOrderDO spOrder = spOrderService.selectById(id);
		model.addAttribute("spOrder", spOrder);
	    return "shop/spOrder/spOrderForm";
	}
	
	/**
	 * 保存订单表.
	 */
	@ResponseBody
	@PostMapping("/save")
	//@RequiresPermissions("shop:spOrder:add")
	public Result<String> save(SpOrderDO spOrder) {
		spOrderService.insert(spOrder);
        return Result.ok();
	}

	/**
	 * 编辑订单表.
	 */
	@ResponseBody
	@RequestMapping("/update")
	//@RequiresPermissions("shop:spOrder:edit")
	public Result<String> update(SpOrderDO spOrder) {
		spOrderService.updateById(spOrder);
		return Result.ok();
	}
	
	/**
	 * 删除订单表.
	 */
	@PostMapping("/remove")
	@ResponseBody
	//@RequiresPermissions("shop:spOrder:remove")
	public Result<String> remove(Long id) {
		spOrderService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 批量删除订单表.
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	//@RequiresPermissions("shop:spOrder:batchRemove")
	public Result<String> remove(@RequestParam("ids[]") Long[] ids) {
		spOrderService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}
}