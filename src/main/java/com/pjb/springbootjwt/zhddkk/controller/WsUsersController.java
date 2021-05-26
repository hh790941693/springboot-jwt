package com.pjb.springbootjwt.zhddkk.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.domain.*;
import com.pjb.springbootjwt.zhddkk.constants.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.constants.OperationEnum;
import com.pjb.springbootjwt.zhddkk.service.WsFriendsApplyService;
import com.pjb.springbootjwt.zhddkk.service.WsFriendsService;
import com.pjb.springbootjwt.zhddkk.service.WsUserProfileService;
import com.pjb.springbootjwt.zhddkk.service.SysUserRoleService;
import com.pjb.springbootjwt.zhddkk.util.ExcelUtil;
import com.pjb.springbootjwt.zhddkk.util.JsonUtil;
import com.pjb.springbootjwt.zhddkk.util.SecurityAESUtil;
import com.pjb.springbootjwt.zhddkk.util.SessionUtil;
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
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import com.pjb.springbootjwt.zhddkk.service.SysRoleService;
import com.pjb.springbootjwt.zhddkk.base.Result;
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
    
    @Autowired
    private SysUserRoleService sysUserRoleService;
    
    @Autowired
    private SysRoleService sysRoleService;
    
    /**
     * 跳转到用户账号表页面.
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.USER_MANAGE, subModule = "", describe = "用户列表页面")
    @GetMapping("/wsUsers")
    public String wsUsers(Model model, String user) {
        model.addAttribute("user", user);
        return "zhddkk/wsUsers/wsUsers";
    }
    
    /**
     * 获取用户账号表列表数据.
     */
    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.USER_MANAGE, subModule = "", describe = "用户列表")
    @ResponseBody
    @GetMapping("/wsUsersList")
    public Result<Page<WsUsersDO>> list(WsUsersDO wsUsersDTO, String curUser) {
        Wrapper<WsUsersDO> wrapper = new EntityWrapper<WsUsersDO>();
        if (StringUtils.isNotBlank(wsUsersDTO.getName())) {
            wrapper.like("t1.name", wsUsersDTO.getName(), SqlLike.DEFAULT);
        }
        Page<WsUsersDO> page = getPage(WsUsersDO.class);
        List<WsUsersDO> userList = wsUsersService.queryUserPage(page, wrapper);
        page.setRecords(userList);
        
        if (null != userList && userList.size() > 0) {
            for (WsUsersDO wu : userList) {
                if (wu.getName().equals(curUser)) {
                    wu.setIsFriend(3);
                    continue;
                }
                wu.setIsFriend(0);
                int isMyFriend = wsFriendsService
                    .selectCount(new EntityWrapper<WsFriendsDO>().eq("uname", curUser).eq("fname", wu.getName()));
                if (isMyFriend > 0) {
                    wu.setIsFriend(3); // 已是好友
                } else {
                    // 0:不是 1:申请中 2:被拒绝 3:申请成功
                    List<WsFriendsApplyDO> applyList = wsFriendsApplyService.selectList(
                        new EntityWrapper<WsFriendsApplyDO>().eq("from_name", curUser).eq("to_name", wu.getName()));
                    if (null == applyList || applyList.size() == 0) {
                        wu.setIsFriend(0); // 去申请
                    } else if (applyList.size() == 1) {
                        Integer processStatus = applyList.get(0).getProcessStatus();
                        wu.setIsFriend(processStatus); // 1:申请中 2:被拒绝 3:申请成功
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
     * 跳转到用户账号表添加页面.
     */
    @GetMapping("/add")
    @RequiresPermissions("zhddkk:wsUsers:add")
    public String add(Model model) {
        WsUsersDO wsUsers = new WsUsersDO();
        model.addAttribute("wsUsers", wsUsers);
        return "zhddkk/wsUsers/wsUsersForm";
    }
    
    /**
     * 跳转到用户账号表编辑页面.
     * 
     * @param id 用户账号表ID
     * @param model 用户账号表实体
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("zhddkk:wsUsers:edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        WsUsersDO wsUsers = wsUsersService.selectById(id);
        model.addAttribute("wsUsers", wsUsers);
        return "zhddkk/wsUsers/wsUsersForm";
    }
    
    /**
     * 保存用户账号表.
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("zhddkk:wsUsers:add")
    public Result<String> save(WsUsersDO wsUsers) {
        wsUsersService.insert(wsUsers);
        return Result.ok();
    }
    
    /**
     * 编辑用户账号表.
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("zhddkk:wsUsers:edit")
    public Result<String> update(WsUsersDO wsUsers) {
        wsUsersService.updateById(wsUsers);
        return Result.ok();
    }
    
    /**
     * 删除用户账号表.
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("zhddkk:wsUsers:remove")
    public Result<String> remove(Integer id) {
        wsUsersService.deleteById(id);
        return Result.ok();
    }
    
    /**
     * 批量删除用户账号表.
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("zhddkk:wsUsers:batchRemove")
    public Result<String> remove(@RequestParam("ids[]") Integer[] ids) {
        wsUsersService.deleteBatchIds(Arrays.asList(ids));
        return Result.ok();
    }

    /**
     * 添加好友.
     * @param friendUserId
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.INSERT, module = ModuleEnum.USER_MANAGE, subModule = "", describe = "添加好友")
    @PostMapping("/addAsFriends")
    @ResponseBody
    public Result<String> addAsFriends(Integer friendUserId) {
        String fromUserId = SessionUtil.getSessionUserId();
        WsUsersDO fromUser = wsUsersService.selectById(fromUserId);

        WsUsersDO friendUser = wsUsersService.selectById(friendUserId);
        
        logger.info(fromUser.getName() + "申请添加" + friendUser.getName() + "为好友");
        WsFriendsDO wf = new WsFriendsDO();
        wf.setUname(fromUser.getName());
        wf.setFname(friendUser.getName());
        int existCount =
            wsFriendsService.selectCount(new EntityWrapper<WsFriendsDO>().eq("uname", fromUser.getName()).eq("fname", friendUser.getName()));
        if (existCount <= 0) {
            WsFriendsApplyDO wfa = new WsFriendsApplyDO();
            wfa.setFromId(fromUser.getId());
            wfa.setFromName(fromUser.getName());
            wfa.setToId(friendUser.getId());
            wfa.setToName(friendUser.getName());
            wfa.setProcessStatus(1);
            wsFriendsApplyService.insert(wfa);
            
            return Result.ok();
        } else {
            logger.info(friendUser.getName() + "已是你的好友了,无需再次申请");
        }
        
        return Result.fail();
    }
    
    /**
     * 跳转到管理员用户列表
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.USER_MANAGE, subModule = "", describe = "用户列表页面(管理员)")
    @GetMapping("/wsUsersForAdmin")
    public String wsUsersForAdmin(Model model, String user) {
        return "zhddkk/wsUsers/wsUsersForAdmin";
    }
    
    /**
     * 获取管理员用户列表.
     */
    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.USER_MANAGE, subModule = "", describe = "用户列表(管理员)")
    @ResponseBody
    @GetMapping("/wsUsersListForAdmin")
    public Result<Page<WsUsersDO>> wsUsersListForAdmin(WsUsersDO wsUsersDTO) {
        Wrapper<WsUsersDO> wrapper = new EntityWrapper<WsUsersDO>();
        if (StringUtils.isNotBlank(wsUsersDTO.getName())) {
            wrapper.like("t1.name", wsUsersDTO.getName(), SqlLike.DEFAULT);
        }
        if (StringUtils.isNotBlank(wsUsersDTO.getState())) {
            wrapper.eq("t1.state", wsUsersDTO.getState());
        }
        if (StringUtils.isNotBlank(wsUsersDTO.getEnable())) {
            wrapper.eq("t1.enable", wsUsersDTO.getEnable());
        }
        if (StringUtils.isNotBlank(wsUsersDTO.getSpeak())) {
            wrapper.eq("t1.speak", wsUsersDTO.getSpeak());
        }
        
        Page<WsUsersDO> page = getPage(WsUsersDO.class);
        List<WsUsersDO> userList = wsUsersService.queryUserPage(page, wrapper);
        page.setRecords(userList);
        
        return Result.ok(page);
    }
    
    /**
     * 让某用户下线.
     * 
     * @param id
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.UPDATE, module = ModuleEnum.USER_MANAGE, subModule = "", describe = "使用户下线")
    @RequestMapping(value = "offlineUser.do")
    @ResponseBody
    public Result<String> offlineUser(@RequestParam("id") String id) {
        WsUsersDO wsUsersDO = wsUsersService.selectById(id);
        if (null == wsUsersDO) {
            return Result.fail("用户不存在");
        }
        String userName = wsUsersDO.getName();
        ZhddWebSocket.removeUserFromAllRoom(wsUsersDO.getName());

        wsUsersDO.setState("0");
        wsUsersDO.setLastLogoutTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        boolean updateFlag = wsUsersService.updateById(wsUsersDO);
        if (updateFlag) {
            return Result.ok();
        }
        return Result.fail();
    }
    
    /**
     * 让某用户禁用/启用.
     * 
     * @param id
     * @param status
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.UPDATE, module = ModuleEnum.USER_MANAGE, subModule = "", describe = "用户的禁用/启用")
    @RequestMapping(value = "operEnableUser.do")
    @ResponseBody
    public Result<String> operEnableUser(@RequestParam("id") String id, @RequestParam("status") String status) {
        WsUsersDO wsUsersDO = wsUsersService.selectById(id);
        if (null == wsUsersDO) {
            return Result.fail("用户不存在");
        }
        wsUsersDO.setEnable(status);
        boolean updateFlag = wsUsersService.updateById(wsUsersDO);
        if (updateFlag) {
            return Result.ok();
        }
        return Result.fail();
    }
    
    /**
     * 让某用户禁言/开言.
     * 
     * @param id
     * @param status
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.UPDATE, module = ModuleEnum.USER_MANAGE, subModule = "", describe = "用户的禁言/开言")
    @RequestMapping(value = "operSpeakUser.do")
    @ResponseBody
    public Result<String> operSpeakUser(@RequestParam("id") String id, @RequestParam("status") String status) {
        WsUsersDO wsUsersDO = wsUsersService.selectById(id);
        if (null == wsUsersDO) {
            return Result.fail("用户不存在");
        }
        wsUsersDO.setSpeak(status);
        boolean updateFlag = wsUsersService.updateById(wsUsersDO);
        if (updateFlag) {
            return Result.ok();
        }
        return Result.fail();
    }
    
    /**
     * 导出用户信息.
     *
     * @param response
     */
    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.USER_MANAGE, subModule = "", describe = "导出用户信息")
    @RequestMapping(value = "exportUser.do", method = RequestMethod.GET)
    public void exportUser(HttpServletResponse response) {
        logger.debug("开始导出用户信息");
        List<WsUsersDO> list =
            wsUsersService.selectList(new EntityWrapper<WsUsersDO>().ne("name", CommonConstants.ADMIN_USER));
        if (null != list && list.size() > 0) {
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
     * 显示用户信息.
     *
     * @param model
     * @param user
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.SETTING, subModule = "", describe = "显示用户信息首页")
    @RequestMapping(value = "showPersonalInfo.page")
    public String showPersonalInfo(Model model, @RequestParam("user") String user) {
        logger.debug("访问showPersonalInfo.page");
        model.addAttribute("user", user);
        return "zhddkk/wsUsers/showPersonalInfo";
    }
    
    /**
     * 查询个人信息.
     *
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.SETTING, subModule = "", describe = "查询个人信息")
    @RequestMapping(value = "queryPersonInfo.json", method = RequestMethod.POST)
    @ResponseBody
    public Result<WsUserProfileDO> queryPersonInfo(@RequestParam("user") String user) {
        WsUserProfileDO wsUserProfileDO =
            wsUserProfileService.selectOne(new EntityWrapper<WsUserProfileDO>().eq("user_name", user));
        if (null == wsUserProfileDO) {
            return Result.fail();
        }
        
        return Result.ok(wsUserProfileDO);
    }
    
    /**
     * 设置个人信息.
     *
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.SETTING, subModule = "", describe = "用户信息设置首页")
    @RequestMapping(value = "setPersonalInfo.page")
    public String setPersonalInfo(Model model, @RequestParam("user") String user) {
        logger.debug("访问setPersonalInfo.page");
        model.addAttribute("user", user);
        WsUserProfileDO wsUserProfileDO =
            wsUserProfileService.selectOne(new EntityWrapper<WsUserProfileDO>().eq("user_name", user));
        if (null != wsUserProfileDO) {
            String location = wsUserProfileDO.getLocation();
            if (StringUtils.isNotBlank(location) && location.contains("-")) {
                String province = location.split("-")[0];
                String city = location.split("-")[1];
                String district = location.split("-")[2];
                wsUserProfileDO.setProvince(province);
                wsUserProfileDO.setCity(city);
                wsUserProfileDO.setDistrict(district);
            }
        }
        model.addAttribute("userProfile", wsUserProfileDO);
        model.addAttribute("userProfileJson", JsonUtil.javaobject2Jsonobject(wsUserProfileDO));
        return "zhddkk/wsUsers/setPersonalInfo";
    }
    
    /**
     * 设置个人信息.
     *
     */
    @OperationLogAnnotation(type = OperationEnum.UPDATE, module = ModuleEnum.SETTING, subModule = "", describe = "设置个人信息")
    @RequestMapping(value = "setPersonInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> setPersonInfo(@RequestParam(value = "userName", required = true) String userName,
        @RequestParam(value = "realName", required = false) String realName,
        @RequestParam(value = "headImg", required = false) String headImg,
        @RequestParam(value = "sign", required = false) String sign,
        @RequestParam(value = "age", required = false) Integer age,
        @RequestParam(value = "sex", required = false) Integer sex,
        @RequestParam(value = "sexText", required = false) String sexText,
        @RequestParam(value = "tel", required = false) String tel,
        @RequestParam(value = "province", required = false) String province,
        @RequestParam(value = "city", required = false) String city,
        @RequestParam(value = "district", required = false) String district,
        @RequestParam(value = "address", required = false) String address,
        @RequestParam(value = "profession", required = false) Integer profession,
        @RequestParam(value = "professionText", required = false) String professionText,
        @RequestParam(value = "hobby", required = false) Integer hobby,
        @RequestParam(value = "hobbyText", required = false) String hobbyText) {
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", userName));
        if (null == wsUsersDO) {
            return Result.fail();
        }
        
        String location = province + "-" + city + "-" + district;
        
        // 检查表中是否有个人信息记录
        WsUserProfileDO wsUserProfileDO =
            wsUserProfileService.selectOne(new EntityWrapper<WsUserProfileDO>().eq("user_id", wsUsersDO.getId()));
        if (null == wsUserProfileDO) {
            logger.info("插入个人信息");
            WsUserProfileDO wup = new WsUserProfileDO();
            wup.setUserId(wsUsersDO.getId());
            wup.setUserName(userName);
            wup.setRealName(realName);
            wup.setImg(headImg);
            wup.setSign(sign);
            wup.setAge(age);
            wup.setSex(sex);
            wup.setSexText(sexText);
            wup.setTel(tel);
            wup.setLocation(location);
            wup.setAddress(address);
            wup.setProfession(profession);
            wup.setProfessionText(professionText);
            wup.setHobby(hobby);
            wup.setHobbyText(hobbyText);
            boolean insertFlag = wsUserProfileService.insert(wup);
            if (insertFlag) {
                return Result.ok();
            }
        } else {
            logger.info("更新个人信息");
            wsUserProfileDO.setUserName(userName);
            wsUserProfileDO.setRealName(realName);
            wsUserProfileDO.setImg(headImg);
            wsUserProfileDO.setSign(sign);
            wsUserProfileDO.setAge(age);
            wsUserProfileDO.setSex(sex);
            wsUserProfileDO.setSexText(sexText);
            wsUserProfileDO.setTel(tel);
            wsUserProfileDO.setLocation(location);
            wsUserProfileDO.setAddress(address);
            wsUserProfileDO.setProfession(profession);
            wsUserProfileDO.setProfessionText(professionText);
            wsUserProfileDO.setHobby(hobby);
            wsUserProfileDO.setHobbyText(hobbyText);
            boolean updateFlag = wsUserProfileService.updateById(wsUserProfileDO);
            if (updateFlag) {
                return Result.ok();
            }
        }
        
        return Result.fail();
    }
    
    /**
     * 更新密码.
     *
     */
    @OperationLogAnnotation(type = OperationEnum.UPDATE, module = ModuleEnum.UPDATE_PASSWORD, subModule = "", describe = "修改密码")
    @RequestMapping(value = "updatePassword.do", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> updatePassword(@RequestParam(value = "user") String user,
        @RequestParam(value = "oldPass") String oldPass, @RequestParam(value = "newPass") String newPass,
        @RequestParam(value = "confirmPass") String confirmPass) {
        if (StringUtils.isBlank(oldPass)) {
            return Result.fail("旧密码不能为空");
        }

        if (StringUtils.isBlank(newPass)) {
            return Result.fail("新密码不能为空");
        }

        if (StringUtils.isBlank(confirmPass)) {
            return Result.fail("确认密码不能为空");
        }

        if (!newPass.equals(confirmPass)) {
            return Result.fail("两次密码不一致");
        }
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
        if (null != wsUsersDO) {
            String passwordDecode = SecurityAESUtil.decryptAES(wsUsersDO.getPassword(), CommonConstants.AES_PASSWORD);
            if (!passwordDecode.equals(oldPass)) {
                return Result.fail("旧密码不正确");
            }
            
            if (passwordDecode.equals(newPass)) {
                return Result.fail("新密码不能是旧密码");
            }
            
            String passwordEncode = SecurityAESUtil.encryptAES(newPass, CommonConstants.AES_PASSWORD);
            wsUsersDO.setPassword(passwordEncode);
            wsUsersService.updateById(wsUsersDO);
            return Result.ok();
        }
        
        return Result.fail();
    }

    /**
     * 选择角色.
     *
     * @param model 对象模型
     * @param userId 用户id
     * @return
     */
    @RequestMapping(value = "/selectRole/{id}")
    public String selectRole(Model model, @PathVariable("id") Integer userId) {
        logger.debug("访问electRole");
        List<SysRoleDO> roleList = sysRoleService.selectList(null);
        SysUserRoleDO sysUserRoleDO =
            sysUserRoleService.selectOne(new EntityWrapper<SysUserRoleDO>().eq("user_id", userId));
        model.addAttribute("sysUserRoleDO", sysUserRoleDO);
        model.addAttribute("userId", userId);
        model.addAttribute("roleList", roleList);
        return "zhddkk/wsUsers/selectRole";
    }

    /**
     * 设置用户角色.
     * @param userId 用户id
     * @param roleId 角色id
     * @return result
     */
    @RequestMapping(value = "/saveRole")
    @ResponseBody
    public Result<String> saveRole(String userId, String roleId) {
        WsUsersDO wsUsersDO = wsUsersService.selectById(userId);
        if (null != wsUsersDO) {
            sysUserRoleService.delete(new EntityWrapper<SysUserRoleDO>().eq("user_id", wsUsersDO.getId()));
            SysRoleDO sysRoleDO = sysRoleService.selectById(roleId);
            if (null != sysRoleDO) {
                SysUserRoleDO sysUserRoleDO = new SysUserRoleDO();
                sysUserRoleDO.setRoleId(Integer.valueOf(roleId));
                sysUserRoleDO.setRoleName(sysRoleDO.getName());
                sysUserRoleDO.setUserId(wsUsersDO.getId());
                sysUserRoleDO.setUserName(wsUsersDO.getName());
                sysUserRoleService.insert(sysUserRoleDO);
            }
            return Result.ok();
        }
        return Result.fail();
    }
}