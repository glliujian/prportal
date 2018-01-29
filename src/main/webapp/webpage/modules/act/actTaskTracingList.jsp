<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title><spring:message code="报表-申请表单"></spring:message></title>
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

		});
		
		
		function exportToExcel(){
			
			ExportToExcelFormat(['contentTable'])
		}
		function page(n,s){
        	location = '${ctx}/oa/pr/tracing/?pageNo='+n+'&pageSize='+s + '&loginName='+$("#initiator").val() + 
        				'&assignee=' + $("#assigneeName").val()+'&applySiteCode=' + $("#applySiteCode").val()+
        				"&applicationNo="+$("#applicationNo").val() + '&status='+$("#status").val()+'&beginDate='+$("#beginDate").val()
        				+"&endDate="+$("#endDate").val();
        }
		function redirectToForm()
		{
			location = '${ctx}/act/task/form?procDefId=pr:19:d17961db888b46eead8b24784efd674d';
		}
		
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5><spring:message code="我的进度"/></h5>
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
	<sys:message type="${MType}" hideType="0" content="${message}"/>
	<%-- ${fns:getSqlDictLable('GET_FLOW_TYPE', '', 'leave')}123 --%>
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="pr" action="${ctx}/oa/pr/tracing/" method="get" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<label><spring:message code="地区"></spring:message>：&nbsp;</label>
			<form:select path="applySiteCode" class="form-control" id="applySiteCode">
				<form:option value="" label="ALL"/>
				<form:options items="${fns:getDictList('Apply_Site_Code')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>

			<label><spring:message code="当前处理人"></spring:message>:&nbsp;</label>
			<form:input path="assignee" id='assigneeName'  maxlength="30" class="form-control  input-sm" />
			
			<label><spring:message code="PR"></spring:message> <spring:message code="号码"></spring:message>:&nbsp;</label>
			<form:input path="applicationNo" id='applicationNo'  maxlength="30" class="form-control  input-sm" />
			
			<label><spring:message code="状态"></spring:message>:&nbsp;</label>
			<form:select path="status" class="form-control" id="status">
				<form:option value="" label="ALL"/>
				<form:options items="${fns:getDictList('PR_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			
			<br/>
			<br/>
			<label><spring:message code="创建时间"></spring:message>：</label>
			<input id="beginDate"  name="beginDate"  type="text" readonly="readonly" maxlength="20" class="laydate-icon form-control layer-date input-sm"
				value="<fmt:formatDate value="${pr.beginDate}" pattern="yyyy-MM-dd" timeZone="${timeZone}" /> "/>
				——
			<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="laydate-icon form-control layer-date input-sm"
				value="<fmt:formatDate value="${pr.endDate}" pattern="yyyy-MM-dd" timeZone="${timeZone} "/>"/> 
		
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
			<button class="btn btn-white btn-sm " onclick="redirectToForm()" ><i class="fa fa-plus"></i><spring:message code="填写申请"></spring:message></button>			
		</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> <spring:message code="查询"></spring:message></button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> <spring:message code="重置"></spring:message></button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="exportToExcel()" ><i class="fa fa-file-excel-o"></i> <spring:message code="导出"></spring:message></button>
			
		 
		</div>
	</div>
	</div>
	

	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th class="sort-column a.application_no"><spring:message code="PR"></spring:message> <spring:message code="号码"></spring:message></th>

				<th class="sort-column a.create_date"><spring:message code="创建时间"></spring:message></th>
				<th class="sort-column a.equipment"><spring:message code="设备名称"></spring:message></th>
				<th><spring:message code="描述"></spring:message></th>
				
				<th class="sort-column b.assignee_"><spring:message code="当前处理人"></spring:message></th> 
				<th class="sort-column Status"><spring:message code="状态"></spring:message></th>

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
					
					<td>${pr.assignee} </td>
					<td><spring:message code="${pr.status}"></spring:message> </td>
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