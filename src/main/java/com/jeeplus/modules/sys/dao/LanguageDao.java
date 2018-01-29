/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sys.entity.Language;

/**
 * 国际化DAO接口
 * @author XJ
 * @version 2017-04-19
 */
@MyBatisDao
public interface LanguageDao extends CrudDao<Language> {

	
}