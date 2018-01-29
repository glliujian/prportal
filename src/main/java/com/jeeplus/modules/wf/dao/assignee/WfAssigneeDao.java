/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.wf.dao.assignee;

import java.util.List;
import java.util.Map;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.wf.entity.assignee.WfAssignee;

/**
 * 工作流處理人設置DAO接口
 * @author XJ
 * @version 2017-04-28
 */
@MyBatisDao
public interface WfAssigneeDao extends CrudDao<WfAssignee> {
	List<Map> executeSql(Map map);	
}