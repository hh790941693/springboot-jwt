package com.pjb.springbootjwt.zhddkk.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.pjb.springbootjwt.zhddkk.bean.ChatMessageBean;
import com.pjb.springbootjwt.zhddkk.util.JsonUtil;
import com.pjb.springbootjwt.zhddkk.websocket.ZhddWebSocket;
import org.apache.commons.lang.StringUtils;
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

	@ResponseBody
	@GetMapping("/queryRecentAdsList")
	public Result<List<WsAdsDO>> queryRecentAdsList(Integer count){
		if (null == count || count.intValue() <= 0){
			count = 4;
		}
		Page<WsAdsDO> page = wsAdsService.selectPage(new Page<>(1,count), new EntityWrapper<WsAdsDO>()
			.orderBy("create_time", false));
		List<WsAdsDO> list = page.getRecords();
		if (null != list && list.size()>0){
			return Result.ok(list);
		}

		return Result.fail();
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
	@Transactional
	public Result<String> save( WsAdsDO wsAds){
		// 接收人列表
		List<String> receiveList = new ArrayList<String>();
		String title = wsAds.getTitle();
		String content = wsAds.getContent();
		if (StringUtils.isBlank(title) || StringUtils.isBlank(content)){
			return Result.fail("参数不能为空");
		}

		// 插入广告记录
		WsAdsDO wsAdsDO = new WsAdsDO();
		wsAdsDO.setTitle(title);
		wsAdsDO.setContent(content);
		boolean insertFlag = wsAdsService.insert(wsAdsDO);
		if (insertFlag){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String curTime = sdf.format(new Date());
			Map<String, ZhddWebSocket> socketMap = ZhddWebSocket.getClients();
			for (Map.Entry<String,ZhddWebSocket> entry : socketMap.entrySet()) {
				if (entry.getKey().equals("admin")) {
					continue;
				}

				try {
					ChatMessageBean chatBean = new ChatMessageBean(curTime,"4","广告消息","admin",entry.getKey(), "title:"+title+";content:"+content);
					entry.getValue().getSession().getBasicRemote().sendText(JsonUtil.javaobject2Jsonstr(chatBean));
					receiveList.add(entry.getKey());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			wsAdsDO.setReceiveList(receiveList.toString());
			wsAdsService.updateById(wsAdsDO);
			return Result.ok();
		}
		return Result.fail();
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
