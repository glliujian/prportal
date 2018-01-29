/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.wf.web.assignee;

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
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.wf.entity.assignee.WfAssignee;
import com.jeeplus.modules.wf.service.assignee.WfAssigneeService;

/**
 * 工作流處理人設置Controller
 * @author XJ
 * @version 2017-04-28
 */
@Controller
@RequestMapping(value = "${adminPath}/wf/assignee/wfAssignee")
public class WfAssigneeController extends BaseController {

	@Autowired
	private WfAssigneeService wfAssigneeService;
	
	@ModelAttribute
	public WfAssignee get(@RequestParam(required=false) String id) {
		WfAssignee entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wfAssigneeService.get(id);
		}
		if (entity == null){
			entity = new WfAssignee();
		}
		return entity;
	}
	
	/**
	 * 工作流處理人設置列表页面
	 */
	@RequiresPermissions("wf:assignee:wfAssignee:list")
	@RequestMapping(value = {"list", ""})
	public String list(WfAssignee wfAssignee, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WfAssignee> page = wfAssigneeService.findPage(new Page<WfAssignee>(request, response), wfAssignee); 
		model.addAttribute("page", page);
		return "modules/wf/assignee/wfAssigneeList";
	}

	/**
	 * 查看，增加，编辑工作流處理人設置表单页面
	 */
	@RequiresPermissions(value={"wf:assignee:wfAssignee:view","wf:assignee:wfAssignee:add","wf:assignee:wfAssignee:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WfAssignee wfAssignee, Model model) {
		model.addAttribute("wfAssignee", wfAssignee);
		return "modules/wf/assignee/wfAssigneeForm";
	}

	/**
	 * 保存工作流處理人設置
	 */
	@RequiresPermissions(value={"wf:assignee:wfAssignee:add","wf:assignee:wfAssignee:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WfAssignee wfAssignee, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, wfAssignee)){
			return form(wfAssignee, model);
		}
		if(!wfAssignee.getIsNewRecord()){//编辑表单保存
			WfAssignee t = wfAssigneeService.get(wfAssignee.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(wfAssignee, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			wfAssigneeService.save(t);//保存
		}else{//新增表单保存
			wfAssigneeService.save(wfAssignee);//保存
		}
		addMessage(redirectAttributes, "保存工作流處理人設置成功");
		return "redirect:"+Global.getAdminPath()+"/wf/assignee/wfAssignee/?repage";
	}
	
	/**
	 * 删除工作流處理人設置
	 */
	@RequiresPermissions("wf:assignee:wfAssignee:del")
	@RequestMapping(value = "delete")
	public String delete(WfAssignee wfAssignee, RedirectAttributes redirectAttributes) {
		wfAssigneeService.delete(wfAssignee);
		addMessage(redirectAttributes, "删除工作流處理人設置成功");
		return "redirect:"+Global.getAdminPath()+"/wf/assignee/wfAssignee/?repage";
	}
	
	/**
	 * 批量删除工作流處理人設置
	 */
	@RequiresPermissions("wf:assignee:wfAssignee:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			wfAssigneeService.delete(wfAssigneeService.get(id));
		}
		addMessage(redirectAttributes, "删除工作流處理人設置成功");
		return "redirect:"+Global.getAdminPath()+"/wf/assignee/wfAssignee/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("wf:assignee:wfAssignee:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WfAssignee wfAssignee, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工作流處理人設置"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WfAssignee> page = wfAssigneeService.findPage(new Page<WfAssignee>(request, response, -1), wfAssignee);
    		new ExportExcel("工作流處理人設置", WfAssignee.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出工作流處理人設置记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wf/assignee/wfAssignee/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("wf:assignee:wfAssignee:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WfAssignee> list = ei.getDataList(WfAssignee.class);
			for (WfAssignee wfAssignee : list){
				try{
					wfAssigneeService.save(wfAssignee);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条工作流處理人設置记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条工作流處理人設置记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入工作流處理人設置失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wf/assignee/wfAssignee/?repage";
    }
	
	/**
	 * 下载导入工作流處理人設置数据模板
	 */
	@RequiresPermissions("wf:assignee:wfAssignee:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工作流處理人設置数据导入模板.xlsx";
    		List<WfAssignee> list = Lists.newArrayList(); 
    		new ExportExcel("工作流處理人設置数据", WfAssignee.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wf/assignee/wfAssignee/?repage";
    }
	
	
	

}