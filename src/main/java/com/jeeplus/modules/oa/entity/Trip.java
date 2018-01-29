/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.oa.entity;

import java.util.Date;
import java.util.List;

import org.activiti.engine.runtime.ProcessInstance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.common.persistence.ActEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 国内出差Entity
 * @author ml
 * @version 2018-01-18
 */
public class Trip extends ActEntity<Trip> {

	private static final long serialVersionUID = 1L;
	private String processInstanceId;		// 流程实例编号
	private String tripType;			//出差类型：china 国内出差 | oversea 港外公干
	private String usrId;			//用户id
	private String enName;			//英文名
	private String chName;			//中文名
	private String staffNo;			//工号
	private String department;		//部门
	private String position;		//职位
	private Date joinedDate;		//入职日期
	private String extNo;			//内线
	private String phone;			//手机号码
	private Date tripBeginDate;		// 出差开始时间
	private Date tripEndDate;		// 出差结束时间
	private String destAddr;		// 出差地点
	private String purposes;		// 出差原因
	private String arrangements;		// 出差安排[{"ariOrg":"", "airDest":"", "hotel":"", "other":""}]
	private  List<TripArrangements> arrangementOBJs;
	private String departManager;		// 部门审批领导
	private String departManagerOpinion;		// 部门领导审批意见
	private Date departAuditDate;			//部门领导审批日期
	private String generalManager;		// 总经理
	private String generalManagerOpinion;		// 总经理意见
	private Date generalAuditDate;			//总经理审批日期
	private Date hrReceiveDate;			//hr接收日期
	private String handledBy;			//负责同事
	private Date issusDate;				//签发日期
	private String ceo;					//ceo
	private String ceoOpinion;					//CEO审批意见，适用港外公干
	private Date ceoAuditDate;					//CEO审批日期，适用港外公干
	private String deputyChairman;					//副主席，适用港外公干
	private String depchairOpinion;					//副主席复核意见，适用港外公干
	private Date depchairCheckDate;					//副主席复核日期，适用港外公干

	// 运行中的流程实例
	private ProcessInstance processInstance;

	public String getTripType() {
		return tripType;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCeo() {
		return ceo;
	}

	public void setCeo(String ceo) {
		this.ceo = ceo;
	}
	
	public String getCeoOpinion() {
		return ceoOpinion;
	}

	public void setCeoOpinion(String ceoOpinion) {
		this.ceoOpinion = ceoOpinion;
	}

	public String getDeputyChairman() {
		return deputyChairman;
	}

	public void setDeputyChairman(String deputyChairman) {
		this.deputyChairman = deputyChairman;
	}

	public String getDepchairOpinion() {
		return depchairOpinion;
	}

	public void setDepchairOpinion(String depchairOpinion) {
		this.depchairOpinion = depchairOpinion;
	}
	

	public Date getCeoAuditDate() {
		return ceoAuditDate;
	}

	public void setCeoAuditDate(Date ceoAuditDate) {
		this.ceoAuditDate = ceoAuditDate;
	}

	public Date getDepchairCheckDate() {
		return depchairCheckDate;
	}

	public void setDepchairCheckDate(Date depchairCheckDate) {
		this.depchairCheckDate = depchairCheckDate;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="部门领导审批日期", align=2, sort=13)
	public Date getDepartAuditDate() {
		return departAuditDate;
	}

	public void setDepartAuditDate(Date departAuditDate) {
		this.departAuditDate = departAuditDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="总经理审批日期", align=2, sort=18)
	public Date getGeneralAuditDate() {
		return generalAuditDate;
	}

	public void setGeneralAuditDate(Date generalAuditDate) {
		this.generalAuditDate = generalAuditDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="HR接收日期", align=2, sort=19)
	public Date getHrReceiveDate() {
		return hrReceiveDate;
	}

	public void setHrReceiveDate(Date hrReceiveDate) {
		this.hrReceiveDate = hrReceiveDate;
	}

	@ExcelField(title="负责同事", align=2, sort=20)
	public String getHandledBy() {
		return handledBy;
	}

	public void setHandledBy(String handledBy) {
		this.handledBy = handledBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="签发日期", align=2, sort=21)
	public Date getIssusDate() {
		return issusDate;
	}

	public void setIssusDate(Date issusDate) {
		this.issusDate = issusDate;
	}

	@ExcelField(title="英文名", align=2, sort=22)
	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	@ExcelField(title="中文名", align=2, sort=23)
	public String getChName() {
		return chName;
	}

	public void setChName(String chName) {
		this.chName = chName;
	}

	@ExcelField(title="工号", align=2, sort=24)
	public String getStaffNo() {
		return staffNo;
	}

	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}

	@ExcelField(title="所属部门", align=2, sort=25)
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="入职日期", align=2, sort=26)
	public Date getJoinedDate() {
		return joinedDate;
	}

	public void setJoinedDate(Date joinedDate) {
		this.joinedDate = joinedDate;
	}

	@ExcelField(title="内线", align=2, sort=27)
	public String getExtNo() {
		return extNo;
	}

	public void setExtNo(String extNo) {
		this.extNo = extNo;
	}

	public Trip() {
		super();
	}

	public Trip(String id){
		super(id);
	}

	@ExcelField(title="流程实例编号", align=2, sort=1)
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="出差开始时间", align=2, sort=8)
	public Date getTripBeginDate() {
		return tripBeginDate;
	}

	public void setTripBeginDate(Date tripBeginDate) {
		this.tripBeginDate = tripBeginDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="出差结束时间", align=2, sort=9)
	public Date getTripEndDate() {
		return tripEndDate;
	}

	public void setTripEndDate(Date tripEndDate) {
		this.tripEndDate = tripEndDate;
	}

	@ExcelField(title="出差地点", align=2, sort=10)
	public String getDestAddr() {
		return destAddr;
	}

	public void setDestAddr(String destAddr) {
		this.destAddr = destAddr;
	}

	@ExcelField(title="出差原因", align=2, sort=11)
	public String getPurposes() {
		return purposes;
	}

	public void setPurposes(String purposes) {
		this.purposes = purposes;
	}

	@ExcelField(title="出差安排", align=2, sort=12)
	public String getArrangements() {
		return arrangements;
	}

	public void setArrangements(String arrangements) {
		this.arrangements = arrangements;
	}
	
	public List<TripArrangements> getArrangementOBJs() {
		return arrangementOBJs;
	}

	public void setArrangementOBJs(List<TripArrangements> arrangementOBJs) {
		this.arrangementOBJs = arrangementOBJs;
	}

	@ExcelField(title="部门审批领导", align=2, sort=14)
	public String getDepartManager() {
		return departManager;
	}

	public void setDepartManager(String departManager) {
		this.departManager = departManager;
	}

	@ExcelField(title="部门审批领导意见", align=2, sort=15)
	public String getDepartManagerOpinion() {
		return departManagerOpinion;
	}

	public void setDepartManagerOpinion(String departManagerOpinion) {
		this.departManagerOpinion = departManagerOpinion;
	}

	@ExcelField(title="总经理", align=2, sort=16)
	public String getGeneralManager() {
		return generalManager;
	}

	public void setGeneralManager(String generalManager) {
		this.generalManager = generalManager;
	}

	@ExcelField(title="总经理意见", align=2, sort=17)
	public String getGeneralManagerOpinion() {
		return generalManagerOpinion;
	}

	public void setGeneralManagerOpinion(String generalManagerOpinion) {
		this.generalManagerOpinion = generalManagerOpinion;
	}

	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	public String getUsrId() {
		return usrId;
	}

	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}

}