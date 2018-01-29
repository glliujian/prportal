/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.act.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.act.entity.Act;

/**
 * 审批DAO接口
 * @author jeeplus
 * @version 2014-05-16
 */
@MyBatisDao
public interface ActDao extends CrudDao<Act> {

	public int updateProcInsIdByBusinessId(Act act);	
	
	public List<Act> getHistoricList(Act act);
	
	public List<Act> getTaskList(Act act);
	
	/**
	 * 通过taskID取出审批建议
	 */
	public List<String> getComment(String taskId);
}
