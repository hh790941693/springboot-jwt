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
import com.pjb.springbootjwt.zhddkk.dto.UpdatePasswordDTO;
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
import org.springframework.validation.annotation.Validated;
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
 * ???????????????
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
     * ??????????????????????????????.
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.USER_MANAGE, subModule = "", describe = "??????????????????")
    @GetMapping("/wsUsers")
    public String wsUsers() {
        return "zhddkk/wsUsers/wsUsers";
    }
    
    /**
     * ?????????????????????????????????.
     */
    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.USER_MANAGE, subModule = "", describe = "????????????")
    @ResponseBody
    @GetMapping("/wsUsersList")
    public Result<Page<WsUsersDO>> list(WsUsersDO wsUsersDTO) {
        Wrapper<WsUsersDO> wrapper = new EntityWrapper<WsUsersDO>();
        if (StringUtils.isNotBlank(wsUsersDTO.getName())) {
            wrapper.like("t1.name", wsUsersDTO.getName(), SqlLike.DEFAULT);
        }
        Page<WsUsersDO> page = getPage(WsUsersDO.class);
        List<WsUsersDO> userList = wsUsersService.queryUserPage(page, wrapper);
        page.setRecords(userList);

        String curUserName = SessionUtil.getSessionUserName();
        if (null != userList && userList.size() > 0) {
            for (WsUsersDO wu : userList) {
                if (wu.getName().equals(curUserName)) {
                    wu.setIsFriend(3);
                    continue;
                }
                wu.setIsFriend(0);
                int isMyFriend = wsFriendsService
                    .selectCount(new EntityWrapper<WsFriendsDO>().eq("uname", curUserName).eq("fname", wu.getName()));
                if (isMyFriend > 0) {
                    wu.setIsFriend(3); // ????????????
                } else {
                    // 0:?????? 1:????????? 2:????????? 3:????????????
                    List<WsFriendsApplyDO> applyList = wsFriendsApplyService.selectList(
                        new EntityWrapper<WsFriendsApplyDO>().eq("from_name", curUserName).eq("to_name", wu.getName()));
                    if (null == applyList || applyList.size() == 0) {
                        wu.setIsFriend(0); // ?????????
                    } else if (applyList.size() == 1) {
                        Integer processStatus = applyList.get(0).getProcessStatus();
                        wu.setIsFriend(processStatus); // 1:????????? 2:????????? 3:????????????
                    } else if (applyList.size() > 1) {
                        // ???????????????????????????
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
     * ????????????????????????????????????.
     */
    @GetMapping("/add")
    @RequiresPermissions("zhddkk:wsUsers:add")
    public String add(Model model) {
        WsUsersDO wsUsers = new WsUsersDO();
        model.addAttribute("wsUsers", wsUsers);
        return "zhddkk/wsUsers/wsUsersForm";
    }
    
    /**
     * ????????????????????????????????????.
     * 
     * @param id ???????????????ID
     * @param model ?????????????????????
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("zhddkk:wsUsers:edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        WsUsersDO wsUsers = wsUsersService.selectById(id);
        model.addAttribute("wsUsers", wsUsers);
        return "zhddkk/wsUsers/wsUsersForm";
    }
    
    /**
     * ?????????????????????.
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("zhddkk:wsUsers:add")
    public Result<String> save(WsUsersDO wsUsers) {
        wsUsersService.insert(wsUsers);
        return Result.ok();
    }
    
    /**
     * ?????????????????????.
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("zhddkk:wsUsers:edit")
    public Result<String> update(WsUsersDO wsUsers) {
        wsUsersService.updateById(wsUsers);
        return Result.ok();
    }
    
    /**
     * ?????????????????????.
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("zhddkk:wsUsers:remove")
    public Result<String> remove(Integer id) {
        wsUsersService.deleteById(id);
        return Result.ok();
    }
    
    /**
     * ???????????????????????????.
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("zhddkk:wsUsers:batchRemove")
    public Result<String> remove(@RequestParam("ids[]") Integer[] ids) {
        wsUsersService.deleteBatchIds(Arrays.asList(ids));
        return Result.ok();
    }

    /**
     * ????????????.
     * @param friendUserId
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.INSERT, module = ModuleEnum.USER_MANAGE, subModule = "", describe = "????????????")
    @PostMapping("/addAsFriends")
    @ResponseBody
    public Result<String> addAsFriends(Integer friendUserId) {
        String fromUserId = SessionUtil.getSessionUserId();
        WsUsersDO fromUser = wsUsersService.selectById(fromUserId);
        WsUsersDO friendUser = wsUsersService.selectById(friendUserId);
        
        logger.info(fromUser.getName() + "????????????" + friendUser.getName() + "?????????");
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
            logger.info(friendUser.getName() + "?????????????????????,??????????????????");
        }
        
        return Result.fail();
    }
    
    /**
     * ??????????????????????????????
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.USER_MANAGE, subModule = "", describe = "??????????????????(?????????)")
    @GetMapping("/wsUsersForAdmin")
    public String wsUsersForAdmin(Model model, String user) {
        return "zhddkk/wsUsers/wsUsersForAdmin";
    }
    
    /**
     * ???????????????????????????.
     */
    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.USER_MANAGE, subModule = "", describe = "????????????(?????????)")
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
     * ??????????????????.
     * 
     * @param id
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.UPDATE, module = ModuleEnum.USER_MANAGE, subModule = "", describe = "???????????????")
    @RequestMapping(value = "offlineUser.do")
    @ResponseBody
    public Result<String> offlineUser(@RequestParam("id") String id) {
        WsUsersDO wsUsersDO = wsUsersService.selectById(id);
        if (null == wsUsersDO) {
            return Result.fail("???????????????");
        }
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
     * ??????????????????/??????.
     * 
     * @param id
     * @param status
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.UPDATE, module = ModuleEnum.USER_MANAGE, subModule = "", describe = "???????????????/??????")
    @RequestMapping(value = "operEnableUser.do")
    @ResponseBody
    public Result<String> operEnableUser(@RequestParam("id") String id, @RequestParam("status") String status) {
        WsUsersDO wsUsersDO = wsUsersService.selectById(id);
        if (null == wsUsersDO) {
            return Result.fail("???????????????");
        }
        wsUsersDO.setEnable(status);
        boolean updateFlag = wsUsersService.updateById(wsUsersDO);
        if (updateFlag) {
            return Result.ok();
        }
        return Result.fail();
    }
    
    /**
     * ??????????????????/??????.
     * 
     * @param id
     * @param status
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.UPDATE, module = ModuleEnum.USER_MANAGE, subModule = "", describe = "???????????????/??????")
    @RequestMapping(value = "operSpeakUser.do")
    @ResponseBody
    public Result<String> operSpeakUser(@RequestParam("id") String id, @RequestParam("status") String status) {
        WsUsersDO wsUsersDO = wsUsersService.selectById(id);
        if (null == wsUsersDO) {
            return Result.fail("???????????????");
        }
        wsUsersDO.setSpeak(status);
        boolean updateFlag = wsUsersService.updateById(wsUsersDO);
        if (updateFlag) {
            return Result.ok();
        }
        return Result.fail();
    }
    
    /**
     * ??????????????????.
     *
     * @param response
     */
    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.USER_MANAGE, subModule = "", describe = "??????????????????")
    @RequestMapping(value = "exportUser.do", method = RequestMethod.GET)
    public void exportUser(HttpServletResponse response) {
        logger.debug("????????????????????????");
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
            ExcelUtil.exportExcel(list, "????????????", "??????", WsUsersDO.class, fileName, response);
        }
    }
    
    /**
     * ??????????????????.
     *
     * @param model
     * @param user
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.SETTING, subModule = "", describe = "????????????????????????")
    @RequestMapping(value = "showPersonalInfo.page")
    public String showPersonalInfo(Model model, @RequestParam("user") String user) {
        logger.debug("??????showPersonalInfo.page");
        model.addAttribute("user", user);
        return "zhddkk/wsUsers/showPersonalInfo";
    }

    /**
     * ??????????????????.
     *
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.SETTING, subModule = "", describe = "??????????????????")
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
     * ??????????????????.
     *
     * @return
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.SETTING, subModule = "", describe = "????????????????????????")
    @RequestMapping(value = "setPersonalInfo.page")
    public String setPersonalInfo(Model model) {
        logger.debug("??????setPersonalInfo.page");
        String userId = SessionUtil.getSessionUserId();
        WsUserProfileDO wsUserProfileDO =
            wsUserProfileService.selectOne(new EntityWrapper<WsUserProfileDO>().eq("user_id", userId));
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
     * ??????????????????.
     *
     */
    @OperationLogAnnotation(type = OperationEnum.UPDATE, module = ModuleEnum.SETTING, subModule = "", describe = "??????????????????")
    @RequestMapping(value = "setPersonInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> setPersonInfo(
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
        String userId = SessionUtil.getSessionUserId();
        WsUsersDO wsUsersDO = wsUsersService.selectById(userId);
        if (null == wsUsersDO) {
            return Result.fail();
        }
        
        String location = province + "-" + city + "-" + district;
        
        // ???????????????????????????????????????
        WsUserProfileDO wsUserProfileDO =
            wsUserProfileService.selectOne(new EntityWrapper<WsUserProfileDO>().eq("user_id", wsUsersDO.getId()));
        if (null == wsUserProfileDO) {
            logger.info("??????????????????");
            WsUserProfileDO wup = new WsUserProfileDO();
            wup.setUserId(wsUsersDO.getId());
            wup.setUserName(wsUsersDO.getName());
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
            logger.info("??????????????????");
            wsUserProfileDO.setUserName(wsUsersDO.getName());
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
     * ????????????.
     *
     */
    @OperationLogAnnotation(type = OperationEnum.UPDATE, module = ModuleEnum.UPDATE_PASSWORD, subModule = "", describe = "????????????")
    @RequestMapping(value = "updatePassword.do", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> updatePassword(@Validated UpdatePasswordDTO updatePasswordDTO) {

        String user = updatePasswordDTO.getUser();
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
        if (null != wsUsersDO) {
            String passwordDecode = SecurityAESUtil.decryptAES(wsUsersDO.getPassword(), CommonConstants.AES_PASSWORD);
            if (!passwordDecode.equals(updatePasswordDTO.getOldPass())) {
                return Result.fail("??????????????????");
            }
            
            if (passwordDecode.equals(updatePasswordDTO.getNewPass())) {
                return Result.fail("???????????????????????????");
            }
            
            String passwordEncode = SecurityAESUtil.encryptAES(updatePasswordDTO.getNewPass(), CommonConstants.AES_PASSWORD);
            wsUsersDO.setPassword(passwordEncode);
            wsUsersService.updateById(wsUsersDO);
            return Result.ok();
        }
        
        return Result.fail();
    }

    /**
     * ????????????.
     *
     * @param model ????????????
     * @param userId ??????id
     * @return
     */
    @RequestMapping(value = "/selectRole/{id}")
    public String selectRole(Model model, @PathVariable("id") Integer userId) {
        logger.debug("??????electRole");
        List<SysRoleDO> roleList = sysRoleService.selectList(null);
        SysUserRoleDO sysUserRoleDO =
            sysUserRoleService.selectOne(new EntityWrapper<SysUserRoleDO>().eq("user_id", userId));
        model.addAttribute("sysUserRoleDO", sysUserRoleDO);
        model.addAttribute("userId", userId);
        model.addAttribute("roleList", roleList);
        return "zhddkk/wsUsers/selectRole";
    }

    /**
     * ??????????????????.
     * @param userId ??????id
     * @param roleId ??????id
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