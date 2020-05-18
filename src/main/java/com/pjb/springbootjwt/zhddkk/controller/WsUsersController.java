package com.pjb.springbootjwt.zhddkk.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.pjb.springbootjwt.zhddkk.domain.WsFriendsApplyDO;
import com.pjb.springbootjwt.zhddkk.domain.WsFriendsDO;
import com.pjb.springbootjwt.zhddkk.entity.WsFriendsApply;
import com.pjb.springbootjwt.zhddkk.service.WsFriendsApplyService;
import com.pjb.springbootjwt.zhddkk.service.WsFriendsService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户账号表
 */
@Controller
@RequestMapping("/zhddkk/wsUsers")
public class WsUsersController extends AdminBaseController {

    private static Logger logger = LoggerFactory.getLogger(WsUsersController.class);

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @Autowired
	private WsUsersService wsUsersService;

    @Autowired
	private WsFriendsService wsFriendsService;

    @Autowired
    private WsFriendsApplyService wsFriendsApplyService;

    /**
    * 跳转到用户账号表页面
	*/
	@GetMapping()
	String wsUsers(Model model, String user){
		model.addAttribute("user", user);
	    return "zhddkk/wsUsers/wsUsers";
	}

    /**
     * 获取用户账号表列表数据
     */
	@ResponseBody
	@GetMapping("/list")
	public Result<Page<WsUsersDO>> list(WsUsersDO wsUsersDTO, String curUser){
        Wrapper<WsUsersDO> wrapper = new EntityWrapper<WsUsersDO>();
        if (StringUtils.isNotBlank(wsUsersDTO.getName())){
        	wrapper.like("name", wsUsersDTO.getName(), SqlLike.DEFAULT);
		}
        Page<WsUsersDO> page = wsUsersService.selectPage(getPage(WsUsersDO.class), wrapper);
		List<WsUsersDO> userlist = page.getRecords();

		if (null != userlist && userlist.size()>0) {
			for (WsUsersDO wu : userlist) {
				if (wu.getName().equals(curUser)) {
					wu.setIsFriend(3);
					continue;
				}
				wu.setIsFriend(0);
				int isMyFriend = wsFriendsService.selectCount(new EntityWrapper<WsFriendsDO>()
						.eq("uname", curUser).eq("fname", wu.getName()));
				if (isMyFriend > 0) {
					wu.setIsFriend(3);//已是好友
				} else {
					// 0:不是  1:申请中 2:被拒绝 3:申请成功
					WsFriendsApply wfa = new WsFriendsApply();
					wfa.setFromName(curUser);
					wfa.setToName(wu.getName());
					wfa.setStart(0);
					wfa.setLimit(10);

					List<WsFriendsApplyDO> applyList = wsFriendsApplyService.selectList(new EntityWrapper<WsFriendsApplyDO>()
							.eq("from_name", curUser).eq("to_name", wu.getName()));
					if (null == applyList || applyList.size() == 0) {
						wu.setIsFriend(0);//去申请
					} else if (applyList.size() == 1) {
						Integer processStatus = applyList.get(0).getProcessStatus();
						wu.setIsFriend(processStatus);// 1:申请中 2:被拒绝 3:申请成功
					} else if (applyList.size() > 1) {
						// 过滤掉被驳回的记录
						for (WsFriendsApplyDO temp : applyList) {
							if (temp.getProcessStatus() == 2) {
								continue;
							}
							wu.setIsFriend(temp.getProcessStatus());
						}
					}
				}
			}
		}

        return Result.ok(page);
	}

    /**
     * 跳转到用户账号表添加页面
     */
	@GetMapping("/add")
	@RequiresPermissions("zhddkk:wsUsers:add")
	String add(Model model){
		WsUsersDO wsUsers = new WsUsersDO();
        model.addAttribute("wsUsers", wsUsers);
	    return "zhddkk/wsUsers/wsUsersForm";
	}

    /**
     * 跳转到用户账号表编辑页面
     * @param id 用户账号表ID
     * @param model 用户账号表实体
     */
	@GetMapping("/edit/{id}")
	@RequiresPermissions("zhddkk:wsUsers:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		WsUsersDO wsUsers = wsUsersService.selectById(id);
		model.addAttribute("wsUsers", wsUsers);
	    return "zhddkk/wsUsers/wsUsersForm";
	}
	
	/**
	 * 保存用户账号表
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("zhddkk:wsUsers:add")
	public Result<String> save( WsUsersDO wsUsers){
		wsUsersService.insert(wsUsers);
        return Result.ok();
	}

	/**
	 * 编辑用户账号表
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("zhddkk:wsUsers:edit")
	public Result<String> update( WsUsersDO wsUsers){
		wsUsersService.updateById(wsUsers);
		return Result.ok();
	}
	
	/**
	 * 删除用户账号表
	 */
	@PostMapping("/remove")
	@ResponseBody
	@RequiresPermissions("zhddkk:wsUsers:remove")
	public Result<String> remove( Integer id){
		wsUsersService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 批量删除用户账号表
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	@RequiresPermissions("zhddkk:wsUsers:batchRemove")
	public Result<String> remove(@RequestParam("ids[]") Integer[] ids){
		wsUsersService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}

	@PostMapping("/addAsFriends")
	@ResponseBody
	public Result<String> addAsFriends(String curUser, Integer toUserId){
		Integer fromUserId = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", curUser)).getId();
		String toUserName = wsUsersService.selectById(toUserId).getName();

		logger.info(curUser+"申请添加"+toUserName+"为好友");
		WsFriendsDO wf = new WsFriendsDO();
		wf.setUname(curUser);
		wf.setFname(toUserName);
		int existCount = wsFriendsService.selectCount(new EntityWrapper<WsFriendsDO>().eq("uname", curUser)
				.eq("fname", toUserName));
		if (existCount<=0) {
			WsFriendsApplyDO wfa = new WsFriendsApplyDO();
			wfa.setFromId(fromUserId);
			wfa.setFromName(curUser);
			wfa.setToId(toUserId);
			wfa.setToName(toUserName);
			wfa.setProcessStatus(1);
			wsFriendsApplyService.insert(wfa);

			return Result.ok();
		}else{
			logger.info(toUserName+"已是你的好友了,无需再次申请");
		}

		return Result.fail();
	}
}
