package com.pjb.springbootjwt.zhddkk.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.pjb.springbootjwt.zhddkk.domain.WsFriendsDO;
import com.pjb.springbootjwt.zhddkk.service.WsFriendsService;
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
import com.pjb.springbootjwt.zhddkk.domain.WsFriendsApplyDO;
import com.pjb.springbootjwt.zhddkk.service.WsFriendsApplyService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 好友申请表
 */
@Controller
@RequestMapping("/zhddkk/wsFriendsApply")
public class WsFriendsApplyController extends AdminBaseController {

    private static Logger logger = LoggerFactory.getLogger(WsFriendsApplyController.class);

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @Autowired
	private WsFriendsApplyService wsFriendsApplyService;

    @Autowired
	private WsFriendsService wsFriendsService;

    /**
    * 我的申请页面
	*/
	@GetMapping("/myApply")
	String myApply(Model model, String user){
		model.addAttribute("user", user);
	    return "zhddkk/wsFriendsApply/myApply";
	}

    /**
     * 我的申请列表
     */
	@ResponseBody
	@GetMapping("/myApplyList")
	public Result<Page<WsFriendsApplyDO>> myApplyList(WsFriendsApplyDO wsFriendsApplyDTO){
        Wrapper<WsFriendsApplyDO> wrapper = new EntityWrapper<WsFriendsApplyDO>();
        if (StringUtils.isNotBlank(wsFriendsApplyDTO.getFromName())){
        	wrapper.eq("from_name", wsFriendsApplyDTO.getFromName());
		}
        wrapper.orderBy("create_time", false).orderBy("process_status", true);
        Page<WsFriendsApplyDO> page = wsFriendsApplyService.selectPage(getPage(WsFriendsApplyDO.class), wrapper);
        return Result.ok(page);
	}

	/**
	 * 好友申请页面
	 */
	@GetMapping("/friendApply")
	String friendApply(Model model, String user){
		model.addAttribute("user", user);
		return "zhddkk/wsFriendsApply/friendApply";
	}

	/**
	 * 好友申请列表
	 */
	@ResponseBody
	@GetMapping("/friendApplyList")
	public Result<Page<WsFriendsApplyDO>> friendApplyList(WsFriendsApplyDO wsFriendsApplyDTO){
		Wrapper<WsFriendsApplyDO> wrapper = new EntityWrapper<WsFriendsApplyDO>();
		if (StringUtils.isNotBlank(wsFriendsApplyDTO.getToName())){
			wrapper.eq("to_name", wsFriendsApplyDTO.getToName());
		}
		wrapper.orderBy("create_time", false).orderBy("process_status", true);
		Page<WsFriendsApplyDO> page = wsFriendsApplyService.selectPage(getPage(WsFriendsApplyDO.class), wrapper);
		return Result.ok(page);
	}


	@PostMapping("/operateFriendApply")
	@ResponseBody
	public Result<String> operateFriendApply(int id, int status){
		if (status != 2 && status != 3){
			return Result.fail("参数错误");
		}

		WsFriendsApplyDO wfa = wsFriendsApplyService.selectById(id);
		if (null == wfa){
			return Result.fail("申请记录不存在");
		}
		if (wfa.getProcessStatus().intValue() != 1){
			return Result.fail("当前申请记录状态不是申请中");
		}

		wfa.setProcessStatus(status);
		boolean updateWfaFlag = wsFriendsApplyService.updateById(wfa);
		if (status == 3) {
			//同意
			if (updateWfaFlag) {
				WsFriendsDO wf1 = new WsFriendsDO();
				wf1.setUid(wfa.getFromId());
				wf1.setUname(wfa.getFromName());
				wf1.setFid(wfa.getToId());
				wf1.setFname(wfa.getToName());
				int isExist1 = wsFriendsService.selectCount(new EntityWrapper<WsFriendsDO>()
						.eq("uname", wfa.getFromName()).eq("fname", wfa.getToName()));
				if (isExist1 <= 0) {
					wsFriendsService.insert(wf1);
				}

				WsFriendsDO wf2 = new WsFriendsDO();
				wf2.setUid(wfa.getToId());
				wf2.setUname(wfa.getToName());
				wf2.setFid(wfa.getFromId());
				wf2.setFname(wfa.getFromName());
				int isExist2 = wsFriendsService.selectCount(new EntityWrapper<WsFriendsDO>()
						.eq("uname", wfa.getToName()).eq("fname", wfa.getFromName()));
				if (isExist2 <= 0) {
					wsFriendsService.insert(wf2);
				}

				return Result.ok();
			}
		}else if (status == 2){
			//拒绝
			if (updateWfaFlag){
				return Result.ok();
			}
		}

		return Result.fail();
	}
}