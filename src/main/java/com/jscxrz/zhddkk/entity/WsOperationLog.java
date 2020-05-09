package com.jscxrz.zhddkk.entity;

import java.util.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 操作日志
 * 
 * @author Administrator
 *
 */
public class WsOperationLog extends Page{
	private Integer id;
	private Integer userId;            //用户id
	@Excel(name = "用户名称",orderNum = "1",width=10,fixedIndex=1)
	private String userName;           //用户名称
	@Excel(name = "操作类型",orderNum = "2",width=10,fixedIndex=2)
	private String operType;           //操作类型
	@Excel(name = "操作模块",orderNum = "3",width=10,fixedIndex=3)
	private String operModule;         //操作模块
	private String operSubmodule;      //操作子模块
	@Excel(name = "操作描述",orderNum = "4",width=25)
	private String operDescribe;       //操作描述
	private String operRemark;         //操作备注
	@Excel(name = "请求地址",orderNum = "5",width=30)
	private String requestUrl;         //请求地址
	@Excel(name = "耗时(ms)",orderNum = "6",width=10,type=10)
	private int costTime;              //花费时间
	@Excel(name = "类名",orderNum = "7",width=30)
	private String className;          //类名  
	@Excel(name = "方法名",orderNum = "8",width=20)
	private String methodName;         //方法名
	@Excel(name = "参数列表",orderNum = "9",width=20)
	private String parameters;         //参数列表
	@Excel(name = "操作结果",orderNum = "11",width=100)
	private String operResult;         //操作结果
	private String errorMsg;           //失败结果
	private Date accessTime;           //访问时间
	private String accessTimeText;
	@Excel(name = "操作时间",orderNum = "10",format = "yyyy-MM-dd HH:mm:ss", width=23)
	private Date createTime;           //创建时间
	private String createTimeText;

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
	public int getCostTime() {
		return costTime;
	}
	public void setCostTime(int costTime) {
		this.costTime = costTime;
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
	public String getAccessTimeText() {
		return accessTimeText;
	}
	public void setAccessTimeText(String accessTimeText) {
		this.accessTimeText = accessTimeText;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateTimeText() {
		return createTimeText;
	}
	public void setCreateTimeText(String createTimeText) {
		this.createTimeText = createTimeText;
	}
	public String getOperDescribe() {
		return operDescribe;
	}
	public void setOperDescribe(String operDescribe) {
		this.operDescribe = operDescribe;
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
	@Override
	public String toString() {
		return "WsOperationLog [id=" + id + ", userId=" + userId + ", userName=" + userName + ", operType=" + operType
				+ ", operModule=" + operModule + ", operSubmodule=" + operSubmodule + ", operDescribe=" + operDescribe
				+ ", operRemark=" + operRemark + ", requestUrl=" + requestUrl + ", costTime=" + costTime
				+ ", className=" + className + ", methodName=" + methodName + ", parameters=" + parameters
				+ ", operResult=" + operResult + ", errorMsg=" + errorMsg + ", accessTime=" + accessTime
				+ ", accessTimeText=" + accessTimeText + ", createTime=" + createTime + ", createTimeText="
				+ createTimeText + "]";
	}
}
