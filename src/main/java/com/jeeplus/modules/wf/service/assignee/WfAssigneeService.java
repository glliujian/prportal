/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.wf.service.assignee;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.wf.entity.assignee.WfAssignee;
import com.jeeplus.modules.wf.dao.assignee.WfAssigneeDao;

/**
 * 工作流處理人設置Service
 * @author XJ
 * @version 2017-04-28
 */
@Service
@Transactional(readOnly = true)
public class WfAssigneeService extends CrudService<WfAssigneeDao, WfAssignee> {

	public WfAssignee get(String id) {
		return super.get(id);
	}
	
	public List<WfAssignee> findList(WfAssignee wfAssignee) {
		return super.findList(wfAssignee);
	}
	
	public Page<WfAssignee> findPage(Page<WfAssignee> page, WfAssignee wfAssignee) {
		return super.findPage(page, wfAssignee);
	}
	
	@Transactional(readOnly = false)
	public void save(WfAssignee wfAssignee) {
		super.save(wfAssignee);
	}
	
	@Transactional(readOnly = false)
	public void delete(WfAssignee wfAssignee) {
		super.delete(wfAssignee);
	}
	
	
	
	
}