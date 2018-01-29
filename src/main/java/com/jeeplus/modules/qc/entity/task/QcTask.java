/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.qc.entity.task;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工作任务Entity
 * @author xj
 * @version 2017-04-15
 */
public class QcTask extends DataEntity<QcTask> {
	
	private static final long serialVersionUID = 1L;
	private String taskNo;		// 任务号
	private String workingNo;		// 客款号
	
	public QcTask() {
		super();
	}

	public QcTask(String id){
		super(id);
	}

	@ExcelField(title="任务号", align=2, sort=7)
	public String getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}
	
	@ExcelField(title="客款号", dictType="working_no", align=2, sort=8)
	public String getWorkingNo() {
		return workingNo;
	}

	public void setWorkingNo(String workingNo) {
		this.workingNo = workingNo;
	}
	
}