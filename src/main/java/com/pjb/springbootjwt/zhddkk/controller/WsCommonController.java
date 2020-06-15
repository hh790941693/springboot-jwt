package com.pjb.springbootjwt.zhddkk.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.baomidou.mybatisplus.enums.SqlLike;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.enumx.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.enumx.OperationEnum;
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
import com.pjb.springbootjwt.zhddkk.domain.WsCommonDO;
import com.pjb.springbootjwt.zhddkk.service.WsCommonService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
@Controller
@RequestMapping("/zhddkk/wsCommon")
public class WsCommonController extends AdminBaseController {

    private static Logger logger = LoggerFactory.getLogger(WsCommonController.class);

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @Autowired
	private WsCommonService wsCommonService;


    /**
    * 跳转到页面
	*/
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.CONFIGURATION,subModule="",describe="通用配置页面")
	@GetMapping("{type}")
	//@RequiresPermissions("zhddkk:wsCommon:wsCommon")
	String wsCommon(Model model, @PathVariable("type")String type){
		model.addAttribute("type", type);
	    return "zhddkk/wsCommon/wsCommon";
	}

    /**
     * 获取列表数据
     */
    @OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.CONFIGURATION,subModule="",describe="通用配置列表")
	@ResponseBody
	@GetMapping("/list")
	//@RequiresPermissions("zhddkk:wsCommon:wsCommon")
	public Result<Page<WsCommonDO>> list(WsCommonDO wsCommonDTO){
        Wrapper<WsCommonDO> wrapper = new EntityWrapper<WsCommonDO>();
        if (StringUtils.isNotBlank(wsCommonDTO.getType())){
        	wrapper.eq("type", wsCommonDTO.getType());
		}
        if (StringUtils.isNotBlank(wsCommonDTO.getName())){
			wrapper.like("name", wsCommonDTO.getName(), SqlLike.DEFAULT);
		}
        wrapper.orderBy("orderby");
        Page<WsCommonDO> page = wsCommonService.selectPage(getPage(WsCommonDO.class), wrapper);
        return Result.ok(page);
	}

    /**
     * 跳转到添加页面
     */
    @OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.CONFIGURATION,subModule="",describe="添加配置页面")
	@GetMapping("/add")
	//@RequiresPermissions("zhddkk:wsCommon:add")
	String add(Model model, String type){
		WsCommonDO wsCommon = new WsCommonDO();
		wsCommon.setType(type);
        model.addAttribute("wsCommon", wsCommon);
	    return "zhddkk/wsCommon/wsCommonForm";
	}

    /**
     * 跳转到编辑页面
     * @param id ID
     * @param model 实体
     */
	@GetMapping("/edit/{id}")
	//@RequiresPermissions("zhddkk:wsCommon:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		WsCommonDO wsCommon = wsCommonService.selectById(id);
		model.addAttribute("wsCommon", wsCommon);
	    return "zhddkk/wsCommon/wsCommonForm";
	}
	
	/**
	 * 保存
	 */
    @OperationLogAnnotation(type=OperationEnum.INSERT,module=ModuleEnum.CONFIGURATION,subModule="",describe="保存通用配置")
	@ResponseBody
	@PostMapping("/save")
	//@RequiresPermissions("zhddkk:wsCommon:add")
	public Result<String> save( WsCommonDO wsCommon){
		wsCommonService.insert(wsCommon);
        return Result.ok();
	}

	/**
	 * 编辑
	 */
	@ResponseBody
	@RequestMapping("/update")
	//@RequiresPermissions("zhddkk:wsCommon:edit")
	public Result<String> update( WsCommonDO wsCommon){
		wsCommonService.updateById(wsCommon);
		return Result.ok();
	}
	
	/**
	 * 删除
	 */
    @OperationLogAnnotation(type=OperationEnum.DELETE,module=ModuleEnum.CONFIGURATION,subModule="",describe="删除通用配置")
	@PostMapping("/remove")
	@ResponseBody
	//@RequiresPermissions("zhddkk:wsCommon:remove")
	public Result<String> remove( Integer id){
		wsCommonService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	//@RequiresPermissions("zhddkk:wsCommon:batchRemove")
	public Result<String> remove(@RequestParam("ids[]") Integer[] ids){
		wsCommonService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}
	
}
