package com.pjb.springbootjwt.zhddkk.entity;

public class PageQueryEntity 
{
	private int curPage;
	
	private int numPerPage;
	
	private Object params;



	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}

	public Object getParams() {
		return params;
	}

	public void setParams(Object params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "PageQueryEntity [curPage=" + curPage + ", numPerPage=" + numPerPage + ", params=" + params + "]";
	}
}
