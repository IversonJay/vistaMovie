package com.lhh.vista.common.model;

import java.util.ArrayList;
import java.util.List;

public class PagerResponse<T> {
	private Integer total = 0;// 总条数
	private List<T> rows=new ArrayList<T>();// 数据List

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public PagerResponse() {
	}

	public PagerResponse(Integer total, List<T> rows) {
		this.total = total;
		this.rows = rows;
	}
}
