package com.jscxrz.zhddkk.entity;

public class WsDic 
{
	private String type;
	
	private String key;
	
	private String value;
	
	private int sort;
	
	private String remark;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "WsDic [type=" + type + ", key=" + key + ", value=" + value + ", sort=" + sort + ", remark=" + remark
				+ "]";
	}	
}
