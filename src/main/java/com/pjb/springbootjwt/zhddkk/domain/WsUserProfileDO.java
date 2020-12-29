package com.pjb.springbootjwt.zhddkk.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 
 */
 @TableName("ws_user_profile")
 @Data
public class WsUserProfileDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    //主键
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id",name = "主键")
    private Integer id;
    //用户ID
    @ApiModelProperty(value = "userId",name = "用户ID")
    private Integer userId;
    //用户名
    @ApiModelProperty(value = "userName",name = "用户名")
    private String userName;
    //真实姓名
    @ApiModelProperty(value = "realName",name = "真实姓名")
    private String realName;
    //头像url
    @ApiModelProperty(value = "img",name = "头像url")
    private String img;
    //个性签名
    @ApiModelProperty(value = "sign",name = "个性签名")
    private String sign;
    //年龄
    @ApiModelProperty(value = "age",name = "年龄")
    private Integer age;
    //性别 1:男 2:女 3:未知
    @ApiModelProperty(value = "sex",name = "性别 1:男 2:女 3:未知")
    private Integer sex;
    //性别(文本)
    @ApiModelProperty(value = "sexText",name = "性别(文本)")
    private String sexText;
    //电话
    @ApiModelProperty(value = "tel",name = "电话")
    private String tel;
    //位置区域
    @ApiModelProperty(value = "location",name = "位置区域")
    private String location;
    //详细地址
    @ApiModelProperty(value = "address",name = "地址")
    private String address;
    //职业  1:IT 2:建筑  3:金融  4:个体商户 5:旅游 99:其他
    @ApiModelProperty(value = "profession",name = "职业  1:IT 2:建筑  3:金融  4:个体商户 5:旅游 99:其他")
    private Integer profession;
    //职业(文本)
    @ApiModelProperty(value = "professionText",name = "职业(文本)")
    private String professionText;
    //爱好 1:篮球 2:足球  3:爬山 4:旅游  5:网游  99:其他
    @ApiModelProperty(value = "hobby",name = "爱好 1:篮球 2:足球  3:爬山 4:旅游  5:网游  99:其他")
    private Integer hobby;
    //爱好(文本)
    @ApiModelProperty(value = "hobbyText",name = "爱好(文本)")
    private String hobbyText;
    //创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "createTime",name = "创建时间")
    private Date createTime;

    // 省
    @TableField(exist = false)
    private String province;
    // 市
    @TableField(exist = false)
    private String city;
    // 区
    @TableField(exist = false)
    private String district;
}
