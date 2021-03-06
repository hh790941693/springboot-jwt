package com.pjb.springbootjwt.zhddkk.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.baomidou.mybatisplus.enums.SqlLike;
import com.pjb.springbootjwt.zhddkk.dto.WsChatroomDTO;
import com.pjb.springbootjwt.zhddkk.util.CommonUtil;
import com.pjb.springbootjwt.zhddkk.util.SessionUtil;
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
import com.pjb.springbootjwt.zhddkk.domain.WsChatroomDO;
import com.pjb.springbootjwt.zhddkk.service.WsChatroomService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 聊天室房间表.
 * 作者:admin
 * 邮箱:anonymous@anonymous.com
 * 创建时间:2021-07-27 09:42:44
 */
@Controller
@RequestMapping("/zhddkk/wsChatroom")
public class WsChatroomController extends AdminBaseController {

    private static final Logger logger = LoggerFactory.getLogger(WsChatroomController.class);

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
	private WsChatroomService wsChatroomService;

    /**
    * 跳转到聊天室房间表页面.
	*/
	@GetMapping()
	//@RequiresPermissions("zhddkk:wsChatroom:wsChatroom")
    public String wsChatroom(){
	    return "zhddkk/wsChatroom/wsChatroom";
	}

    /**
     * 获取聊天室房间表列表数据.
     */
	@ResponseBody
	@GetMapping("/list")
	//@RequiresPermissions("zhddkk:wsChatroom:wsChatroom")
	public Result<Page<WsChatroomDO>> list(WsChatroomDO wsChatroomDto) {
        Wrapper<WsChatroomDO> wrapper = new EntityWrapper<WsChatroomDO>(wsChatroomDto);
        Page<WsChatroomDO> page = wsChatroomService.selectPage(getPage(WsChatroomDO.class), wrapper);
        return Result.ok(page);
	}

    /**
     * 跳转到聊天室房间表添加页面.
     */
	@GetMapping("/add")
	//@RequiresPermissions("zhddkk:wsChatroom:add")
    public String add(Model model) {
		WsChatroomDO wsChatroom = new WsChatroomDO();
        model.addAttribute("wsChatroom", wsChatroom);
	    return "zhddkk/wsChatroom/wsChatroomForm";
	}

    /**
     * 跳转到聊天室房间表编辑页面.
     * @param id 聊天室房间表ID
     * @param model 聊天室房间表实体
     */
	@GetMapping("/edit/{id}")
	//@RequiresPermissions("zhddkk:wsChatroom:edit")
    public String edit(@PathVariable("id") Long id, Model model) {
		WsChatroomDO wsChatroom = wsChatroomService.selectById(id);
		model.addAttribute("wsChatroom", wsChatroom);
	    return "zhddkk/wsChatroom/wsChatroomForm";
	}
	
	/**
	 * 保存聊天室房间表.
	 */
	@ResponseBody
	@PostMapping("/save")
	@Transactional(rollbackFor = Exception.class)
	//@RequiresPermissions("zhddkk:wsChatroom:add")
	public Result<String> save(WsChatroomDO wsChatroom) {
		wsChatroom.setCreateUserId(Long.valueOf(SessionUtil.getSessionUserId()));
		wsChatroom.setRoomId(CommonUtil.generateRandomCode(8));
		wsChatroom.setCreateTime(new Date());
		wsChatroom.setUpdateTime(new Date());
		wsChatroom.setStatus(1);
		if (StringUtils.isBlank(wsChatroom.getPassword())) {
			wsChatroom.setPassword(null);
		}
		wsChatroomService.insert(wsChatroom);
        return Result.ok();
	}

	/**
	 * 编辑聊天室房间表.
	 */
	@ResponseBody
	@RequestMapping("/update")
	//@RequiresPermissions("zhddkk:wsChatroom:edit")
	public Result<String> update(WsChatroomDO wsChatroom) {
		if (StringUtils.isBlank(wsChatroom.getPassword())) {
			wsChatroom.setPassword(null);
		}
		wsChatroomService.updateById(wsChatroom);
		return Result.ok();
	}
	
	/**
	 * 删除聊天室房间表.
	 */
	@PostMapping("/remove")
	@ResponseBody
	//@RequiresPermissions("zhddkk:wsChatroom:remove")
	public Result<String> remove(Long id) {
		wsChatroomService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 批量删除聊天室房间表.
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	//@RequiresPermissions("zhddkk:wsChatroom:batchRemove")
	public Result<String> remove(@RequestParam("ids[]") Long[] ids) {
		wsChatroomService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}

	@GetMapping("/queryChatRoomList")
	@ResponseBody
	public Result<List<WsChatroomDTO>> queryChatRoomList(WsChatroomDO wsChatroomDO) {
		Wrapper<WsChatroomDO> wrapper = new EntityWrapper<>();
		if (StringUtils.isNotBlank(wsChatroomDO.getName())) {
			wrapper.like("t1.name", wsChatroomDO.getName(), SqlLike.DEFAULT);
		}
		if (StringUtils.isNotBlank(wsChatroomDO.getRoomId())) {
			wrapper.like("t1.room_id", wsChatroomDO.getRoomId(), SqlLike.DEFAULT);
		}
		if (StringUtils.isNotBlank(wsChatroomDO.getCategory1())) {
			wrapper.eq("t1.category1", wsChatroomDO.getCategory1());
		}
		if (StringUtils.isNotBlank(wsChatroomDO.getCategory2())) {
			wrapper.eq("t1.category2", wsChatroomDO.getCategory2());
		}
		if (StringUtils.isNotBlank(wsChatroomDO.getPasswordOrNot())) {
			if (wsChatroomDO.getPasswordOrNot().equals("0")) {
				wrapper.andNew().eq("t1.password", "").or().isNull("t1.password");
			} else {
				wrapper.ne("t1.password", "");
			}
		}
		List<WsChatroomDTO> wsChatroomDOList = wsChatroomService.queryChatRoomInfoList(wrapper, SessionUtil.getSessionUserId());
		return Result.ok(wsChatroomDOList);
	}

	@RequestMapping("/wsSimpleChatRoom.page")
	public String simpleChatRoom(String roomId, String roomPassword, Model model) {
		model.addAttribute("roomId", roomId);
		model.addAttribute("roomPassword", roomPassword);
		return "ws/wsSimpleChatRoom";
	}
}