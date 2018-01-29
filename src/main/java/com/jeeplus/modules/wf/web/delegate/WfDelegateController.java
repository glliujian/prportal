/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.wf.web.delegate;

import java.util.Date;
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
import org.springframework.web.bind.annotation.ResponseBody;
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
import com.jeeplus.modules.act.entity.Act;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.wf.entity.delegate.WfDelegate;
import com.jeeplus.modules.wf.service.delegate.WfDelegateService;

/**
 * 工作流委托设置Controller
 * @author XJ
 * @version 2017-04-28
 */
@Controller
@RequestMapping(value = "${adminPath}/wf/delegate/wfDelegate")
public class WfDelegateController extends BaseController {
	//private static LanguageService languageService = SpringContextHolder.getBean(LanguageService.class);
	@Autowired
	private WfDelegateService wfDelegateService;
	
	@ModelAttribute
	public WfDelegate get(@RequestParam(required=false) String id) {
		WfDelegate entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wfDelegateService.get(id);
		}
		if (entity == null){
			entity = new WfDelegate();
		}
		return entity;
	}
	
	/**
	 * 工作流委托设置列表页面
	 */
	@RequiresPermissions("wf:delegate:wfDelegate:list")
	@RequestMapping(value = {"list", ""})
	public String list(WfDelegate wfDelegate, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WfDelegate> page = wfDelegateService.findPage(new Page<WfDelegate>(request, response), wfDelegate); 
		model.addAttribute("page", page);
		model.addAttribute("MType", request.getParameter("MType"));
		return "modules/wf/delegate/wfDelegateList";
	}

	/**
	 * 查看，增加，编辑工作流委托设置表单页面
	 */
	@RequiresPermissions(value={"wf:delegate:wfDelegate:view","wf:delegate:wfDelegate:add","wf:delegate:wfDelegate:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WfDelegate wfDelegate, Model model) {
		if (wfDelegate.getId()==null) {
			wfDelegate.setOwner(UserUtils.getUser());
			wfDelegate.setStartTime(new Date());
			wfDelegate.setEndTime(DateUtils.addDays(wfDelegate.getStartTime(), 3));
			model.addAttribute("wfDelegate", wfDelegate);
		}
		return "modules/wf/delegate/wfDelegateForm";
	}
	
	/**
	 * 查看，增加，编辑工作流委托设置表单页面
	 */
	@RequestMapping(value = "formForTask")
	public String formForTask(WfDelegate wfDelegate, HttpServletRequest request, HttpServletResponse response, Model model) {
		wfDelegate.setTaskId(request.getParameter("taskId"));
		wfDelegate.setTaskName(request.getParameter("taskName"));
		wfDelegate.setProcInsId(request.getParameter("procInsId"));
		wfDelegate.setApplicationNo(request.getParameter("applicationNo"));
		wfDelegate.setProcDefKey(request.getParameter("procDefKey"));
		wfDelegate.setOwner(UserUtils.getByLoginName(request.getParameter("assignee")));
		wfDelegate.setStartTime(new Date());
		wfDelegate.setEndTime(DateUtils.addDays(wfDelegate.getStartTime(), 3));
		model.addAttribute("wfDelegate", wfDelegate);
		return "modules/wf/delegate/wfDelegateFormForTask";
	}

	/**
	 * 保存工作流委托设置
	 */
	@RequiresPermissions(value={"wf:delegate:wfDelegate:add","wf:delegate:wfDelegate:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WfDelegate wfDelegate, Model model, RedirectAttributes redirectAttributes) throws Exception{
		String messageType = "success";
		if (!beanValidator(model, wfDelegate)){
			return form(wfDelegate, model);
		}
		try{
		if(!wfDelegate.getIsNewRecord()){//编辑表单保存
			WfDelegate t = wfDelegateService.get(wfDelegate.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(wfDelegate, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			wfDelegateService.save(t);//保存
		}else{//新增表单保存
			wfDelegateService.save(wfDelegate);//保存
		}
		}catch(Exception e){
			addMessage(redirectAttributes, e.getMessage());
			messageType = "danger";
			return "redirect:"+Global.getAdminPath()+"/wf/delegate/wfDelegate/?repage"+"&MType="+messageType;
		}
		addMessage(redirectAttributes, DictUtils.getLanguageLabel("保存工作流委托设置成功"));
		return "redirect:"+Global.getAdminPath()+"/wf/delegate/wfDelegate/?repage"+"&MType="+messageType;
	}
	
	/**
	 * 保存工作流委托设置
	 */
	@RequestMapping(value = "saveForTask")
	public String saveForTask(WfDelegate wfDelegate, Model model, RedirectAttributes redirectAttributes) throws Exception{
		String messageType = "success";
		if (!beanValidator(model, wfDelegate)){
			return form(wfDelegate, model);
		}
		try{
			if(!wfDelegate.getIsNewRecord()){//编辑表单保存
				WfDelegate t = wfDelegateService.get(wfDelegate.getId());//从数据库取出记录的值
				MyBeanUtils.copyBeanNotNull2Bean(wfDelegate, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
				wfDelegateService.save(t);//保存
			}else{//新增表单保存
				wfDelegateService.save(wfDelegate);//保存
			}
			addMessage(redirectAttributes, DictUtils.getLanguageLabel("保存工作流委托设置成功"));
		}catch (Exception e) {
			messageType = "danger";
			addMessage(redirectAttributes, e.getMessage());
			
		}
		
		model.addAttribute("act", new Act());
		return "redirect:"+Global.getAdminPath()+"/act/task/todo"+"?MType="+messageType;
	}
	
	/**
	 * 删除工作流委托设置
	 */
	@RequiresPermissions("wf:delegate:wfDelegate:del")
	@RequestMapping(value = "delete")
	public String delete(WfDelegate wfDelegate, RedirectAttributes redirectAttributes) {
		wfDelegateService.delete(wfDelegate);
		addMessage(redirectAttributes, DictUtils.getLanguageLabel("删除工作流委托设置成功"));
		return "redirect:"+Global.getAdminPath()+"/wf/delegate/wfDelegate/?repage";
	}
	
	/**
	 * 删除工作流委托设置
	 */
	@RequestMapping(value = "deleteForTask")
	@ResponseBody
	public String deleteForTask(WfDelegate wfDelegate) {
		wfDelegateService.delete(wfDelegate);
		//addMessage(redirectAttributes, "删除工作流委托设置成功");
		return "true";
	}
	
	/**
	 * 批量删除工作流委托设置
	 */
	@RequiresPermissions("wf:delegate:wfDelegate:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			wfDelegateService.delete(wfDelegateService.get(id));
		}
		addMessage(redirectAttributes, DictUtils.getLanguageLabel("删除工作流委托设置成功"));
		return "redirect:"+Global.getAdminPath()+"/wf/delegate/wfDelegate/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("wf:delegate:wfDelegate:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WfDelegate wfDelegate, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = DictUtils.getLanguageLabel("工作流委托设置")+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WfDelegate> page = wfDelegateService.findPage(new Page<WfDelegate>(request, response, -1), wfDelegate);
    		new ExportExcel(DictUtils.getLanguageLabel("工作流委托设置"), WfDelegate.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, DictUtils.getLanguageLabel("导出工作流委托设置记录失败！失败信息：")+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wf/delegate/wfDelegate/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("wf:delegate:wfDelegate:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WfDelegate> list = ei.getDataList(WfDelegate.class);
			for (WfDelegate wfDelegate : list){
				try{
					wfDelegateService.save(wfDelegate);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, DictUtils.getLanguageLabel("，失败")+failureNum+DictUtils.getLanguageLabel("条工作流委托设置记录。"));
			}
			addMessage(redirectAttributes, DictUtils.getLanguageLabel("已成功导入")+successNum+DictUtils.getLanguageLabel("条工作流委托设置记录。")+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, DictUtils.getLanguageLabel("导入工作流委托设置失败！失败信息：")+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wf/delegate/wfDelegate/?repage";
    }
	
	/**
	 * 下载导入工作流委托设置数据模板
	 */
	@RequiresPermissions("wf:delegate:wfDelegate:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = DictUtils.getLanguageLabel("工作流委托设置数据导入模板")+".xlsx";
    		List<WfDelegate> list = Lists.newArrayList(); 
    		new ExportExcel(DictUtils.getLanguageLabel("工作流委托设置数据"), WfDelegate.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, DictUtils.getLanguageLabel("导入模板下载失败！失败信息：")+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wf/delegate/wfDelegate/?repage";
    }
	
	
	

}