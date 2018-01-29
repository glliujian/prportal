/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.qc.dao.task;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.qc.entity.task.QcTask;

/**
 * 工作任务DAO接口
 * @author xj
 * @version 2017-04-15
 */
@MyBatisDao
public interface QcTaskDao extends CrudDao<QcTask> {

	
}