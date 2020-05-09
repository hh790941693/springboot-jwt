package com.pjb.springbootjwt.zhddkk.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 操作日志,主要记录增删改的日志
 * </p>
 *
 * @author huangchaohui
 * @since 2019-02-22
 */
@TableName("ws_operation_log")
public class WsOperationLogExt extends Model<WsOperationLogExt> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 操作人ID
     */
	@TableField("user_id")
	private Integer userId;
    /**
     * 操作人姓名
     */
	@TableField("user_name")
	private String userName;
    /**
     * 操作类型(增|删|改)
     */
	@TableField("oper_type")
	private String operType;
    /**
     * 操作模块
     */
	@TableField("oper_module")
	private String operModule;
    /**
     * 操作子模块
     */
	@TableField("oper_submodule")
	private String operSubmodule;
    /**
     * 操作描述
     */
	@TableField("oper_describe")
	private String operDescribe;
    /**
     * 操作描述
     */
	@TableField("oper_remark")
	private String operRemark;
    /**
     * 请求路径
     */
	@TableField("request_url")
	private String requestUrl;
    /**
     * 耗时(毫秒)
     */
	@TableField("cost_time")
	private Integer costTime;
    /**
     * 类名
     */
	@TableField("class_name")
	private String className;
    /**
     * 方法名
     */
	@TableField("method_name")
	private String methodName;
    /**
     * 参数列表
     */
	private String parameters;
    /**
     * 操作结果
     */
	@TableField("oper_result")
	private String operResult;
    /**
     * 错误信息
     */
	@TableField("error_msg")
	private String errorMsg;
    /**
     * 访问时间
     */
	@TableField("access_time")
	private Date accessTime;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getOperModule() {
		return operModule;
	}

	public void setOperModule(String operModule) {
		this.operModule = operModule;
	}

	public String getOperSubmodule() {
		return operSubmodule;
	}

	public void setOperSubmodule(String operSubmodule) {
		this.operSubmodule = operSubmodule;
	}

	public String getOperDescribe() {
		return operDescribe;
	}

	public void setOperDescribe(String operDescribe) {
		this.operDescribe = operDescribe;
	}

	public String getOperRemark() {
		return operRemark;
	}

	public void setOperRemark(String operRemark) {
		this.operRemark = operRemark;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public Integer getCostTime() {
		return costTime;
	}

	public void setCostTime(Integer costTime) {
		this.costTime = costTime;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getOperResult() {
		return operResult;
	}

	public void setOperResult(String operResult) {
		this.operResult = operResult;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Date getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Date accessTime) {
		this.accessTime = accessTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "WsOperationLog{" +
			"id=" + id +
			", userId=" + userId +
			", userName=" + userName +
			", operType=" + operType +
			", operModule=" + operModule +
			", operSubmodule=" + operSubmodule +
			", operDescribe=" + operDescribe +
			", operRemark=" + operRemark +
			", requestUrl=" + requestUrl +
			", costTime=" + costTime +
			", className=" + className +
			", methodName=" + methodName +
			", parameters=" + parameters +
			", operResult=" + operResult +
			", errorMsg=" + errorMsg +
			", accessTime=" + accessTime +
			", createTime=" + createTime +
			"}";
	}
}
