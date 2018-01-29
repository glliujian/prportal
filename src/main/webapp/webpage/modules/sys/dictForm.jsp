<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title><spring:message code="字典管理"></spring:message></title>
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
			$("#value").focus();
			 validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('<spring:message code="正在提交，请稍等"></spring:message>...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text('<spring:message code="输入有误，请先更正"></spring:message>。');
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
	<form:form id="inputForm" modelAttribute="dict" action="${ctx}/sys/dict/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		         <td  class="width-15 active">	<label class="pull-right"><spring:message code="键值"></spring:message>:</label></td>
		         <td class="width-35" ><form:input path="value" htmlEscape="false" maxlength="200" class="form-control required"/></td>
		         <td  class="width-15 active">	<label class="pull-right"><spring:message code="标签"></spring:message>:</label></td>
		          <td  class="width-35" ><form:input path="label" htmlEscape="false" maxlength="50" class="form-control required"/></td>
		      </tr>
		       <tr>
		         <td  class="width-15 active">	<label class="pull-right"><spring:message code="类型"></spring:message>:</label></td>
		         <td class="width-35" ><form:input path="type" htmlEscape="false" maxlength="50" class="form-control required abc"/></td>
		         <td  class="width-15 active">	<label class="pull-right"><spring:message code="描述"></spring:message>:</label></td>
		          <td  class="width-35" ><form:input path="description" htmlEscape="false" maxlength="50" class="form-control required"/></td>
		      </tr>
		       <tr>
		         <td  class="width-15 active">	<label class="pull-right"><spring:message code="排序"></spring:message>:</label></td>
		         <td class="width-35" ><form:input path="sort" htmlEscape="false" maxlength="11" class="form-control required digits"/></td>
		         <td  class="width-15 active">	<label class="pull-right"><spring:message code="数据"></spring:message>:</label></td>
		          <td  class="width-35" ><form:textarea path="jsonData" htmlEscape="false" rows="3" maxlength="200" class="form-control "/></td>
		      </tr>
		   </tbody>
		   </table>   
	</form:form>
</body>
</html>