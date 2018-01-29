<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>出差管理</title>
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
					event : 'focus', //响应事件。如果没有传入event，则按照默认的click
				//min: laydate.now()
				});

				laydate({
					elem : '#tripEndDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
					event : 'focus', //响应事件。如果没有传入event，则按照默认的click
					choose : function(datas) {
						if ($("#tripEndDate").val() != "") {
							var d = new Date($("#tripEndDate").val()).getTime()
									- new Date($("#tripBeginDate").val())
											.getTime();
							//d=d/86400000;
							if (d < 0) {
								alert(getLabel("开始时间需要小于结束时间"))
								$("#stayDays").val("")
							}
						}
					}
				});
			});
</script>
</head>
<body class="hideScroll">
	<div class="ibox-title">
		<h5>出差申请</h5>
	</div>
	<form:form id="inputForm" modelAttribute="trip"
		action="${ctx}/oa/trip/save" method="post" class="form-horizontal"
		onsubmit="return onCheck()">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">姓名：</label></td>
					<td class="width-35"><input id="chName" name="chName"
						type="text" readonly="readonly" maxlength="20"
						class="form-control " value="${trip.createBy.name}" /></td>
					<td class="width-15 active"><label class="pull-right">工号：</label></td>
					<td class="width-35"><input id="staffNo" name="staffNo"
						type="text" readonly="readonly" maxlength="20"
						class="form-control " value="${trip.createBy.no}" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">部门：</label></td>
					<td class="width-35"><input id="department" name="department"
						type="text" readonly="readonly" maxlength="20"
						class="form-control " value="${trip.createBy.office.name}" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">职位：</label></td>
					<td class="width-35"><input id="position" name="position"
						type="text" readonly="readonly" maxlength="20"
						class="form-control " value="${trip.createBy.role.name}" /></td>
					<td class="width-15 active"><label class="pull-right">入职时间：</label></td>
					<td class="width-35"><input id="joinedDate" name="joinedDate"
						type="text" maxlength="20" readonly="readonly"
						class="laydate-icon form-control layer-date "
						value="<fmt:formatDate value="${trip.createBy.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">内线：</label></td>
					<td class="width-35"><input id="extNo" name="extNo"
						type="text" readonly="readonly" maxlength="20"
						class="form-control " value="${trip.createBy.phone}" /></td>
					<td class="width-15 active"><label class="pull-right">手机号码：</label></td>
					<td class="width-35"><input id="phone" name="phone"
						type="text" readonly="readonly" maxlength="20"
						class="form-control " value="${trip.createBy.mobile}" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">出差类型</label></td>
					<td><form:select path="tripType" class="form-control required">
							<form:option value="china">国内出差</form:option>
							<form:option value="oversea">港外公干</form:option>
						</form:select></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">出差开始时间：</label></td>
					<td class="width-35"><input id="tripBeginDate"
						name="tripBeginDate" type="text" maxlength="20"
						class="laydate-icon form-control layer-date required "
						value="<fmt:formatDate value="${trip.tripBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
					</td>
					<td class="width-15 active"><label class="pull-right">出差结束时间：</label></td>
					<td class="width-35"><input id="tripEndDate"
						name="tripEndDate" type="text" maxlength="20"
						class="laydate-icon form-control layer-date required "
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
					<form:textarea cssStyle="display:none" id="arrangements" path="arrangements"
							htmlEscape="false" rows="4" class="form-control " />
						<div id="addArrangement" class="btn btn-primary"  onclick="addTripArrangement()" ><i class="glyphicon glyphicon-plus"></i> 新建</div>
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
									<div  onclick="deleteTripArrangement(this)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</div>
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
			<input id="btnSubmit" class="btn btn-primary" type="submit"
				value="提 交" onClick="onCheck" />&nbsp; <input id="btnCancel"
				class="btn" type="button" value="返 回" onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>