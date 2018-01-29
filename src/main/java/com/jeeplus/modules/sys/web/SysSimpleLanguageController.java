/**
 * MFG
 */
package com.jeeplus.modules.sys.web;

import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.sys.entity.SysSimpleLanguage;
import com.jeeplus.modules.sys.service.SysSimpleLanguageService;
import com.jeeplus.modules.sys.utils.DictUtils;

/**
 * 国际化语言设置Controller
 * @author Jack
 * @version 2017-08-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysSimpleLanguage")
public class SysSimpleLanguageController extends BaseController {

	@Autowired
	private SysSimpleLanguageService sysSimpleLanguageService;
	
	@ModelAttribute
	public SysSimpleLanguage get(@RequestParam(required=false) String id) {
		SysSimpleLanguage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysSimpleLanguageService.get(id);
		}
		if (entity == null){
			entity = new SysSimpleLanguage();
		}
		return entity;
	}
	
	/**
	 * 国际化语言设置列表页面
	 */
	@RequiresPermissions("sys:sysSimpleLanguage:list")
	@RequestMapping(value = {"list", ""})
	public String list(SysSimpleLanguage sysSimpleLanguage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysSimpleLanguage> page = sysSimpleLanguageService.findPage(new Page<SysSimpleLanguage>(request, response), sysSimpleLanguage); 
		model.addAttribute("page", page);
		return "modules/sys/sysSimpleLanguageList";
	}

	/**
	 * 查看，增加，编辑国际化语言设置表单页面
	 */
	@RequiresPermissions(value={"sys:sysSimpleLanguage:view","sys:sysSimpleLanguage:add","sys:sysSimpleLanguage:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SysSimpleLanguage sysSimpleLanguage, Model model, HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("sysSimpleLanguage", sysSimpleLanguage);
		model.addAttribute("pageSize", request.getParameter("pageSize"));
		return "modules/sys/sysSimpleLanguageForm";
	}

	/**
	 * 保存国际化语言设置
	 */
	@RequiresPermissions(value={"sys:sysSimpleLanguage:add","sys:sysSimpleLanguage:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(SysSimpleLanguage sysSimpleLanguage, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if (!beanValidator(model, sysSimpleLanguage)){
			return form(sysSimpleLanguage, model, request, response);
		}
		if(!sysSimpleLanguage.getIsNewRecord()){//编辑表单保存
			SysSimpleLanguage t = sysSimpleLanguageService.get(sysSimpleLanguage.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(sysSimpleLanguage, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			sysSimpleLanguageService.save(t);//保存
		}else{//新增表单保存
			sysSimpleLanguageService.save(sysSimpleLanguage);//保存
		}
		addMessage(redirectAttributes, DictUtils.getLanguageLabel("保存成功"));
		return "redirect:"+Global.getAdminPath()+"/sys/sysSimpleLanguage/?repage&pageSize="+request.getParameter("pageSize");
	}
	
	/**
	 * 删除国际化语言设置
	 */
	@RequiresPermissions("sys:sysSimpleLanguage:del")
	@RequestMapping(value = "delete")
	public String delete(SysSimpleLanguage sysSimpleLanguage, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
		sysSimpleLanguageService.delete(sysSimpleLanguage);
		addMessage(redirectAttributes, DictUtils.getLanguageLabel("删除成功"));
		return "redirect:"+Global.getAdminPath()+"/sys/sysSimpleLanguage/?repage&pageSize="+request.getParameter("pageSize");
	}
	
	/**
	 * 批量删除国际化语言设置
	 */
	@RequiresPermissions("sys:sysSimpleLanguage:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			sysSimpleLanguageService.delete(sysSimpleLanguageService.get(id));
		}
		addMessage(redirectAttributes, DictUtils.getLanguageLabel("删除成功"));
		return "redirect:"+Global.getAdminPath()+"/sys/sysSimpleLanguage/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("sys:sysSimpleLanguage:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(SysSimpleLanguage sysSimpleLanguage, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "国际化语言设置"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SysSimpleLanguage> page = sysSimpleLanguageService.findPage(new Page<SysSimpleLanguage>(request, response, -1), sysSimpleLanguage);
    		new ExportExcel("国际化语言设置", SysSimpleLanguage.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出国际化语言设置记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/sysSimpleLanguage/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sys:sysSimpleLanguage:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SysSimpleLanguage> list = ei.getDataList(SysSimpleLanguage.class);
			for (SysSimpleLanguage sysSimpleLanguage : list){
				try{
					sysSimpleLanguageService.save(sysSimpleLanguage);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条国际化语言设置记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条国际化语言设置记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入国际化语言设置失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/sysSimpleLanguage/?repage";
    }
	
	/**
	 * 下载导入国际化语言设置数据模板
	 */
	@RequiresPermissions("sys:sysSimpleLanguage:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "国际化语言设置数据导入模板.xlsx";
    		List<SysSimpleLanguage> list = Lists.newArrayList(); 
    		new ExportExcel("国际化语言设置数据", SysSimpleLanguage.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/sysSimpleLanguage/?repage";
    }
	
	
	

}