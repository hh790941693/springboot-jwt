package com.pjb.springbootjwt.zhddkk.controller;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.common.vo.Result;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.cache.CoreCache;
import com.pjb.springbootjwt.zhddkk.constants.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.constants.OperationEnum;
import com.pjb.springbootjwt.zhddkk.domain.WsCommonDO;
import com.pjb.springbootjwt.zhddkk.service.CacheService;
import com.pjb.springbootjwt.zhddkk.service.WsCommonService;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.pjb.springbootjwt.zhddkk.util.CommonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * 通用配置.
 */
@Controller
@RequestMapping("/zhddkk/wsCommon")
public class WsCommonController extends AdminBaseController {

    private static final Logger logger = LoggerFactory.getLogger(WsCommonController.class);

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
    private WsCommonService wsCommonService;

    /**
     * 缓存.
     */
    @Autowired
    private CacheService cacheService;

    /**
    * 跳转到页面.
    */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.CONFIGURATION, subModule = "", describe = "通用配置页面")
    @GetMapping("{type}")
    //@RequiresPermissions("zhddkk:wsCommon:wsCommon")
    public String wsCommon(Model model, @PathVariable("type")String type) {
        logger.info("进入通用配置页面:{}", type);
        model.addAttribute("type", type);
        return "zhddkk/wsCommon/wsCommon";
    }

    /**
     * 获取列表数据.
     */
    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.CONFIGURATION, subModule = "", describe = "通用配置列表")
    @ResponseBody
    @GetMapping("/list")
    //@RequiresPermissions("zhddkk:wsCommon:wsCommon")
    public Result<Page<WsCommonDO>> list(WsCommonDO wsCommonDto) {
        List<WsCommonDO> cacheList = CoreCache.getInstance().getCommonList();
        if (null != cacheList && cacheList.size() > 0) {
            cacheList = cacheList.stream().filter(cache -> StringUtils.isNotBlank(cache.getType()) && cache.getType().equals(wsCommonDto.getType()))
                    .filter(cache -> StringUtils.isNotBlank(cache.getName()) && cache.getName().contains(wsCommonDto.getName()))
                    .sorted(Comparator.comparing(WsCommonDO::getId))
                    .collect(Collectors.toList());
            Page<WsCommonDO> page = getPage(WsCommonDO.class);
            page.setTotal(cacheList.size());
            page.setRecords(CommonUtil.pageToList(cacheList, page.getCurrent(), page.getSize()));
            return Result.ok(page);
        } else {
            Wrapper<WsCommonDO> wrapper = new EntityWrapper<WsCommonDO>();
            if (StringUtils.isNotBlank(wsCommonDto.getType())) {
                wrapper.eq("type", wsCommonDto.getType());
            }
            if (StringUtils.isNotBlank(wsCommonDto.getName())) {
                wrapper.like("name", wsCommonDto.getName(), SqlLike.DEFAULT);
            }
            wrapper.orderBy("orderby");
            Page<WsCommonDO> page = wsCommonService.selectPage(getPage(WsCommonDO.class), wrapper);
            return Result.ok(page);
        }
    }

    /**
     * 跳转到添加页面.
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.CONFIGURATION, subModule = "", describe = "添加配置页面")
    @GetMapping("/add")
    //@RequiresPermissions("zhddkk:wsCommon:add")
    public String add(Model model, String type) {
        WsCommonDO wsCommon = new WsCommonDO();
        wsCommon.setType(type);
        model.addAttribute("wsCommon", wsCommon);
        return "zhddkk/wsCommon/wsCommonForm";
    }

    /**
     * 跳转到编辑页面.
     * @param id ID
     * @param model 实体
     */
    @GetMapping("/edit/{id}")
    //@RequiresPermissions("zhddkk:wsCommon:edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        WsCommonDO wsCommon = wsCommonService.selectById(id);
        model.addAttribute("wsCommon", wsCommon);
        return "zhddkk/wsCommon/wsCommonForm";
    }

    /**
     * 保存.
     */
    @OperationLogAnnotation(type = OperationEnum.INSERT, module = ModuleEnum.CONFIGURATION, subModule = "", describe = "保存通用配置")
    @ResponseBody
    @PostMapping("/save")
    //@RequiresPermissions("zhddkk:wsCommon:add")
    public Result<String> save(WsCommonDO wsCommon) {
        wsCommonService.insert(wsCommon);
        // 刷新缓存
        cacheService.cacheCommonData();
        return Result.ok();
    }

    /**
     * 编辑.
     */
    @ResponseBody
    @RequestMapping("/update")
    //@RequiresPermissions("zhddkk:wsCommon:edit")
    public Result<String> update(WsCommonDO wsCommon) {
        wsCommonService.updateById(wsCommon);
        // 刷新缓存
        cacheService.cacheCommonData();
        return Result.ok();
    }

    /**
     * 删除.
     */
    @OperationLogAnnotation(type = OperationEnum.DELETE, module = ModuleEnum.CONFIGURATION, subModule = "", describe = "删除通用配置")
    @PostMapping("/remove")
    @ResponseBody
    //@RequiresPermissions("zhddkk:wsCommon:remove")
    public Result<String> remove(Integer id) {
        wsCommonService.deleteById(id);
        // 刷新缓存
        cacheService.cacheCommonData();
        return Result.ok();
    }

    /**
     * 批量删除.
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    //@RequiresPermissions("zhddkk:wsCommon:batchRemove")
    public Result<String> remove(@RequestParam("ids[]") Integer[] ids) {
        wsCommonService.deleteBatchIds(Arrays.asList(ids));
        // 刷新缓存
        cacheService.cacheCommonData();
        return Result.ok();
    }

    @ResponseBody
    @GetMapping("/jsonlist")
    public String queryCommonList(String type, String name){
        Wrapper<WsCommonDO> wrapper = new EntityWrapper<WsCommonDO>();
        if (StringUtils.isNotBlank(type)) {
            wrapper.eq("type", type);
        }
        if (StringUtils.isNotBlank(name)) {
            wrapper.like("name", name, SqlLike.DEFAULT);
        }
        wrapper.orderBy("orderby");
        Page<WsCommonDO> page = wsCommonService.selectPage(new Page<>(1,99), wrapper);
        List<WsCommonDO> list = page.getRecords();

        return JSON.toJSONString(list);
    }
}
