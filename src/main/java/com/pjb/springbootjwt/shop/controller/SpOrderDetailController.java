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
import com.pjb.springbootjwt.shop.domain.SpOrderDetailDO;
import com.pjb.springbootjwt.shop.service.SpOrderDetailService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 订单详情表.
 */
@Controller
@RequestMapping("/shop/spOrderDetail")
public class SpOrderDetailController extends AdminBaseController {

    private static final Logger logger = LoggerFactory.getLogger(SpOrderDetailController.class);

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
	private SpOrderDetailService spOrderDetailService;

    /**
    * 跳转到订单详情表页面.
	*/
	@GetMapping()
	//@RequiresPermissions("shop:spOrderDetail:spOrderDetail")
    public String spOrderDetail(){
	    return "shop/spOrderDetail/spOrderDetail";
	}

    /**
     * 获取订单详情表列表数据.
     */
	@ResponseBody
	@GetMapping("/list")
	//@RequiresPermissions("shop:spOrderDetail:spOrderDetail")
	public Result<Page<SpOrderDetailDO>> list(SpOrderDetailDO spOrderDetailDto) {
        Wrapper<SpOrderDetailDO> wrapper = new EntityWrapper<SpOrderDetailDO>(spOrderDetailDto);
        Page<SpOrderDetailDO> page = spOrderDetailService.selectPage(getPage(SpOrderDetailDO.class), wrapper);
        return Result.ok(page);
	}

    /**
     * 跳转到订单详情表添加页面.
     */
	@GetMapping("/add")
	//@RequiresPermissions("shop:spOrderDetail:add")
    public String add(Model model) {
		SpOrderDetailDO spOrderDetail = new SpOrderDetailDO();
        model.addAttribute("spOrderDetail", spOrderDetail);
	    return "shop/spOrderDetail/spOrderDetailForm";
	}

    /**
     * 跳转到订单详情表编辑页面.
     * @param id 订单详情表ID
     * @param model 订单详情表实体
     */
	@GetMapping("/edit/{id}")
	//@RequiresPermissions("shop:spOrderDetail:edit")
    public String edit(@PathVariable("id") Long id, Model model) {
		SpOrderDetailDO spOrderDetail = spOrderDetailService.selectById(id);
		model.addAttribute("spOrderDetail", spOrderDetail);
	    return "shop/spOrderDetail/spOrderDetailForm";
	}
	
	/**
	 * 保存订单详情表.
	 */
	@ResponseBody
	@PostMapping("/save")
	//@RequiresPermissions("shop:spOrderDetail:add")
	public Result<String> save(SpOrderDetailDO spOrderDetail) {
		spOrderDetailService.insert(spOrderDetail);
        return Result.ok();
	}

	/**
	 * 编辑订单详情表.
	 */
	@ResponseBody
	@RequestMapping("/update")
	//@RequiresPermissions("shop:spOrderDetail:edit")
	public Result<String> update(SpOrderDetailDO spOrderDetail) {
		spOrderDetailService.updateById(spOrderDetail);
		return Result.ok();
	}
	
	/**
	 * 删除订单详情表.
	 */
	@PostMapping("/remove")
	@ResponseBody
	//@RequiresPermissions("shop:spOrderDetail:remove")
	public Result<String> remove(Long id) {
		spOrderDetailService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 批量删除订单详情表.
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	//@RequiresPermissions("shop:spOrderDetail:batchRemove")
	public Result<String> remove(@RequestParam("ids[]") Long[] ids) {
		spOrderDetailService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}
}