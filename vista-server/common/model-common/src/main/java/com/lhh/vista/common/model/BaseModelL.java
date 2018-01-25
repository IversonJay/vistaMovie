package com.lhh.vista.common.model;

import java.io.Serializable;

public class BaseModelL implements Serializable {
	/**
	 * 作者: Soap
	 * 创建:2012-7-30 下午1:22:23
	 * 基础bean类
	 */
	protected Long id;// ID
	
	protected String createDate;// 创建日期
	
	protected String modifyDate;// 修改日期

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
}