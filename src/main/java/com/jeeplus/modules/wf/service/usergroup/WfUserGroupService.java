/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.wf.service.usergroup;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.wf.entity.usergroup.WfUserGroup;
import com.jeeplus.modules.wf.dao.usergroup.WfUserGroupDao;

/**
 * 工作流用户组Service
 * @author xj
 * @version 2017-06-25
 */
@Service
@Transactional(readOnly = true)
public class WfUserGroupService extends CrudService<WfUserGroupDao, WfUserGroup> {

	public WfUserGroup get(String id) {
		return super.get(id);
	}
	
	public List<WfUserGroup> findList(WfUserGroup wfUserGroup) {
		return super.findList(wfUserGroup);
	}
	
	public Page<WfUserGroup> findPage(Page<WfUserGroup> page, WfUserGroup wfUserGroup) {
		return super.findPage(page, wfUserGroup);
	}
	
	@Transactional(readOnly = false)
	public void save(WfUserGroup wfUserGroup) {
		super.save(wfUserGroup);
	}
	
	@Transactional(readOnly = false)
	public void delete(WfUserGroup wfUserGroup) {
		super.delete(wfUserGroup);
	}
	
	
	
	
}