<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>国内出差申请</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	var validateForm;
	var arrangements=${trip.arrangements}||[];
	function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if (validateForm.form()) {
			$("#inputForm").submit();
			return true;
		}

		return false;
	}
	function addTripArrangement(){
		var tr = $("<tr><td></td><td></td><td></td><td></td><td></td></tr>");
		$("td", tr).eq(0).html("<input></input>");
		$("td", tr).eq(1).html("<input></input>");
		$("td", tr).eq(2).html("<input></input>");
		$("td", tr).eq(3).html("<input></input>");
		$("td", tr).eq(4).html(
				"<a class='btn btn-primary'  onclick='addTripArrangementOK(this)' >"
					+"<i class='glyphicon glyphicon-plus'></i> 确定</a>");
		$("#arrangementsTable").append(tr);
		$("#addArrangement").attr("disabled",true); 
	}
	function removeInput(td){
		var d=td.children("input:first-child").val();
		td.html(d);
		return d;
	}
	function addTripArrangementOK(pThis){
		var parentTR=$(pThis.parentElement.parentElement);
		var json={};
		json.ariOrg=removeInput($("td", parentTR).eq(0));
		json.airDest=removeInput($("td", parentTR).eq(1));
		json.hotel=removeInput($("td", parentTR).eq(2));
		json.other=removeInput($("td", parentTR).eq(3));
		arrangements.push(json);
		$("#arrangements").val(encodeURIComponent(JSON.stringify(arrangements)));
		$("td", parentTR).eq(4).html(
				"<div  onclick='deleteTripArrangement(this)'   class='btn btn-danger btn-xs'"+
				"><i class='fa fa-trash'></i> 删除</div>");
		$("#addArrangement").removeAttr("disabled"); 
		
	}
	function deleteTripArrangement(pThis){
		arrangements.splice(pThis.parentElement.parentElement.rowIndex-1, 1);
		$("#arrangements").val(JSON.stringify(arrangements));
		$(pThis.parentElement.parentElement).remove(); 
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
				$("#arrangements").val(encodeURIComponent(JSON.stringify(arrangements)));
				laydate({
					elem : '#tripBeginDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
					event : 'focus' //响应事件。如果没有传入event，则按照默认的click
				});
				laydate({
					elem : '#tripEndDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
					event : 'focus' //响应事件。如果没有传入event，则按照默认的click
				});

				if ('${trip.act.taskDefKey}' == "tripChange") {
					//$('#chName').removeAttr("readonly");
					//$('#department').removeAttr("readonly");
					//$('#staffNo').removeAttr("readonly");
					//$('#joinedDate').removeAttr("readonly");
					//$('#extNo').removeAttr("readonly");
					$('#tripBeginDate').removeAttr("readonly");
					$('#tripEndDate').removeAttr("readonly");
					$('#destAddr').removeAttr("readonly");
					$('#purposes').removeAttr("readonly");
					$('#arrangements').removeAttr("readonly");
					document.getElementById("act.comment").setAttribute(
							"readonly", "readonly");
				} else {
					document.getElementById("chName").setAttribute("readonly",
							"readonly");
					document.getElementById("department").setAttribute(
							"readonly", "readonly");
					document.getElementById("staffNo").setAttribute("readonly",
							"readonly");
					document.getElementById("joinedDate").setAttribute(
							"readonly", "readonly");
					document.getElementById("extNo").setAttribute("readonly",
							"readonly");
					document.getElementById("tripBeginDate").setAttribute(
							"readonly", "readonly");
					document.getElementById("tripEndDate").setAttribute(
							"readonly", "readonly");
					document.getElementById("destAddr").setAttribute(
							"readonly", "readonly");
					document.getElementById("purposes").setAttribute(
							"readonly", "readonly");
					document.getElementById("arrangements").setAttribute(
							"readonly", "readonly");
					$('#act.comment').removeAttr("readonly");
					$('#tripBeginDate').attr("disabled", true);
					$('#tripEndDate').attr("disabled", true);
				}
			});
</script>
</head>
<body class="hideScroll">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>${trip.act.taskName}</h5>
				<div class="ibox-tools">
					<a class="collapse-link"> <i class="fa fa-chevron-up"></i>
					</a>
					<!-- <a class="dropdown-toggle" data-toggle="dropdown" href="#"> <i
						class="fa fa-wrench"></i>
					</a>
					<ul class="dropdown-menu dropdown-user">
						<li><a href="#">选项1</a></li>
						<li><a href="#">选项2</a></li>
					</ul> -->
					<a class="close-link"> <i class="fa fa-times"></i>
					</a>
				</div>
			</div>
			<div class="ibox-content">
				<form:form id="inputForm" modelAttribute="trip"
					action="${ctx}/oa/trip/saveAudit" method="post"
					class="form-horizontal">
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
								<td class="width-35"><input id="chName" name="chName"
									type="text" readonly="readonly" maxlength="20"
									class="form-control " value="${trip.chName}" /></td>
								<td class="width-15 active"><label class="pull-right">工号：</label></td>
								<td class="width-35"><input id="staffNo" name="staffNo"
									type="text" readonly="readonly" maxlength="20"
									class="form-control " value="${trip.staffNo}" /></td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">部门：</label></td>
								<td class="width-35"><input id="department"
									name="department" type="text" readonly="readonly"
									maxlength="20" class="form-control " value="${trip.department}" /></td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">职位：</label></td>
								<td class="width-35"><input id="position"
									name="position" type="text" readonly="readonly"
									maxlength="20" class="form-control " value="${trip.position}" /></td>
								<td class="width-15 active"><label class="pull-right">入职时间：</label></td>
								<td class="width-35"><input id="joinedDate"
									name="joinedDate" type="text" maxlength="20"
									readonly="readonly"
									class="laydate-icon form-control layer-date "
									value="<fmt:formatDate value="${trip.joinedDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
								</td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">内线：</label></td>
								<td class="width-35"><input id="extNo" name="extNo"
									type="text" readonly="readonly" maxlength="20"
									class="form-control " value="${trip.extNo}" /></td>
									<td class="width-15 active"><label class="pull-right">手机号码：</label></td>
								<td class="width-35"><input id="phone" name="phone"
									type="text" readonly="readonly" maxlength="20"
									class="form-control " value="${trip.phone}" /></td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">出差开始时间：</label></td>
								<td class="width-35"><input id="tripBeginDate"
									name="tripBeginDate" type="text" maxlength="20"
									readonly="readonly"
									class="laydate-icon form-control layer-date "
									value="<fmt:formatDate value="${trip.tripBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
								</td>
								<td class="width-15 active"><label class="pull-right">出差结束时间：</label></td>
								<td class="width-35"><input id="tripEndDate"
									name="tripEndDate" type="text" maxlength="20"
									readonly="readonly"
									class="laydate-icon form-control layer-date "
									value="<fmt:formatDate value="${trip.tripEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
								</td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">出差地点：</label></td>
								<td class="width-35"><input id="destAddr" name="destAddr"
									type="text" readonly="destAddr" maxlength="20"
									class="form-control " value="${trip.destAddr}" /></td>
									<td class="width-15 active"><label class="pull-right">出差原因：</label></td>
								<td class="width-35"><input id="purposes" name="purposes"
									type="text" readonly="purposes" maxlength="20" 
									class="form-control " value="${trip.purposes}" /></td>
							</tr>
							<tr>
								<td class="width-15 active"><label class="pull-right">出差安排：</label></td>
								<td class="width-35">
								<form:textarea cssStyle="display:none" id="arrangements" path="arrangements"
										htmlEscape="false" rows="4" class="form-control " />
									<c:if test="${trip.act.taskDefKey eq 'tripChange'}">
										<div id="addArrangement" class="btn btn-primary"  onclick="addTripArrangement()" ><i class="glyphicon glyphicon-plus"></i> 新建</div>
									</c:if>
									<table id="arrangementsTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
									<thead>
										<tr>
											<th  class="ariOrg">出发地点 </th>
											<th  class="airDest">目的地</th>
											<th  class="hotel">酒店名称</th>
											<th  class="other">其他信息</th>
											<th  >操作</th>
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
											<td>
											<c:if test="${trip.act.taskDefKey eq 'tripChange'}">
												<div  onclick="deleteTripArrangement(this)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</div>
											</c:if>
											</td>
										</tr>
									</c:forEach>
									</tbody>
								</table>
								</td>
							</tr>
							<c:if test="${trip.act.taskDefKey ne 'tripChange'}">
								<tr>
									<td class="width-15 active"><label class="pull-right">您的意见:</label></td>
									<td colspan="5"><form:textarea path="act.comment"
											class="form-control required" rows="5" maxlength="20" /></td>
								</tr>
							</c:if>
						</tbody>
					</table>
					<div class="form-actions">
						<c:if
							test="${trip.act.taskDefKey ne 'tripEnd' and trip.act.taskDefKey ne 'tripChange'}">
							<input id="btnSubmit" class="btn btn-primary" type="submit"
								value="同 意" onclick="$('#flag').val('yes')" />&nbsp;
					<input id="btnSubmit" class="btn btn-inverse" type="submit"
								value="驳 回" onclick="$('#flag').val('no')" />&nbsp;
						</c:if>
						<c:if test="${trip.act.taskDefKey eq 'tripChange'}">
							<input id="btnSubmit" class="btn btn-primary" type="submit"
								value="提交" onclick="$('#flag').val('yes')" />&nbsp;
					<input id="btnSubmit" class="btn btn-inverse" type="submit"
								value="停止" onclick="$('#flag').val('no')" />&nbsp;
				</c:if>
						<input id="btnCancel" class="btn" type="button" value="返 回"
							onclick="history.go(-1)" />
					</div>
					<act:flowChart procInsId="${trip.act.procInsId}" />
					<act:histoicFlow procInsId="${trip.act.procInsId}" />
				</form:form>
			</div>
		</div>
	</div>
</body>
</html>