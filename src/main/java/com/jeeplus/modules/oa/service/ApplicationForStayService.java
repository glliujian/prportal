/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.oa.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.act.utils.ActUtils;
import com.jeeplus.modules.oa.dao.ApplicationForStayDao;
import com.jeeplus.modules.oa.entity.ApplicationForStay;
import com.jeeplus.modules.sys.utils.UserUtils;
/**
 * 入住申请Service
 * @author LiuJian
 * @version 2018-01-20
 */
@Service
@Transactional(readOnly = true)
public class ApplicationForStayService extends CrudService<ApplicationForStayDao, ApplicationForStay> {
	@Autowired
	private IdentityService identityService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private ActTaskService actTaskService;
	
	static public JSONObject auditStat=JSON.parseObject(
			"{'deptLeaderAudit':'pass',"
		  + " 'genLeaderAudit':'pass',"
		  + " 'reservationChange':'pass',"
		  + " 'checkIn':'pass',"
		  + " 'checkOut':'pass',"
		  + " 'end':'pass'}");
	
	public static final String[] PASS_FLAG = new String[]{"yes", "no"};//审批通过标记
	
	public ApplicationForStay get(String id) {
		return super.get(id);
	}
	
	public List<ApplicationForStay> findList(ApplicationForStay applicationForStay) {
		return super.findList(applicationForStay);
	}
	
	public Page<ApplicationForStay> findPage(Page<ApplicationForStay> page, ApplicationForStay applicationForStay) {
		return super.findPage(page, applicationForStay);
	}
	
	@Transactional(readOnly = false)
	public void save(ApplicationForStay applicationForStay) {
		super.save(applicationForStay);
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(applicationForStay.getCurrentUser().getLoginName());
				
		// 启动流程
		String businessKey = applicationForStay.getId();
		Map<String, Object> variables = Maps.newHashMap();
		variables.put("type", "reservation");
		variables.put("busId", businessKey);
		variables.put("title", applicationForStay.getReason());//设置标题；
		actTaskService.setAssigneeVar(ActUtils.PD_STAY[0], UserUtils.get(applicationForStay.getCreateBy().getId()).getLoginName(), variables);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ActUtils.PD_STAY[0], businessKey, variables);
		applicationForStay.setProcessInstance(processInstance);
		// 更新流程实例ID
		applicationForStay.setProcessInstanceId(processInstance.getId());
		super.dao.updateProcessInstanceId(applicationForStay);
	}
	/**
	 * 获取审批结果getAuditResult
	 * @param taskDefKey
	 * @param comment
	 * @return
	 */
	private Map<String, Object> getAuditResult(String taskDefKey, String passFlag){
		Map<String, Object> vars = null;
		if (auditStat.containsKey(taskDefKey)){
			vars = Maps.newHashMap();
			vars.put(auditStat.getString(taskDefKey), PASS_FLAG[0].equals(passFlag) ? true : false);
		}
		return vars;
	}
	public void setProcessData(ApplicationForStay applicationForStay){
		if(applicationForStay.getAct()!=null){
			switch (applicationForStay.getAct().getTaskDefKey()) {
			case "deptLeaderAudit":{
				applicationForStay.setFirstApprover(UserUtils.getUser().getLoginName());
				applicationForStay.setFirstApproverDate(new Date());
			}
			break;
			case "genLeaderAudit":{
				applicationForStay.setSecondApprover(UserUtils.getUser().getLoginName());
				applicationForStay.setSecondApproverDate(new Date());
			}
			break;
			case "checkIn":{
				applicationForStay.setCheckInTime(new Date());
				applicationForStay.setCheckInDormitoryAdministrator(UserUtils.getUser().getLoginName());
			}
			break;
			case "checkOut":{
				applicationForStay.setCheckOutTime(new Date());
				applicationForStay.setCheckOutDormitoryAdministrator(UserUtils.getUser().getLoginName());
			}
			break;
			default:
				break;
			}
		}
	}
	/* * 审核审批保存
	 * @param testAudit
	 */
	@Transactional(readOnly = false)
	public void auditSave(ApplicationForStay applicationForStay) {
		try{
			String passFlag = applicationForStay.getAct().getFlag();
			String taskDefKey = applicationForStay.getAct().getTaskDefKey();
			// 设置意见
			if("reservationChange".equals(taskDefKey)){
				applicationForStay.getAct().setComment((PASS_FLAG[0].equals(passFlag)?"[再次申请] ":"[放弃申请] "));
			}
			else if("checkIn".equals(taskDefKey)){
				applicationForStay.getAct().setComment((PASS_FLAG[0].equals(passFlag)?"[办理入住成功] ":"[bug!] "));
			}
			else if("checkOut".equals(taskDefKey)){
				applicationForStay.getAct().setComment((PASS_FLAG[0].equals(passFlag)?"[办理退房成功] ":"[bug!] "));
			}
			else{
				applicationForStay.getAct().setComment((PASS_FLAG[0].equals(passFlag)?"[同意] ":"[驳回] ")+applicationForStay.getAct().getComment());
			}
			
			// 保存审批信息
			setProcessData(applicationForStay);
			super.save(applicationForStay);
			// 获取审批结果
			Map<String, Object> vars = getAuditResult(taskDefKey, passFlag);
			
			if (null != vars){//提交审批结果，进行下一步流程
				actTaskService.complete(applicationForStay.getAct().getTaskId()
						, applicationForStay.getAct().getProcInsId(), applicationForStay.getAct().getComment(), vars);
			}
		}catch(Exception e){
			logger.error("auditSave: {} | {}", applicationForStay, e);
		}
	}
	
	
	@Transactional(readOnly = false)
	public void delete(ApplicationForStay applicationForStay) {
		super.delete(applicationForStay);
	}
	
	
	
	
}