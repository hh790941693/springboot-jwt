package com.pjb.springbootjwt.zhddkk.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import com.pjb.springbootjwt.zhddkk.constants.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.constants.OperationEnum;
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import com.pjb.springbootjwt.zhddkk.util.ExcelUtil;
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
import com.pjb.springbootjwt.zhddkk.domain.WsOperationLogDO;
import com.pjb.springbootjwt.zhddkk.service.WsOperationLogService;
import com.pjb.springbootjwt.zhddkk.base.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

/**
 * 操作日志,主要记录增删改的日志
 */
@Controller
@RequestMapping("/zhddkk/wsOperationLog")
public class WsOperationLogController extends AdminBaseController {

    private static Logger logger = LoggerFactory.getLogger(WsOperationLogController.class);

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @Autowired
    private WsOperationLogService wsOperationLogService;

    @Autowired
    private WsUsersService wsUsersService;

    /**
    * 跳转到操作日志,主要记录增删改的日志页面
    */
    @OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.OPERATION_LOG_MANAGE,subModule="",describe="操作日志列表页面")
    @GetMapping()
    //@RequiresPermissions("zhddkk:wsOperationLog:wsOperationLog")
    public String wsOperationLog(Model model){
        //用户列表
        List<WsUsersDO> allUserList = wsUsersService.selectList(new EntityWrapper<WsUsersDO>().ne("name", CommonConstants.ADMIN_USER)
                .orderBy("name"));
        model.addAttribute("allUserList", allUserList);

        //模块列表
        ModuleEnum[] values = ModuleEnum.values();
        List<String> moduleList = new ArrayList<String>();
        for (ModuleEnum me : values) {
            moduleList.add(me.getValue());
        }
        model.addAttribute("moduleList", moduleList);

        model.addAttribute("wsOperationLogDO", new WsOperationLogDO());
        return "zhddkk/wsOperationLog/wsOperationLog";
    }

    /**
     * 获取操作日志,主要记录增删改的日志列表数据
     */
    //@OperationLogAnnotation(type=OperationEnum.QUERY,module=ModuleEnum.OPERATION_LOG_MANAGE,subModule="",describe="操作日志列表")
    @ResponseBody
    @GetMapping("/list")
    //@RequiresPermissions("zhddkk:wsOperationLog:wsOperationLog")
    public Result<Page<WsOperationLogDO>> list(WsOperationLogDO wsOperationLogDTO){
        Wrapper<WsOperationLogDO> wrapper = new EntityWrapper<WsOperationLogDO>();
        if (wsOperationLogDTO.getUserId() != null){
            wrapper.eq("user_id", wsOperationLogDTO.getUserId());
        }
        if (StringUtils.isNotBlank(wsOperationLogDTO.getOperModule())){
            wrapper.eq("oper_module", wsOperationLogDTO.getOperModule());
        }
        wrapper.orderBy("create_time", false);
        Page<WsOperationLogDO> page = wsOperationLogService.selectPage(getPage(WsOperationLogDO.class), wrapper);
        return Result.ok(page);
    }

    /**
     * 跳转到操作日志,主要记录增删改的日志添加页面
     */
    @GetMapping("/add")
    //@RequiresPermissions("zhddkk:wsOperationLog:add")
    public String add(Model model){
        WsOperationLogDO wsOperationLog = new WsOperationLogDO();
        model.addAttribute("wsOperationLog", wsOperationLog);
        return "zhddkk/wsOperationLog/wsOperationLogForm";
    }

    /**
     * 跳转到操作日志,主要记录增删改的日志编辑页面
     * @param id 操作日志,主要记录增删改的日志ID
     * @param model 操作日志,主要记录增删改的日志实体
     */
    @GetMapping("/edit/{id}")
    //@RequiresPermissions("zhddkk:wsOperationLog:edit")
    public String edit(@PathVariable("id") Integer id,Model model){
        WsOperationLogDO wsOperationLog = wsOperationLogService.selectById(id);
        model.addAttribute("wsOperationLog", wsOperationLog);
        return "zhddkk/wsOperationLog/wsOperationLogForm";
    }

    /**
     * 保存操作日志,主要记录增删改的日志
     */
    @ResponseBody
    @PostMapping("/save")
    //@RequiresPermissions("zhddkk:wsOperationLog:add")
    public Result<String> save( WsOperationLogDO wsOperationLog){
        wsOperationLogService.insert(wsOperationLog);
        return Result.ok();
    }

    /**
     * 编辑操作日志,主要记录增删改的日志
     */
    @ResponseBody
    @RequestMapping("/update")
    //@RequiresPermissions("zhddkk:wsOperationLog:edit")
    public Result<String> update( WsOperationLogDO wsOperationLog){
        wsOperationLogService.updateById(wsOperationLog);
        return Result.ok();
    }

    /**
     * 删除操作日志,主要记录增删改的日志
     */
    @PostMapping("/remove")
    @ResponseBody
    //@RequiresPermissions("zhddkk:wsOperationLog:remove")
    public Result<String> remove( Integer id){
        wsOperationLogService.deleteById(id);
        return Result.ok();
    }

    /**
     * 批量删除操作日志,主要记录增删改的日志
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    //@RequiresPermissions("zhddkk:wsOperationLog:batchRemove")
    public Result<String> remove(@RequestParam("ids[]") Integer[] ids){
        wsOperationLogService.deleteBatchIds(Arrays.asList(ids));
        return Result.ok();
    }

    /**
     * 清空操作日志
     * @return
     */
    @PostMapping("/clearOperationLog.do")
    @ResponseBody
    public Result<String> clearOperationLog(){
        wsOperationLogService.delete(null);
        return Result.ok();
    }

    /**
     * 导出操作日志信息
     *
     * @param response
     */
    @OperationLogAnnotation(type= OperationEnum.EXPORT,module=ModuleEnum.OPERATION_LOG_MANAGE,subModule="",describe="导出操作日志")
    @RequestMapping(value = "exportOperateLog.do", method = RequestMethod.GET)
    public void exportOperateLog(String userId, String operModule, HttpServletResponse response){
        logger.debug("开始导出操作日志");

        Wrapper<WsOperationLogDO> wrapper = new EntityWrapper<WsOperationLogDO>();
        if (StringUtils.isNotBlank(userId)){
            wrapper.eq("user_id", userId);
        }
        if (StringUtils.isNotBlank(operModule)){
            wrapper.eq("oper_module", operModule);
        }
        List<WsOperationLogDO> list = wsOperationLogService.selectList(wrapper);
        if (null != list && list.size()>0) {
            for (WsOperationLogDO woltmp : list) {
                if (StringUtils.isNotBlank(woltmp.getOperResult()) && woltmp.getOperResult().length()>32000) {
                    woltmp.setOperResult(woltmp.getOperResult().substring(0, 32000));
                }
            }
            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String fileName = "wsOperationLog".concat("_").concat(time).concat(".xls");
            ExcelUtil.exportExcel(list, "操作日志", "操作日志", WsOperationLogDO.class, fileName, response);
        }
    }
}
