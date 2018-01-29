/**
 * There are <a href="http://www.jeeplus.org/">jeeplus</a> code generation
 */
package com.jeeplus.modules.oa.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.BaseService;
import com.jeeplus.common.utils.Collections3;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.act.utils.ActUtils;
import com.jeeplus.modules.oa.dao.PrDao;
import com.jeeplus.modules.oa.entity.Pr;
import com.jeeplus.modules.sys.dao.QuerySqlDao;
import com.jeeplus.modules.sys.entity.QuerySql;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.ToolUtils;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 请假Service
 * @author liuj
 * @version 2013-04-05
 */
@Service
@Transactional(readOnly = true)
public class PrService extends BaseService {

	@Autowired
	private PrDao prDao;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	protected TaskService taskService;
	@Autowired
	protected HistoryService historyService;
	@Autowired
	protected RepositoryService repositoryService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private QuerySqlDao querySqlDao;	
	@Autowired
	private ActTaskService actTaskService;


	/**
	 * 获取流程详细及工作流参数
	 * @param id
	 */
	@SuppressWarnings("unchecked")
	public Pr get(String id) {
		Pr pr = prDao.get(id);
		Map<String,Object> variables=null;
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(pr.getProcessInstanceId()).singleResult();
		if(historicProcessInstance!=null) {
			variables = Collections3.extractToMap(historyService.createHistoricVariableInstanceQuery().processInstanceId(historicProcessInstance.getId()).list(), "variableName", "value");
		} else {
			variables = runtimeService.getVariables(runtimeService.createProcessInstanceQuery().processInstanceId(pr.getProcessInstanceId()).active().singleResult().getId());
		}
		pr.setVariables(variables);
		return pr;
	}
	
	/**
	 * 获取草稿
	 */
	public List<Pr> getDraft(String userId)
	{
		List<Pr> draftList = prDao.getDraft(userId);
		return draftList;
	}
	
	public Pr getById(String Id)
	{
		Pr pr = prDao.get(Id);
		return pr;
	}
	
	/**
	 * 获取所有申请表单
	 * @param page
	 * @param procDefKey 流程定义标识
	 */
	
	public Page<Pr> OverViewListPage(Page<Pr> page, Pr pr){
		//String userId = UserUtils.getUser().getLoginName();//ObjectUtils.toString(UserUtils.getUser().getId());
		
		//page.setOrderBy("a.create_date");
		pr.setPage(page);
		
		List<Pr> list = prDao.getList(pr);
		
		for (Pr prItem : list) {
			TaskQuery taskquery = taskService.createTaskQuery().active().processInstanceId(prItem.getProcessInstanceId());
			List<Task> taskList = taskquery.list();
			if(taskList.size()>0)
			prItem.setTask(taskList.get(0));
			else{
				
				HistoricProcessInstanceQuery historyTaskQuery =  historyService.createHistoricProcessInstanceQuery().processInstanceId(prItem.getProcessInstanceId());
				List<HistoricProcessInstance> histaskList = historyTaskQuery.list();
				if(histaskList.size()>0)
					prItem.setHistoricProcessInstance(histaskList.get(0));
			}
			
		}

		return page.setList(list);
	}
	
	
	/**
	 * 启动流程
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void save(Pr pr, Map<String, Object> variables) {
		
		// 保存业务数据
		if (StringUtils.isBlank(pr.getId())){
			pr.preInsert();
			prDao.insert(pr);
		}else{
			pr.preUpdate();
			prDao.update(pr);
		}
		logger.debug("save entity: {}", pr);
		
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(pr.getCurrentUser().getLoginName());
		
		// 启动流程
		String businessKey = pr.getId().toString();
		//add OEM modification by J 20180128 --begin
		String ProcessInstance = "";
		if(pr.getDocuType()==1)
		{
			variables.put("type", "pr");
			ProcessInstance = ActUtils.PD_PR[0];
		}
		
		else if(pr.getDocuType()==2)
		{
			variables.put("type", "pr_oem");
			ProcessInstance = ActUtils.PD_PR_OEM[0];
		}
		//add OEM modification by J 20180128 --end
		variables.put("busId", businessKey);
		//add in 2017/6/8 begin
		variables.put("title", pr.getApplicationNo());//设置标题；
		variables.put("site",pr.getCurrentUser().getCompany().getCode());
		variables.put("Equipment", pr.getEquipment());
		variables.put("description", pr.getEquipmentDesciption());
		variables.put("EmailTitle", "PR Portal Remind Email-[" + pr.getApplicationNo()+"]-" +pr.getEquipment() );
		
		//end
		/*actTaskService.setAssigneeVar(businessKey, UserUtils.getUser().getLoginName(), variables);*/
		//OEM modification by J 20180128 --begin
		//actTaskService.setAssigneeVar(businessKey, UserUtils.get(pr.getCreateBy().getId()).getLoginName(), variables);
		actTaskService.setAssigneeVar(variables.get("type").toString(), UserUtils.get(pr.getCreateBy().getId()).getLoginName(), variables);
		//ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ActUtils.PD_PR[0], businessKey, variables);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ProcessInstance, businessKey, variables);
		//OEM modification by J 20180128 --end
		pr.setProcessInstance(processInstance);
		
		// 更新流程实例ID
		pr.setProcessInstanceId(processInstance.getId());
		prDao.updateProcessInstanceId(pr);
		
		logger.debug("start process of {key={}, bkey={}, pid={}, variables={}}", new Object[] { 
				ProcessInstance, businessKey, processInstance.getId(), variables });
		
	}
	
	/**
	 * 保存草稿
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void saveAsDraft(Pr pr, Map<String, Object> variables) {
		
		// 保存业务数据
		if (StringUtils.isBlank(pr.getId())){
			pr.preInsert();
			prDao.insert(pr);
		}else{
			pr.preUpdate();
			prDao.update(pr);
		}

		
	}

	/**
	 * 查询待办任务
	 * @param userId 用户ID
	 * @return
	 */
	public List<Pr> findTodoTasks(String userId) {
		
		List<Pr> results = new ArrayList<Pr>();
		List<Task> tasks = new ArrayList<Task>();
		// 根据当前人的ID查询
		List<Task> todoList = taskService.createTaskQuery().processDefinitionKey(ActUtils.PD_PR[0]).taskAssignee(userId).active().orderByTaskPriority().desc().orderByTaskCreateTime().desc().list();
		// 根据当前人未签收的任务
		List<Task> unsignedTasks = taskService.createTaskQuery().processDefinitionKey(ActUtils.PD_PR[0]).taskCandidateUser(userId).active().orderByTaskPriority().desc().orderByTaskCreateTime().desc().list();
		// 合并
		tasks.addAll(todoList);
		tasks.addAll(unsignedTasks);
		// 根据流程的业务ID查询实体并关联
		for (Task task : tasks) {
			String processInstanceId = task.getProcessInstanceId();
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
			String businessKey = processInstance.getBusinessKey();
			Pr pr = prDao.get(businessKey);
			pr.setTask(task);
			pr.setProcessInstance(processInstance);
			pr.setProcessDefinition(repositoryService.createProcessDefinitionQuery().processDefinitionId((processInstance.getProcessDefinitionId())).singleResult());
			results.add(pr);
		}
		return results;
	}

	public Page<Pr> find(Page<Pr> page, Pr pr) {

		pr.getSqlMap().put("dsf", dataScopeFilter(pr.getCurrentUser(), "o", "u"));
		
		pr.setPage(page);
		page.setList(prDao.findList(pr));
		
		for(Pr item : page.getList()) {
			String processInstanceId = item.getProcessInstanceId();
			Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
			item.setTask(task);
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			if(historicProcessInstance!=null) {
				item.setHistoricProcessInstance(historicProcessInstance);
				item.setProcessDefinition(repositoryService.createProcessDefinitionQuery().processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult());
			} else {
				ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
				if (processInstance != null){
					item.setProcessInstance(processInstance);
					item.setProcessDefinition(repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult());
				}
			}
		}
		return page;
	}
	
	/**
	 * 通过applicationNo 找到PR单
	 * @param pr
	 */
	public Pr findbyPRPO(String PRPO)
	{
		Pr pr = prDao.findPRPO(PRPO);
		return pr;
	}	
	/**
	 * 更新PR 信息
	 * @param pr
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean UpdatePr(Pr pr)
	{
		pr.preUpdate();
		if(prDao.update(pr)>0)return true;
		else return false;
	}
	
	/* * 审核审批保存
	 * @param testAudit
	 */
	@Transactional(readOnly = false)
	public void auditSave(Pr pr) {
		
		// 设置意见
		//pr.getAct().setComment(("yes".equals(pr.getAct().getFlag())?"[PASS] ":"[REJECT] ")+pr.getAct().getComment());
		//modified by J 2017-7-12 no need 
		/*if(pr.getAct().getFlag().equals("1"))
		{
			pr.getAct().setComment("[PASS] "+pr.getAct().getComment());
		}else if(pr.getAct().getFlag().equals("0")||(pr.getAct().getFlag().equals("2"))){
			pr.getAct().setComment("[REJECT] "+pr.getAct().getComment());
		}*/
		
		pr.preUpdate();
		//保存PR數據 add by J 2017/6/27 begin
		prDao.update(pr);
		//end
		// 对不同环节的业务逻辑进行操作
		String taskDefKey = pr.getAct().getTaskDefKey();
       
		//业务逻辑对应的条件表达式
		/*String exp = "";
		// 审核环节
		if ("factoryGMAudit".equals(taskDefKey)){			
			exp = "factoryGMPass";			
		}
		else if ("assetAudit".equals(taskDefKey)){
			exp = "assetPass";
		}
		else if ("regionFCAudit".equals(taskDefKey)){
			exp = "regionFCPass";
		}
		else if ("groupMDAudit".equals(taskDefKey)){
			exp = "groupMDPass";
		}
		else if ("groupFCAudit".equals(taskDefKey)){
			exp = "groupFCPass";
		}
		else if ("ceoAudit".equals(taskDefKey)){
			exp = "ceoPass";
		}
		else if ("cfoAudit".equals(taskDefKey)){
			exp = "cfoPass";
		}		
		// 未知环节，直接返回
		else{
			return;
		}*/
		
		// 提交流程任务
		
		
		Map<String, Object> vars = Maps.newHashMap();
		//vars.put(exp, "yes".equals(pr.getAct().getFlag())? true : false);
		vars.put("pass", pr.getAct().getFlag());
		
		//add by J 2017/6/14 begin
		vars.put("EmailTitle", "PR Portal Remind Email-[" + pr.getApplicationNo()+"]-" +pr.getEquipment() );
		
		if(vars.get("pass")=="0")
		{
			vars.put("EmailTitle", "PR Portal Remind Email-[" + pr.getApplicationNo()+"]-" +pr.getEquipment() + "-Reject by " + pr.getCurrentUser().getLoginName() );
			
		}
		//end
		
		vars.put("priceLimit1",ToolUtils.convertPriceLimited(pr.getPriceCurrency(),1));
		vars.put("priceLimit2",ToolUtils.convertPriceLimited(pr.getPriceCurrency(),2));
		
		vars.put("currency", pr.getPriceCurrency());
		vars.put("priceAmount", pr.getPriceAmount());
		vars.put("gogoman", "fbb");
		
		
		vars.put("site", pr.getApplySiteCode());
				
		// 提交流程任务
		//actTaskService.complete(pr.getAct().getTaskId(), pr.getAct().getProcInsId(), pr.getAct().getComment(), vars);
		actTaskService.complete(pr,pr.getAct(), vars);		
	}
	
	@Transactional(readOnly = false)
	public void modifySave(Pr pr) {
		
		// 设置意见
		//pr.getAct().setComment(("yes".equals(pr.getAct().getFlag())?"[重新提交] ":"[撤销] "));
		
		pr.preUpdate();
		prDao.update(pr);
		
		// 提交流程任务
		Map<String, Object> vars = Maps.newHashMap();
		
		vars.put("pass", "yes".equals(pr.getAct().getFlag())? "3" : "4");
		vars.put("priceAmount", pr.getPriceAmount());
		// 提交流程任务
		//actTaskService.complete(pr.getAct().getTaskId(), pr.getAct().getProcInsId(), pr.getAct().getComment(), vars);
		actTaskService.complete(pr,pr.getAct(), vars);
		
	}
	
	public List<Map> getQuerySql(String type, String params){
		QuerySql qs = querySqlDao.findUniqueByProperty("sql_code", type);
		Map<String, String> sqlMap = new HashMap<String, String>();
		sqlMap.put("sql", qs.getSqlStr());
		if (!StringUtils.isEmpty(params)) {
			String[] paramArray = StringUtils.split(params, ",");
			int i = 1;
			//where category_ = #{param1}
			for (String str : paramArray) {
				sqlMap.put("param" + i, str);
				i++;
			}
		}
		List<Map> list = querySqlDao.executeSql(sqlMap);
		return list;
	}
	@Transactional(readOnly = false)
	public Pr DeleteByPRPO(String PRPO)
	{
		Pr pr = prDao.findPRPO(PRPO);
		int result = prDao.deleteByLogic(pr);
		if(result>0)
		{
			return pr;
		}else return null;
	}
}
