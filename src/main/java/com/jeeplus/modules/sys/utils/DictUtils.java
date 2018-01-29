/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.web.Servlets;
import com.jeeplus.modules.sys.dao.DictDao;
import com.jeeplus.modules.sys.dao.QuerySqlDao;
import com.jeeplus.modules.sys.entity.Dict;
import com.jeeplus.modules.sys.entity.QuerySql;
import com.jeeplus.modules.sys.entity.SysSimpleLanguage;
import com.jeeplus.modules.sys.service.SysSimpleLanguageService;

/**
 * 字典工具类
 * @author jeeplus
 * @version 2013-5-29
 */
public class DictUtils {
	private static SysSimpleLanguageService sysSimpleLanguageService = SpringContextHolder.getBean(SysSimpleLanguageService.class);
	private static DictDao dictDao = SpringContextHolder.getBean(DictDao.class);
	private static QuerySqlDao querySqlDao = SpringContextHolder.getBean(QuerySqlDao.class);
	public static final String CACHE_DICT_MAP_Language = "dictMapLanuage";
	public static final String CACHE_DICT_MAP = "dictMap";
	
	public static String getDictLabel(String value, String type, String defaultValue){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)){
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && value.equals(dict.getValue())){
					
					return getLanguageLabel(dict.getLabel());
				}
			}
		}
		return defaultValue;
	}
	
	public static String getLanguageLabel(String text,String language){
    	JSONObject it =getLanguageMap(language,0);
    	if(it!=null){
    		String it2=it.getString(text);
    		if(!StringUtils.isEmpty(it2)){
    			return it2;
    		}
    	}
    	return text;
    }	
	
	
	public static String getLanguageLabel(String text){
		HttpServletRequest request=Servlets.getRequest();
		if(request!=null){
	    	Locale locale = RequestContextUtils.getLocaleResolver(request).resolveLocale(request);
	    	return getLanguageLabel(text,locale.getLanguage());
		}
		else{
			return text;
		}
    }	
	
	private static JSONObject getLanguageMap(String language ,int isSavaToPage){
    	JSONObject it = (JSONObject) CacheUtils.get(String.format("language_%d_%s", isSavaToPage,language));
    	if(it==null){
    		SysSimpleLanguage l = new SysSimpleLanguage();
    		if(isSavaToPage!=0){
    			l.setIsSaveToPage("1");
    		}
    		
    		List<SysSimpleLanguage> list=sysSimpleLanguageService.findList(l);
    		if(list.size()==0){
    			return null;
    		}
    		it=new JSONObject();
    	    for(SysSimpleLanguage lang:list){
    			if(language.equals("en")){
    				it.put(lang.getCode(), lang.getEn());
    			}else if(language.equals("zh")){
    				it.put(lang.getCode(), lang.getCn());
    			}else if(language.equals("hk")){
    				it.put(lang.getCode(), lang.getHk());
    			}else if(language.equals("vn")){
    				it.put(lang.getCode(), lang.getVn());
    			}else if(language.equals("cam")){
    				it.put(lang.getCode(), lang.getCam());
    			}else{
    				it.put(lang.getCode(), lang.getCode());
    			}
    	    	
    	    }
    	    CacheUtils.put(String.format("language_%d_%s", isSavaToPage,language), it);
    	}
    	return it;
	}
	
	public static JSONObject getLanguageMap(){
    	Locale locale = RequestContextUtils.getLocaleResolver(Servlets.getRequest()).resolveLocale(Servlets.getRequest());
    	return getLanguageMap(locale.getLanguage(),1);
    }
	
	public static String getDictLabels(String values, String type, String defaultValue){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(values)){
			List<String> valueList = Lists.newArrayList();
			for (String value : StringUtils.split(values, ",")){
				valueList.add(getDictLabel(value, type, defaultValue));
			}
			return StringUtils.join(valueList, ",");
		}
		return defaultValue;
	}

	public static String getDictValue(String label, String type, String defaultLabel){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)){
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && label.equals(dict.getLabel())){
					return dict.getValue();
				}
			}
		}
		return defaultLabel;
	}
	
	public static List<Dict> getDictList(String type){
		@SuppressWarnings("unchecked")
		Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>)CacheUtils.get(CACHE_DICT_MAP);
		if (dictMap==null){
			dictMap = Maps.newHashMap();
			for (Dict dict : dictDao.findAllList(new Dict())){
				List<Dict> dictList = dictMap.get(dict.getType());
				if (dictList != null){
					dictList.add(dict);
				}else{
					dictMap.put(dict.getType(), Lists.newArrayList(dict));
				}
			}
			CacheUtils.put(CACHE_DICT_MAP, dictMap);
		}
		List<Dict> dictList = dictMap.get(type);
		if(dictList!=null){
			List<Dict> dictList2=CacheUtils.deepCopy(dictList);
			for(Dict dict:dictList2){
				dict.setLabel(DictUtils.getLanguageLabel(dict.getLabel()));		
			}
			return dictList2;
		}
		else{
			return Lists.newArrayList();
		}

		
	}
	
	public static String getQuerySql(String type) {
		QuerySql qs = querySqlDao.findUniqueByProperty("sql_code", type);
		if (qs!=null){
			return qs.getSqlStr();
		}
		return "";
	}

	public static List<Dict> getDictListWithKey(String type){
		@SuppressWarnings("unchecked")
		Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>)CacheUtils.get(CACHE_DICT_MAP);
		if (dictMap==null){
			dictMap = Maps.newHashMap();
			for (Dict dict : dictDao.findAllList(new Dict())){
				List<Dict> dictList = dictMap.get(dict.getType());
				if (dictList != null){
					//dict.setLabel(languageService.getLanguageLabel(dict.getLabel()));
					//dict.setLabel(dict.getValue() + " - " + dict.getLabel());
					dict.setLabel(dict.getValue() + " - " + getLanguageLabel(dict.getLabel()));
					dictList.add(dict);
				}else{
					//dict.setLabel(dict.getValue() + " - " + dict.getLabel());
					dict.setLabel(dict.getValue() + " - " + getLanguageLabel(dict.getLabel()));
					dictMap.put(dict.getType(), Lists.newArrayList(dict));
				}
			}
			CacheUtils.put(CACHE_DICT_MAP, dictMap);
		}
		List<Dict> dictList = dictMap.get(type);
		if(dictList!=null){
			List<Dict> dictList2=CacheUtils.deepCopy(dictList);
			for(Dict dict:dictList2){
				dict.setLabel(dict.getValue()+ "-"+ DictUtils.getLanguageLabel(dict.getLabel()));		
			}
			return dictList2;
		}
		else{
			return Lists.newArrayList();
		}

		
	}
	
	public static List<Dict> getSqlDictList(String type, String params) {
		/*@SuppressWarnings("unchecked")
		Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>) CacheUtils.get(CACHE_DICT_MAP); 
		if (dictMap == null) {
			dictMap = Maps.newHashMap();
			for (Dict dict : dictDao.findAllList(new Dict())) {
				List<Dict> dictList = dictMap.get(dict.getType());
				if (dictList != null) {
					dictList.add(dict);
				} else {
					dictMap.put(dict.getType(), Lists.newArrayList(dict));
				}
			}
			CacheUtils.put(CACHE_DICT_MAP, dictMap);
		}
		if (dictMap.get(type) == null) {
			QuerySql qs = querySqlDao.findUniqueByProperty("sql_code", type);
			Map<String, String> sqlMap = new HashMap<String, String>();
			sqlMap.put("sql", qs.getSqlStr());
			if (!StringUtils.isEmpty(params)) {
				String[] paramArray = StringUtils.split(params, ",");
				int i = 1;
				for (String str : paramArray) {
					sqlMap.put("param" + i, qs.getSqlStr());
					i++;
				}
			}
			List<Map> list = querySqlDao.executeSql(sqlMap);

			for (Map map : list) {
				Dict dict = new Dict();
				dict.setType(type);
				dict.setValue(String.valueOf(map.get("key")));
				dict.setLabel(String.valueOf(map.get("value")));

				List<Dict> dictList = dictMap.get(type);
				if (dictList != null) {
					dictList.add(dict);
				} else {
					dictMap.put(dict.getType(), Lists.newArrayList(dict));
				}
			}
		}
		List<Dict> dictList = dictMap.get(type);
		if (dictList == null) {
			dictList = Lists.newArrayList();
		}*/
		
		QuerySql qs = querySqlDao.findUniqueByProperty("sql_code", type);
		Map<String, String> sqlMap = new HashMap<String, String>();
		sqlMap.put("sql", qs.getSqlStr());
		if (!StringUtils.isEmpty(params)) {
			String[] paramArray = StringUtils.split(params, ",");
			int i = 1;
			//where category_ = #{param1}
			for (String str : paramArray) {
				sqlMap.put("param" + i, str);
				i++;
			}
		}
		List<Map> list = querySqlDao.executeSql(sqlMap);
		List<Dict> dictList = Lists.newArrayList();
		for (Map map : list) {
			Dict dict = new Dict();
			dict.setType(type);
			dict.setValue(String.valueOf(map.get("value")));
			String l = String.valueOf(map.get("lable"));
			dict.setLabel(DictUtils.getLanguageLabel(l));
			if (l!=null && l.contains(" - ")) {
				String[] ls = l.split(" - ");
				if (ls.length > 1){
					l = ls[0] + " - " + DictUtils.getLanguageLabel(ls[1]);
					if (ls.length > 2){
						l += " - " + DictUtils.getLanguageLabel(ls[2]);
					}
					dict.setLabel(l);
				}
			}
			dictList.add(dict);
		} 
		return dictList;
	}
	
	public static String getSqlField(String type, String fieldName, String params) {
		if (StringUtils.isEmpty(type)) {
			return "";
		}
		QuerySql qs = querySqlDao.findUniqueByProperty("sql_code", type);
		Map<String, String> sqlMap = new HashMap<String, String>();
		sqlMap.put("sql", qs.getSqlStr() );
		
		if (!StringUtils.isEmpty(params)) {
			String[] paramArray = StringUtils.split(params, ",");
			int i = 1;
			for (String str : paramArray) {
				sqlMap.put("param" + i, str);
				i++;
			}
		}
		
		List<Map> list = querySqlDao.executeSql(sqlMap);
		if (list != null && list.iterator().hasNext())	 
			return String.valueOf(list.iterator().next().get(fieldName));
		else
			return "";
	 
	}
	
	public static String getSqlDictLabel(String type, String params, String value) {
		if (StringUtils.isEmpty(type) || StringUtils.isEmpty(value)) {
			return "";
		}
		QuerySql qs = querySqlDao.findUniqueByProperty("sql_code", type);
		Map<String, String> sqlMap = new HashMap<String, String>();
		sqlMap.put("sql","select value,lable from (" + qs.getSqlStr() + ") a where a.value='"+value+"'");
		
		if (!StringUtils.isEmpty(params)) {
			String[] paramArray = StringUtils.split(params, ",");
			int i = 1;
			for (String str : paramArray) {
				sqlMap.put("param" + i, str);
				i++;
			}
		}
		
		List<Map> list = querySqlDao.executeSql(sqlMap);
		for (Map map : list) {
			return String.valueOf(map.get("lable"));
		} 
		return value;
	}

	/*
	 * 反射根据对象和属性名获取属性值
	 */
	public static Object getValue(Object obj, String filed) {
		try {
			if (obj == null) {
				return null;
			}
			Class clazz = obj.getClass();
			if (!filed.contains(".")){
				PropertyDescriptor pd = new PropertyDescriptor(filed, clazz);
				Method getMethod = pd.getReadMethod();//获得get方法
				if (pd != null) {
					Object o = getMethod.invoke(obj);//执行get方法返回一个Object
					return o;
				}
			}else{
				String[] fileds = StringUtils.split(filed, ".");
				Object o1 = getValue(obj,fileds[0]); 
				return getValue(o1,fileds[1]);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
