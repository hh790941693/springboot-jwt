package com.pjb.springbootjwt.zhddkk.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;


/**
 * 
 */
 @TableName("ws_user_profile")
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
    //地址
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


    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return realName;
    }


    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }


    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }


    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }


    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getSex() {
        return sex;
    }


    public void setSexText(String sexText) {
        this.sexText = sexText;
    }

    public String getSexText() {
        return sexText;
    }


    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTel() {
        return tel;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }


    public void setProfession(Integer profession) {
        this.profession = profession;
    }

    public Integer getProfession() {
        return profession;
    }


    public void setProfessionText(String professionText) {
        this.professionText = professionText;
    }

    public String getProfessionText() {
        return professionText;
    }


    public void setHobby(Integer hobby) {
        this.hobby = hobby;
    }

    public Integer getHobby() {
        return hobby;
    }


    public void setHobbyText(String hobbyText) {
        this.hobbyText = hobbyText;
    }

    public String getHobbyText() {
        return hobbyText;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

}
