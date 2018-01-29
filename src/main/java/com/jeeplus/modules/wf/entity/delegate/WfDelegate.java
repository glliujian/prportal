/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.wf.entity.delegate;

import com.jeeplus.modules.sys.entity.User;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工作流委托設置Entity
 * @author XJ
 * @version 2017-04-28
 */
public class WfDelegate extends DataEntity<WfDelegate> {
	
	private static final long serialVersionUID = 1L;
	private User owner;		// 委托人
	private User delegate;		// 代理人
	private String flowType;		// 流程類型
	private String procDefKey; 	// 流程定义Key（流程定义标识）
	private String procInsId; 	// 任务定义Key（任务环节标识）
	private String applicationNo;
	private String taskId; 		// 任务编号
	private String taskName;
	private Date startTime;		// 開始時間
	private Date endTime;		// 結束時間
	
	public WfDelegate() {
		super();
	}

	public WfDelegate(String id){
		super(id);
	}

	@ExcelField(title="委托人", fieldType=User.class, value="owner.name", align=2, sort=7)
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	@ExcelField(title="代理人", fieldType=User.class, value="delegate.name", align=2, sort=8)
	public User getDelegate() {
		return delegate;
	}

	public void setDelegate(User delegate) {
		this.delegate = delegate;
	}
	
	@ExcelField(title="流程類型", dictType="GET_FLOW_TYPE", align=2, sort=9)
	public String getFlowType() {
		return flowType;
	}

	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="開始時間", align=2, sort=10)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="結束時間", align=2, sort=11)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getProcDefKey() {
		return procDefKey;
	}

	public void setProcDefKey(String procDefKey) {
		this.procDefKey = procDefKey;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}
	
}