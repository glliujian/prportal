<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>通知管理</title>
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
		$(document).ready(function() {
			//$("#name").focus();
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading("<spring:message code="<spring:message code='正在提交，请稍等...'></spring:message>"></spring:message>");
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
		});
	</script>
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="oaNotify" action="${ctx}/oa/oaNotify/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		         <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font><spring:message code="类型"></spring:message>：</label></td>
		         <td class="width-35" ><form:select path="type" class="form-control required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('oa_notify_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select></td>
		         <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font><spring:message code="标题"></spring:message>：</label></td>
		         <td class="width-35" ><form:input path="title" htmlEscape="false" maxlength="200" class="form-control required"/></td>
		      </tr>
		       <tr>
		         <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font><spring:message code="内容"></spring:message>：</label></td>
		         <td class="width-35" ><form:textarea path="content" htmlEscape="false" rows="6" maxlength="2000" class="form-control required"/></td>
		         <td  class="width-15 active">	<label class="pull-right"><spring:message code="附件"></spring:message>：</label></td>
		         <td class="width-35" >
		         <c:if test="${oaNotify.status ne '1'}">
					<form:hidden id="files" path="files" htmlEscape="false" maxlength="255" class="form-control"/>
					<sys:ckfinder input="files" type="files" uploadPath="/oa/notify" selectMultiple="true"/>
				</c:if>
		         <c:if test="${oaNotify.status eq '1'}">
					<form:hidden id="files" path="files" htmlEscape="false" maxlength="255" class="form-control"/>
					<sys:ckfinder input="files" type="files" uploadPath="/oa/notify" selectMultiple="true" readonly="true" />
		         </c:if>
		         
		         
		         </td>
		      </tr>
		      
		      <c:if test="${oaNotify.status ne '1'}">
		      	 <tr>
			         <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font><spring:message code="状态"></spring:message>：</label></td>
			         <td class="width-35" ><form:radiobuttons path="status" items="${fns:getDictList('oa_notify_status')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/></td>
			         <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font><spring:message code="接受人"></spring:message>：</label></td>
			         <td class="width-35" ><sys:treeselect id="oaNotifyRecord" name="oaNotifyRecordIds" value="${oaNotify.oaNotifyRecordIds}" labelName="oaNotifyRecordNames" labelValue="${oaNotify.oaNotifyRecordNames}"
							title="用户" url="/sys/office/treeData?type=3" cssClass="form-control required" notAllowSelectParent="true" checked="true"/></td>
		      	</tr>
			</c:if>
			
					<c:if test="${oaNotify.status eq '1'}">
					  <tr>
				         <td  class="width-15 active">	<label class="pull-right"><spring:message code="接受人"></spring:message>：</label></td>
				         <td class="width-35" colspan="3"><table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
								<thead>
									<tr>
										<th><spring:message code="接受人"></spring:message></th>
										<th><spring:message code="接受部门"></spring:message></th>
										<th><spring:message code="阅读状态"></spring:message></th>
										<th><spring:message code="阅读时间"></spring:message></th>
									</tr>
								</thead>
								<tbody>
								<c:forEach items="${oaNotify.oaNotifyRecordList}" var="oaNotifyRecord">
									<tr>
										<td>
											${oaNotifyRecord.user.name}
										</td>
										<td>
											${oaNotifyRecord.user.office.name}
										</td>
										<td>
											${fns:getDictLabel(oaNotifyRecord.readFlag, 'oa_notify_read', '')}
										</td>
										<td>
											<fmt:formatDate value="${oaNotifyRecord.readDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
										</td>
									</tr>
								</c:forEach>
								</tbody>
							</table>
							<spring:message code='已查阅'></spring:message>：${oaNotify.readNum} &nbsp; <spring:message code='未查阅'></spring:message>：${oaNotify.unReadNum} &nbsp; <spring:message code='总共'></spring:message>：${oaNotify.readNum + oaNotify.unReadNum}</td>
				      </tr>
		</c:if>
		</tbody>
		</table>
		     
		

	</form:form>
</body>
</html>