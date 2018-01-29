<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工作任务管理</title>
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
		<h5>工作任务列表 </h5>
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
	<form:form id="searchForm" modelAttribute="qcTask" action="${ctx}/qc/task/qcTask/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="qc:task:qcTask:add">
				<table:addRow url="${ctx}/qc/task/qcTask/form" title="工作任务"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="qc:task:qcTask:edit">
			    <table:editRow url="${ctx}/qc/task/qcTask/form" title="工作任务" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="qc:task:qcTask:del">
				<table:delRow url="${ctx}/qc/task/qcTask/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="qc:task:qcTask:import">
				<table:importExcel url="${ctx}/qc/task/qcTask/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="qc:task:qcTask:export">
	       		<table:exportExcel url="${ctx}/qc/task/qcTask/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column createBy.id">创建者</th>
				<th  class="sort-column remarks">备注信息</th>
				<th  class="sort-column taskNo">任务号</th>
				<th  class="sort-column workingNo">客款号</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="qcTask">
			<tr>
				<td> <input type="checkbox" id="${qcTask.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看工作任务', '${ctx}/qc/task/qcTask/form?id=${qcTask.id}','800px', '500px')">
					${qcTask.createBy.id}
				</a></td>
				<td>
					${qcTask.remarks}
				</td>
				<td>
					${qcTask.taskNo}
				</td>
				<td>
					${fns:getDictLabel(qcTask.workingNo, 'working_no', '')}
				</td>
				<td>
					<shiro:hasPermission name="qc:task:qcTask:view">
						<a href="#" onclick="openDialogView('查看工作任务', '${ctx}/qc/task/qcTask/form?id=${qcTask.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> <spring:message code="查看"></spring:message></a>
					</shiro:hasPermission>
					<shiro:hasPermission name="qc:task:qcTask:edit">
    					<a href="#" onclick="openDialog('修改工作任务', '${ctx}/qc/task/qcTask/form?id=${qcTask.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> <spring:message code="修改"></spring:message></a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="qc:task:qcTask:del">
						<a href="${ctx}/qc/task/qcTask/delete?id=${qcTask.id}" onclick="return confirmx('<spring:message code="确认要删除该工作任务吗？"></spring:message>', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> <spring:message code="删除"></spring:message></a>
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