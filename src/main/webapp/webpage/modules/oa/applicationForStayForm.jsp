<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>入住申请管理</title>
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
			var now = new Date();
			if($("#startTime").val()==""){
				$("#startTime").val(new Date().format("yyyy-MM-dd"));
			}
			
			laydate({
	            elem: '#startTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus', //响应事件。如果没有传入event，则按照默认的click
	            choose: function (datas) {
	            	if($("#endTime").val()!=""){
	            		var d=new Date($("#endTime").val()).getTime()-new Date(datas).getTime();
	            		d=d/86400000;
	            		if(d<=0){
	            			alert(getLabel("开始时间需要小于结束时间"))
	            		}
	            		else{
	            			$("#stayDays").val(d)
	            		}
	            	}
	            }
	        });
			laydate({
	            elem: '#endTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus', //响应事件。如果没有传入event，则按照默认的click
	            choose: function (datas) {
		 	    	if($("#startTime").val()!=""){
	            		var d=new Date(datas).getTime()-new Date($("#startTime").val()).getTime();
	            		d=d/86400000;
	            		if(d<=0){
	            			alert(getLabel("开始时间需要小于结束时间"))
	            		}
	            		else{
	            			$("#stayDays").val(d)
	            		}
		 	    	}
		 	    }
	        });
			
		});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="applicationForStay" action="${ctx}/oa/applicationForStay/save" method="post" class="form-horizontal">
		<form:hidden id="applicationForStayID" path="id"/>
		<sys:message hideType="0" content="${message}"/>
		<div style="text-align:center">
		<font size="6pt">		
			<spring:message code="客房入住申請表"></spring:message>	
		</font>
		</div>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		   		<tr>
					<td class="width-15 active"><label class="pull-right">姓名：</label></td>
					<td class="width-35">
						<form:input path="createBy.name" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">部门：</label></td>
					<td class="width-35">
						<form:input path="createBy.office.name" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">性别：</label></td>
					<td class="width-35">
						<form:select id='gender' path="gender"  readonly="true" cssClass="form-control input-sm" cssStyle="height:35px">
							<form:options items="${fns:getDictList('gender')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">电话：</label></td>
					<td class="width-35">
						<form:input path="createBy.phone" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">职位：</label></td>
					<td class="width-35">
						<form:input path="position" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">籍贯：</label></td>
					<td class="width-35">
						<form:input path="nativePlace" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right ">原因：</label></td>
					<td class="width-35">
						<form:input path="reason" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right" >入住天数：</label></td>
					<td class="width-35">
						<form:input id="stayDays" path="days" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">开始时间：</label></td>
					<td class="width-35">
						<input id="startTime" name="startTime" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${applicationForStay.startTime}" pattern="yyyy-MM-dd"/>"/>
					</td>
					<td class="width-15 active"><label class="pull-right">结束时间：</label></td>
					<td class="width-35">
						<input id="endTime" name="endTime" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${applicationForStay.endTime}" pattern="yyyy-MM-dd"/>"/>
					</td>
				</tr>
		 	</tbody>
		</table>
		<c:if test="${empty applicationForStay.id}">
			<div class="form-actions" align="center">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" />
			</div>
		</c:if>
	</form:form>
</body>
</html>