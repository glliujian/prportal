/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.dao;

import java.util.List;
import java.util.Map;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sys.entity.QuerySql;

/**
 * 自定义查询DAO接口
 * @author xj
 * @version 2017-04-21
 */
@MyBatisDao
public interface QuerySqlDao extends CrudDao<QuerySql> {
	List<Map> executeSql(Map map);
}