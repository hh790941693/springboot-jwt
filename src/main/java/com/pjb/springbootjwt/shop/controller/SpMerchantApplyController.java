package com.pjb.springbootjwt.shop.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.pjb.springbootjwt.zhddkk.util.SessionUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
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
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.shop.domain.SpMerchantApplyDO;
import com.pjb.springbootjwt.shop.domain.SpMerchantDO;
import com.pjb.springbootjwt.shop.service.SpMerchantApplyService;
import com.pjb.springbootjwt.shop.service.SpMerchantService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 申请成为商家表.
 */
@Controller
@RequestMapping("/shop/spMerchantApply")
public class SpMerchantApplyController extends AdminBaseController {

    private static final Logger logger = LoggerFactory.getLogger(SpMerchantApplyController.class);

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
	private SpMerchantApplyService spMerchantApplyService;

	@Autowired
	private SpMerchantService spMerchantService;

    /**
    * 跳转到申请成为商家表页面.
	*/
	@GetMapping()
	//@RequiresPermissions("shop:spMerchantApply:spMerchantApply")
    public String spMerchantApply(){
	    return "shop/spMerchantApply/spMerchantApply";
	}

    /**
     * 获取申请成为商家表列表数据.
     */
	@ResponseBody
	@GetMapping("/list")
	//@RequiresPermissions("shop:spMerchantApply:spMerchantApply")
	public Result<Page<SpMerchantApplyDO>> list(SpMerchantApplyDO spMerchantApplyDto) {
        Wrapper<SpMerchantApplyDO> wrapper = new EntityWrapper<SpMerchantApplyDO>();
        if (StringUtils.isNotBlank(spMerchantApplyDto.getName())) {
        	wrapper.like("name", spMerchantApplyDto.getName());
		}
        System.out.println(spMerchantApplyDto.getStatus());
        if (null != spMerchantApplyDto.getStatus()) {
			wrapper.eq("status", spMerchantApplyDto.getStatus());
		}
        Page<SpMerchantApplyDO> page = spMerchantApplyService.selectPage(getPage(SpMerchantApplyDO.class), wrapper);
        return Result.ok(page);
	}

    /**
     * 跳转到申请成为商家表添加页面.
     */
	@GetMapping("/add")
	//@RequiresPermissions("shop:spMerchantApply:add")
    public String add(Model model) {
		SpMerchantApplyDO spMerchantApply = new SpMerchantApplyDO();
        model.addAttribute("spMerchantApply", spMerchantApply);
	    return "shop/spMerchantApply/spMerchantApplyForm";
	}

    /**
     * 跳转到申请成为商家表编辑页面.
     * @param id 申请成为商家表ID
     * @param model 申请成为商家表实体
     */
	@GetMapping("/edit/{id}")
	//@RequiresPermissions("shop:spMerchantApply:edit")
    public String edit(@PathVariable("id") Long id, Model model) {
		SpMerchantApplyDO spMerchantApply = spMerchantApplyService.selectById(id);
		model.addAttribute("spMerchantApply", spMerchantApply);
	    return "shop/spMerchantApply/spMerchantApplyForm";
	}
	
	/**
	 * 保存申请成为商家表.
	 */
	@ResponseBody
	@PostMapping("/save")
	@Transactional(rollbackFor = Exception.class)
	//@RequiresPermissions("shop:spMerchantApply:add")
	public Result<String> save(SpMerchantApplyDO spMerchantApply) {
		String userId = SessionUtil.getSessionUserId();
		List<SpMerchantApplyDO> spMerchantApplyDOList = spMerchantApplyService.selectList(new EntityWrapper<SpMerchantApplyDO>().eq("user_id", userId));
        if (null != spMerchantApplyDOList && spMerchantApplyDOList.size() > 0) {
        	for (SpMerchantApplyDO spMerchantApplyDO : spMerchantApplyDOList) {
        		if (spMerchantApplyDO.getStatus().intValue() == 1) {
        			// 待审批
					return Result.fail("尚有待审批的订单,请不要重复申请。");
				}
        		if (spMerchantApplyDO.getStatus().intValue() == 2) {
        			// 审批通过
					return Result.fail("你已经是商家了,请不要重复申请。");
				}
			}
		}
		String applyNo = "mer_apy_" + UUID.randomUUID().toString().replaceAll("-","");
        spMerchantApply.setApplyNo(applyNo);
        spMerchantApply.setUserId(Long.valueOf(SessionUtil.getSessionUserId()));
		spMerchantApply.setCreateTime(new Date());
		spMerchantApplyService.insert(spMerchantApply);
        return Result.ok();
	}

	/**
	 * 编辑申请成为商家表.
	 */
	@ResponseBody
	@RequestMapping("/update")
	//@RequiresPermissions("shop:spMerchantApply:edit")
	public Result<String> update(SpMerchantApplyDO spMerchantApply) {
		spMerchantApplyService.updateById(spMerchantApply);
		return Result.ok();
	}
	
	/**
	 * 删除申请成为商家表.
	 */
	@PostMapping("/remove")
	@ResponseBody
	//@RequiresPermissions("shop:spMerchantApply:remove")
	public Result<String> remove(Long id) {
		spMerchantApplyService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 批量删除申请成为商家表.
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	//@RequiresPermissions("shop:spMerchantApply:batchRemove")
	public Result<String> remove(@RequestParam("ids[]") Long[] ids) {
		spMerchantApplyService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}

    /**
     * 审批商家的申请
     * @param id      申请记录id
     * @param status  状态
     * @return
     */
    @ResponseBody
    @PostMapping("/updateStatus")
    @Transactional(rollbackFor = Exception.class)
    public Result<String> list(int id, int status) {
        SpMerchantApplyDO spMerchantApplyDO = spMerchantApplyService.selectById(id);
        if (null == spMerchantApplyDO) {
            return Result.fail("申请记录不存在");
        }
        spMerchantApplyDO.setStatus(status);
		spMerchantApplyDO.setUpdateTime(new Date());
        spMerchantApplyService.updateById(spMerchantApplyDO);
        if (status == 2) {
            // 审批通过
            SpMerchantDO spMerchantDO = new SpMerchantDO();
            BeanUtils.copyProperties(spMerchantApplyDO, spMerchantDO);
            spMerchantDO.setId(null);
            spMerchantDO.setStatus(1);
            spMerchantDO.setCreateTime(new Date());
            spMerchantDO.setUpdateTime(new Date());
            String merchantId = "mer_" + UUID.randomUUID().toString().replaceAll("-", "");
            spMerchantDO.setMerchantId(merchantId);
            spMerchantService.insert(spMerchantDO);
        }
        return Result.ok();
    }
}