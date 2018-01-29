<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>语言配置管理</title>
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
		<form:form id="inputForm" modelAttribute="language" action="${ctx}/sys/language/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="标签"></spring:message><spring:message code="："></spring:message></label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><spring:message code="内容展现"></spring:message><spring:message code="："></spring:message></label></td>
					<td class="width-35">
						<form:input path="text" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="语言"></spring:message><spring:message code="："></spring:message></label></td>
					<td class="width-35">
						<form:select path="language"  cssClass="form-control input-sm required" >
							<form:option value="" label="--Please select--"/>
							<form:options items="${fns:getDictList('LANGUAGE')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>