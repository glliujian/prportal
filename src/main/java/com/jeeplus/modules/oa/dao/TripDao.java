/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.oa.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.oa.entity.Leave;
import com.jeeplus.modules.oa.entity.Trip;

/**
 * 国内出差DAO接口
 * @author ml
 * @version 2018-01-18
 */
@MyBatisDao
public interface TripDao extends CrudDao<Trip> {

	/**
	 * 更新流程实例ID
	 * @param leave
	 * @return
	 */
	public int updateProcessInstanceId(Trip trip);
	
}