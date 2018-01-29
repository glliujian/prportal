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

			if($("#checkInTime").val()==""){
				$("#checkInTime").val(new Date().format("yyyy-MM-dd"));
			}

			if($("#checkOutTime").val()==""){
				$("#checkOutTime").val(new Date().format("yyyy-MM-dd"));
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
			if("${applicationForStay.act.taskDefKey}"=='checkIn'){//removeAttr
				$("#act-comment").hide();$("#interPayer").hide();
				$("#interCheckOut").hide();$("#changeSubmitButton").hide();
				$("#leadSubmitButton").hide();
				
				$('#endTime').attr("disabled",true); 
				$('#startTime').attr("disabled",true);
				if("${applicationForStay.act.status}"!="finish"){
					$('#checkInTime').addClass("required")
					$('#roomNo').addClass("required")
					$("#roomNo").removeAttr("readonly");
				}
				else{
					$("#dormitorySubmitButton").hide();
					$('#checkInTime').attr("disabled",true);
				}
			}
			if("${applicationForStay.act.taskDefKey}"=='checkOut'){
				$("#act-comment").hide();
				$("#changeSubmitButton").hide();$("#leadSubmitButton").hide();
			
				$('#checkInTime').attr("disabled",true);
				$('#endTime').attr("disabled",true); 
				$('#startTime').attr("disabled",true);
				
				if("${applicationForStay.act.status}"!="finish"){
					$('#checkOutTime').addClass("required")
					$('#cost').addClass("required")
					$('#payer').addClass("required")
					$("#cost").removeAttr("readonly");
					$("#payer").removeAttr("readonly");
				}
				else{
					$("#dormitorySubmitButton").hide();
					$('#checkOutTime').attr("disabled",true);
				}
			}
			if("${applicationForStay.act.taskDefKey}"=='reservationChange'){
				$("#dormitorySubmitButton").hide();
				$("#interCheckIn").hide();
				$("#interPayer").hide();
				$("#interCheckOut").hide();
				$("#leadSubmitButton").hide();
				$("#act-comment").hide();
				
				if("${applicationForStay.act.status}"!="finish"){
					$("#createByName").removeAttr("readonly");
					$("#officeName").removeAttr("readonly");
					$("#gender").removeAttr("readonly");
					$("#createByPhone").removeAttr("readonly");
					$("#position").removeAttr("readonly");
					$("#nativePlace").removeAttr("readonly");
					$("#reason").removeAttr("readonly");
				}
				else{
					$("#changeSubmitButton").hide();
				}
			}

			if("${applicationForStay.act.taskDefKey}"=='genLeaderAudit' || "${applicationForStay.act.taskDefKey}"=='deptLeaderAudit'){
				$("#interCheckIn").hide();$("#interCheckOut").hide();$("#dormitorySubmitButton").hide();
				$("#interPayer").hide();$("#changeSubmitButton").hide();
				if("${applicationForStay.act.status}"!="finish"){
					$("#inputActComment").removeAttr("readonly");
				}
				else{
					$("#leadSubmitButton").hide();
				}
				$('#endTime').attr("disabled",true); 
				$('#startTime').attr("disabled",true);
				
			}
			laydate({
		        elem: '#checkInTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
		        event: 'focus' //响应事件。如果没有传入event，则按照默认的click
		    });
			
			laydate({
		        elem: '#checkOutTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
		        event: 'focus' //响应事件。如果没有传入event，则按照默认的click
		    });
	
		});
	</script>
</head>
<body class="hideScroll">
		<h5>当前步骤--[${applicationForStay.act.taskName}]</h5>
		<form:form id="inputForm" modelAttribute="applicationForStay" action="${ctx}/oa/applicationForStay/saveAudit" method="post" class="form-horizontal">
		<form:hidden id="applicationForStayID" path="id"/>
		<form:hidden path="act.taskId" />
		<form:hidden path="act.taskName" />
		<form:hidden path="act.taskDefKey" />
		<form:hidden path="act.procInsId" />
		<form:hidden path="act.procDefId" />
		<form:hidden id="flag" path="act.flag" />
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
						<form:input id='createByName' path="createBy.name" htmlEscape="false" readonly="true" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">部门：</label></td>
					<td class="width-35">
						<form:input id='officeName' path="createBy.office.name" htmlEscape="false" readonly="true"   class="form-control "/>
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
						<form:input id='createByPhone' path="createBy.phone" htmlEscape="false" readonly="true"   class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">职位：</label></td>
					<td class="width-35">
						<form:input id='position' path="position" htmlEscape="false" readonly="true"   class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">籍贯：</label></td>
					<td class="width-35">
						<form:input id='nativePlace' path="nativePlace" htmlEscape="false" readonly="true"   class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">原因：</label></td>
					<td class="width-35">
						<form:input id='reason' path="reason" htmlEscape="false" readonly="true"   class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">入住天数：</label></td>
					<td class="width-35">
						<form:input id="stayDays" path="days" htmlEscape="false" readonly="true"   class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">开始时间：</label></td>
					<td class="width-35">
						<input id="startTime"  name="startTime" type="text" maxlength="20"  class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${applicationForStay.startTime}" pattern="yyyy-MM-dd"/>"/>
					</td>
					<td class="width-15 active"><label class="pull-right">结束时间：</label></td>
					<td class="width-35">
						<input id="endTime"  name="endTime" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${applicationForStay.endTime}" pattern="yyyy-MM-dd"/>"/>
					</td>
				</tr>

				<tr id="act-comment">
					<td class="width-15 active"><label class="pull-right">审批意见：</label></td>	
					<td class="width-35">
						<form:textarea id="inputActComment" path="act.comment" readonly="true"   class="form-control required" rows="5" maxlength="20" />
					</td>
					<td class="width-15 active"></td>
					<td class="width-35">
	
					</td>
				</tr>
		 		<tr id="interCheckIn">
					<td class="width-15 active"><label class="pull-right">入住时间：</label></td>	
					<td class="width-35">
						<input id="checkInTime"  name="checkInTime" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${applicationForStay.checkInTime}" pattern="yyyy-MM-dd"/>"/>
					</td>
					<td class="width-15 active"><label class="pull-right">房号：</label></td>
					<td class="width-35">
						<form:input id='roomNo' path="roomNo" readonly="true" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
		 		<tr id="interCheckOut">
					<td class="width-15 active"><label class="pull-right">退房时间：</label></td>	
					<td class="width-35">
						<input id="checkOutTime" name="checkInTime" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${applicationForStay.checkOutTime}" pattern="yyyy-MM-dd"/>"/>
					</td>
					<td class="width-15 active"><label class="pull-right">费用：</label></td>
					<td class="width-35">
						<form:input id='cost' path="cost" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr id="interPayer">
					<td class="width-15 active"><label class="pull-right">付款人：</label></td>	
					<td class="width-35">
						<form:select id='payer' path="payer"  readonly="true" cssClass="form-control input-sm" cssStyle="height:35px" >
							<form:options items="${fns:getDictList('payer')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</td>
					<td class="width-15 active"></td>
					<td class="width-35">
						
					</td>
				</tr>
		 	</tbody>
		</table>

		<div class="form-actions" align="center">
			<span id='leadSubmitButton'>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="同 意" onclick="$('#flag').val('yes')" />&nbsp;
				<input  class="btn btn-inverse" type="submit" value="驳 回" onclick="$('#flag').val('no')" />&nbsp;				
			</span>
			<span id='changeSubmitButton'>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="提交" onclick="$('#flag').val('yes')" />&nbsp;
				<input id="btnSubmit" class="btn btn-inverse" type="submit" value="停止" onclick="$('#flag').val('no')" />&nbsp;
			</span>
			<span id='dormitorySubmitButton'>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="提交" onclick="$('#flag').val('yes')" />
			</span>

		</div>
			<act:flowChart procInsId="${applicationForStay.act.procInsId}" />
			<act:histoicFlow procInsId="${applicationForStay.act.procInsId}" />

	</form:form>
</body>
</html>