/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.oa.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.oa.entity.ApplicationForStay;

/**
 * 入住申请DAO接口
 * @author LiuJian
 * @version 2018-01-20
 */
@MyBatisDao
public interface ApplicationForStayDao extends CrudDao<ApplicationForStay> {
	public int updateProcessInstanceId(ApplicationForStay applicationForStay);
}