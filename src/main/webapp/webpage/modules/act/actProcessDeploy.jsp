<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>部署流程 - 流程管理</title>
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
		$(document).ready(function(){
			top.$.jBox.tip.mess = null;
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
<body>
	<br/>
	<sys:message content="${message}"/>
	<form id="inputForm" action="${ctx}/act/process/deploy" method="post" enctype="multipart/form-data" class="form-horizontal">
		<div class="control-group">
			<label class="control-label"><spring:message code="流程分类"></spring:message>：</label>
			<div class="controls">
				<select id="category" name="category" class="required form-control">
					<c:forEach items="${fns:getDictList('act_category')}" var="dict">
						<option value="${dict.value}">${dict.label}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><spring:message code="流程文件"></spring:message>：</label>
			<div class="controls">
				<input type="file" id="file" name="file" class="required form-control"/>
				<span class="help-inline"><spring:message code="支持文件格式"></spring:message>：zip<spring:message code="、"></spring:message>bar<spring:message code="、"></spring:message>bpmn<spring:message code="、"></spring:message>bpmn20.xml</span>
			</div>
		</div>
	</form>
</body>
</html>
