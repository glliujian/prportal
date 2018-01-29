/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.sys.entity.QuerySql;
import com.jeeplus.modules.sys.dao.QuerySqlDao;

/**
 * 自定义查询Service
 * @author xj
 * @version 2017-04-21
 */
@Service
@Transactional(readOnly = true)
public class QuerySqlService extends CrudService<QuerySqlDao, QuerySql> {

	public QuerySql get(String id) {
		return super.get(id);
	}
	
	public List<QuerySql> findList(QuerySql querySql) {
		return super.findList(querySql);
	}
	
	public Page<QuerySql> findPage(Page<QuerySql> page, QuerySql querySql) {
		return super.findPage(page, querySql);
	}
	
	@Transactional(readOnly = false)
	public void save(QuerySql querySql) {
		super.save(querySql);
	}
	
	@Transactional(readOnly = false)
	public void delete(QuerySql querySql) {
		super.delete(querySql);
	}
	
	
	
	
}