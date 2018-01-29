<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工作流用户组管理</title>
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
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
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
		<form:form id="inputForm" modelAttribute="wfUserGroup" action="${ctx}/wf/usergroup/wfUserGroup/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>					
					<td class="width-15 active"><label class="pull-right"><spring:message code="公司"></spring:message>：</label></td>
					<td class="width-35">
						<sys:treeselect id="company" name="company.id" value="${wfUserGroup.company.id}" labelName="company.name" labelValue="${wfUserGroup.company.name}"
							title="公司" url="/sys/office/treeData?type=1" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><spring:message code="部门"></spring:message>：</label></td>
					<td class="width-35">
						<sys:treeselect id="department" name="department.id" value="${wfUserGroup.department.id}" labelName="department.name" labelValue="${wfUserGroup.department.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					</td>
				</tr>
				<tr>					
					<td class="width-15 active"><label class="pull-right"><spring:message code="流程标识"></spring:message>：</label></td>
					<td class="width-35">
						<form:select path="procDefKey" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getSqlDictList('GET_FLOW_TYPE','')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><spring:message code="组标识"></spring:message>：</label></td>
					<td class="width-35">
						<form:input path="userGroup" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>					
					<td class="width-15 active"><label class="pull-right"><spring:message code="用户"></spring:message>：</label></td>
					<td class="width-35">
						<sys:treeselect id="user" name="user.id" value="${wfUserGroup.user.id}" labelName="user.name" labelValue="${wfUserGroup.user.name}"
							title="用户" url="/sys/office/treeData?type=3" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><spring:message code="备注"></spring:message>：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
				
		 	</tbody>
		</table>
	</form:form>
</body>
</html>