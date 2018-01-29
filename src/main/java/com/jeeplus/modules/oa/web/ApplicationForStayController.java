/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.oa.web;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.Encodes;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.oa.entity.ApplicationForStay;
import com.jeeplus.modules.oa.service.ApplicationForStayService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 入住申请Controller
 * @author LiuJian
 * @version 2018-01-20
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/applicationForStay")
public class ApplicationForStayController extends BaseController {

	@Autowired
	private ApplicationForStayService applicationForStayService;
	static public JSONObject processStatForm=JSON.parseObject(
		    "{'deptLeaderAudit':'applicationForStayAudit',"
		  + " 'genLeaderAudit':'applicationForStayAudit',"
		  + " 'reservationChange':'applicationForStayAudit',"
		  + " 'checkIn':'applicationForStayAudit',"
		  + " 'checkOut':'applicationForStayAudit',"
		  + " 'end':'applicationForStayView'}");
	
	@ModelAttribute
	public ApplicationForStay get(@RequestParam(required=false) String id) {
		ApplicationForStay entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = applicationForStayService.get(id);
		}
		if (entity == null){
			entity = new ApplicationForStay();
		}
		return entity;
	}
	
	/**
	 * 入住申请列表页面
	 */
	@RequiresPermissions("oa:applicationForStay:list")
	@RequestMapping(value = {"list", ""})
	public String list(ApplicationForStay applicationForStay, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ApplicationForStay> page = applicationForStayService.findPage(new Page<ApplicationForStay>(request, response), applicationForStay); 
		model.addAttribute("page", page);
		return "modules/oa/applicationForStayList";
	}

	/**
	 * 查看，增加，编辑入住申请表单页面
	 */
	@RequiresPermissions(value={"oa:applicationForStay:view","oa:applicationForStay:add","oa:applicationForStay:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ApplicationForStay applicationForStay, Model model) {
		String view = processStatForm.containsKey(applicationForStay.getAct().getTaskDefKey()) 
				? processStatForm.getString(applicationForStay.getAct().getTaskDefKey()) : "applicationForStayForm";
	
		if(applicationForStay.getAct()!=null&&applicationForStay.getAct().getTaskName()!=null){
			applicationForStay.getAct().setTaskName(Encodes.urlDecode(applicationForStay.getAct().getTaskName()));
		}
		if(applicationForStay.getAct()!=null&&applicationForStay.getAct().getComment()!=null){
			applicationForStay.getAct().setComment(Encodes.urlDecode(applicationForStay.getAct().getComment()));
		}

		if(applicationForStay.getCreateBy()==null){
			applicationForStay.setCreateBy(UserUtils.getUser());
		}
		else if(applicationForStay.getCreateBy().getLoginName()==null||
				applicationForStay.getCreateBy().getId()!=null){
			applicationForStay.setCreateBy(UserUtils.get(applicationForStay.getCreateBy().getId()));
		}
		model.addAttribute("applicationForStay", applicationForStay);
		return "modules/oa/"+view;
	}

	/**
	 * 保存入住申请
	 */
	@RequiresPermissions(value={"oa:applicationForStay:add","oa:applicationForStay:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ApplicationForStay applicationForStay, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, applicationForStay)){
			return form(applicationForStay, model);
		}
		if(!applicationForStay.getIsNewRecord()){//编辑表单保存
			ApplicationForStay t = applicationForStayService.get(applicationForStay.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(applicationForStay, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			applicationForStayService.save(t);//保存
		}else{//新增表单保存
			applicationForStayService.save(applicationForStay);//保存
		}
		addMessage(redirectAttributes, "保存入住申请成功");
		return "redirect:"+Global.getAdminPath()+"/oa/applicationForStay/form?id="+applicationForStay.getId();
	}
	
	/**
	 * 工单执行（完成任务）
	 * @param testAudit
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"oa:applicationForStay:add","oa:applicationForStay:edit"},logical=Logical.OR)
	@RequestMapping(value = "saveAudit")
	public String saveAudit(ApplicationForStay applicationForStay, Model model) {
		try{
			if (StringUtils.isBlank(applicationForStay.getAct().getComment())&&
				!"checkIn".equals(applicationForStay.getAct().getTaskDefKey())&&
				!"checkOut".equals(applicationForStay.getAct().getTaskDefKey())&&
				!"reservationChange".equals(applicationForStay.getAct().getTaskDefKey())){
				addMessage(model, "请填写审核意见。");
				return form(applicationForStay, model);
			}
			applicationForStayService.auditSave(applicationForStay);
		}catch(Exception e){
			logger.error("入住申请审批流程失败：", e);
			addMessage(model, "系统内部错误！");
		}
		return "redirect:" + adminPath + "/act/task";
	}
	
	/**
	 * 删除入住申请
	 */
	@RequiresPermissions("oa:applicationForStay:del")
	@RequestMapping(value = "delete")
	public String delete(ApplicationForStay applicationForStay, RedirectAttributes redirectAttributes) {
		applicationForStayService.delete(applicationForStay);
		addMessage(redirectAttributes, "删除入住申请成功");
		return "redirect:"+Global.getAdminPath()+"/oa/applicationForStay/?repage";
	}
	
	/**
	 * 批量删除入住申请
	 */
	@RequiresPermissions("oa:applicationForStay:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			applicationForStayService.delete(applicationForStayService.get(id));
		}
		addMessage(redirectAttributes, "删除入住申请成功");
		return "redirect:"+Global.getAdminPath()+"/oa/applicationForStay/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("oa:applicationForStay:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(ApplicationForStay applicationForStay, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "入住申请"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ApplicationForStay> page = applicationForStayService.findPage(new Page<ApplicationForStay>(request, response, -1), applicationForStay);
    		new ExportExcel("入住申请", ApplicationForStay.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出入住申请记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/oa/applicationForStay/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("oa:applicationForStay:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ApplicationForStay> list = ei.getDataList(ApplicationForStay.class);
			for (ApplicationForStay applicationForStay : list){
				try{
					applicationForStayService.save(applicationForStay);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条入住申请记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条入住申请记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入入住申请失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/oa/applicationForStay/?repage";
    }
	
	/**
	 * 下载导入入住申请数据模板
	 */
	@RequiresPermissions("oa:applicationForStay:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "入住申请数据导入模板.xlsx";
    		List<ApplicationForStay> list = Lists.newArrayList(); 
    		new ExportExcel("入住申请数据", ApplicationForStay.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/oa/applicationForStay/?repage";
    }
	
}