package com.pjb.springbootjwt.zhddkk.controller;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.common.annotation.SubToken;
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.constants.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.constants.OperationEnum;
import com.pjb.springbootjwt.zhddkk.domain.WsAdsDO;
import com.pjb.springbootjwt.zhddkk.service.WsAdsService;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * 广告表.
 */
@Controller
@RequestMapping("/zhddkk/wsAds")
public class WsAdsController extends AdminBaseController {

    private static final Logger logger = LoggerFactory.getLogger(WsAdsController.class);

    /**
     * 初始化用.
     * @param binder binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @Autowired
    private WsAdsService wsAdsService;

    /**
    * 跳转到广告表页面.
    */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.AD_PUBLISH, subModule = "", describe = "广告列表页面")
    @GetMapping()
    public String wsAds(HttpServletRequest request, Model model) {
        // 获取get请求参数
        String userId = request.getParameter("userId");
        String userName = request.getParameter("userName");
        String roleId = request.getParameter("roleId");

        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        model.addAttribute("roleId", roleId);
        Map<String, String[]> paraMap = request.getParameterMap();

        return "zhddkk/wsAds/wsAds";
    }

    /**
     * 获取广告表列表数据.
     */
    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.AD_PUBLISH, subModule = "", describe = "广告列表")
    @ResponseBody
    @GetMapping("/list")
    public Result<Page<WsAdsDO>> list(WsAdsDO wsAdsDto) {
        Wrapper<WsAdsDO> wrapper = new EntityWrapper<WsAdsDO>();
        if (StringUtils.isNotBlank(wsAdsDto.getTitle())) {
            wrapper.like("title", wsAdsDto.getTitle(), SqlLike.DEFAULT);
        }
        if (StringUtils.isNotBlank(wsAdsDto.getContent())) {
            wrapper.like("content", wsAdsDto.getContent(), SqlLike.DEFAULT);
        }
        wrapper.orderBy("create_time", false);
        Page<WsAdsDO> page = wsAdsService.selectPage(getPage(WsAdsDO.class), wrapper);
        return Result.ok(page);
    }

    /**
     * 查询最新广告数据.
     * @param count 显示条数
     * @return 最新广告数据
     */
    @ResponseBody
    @GetMapping("/queryRecentAdsList")
    public Result<List<WsAdsDO>> queryRecentAdsList(Integer count) {
        if (null == count || count.intValue() <= 0) {
            count = 4;
        }
        Page<WsAdsDO> page = wsAdsService.selectPage(new Page<>(1, count), new EntityWrapper<WsAdsDO>()
            .orderBy("create_time", false));
        List<WsAdsDO> list = page.getRecords();
        if (null != list) {
            return Result.ok(list);
        }

        return Result.fail();
    }

    /**
     * 根据id查询广告详情.
     * @param id 广告id
     * @return 查询指定的广告信息
     */
    @ResponseBody
    @GetMapping("/selectById")
    public Result<WsAdsDO> selectById(String id) {
        WsAdsDO wsAdsDO = wsAdsService.selectById(id);
        return Result.ok(wsAdsDO);
    }

    /**
     * 跳转到广告表添加页面.
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.AD_PUBLISH, subModule = "", describe = "添加广告页面")
    @GetMapping("/add")
    @SubToken(save = true)
    public String add(Model model) {
        WsAdsDO wsAds = new WsAdsDO();
        model.addAttribute("wsAds", wsAds);
        return "zhddkk/wsAds/wsAdsForm";
    }

    /**
     * 跳转到广告表编辑页面.
     * @param id 广告表ID
     * @param model 广告表实体
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        WsAdsDO wsAds = wsAdsService.selectById(id);
        model.addAttribute("wsAds", wsAds);
        return "zhddkk/wsAds/wsAdsForm";
    }

    /**
     * 保存广告表.
     */
    @OperationLogAnnotation(type = OperationEnum.INSERT, module = ModuleEnum.AD_PUBLISH, subModule = "", describe = "保存广告")
    @ResponseBody
    @PostMapping("/save")
    @Transactional
    @SubToken(remove = true)
    public Result<String> save(@Validated WsAdsDO wsAds, String subToken) {
        logger.info("进入保存广告信息");
        // 接收人列表
        List<String> receiveList = new ArrayList<>();

        // 模拟网络延迟,验证重复提交请求
        try{
            Thread.sleep(1500);//暂停1.5秒后程序继续执行
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 插入广告记录
        boolean insertFlag = wsAdsService.insert(wsAds);
        if (insertFlag) {
            wsAds.setReceiveList(receiveList.toString());
            wsAdsService.updateById(wsAds);
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 编辑广告表.
     */
    @ResponseBody
    @RequestMapping("/update")
    public Result<String> update(WsAdsDO wsAds) {
        wsAdsService.updateById(wsAds);
        return Result.ok();
    }

    /**
     * 删除广告表.
     */
    @OperationLogAnnotation(type = OperationEnum.DELETE, module = ModuleEnum.AD_PUBLISH, subModule = "", describe = "删除广告")
    @PostMapping("/remove")
    @ResponseBody
    public Result<String> remove(Integer id) {
        wsAdsService.deleteById(id);
        return Result.ok();
    }

    /**
     * 批量删除广告表.
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    public Result<String> remove(@RequestParam("ids[]") Integer[] ids) {
        wsAdsService.deleteBatchIds(Arrays.asList(ids));
        return Result.ok();
    }

    @GetMapping("/adsDetail.page")
    public String adsDetail(@RequestParam("id") String id, Model model) {
        model.addAttribute("id", id);
        return "zhddkk/wsAds/wsAdsDetail";
    }
}
