/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.wf.entity.assignee;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工作流處理人設置Entity
 * @author XJ
 * @version 2017-04-28
 */
public class WfAssignee extends DataEntity<WfAssignee> {
	
	private static final long serialVersionUID = 1L;
	private String assigneeVar;		// 處理人流程變量
	private String flowType;		// 流程類型
	private String sqlStr;		// 取值sql
	
	public WfAssignee() {
		super();
	}

	public WfAssignee(String id){
		super(id);
	}

	@ExcelField(title="處理人流程變量", align=2, sort=7)
	public String getAssigneeVar() {
		return assigneeVar;
	}

	public void setAssigneeVar(String assigneeVar) {
		this.assigneeVar = assigneeVar;
	}
	
	@ExcelField(title="流程類型", dictType="GET_FLOW_TYPE", align=2, sort=8)
	public String getFlowType() {
		return flowType;
	}

	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}
	
	@ExcelField(title="取值sql", align=2, sort=9)
	public String getSqlStr() {
		return sqlStr;
	}

	public void setSqlStr(String sqlStr) {
		this.sqlStr = sqlStr;
	}
	
}