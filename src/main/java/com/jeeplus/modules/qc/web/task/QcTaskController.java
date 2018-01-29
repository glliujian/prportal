/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.qc.web.task;

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
import com.jeeplus.modules.qc.entity.task.QcTask;
import com.jeeplus.modules.qc.service.task.QcTaskService;

/**
 * 工作任务Controller
 * @author xj
 * @version 2017-04-15
 */
@Controller
@RequestMapping(value = "${adminPath}/qc/task/qcTask")
public class QcTaskController extends BaseController {

	@Autowired
	private QcTaskService qcTaskService;
	
	@ModelAttribute
	public QcTask get(@RequestParam(required=false) String id) {
		QcTask entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = qcTaskService.get(id);
		}
		if (entity == null){
			entity = new QcTask();
		}
		return entity;
	}
	
	/**
	 * 工作任务列表页面
	 */
	@RequiresPermissions("qc:task:qcTask:list")
	@RequestMapping(value = {"list", ""})
	public String list(QcTask qcTask, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<QcTask> page = qcTaskService.findPage(new Page<QcTask>(request, response), qcTask); 
		model.addAttribute("page", page);
		return "modules/qc/task/qcTaskList";
	}

	/**
	 * 查看，增加，编辑工作任务表单页面
	 */
	@RequiresPermissions(value={"qc:task:qcTask:view","qc:task:qcTask:add","qc:task:qcTask:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(QcTask qcTask, Model model) {
		model.addAttribute("qcTask", qcTask);
		return "modules/qc/task/qcTaskForm";
	}

	/**
	 * 保存工作任务
	 */
	@RequiresPermissions(value={"qc:task:qcTask:add","qc:task:qcTask:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(QcTask qcTask, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, qcTask)){
			return form(qcTask, model);
		}
		if(!qcTask.getIsNewRecord()){//编辑表单保存
			QcTask t = qcTaskService.get(qcTask.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(qcTask, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			qcTaskService.save(t);//保存
		}else{//新增表单保存
			qcTaskService.save(qcTask);//保存
		}
		addMessage(redirectAttributes, "保存工作任务成功");
		return "redirect:"+Global.getAdminPath()+"/qc/task/qcTask/?repage";
	}
	
	/**
	 * 删除工作任务
	 */
	@RequiresPermissions("qc:task:qcTask:del")
	@RequestMapping(value = "delete")
	public String delete(QcTask qcTask, RedirectAttributes redirectAttributes) {
		qcTaskService.delete(qcTask);
		addMessage(redirectAttributes, "删除工作任务成功");
		return "redirect:"+Global.getAdminPath()+"/qc/task/qcTask/?repage";
	}
	
	/**
	 * 批量删除工作任务
	 */
	@RequiresPermissions("qc:task:qcTask:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			qcTaskService.delete(qcTaskService.get(id));
		}
		addMessage(redirectAttributes, "删除工作任务成功");
		return "redirect:"+Global.getAdminPath()+"/qc/task/qcTask/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("qc:task:qcTask:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(QcTask qcTask, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工作任务"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<QcTask> page = qcTaskService.findPage(new Page<QcTask>(request, response, -1), qcTask);
    		new ExportExcel("工作任务", QcTask.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出工作任务记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/qc/task/qcTask/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("qc:task:qcTask:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<QcTask> list = ei.getDataList(QcTask.class);
			for (QcTask qcTask : list){
				try{
					qcTaskService.save(qcTask);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条工作任务记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条工作任务记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入工作任务失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/qc/task/qcTask/?repage";
    }
	
	/**
	 * 下载导入工作任务数据模板
	 */
	@RequiresPermissions("qc:task:qcTask:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工作任务数据导入模板.xlsx";
    		List<QcTask> list = Lists.newArrayList(); 
    		new ExportExcel("工作任务数据", QcTask.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/qc/task/qcTask/?repage";
    }
	
	
	

}