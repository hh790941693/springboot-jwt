package com.pjb.springbootjwt.zhddkk.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.common.vo.Result;
import com.pjb.springbootjwt.zhddkk.domain.WsFriendsApplyDO;
import com.pjb.springbootjwt.zhddkk.service.WsFriendsApplyService;
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
import com.pjb.springbootjwt.zhddkk.domain.WsFriendsDO;
import com.pjb.springbootjwt.zhddkk.service.WsFriendsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 好友列表
 */
@Controller
@RequestMapping("/zhddkk/wsFriends")
public class WsFriendsController extends AdminBaseController {

    private static Logger logger = LoggerFactory.getLogger(WsFriendsController.class);

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @Autowired
	private WsFriendsService wsFriendsService;

    @Autowired
    private WsFriendsApplyService wsFriendsApplyService;

    /**
    * 跳转到好友列表页面
	*/
	@GetMapping()
	String wsFriends(Model model, String user){
		model.addAttribute("user", user);
	    return "zhddkk/wsFriends/wsFriends";
	}

    /**
     * 获取好友列表列表数据
     */
	@ResponseBody
	@GetMapping("/list")
	public Result<Page<WsFriendsDO>> list(WsFriendsDO wsFriendsDTO){
		Wrapper<WsFriendsDO> wrapper = new EntityWrapper<WsFriendsDO>();
		if (StringUtils.isNotBlank(wsFriendsDTO.getUname())){
			wrapper.eq("t1.uname", wsFriendsDTO.getUname());
		}
		if (StringUtils.isNotBlank(wsFriendsDTO.getFname())){
			wrapper.like("t1.fname", wsFriendsDTO.getFname());
		}
		wrapper.orderBy("t1.create_time", false);
		Page<WsFriendsDO> qryPage = getPage(WsFriendsDO.class);
		List<WsFriendsDO> list = wsFriendsService.queryFriendsPage(qryPage, wrapper);
		qryPage.setRecords(list);
		return Result.ok(qryPage);
	}

	/**
	 * 删除好友列表
	 */
	@PostMapping("/remove")
	@ResponseBody
	@Transactional
	public Result<String> remove( Integer id){
		WsFriendsDO wsFriendsDO = wsFriendsService.selectById(id);
		if (null == wsFriendsDO){
			return Result.fail();
		}

		String uname = wsFriendsDO.getUname();
		String fname = wsFriendsDO.getFname();
		logger.info("uname:"+uname+"   fname:"+fname);
		if (StringUtils.isNotEmpty(uname) && StringUtils.isNotEmpty(fname)) {
			wsFriendsService.delete(new EntityWrapper<WsFriendsDO>().eq("uname", uname).eq("fname", fname));
			wsFriendsService.delete(new EntityWrapper<WsFriendsDO>().eq("uname", fname).eq("fname", uname));
			wsFriendsApplyService.delete(new EntityWrapper<WsFriendsApplyDO>().eq("from_name", fname)
					.eq("to_name", uname));
			wsFriendsApplyService.delete(new EntityWrapper<WsFriendsApplyDO>().eq("from_name", uname)
					.eq("to_name", fname));
		}

        return Result.ok();
	}
	
	/**
	 * 批量删除好友列表
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	public Result<String> remove(@RequestParam("ids[]") Integer[] ids){
		wsFriendsService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}
}
