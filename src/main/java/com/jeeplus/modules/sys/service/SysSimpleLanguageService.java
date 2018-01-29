/**
 * MFG
 */
package com.jeeplus.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.modules.sys.entity.SysSimpleLanguage;
import com.jeeplus.modules.sys.dao.SysSimpleLanguageDao;

/**
 * 国际化语言设置Service
 * @author Jack
 * @version 2017-08-29
 */
@Service
public class SysSimpleLanguageService extends CrudService<SysSimpleLanguageDao, SysSimpleLanguage> {

	public SysSimpleLanguage get(String id) {
		return super.get(id);
	}
	
	private void clearLanguageCache() {
		CacheUtils.remove(String.format("language_0_en"));
		CacheUtils.remove(String.format("language_1_en"));
		CacheUtils.remove(String.format("language_0_zh"));
		CacheUtils.remove(String.format("language_1_zh"));
		CacheUtils.remove(String.format("language_0_hk"));
		CacheUtils.remove(String.format("language_1_hk"));
		CacheUtils.remove(String.format("language_0_vn"));
		CacheUtils.remove(String.format("language_1_vn"));
		CacheUtils.remove(String.format("language_0_cam"));
		CacheUtils.remove(String.format("language_1_cam"));
	}
	
	public List<SysSimpleLanguage> findList(SysSimpleLanguage sysSimpleLanguage) {
		return super.findList(sysSimpleLanguage);
	}
	
	public Page<SysSimpleLanguage> findPage(Page<SysSimpleLanguage> page, SysSimpleLanguage sysSimpleLanguage) {
		return super.findPage(page, sysSimpleLanguage);
	}
	
	@Transactional(readOnly = false)
	public void save(SysSimpleLanguage sysSimpleLanguage) {
		this.clearLanguageCache();
		String vn = sysSimpleLanguage.getVn();
		String unescapeVn=org.apache.commons.lang.StringEscapeUtils.unescapeHtml(vn);
		sysSimpleLanguage.setVn(unescapeVn);
		super.save(sysSimpleLanguage);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysSimpleLanguage sysSimpleLanguage) {
		this.clearLanguageCache();
		super.delete(sysSimpleLanguage);
	}
	
}