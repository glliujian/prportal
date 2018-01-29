/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.oa.web;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.Encodes;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.oa.entity.Trip;
import com.jeeplus.modules.oa.entity.TripArrangements;
import com.jeeplus.modules.oa.service.TripService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 出差申请Controller
 * @author ml
 * @version 2018-01-18
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/trip")
public class TripController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static Map<String, String> processStatForm = new HashMap<String, String>();

	//不同状态对应的页面
	static{
		processStatForm.put("tripStart", 		"tripForm");//启动
		processStatForm.put("deptLeaderAudit", 	"tripAudit");//部门领导审批
		processStatForm.put("genLeaderAudit", 	"tripAudit");//总经理审批
		processStatForm.put("CEOAudit", 	"tripAudit");//CEO审批
		processStatForm.put("deputyChairmanCheck", 	"tripAudit");//副总裁审批
		processStatForm.put("HRRecord", 		"tripView");//HR备案
		processStatForm.put("tripChange", 		"tripAudit");//变更申请
		processStatForm.put("tripEnd", 			"tripList");//申请结束
	}

	@Autowired
	private TripService tripService;

	@ModelAttribute
	public Trip get(@RequestParam(required=false) String id) {
		Trip entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tripService.get(id);
		}
		if (entity == null){
			entity = new Trip();
		}
		return entity;
	}

	/**
	 * 出差申请列表页面
	 */
	@RequiresPermissions("oa:trip:list")
	@RequestMapping(value = {"list", ""})
	public String list(Trip trip, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Trip> page = tripService.findPage(new Page<Trip>(request, response), trip); 
		model.addAttribute("page", page);
		return "modules/oa/tripList";
	}

	/**
	 * 查看，增加，编辑出差申请表单页面
	 */
	//@RequiresPermissions(value={"oa:trip:view","oa:trip:add","oa:trip:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Trip trip, Model model) {
		String view = "tripForm";
		try {
			view = processStatForm.containsKey(trip.getAct().getTaskDefKey()) 
					? processStatForm.get(trip.getAct().getTaskDefKey()) : "tripForm";
			
			if ("tripForm".equals(view)) {
				trip.setCreateBy(UserUtils.getUser());
			}
			
			if (null != trip.getAct()&&trip.getAct().getTaskName()!=null) {
				trip.getAct().setTaskName(URLDecoder.decode(trip.getAct().getTaskName(),"utf-8"));
			}

			if(trip.getArrangements()!=null&&!"".equals(trip.getArrangements())){
				trip.setArrangements(URLDecoder.decode(trip.getArrangements(),"utf-8"));
				List<TripArrangements> it=JSON.parseArray(trip.getArrangements(), TripArrangements.class);
				trip.setArrangementOBJs(it);
			}
			else{
				trip.setArrangements("[]");
			}
			model.addAttribute("trip", trip);
		}catch(Exception e) {
			e.printStackTrace();
			logger.debug("表单选择失败 {}",e);
		}

		return "modules/oa/"+view;
	}

	/**
	 * 保存出差申请
	 */
	@RequiresPermissions(value={"oa:trip:add","oa:trip:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Trip trip, Model model, RedirectAttributes redirectAttributes) throws Exception{
		try{
			if (!beanValidator(model, trip)){
				return form(trip, model);
			}
			trip.setArrangements(Encodes.urlDecode(trip.getArrangements()));
			if(!trip.getIsNewRecord()){//编辑表单保存
				Trip t = tripService.get(trip.getId());//从数据库取出记录的值
				MyBeanUtils.copyBeanNotNull2Bean(trip, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
				tripService.save(t);//保存
			}else{//新增表单保存
				tripService.save(trip);//保存
			}
			addMessage(redirectAttributes, "出差申请已经提交");
		}catch(Exception e){
			logger.error("启动出差流程失败：", e);
			addMessage(redirectAttributes, "系统内部错误！");
		}
		return "redirect:" + adminPath + "/act/task/process/";
		//return "redirect:"+Global.getAdminPath()+"/oa/trip/?repage";
	}

	/**
	 * 备份出差申请信息
	 */
	@RequiresPermissions(value={"oa:trip:edit"},logical=Logical.OR)
	@RequestMapping(value = "record")
	public String record(Trip trip, Model model, RedirectAttributes redirectAttributes) throws Exception{
		try{
			trip.setArrangements(Encodes.urlDecode(trip.getArrangements()));
			tripService.auditSave(trip);//保存
			addMessage(redirectAttributes, "出差申请流程已完成");
		}catch(Exception e){
			logger.error("启动出差申请流程失败：", e);
			addMessage(redirectAttributes, "系统内部错误！");
		}
		addMessage(redirectAttributes, "出差申请提交成功");
		return "redirect:" + adminPath + "/act/task/process/";
	}

	/**
	 * 工单执行（完成任务）
	 * @param testAudit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveAudit")
	public String saveAudit(Trip trip, Model model) {
		try{
			if (StringUtils.isBlank(trip.getAct().getComment())&&
				!"tripChange".equals(trip.getAct().getTaskDefKey())){
				addMessage(model, "请填写审核意见。");
				return form(trip, model);
			}
			trip.setArrangements(Encodes.urlDecode(trip.getArrangements()));
			tripService.auditSave(trip);
		}catch(Exception e){
			logger.error("出差申请审批流程失败：", e);
			addMessage(model, "系统内部错误！");
		}
		return "redirect:" + adminPath + "/act/task";
	}

	/**
	 * 删除出差申请
	 */
	@RequiresPermissions("oa:trip:del")
	@RequestMapping(value = "delete")
	public String delete(Trip trip, RedirectAttributes redirectAttributes) {
		tripService.delete(trip);
		addMessage(redirectAttributes, "删除出差申请成功");
		return "redirect:"+Global.getAdminPath()+"/oa/trip/?repage";
	}

	/**
	 * 批量删除出差申请
	 */
	@RequiresPermissions("oa:trip:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tripService.delete(tripService.get(id));
		}
		addMessage(redirectAttributes, "删除出差申请成功");
		return "redirect:"+Global.getAdminPath()+"/oa/trip/?repage";
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("oa:trip:export")
	@RequestMapping(value = "export", method=RequestMethod.POST)
	public String exportFile(Trip trip, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "出差申请"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
			Page<Trip> page = tripService.findPage(new Page<Trip>(request, response, -1), trip);
			new ExportExcel("出差申请", Trip.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出出差申请记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/oa/trip/?repage";
	}

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("oa:trip:import")
	@RequestMapping(value = "import", method=RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Trip> list = ei.getDataList(Trip.class);
			for (Trip trip : list){
				try{
					tripService.save(trip);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条出差申请记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条出差申请记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入出差申请失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/oa/trip/?repage";
	}

	/**
	 * 下载导入出差申请数据模板
	 */
	@RequiresPermissions("oa:trip:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "出差申请数据导入模板.xlsx";
			List<Trip> list = Lists.newArrayList(); 
			new ExportExcel("出差申请数据", Trip.class, 1).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/oa/trip/?repage";
	}
}