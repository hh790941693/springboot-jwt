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
 * 用户账号表
 */
 @TableName("ws_users")
public class WsUsersDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    //主键
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id",name = "主键")
    private Integer id;
    //姓名
    @ApiModelProperty(value = "name",name = "姓名")
    private String name;
    //密码
    @ApiModelProperty(value = "password",name = "密码")
    private String password;
    //注册时间
    @ApiModelProperty(value = "registerTime",name = "注册时间")
    private String registerTime;
    //是否在线 0:离线 1:在线
    @ApiModelProperty(value = "state",name = "是否在线 0:离线 1:在线")
    private String state;
    //上次登录时间
    @ApiModelProperty(value = "lastLoginTime",name = "上次登录时间")
    private String lastLoginTime;
    //上次登出时间
    @ApiModelProperty(value = "lastLogoutTime",name = "上次登出时间")
    private String lastLogoutTime;
    //是否可用 0:不可用  1:可用
    @ApiModelProperty(value = "enable",name = "是否可用 0:不可用  1:可用")
    private String enable;
    //是否禁言  0:禁言 1：没有禁言
    @ApiModelProperty(value = "speak",name = "是否禁言  0:禁言 1：没有禁言")
    private String speak;
    //问题1
    @ApiModelProperty(value = "question1",name = "问题1")
    private String question1;
    //答案1
    @ApiModelProperty(value = "answer1",name = "答案1")
    private String answer1;
    //问题2
    @ApiModelProperty(value = "question2",name = "问题2")
    private String question2;
    //答案2
    @ApiModelProperty(value = "answer2",name = "答案2")
    private String answer2;
    //问题3
    @ApiModelProperty(value = "question3",name = "问题3")
    private String question3;
    //答案3
    @ApiModelProperty(value = "answer3",name = "答案3")
    private String answer3;
    //创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "createTime",name = "创建时间")
    private Date createTime;


    @TableField(exist = false)
    // 非表字段  是否是好友 0:不是  1:申请中 2:被拒绝 3:申请成功
    private Integer isFriend;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getEnable() {
        return enable;
    }


    public void setSpeak(String speak) {
        this.speak = speak;
    }

    public String getSpeak() {
        return speak;
    }


    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getQuestion1() {
        return question1;
    }


    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer1() {
        return answer1;
    }


    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getQuestion2() {
        return question2;
    }


    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer2() {
        return answer2;
    }


    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    public String getQuestion3() {
        return question3;
    }


    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer3() {
        return answer3;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLogoutTime() {
        return lastLogoutTime;
    }

    public void setLastLogoutTime(String lastLogoutTime) {
        this.lastLogoutTime = lastLogoutTime;
    }

    public Integer getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(Integer isFriend) {
        this.isFriend = isFriend;
    }
}
