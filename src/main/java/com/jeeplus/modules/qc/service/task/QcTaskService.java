/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.qc.service.task;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.qc.entity.task.QcTask;
import com.jeeplus.modules.qc.dao.task.QcTaskDao;

/**
 * 工作任务Service
 * @author xj
 * @version 2017-04-15
 */
@Service
@Transactional(readOnly = true)
public class QcTaskService extends CrudService<QcTaskDao, QcTask> {

	public QcTask get(String id) {
		return super.get(id);
	}
	
	public List<QcTask> findList(QcTask qcTask) {
		return super.findList(qcTask);
	}
	
	public Page<QcTask> findPage(Page<QcTask> page, QcTask qcTask) {
		return super.findPage(page, qcTask);
	}
	
	@Transactional(readOnly = false)
	public void save(QcTask qcTask) {
		super.save(qcTask);
	}
	
	@Transactional(readOnly = false)
	public void delete(QcTask qcTask) {
		super.delete(qcTask);
	}
	
	
	
	
}