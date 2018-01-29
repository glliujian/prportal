<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工作流處理人設置管理</title>
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
			
		});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="wfAssignee" action="${ctx}/wf/assignee/wfAssignee/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>					
					<td class="width-15 active"><label class="pull-right"><spring:message code="处理人流程变量"></spring:message>：</label></td>
					<td class="width-35">
						<form:input path="assigneeVar" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><spring:message code="流程类型"></spring:message>：</label></td>
					<td class="width-35">
						<form:select path="flowType" class="form-control ">
							<form:option value="*" label="ALL"/>
							<form:options items="${fns:getSqlDictList('GET_FLOW_TYPE',null)}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="取值sql"></spring:message>：</label></td>
					<td class="width-85" colspan=3>
						<form:textarea path="sqlStr" htmlEscape="false"  rows="4"  class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="说明"></spring:message>：</label></td>
					<td class="width-85 active" colspan=3>
						<%-- <form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/> --%>
						<spring:message code="以登录的用户ID为唯一变量，找出与其关联的人"></spring:message>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>