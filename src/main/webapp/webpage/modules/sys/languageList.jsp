<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>语言配置管理</title>
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
		<h5><spring:message code="语言配置列表"></spring:message> </h5>
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
	<form:form id="searchForm" modelAttribute="language" action="${ctx}/sys/language/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span><spring:message code="标签"></spring:message><spring:message code="："></spring:message></span>
				<form:input path="name" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span><spring:message code="内容展现"></spring:message><spring:message code="："></spring:message></span>
				<form:input path="text" htmlEscape="false" maxlength="255"  class=" form-control input-sm"/>
			<span><spring:message code="语言"></spring:message><spring:message code="："></spring:message></span>
				<form:input path="language" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="sys:language:add">
				<table:addRow url="${ctx}/sys/language/form" title="语言配置"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:language:edit">
			    <table:editRow url="${ctx}/sys/language/form" title="语言配置" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:language:del">
				<table:delRow url="${ctx}/sys/language/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:language:import">
				<table:importExcel url="${ctx}/sys/language/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:language:export">
	       		<table:exportExcel url="${ctx}/sys/language/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column name"><spring:message code="标签"></spring:message></th>
				<th  class="sort-column text"><spring:message code="内容展现"></spring:message></th>
				<th  class="sort-column language"><spring:message code="语言"></spring:message></th>
				<th><spring:message code="操作"></spring:message></th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="language">
			<tr>
				<td> <input type="checkbox" id="${language.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看语言配置', '${ctx}/sys/language/form?id=${language.id}','800px', '500px')">
					${language.name}
				</a></td>
				<td>
					${language.text}
				</td>
				<td>
					${language.language}
				</td>
				<td>
					<shiro:hasPermission name="sys:language:view">
						<a href="#" onclick="openDialogView('<spring:message code="查看语言配置"></spring:message>', '${ctx}/sys/language/form?id=${language.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> <spring:message code="查看"></spring:message></a>
					</shiro:hasPermission>
					<shiro:hasPermission name="sys:language:edit">
    					<a href="#" onclick="openDialog('<spring:message code="修改语言配置"></spring:message>', '${ctx}/sys/language/form?id=${language.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> <spring:message code="修改"></spring:message></a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="sys:language:del">
						<a href="${ctx}/sys/language/delete?id=${language.id}" onclick="return confirmx('<spring:message code="确认要删除该语言配置吗？"></spring:message>', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> <spring:message code="删除"></spring:message></a>
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