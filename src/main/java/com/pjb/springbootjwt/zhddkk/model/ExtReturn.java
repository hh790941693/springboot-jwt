package com.pjb.springbootjwt.zhddkk.model;

import java.util.List;

public class ExtReturn 
{
	private boolean success = true; // 是否成功
	private String msg = ""; // 返回消息
	private Integer total = 0; // 数量
	private List<?> dataList = null; // 结果

	public ExtReturn() {
	}

	public ExtReturn(boolean success, String msg) {
		this.success = success;
		this.msg = msg;
	}

	public ExtReturn(Integer total, List<?> dataList) {
		this.total = total;
		this.dataList = dataList;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<?> getDataList() {
		return dataList;
	}

	public void setDataList(List<?> dataList) {
		this.dataList = dataList;
	}
}
