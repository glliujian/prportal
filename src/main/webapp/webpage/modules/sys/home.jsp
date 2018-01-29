<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>PR审批单管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/common/inspinia.js?v=3.2.0"></script>
	<script src="${ctxStatic}/common/contabs.js"></script> 
	<script type="text/javascript">
		$(document).ready(function() {
			
			
		});
	</script>
	<link href="${ctxStatic}/common/css/home.css" type="text/css" rel="stylesheet" />
	
</head>
<body>
	<div class="contentBox">
		<div class="wrap">
		<c:if test="${flag1==true}">
			<!-- <a class='J_menuItem' href="${ctx}/act/task/todo/" ><img src="${ctxStatic}/images/MyTask.png" /><h2>My Approval</h2></a>-->
			
			<a class='J_menuItem' onclick="top.openTab('${ctx}/act/task/todo/','<spring:message code='我的任务'></spring:message>', false)" ><img src="${ctxStatic}/images/MyTask.png" /><h2><spring:message code='我的任务'></spring:message></h2></a>
		</c:if>	
		<c:if test="${flag1==false}">
			<a class='J_menuItem' ><img src="${ctxStatic}/images/MyTaskDisable.png" /><h2><spring:message code='我的任务'></spring:message></h2></a>
		
		</c:if>	
		
		<c:if test="${flag2==true}">
			<!-- <a class='J_menuItem' href="${ctx}/act/task/tracing/"  ><img src="${ctxStatic}/images/myApplication.png" /><h2><spring:message code='我的申请'></spring:message></h2></a> -->
			<a class='J_menuItem' onclick="top.openTab('${ctx}/oa/pr/tracing/','<spring:message code='我的申请'></spring:message>', false)" ><img src="${ctxStatic}/images/myApplication.png" /><h2><spring:message code='我的申请'></spring:message></h2></a>
		</c:if>	
		<c:if test="${flag2==false}">
			<!-- <a class='J_menuItem' ><img src="${ctxStatic}/images/myApplicationDisable.png" /><h2><spring:message code='我的申请'></spring:message></h2></a> -->
		 	<a class='J_menuItem' ><img src="${ctxStatic}/images/myApplicationDisable.png" /><h2><spring:message code='我的申请'></spring:message></h2></a>
		</c:if>		
		<c:if test="${flag3==true}">	
			<!-- <a class='J_menuItem' href="${ctx}/act/task/overview/" ><img src="${ctxStatic}/images/Report.png" /><h2>Enquiry & Report</h2></a> -->
			<a class='J_menuItem' onclick="top.openTab('${ctx}/oa/pr/overview/','<spring:message code='查询与报表'></spring:message>', false)" ><img src="${ctxStatic}/images/Report.png" /><h2><spring:message code='查询与报表'></spring:message></h2></a>
		</c:if>			
		<c:if test="${flag3==false}">	
			<a class='J_menuItem'  ><img src="${ctxStatic}/images/ReportDisable.png" /><h2><spring:message code='查询与报表'></spring:message></h2></a>
		</c:if>	
		
		<c:if test="${flag4==true}">	
			<!-- <a class='J_menuItem' href="${ctx}/act/task/form?procDefId=pr:19:d17961db888b46eead8b24784efd674d" ><img src="${ctxStatic}/images/CreatePR.png" /><h2>Create PR</h2></a> -->
			<a class='J_menuItem' onclick="top.openTab('${ctx}/act/task/form?procDefId=pr:19:d17961db888b46eead8b24784efd674d','<spring:message code='填写申请'></spring:message>', false)" ><img src="${ctxStatic}/images/CreatePR.png" /><h2><spring:message code='填写申请'></spring:message></h2></a>
		 </c:if>
		 <c:if test="${flag4==false}">	
			<a class='J_menuItem' ><img src="${ctxStatic}/images/CreatePRDisable.png" /><h2><spring:message code='填写申请'></spring:message></h2></a>
		 </c:if>	
		 <!-- ${menu} --> 
		</div>
	</div>
	<div class="table">
		
		<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
			<thead>
				<tr>
				<td colspan=8 style="background-color: rgb(47, 64, 80) !important; color: white;"><font size="4pt"><spring:message code="我的任务"></spring:message></font></td>
				</tr>
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
						<td><c:if test="${empty fns:getByLoginName(act.vars.map.applyUserId)}">
							${act.vars.map.applyUserId}
						</c:if>
						<c:if test="${not empty fns:getByLoginName(act.vars.map.applyUserId)}">
							${fns:getByLoginName(act.vars.map.applyUserId).name}
						</c:if></td>
						
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
									 <a  href="javascript:openDialog('<spring:message code='委托'></spring:message>','${ctx}/wf/delegate/wfDelegate/formForTask?procInsId=${task.processInstanceId}&applicationNo=${applicationNo}&taskId=${task.id}&taskName=${task.name}&assignee=${task.assignee}&procDefKey=${act.procDefKey}','1000px', '600px')"><spring:message code="委托"></spring:message></a>
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
			<tfoot>
				<tr>
					<td colspan=8 class="more" ><a onclick="top.openTab('${ctx}/act/task/todo/','<spring:message code='我的任务'></spring:message>', false)"><spring:message code='更多'></spring:message></a></td>
					
				</tr>
			</tfoot>
		</table>
	</div>
	<div class="table">
	<!-- 表格 -->
	<table id="contentTableTracing"  class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<td colspan=8 style="background-color: rgb(47, 64, 80) !important; color: white;"><font size="4pt"><spring:message code="我的进度"></spring:message></font></td>
			</tr>
			<tr>
				<th><spring:message code="PR"></spring:message> <spring:message code="号码"></spring:message></th>

				<th><spring:message code="创建时间"></spring:message></th>
				<th><spring:message code="设备名称"></spring:message></th>
				<th><spring:message code="描述"></spring:message></th>
				
				<th><spring:message code="当前处理人"></spring:message></th> 
				<th><spring:message code="状态"></spring:message></th>

			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="pr">
				<c:set var="task" value="${pr.task}" />
				<c:set var="hisTask" value="${pr.historicProcessInstance}" />

				<tr>
					<td>
						<c:if test="${not empty task.id}">
						<a href="${ctx}/act/task/form?taskId=${task.id}&taskName=${fns:urlEncode(fns:urlEncode(task.name))}&taskDefKey=${task.taskDefinitionKey}&procInsId=${task.processInstanceId}&procDefId=${task.processDefinitionId}&status=finish&assignee=${task.assignee}">${pr.applicationNo}</a>
						</c:if>
						<c:if test="${not empty hisTask.id}">
						<a href="${ctx}/act/task/form?taskId=${hisTask.id}&taskName=${fns:urlEncode(fns:urlEncode(hisTask.name))}&procInsId=${hisTask.processInstanceId}&procDefId=${hisTask.processDefinitionId}&status=finish">${pr.applicationNo}</a>
						</c:if>
						<c:if test="${empty hisTask.id and empty task.id}">
						${pr.applicationNo}
						</c:if>
					</td>

					<td><fmt:formatDate value="${pr.createDate}" type="both" timeZone="${timeZone}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>${pr.equipment}</td>
					<td>${pr.equipmentDesciption}</td>
					
					<td>
					<c:if test="${empty fns:getByLoginName(pr.assignee)}">
								${pr.assignee}
							</c:if>
							<c:if test="${not empty fns:getByLoginName(pr.assignee)}">
								${fns:getByLoginName(pr.assignee).name}
							</c:if>
					</td>
					
					<td><spring:message code="${pr.status}"></spring:message></td>
				</tr>
			</c:forEach>
		</tbody>
		<tfoot>
				<tr>
					<td colspan=8 class="more" ><a onclick="top.openTab('${ctx}/oa/pr/tracing/','<spring:message code='我的申请'></spring:message>', false)" ><spring:message code='更多'></spring:message></a></td>
					
				</tr>
			</tfoot>	
	</table>
	</div>
</body>
</html>