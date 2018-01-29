/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 国际化Entity
 * @author XJ
 * @version 2017-04-19
 */
public class Language extends DataEntity<Language> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 标签
	private String text;		// 内容展现
	private String language;		// 语言
	
	public Language() {
		super();
	}

	public Language(String id){
		super(id);
	}

	@ExcelField(title="标签", align=2, sort=7)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="内容展现", align=2, sort=8)
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	@ExcelField(title="语言", align=2, sort=9)
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
}