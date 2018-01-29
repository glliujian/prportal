<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
	<title>PR Application</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		function caculateTotal()
		{
			
			if(!isNaN($("#unitPrice").val()) &&!isNaN($("#quantity").val()))
				{
				
					$("#priceAmount").val($("#unitPrice").val() * $("#quantity").val());
				}
			else{
				$("#priceAmount").val("");
			}
		}
		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading("<spring:message code='正在提交，请稍等...'></spring:message>");
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("<spring:message code='输入有误，请先更正。'></spring:message>");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
					laydate({
			            elem: '#requestArrivalDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
					laydate({
			            elem: '#actualArrivalDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
					laydate({
			            elem: '#lastTimeBuySameEquipment', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
		});
		
		
		
		
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5><spring:message code="采购申请表"></spring:message></h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<!-- <a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#">选项1</a>
				</li>T
				<li><a href="#">选项2</a>
				</li>
			</ul> -->
			<a class="close-link" onclick="history.go(-1)">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
	<div class="ibox-content">
	<form:form id="inputForm" modelAttribute="pr" action="${ctx}/oa/pr/saveAudit" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden path="act.assignee"/>
		<form:hidden id="flag" path="act.flag"/>
		
		<sys:message content="${message}"/>
		<!-- <div style="text-align:center"><font size="6pt">Bowker Asia Limited</font><br><font size="4pt">Fixed Asset Application Form (IT Equipments)</font></div> -->
		<table id="tableContent" class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		   		<tr>
		   			<td colspan=6 style="text-align:center"><label><font size="6pt">
		   			<!-- Bowker Asia Limited -->	
		   			<!-- ${fns:getUserById(pr.createBy).company} -->
		   			Win Hanverky Group
		   			</font><br><font size="4pt"><spring:message code="采购申请表（IT 设备）"></spring:message></font></label></td>
		   		</tr>
		   		<tr>
		   			<td class="width-15 active"><label class="pull-right">PR <spring:message code="号码"></spring:message>:</label></td>
					<td  colspan=3 class="width-35">
						<form:input path="applicationNo" htmlEscape="false" value="${pr.applicationNo}"  readonly="true" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><spring:message code="提交日期"></spring:message>:</label></td>
					<td class="width-35">
						<input type="text" maxlength="20" class="laydate-icon form-control layer-date " readonly="true"
							value="<fmt:formatDate value="${pr.createDate}" timeZone="${timeZone}" pattern="yyyy-MM-dd"/>"/>
					</td>
		   		</tr>
				<tr>
				    <td class="width-15 active"><label class="pull-right"><spring:message code="部门名称"></spring:message>:</label></td>
					<td colspan=3 class="width-35">
						<form:input path="applyUserDepartmentName" htmlEscape="false"  readonly="true" class="form-control required"/>
					</td> 
					<!-- <td class="width-15 active"><label class="pull-right"><spring:message code="成本中心编号"></spring:message>:</label></td>
					<td class="width-35">
						
						<form:input path="costCenter" htmlEscape="false" readonly="true" class="form-control"/>
					</td> -->
					<td class="width-15 active"><label class="pull-right"><spring:message code="成本中心编号"></spring:message>:</label></td>
					<td >
						<form:select path="costCenter" cssClass="form-control" readonly="true">
							<form:option value="" label="-"/>
							<form:options items="${fns:getDictListWithKey('cost_center')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</td>
					
				</tr>
				<tr>	
					<td class="width-15 active"><label class="pull-right"><spring:message code="供应商名称"></spring:message>:</label></td>
					<td  colspan=3  class="width-35">
						<form:input path="supplier" htmlEscape="false" readonly="true"   class="form-control "/>
					</td> 		
					<td class="width-15 active"><label class="pull-right"><spring:message code="费用类型"></spring:message>:</label></td>
					<td class="width-35">
						<form:input path="expenseType" readonly="true" cssClass="form-control input-sm" />
							
					</td>
					
				</tr>
				<tr>
					
					
					<td class="width-15 active"><label class="pull-right"><spring:message code="请求者姓名"></spring:message>:</label></td>
					<td colspan=3 class="width-35">
						<form:input path="requesterName" htmlEscape="false"  readonly="true"  class="form-control "/>
					</td>
					
					<td class="width-15 active"><label class="pull-right"><spring:message code="采购地区"></spring:message>:</label></td>
					<td  class="width-35">
						<form:input path="applySiteCode" htmlEscape="false" readonly="true"  class="form-control "/>
						
					</td>
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="请求者部门"></spring:message>:</label></td>
					<td  colspan=3 class="width-35">
						<form:input path="requestUserDepartment" htmlEscape="false" readonly="true" maxlength="50" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><spring:message code="和供应商签约方公司"></spring:message>:</label></td>
					<td >
						<form:select path="contractCompany" cssClass="form-control " readonly="true"  >
							<form:option value="" label="-"/>
							<form:options items="${fns:getDictListWithKey('all_company')}" itemLabel="label" disabled="true"  readonly="true" itemValue="value" htmlEscape="false" />
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="入账及使用方公司"></spring:message>:</label></td>
					<td colspan=3  class="width-35">
						
						<form:select path="finalBookCompany" cssClass="form-control"  readonly="true">
							<form:option value="" label="-"/>
							<form:options items="${fns:getDictListWithKey('all_company')}" itemLabel="label" disabled="true"  itemValue="value" htmlEscape="false" />
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><spring:message code="固定资产类别"></spring:message>:</label></td>
					<td >
						<form:select path="assetGroup" cssClass="form-control" readonly="true">
							<form:option value="" label="-"/>
							<form:options items="${fns:getDictListWithKey('asset_Group')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</td>
				</tr>
				<tr>
					<td colspan=6><label class="pull-left"><font size="4pt"><spring:message code="具体申购内容"></spring:message></font></label></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="设备名称"></spring:message>:</label></td>
					<td  colspan=3 class="width-35">
						<form:input path="equipment" htmlEscape="false"   readonly="true"  class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><spring:message code="品牌"></spring:message>:</label></td>
					<td class="width-35">
						<form:input path="brand" htmlEscape="false"   readonly="true"  class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="型号"></spring:message>:</label></td>
					<td  colspan=3 class="width-35">
						<form:input path="modelNo" htmlEscape="false"   readonly="true"  class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><spring:message code="要求到货日期"></spring:message>:</label></td>
					<td colspan=3 class="width-35">
						<input readonly="true" name="requestArrivalDate" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${pr.requestArrivalDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="单价"></spring:message>:</label></td>
					<td class="width-15">
						<%--<form:input path="unitPrice" id="unitPrice" htmlEscape="false"  readonly="true"  class="form-control required"/> --%>
						<input name="unitPrice" id="unitPrice" readonly="true" type="text" class="form-control" value="<fmt:formatNumber value="${pr.unitPrice}" pattern="0.00"/>"/>
					</td>
					<td class="width-10 active"><label class="pull-right"><spring:message code="币种"></spring:message>:</label></td> 
					<td class="width-15">						
						<form:input path="priceCurrency"  readonly="true" cssClass="form-control required" />
							
					</td>
					<td class="width-15 active"><label class="pull-right"><spring:message code="数量"></spring:message>:</label></td>
					<td class="width-35">
						<form:input path="quantity" id="quantity" htmlEscape="false"  readonly="true"   class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="总金额"></spring:message>:</label></td>
					<td  colspan=3 class="width-35">
						<form:input path="" id="priceAmountDisplay"  htmlEscape="false"  readonly="true"   class="form-control required"/>
						<%--<form:input path="priceAmount"  id="priceAmount"  htmlEscape="false"  readonly="true"   class="hide form-control"/> --%>
						<input name="priceAmount" id="priceAmount" readonly="true" type="text" class="hide form-control" value="<fmt:formatNumber value="${pr.priceAmount}" pattern="0.00"/>"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><spring:message code="描述"></spring:message></label></td>
					<td class="width-35">
						<form:input path="equipmentDesciption"  htmlEscape="false"  readonly="true"  class="form-control "/>
					</td>
				</tr>
				<!-- 
				<tr>
					<td class="width-15 active"><label class="pull-right">Actual arrival date：</label></td>
					<td colspan=3 class="width-35">
						<input id="actualArrivalDate" name="actualArrivalDate" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${pr.actualArrivalDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> 
					</td>
				</tr> -->
				<tr>
					<td colspan=6><label class="pull-left"><font size="4pt"><spring:message code="账务处理"></spring:message></font></label></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="预算"></spring:message>:</label></td>
					<td  colspan=3 class="width-35">
						<%-- <form:input path="budgetType" value="${fns:getDictLabel(pr.budgetType,'IT_PR_BUDGET','')}" readonly="true" cssClass="form-control input-sm " />--%>
						<form:radiobuttons cssClass="form-control i-checks" disabled="true"  path="budgetType" items="${fns:getDictList('IT_PR_BUDGET')}" itemLabel="label" itemValue="value" htmlEscape="false" />
					
					</td>
					<td class="width-15 active"><label class="pull-right"><!--<spring:message code="购买地点"></spring:message>:--></label></td>
					<td class="width-35">
						<!--<form:input path="purchaseLocation"  readonly="true"  cssClass="form-control input-sm " />-->
							
					</td>
				</tr>
				<%--
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="财务承担"></spring:message>:</label></td>
					<td colspan=3 class="width-35">
						<form:input path="paidByFinance"  readonly="true"  cssClass="form-control input-sm " />
						
					</td>
				</tr> --%>
				<!-- add by j 2017/6/8 begin -->
				<tr>
					<td colspan=6><label class="pull-left"><font size="4pt"><spring:message code="申请原因"></spring:message></font></label></td>
				</tr>
				<tr>
					<td  colspan=6 class="width-15 active"><label class="pull-left"><spring:message code="付款特別事項 (如有)"></spring:message>:</label></td>
					
				</tr>
				
				<tr>
					<td colspan=6 class="width-85">
						<form:textarea path="paymentSpecial" class="form-control " readonly='true' rows="3" maxlength="1000" htmlEscape="false" />
					</td>
				</tr>
				<tr>
					<td  colspan=6 class="width-15 active"><label class="pull-left"><spring:message code="请详述申请原因，并提供相关的数据"></spring:message>:</label></td>
					
				</tr>
				
				<tr>
					<td colspan=6 class="width-85">
						<form:textarea path="purchasePurpose" readonly="true" class="form-control required" rows="3" maxlength="1000" htmlEscape="false" />
					</td>
				</tr>
				<!-- 
				<tr>
					<td  class="width-15 active"><label class="pull-right">The date of last purchase same features of equipment:</label></td>
					<td colspan=5 class="width-45">
						
						<input id="lastTimeBuySameEquipment" name="lastTimeBuySameEquipment" type="text" maxlength="20" class=" laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${pr.lastTimeBuySameEquipment}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
				</tr> -->
				<tr>
					<td colspan=6 class="width-15 active"><label class="pull-left"><spring:message code="购买效益及汇报情况"></spring:message>:</label></td>
					
				</tr>
				<tr>
					<td colspan=6 class="width-85">
						<form:textarea path="returnOnInvestment"  readonly="true" class="form-control" rows="3" maxlength="1000" htmlEscape="false" />
					</td>
				</tr>
				<tr>
					<td colspan=6 class="width-15 active"><label class="pull-left"><spring:message code="预算使用情况和余额，如不包括于预算内，请详述原因"></spring:message>:</label></td>
					
				</tr>
				<tr>
					<td colspan=6 class="width-85">
						<form:textarea path="budgetRestSituation" class="form-control"  readonly="true" rows="3" maxlength="1000" htmlEscape="false" />
					</td>
				</tr>
				
				<!-- end -->
				
				<tr>
					<td colspan=6><label class="pull-left"><font size="4pt"><spring:message code="审批意见"></spring:message></font></label></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="意见"></spring:message>:</label></td>
					<td class="width-85" colspan=5>
						<form:textarea path="act.comment"  class="form-control" rows="3" maxlength="1000" htmlEscape="false" />
					</td>
				</tr>
				<tr>
					<td colspan=6><label class="pull-left"><font size="4pt"><spring:message code="报价单上传"></spring:message>:</font></label></td>					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red"></font><spring:message code='Quotation'></spring:message>1: </label></td>
					<td colspan=5 class="width-15 active">
					<c:if test="${not empty pr.attachment}">
							<a href="${fns:getDictValue('AzureStorage','AzureStorage','')}/prportal/${ pr.attachment}" target="_BLANK"  >${pr.attachment }</a>
							
					</c:if>
					
					</td>
				</tr>
				
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red"></font><spring:message code='Quotation'></spring:message>2: </label></td>
					<td colspan=5 class="width-15 active">
					<c:if test="${not empty pr.attachment2}">
							<a href="${fns:getDictValue('AzureStorage','AzureStorage','')}/prportal/${ pr.attachment2}" target="_BLANK"  >${pr.attachment2 }</a>
							
					</c:if>
					
					</td>
				</tr>
				
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red"></font><spring:message code='Quotation'></spring:message>3: </label></td>
					<td colspan=5 class="width-15 active">
					<c:if test="${not empty pr.attachment3}">
							<a href="${fns:getDictValue('AzureStorage','AzureStorage','')}/prportal/${ pr.attachment3}" target="_BLANK"  >${pr.attachment3 }</a>
							
					</c:if>
					
					</td>
				</tr>
				
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red"></font><spring:message code='Remarks/Appendix'></spring:message>: </label></td>
					<td colspan=5 class="width-15 active">
						<c:forEach items="${pr.attachmentArray}" var="atta">
							<a href="${fns:getDictValue('AzureStorage','AzureStorage','')}/prportal/${ atta}" target="_BLANK"  >${atta }</a>
							<br/>
						</c:forEach>
					
					
					</td>
				</tr>
		 	</tbody>
		</table>
		
		
	
		<div class="form-actions" style="padding:0;text-align:center;">
				<c:if test="${pr.act.taskDefKey ne 'apply_end'}">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="<spring:message code='同意'></spring:message>" onclick="$('#flag').val(1)"/>&nbsp;
					<input id="btnSubmit" class="btn btn-inverse" type="submit" value="<spring:message code='驳回到上一级'></spring:message>" onclick="$('#flag').val(2)"/>&nbsp; 
					<input id="btnSubmit" class="btn btn-inverse" type="submit" value="<spring:message code='驳回到申请者'></spring:message>" onclick="$('#flag').val(0)"/>&nbsp;
				</c:if>
			<input id="btnCancel" class="btn" type="button" value="<spring:message code='取消'></spring:message>" onclick="history.go(-1)"/>
			<!-- export add by J Li 2017/6/1 -->
			<!-- <input id="btnExport" class="btn" type="button" value="<spring:message code='导出'></spring:message>" onclick="ExportToExcelFormat(['tableContent','CurrentFlowTable','HistoryFlowTable'])" /> -->
		 
		</div>
		<p></p>
		<% /*<act:flowChart procInsId="${pr.act.procInsId}"/>*/%>
		<act:histoicFlow procInsId="${pr.act.procInsId}"/>
	</form:form>
</div>
	</div>
	</div>
</body>
</html>

