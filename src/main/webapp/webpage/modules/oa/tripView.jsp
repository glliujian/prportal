<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>国内出差管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	var validateForm;
	function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if (validateForm.form()) {
			$("#inputForm").submit();
			return true;
		}

		return false;
	}
	$(document).ready(
			function() {
				validateForm = $("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
										} else {
											error.insertAfter(element);
										}
									}
								});

				laydate({
					elem : '#tripBeginDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
					event : 'focus' //响应事件。如果没有传入event，则按照默认的click
				});
				laydate({
					elem : '#tripEndDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
					event : 'focus' //响应事件。如果没有传入event，则按照默认的click
				});
				
				document.getElementById("chName").setAttribute("readonly","readonly");
				document.getElementById("department").setAttribute("readonly","readonly");
				document.getElementById("staffNo").setAttribute("readonly","readonly");
				document.getElementById("joinedDate").setAttribute("readonly","readonly");
				document.getElementById("extNo").setAttribute("readonly","readonly");
				document.getElementById("tripBeginDate").setAttribute("readonly","readonly");
				document.getElementById("tripEndDate").setAttribute("readonly","readonly");
				document.getElementById("destAddr").setAttribute("readonly","readonly");
				document.getElementById("purposes").setAttribute("readonly","readonly");
				document.getElementById("arrangements").setAttribute("readonly","readonly");
				document.getElementById("position").setAttribute("readonly","readonly");
				document.getElementById("phone").setAttribute("readonly","readonly");
			});
</script>
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="trip"
		action="${ctx}/oa/trip/record" method="post" class="form-horizontal">
		<form:hidden path="id" />
		<form:hidden path="act.taskId" />
		<form:hidden path="act.taskName" />
		<form:hidden path="act.taskDefKey" />
		<form:hidden path="act.procInsId" />
		<form:hidden path="act.procDefId" />
		<form:hidden id="flag" path="act.flag" />
		<sys:message content="${message}" />
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">姓名：</label></td>
					<td class="width-35"><form:input path="chName"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">工号：</label></td>
					<td class="width-35"><form:input path="staffNo"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">部门：</label></td>
					<td class="width-35"><form:input path="department"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">职位：</label></td>
					<td class="width-35"><form:input path="position"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">入职时间：</label></td>
					<td class="width-35"><input id="joinedDate" name="joinedDate"
						type="text" maxlength="20"
						class="laydate-icon form-control layer-date "
						value="<fmt:formatDate value="${trip.joinedDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">内线：</label></td>
					<td class="width-35"><form:input path="extNo"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">电话号码：</label></td>
					<td class="width-35"><form:input path="phone"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">出差开始时间：</label></td>
					<td class="width-35"><input id="tripBeginDate"
						name="tripBeginDate" type="text" maxlength="20"
						class="laydate-icon form-control layer-date "
						value="<fmt:formatDate value="${trip.tripBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
					</td>
					<td class="width-15 active"><label class="pull-right">出差结束时间：</label></td>
					<td class="width-35"><input id="tripEndDate"
						name="tripEndDate" type="text" maxlength="20"
						class="laydate-icon form-control layer-date "
						value="<fmt:formatDate value="${trip.tripEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">出差地点：</label></td>
					<td class="width-35"><form:textarea path="destAddr"
							htmlEscape="false" rows="4" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">出差原因：</label></td>
					<td class="width-35"><form:textarea path="purposes"
							htmlEscape="false" rows="4" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">出差安排：</label></td>
					<td class="width-35">
					<table id="arrangementsTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<tr>
								<th  class="ariOrg">出发地点 </th>
								<th  class="airDest">目的地</th>
								<th  class="hotel">酒店名称</th>
								<th  class="other">其他信息</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${trip.arrangementOBJs}" var="arrangementOBJ">
							<tr>
								<td> 
									${arrangementOBJ.ariOrg}
								</td>
								<td>
									${arrangementOBJ.airDest}
								</td>
								<td>
									${arrangementOBJ.hotel}
								</td>
								<td>
									${arrangementOBJ.other}
								</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="form-actions" align="center">
			<input id="btnSubmit" class="btn btn-inverse" type="submit"
				value="备案" onclick="$('#flag').val('yes')" />&nbsp; <input
				id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
		<act:flowChart procInsId="${trip.act.procInsId}" />
		<act:histoicFlow procInsId="${trip.act.procInsId}" />
	</form:form>
</body>
</html>