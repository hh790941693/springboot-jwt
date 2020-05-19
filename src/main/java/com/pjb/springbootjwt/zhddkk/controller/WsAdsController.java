package com.pjb.springbootjwt.zhddkk.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import com.baomidou.mybatisplus.enums.SqlLike;
import org.apache.commons.lang.StringUtils;
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
import com.pjb.springbootjwt.zhddkk.domain.WsAdsDO;
import com.pjb.springbootjwt.zhddkk.service.WsAdsService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 广告表
 */
@Controller
@RequestMapping("/zhddkk/wsAds")
public class WsAdsController extends AdminBaseController {

    private static Logger logger = LoggerFactory.getLogger(WsAdsController.class);

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @Autowired
	private WsAdsService wsAdsService;


    /**
    * 跳转到广告表页面
	*/
	@GetMapping()
	String wsAds(){
	    return "zhddkk/wsAds/wsAds";
	}

    /**
     * 获取广告表列表数据
     */
	@ResponseBody
	@GetMapping("/list")
	public Result<Page<WsAdsDO>> list(WsAdsDO wsAdsDTO){
        Wrapper<WsAdsDO> wrapper = new EntityWrapper<WsAdsDO>();
        if (StringUtils.isNotBlank(wsAdsDTO.getTitle())){
        	wrapper.like("title", wsAdsDTO.getTitle(), SqlLike.DEFAULT);
		}
		if (StringUtils.isNotBlank(wsAdsDTO.getContent())){
			wrapper.like("content", wsAdsDTO.getContent(), SqlLike.DEFAULT);
		}
		wrapper.orderBy("create_time", false);
        Page<WsAdsDO> page = wsAdsService.selectPage(getPage(WsAdsDO.class), wrapper);
        return Result.ok(page);
	}

    /**
     * 跳转到广告表添加页面
     */
	@GetMapping("/add")
	String add(Model model){
		WsAdsDO wsAds = new WsAdsDO();
        model.addAttribute("wsAds", wsAds);
	    return "zhddkk/wsAds/wsAdsForm";
	}

    /**
     * 跳转到广告表编辑页面
     * @param id 广告表ID
     * @param model 广告表实体
     */
	@GetMapping("/edit/{id}")
	String edit(@PathVariable("id") Integer id,Model model){
		WsAdsDO wsAds = wsAdsService.selectById(id);
		model.addAttribute("wsAds", wsAds);
	    return "zhddkk/wsAds/wsAdsForm";
	}
	
	/**
	 * 保存广告表
	 */
	@ResponseBody
	@PostMapping("/save")
	public Result<String> save( WsAdsDO wsAds){
		wsAdsService.insert(wsAds);
        return Result.ok();
	}

	/**
	 * 编辑广告表
	 */
	@ResponseBody
	@RequestMapping("/update")
	public Result<String> update( WsAdsDO wsAds){
		wsAdsService.updateById(wsAds);
		return Result.ok();
	}
	
	/**
	 * 删除广告表
	 */
	@PostMapping("/remove")
	@ResponseBody
	public Result<String> remove( Integer id){
		wsAdsService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 批量删除广告表
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	public Result<String> remove(@RequestParam("ids[]") Integer[] ids){
		wsAdsService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}
	
}
