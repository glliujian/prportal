/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.wf.dao.usergroup;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.wf.entity.usergroup.WfUserGroup;

/**
 * 工作流用户组DAO接口
 * @author xj
 * @version 2017-06-25
 */
@MyBatisDao
public interface WfUserGroupDao extends CrudDao<WfUserGroup> {

	
}