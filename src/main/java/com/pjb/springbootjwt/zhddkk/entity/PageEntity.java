package com.pjb.springbootjwt.zhddkk.entity;

/**
 * 分页参数
 * 
 * @author Administrator
 *
 */
public class PageEntity {
	// 用户接受前台参数用
	public int curPage;    //当前页码
	public int numPerPage; //每页显示条数
	public String sort;    //要排序的字段名称  如果没值则为undefined
	public String order;   //排序   asc  desc
	public String search;  //搜索框中的关键词

	// 用于去数据库分页查询用
	public int start;
	public int limit;
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
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		if (this.limit == 0) {
			this.limit = 10;
		}
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	@Override
	public String toString() {
		return "Page [curPage=" + curPage + ", numPerPage=" + numPerPage + ", sort=" + sort + ", order=" + order
				+ ", search=" + search + ", start=" + start + ", limit=" + limit + "]";
	}
}
