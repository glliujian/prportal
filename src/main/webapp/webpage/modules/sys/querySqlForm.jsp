<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>自定义查询管理</title>
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
		<form:form id="inputForm" modelAttribute="querySql" action="${ctx}/sys/querySql/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="SQL代码"></spring:message>：</label></td>
					<td class="width-85" colspan=3>
						<form:input path="sqlCode" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">SQL：</label></td>
					<td class="width-85" colspan=3>
						<form:textarea path="sqlStr" htmlEscape="false" rows="4" class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">Sample：</label></td>
					<td class="width-85" colspan=3>
						select key_ as <font color="red">value</font>, name_ as <font color="red">lable</font> from act_re_model where category_ = <font color="red">\#{param1}</font> and version_= <font color="red">\#{param2}</font>
						<br>
						<li><spring:message code="输出字段必须转换为value,lable"></spring:message></li>
						<li>\#{param1},\#{param2}...<spring:message code="是sql的变量，可选"></spring:message></li>
						<li><spring:message code="对应JSP调用"></spring:message><spring:message code="："></spring:message>：\${fns:getSqlDictList('GET_FLOW_TYPE')}</li>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>