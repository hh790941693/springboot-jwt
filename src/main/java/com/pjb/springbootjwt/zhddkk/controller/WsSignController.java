package com.pjb.springbootjwt.zhddkk.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import com.pjb.springbootjwt.zhddkk.enumx.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.enumx.OperationEnum;
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import com.pjb.springbootjwt.zhddkk.util.TimeUtil;
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
import com.pjb.springbootjwt.zhddkk.domain.WsSignDO;
import com.pjb.springbootjwt.zhddkk.service.WsSignService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户签到表
 */
@Controller
@RequestMapping("/zhddkk/wsSign")
public class WsSignController extends AdminBaseController {

    private static Logger logger = LoggerFactory.getLogger(WsSignController.class);

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
    * 跳转到用户签到表页面
	*/
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.SIGN,subModule="",describe="用户签到页面")
	@GetMapping()
	//@RequiresPermissions("zhddkk:wsSign:wsSign")
	String wsSign(Model model, String user){
	    model.addAttribute("user", user);
	    return "zhddkk/wsSign/wsSign";
	}

    /**
     * 获取用户签到表列表数据
     */
	@ResponseBody
	@GetMapping("/list")
	//@RequiresPermissions("zhddkk:wsSign:wsSign")
	public Result<Page<WsSignDO>> list(String userName){
        Wrapper<WsSignDO> wrapper = new EntityWrapper<WsSignDO>();
        wrapper.eq("user_name", userName).orderBy("create_time", false);
        Page<WsSignDO> page = wsSignService.selectPage(getPage(WsSignDO.class), wrapper);
        return Result.ok(page);
	}

    @OperationLogAnnotation(type=OperationEnum.INSERT,module=ModuleEnum.SIGN,subModule="",describe="用户签到")
	@ResponseBody
    @PostMapping("/sign")
	public Result<String> sign(String user){
        if (StringUtils.isBlank(user)){
            return Result.fail();
        }
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
        if (null == wsUsersDO){
            return Result.fail("用户不存在");
        }
        Date dayStart = TimeUtil.getDayStart(new Date());
        Date dayEnd = TimeUtil.getDayEnd(new Date());
        int signCnt = wsSignService.selectCount(new EntityWrapper<WsSignDO>().eq("user_id", wsUsersDO.getId()).
                ge("create_time", dayStart).lt("create_time", dayEnd));
	    if (signCnt>0){
	        return Result.build(Result.CODE_FAIL, "已经签到过呐");
        }
        WsSignDO wsSignDO = new WsSignDO();
        wsSignDO.setUserId(wsUsersDO.getId());
        wsSignDO.setUserName(wsUsersDO.getName());
        wsSignDO.setCreateTime(new Date());
        boolean insertFlag = wsSignService.insert(wsSignDO);
        if (insertFlag){
            wsUsersDO.setCoinNum(wsUsersDO.getCoinNum()+10);
            wsUsersService.updateById(wsUsersDO);
            return Result.build(Result.CODE_SUCCESS, "签到成功");
        }
        return Result.fail();
    }

    /**
     * 跳转到用户签到表添加页面
     */
	@GetMapping("/add")
	//@RequiresPermissions("zhddkk:wsSign:add")
	String add(Model model){
		WsSignDO wsSign = new WsSignDO();
        model.addAttribute("wsSign", wsSign);
	    return "zhddkk/wsSign/wsSignForm";
	}

    /**
     * 跳转到用户签到表编辑页面
     * @param id 用户签到表ID
     * @param model 用户签到表实体
     */
	@GetMapping("/edit/{id}")
	//@RequiresPermissions("zhddkk:wsSign:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		WsSignDO wsSign = wsSignService.selectById(id);
		model.addAttribute("wsSign", wsSign);
	    return "zhddkk/wsSign/wsSignForm";
	}
	
	/**
	 * 保存用户签到表
	 */
	@ResponseBody
	@PostMapping("/save")
	//@RequiresPermissions("zhddkk:wsSign:add")
	public Result<String> save( WsSignDO wsSign){
		wsSignService.insert(wsSign);
        return Result.ok();
	}

	/**
	 * 编辑用户签到表
	 */
	@ResponseBody
	@RequestMapping("/update")
	//@RequiresPermissions("zhddkk:wsSign:edit")
	public Result<String> update( WsSignDO wsSign){
		wsSignService.updateById(wsSign);
		return Result.ok();
	}
	
	/**
	 * 删除用户签到表
	 */
	@PostMapping("/remove")
	@ResponseBody
	//@RequiresPermissions("zhddkk:wsSign:remove")
	public Result<String> remove( Integer id){
		wsSignService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 批量删除用户签到表
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	//@RequiresPermissions("zhddkk:wsSign:batchRemove")
	public Result<String> remove(@RequestParam("ids[]") Integer[] ids){
		wsSignService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}
	
}
