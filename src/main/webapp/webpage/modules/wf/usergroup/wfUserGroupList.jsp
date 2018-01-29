<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工作流用户组管理</title>
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
		<h5><spring:message code="工作流用户组列表"></spring:message> </h5>
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
	<form:form id="searchForm" modelAttribute="wfUserGroup" action="${ctx}/wf/usergroup/wfUserGroup/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span><spring:message code="公司"></spring:message>：</span>
				<sys:treeselect id="company" name="company.id" value="${wfUserGroup.company.id}" labelName="company.name" labelValue="${wfUserGroup.company.name}"
					title="公司" url="/sys/office/treeData?type=1" cssClass="form-control input-sm" allowClear="true" notAllowSelectParent="true"/>
			<span><spring:message code="部门"></spring:message>：</span>
				<sys:treeselect id="department" name="department.id" value="${wfUserGroup.department.id}" labelName="department.name" labelValue="${wfUserGroup.department.name}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="form-control input-sm" allowClear="true" notAllowSelectParent="true"/>
			<span><spring:message code="流程标识"></spring:message>：</span>
				<form:select path="procDefKey"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getSqlDictList('GET_FLOW_TYPE','')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<span><spring:message code="组标识"></spring:message>：</span>
				<form:input path="userGroup" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="wf:usergroup:wfUserGroup:add">
				<table:addRow url="${ctx}/wf/usergroup/wfUserGroup/form" title="工作流用户组"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="wf:usergroup:wfUserGroup:edit">
			    <table:editRow url="${ctx}/wf/usergroup/wfUserGroup/form" title="工作流用户组" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="wf:usergroup:wfUserGroup:del">
				<table:delRow url="${ctx}/wf/usergroup/wfUserGroup/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="wf:usergroup:wfUserGroup:import">
				<table:importExcel url="${ctx}/wf/usergroup/wfUserGroup/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="wf:usergroup:wfUserGroup:export">
	       		<table:exportExcel url="${ctx}/wf/usergroup/wfUserGroup/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
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
				<th> <input type="checkbox" class="i-checks"></th>				
				<th  class="sort-column company.name"><spring:message code="公司"></spring:message></th>
				<th  class="sort-column department.name"><spring:message code="部门"></spring:message></th>
				<th  class="sort-column procDefKey"><spring:message code="流程标识"></spring:message></th>
				<th  class="sort-column userGroup"><spring:message code="组标识"></spring:message></th>
				<th  class="sort-column user.name"><spring:message code="用户"></spring:message></th>				
				<th  class="sort-column remarks"><spring:message code="备注"></spring:message></th>

				<th><spring:message code="操作"></spring:message></th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wfUserGroup">
			<tr>
				<td> <input type="checkbox" id="${wfUserGroup.id}" class="i-checks"></td>
				
				<td>
					${wfUserGroup.company.name}
				</td>
				<td>
					${wfUserGroup.department.name}
				</td>
				<td>
					${fns:getSqlDictLabel('GET_FLOW_TYPE', '', wfUserGroup.procDefKey)}
				</td>
				<td>
					${wfUserGroup.userGroup}
				</td>
				<td>
					${wfUserGroup.user.name}
				</td>				
				<td>
					${wfUserGroup.remarks}
				</td>
				<td>
					<shiro:hasPermission name="wf:usergroup:wfUserGroup:view">
						<a href="#" onclick="openDialogView('<spring:message code='查看工作流用户组'></spring:message>', '${ctx}/wf/usergroup/wfUserGroup/form?id=${wfUserGroup.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> <spring:message code="查看"></spring:message></a>
					</shiro:hasPermission>
					<shiro:hasPermission name="wf:usergroup:wfUserGroup:edit">
    					<a href="#" onclick="openDialog('<spring:message code='修改工作流用户组'></spring:message>', '${ctx}/wf/usergroup/wfUserGroup/form?id=${wfUserGroup.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> <spring:message code="修改"></spring:message></a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="wf:usergroup:wfUserGroup:del">
						<a href="${ctx}/wf/usergroup/wfUserGroup/delete?id=${wfUserGroup.id}" onclick="return confirmx('<spring:message code='确认要删除该工作流用户组吗？'></spring:message>', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> <spring:message code="删除"></spring:message></a>
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