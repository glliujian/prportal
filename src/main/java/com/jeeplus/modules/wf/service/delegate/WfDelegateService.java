/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.wf.service.delegate;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.BaseService;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.wf.dao.delegate.WfDelegateDao;
import com.jeeplus.modules.wf.entity.delegate.WfDelegate;

/**
 * 工作流委托設置Service
 * @author XJ
 * @version 2017-04-28
 */
@Service
@Transactional(readOnly = true)
public class WfDelegateService extends CrudService<WfDelegateDao, WfDelegate> {
	//private static LanguageService languageService = SpringContextHolder.getBean(LanguageService.class);
	public WfDelegate get(String id) {
		return super.get(id);
	}
	
	public List<WfDelegate> findList(WfDelegate wfDelegate) {
		wfDelegate.getSqlMap().put("dsf", BaseService.dataScopeFilter(UserUtils.getUser(), "", "owner"));
		return super.findList(wfDelegate);
	}
	
	public Page<WfDelegate> findPage(Page<WfDelegate> page, WfDelegate wfDelegate) {
		wfDelegate.getSqlMap().put("dsf", BaseService.dataScopeFilter(UserUtils.getUser(), "", "owner"));
		return super.findPage(page, wfDelegate);
	}
	
	@Transactional(readOnly = false)
	public void save(WfDelegate wfDelegate) {
		//if (StringUtils.isEmpty(wfDelegate.getId())) {
			List<WfDelegate> list = dao.findOverlapList(wfDelegate);
			if (list!=null && list.iterator().hasNext()){
				throw new RuntimeException(DictUtils.getLanguageLabel("不能同时存在多条生效的委托记录"));
			}
			
			//add by J 2017/7/3
			if(wfDelegate.getOwner()!=null && wfDelegate.getDelegate()!=null&&wfDelegate.getOwner().getId().equals(wfDelegate.getDelegate().getId()))
			{
				throw new RuntimeException(DictUtils.getLanguageLabel("不能委托给本人"));
			}
			//end
		//}
		super.save(wfDelegate);
	}
	
	@Transactional(readOnly = false)
	public void delete(WfDelegate wfDelegate) {
		super.delete(wfDelegate);
	}
	
	
	
	
}