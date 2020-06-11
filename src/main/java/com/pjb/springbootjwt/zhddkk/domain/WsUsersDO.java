package com.pjb.springbootjwt.zhddkk.domain;

import java.io.Serializable;
import java.util.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "姓名",orderNum = "1",width=10)
    @ApiModelProperty(value = "name",name = "姓名")
    private String name;
    //密码
    @ApiModelProperty(value = "password",name = "密码")
    private String password;
    //注册时间
    @Excel(name = "注册时间",orderNum = "3",width=20)
    @ApiModelProperty(value = "registerTime",name = "注册时间")
    private String registerTime;
    //是否在线 0:离线 1:在线
    @Excel(name = "在线状态",orderNum = "4",width=10,replace={"离线_0","在线_1"})
    @ApiModelProperty(value = "state",name = "是否在线 0:离线 1:在线")
    private String state;
    //上次登录时间
    @Excel(name = "最近登陆时间",orderNum = "5",width=20)
    @ApiModelProperty(value = "lastLoginTime",name = "上次登录时间")
    private String lastLoginTime;
    //上次登出时间
    @ApiModelProperty(value = "lastLogoutTime",name = "上次登出时间")
    private String lastLogoutTime;
    //是否可用 0:不可用  1:可用
    @Excel(name = "禁用状态",orderNum = "6",width=10,replace={"禁用_0","正常_1"})
    @ApiModelProperty(value = "enable",name = "是否可用 0:不可用  1:可用")
    private String enable;
    //是否禁言  0:禁言 1：没有禁言
    @Excel(name = "禁言状态",orderNum = "7",width=10,replace={"禁言_0","正常_1"})
    @ApiModelProperty(value = "speak",name = "是否禁言  0:禁言 1：没有禁言")
    private String speak;

    //积分数量
    @ApiModelProperty(value = "coinNum",name = "积分数量")
    private Integer coinNum;
    //问题1
    @Excel(name = "问题1",orderNum = "8",width=25)
    @ApiModelProperty(value = "question1",name = "问题1")
    private String question1;
    //答案1
    @Excel(name = "回答1",orderNum = "9",width=15)
    @ApiModelProperty(value = "answer1",name = "答案1")
    private String answer1;
    //问题2
    @Excel(name = "问题2",orderNum = "10",width=25)
    @ApiModelProperty(value = "question2",name = "问题2")
    private String question2;
    //答案2
    @Excel(name = "回答2",orderNum = "11",width=15)
    @ApiModelProperty(value = "answer2",name = "答案2")
    private String answer2;
    //问题3
    @Excel(name = "问题3",orderNum = "12",width=25)
    @ApiModelProperty(value = "question3",name = "问题3")
    private String question3;
    //答案3
    @Excel(name = "回答3",orderNum = "13",width=15)
    @ApiModelProperty(value = "answer3",name = "答案3")
    private String answer3;
    //创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "createTime",name = "创建时间")
    private Date createTime;

    @TableField(exist = false)
    // 非表字段  是否是好友 0:不是  1:申请中 2:被拒绝 3:申请成功
    private Integer isFriend;

    @TableField(exist = false)
    @Excel(name = "密码",orderNum = "2",width=10)
    private String passwordDecode;

    @TableField(exist = false)
    private String sendMsg;

    @TableField(exist = false)
    private String headImage;

    @TableField(exist = false)
    private String sign;

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

    public String getPasswordDecode() {
        return passwordDecode;
    }

    public void setPasswordDecode(String passwordDecode) {
        this.passwordDecode = passwordDecode;
    }

    public String getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(String sendMsg) {
        this.sendMsg = sendMsg;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public Integer getCoinNum() {
        return coinNum;
    }

    public void setCoinNum(Integer coinNum) {
        this.coinNum = coinNum;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
