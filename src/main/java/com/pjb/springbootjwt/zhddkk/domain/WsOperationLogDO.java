package com.pjb.springbootjwt.zhddkk.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;


/**
 * 操作日志,主要记录增删改的日志
 */
 @TableName("ws_operation_log")
public class WsOperationLogDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    //主键
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id",name = "主键")
    private Integer id;
    //操作人ID
    @ApiModelProperty(value = "userId",name = "操作人ID")
    private Integer userId;
    //操作人姓名
    @ApiModelProperty(value = "userName",name = "操作人姓名")
    private String userName;
    //操作类型(增|删|改)
    @ApiModelProperty(value = "operType",name = "操作类型(增|删|改)")
    private String operType;
    //操作模块
    @ApiModelProperty(value = "operModule",name = "操作模块")
    private String operModule;
    //操作子模块
    @ApiModelProperty(value = "operSubmodule",name = "操作子模块")
    private String operSubmodule;
    //操作描述
    @ApiModelProperty(value = "operDescribe",name = "操作描述")
    private String operDescribe;
    //操作描述
    @ApiModelProperty(value = "operRemark",name = "操作描述")
    private String operRemark;
    //请求路径
    @ApiModelProperty(value = "requestUrl",name = "请求路径")
    private String requestUrl;
    //耗时(毫秒)
    @ApiModelProperty(value = "costTime",name = "耗时(毫秒)")
    private Integer costTime;
    //类名
    @ApiModelProperty(value = "className",name = "类名")
    private String className;
    //方法名
    @ApiModelProperty(value = "methodName",name = "方法名")
    private String methodName;
    //参数列表
    @ApiModelProperty(value = "parameters",name = "参数列表")
    private String parameters;
    //操作结果
    @ApiModelProperty(value = "operResult",name = "操作结果")
    private String operResult;
    //错误信息
    @ApiModelProperty(value = "errorMsg",name = "错误信息")
    private String errorMsg;
    //访问时间
    @ApiModelProperty(value = "accessTime",name = "访问时间")
    private Date accessTime;
    //创建时间
    @ApiModelProperty(value = "createTime",name = "创建时间")
    private Date createTime;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }


    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getOperType() {
        return operType;
    }


    public void setOperModule(String operModule) {
        this.operModule = operModule;
    }

    public String getOperModule() {
        return operModule;
    }


    public void setOperSubmodule(String operSubmodule) {
        this.operSubmodule = operSubmodule;
    }

    public String getOperSubmodule() {
        return operSubmodule;
    }


    public void setOperDescribe(String operDescribe) {
        this.operDescribe = operDescribe;
    }

    public String getOperDescribe() {
        return operDescribe;
    }


    public void setOperRemark(String operRemark) {
        this.operRemark = operRemark;
    }

    public String getOperRemark() {
        return operRemark;
    }


    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestUrl() {
        return requestUrl;
    }


    public void setCostTime(Integer costTime) {
        this.costTime = costTime;
    }

    public Integer getCostTime() {
        return costTime;
    }


    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }


    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }


    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getParameters() {
        return parameters;
    }


    public void setOperResult(String operResult) {
        this.operResult = operResult;
    }

    public String getOperResult() {
        return operResult;
    }


    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }


    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

    public Date getAccessTime() {
        return accessTime;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

}
