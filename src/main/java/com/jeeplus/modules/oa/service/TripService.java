/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.oa.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.act.utils.ActUtils;
import com.jeeplus.modules.oa.dao.TripDao;
import com.jeeplus.modules.oa.entity.Trip;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 国内出差Service
 * @author ml
 * @version 2018-01-18
 */
@Service
@Transactional(readOnly = true)
public class TripService extends CrudService<TripDao, Trip> {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static Map<String, String> auditStat = new HashMap<String, String>();
	public static final String[] PASS_FLAG = new String[]{"yes", "no"};//审批通过标记
	
	@Autowired
	private TripDao tripDao;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private ActTaskService actTaskService;
	
	static{
		/*auditStat.put("deptLeaderAudit", 	"deptLeaderPass");//部门领导审批
		auditStat.put("genLeaderAudit", 	"genLeaderPass");//总经理审批
		auditStat.put("tripChange", 		"reApply");//重新提交审核
		auditStat.put("HRRecord", 			"");//HR备案
		auditStat.put("tripEnd", 			"");//流程结束
*/	
		auditStat.put("deptLeaderAudit", 	"pass");//部门领导审批
		auditStat.put("genLeaderAudit", 	"pass");//总经理审批
		auditStat.put("CEOAudit", 	"pass");//CEO审批
		auditStat.put("deputyChairmanCheck", 	"pass");//副总裁复核
		auditStat.put("tripChange", 		"pass");//重新提交审核
		auditStat.put("HRRecord", 			"pass");//HR备案
		auditStat.put("tripEnd", 			"pass");//流程结束
		}
	
	/**
	 * 获取审批结果getAuditResult
	 * @param taskDefKey
	 * @param comment
	 * @return
	 */
	private Map<String, Object> getAuditResult(String taskDefKey, String passFlag){
		Map<String, Object> vars = null;
		try{
			if (auditStat.containsKey(taskDefKey)){
				vars = Maps.newHashMap();
				vars.put(auditStat.get(taskDefKey), PASS_FLAG[0].equals(passFlag) ? true : false);
			}
		}catch(Exception e){
			logger.debug("getAuditResult: {}", e);
		}
		return vars;
	}
	
	/**
	 * 保存审批结果
	 * @param taskDefKey
	 * @param comment
	 * @return
	 */
	private void saveAuditResult(Trip trip)throws Exception {
		String taskDefKey = trip.getAct().getTaskDefKey();
		//String procDefKey = trip.getAct().getProcDefKey();
		
		if (taskDefKey.equals("deptLeaderAudit")) {
			trip.setDepartManager(trip.getAct().getAssigneeName());
			trip.setDepartManagerOpinion(trip.getAct().getComment());
			trip.setDepartAuditDate(new Date());
		}else if (taskDefKey.equals("genLeaderAudit")) {
			trip.setGeneralManager(trip.getAct().getAssigneeName());
			trip.setGeneralManagerOpinion(trip.getAct().getComment());
			trip.setGeneralAuditDate(new Date());
		}else if (taskDefKey.equals("CEOAudit")) {
			trip.setCeo(trip.getAct().getAssigneeName());
			trip.setCeoOpinion(trip.getAct().getComment());
			trip.setCeoAuditDate(new Date());
		}else if (taskDefKey.equals("deputyChairmanCheck")) {
			trip.setDeputyChairman(trip.getAct().getAssigneeName());
			trip.setDepchairOpinion(trip.getAct().getComment());
			trip.setDepchairCheckDate(new Date());
		}
		tripDao.update(trip);
	}
	
	public Trip get(String id) {
		return super.get(id);
	}

	public List<Trip> findList(Trip trip) {
		return super.findList(trip);
	}

	public Page<Trip> findPage(Page<Trip> page, Trip trip) {
		return super.findPage(page, trip);
	}

	@Transactional(readOnly = false)
	public void save(Trip trip) {
		// 保存业务数据
		if (StringUtils.isBlank(trip.getId())){
			trip.preInsert();
			tripDao.insert(trip);
		}else{
			trip.preUpdate();
			tripDao.update(trip);
		}
		logger.debug("save entity: {}", trip);

		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(trip.getCurrentUser().getLoginName());

		// 启动流程
		Map<String, Object> variables = Maps.newHashMap();
		String businessKey = trip.getId().toString();
		
		variables.put("type", "trip");
		variables.put("busId", businessKey);
		variables.put("title", trip.getRemarks());//设置标题；
		
		actTaskService.setAssigneeVar(ActUtils.PD_TRIP[0], UserUtils.get(trip.getCreateBy().getId()).getLoginName(), variables);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("china".equals(trip.getTripType()) ? ActUtils.PD_TRIP[0] : ActUtils.PD_TRIP_OVERSEA[0]
				, businessKey, variables);
		
		trip.setProcessInstance(processInstance);

		// 更新流程实例ID
		trip.setProcessInstanceId(processInstance.getId());
		tripDao.updateProcessInstanceId(trip);

		logger.debug("start process of {key={}, bkey={}, pid={}, variables={}}", new Object[] { 
				ActUtils.PD_TRIP[0], businessKey, processInstance.getId(), variables });
	}
	
	/* * 审核审批保存
	 * @param testAudit
	 */
	@Transactional(readOnly = false)
	public void auditSave(Trip trip) {
		try{
			String passFlag = trip.getAct().getFlag();
			String taskDefKey = trip.getAct().getTaskDefKey();
			
			// 设置意见
			trip.getAct().setComment((PASS_FLAG[0].equals(passFlag)?"[同意] ":"[驳回] ")+trip.getAct().getComment());
			// 保存审批信息
			trip.preUpdate();
			saveAuditResult(trip);
			
			// 获取审批结果
			Map<String, Object> vars = getAuditResult(taskDefKey, passFlag);
			
			if (null != vars){//提交审批结果，进行下一步流程
				actTaskService.complete(trip.getAct().getTaskId()
						, trip.getAct().getProcInsId(), trip.getAct().getComment(), vars);
			}
		}catch(Exception e){
			logger.error("auditSave: {} | {}", trip, e);
		}
	}

	@Transactional(readOnly = false)
	public void delete(Trip trip) {
		super.delete(trip);
	}
}