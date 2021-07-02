package com.pjb.springbootjwt.shop.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

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
import com.pjb.springbootjwt.zhddkk.util.SessionUtil;
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.shop.domain.SpShoppingCartDO;
import com.pjb.springbootjwt.shop.service.SpShoppingCartService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 */
@Controller
@RequestMapping("/shop/spShoppingCart")
public class SpShoppingCartController extends AdminBaseController {

    private static final Logger logger = LoggerFactory.getLogger(SpShoppingCartController.class);

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
	private SpShoppingCartService spShoppingCartService;

    /**
    * 跳转到页面.
	*/
	@GetMapping()
	//@RequiresPermissions("shop:spShoppingCart:spShoppingCart")
    public String spShoppingCart(){
	    return "shop/spShoppingCart/spShoppingCart";
	}

    /**
     * 获取列表数据.
     */
	@ResponseBody
	@GetMapping("/list")
	//@RequiresPermissions("shop:spShoppingCart:spShoppingCart")
	public Result<Page<SpShoppingCartDO>> list(SpShoppingCartDO spShoppingCartDto) {
        Wrapper<SpShoppingCartDO> wrapper = new EntityWrapper<SpShoppingCartDO>(spShoppingCartDto);
        Page<SpShoppingCartDO> page = spShoppingCartService.selectPage(getPage(SpShoppingCartDO.class), wrapper);
        return Result.ok(page);
	}

    /**
     * 跳转到添加页面.
     */
	@GetMapping("/add")
	//@RequiresPermissions("shop:spShoppingCart:add")
    public String add(Model model) {
		SpShoppingCartDO spShoppingCart = new SpShoppingCartDO();
        model.addAttribute("spShoppingCart", spShoppingCart);
	    return "shop/spShoppingCart/spShoppingCartForm";
	}

    /**
     * 跳转到编辑页面.
     * @param id ID
     * @param model 实体
     */
	@GetMapping("/edit/{id}")
	//@RequiresPermissions("shop:spShoppingCart:edit")
    public String edit(@PathVariable("id") Long id, Model model) {
		SpShoppingCartDO spShoppingCart = spShoppingCartService.selectById(id);
		model.addAttribute("spShoppingCart", spShoppingCart);
	    return "shop/spShoppingCart/spShoppingCartForm";
	}
	
	/**
	 * 保存.
	 */
	@ResponseBody
	@PostMapping("/save")
	//@RequiresPermissions("shop:spShoppingCart:add")
	public Result<String> save(SpShoppingCartDO spShoppingCart) {
		spShoppingCartService.insert(spShoppingCart);
        return Result.ok();
	}

	/**
	 * 编辑.
	 */
	@ResponseBody
	@RequestMapping("/update")
	//@RequiresPermissions("shop:spShoppingCart:edit")
	public Result<String> update(SpShoppingCartDO spShoppingCart) {
		spShoppingCartService.updateById(spShoppingCart);
		return Result.ok();
	}
	
	/**
	 * 删除.
	 */
	@PostMapping("/remove")
	@ResponseBody
	//@RequiresPermissions("shop:spShoppingCart:remove")
	public Result<String> remove(Long id) {
		spShoppingCartService.deleteById(id);
        return Result.ok();
	}

	/**
	 * 删除所有.
	 */
	@PostMapping("/removeAll")
	@ResponseBody
	//@RequiresPermissions("shop:spShoppingCart:removeAll")
	public Result<String> removeAll() {
		spShoppingCartService.delete(new EntityWrapper<SpShoppingCartDO>().eq("user_id", SessionUtil.getSessionUserId()));
		return Result.ok();
	}
	
	/**
	 * 批量删除.
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	//@RequiresPermissions("shop:spShoppingCart:batchRemove")
	public Result<String> remove(@RequestParam("ids[]") Long[] ids) {
		spShoppingCartService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}

	@ResponseBody
	@PostMapping("/addToShoppingCart")
    @Transactional(rollbackFor = Exception.class)
	//@RequiresPermissions("shop:spShoppingCart:spShoppingCart")
	public Result<String> addToShoppingCart(String goodsId, int goodsCount, String merchantId) {
        SpShoppingCartDO spShoppingCartDO = spShoppingCartService.selectOne(new EntityWrapper<SpShoppingCartDO>().eq("user_id", SessionUtil.getSessionUserId())
                .eq("goods_id", goodsId));
        if (null == spShoppingCartDO) {
            spShoppingCartDO = new SpShoppingCartDO();
            spShoppingCartDO.setUserId(Long.valueOf(SessionUtil.getSessionUserId()));
            spShoppingCartDO.setGoodsId(goodsId);
			spShoppingCartDO.setMerchantId(merchantId);
            spShoppingCartDO.setGoodsCount(goodsCount);
            spShoppingCartDO.setCreateTime(new Date());
            spShoppingCartDO.setUpdateTime(new Date());

            spShoppingCartService.insert(spShoppingCartDO);
        } else {
            spShoppingCartDO.setGoodsCount(spShoppingCartDO.getGoodsCount() + goodsCount);
            spShoppingCartDO.setUpdateTime(new Date());
            spShoppingCartService.updateById(spShoppingCartDO);
        }
        return Result.ok();
    }
}