/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.wf.web.usergroup;

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
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.wf.entity.usergroup.WfUserGroup;
import com.jeeplus.modules.wf.service.usergroup.WfUserGroupService;

/**
 * 工作流用户组Controller
 * @author xj
 * @version 2017-06-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wf/usergroup/wfUserGroup")
public class WfUserGroupController extends BaseController {
	//private static LanguageService languageService = SpringContextHolder.getBean(LanguageService.class);
	@Autowired
	private WfUserGroupService wfUserGroupService;
	
	@ModelAttribute
	public WfUserGroup get(@RequestParam(required=false) String id) {
		WfUserGroup entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wfUserGroupService.get(id);
		}
		if (entity == null){
			entity = new WfUserGroup();
		}
		return entity;
	}
	
	/**
	 * 工作流用户组列表页面
	 */
	@RequiresPermissions("wf:usergroup:wfUserGroup:list")
	@RequestMapping(value = {"list", ""})
	public String list(WfUserGroup wfUserGroup, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WfUserGroup> page = wfUserGroupService.findPage(new Page<WfUserGroup>(request, response), wfUserGroup); 
		model.addAttribute("page", page);
		return "modules/wf/usergroup/wfUserGroupList";
	}

	/**
	 * 查看，增加，编辑工作流用户组表单页面
	 */
	@RequiresPermissions(value={"wf:usergroup:wfUserGroup:view","wf:usergroup:wfUserGroup:add","wf:usergroup:wfUserGroup:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WfUserGroup wfUserGroup, Model model) {
		model.addAttribute("wfUserGroup", wfUserGroup);
		return "modules/wf/usergroup/wfUserGroupForm";
	}

	/**
	 * 保存工作流用户组
	 */
	@RequiresPermissions(value={"wf:usergroup:wfUserGroup:add","wf:usergroup:wfUserGroup:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WfUserGroup wfUserGroup, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, wfUserGroup)){
			return form(wfUserGroup, model);
		}
		if(!wfUserGroup.getIsNewRecord()){//编辑表单保存
			WfUserGroup t = wfUserGroupService.get(wfUserGroup.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(wfUserGroup, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			wfUserGroupService.save(t);//保存
		}else{//新增表单保存
			wfUserGroupService.save(wfUserGroup);//保存
		}
		addMessage(redirectAttributes, DictUtils.getLanguageLabel("保存工作流用户组成功"));
		return "redirect:"+Global.getAdminPath()+"/wf/usergroup/wfUserGroup/?repage";
	}
	
	/**
	 * 删除工作流用户组
	 */
	@RequiresPermissions("wf:usergroup:wfUserGroup:del")
	@RequestMapping(value = "delete")
	public String delete(WfUserGroup wfUserGroup, RedirectAttributes redirectAttributes) {
		wfUserGroupService.delete(wfUserGroup);
		addMessage(redirectAttributes, DictUtils.getLanguageLabel("删除工作流用户组成功"));
		return "redirect:"+Global.getAdminPath()+"/wf/usergroup/wfUserGroup/?repage";
	}
	
	/**
	 * 批量删除工作流用户组
	 */
	@RequiresPermissions("wf:usergroup:wfUserGroup:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			wfUserGroupService.delete(wfUserGroupService.get(id));
		}
		addMessage(redirectAttributes, DictUtils.getLanguageLabel("删除工作流用户组成功"));
		return "redirect:"+Global.getAdminPath()+"/wf/usergroup/wfUserGroup/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("wf:usergroup:wfUserGroup:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WfUserGroup wfUserGroup, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = DictUtils.getLanguageLabel("工作流用户组")+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WfUserGroup> page = wfUserGroupService.findPage(new Page<WfUserGroup>(request, response, -1), wfUserGroup);
    		new ExportExcel(DictUtils.getLanguageLabel("工作流用户组"), WfUserGroup.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, DictUtils.getLanguageLabel("导出工作流用户组记录失败！失败信息：")+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wf/usergroup/wfUserGroup/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("wf:usergroup:wfUserGroup:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WfUserGroup> list = ei.getDataList(WfUserGroup.class);
			for (WfUserGroup wfUserGroup : list){
				try{
					wfUserGroupService.save(wfUserGroup);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, DictUtils.getLanguageLabel("，失败")+failureNum+DictUtils.getLanguageLabel("条工作流用户组记录。"));
			}
			addMessage(redirectAttributes, DictUtils.getLanguageLabel("已成功导入")+successNum+DictUtils.getLanguageLabel("条工作流用户组记录")+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, DictUtils.getLanguageLabel("导入工作流用户组失败！失败信息：")+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wf/usergroup/wfUserGroup/?repage";
    }
	
	/**
	 * 下载导入工作流用户组数据模板
	 */
	@RequiresPermissions("wf:usergroup:wfUserGroup:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = DictUtils.getLanguageLabel("工作流用户组数据导入模板")+".xlsx";
    		List<WfUserGroup> list = Lists.newArrayList(); 
    		new ExportExcel(DictUtils.getLanguageLabel("工作流用户组数据"), WfUserGroup.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, DictUtils.getLanguageLabel("导入模板下载失败！失败信息：")+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wf/usergroup/wfUserGroup/?repage";
    }
	
	
	

}