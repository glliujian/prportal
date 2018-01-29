/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.oa.entity;

import java.util.Date;

import org.activiti.engine.runtime.ProcessInstance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.common.persistence.ActEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 入住申请Entity
 * @author LiuJian
 * @version 2018-01-20
 */
public class ApplicationForStay extends ActEntity<ApplicationForStay> {
	private static final long serialVersionUID = 1L;
	private String processInstanceId;		// 流程实例编号
	private String gender;		// 性别
	private String position;		// 职位
	private String nativePlace;		// 籍贯
	private String reason;		// 原因
	private String days;		// 入住天数
	private Date startTime;		// 开始时间
	private Date endTime;		// 结束时间
	private String firstApprover;		// 第一审批人
	private Date firstApproverDate;		// 第一审批人日期
	private String SecondApprover;		// 第二审批人
	private Date SecondApproverDate;		// 第二审批人日期
	private Date checkInTime;		// 入住时间
	private Date checkOutTime;		// 退房时间
	private String checkInDormitoryAdministrator;		// 宿舍管理员
	private String checkOutDormitoryAdministrator;		// 宿舍管理员
	private String roomNo;		// 房号
	private Double cost;		// 费用
	private String payer;		// 付款人
	
	
	// 运行中的流程实例
	private ProcessInstance processInstance;
	
	public ApplicationForStay() {
		super();
	}

	public ApplicationForStay(String id){
		super(id);
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@ExcelField(title="性别", align=2, sort=7)
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	@ExcelField(title="职位", align=2, sort=9)
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	@ExcelField(title="籍贯", align=2, sort=10)
	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}
	
	@ExcelField(title="原因", align=2, sort=11)
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@ExcelField(title="入住天数", align=2, sort=12)
	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="开始时间", align=2, sort=13)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="结束时间", align=2, sort=14)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@ExcelField(title="第一审批人", align=2, sort=15)
	public String getFirstApprover() {
		return firstApprover;
	}

	public void setFirstApprover(String firstApprover) {
		this.firstApprover = firstApprover;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="第一审批人日期", align=2, sort=16)
	public Date getFirstApproverDate() {
		return firstApproverDate;
	}

	public void setFirstApproverDate(Date firstApproverDate) {
		this.firstApproverDate = firstApproverDate;
	}
	
	@ExcelField(title="第二审批人", align=2, sort=18)
	public String getSecondApprover() {
		return SecondApprover;
	}

	public void setSecondApprover(String SecondApprover) {
		this.SecondApprover = SecondApprover;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="第二审批人日期", align=2, sort=19)
	public Date getSecondApproverDate() {
		return SecondApproverDate;
	}

	public void setSecondApproverDate(Date SecondApproverDate) {
		this.SecondApproverDate = SecondApproverDate;
	}
	
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="入住时间", align=2, sort=21)
	public Date getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(Date checkInTime) {
		this.checkInTime = checkInTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="退房时间", align=2, sort=22)
	public Date getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(Date checkOutTime) {
		this.checkOutTime = checkOutTime;
	}
	
	@ExcelField(title="宿舍管理员", align=2, sort=23)
	public String getCheckInDormitoryAdministrator() {
		return checkInDormitoryAdministrator;
	}

	public void setCheckInDormitoryAdministrator(String checkInDormitoryAdministrator) {
		this.checkInDormitoryAdministrator = checkInDormitoryAdministrator;
	}
	
	@ExcelField(title="宿舍管理员", align=2, sort=24)
	public String getCheckOutDormitoryAdministrator() {
		return checkOutDormitoryAdministrator;
	}

	public void setCheckOutDormitoryAdministrator(String checkOutDormitoryAdministrator) {
		this.checkOutDormitoryAdministrator = checkOutDormitoryAdministrator;
	}
	
	@ExcelField(title="房号", align=2, sort=25)
	public String getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	
	
	@ExcelField(title="费用", align=2, sort=26)
	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}
	
	@ExcelField(title="付款人", align=2, sort=27)
	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}
	
	
}