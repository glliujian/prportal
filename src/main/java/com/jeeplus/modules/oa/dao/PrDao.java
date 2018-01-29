/**
 * There are <a href="http://www.jeeplus.org/">jeeplus</a> code generation
 */
package com.jeeplus.modules.oa.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.oa.entity.Pr;

/**
 * 请假DAO接口
 * @author liuj
 * @version 2013-8-23
 */
@MyBatisDao
public interface PrDao extends CrudDao<Pr> {
	
	/**
	 * 更新流程实例ID
	 * @param leave
	 * @return
	 */
	public int updateProcessInstanceId(Pr pr);
	
	/**
	 * 更新实际开始结束时间
	 * @param leave
	 * @return
	 */
	//public int updateRealityTime(Leave leave);
	/**
	 * 取得草稿列表
	 * 
	 */
	public List<Pr> getDraft(String userId);
	/**
	 * 通过PRPO 找到PR 单据
	 * 
	 */
	public Pr findPRPO(String prpo);

	/**
	 * 取得所有的PR 申請
	 * @param act
	 * @return
	 */
	public List<Pr> getList(Pr pr);
	
	
}
