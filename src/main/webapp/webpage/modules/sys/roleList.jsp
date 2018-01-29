]<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>角色管理</title>
	<meta name="decorator" content="default"/>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
			<h5><spring:message code="角色列表"></spring:message> </h5>
			<div class="ibox-tools">
				<a class="collapse-link">
					<i class="fa fa-chevron-up"></i>
				</a>
				<a class="dropdown-toggle" data-toggle="dropdown" href="form_basic.html#">
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
	
		<!-- 查询条件 -->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="role" action="${ctx}/sys/role/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span><spring:message code="角色名称"></spring:message><spring:message code="："></spring:message></span>
				<form:input path="name" value="${role.name}"  htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
				
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
		<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="sys:role:add">
				<table:addRow url="${ctx}/sys/role/form" title="角色"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:role:edit">
			    <table:editRow url="${ctx}/sys/role/form" id="contentTable"  title="角色"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:role:del">
				<table:delRow url="${ctx}/sys/role/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> <spring:message code="刷新"></spring:message></button>
		
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> <spring:message code="查询"></spring:message></button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> <spring:message code="重置"></spring:message></button>
		</div>
	</div>
	</div>
	
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th><input type="checkbox" class="i-checks"></th>
				<th><spring:message code="角色名称"></spring:message></th>
				<th><spring:message code="英文名称"></spring:message></th>
				<th><spring:message code="归属机构"></spring:message></th>
				<th><spring:message code="数据范围"></spring:message></th>
				<shiro:hasPermission name="sys:role:edit"><th><spring:message code="操作"></spring:message></th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="role">
				<tr>
					<td> <input type="checkbox" id="${role.id}" class="i-checks"></td>
					<td><a  href="#" onclick="openDialogView('<spring:message code='查看角色'></spring:message>', '${ctx}/sys/role/form?id=${role.id}','800px', '500px')">${role.name}</a></td>
					<td><a  href="#" onclick="openDialogView('<spring:message code='查看角色'></spring:message>', '${ctx}/sys/role/form?id=${role.id}','800px', '500px')">${role.enname}</a></td>
					<td>${role.office.name}</td>
					<td>${fns:getDictLabel(role.dataScope, 'sys_data_scope', '无')}</td>
					<td>
						<shiro:hasPermission name="sys:role:view">
						<a href="#" onclick="openDialogView('<spring:message code='查看角色'></spring:message>', '${ctx}/sys/role/form?id=${role.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> <spring:message code="查看"></spring:message></a>
						</shiro:hasPermission>
						<shiro:hasPermission name="sys:role:edit"> 
						<c:if test="${(role.sysData eq fns:getDictValue('是', 'yes_no', '1') && fns:getUser().admin)||!(role.sysData eq fns:getDictValue('是', 'yes_no', '1'))}">
							<a href="#" onclick="openDialog('<spring:message code='修改角色'></spring:message>', '${ctx}/sys/role/form?id=${role.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> <spring:message code="修改"></spring:message></a>
						</c:if>
						</shiro:hasPermission>
						<shiro:hasPermission name="sys:role:del"> 
						<a href="${ctx}/sys/role/delete?id=${role.id}" onclick="return confirmx('<spring:message code="确认要删除该角色吗？"></spring:message>', this.href)" class="btn  btn-danger btn-xs"><i class="fa fa-trash"></i> <spring:message code="删除"></spring:message></a>
						</shiro:hasPermission>
						<shiro:hasPermission name="sys:role:assign"> 
						<a href="#" onclick="openDialog('<spring:message code='权限设置'></spring:message>', '${ctx}/sys/role/auth?id=${role.id}','350px', '700px')" class="btn btn-primary btn-xs" ><i class="fa fa-edit"></i> <spring:message code="权限设置"></spring:message></a> 
						</shiro:hasPermission>
						<shiro:hasPermission name="sys:role:assign"> 
						<a href="#" onclick="openDialogView('<spring:message code='分配用户'></spring:message>', '${ctx}/sys/role/assign?id=${role.id}','800px', '600px')"  class="btn  btn-warning btn-xs" ><i class="glyphicon glyphicon-plus"></i> <spring:message code="分配用户"></spring:message></a>
						</shiro:hasPermission>
						</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
	</div>
	</div>
</body>
</html>