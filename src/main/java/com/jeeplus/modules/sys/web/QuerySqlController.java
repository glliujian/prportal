/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
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
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.sys.entity.QuerySql;
import com.jeeplus.modules.sys.service.QuerySqlService;

/**
 * 自定义查询Controller
 * @author xj
 * @version 2017-04-21
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/querySql")
public class QuerySqlController extends BaseController {

	@Autowired
	private QuerySqlService querySqlService;
	
	@ModelAttribute
	public QuerySql get(@RequestParam(required=false) String id) {
		QuerySql entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = querySqlService.get(id);
		}
		if (entity == null){
			entity = new QuerySql();
		}
		return entity;
	}
	
	/**
	 * 自定义查询列表页面
	 */
	@RequiresPermissions("sys:querySql:list")
	@RequestMapping(value = {"list", ""})
	public String list(QuerySql querySql, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<QuerySql> page = querySqlService.findPage(new Page<QuerySql>(request, response), querySql); 
		model.addAttribute("page", page);
		return "modules/sys/querySqlList";
	}

	/**
	 * 查看，增加，编辑自定义查询表单页面
	 */
	@RequiresPermissions(value={"sys:querySql:view","sys:querySql:add","sys:querySql:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(QuerySql querySql, Model model) {
		model.addAttribute("querySql", querySql);
		return "modules/sys/querySqlForm";
	}

	/**
	 * 保存自定义查询
	 */
	@RequiresPermissions(value={"sys:querySql:add","sys:querySql:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(QuerySql querySql, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, querySql)){
			return form(querySql, model);
		}
		if(!querySql.getIsNewRecord()){//编辑表单保存
			QuerySql t = querySqlService.get(querySql.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(querySql, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			querySqlService.save(t);//保存
		}else{//新增表单保存
			querySqlService.save(querySql);//保存
		}
		addMessage(redirectAttributes, "保存自定义查询成功");
		return "redirect:"+Global.getAdminPath()+"/sys/querySql/?repage";
	}
	
	/**
	 * 删除自定义查询
	 */
	@RequiresPermissions("sys:querySql:del")
	@RequestMapping(value = "delete")
	public String delete(QuerySql querySql, RedirectAttributes redirectAttributes) {
		querySqlService.delete(querySql);
		addMessage(redirectAttributes, "删除自定义查询成功");
		return "redirect:"+Global.getAdminPath()+"/sys/querySql/?repage";
	}
	
	/**
	 * 批量删除自定义查询
	 */
	@RequiresPermissions("sys:querySql:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			querySqlService.delete(querySqlService.get(id));
		}
		addMessage(redirectAttributes, "删除自定义查询成功");
		return "redirect:"+Global.getAdminPath()+"/sys/querySql/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("sys:querySql:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(QuerySql querySql, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "自定义查询"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<QuerySql> page = querySqlService.findPage(new Page<QuerySql>(request, response, -1), querySql);
    		new ExportExcel("自定义查询", QuerySql.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出自定义查询记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/querySql/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sys:querySql:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<QuerySql> list = ei.getDataList(QuerySql.class);
			for (QuerySql querySql : list){
				try{
					querySqlService.save(querySql);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条自定义查询记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条自定义查询记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入自定义查询失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/querySql/?repage";
    }
	
	/**
	 * 下载导入自定义查询数据模板
	 */
	@RequiresPermissions("sys:querySql:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "自定义查询数据导入模板.xlsx";
    		List<QuerySql> list = Lists.newArrayList(); 
    		new ExportExcel("自定义查询数据", QuerySql.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/querySql/?repage";
    }
	
	
	

}