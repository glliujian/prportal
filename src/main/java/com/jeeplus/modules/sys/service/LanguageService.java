/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.web.Servlets;
import com.jeeplus.modules.sys.dao.LanguageDao;
import com.jeeplus.modules.sys.dao.SysSimpleLanguageDao;
import com.jeeplus.modules.sys.dao.UserDao;
import com.jeeplus.modules.sys.entity.Language;
import com.jeeplus.modules.sys.entity.SysSimpleLanguage;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 国际化Service
 * @author XJ
 * @version 2017-04-19
 */
@Service
public class LanguageService extends CrudService<LanguageDao, Language> {
	@Autowired
	SysSimpleLanguageDao sysSimpleLanguageDao ;
	
	@Autowired
	private UserDao userDao;

	public Language get(String id) {
		return super.get(id);
	}
	
	public List<Language> findList(Language language) {
		return super.findList(language);
	}
	
	public Page<Language> findPage(Page<Language> page, Language language) {
		return super.findPage(page, language);
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
	
	@Transactional(readOnly = false)
	public void save(Language language) {
		super.save(language);
		this.clearLanguageCache();
	}
	
	@Transactional(readOnly = false)
	public void delete(Language language) {
		super.delete(language);
		this.clearLanguageCache();
	}
	
	@Transactional(readOnly = false)
	public void updateLanguage(String language, String id) {
		userDao.updateLanguage(language, id);
		UserUtils.clearCache(UserUtils.getUser());
		this.clearLanguageCache();
	}
		
}