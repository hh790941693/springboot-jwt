package com.pjb.springbootjwt.zhddkk.entity;

import java.util.List;

public class PageResponseEntity 
{
	private int totalPage;
	
	private int totalCount;
		
	private List<?> list;

	private Object parameter1;
	
	private Object parameter2;
	
	private Object parameter3;
	
	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}
	
	public Object getParameter1() {
		return parameter1;
	}

	public void setParameter1(Object parameter1) {
		this.parameter1 = parameter1;
	}
		
	public Object getParameter2() {
		return parameter2;
	}

	public void setParameter2(Object parameter2) {
		this.parameter2 = parameter2;
	}

	public Object getParameter3() {
		return parameter3;
	}

	public void setParameter3(Object parameter3) {
		this.parameter3 = parameter3;
	}

	@Override
	public String toString() {
		return "PageResponseEntity [totalPage=" + totalPage + ", totalCount=" + totalCount + ", parameter1="
				+ parameter1 + ", list=" + list + "]";
	}
}
