/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 自定义查询Entity
 * @author xj
 * @version 2017-04-21
 */
public class QuerySql extends DataEntity<QuerySql> {
	
	private static final long serialVersionUID = 1L;
	private String sqlCode;		// SQL代码
	private String sqlStr;		// SQL
	
	public QuerySql() {
		super();
	}

	public QuerySql(String id){
		super(id);
	}

	@ExcelField(title="SQL代码", align=2, sort=7)
	public String getSqlCode() {
		return sqlCode;
	}

	public void setSqlCode(String sqlCode) {
		this.sqlCode = sqlCode;
	}
	
	@ExcelField(title="SQL", align=2, sort=8)
	public String getSqlStr() {
		return sqlStr;
	}

	public void setSqlStr(String sqlStr) {
		this.sqlStr = sqlStr;
	}
	
}