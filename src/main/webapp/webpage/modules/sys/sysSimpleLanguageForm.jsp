<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>国际化语言设置管理</title>
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
					loading('<spring:message code="正在提交，请稍等..."/>');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("<spring:message code='输入有误，请先更正。'/>");
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
		<form:form id="inputForm" modelAttribute="sysSimpleLanguage" action="${ctx}/sys/sysSimpleLanguage/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${pageSize}"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		   		<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code='标识'/>：</label></td>
					<td class="width-35">
						<form:input path="code" htmlEscape="false"    class="form-control "/>
					</td> 
					<td class="width-15 active"><label class="pull-right"><spring:message code='英文'/>：</label></td>
					<td class="width-35">
						<form:input path="en" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code='简体中文'/>：</label></td>
					<td class="width-35">
						<form:input path="cn" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><spring:message code='繁体中文'/>：</label></td>
					<td class="width-35">
						<form:input path="hk" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code='柬埔寨文'/>：</label></td>
					<td class="width-35">
						<form:input path="cam" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><spring:message code='越南文'/>：</label></td>
					<td class="width-35">
						<form:input path="vn" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr >
					<td class="width-15 active"><label  class="pull-right"><spring:message code='是否属于框架国际化'/>：</label></td>
					<td class="width-35">
						<form:select path="isSaveToPage" class="form-control " >
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>