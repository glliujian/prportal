/**
 * Copyright &copy; 2014-2017 <a href="http://www.jeeplus.cn/">jeeplus</a> All rights reserved.
 */
package com.jeeplus.modules.act.web.restful;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.modules.act.entity.Act;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.act.utils.Variable;
import com.jeeplus.modules.sys.security.RestUtil;
import com.linkgoo.api.constant.APPConst;
import com.linkgoo.web.common.TokenUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 流程个人任务相关Controller
 * 
 * @author jeeplus
 * @version 2013-11-03
 */
@RequestMapping(value = "/api/task")
@Api(value = "RestActTaskController", description = "工作流引擎相关接口")
@Controller
public class RestActTaskController {

	@Autowired
	private ActTaskService actTaskService;
	

	/**
	 * 获取我的申请进度
	 * @param procDefKey
	 * @param beginDate
	 * @param endDate
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */	
	@ApiOperation(notes = "tracing", httpMethod = "GET", value = "获取我的申请进度")
	@ResponseBody
	@RequestMapping(value = "tracing", method = RequestMethod.GET)
	public ResponseEntity<?> tracingList(@RequestParam(required = false) String procDefKey, @RequestParam(required = false) Date beginDate,
			@RequestParam(required = false) Date endDate, HttpServletResponse response, HttpServletRequest request)
			throws Exception {
		String initiator = RestUtil.validateToken(request);
		Act act = new Act();
		act.setProcDefKey(procDefKey);
		act.setBeginDate(beginDate);
		act.setEndDate(endDate);
		List<Act> list = actTaskService.TracingList(act, initiator);
		Map<String, Object> result = TokenUtil.status(APPConst.SUCESS_CODE, request.getLocale());
		result.put("list", list);
		return ResponseEntity.ok(result);
	}

	/**
	 * 获取待办列表
	 * @param assignee
	 * @param procDefKey
	 * @param beginDate
	 * @param endDate
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(notes = "tracing", httpMethod = "GET", value = "获取待办任务列表")
	@ResponseBody
	@RequestMapping(value = "todo", method = RequestMethod.GET)
	public ResponseEntity<?> todoList(@RequestParam(required = false) String procDefKey,
			@RequestParam(required = false) Date beginDate, @RequestParam(required = false) Date endDate,
			HttpServletResponse response, HttpServletRequest request) throws Exception {
		String assignee = RestUtil.validateToken(request);
		Act act = new Act();
		act.setProcDefKey(procDefKey);
		act.setBeginDate(beginDate);
		act.setEndDate(endDate);
		act.setAssignee(assignee);
		List<Act> list = actTaskService.todoList(act);
		Map<String, Object> result = TokenUtil.status(APPConst.SUCESS_CODE, request.getLocale());
		result.put("list", list);
		return ResponseEntity.ok(result);
	}

	/**
	 * 获取已办任务
	 * @param assignee
	 * @param procDefKey
	 * @param beginDate
	 * @param endDate
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(notes = "tracing", httpMethod = "GET", value = "获取已办任务")
	@ResponseBody
	@RequestMapping(value = "historic", method = RequestMethod.GET)
	public ResponseEntity<?> historicList(@RequestParam(required = false) String procDefKey, @RequestParam(required = false) Date beginDate,
			@RequestParam(required = false) Date endDate, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String assignee = RestUtil.validateToken(request);
		Act act = new Act();
		act.setProcDefKey(procDefKey);
		act.setBeginDate(beginDate);
		act.setEndDate(endDate);
		act.setAssignee(assignee);
		Page<Act> page = actTaskService.historicList(new Page<Act>(request, response), act);
		Map<String, Object> result = TokenUtil.status(APPConst.SUCESS_CODE, request.getLocale());
		result.put("page", page);
		return ResponseEntity.ok(result);
	}

	/**
	 * 获取流转历史列表
	 * @param procInsId
	 * @param request
	 * @return
	 */
	@ApiOperation(notes = "tracing", httpMethod = "GET", value = "获取流转历史列表")
	@ResponseBody
	@RequestMapping(value = "histoicFlow", method = RequestMethod.GET)
	public ResponseEntity<?> histoicFlow(@RequestParam String procInsId, HttpServletRequest request) {
		String assignee = RestUtil.validateToken(request);
		Map<String, Object> result = TokenUtil.status(APPConst.SUCESS_CODE, request.getLocale());
		List<Act> histoicFlowList = actTaskService.histoicFlowList(procInsId, null, null);
		result.put("histoicFlowList", histoicFlowList);
		return ResponseEntity.ok(result);
	}

	/**
	 * 启动流程
	 * @param procDefKey
	 * @param businessTable
	 * @param businessId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(notes = "tracing", httpMethod = "POST", value = "启动流程")
	@RequestMapping(value = "start", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> start(@RequestParam String procDefKey,
			@RequestParam(required = false) String businessTable, @RequestParam String businessId,
			HttpServletRequest request) throws Exception {
		RestUtil.validateToken(request);
		actTaskService.startProcess(procDefKey, businessId, businessTable, "");
		Map<String, Object> result = TokenUtil.status(APPConst.SUCESS_CODE, request.getLocale());
		return ResponseEntity.ok(result);
	}

	/**
	 * 签收任务
	 * @param taskId
	 * @param loginName
	 * @param request
	 * @return
	 */
	@ApiOperation(notes = "tracing", httpMethod = "POST", value = "签收任务")
	@RequestMapping(value = "claim", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> claim(@RequestParam String taskId, @RequestParam String loginName,
			HttpServletRequest request) {
		RestUtil.validateToken(request);
		actTaskService.claim(taskId, loginName);
		Map<String, Object> result = TokenUtil.status(APPConst.SUCESS_CODE, request.getLocale());
		return ResponseEntity.ok(result);
	}

	/**
	 * 完成任务
	 * @param taskId
	 * @param procInsId
	 * @param comment
	 * @param vars
	 * @param request
	 * @return
	 */
	@ApiOperation(notes = "tracing", httpMethod = "POST", value = "完成任务")
	@RequestMapping(value = "complete", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> complete(@RequestParam String taskId, @RequestParam String procInsId,
			@RequestParam String comment, @RequestBody(required = false) Map<String, Object> vars, HttpServletRequest request) {
		//RestUtil.validateToken(request);
		actTaskService.complete(taskId, procInsId, comment, vars);
		Map<String, Object> result = TokenUtil.status(APPConst.SUCESS_CODE, request.getLocale());
		return ResponseEntity.ok(result);
	}

	/**
	 * 删除任务
	 * @param taskId
	 * @param reason
	 * @param request
	 * @return
	 */
	@ApiOperation(notes = "tracing", httpMethod = "POST", value = "删除任务")
	@ResponseBody
	@RequestMapping(value = "deleteTask", method = RequestMethod.POST)
	public ResponseEntity<?> deleteTask(@RequestParam String taskId, @RequestParam String reason,
			HttpServletRequest request) {
		RestUtil.validateToken(request);
		actTaskService.deleteTask(taskId, reason);
		Map<String, Object> result = TokenUtil.status(APPConst.SUCESS_CODE, request.getLocale());
		return ResponseEntity.ok(result);
	}
/*
	private Act convertParamToAct(RestParamAct paramObj) {
		Act act = new Act();
		act.setProcDefKey(paramObj.getProcDefKey());
		act.setBeginDate(paramObj.getBeginDate());
		act.setEndDate(paramObj.getEndDate());
		act.setAssignee(paramObj.getLoginName());
		act.setCurrentUser(UserUtils.getByLoginName(paramObj.getLoginName()));

		return act;
	}*/
}
