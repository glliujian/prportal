/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.sys.entity.Dict;
import com.jeeplus.modules.sys.entity.Language;
import com.jeeplus.modules.sys.service.LanguageService;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.sys.utils.UserUtils;

import io.swagger.annotations.ApiOperation;

/**
 * 国际化Controller
 * @author XJ
 * @version 2017-04-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/language")
public class LanguageController extends BaseController {
	//@Autowired CookieLocaleResolver resolver;
	
	@Autowired SessionLocaleResolver resolver;
	
	@Autowired
	private LanguageService languageService;
	
	@ModelAttribute
	public Language get(@RequestParam(required=false) String id) {
		Language entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = languageService.get(id);
		}
		if (entity == null){
			entity = new Language();
		}
		return entity;
	}
	
	/**
	 * 语言配置列表页面
	 */
	@RequiresPermissions("sys:language:list")
	@RequestMapping(value = {"list", ""})
	public String list(Language language, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Language> page = languageService.findPage(new Page<Language>(request, response), language); 
		model.addAttribute("page", page);
		return "modules/sys/languageList";
	}

	/**
	 * 查看，增加，编辑语言配置表单页面
	 */
	@RequiresPermissions(value={"sys:language:view","sys:language:add","sys:language:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Language language, Model model) {
		model.addAttribute("language", language);
		return "modules/sys/languageForm";
	}

	/**
	 * 保存语言配置
	 */
	@RequiresPermissions(value={"sys:language:add","sys:language:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Language language, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, language)){
			return form(language, model);
		}
		if(!language.getIsNewRecord()){//编辑表单保存
			Language t = languageService.get(language.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(language, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			languageService.save(t);//保存
		}else{//新增表单保存
			languageService.save(language);//保存
		}
		addMessage(redirectAttributes, DictUtils.getLanguageLabel("保存成功"));
		return "redirect:"+Global.getAdminPath()+"/sys/language/?repage";
	}
	
	/**
	 * 删除语言配置
	 */
	@RequiresPermissions("sys:language:del")
	@RequestMapping(value = "delete")
	public String delete(Language language, RedirectAttributes redirectAttributes) {
		languageService.delete(language);
		addMessage(redirectAttributes, DictUtils.getLanguageLabel("删除成功"));
		return "redirect:"+Global.getAdminPath()+"/sys/language/?repage";
	}
	
	/**
	 * 批量删除语言配置
	 */
	@RequiresPermissions("sys:language:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			languageService.delete(languageService.get(id));
		}
		addMessage(redirectAttributes, DictUtils.getLanguageLabel("删除成功"));
		return "redirect:"+Global.getAdminPath()+"/sys/language/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("sys:language:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Language language, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "语言配置"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Language> page = languageService.findPage(new Page<Language>(request, response, -1), language);
    		new ExportExcel("语言配置", Language.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出语言配置记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/language/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sys:language:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Language> list = ei.getDataList(Language.class);
			for (Language language : list){
				try{
					languageService.save(language);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条语言配置记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条语言配置记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入语言配置失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/language/?repage";
    }
	
	/**
	 * 下载导入语言配置数据模板
	 */
	@RequiresPermissions("sys:language:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "语言配置数据导入模板.xlsx";
    		List<Language> list = Lists.newArrayList(); 
    		new ExportExcel("语言配置数据", Language.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/language/?repage";
    }
	
	/**
	 * 语言切换
	 */
	@RequestMapping(value = "language")
	public ModelAndView language(HttpServletRequest request,HttpServletResponse response,String language){
		
		language=language.toLowerCase();
		if(language==null||language.equals("")){
			return new ModelAndView("redirect:/");
		}else{
			if(language.equals("zh_cn")){
				resolver.setLocale(request, response, Locale.CHINA );
			}else if(language.equals("hk")){
				resolver.setLocale(request, response, new Locale("hk", "HK"));
			}else if(language.equals("en")){
				resolver.setLocale(request, response, Locale.ENGLISH );
			}else if(language.equals("vn")){
				resolver.setLocale(request, response, new Locale("vn", "VN"));
			}else if(language.equals("cam")){
				resolver.setLocale(request, response, new Locale("cam", "CAM"));
			}else{
				resolver.setLocale(request, response, Locale.CHINA );
			}
			languageService.updateLanguage(language, UserUtils.getUser().getId());
			//add by J 删除缓存字典表
			Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>)CacheUtils.get(DictUtils.CACHE_DICT_MAP);
			if(dictMap!=null){
				CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
			}
			//end
		}
		
		return new ModelAndView("redirect:/");
	}
	
	/**
	 * 演示一些消息
	 */
	@RequestMapping("something")
	public void something(){}
	
	/**
	 * 换页面
	 */
	@RequestMapping("welcome")
	public void welcome(){}
	
	
	/*@ApiOperation(notes = "getLanguageMap", httpMethod = "GET", value = "获取国际化字典")
	@RequestMapping(value = "getLanguageMap", method = RequestMethod.GET)
	@ResponseBody*/
	@RequestMapping(value = "getLanguageMap")
	public Map<String,String > getLanguageMap()  {
		JSONObject its =DictUtils.getLanguageMap();
		Map<String,String > itmap=new HashMap<>();
		if(its!=null){
			Set<String>keys= its.keySet();
			for(String key:keys){
				itmap.put(key, its.getString(key));
			}
		}
		return itmap;
	}

}