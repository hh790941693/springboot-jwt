package com.pjb.springbootjwt.shop.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.pjb.springbootjwt.shop.domain.SpGoodsDO;
import com.pjb.springbootjwt.shop.domain.SpMerchantDO;
import com.pjb.springbootjwt.shop.service.SpGoodsService;
import com.pjb.springbootjwt.shop.service.SpMerchantService;
import com.pjb.springbootjwt.zhddkk.util.SessionUtil;
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
import com.pjb.springbootjwt.shop.domain.SpFavoriteDO;
import com.pjb.springbootjwt.shop.service.SpFavoriteService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 收藏表，包括店铺、商品等。.
 */
@Controller
@RequestMapping("/shop/spFavorite")
public class SpFavoriteController extends AdminBaseController {

    private static final Logger logger = LoggerFactory.getLogger(SpFavoriteController.class);

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
	private SpFavoriteService spFavoriteService;

	@Autowired
	private SpMerchantService spMerchantService;

	@Autowired
	private SpGoodsService spGoodsService;

    /**
    * 跳转到收藏表，包括店铺、商品等。页面.
	*/
	@GetMapping()
	//@RequiresPermissions("shop:spFavorite:spFavorite")
    public String spFavorite(){
	    return "shop/spFavorite/spFavorite";
	}

    /**
     * 获取收藏表，包括店铺、商品等。列表数据.
     */
	@ResponseBody
	@GetMapping("/list")
	//@RequiresPermissions("shop:spFavorite:spFavorite")
	public Result<Page<SpFavoriteDO>> list(SpFavoriteDO spFavoriteDto) {
        Wrapper<SpFavoriteDO> wrapper = new EntityWrapper<SpFavoriteDO>(spFavoriteDto);
        Page<SpFavoriteDO> page = spFavoriteService.selectPage(getPage(SpFavoriteDO.class), wrapper);
        return Result.ok(page);
	}

    /**
     * 跳转到收藏表，包括店铺、商品等。添加页面.
     */
	@GetMapping("/add")
	//@RequiresPermissions("shop:spFavorite:add")
    public String add(Model model) {
		SpFavoriteDO spFavorite = new SpFavoriteDO();
        model.addAttribute("spFavorite", spFavorite);
	    return "shop/spFavorite/spFavoriteForm";
	}

    /**
     * 跳转到收藏表，包括店铺、商品等。编辑页面.
     * @param id 收藏表，包括店铺、商品等。ID
     * @param model 收藏表，包括店铺、商品等。实体
     */
	@GetMapping("/edit/{id}")
	//@RequiresPermissions("shop:spFavorite:edit")
    public String edit(@PathVariable("id") Long id, Model model) {
		SpFavoriteDO spFavorite = spFavoriteService.selectById(id);
		model.addAttribute("spFavorite", spFavorite);
	    return "shop/spFavorite/spFavoriteForm";
	}
	
	/**
	 * 保存收藏表，包括店铺、商品等。.
	 */
	@ResponseBody
	@PostMapping("/save")
	//@RequiresPermissions("shop:spFavorite:add")
	public Result<String> save(SpFavoriteDO spFavorite) {
		spFavoriteService.insert(spFavorite);
        return Result.ok();
	}

	/**
	 * 编辑收藏表，包括店铺、商品等。.
	 */
	@ResponseBody
	@RequestMapping("/update")
	//@RequiresPermissions("shop:spFavorite:edit")
	public Result<String> update(SpFavoriteDO spFavorite) {
		spFavoriteService.updateById(spFavorite);
		return Result.ok();
	}
	
	/**
	 * 删除收藏表，包括店铺、商品等。.
	 */
	@PostMapping("/remove")
	@ResponseBody
	//@RequiresPermissions("shop:spFavorite:remove")
	public Result<String> remove(Long id) {
		spFavoriteService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 批量删除收藏表，包括店铺、商品等。.
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	//@RequiresPermissions("shop:spFavorite:batchRemove")
	public Result<String> remove(@RequestParam("ids[]") Long[] ids) {
		spFavoriteService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}

	/**
	 * 收藏/取消收藏 店铺
	 */
	@PostMapping("/favoriteMerchant")
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public Result<String> favoriteMerchant(String merchantId, int status) {
		SpMerchantDO spMerchantDO = spMerchantService.selectOne(new EntityWrapper<SpMerchantDO>().eq("merchant_id", merchantId));
		if (null == spMerchantDO) {
			return Result.fail("店铺不存在");
		}
		SpFavoriteDO spFavoriteDO = spFavoriteService.selectOne(new EntityWrapper<SpFavoriteDO>().eq("user_id", SessionUtil.getSessionUserId())
				.eq("subject_id", merchantId).eq("subject_type", 2));
		if (null != spFavoriteDO) {
			spFavoriteDO.setStatus(status);
			spFavoriteDO.setUpdateTime(new Date());
			spFavoriteService.updateById(spFavoriteDO);
			return Result.ok();
		}

		SpFavoriteDO spFavoriteInsert = new SpFavoriteDO();
		spFavoriteInsert.setUserId(Long.valueOf(SessionUtil.getSessionUserId()));
		spFavoriteInsert.setSubjectId(merchantId);
		spFavoriteInsert.setSubjectType(2);
		spFavoriteInsert.setStatus(1);
		spFavoriteInsert.setCreateTime(new Date());
		spFavoriteInsert.setUpdateTime(new Date());
		spFavoriteService.insert(spFavoriteInsert);
		return Result.ok();
	}

	/**
	 * 收藏/取消收藏 商品
	 */
	@PostMapping("/favoriteGoods")
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public Result<String> favoriteGoods(String goodsId, Integer status) {
		SpGoodsDO spGoodsDO = spGoodsService.selectOne(new EntityWrapper<SpGoodsDO>().eq("goods_id", goodsId));
		if (null == spGoodsDO) {
			return Result.fail("商品不存在");
		}
		SpFavoriteDO spFavoriteDO = spFavoriteService.selectOne(new EntityWrapper<SpFavoriteDO>().eq("user_id", SessionUtil.getSessionUserId())
				.eq("subject_id", goodsId).eq("subject_type", 1));
		if (null != spFavoriteDO) {
			spFavoriteDO.setStatus(status);
			spFavoriteDO.setUpdateTime(new Date());
			spFavoriteService.updateById(spFavoriteDO);
			return Result.ok();
		}

		SpFavoriteDO spFavoriteInsert = new SpFavoriteDO();
		spFavoriteInsert.setUserId(Long.valueOf(SessionUtil.getSessionUserId()));
		spFavoriteInsert.setSubjectId(goodsId);
		spFavoriteInsert.setSubjectType(1);
		spFavoriteInsert.setStatus(1);
		spFavoriteInsert.setCreateTime(new Date());
		spFavoriteInsert.setUpdateTime(new Date());
		spFavoriteService.insert(spFavoriteInsert);
		return Result.ok();
	}
}