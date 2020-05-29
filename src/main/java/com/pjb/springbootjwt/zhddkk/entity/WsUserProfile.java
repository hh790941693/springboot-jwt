package com.pjb.springbootjwt.zhddkk.entity;

import java.util.Date;

/**
 * 用户基本信息
 * 
 * @author Administrator
 *
 */
@Deprecated
public class WsUserProfile {
	
	//主键
	private Integer id;
	
	//用户id
	private Integer userId;
	
	//用户名
	private String userName;
	
	// 真实姓名
	private String realName;
	
	// 头像
	private String img;
	
	//个性签名
	private String sign;
	
	//年龄
	private Integer age;
	
	//性别  1:男 2:女 3:未知
	private Integer sex;
	
	private String sexText;
	
	//号码
	private String tel;
	
	//地址
	private String address;
	
	//职业  1:IT 2:建筑  3:金融  4:个体商户 5:旅游  99:其他
	private Integer profession;
	
	private String professionText;
	
	//爱好 1:篮球 2:足球  3:爬山 4:旅游  5:网游  99:其他
	private Integer hobby;
	
	private String hobbyText;
	
	//创建时间
	private Date createTime;
	private String createTimeStr;

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getSexText() {
		return sexText;
	}

	public void setSexText(String sexText) {
		this.sexText = sexText;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getProfession() {
		return profession;
	}

	public void setProfession(Integer profession) {
		this.profession = profession;
	}

	public Integer getHobby() {
		return hobby;
	}

	public void setHobby(Integer hobby) {
		this.hobby = hobby;
	}

	public String getHobbyText() {
		return hobbyText;
	}

	public void setHobbyText(String hobbyText) {
		this.hobbyText = hobbyText;
	}

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
	
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getProfessionText() {
		return professionText;
	}

	public void setProfessionText(String professionText) {
		this.professionText = professionText;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@Override
	public String toString() {
		return "WsUserProfile [id=" + id + ", userId=" + userId + ", userName=" + userName + ", realName=" + realName
				+ ", img=" + img + ", sign=" + sign + ", age=" + age + ", sex=" + sex + ", sexText=" + sexText
				+ ", tel=" + tel + ", address=" + address + ", profession=" + profession + ", professionText="
				+ professionText + ", hobby=" + hobby + ", hobbyText=" + hobbyText + ", createTime=" + createTime
				+ ", createTimeStr=" + createTimeStr + "]";
	}
}
