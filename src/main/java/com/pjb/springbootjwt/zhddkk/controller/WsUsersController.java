package com.pjb.springbootjwt.zhddkk.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.domain.WsFriendsApplyDO;
import com.pjb.springbootjwt.zhddkk.domain.WsFriendsDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUserProfileDO;
import com.pjb.springbootjwt.zhddkk.entity.WsFriendsApply;
import com.pjb.springbootjwt.zhddkk.enumx.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.enumx.OperationEnum;
import com.pjb.springbootjwt.zhddkk.service.WsFriendsApplyService;
import com.pjb.springbootjwt.zhddkk.service.WsFriendsService;
import com.pjb.springbootjwt.zhddkk.service.WsUserProfileService;
import com.pjb.springbootjwt.zhddkk.util.ExcelUtil;
import com.pjb.springbootjwt.zhddkk.util.SecurityAESUtil;
import com.pjb.springbootjwt.zhddkk.websocket.ZhddWebSocket;
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

import javax.servlet.http.HttpServletResponse;

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

    @Autowired
    private WsUserProfileService wsUserProfileService;

    /**
    * 跳转到用户账号表页面
	*/
	@GetMapping("/wsUsers")
	String wsUsers(Model model, String user){
		model.addAttribute("user", user);
	    return "zhddkk/wsUsers/wsUsers";
	}

    /**
     * 获取用户账号表列表数据
     */
	@ResponseBody
	@GetMapping("/wsUsersList")
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

	/**
	 * 跳转到管理员用户列表
	 */
	@GetMapping("/wsUsersForAdmin")
	String wsUsersForAdmin(Model model, String user){
		return "zhddkk/wsUsers/wsUsersForAdmin";
	}

	/**
	 * 获取管理员用户列表
	 */
	@ResponseBody
	@GetMapping("/wsUsersListForAdmin")
	public Result<Page<WsUsersDO>> wsUsersListForAdmin(WsUsersDO wsUsersDTO) {
		Wrapper<WsUsersDO> wrapper = new EntityWrapper<WsUsersDO>();
		if (StringUtils.isNotBlank(wsUsersDTO.getName())) {
			wrapper.like("name", wsUsersDTO.getName(), SqlLike.DEFAULT);
		}
		if (StringUtils.isNotBlank(wsUsersDTO.getState())) {
			wrapper.eq("state", wsUsersDTO.getState());
		}
		if (StringUtils.isNotBlank(wsUsersDTO.getEnable())) {
			wrapper.eq("enable", wsUsersDTO.getEnable());
		}
		if (StringUtils.isNotBlank(wsUsersDTO.getSpeak())) {
			wrapper.eq("speak", wsUsersDTO.getSpeak());
		}

		wrapper.ne("name", "admin").orderBy("state", false);
		Page<WsUsersDO> page = wsUsersService.selectPage(getPage(WsUsersDO.class), wrapper);
		return Result.ok(page);
	}

	/**
	 * 让某用户下线
	 * @param id
	 * @return
	 */
	@OperationLogAnnotation(type= OperationEnum.UPDATE,module= ModuleEnum.USER_MANAGE,subModule="",describe="使用户下线")
	@RequestMapping(value = "offlineUser.do")
	@ResponseBody
	public Result<String> offlineUser(@RequestParam("id") String id) {
		WsUsersDO wsUsersDO = wsUsersService.selectById(id);
		if (null == wsUsersDO){
			return Result.fail("用户不存在");
		}
		String userName = wsUsersDO.getName();
		Map<String, ZhddWebSocket> socketMap = ZhddWebSocket.getClients();
		for (Map.Entry<String,ZhddWebSocket> entry : socketMap.entrySet()) {
			if (entry.getKey().equals(userName)) {
				try {
					entry.getValue().getSession().close();
					ZhddWebSocket.getClients().remove(userName);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		wsUsersDO.setState("0");
		boolean updateFlag = wsUsersService.updateById(wsUsersDO);
		if (updateFlag){
			return Result.ok();
		}
		return Result.fail();
	}

	/**
	 * 让某用户禁用/启用
	 * @param id
	 * @param status
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.UPDATE,module=ModuleEnum.USER_MANAGE,subModule="",describe="用户的禁用/启用")
	@RequestMapping(value = "operEnableUser.do")
	@ResponseBody
	public Result<String> operEnableUser(@RequestParam("id") String id,@RequestParam("status") String status) {
		WsUsersDO wsUsersDO = wsUsersService.selectById(id);
		if (null == wsUsersDO){
			return Result.fail("用户不存在");
		}
		wsUsersDO.setEnable(status);
		boolean updateFlag = wsUsersService.updateById(wsUsersDO);
		if (updateFlag){
			return Result.ok();
		}
		return Result.fail();
	}

	/**
	 * 让某用户禁言/开言
	 * @param id
	 * @param status
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.UPDATE,module=ModuleEnum.USER_MANAGE,subModule="",describe="用户的禁言/开言")
	@RequestMapping(value = "operSpeakUser.do")
	@ResponseBody
	public Result<String> operSpeakUser(@RequestParam("id") String id,@RequestParam("status") String status) {
		WsUsersDO wsUsersDO = wsUsersService.selectById(id);
		if (null == wsUsersDO){
			return Result.fail("用户不存在");
		}
		wsUsersDO.setSpeak(status);
		boolean updateFlag = wsUsersService.updateById(wsUsersDO);
		if (updateFlag){
			return Result.ok();
		}
		return Result.fail();
	}

	/**
	 * 导出用户信息
	 *
	 * @param response
	 */
	@OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.USER_MANAGE,subModule="",describe="导出用户信息")
	@RequestMapping(value = "exportUser.do", method = RequestMethod.GET)
	public void exportUser(HttpServletResponse response){
		logger.debug("开始导出用户信息");
		List<WsUsersDO> list = wsUsersService.selectList(new EntityWrapper<WsUsersDO>().ne("name", "admin"));
		if (null != list && list.size()>0) {
			for (WsUsersDO wu : list) {
				String password = wu.getPassword();
				if (StringUtils.isNotEmpty(password)) {
					String passwordDecode = SecurityAESUtil.decryptAES(password, CommonConstants.AES_PASSWORD);
					wu.setPasswordDecode(passwordDecode);
				}
			}

			String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String fileName = "wsUser".concat("_").concat(time).concat(".xls");
			ExcelUtil.exportExcel(list, "用户信息", "用户", WsUsersDO.class, fileName, response);
		}
	}

	/**
	 * 显示用户信息
	 *
	 * @param model
	 * @param user
	 * @return
	 */
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.SETTING,subModule="",describe="显示个用户信息首页")
	@RequestMapping(value = "showPersonalInfo.page")
	public String showPersonalInfo(Model model,@RequestParam("user")String user) {
		logger.debug("访问showPersonalInfo.page");
		model.addAttribute("user", user);
		return "zhddkk/wsUsers/showPersonalInfo";
	}

    /**
     * 查询个人信息
     *
     * @return
     */
    @OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.SETTING,subModule="",describe="查询个人信息")
    @RequestMapping(value="queryPersonInfo.json",method=RequestMethod.POST)
    @ResponseBody
    public Result<WsUserProfileDO> queryPersonInfo(@RequestParam("user") String user) {
        WsUserProfileDO wsUserProfileDO = wsUserProfileService.selectOne(new EntityWrapper<WsUserProfileDO>()
                .eq("user_name", user));
        if (null == wsUserProfileDO) {
            return Result.fail();
        }

        return Result.ok(wsUserProfileDO);
    }
}