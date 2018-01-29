<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title><spring:message code="待办任务"></spring:message></title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
	        laydate({
	            elem: '#beginDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        refreshTabLocal($(".J_menuTabs", window.parent.document).find(".J_menuTab").eq(0).data('id'));
	        
	        closeTabLocal();
		});
		function closeTabLocal()
		{
			console.log("${MType}")
			
			if("${MType}"=="success")
			{			
				refreshTabLocal("/prportal/a/act/task/todo/");
				$(".J_menuTabs", window.parent.document).find(".J_menuTab").filter(".active").find("i").trigger("click");				
			}
			
		}
		
		/**
		 * 签收任务
		 */
		function claim(taskId) {
			$.get('${ctx}/act/task/claim' ,{taskId: taskId}, function(data) {
				if (data == 'true'){
		        	top.$.jBox.tip("<spring:message code='签收完成'></spring:message>");
		            location = '${ctx}/act/task/todo/?loginName='+$("#initiator").val() + 	    				
    				"&applicationNo="+$("#applicationNo").val() +'&beginDate='+$("#beginDate").val()
    				+"&endDate="+$("#endDate").val()+"&applySiteCode = ";
				}else{
		        	top.$.jBox.tip("<spring:message code='签收失败'></spring:message>");
				}
		    });
		}
		
		function cancelDelegate(delegateId){
			$.get('${ctx}/wf/delegate/wfDelegate/deleteForTask' ,{id: delegateId}, function(data) {
				if (data == 'true'){
		        	top.$.jBox.tip("<spring:message code='取消成功'></spring:message>");
		            location = '${ctx}/act/task/todo/?loginName='+$("#initiator").val() + 	    				
	    				"&applicationNo="+$("#applicationNo").val() +'&beginDate='+$("#beginDate").val()
	    				+"&endDate="+$("#endDate").val();
				}else{
		        	top.$.jBox.tip("<spring:message code='取消失败'></spring:message>");
				}
		    });
			 /* $.post(ctx + '/oa/leave/complete/' + taskId, {
			        keys: keys,
			        values: values,
			        types: types
			    }, function(resp) {
					$.unblockUI();
			        if (resp == 'success') {
			            alert('任务完成');
			            location.reload();
			        } else {
			            alert('操作失败!');
			        }
			  }); */
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5><spring:message code="待办任务"/></h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<!-- <a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#">选项1</a>
				</li>
				<li><a href="#">选项2</a>
				</li>
			</ul> -->
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message hideType="1" type="${MType }" content="${message}"/>
	<%-- ${fns:getSqlDictLable('GET_FLOW_TYPE', '', 'leave')}123 --%>
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="act" action="${ctx}/act/task/todo/" method="get" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<!-- <label><spring:message code="流程类型"></spring:message>：&nbsp;</label>
			<form:select path="procDefKey" class="form-control">
				<form:option value="" label="ALL"/>
				<form:options items="${fns:getSqlDictList('GET_FLOW_TYPE',null)}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select> -->
			<label><spring:message code="PR"></spring:message> <spring:message code="号码"></spring:message>:&nbsp;</label>
			<form:input path="applicationNo"  maxlength="30" class="form-control  input-sm" />
			<label><spring:message code="地区"></spring:message>：&nbsp;</label>
			<form:select path="applySiteCode" class="form-control" id="applySiteCode">
				<form:option value="" label="ALL"/>
				<form:options items="${fns:getDictList('Apply_Site_Code')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			
			<label><spring:message code="发起人"></spring:message>：&nbsp;</label>
			<form:input path="initiator"  maxlength="30" class="form-control  input-sm" />
			<label><spring:message code="创建时间"></spring:message>：</label>
			<input id="beginDate"  name="beginDate"  type="text" readonly="readonly" maxlength="20" class="laydate-icon form-control layer-date input-sm"
				value="<fmt:formatDate value="${act.beginDate}" pattern="yyyy-MM-dd"/>"/>
				　--　
			<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="laydate-icon form-control layer-date input-sm"
				value="<fmt:formatDate value="${act.endDate}" pattern="yyyy-MM-dd"/>"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> <spring:message code="刷新"></spring:message></button>
			
		</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> <spring:message code="查询"></spring:message></button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> <spring:message code="重置"></spring:message></button>
		</div>
	</div>
	</div>
	

	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th><spring:message code="PR"></spring:message> <spring:message code="号码"></spring:message></th>
				<th><spring:message code="地区"></spring:message></th>
				<th><spring:message code="流程发起人"></spring:message></th>
				<th><spring:message code="创建时间"></spring:message></th>
				<th><spring:message code="设备"></spring:message></th>
				<th><spring:message code="描述"></spring:message></th>
				<!-- <th><spring:message code="当前环节"></spring:message></th> -->
				<th><spring:message code="当前处理人"></spring:message></th>
				<%-- <th><spring:message code="任务内容"></spring:message></th> --%>
				<!-- <th><spring:message code="流程版本"></spring:message></th> -->		
				
				<th><spring:message code="操作"></spring:message></th> 
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="act">
				<c:set var="task" value="${act.task}" />
				<c:set var="vars" value="${act.vars}" />
				<c:set var="procDef" value="${act.procDef}" /><%--
				<c:set var="procExecUrl" value="${act.procExecUrl}" /> --%>
				<c:set var="status" value="${act.status}" />
				<c:set var="ownerId" value="${fns:getByLoginName(task.assignee).id}" />				
				<c:set var="delegateStr" value="${fns:getSqlField('GET_DELEGATE_NAME', 'lable',ownerId.concat(',').concat(task.id))}" />
				<c:set var="delegateId" value="${fn:split(delegateStr,'*')[0]}" />
				<c:set var="delegater" value="${fn:split(delegateStr,'*')[1]}" />
				<c:set var="delegateTaskId" value="${fn:split(delegateStr,'*')[2]}" />
				<c:set var="applicationNo" value="${fns:abbr(not empty act.vars.map.title ? act.vars.map.title : task.id, 60)}" />
				<tr>
					<td>
						<c:if test="${empty task.assignee}">
							<a href="javascript:claim('${task.id}');" title="签收任务">${fns:abbr(not empty act.vars.map.title ? act.vars.map.title : task.id, 60)}</a>
						</c:if>
						<c:if test="${not empty task.assignee}">
						<a onclick ="top.openTab('${ctx}/act/task/form?taskId=${task.id}&taskName=${fns:urlEncode(fns:urlEncode(task.name))}&taskDefKey=${task.taskDefinitionKey}&procInsId=${task.processInstanceId}&procDefId=${task.processDefinitionId}&status=${status}&assignee=${task.assignee}','${fns:abbr(not empty vars.map.title ? vars.map.title : task.id, 60)}', false)" >${fns:abbr(not empty vars.map.title ? vars.map.title : task.id, 60)}</a>
							<%-- <a href="${ctx}/act/task/form?taskId=${task.id}&taskName=${fns:urlEncode(fns:urlEncode(task.name))}&taskDefKey=${task.taskDefinitionKey}&procInsId=${task.processInstanceId}&procDefId=${task.processDefinitionId}&status=${status}&assignee=${task.assignee}">${fns:abbr(not empty vars.map.title ? vars.map.title : task.id, 60)}</a>--%>
						</c:if>
					</td>
					<td>${vars.map.site}</td>
					<td>
					<c:if test="${empty fns:getByLoginName(act.vars.map.applyUserId)}">
							${act.vars.map.applyUserId}
						</c:if>
						<c:if test="${not empty fns:getByLoginName(act.vars.map.applyUserId)}">
							${fns:getByLoginName(act.vars.map.applyUserId).name}
						</c:if>
						</td>
					<%-- <td>${task.description}</td>  --%>
					<td><fmt:formatDate value="${task.createTime}" timeZone="${timeZone}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>${vars.map.Equipment}</td>
					<td>${vars.map.description}</td>
					<!-- <td>
						<a  href="javascript:openDialog('<spring:message code="跟踪"></spring:message>','${ctx}/act/task/trace/photo/${task.processDefinitionId}/${task.executionId}','1000px', '600px')">${task.name}</a>
					</td> -->
					<td>
						<c:if test="${empty fns:getByLoginName(task.assignee)}">
							${task.assignee}
						</c:if>
						<c:if test="${not empty fns:getByLoginName(task.assignee)}">
							${fns:getByLoginName(task.assignee).name}
						</c:if>
						<c:if test="${not empty delegater}"><br>
							<font color="rgb(197,90,17)">[<spring:message code="被委托人"></spring:message>:
							
							<c:if test="${empty fns:getByLoginName(delegater)}">
							${delegater}
							</c:if>
							<c:if test="${not empty fns:getByLoginName(delegater)}">
								${fns:getByLoginName(delegater).name}
							</c:if>
							]</font>
						</c:if>
					</td>
					<!-- <td><b title='流程版本号'>V: ${procDef.version}</b></td> -->
					
					<td>
						<c:if test="${empty task.assignee}">
							<a href="javascript:claim('${task.id}');"><spring:message code="签收任务"></spring:message></a>
						</c:if>
						<c:if test="${not empty task.assignee}"><%--
							<a href="${ctx}${procExecUrl}/exec/${task.taskDefinitionKey}?procInsId=${task.processInstanceId}&act.taskId=${task.id}">办理</a> --%>
							<%--<a href="${ctx}/act/task/form?taskId=${task.id}&taskName=${fns:urlEncode(fns:urlEncode(task.name))}&taskDefKey=${task.taskDefinitionKey}&procInsId=${task.processInstanceId}&procDefId=${task.processDefinitionId}&status=${status}&assignee=${task.assignee}"><spring:message code="任务办理"></spring:message></a> --%>
							<c:if test="${empty delegateId}">
								<a  href="javascript:openDialog('委托','${ctx}/wf/delegate/wfDelegate/formForTask?procInsId=${task.processInstanceId}&applicationNo=${applicationNo}&taskId=${task.id}&taskName=${task.name}&assignee=${task.assignee}&procDefKey=${act.procDefKey}','1000px', '600px')"><spring:message code="委托"></spring:message></a>
							</c:if>
							<c:if test="${not empty delegateId}">
								<c:if test="${fns:getUser().loginName eq task.assignee and delegateTaskId eq task.id}">
									<a href="javascript:cancelDelegate('${delegateId}');" >
										<spring:message code="取消委托"></spring:message></a>
								</c:if>
							</c:if>
						</c:if>
						<!-- 
						<shiro:hasPermission name="act:process:edit">
							<c:if test="${empty task.executionId}">
								<a href="${ctx}/act/task/deleteTask?taskId=${task.id}&reason=" onclick="return promptx('删除任务','删除原因',this.href);"><spring:message code="删除任务"></spring:message> | </a>
							</c:if>
						</shiro:hasPermission>
						<a  href="javascript:openDialog('跟踪','${ctx}/act/task/trace/photo/${task.processDefinitionId}/${task.executionId}','1000px', '600px')"><spring:message code="跟踪"></spring:message></a> -->
						
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

		<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>