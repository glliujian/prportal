<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工作流委托設置管理</title>
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
			
					laydate({
			            elem: '#startTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
					laydate({
			            elem: '#endTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
		});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="wfDelegate" action="${ctx}/wf/delegate/wfDelegate/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<c:set var="dataScope" value="${fns:getUser().dataScope }" />
		<c:set var="flag" value="disabled" />
		<c:if test="${dataScope eq '1'}">
			<c:set var="flag" value="" />
		</c:if>
		
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>					
					<td class="width-15 active"><label class="pull-right"><spring:message code="委托人"></spring:message>：</label></td>
					<td class="width-35">
						<sys:treeselect id="owner" name="owner.id" value="${wfDelegate.owner.id}" labelName="owner.name" labelValue="${wfDelegate.owner.name}"
							title="用户" url="/sys/office/treeData?type=3" cssClass="form-control " allowClear="true" notAllowSelectParent="true" disabled= '${flag}' />
					</td>
					<td class="width-15 active"><label class="pull-right"><spring:message code="代理人"></spring:message>：</label></td>
					<td class="width-35">
						<sys:treeselect id="delegate" name="delegate.id" value="${wfDelegate.delegate.id}" labelName="delegate.name" labelValue="${wfDelegate.delegate.name}"
							title="用户" url="/sys/office/treeData?type=3" cssClass="form-control " allowClear="true" notAllowSelectParent="true"  />
					</td>
				</tr>
				<%--<tr>
					
					 <td class="width-15 active"><label class="pull-right"><spring:message code="流程类型"></spring:message>：</label></td>
					<td class="width-35">
						<form:select path="flowType" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getSqlDictList('GET_FLOW_TYPE','')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>--%>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="开始时间"></spring:message>：</label></td>
					<td class="width-35">
						<input id="startTime" name="startTime" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${wfDelegate.startTime}" pattern="yyyy-MM-dd"/>"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><spring:message code="结束时间"></spring:message>：</label></td>
					<td class="width-35">
						<input id="endTime" name="endTime" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${wfDelegate.endTime}" pattern="yyyy-MM-dd"/>"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="备注信息"></spring:message>：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>