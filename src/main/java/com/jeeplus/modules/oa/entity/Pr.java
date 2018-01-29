/**
 * There are <a href="http://www.jeeplus.org/">jeeplus</a> code generation
 */
package com.jeeplus.modules.oa.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.ActEntity;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.DictUtils;

/**
 * 请假Entity
 * @author liuj
 * @version 2013-04-05
 */
public class Pr extends ActEntity<Pr> {
	
	private static final long serialVersionUID = 1L;	
	
	private String costCenter; 	// costCenter
	private String processInstanceId; // 流程实例编号
	private String payeeCompany;	// 
	
	private String applicationNo;
	private String poNo;	// 请假结束日期	
	private String transferNo;
	
	private String equipment;	
	private String brand;
	private String modelNo;
	private String supplier;
	private double unitPrice;
	private Long quantity;
	private String requestStatus;
	private Date requestArrivalDate;
	private String importMethod;
	private Date actualArrivalDate;
	
	private String budgetType;
	private String purchaseLocation;
	private String paidByFinance;	
	
	private double priceAmount;
	private String ids;
	//add by J 2017/6/8 begin
	private String purchasePurpose;
	private String returnOnInvestment;
	private String budgetRestSituation;
	private String expenseType;
	private String applySiteCode;
	private Date lastTimeBuySameEquipment; 	
	private String applyUserDepartmentName;
	private String priceCurrency;
    private String equipmentDesciption;
    private String attachment;
    private String attachment2;
	private String attachment3;
    private String attachment4;
    private int seq;
    private String requesterName;
	private String paymentSpecial;
	
	private String requestUserDepartment;
	private String contractCompany;
	
	private String finalBookCompany;
	private String assetGroup;
	//end
	//页面显示字段 begin
	private String status;
	private String assignee;
	private Date beginDate;	// 开始查询日期
	private Date endDate;	// 结束查询日期
	private String loginName;
	private List<String> attachmentArray;
	//页面显示字段 end
	
	//标识PR 类型 20180127 by J
	private int docuType;
	
	//-- 临时属性 --//
	// 流程任务
	private Task task;
	private Map<String, Object> variables;
	// 运行中的流程实例
	private ProcessInstance processInstance;
	// 历史的流程实例
	private HistoricProcessInstance historicProcessInstance;
	// 流程定义
	private ProcessDefinition processDefinition;	

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	public User getUser() {
		return createBy;
	}
	
	public void setUser(User user) {
		this.createBy = user;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}

	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	public HistoricProcessInstance getHistoricProcessInstance() {
		return historicProcessInstance;
	}

	public void setHistoricProcessInstance(HistoricProcessInstance historicProcessInstance) {
		this.historicProcessInstance = historicProcessInstance;
	}

	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

	public String getIds() {
		List<String> idList = Lists.newArrayList();
		if (StringUtils.isNotBlank(ids)){
			String ss = ids.trim().replace("　", ",").replace(" ",",").replace("，", ",").replace("'", "");
			for(String s : ss.split(",")) {
//				if(s.matches("\\d*")) {
					idList.add("'"+s+"'");
//				}
			}
		}
		return StringUtils.join(idList, ",");
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getPayeeCompany() {
		return payeeCompany;
	}

	public void setPayeeCompany(String payeeCompany) {
		this.payeeCompany = payeeCompany;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public double getPriceAmount() {
		return priceAmount;
	}

	public void setPriceAmount(double priceAmount) {
		this.priceAmount = priceAmount;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getTransferNo() {
		return transferNo;
	}

	public void setTransferNo(String transferNo) {
		this.transferNo = transferNo;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModelNo() {
		return modelNo;
	}

	public void setModelNo(String modelNo) {
		this.modelNo = modelNo;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}
	 
	public String getImportMethod() {
		return importMethod;
	}

	public void setImportMethod(String importMethod) {
		this.importMethod = importMethod;
	}
	
	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Date getRequestArrivalDate() {
		return requestArrivalDate;
	}

	public void setRequestArrivalDate(Date requestArrivalDate) {
		this.requestArrivalDate = requestArrivalDate;
	}

	public Date getActualArrivalDate() {
		return actualArrivalDate;
	}

	public void setActualArrivalDate(Date actualArrivalDate) {
		this.actualArrivalDate = actualArrivalDate;
	}

	public String getBudgetType() {
		return budgetType;
	}

	public void setBudgetType(String budgetType) {
		this.budgetType = budgetType;
	}

	public String getPurchaseLocation() {
		return purchaseLocation;
	}

	public void setPurchaseLocation(String purchaseLocation) {
		this.purchaseLocation = purchaseLocation;
	}

	public String getPaidByFinance() {
		return paidByFinance;
	}

	public void setPaidByFinance(String paidByFinance) {
		this.paidByFinance = paidByFinance;
	}
	//add by J 2017/6/8 begin
	public String getPurchasePurpose() {
		return purchasePurpose;
	}

	public void setPurchasePurpose(String purchasePurpose) {
		this.purchasePurpose = purchasePurpose;
	}
	
	public String getReturnOnInvestment() {
		return returnOnInvestment;
	}

	public void setReturnOnInvestment(String returnOnInvestment) {
		this.returnOnInvestment = returnOnInvestment;
	}
	public String getBudgetRestSituation() {
		return budgetRestSituation;
	}

	public void setBudgetRestSituation(String budgetRestSituation) {
		this.budgetRestSituation = budgetRestSituation;
	}
	

	public String getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(String expenseType) {
		this.expenseType = expenseType;
	}
	public String getApplySiteCode() {
		return applySiteCode;
	}

	public void setApplySiteCode(String applySiteCode) {
		this.applySiteCode = applySiteCode;
	}
	
	public Date getLastTimeBuySameEquipment() {
		return lastTimeBuySameEquipment;
	}

	public void setLastTimeBuySameEquipment(Date lastTimeBuySameEquipment) {
		this.lastTimeBuySameEquipment = lastTimeBuySameEquipment;
	}
	
	public String getApplyUserDepartmentName() {
		return applyUserDepartmentName;
	}

	public void setApplyUserDepartmentName(String applyUserDepartmentName) {
		this.applyUserDepartmentName = applyUserDepartmentName;
	}

	public String getPriceCurrency() {
		return priceCurrency;
	}

	public void setPriceCurrency(String priceCurrency) {
		this.priceCurrency = priceCurrency;
	}
	public String getEquipmentDesciption() {
		return equipmentDesciption;
	}

	public void setEquipmentDesciption(String equipmentDesciption) {
		this.equipmentDesciption = equipmentDesciption;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getAttachment2() {
		return attachment2;
	}

	public void setAttachment2(String attachment2) {
		this.attachment2 = attachment2;
	}

	public String getAttachment3() {
		return attachment3;
	}

	public void setAttachment3(String attachment3) {
		this.attachment3 = attachment3;
	}

	public String getAttachment4() {
		return attachment4;
	}

	public void setAttachment4(String attachment4) {
		this.attachment4 = attachment4;
	}
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	//end
	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	public String getPaymentSpecial() {
		return paymentSpecial;
	}

	public void setPaymentSpecial(String paymentSpecial) {
		this.paymentSpecial = paymentSpecial;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public List<String> getAttachmentArray() {
		return attachmentArray;
	}

	public void setAttachmentArray(List<String> attachmentArray) {
		this.attachmentArray = attachmentArray;
	}

	public String getRequestUserDepartment() {
		return requestUserDepartment;
	}

	public void setRequestUserDepartment(String requestUserDepartment) {
		this.requestUserDepartment = requestUserDepartment;
	}
	public String getContractCompany() {
		return contractCompany;
	}

	public void setContractCompany(String contractCompany) {
		this.contractCompany = contractCompany;
	}

	public String getFinalBookCompany() {
		return finalBookCompany;
	}

	public void setFinalBookCompany(String finalBookCompany) {
		this.finalBookCompany = finalBookCompany;
	}

	public String getAssetGroup() {
		return assetGroup;
	}

	public void setAssetGroup(String assetGroup) {
		this.assetGroup = assetGroup;
	}
	
	public int getDocuType() {
		return docuType;
	}

	public void setDocuType(int docuType) {
		this.docuType = docuType;
	}
	

}


