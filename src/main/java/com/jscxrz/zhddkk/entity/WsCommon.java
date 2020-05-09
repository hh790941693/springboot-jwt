package com.jscxrz.zhddkk.entity;

public class WsCommon extends Page
{
	private String id;
	private String type;
	private String name;
	private int orderby;
	private String remark;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getOrderby() {
		return orderby;
	}

	public void setOrderby(int orderby) {
		this.orderby = orderby;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "WsCommon [id=" + id + ", type=" + type + ", name=" + name + ", orderby=" + orderby + ", remark="
				+ remark + "]";
	}
}
