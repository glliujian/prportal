/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.wf.entity.usergroup;

import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工作流用户组Entity
 * @author xj
 * @version 2017-06-25
 */
public class WfUserGroup extends DataEntity<WfUserGroup> {
	
	private static final long serialVersionUID = 1L;
	private Office company;		// 公司
	private Office department;		// 部门
	private String procDefKey;		// 流程标识
	private String userGroup;		// 组标识
	private User user;		// 用户
	
	private int isApprover;//是否为代理人
	
	public WfUserGroup() {
		super();
	}

	public WfUserGroup(String id){
		super(id);
	}

	@ExcelField(title="公司", fieldType=Office.class, value="company.name", align=2, sort=7)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}
	
	@ExcelField(title="部门", fieldType=Office.class, value="department.name", align=2, sort=8)
	public Office getDepartment() {
		return department;
	}

	public void setDepartment(Office department) {
		this.department = department;
	}
	
	@ExcelField(title="流程标识", dictType="GET_FLOW_TYPE", align=2, sort=9)
	public String getProcDefKey() {
		return procDefKey;
	}

	public void setProcDefKey(String procDefKey) {
		this.procDefKey = procDefKey;
	}
	
	@ExcelField(title="组标识", align=2, sort=10)
	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}
	
	@ExcelField(title="用户", fieldType=User.class, value="user.name", align=2, sort=11)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getIsApprover() {
		return isApprover;
	}

	public void setIsApprover(int isApprover) {
		this.isApprover = isApprover;
	}
	
}