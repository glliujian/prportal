/**
 * Copyright &copy; 2014-2017 <a href="http://www.jeeplus.cn/">jeeplus</a> All rights reserved.
 */
package com.jeeplus.modules.act.web.restful;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.modules.act.service.ActProcessService;
import com.linkgoo.api.constant.APPConst;
import com.linkgoo.web.common.TokenUtil;
import io.swagger.annotations.Api;

/**
 * 流程定义相关Controller
 * 
 * @author jeeplus
 * @version 2013-11-03
 */
@Controller
@RequestMapping(value = "/api/process")
@Api(value="RestActProcessController",description="测试接口描述")
public class RestActProcessController {

	@Autowired
	private ActProcessService actProcessService;

	@Autowired
	private HistoryService historyService;
    
/*	*//**
	 * 流程定义列表
	 *//*
	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ResponseEntity<?> processList(@RequestParam String category,
			HttpServletRequest request, HttpServletResponse response) {
		
		 * 保存两个对象，一个是ProcessDefinition（流程定义），一个是Deployment（流程部署）
		 
		Page<Object[]> page = actProcessService.processList(new Page<Object[]>(
				request, response), category);

		Map<String, Object> result = TokenUtil.status(APPConst.SUCESS_CODE,
				request.getLocale());
		result.put("page",  new User() );
		result.put("category", category);
		return ResponseEntity.ok(result);
	}
*/
	/**
	 * 运行中的实例列表
	 */
	@ResponseBody
	@RequestMapping(value = "running", method = RequestMethod.GET)
	public ResponseEntity<?> runningList(@RequestParam String procInsId,
			@RequestParam String procDefKey, HttpServletRequest request,
			HttpServletResponse response) {
		Page<ProcessInstance> page = actProcessService.runningList(
				new Page<ProcessInstance>(request, response), procInsId,
				procDefKey);
		Map<String, Object> result = TokenUtil.status(APPConst.SUCESS_CODE,
				request.getLocale());
	 
		result.put("page", new Page());
		result.put("procInsId", procInsId);
		result.put("procDefKey", procDefKey);
		return ResponseEntity.ok(result);
	}

	/**
	 * 已结束的实例
	 */
	@ResponseBody
	@RequestMapping(value = "historyList", method = RequestMethod.GET)
	public ResponseEntity<?> historyList(@RequestParam String procInsId,
			@RequestParam String procDefKey, HttpServletRequest request,
			HttpServletResponse response) {
		Page<HistoricProcessInstance> page = actProcessService.historyList(
				new Page<HistoricProcessInstance>(request, response),
				procInsId, procDefKey);

		Map<String, Object> result = TokenUtil.status(APPConst.SUCESS_CODE,
				request.getLocale());
		result.put("page", page);
		result.put("procInsId", procInsId);
		result.put("procDefKey", procDefKey);
		return ResponseEntity.ok(result);
	}

	/**
	 * 设置流程分类
	 */
	@ResponseBody
	@RequestMapping(value = "updateCategory", method = RequestMethod.POST)
	public ResponseEntity<?> updateCategory(@RequestParam String procDefId,
			@RequestParam String category, HttpServletRequest request) {
		actProcessService.updateCategory(procDefId, category);
		Map<String, Object> result = TokenUtil.status(APPConst.SUCESS_CODE,
				request.getLocale());
		return ResponseEntity.ok(result);
	}

	/**
	 * 挂起、激活流程实例
	 */
	@ResponseBody
	@RequestMapping(value = "update/{state}")
	public ResponseEntity<?> updateState(@PathVariable("state") String state,
			HttpServletRequest request, String procDefId) {
		String message = actProcessService.updateState(state, procDefId);
		Map<String, Object> result = TokenUtil.status(APPConst.SUCESS_CODE,
				request.getLocale());
		result.put("message", message);
		return ResponseEntity.ok(result);
	}
}
