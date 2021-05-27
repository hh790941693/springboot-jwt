package com.pjb.springbootjwt.zhddkk.controller;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.constants.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.constants.OperationEnum;
import com.pjb.springbootjwt.zhddkk.domain.WsSignDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import com.pjb.springbootjwt.zhddkk.service.WsSignService;
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import com.pjb.springbootjwt.zhddkk.util.SessionUtil;
import com.pjb.springbootjwt.zhddkk.util.TimeUtil;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * 用户签到表.
 */
@Controller
@RequestMapping("/zhddkk/wsSign")
public class WsSignController extends AdminBaseController {

    private static final Logger logger = LoggerFactory.getLogger(WsSignController.class);

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
    private WsSignService wsSignService;

    @Autowired
    private WsUsersService wsUsersService;

    /**
    * 跳转到用户签到表页面.
    */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.SIGN, subModule = "", describe = "用户签到页面")
    @GetMapping()
    //@RequiresPermissions("zhddkk:wsSign:wsSign")
    public String wsSign() {
        logger.info("用户签到页面");
        return "zhddkk/wsSign/wsSign";
    }

    /**
     * 获取用户签到表列表数据.
     */
    @ResponseBody
    @GetMapping("/list")
    //@RequiresPermissions("zhddkk:wsSign:wsSign")
    public Result<Page<WsSignDO>> list() {
        Wrapper<WsSignDO> wrapper = new EntityWrapper<WsSignDO>();
        String userName = SessionUtil.getSessionUserName();
        wrapper.eq("user_name", userName).orderBy("create_time", false);
        Page<WsSignDO> page = wsSignService.selectPage(getPage(WsSignDO.class), wrapper);
        return Result.ok(page);
    }

    /**
     * 签到.
     * @return 签到结果
     */
    @OperationLogAnnotation(type = OperationEnum.INSERT, module = ModuleEnum.SIGN, subModule = "", describe = "用户签到")
    @ResponseBody
    @PostMapping("/sign")
    public Result<String> sign() {
        String userId = SessionUtil.getSessionUserId();
        WsUsersDO wsUsersDO = wsUsersService.selectById(userId);
        if (null == wsUsersDO) {
            return Result.fail("用户不存在");
        }
        Date dayStart = TimeUtil.getDayStart(new Date());
        Date dayEnd = TimeUtil.getDayEnd(new Date());
        int signCnt = wsSignService.selectCount(new EntityWrapper<WsSignDO>()
                .eq("user_id", wsUsersDO.getId())
                .ge("create_time", dayStart).lt("create_time", dayEnd));
        if (signCnt > 0) {
            return Result.build(Result.CODE_FAIL, "已经签到过呐");
        }
        WsSignDO wsSignDO = new WsSignDO();
        wsSignDO.setUserId(wsUsersDO.getId());
        wsSignDO.setUserName(wsUsersDO.getName());
        wsSignDO.setCreateTime(new Date());
        boolean insertFlag = wsSignService.insert(wsSignDO);
        if (insertFlag) {
            wsUsersDO.setCoinNum(wsUsersDO.getCoinNum() + 10);
            wsUsersService.updateById(wsUsersDO);
            return Result.build(Result.CODE_SUCCESS, "签到成功");
        }
        return Result.fail();
    }

    /**
     * 跳转到用户签到表添加页面.
     */
    @GetMapping("/add")
    //@RequiresPermissions("zhddkk:wsSign:add")
    public String add(Model model) {
        WsSignDO wsSign = new WsSignDO();
        model.addAttribute("wsSign", wsSign);
        return "zhddkk/wsSign/wsSignForm";
    }

    /**
     * 跳转到用户签到表编辑页面.
     * @param id 用户签到表ID
     * @param model 用户签到表实体
     */
    @GetMapping("/edit/{id}")
    //@RequiresPermissions("zhddkk:wsSign:edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        WsSignDO wsSign = wsSignService.selectById(id);
        model.addAttribute("wsSign", wsSign);
        return "zhddkk/wsSign/wsSignForm";
    }

    /**
     * 保存用户签到表.
     */
    @ResponseBody
    @PostMapping("/save")
    //@RequiresPermissions("zhddkk:wsSign:add")
    public Result<String> save(WsSignDO wsSign) {
        wsSignService.insert(wsSign);
        return Result.ok();
    }

    /**
     * 编辑用户签到表.
     */
    @ResponseBody
    @RequestMapping("/update")
    //@RequiresPermissions("zhddkk:wsSign:edit")
    public Result<String> update(WsSignDO wsSign) {
        wsSignService.updateById(wsSign);
        return Result.ok();
    }

    /**
     * 删除用户签到表.
     */
    @PostMapping("/remove")
    @ResponseBody
    //@RequiresPermissions("zhddkk:wsSign:remove")
    public Result<String> remove(Integer id) {
        wsSignService.deleteById(id);
        return Result.ok();
    }

    /**
     * 批量删除用户签到表.
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    //@RequiresPermissions("zhddkk:wsSign:batchRemove")
    public Result<String> remove(@RequestParam("ids[]") Integer[] ids) {
        wsSignService.deleteBatchIds(Arrays.asList(ids));
        return Result.ok();
    }
}
