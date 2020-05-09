package com.jscxrz.zhddkk.swagger;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.jscxrz.zhddkk.base.Result;
import com.jscxrz.zhddkk.entity.WsOperationLogExt;
import com.jscxrz.zhddkk.service.WsOperationLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 操作日志,主要记录增删改的日志 前端控制器
 * </p>
 *
 * @author huangchaohui
 * @since 2019-02-22
 */
@Controller
@RequestMapping("/zhddkk/wsOperationLog")
@Api("操作日志接口")
public class WsOperationLogController {
	
	@Autowired
	private WsOperationLogService wsOperationLogService;
	
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	
	@ApiOperation(value = "操作日志查询", notes = "操作日志查询接口")
    @ResponseBody
    @RequestMapping(value = "getOperationLogByPage", method = RequestMethod.GET)
    public Result<List<WsOperationLogExt>> getOperationLogByPage(
    		@ApiParam(name = "start", value = "当前页码", required = true, defaultValue="1") @RequestParam("start") Integer start,
    		@ApiParam(name = "length", value = "每页长度", required = true, defaultValue="10") @RequestParam("length") Integer length,
    		@ApiParam(name = "user_name", value = "用户名", required = false) @RequestParam(value="user_name",required=false) String userName,
    		@ApiParam(name = "oper_type", value = "操作类型", required = false) @RequestParam(value="oper_type",required=false) String operType) {
		String redisValue = (String)redisTemplate.opsForValue().get("myname");
		System.out.println("redisValue:"+redisValue);

		Page<WsOperationLogExt> page = new Page<WsOperationLogExt>(start,length);
		Wrapper<WsOperationLogExt> wrapper = new EntityWrapper<WsOperationLogExt>();
		if (StringUtils.isNotEmpty(userName)) {
			wrapper.eq("user_name", userName);
		}
		if (StringUtils.isNotEmpty(operType)) {
			wrapper.eq("oper_type", operType);
		}
		List<WsOperationLogExt> list = wsOperationLogService.selectPage(page, wrapper).getRecords();
        return Result.ok(list);
    }
}

