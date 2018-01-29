<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工作流處理人設置管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5><spring:message code="工作流处理人设置列表"></spring:message> </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#">选项1</a>
				</li>
				<li><a href="#">选项2</a>
				</li>
			</ul>
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="wfAssignee" action="${ctx}/wf/assignee/wfAssignee/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span><spring:message code="处理人流程变量"></spring:message>：</span>
				<form:input path="assigneeVar" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span><spring:message code="流程类型"></spring:message>：</span>
				<form:select path="flowType"  class="form-control m-b">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getSqlDictList('GET_FLOW_TYPE',null)}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="wf:assignee:wfAssignee:add">
				<table:addRow url="${ctx}/wf/assignee/wfAssignee/form" title="工作流處理人設置"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="wf:assignee:wfAssignee:edit">
			    <table:editRow url="${ctx}/wf/assignee/wfAssignee/form" title="工作流處理人設置" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="wf:assignee:wfAssignee:del">
				<table:delRow url="${ctx}/wf/assignee/wfAssignee/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="wf:assignee:wfAssignee:import">
				<table:importExcel url="${ctx}/wf/assignee/wfAssignee/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="wf:assignee:wfAssignee:export">
	       		<table:exportExcel url="${ctx}/wf/assignee/wfAssignee/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> <spring:message code="刷新"></spring:message></button>
		
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> <spring:message code="查询"></spring:message></button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> <spring:message code="重置"></spring:message>
</button>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				
				<th  class="sort-column assigneeVar"><spring:message code="处理人流程变量"></spring:message></th>
				<th  class="sort-column flowType"><spring:message code="流程类型"></spring:message></th>
				<th  class="sort-column sqlStr"><spring:message code="取值sql"></spring:message></th>
				<th><spring:message code="操作"></spring:message></th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wfAssignee">
			<tr>
				<td> <input type="checkbox" id="${wfAssignee.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('<spring:message code="查看工作流處理人設置"></spring:message>', '${ctx}/wf/assignee/wfAssignee/form?id=${wfAssignee.id}','800px', '500px')">
					${wfAssignee.assigneeVar}
				</a></td>				
				<td>
					${fns:getSqlDictLabel('GET_FLOW_TYPE', '', wfAssignee.flowType)}
				</td>
				<td>
					${wfAssignee.sqlStr}
				</td>
				<td>
					<shiro:hasPermission name="wf:assignee:wfAssignee:view">
						<a href="#" onclick="openDialogView('<spring:message code="查看工作流處理人設置"></spring:message>', '${ctx}/wf/assignee/wfAssignee/form?id=${wfAssignee.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> <spring:message code="查看"></spring:message></a>
					</shiro:hasPermission>
					<shiro:hasPermission name="wf:assignee:wfAssignee:edit">
    					<a href="#" onclick="openDialog('<spring:message code="修改工作流處理人設置"></spring:message>', '${ctx}/wf/assignee/wfAssignee/form?id=${wfAssignee.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> <spring:message code="修改"></spring:message></a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="wf:assignee:wfAssignee:del">
						<a href="${ctx}/wf/assignee/wfAssignee/delete?id=${wfAssignee.id}" onclick="return confirmx('<spring:message code="确认要删除该工作流處理人設置吗？"></spring:message>', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> <spring:message code="删除"></spring:message></a>
					</shiro:hasPermission>
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