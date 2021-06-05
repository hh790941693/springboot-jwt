package com.pjb.springbootjwt.shop.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 申请成为商家表.
 */
@TableName("sp_merchant_apply")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpMerchantApplyDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    //主键id
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id" , name = "主键id")
    private Long id;

    //申请编号
    @ApiModelProperty(value = "applyNo" , name = "申请编号")
    private String applyNo;

    //申请人id
    @ApiModelProperty(value = "userId" , name = "申请人id")
    private Long userId;

    //店铺名称
    @ApiModelProperty(value = "name" , name = "店铺名称")
    private String name;

    //店铺地址
    @ApiModelProperty(value = "address" , name = "店铺地址")
    private String address;

    //店铺图片
    @ApiModelProperty(value = "image" , name = "店铺图片")
    private String image;

    //店铺描述
    @ApiModelProperty(value = "desc" , name = "店铺描述")
    private String desc;

    //联系电话
    @ApiModelProperty(value = "contact" , name = "联系电话")
    private String contact;

    //审批状态 1:待审批 2:审批通过 3:审批不通过
    @ApiModelProperty(value = "status" , name = "审批状态 1:待审批 2:审批通过 3:审批不通过")
    private Integer status;

    //创建时间
    @ApiModelProperty(value = "createTime" , name = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    //更新时间
    @ApiModelProperty(value = "updateTime" , name = "更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

}
