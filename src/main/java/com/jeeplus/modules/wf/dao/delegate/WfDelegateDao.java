/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.wf.dao.delegate;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.wf.entity.delegate.WfDelegate;

/**
 * 工作流委托設置DAO接口
 * @author XJ
 * @version 2017-04-28
 */
@MyBatisDao
public interface WfDelegateDao extends CrudDao<WfDelegate> {

	public List<WfDelegate> findNowList(WfDelegate entity);
	
	public List<WfDelegate> findOverlapList(WfDelegate entity);
}