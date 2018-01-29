<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>国际化语言设置管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		
		function deleteLanguage(id){
			var deleteUrl = "${ctx}/sys/sysSimpleLanguage/delete?id=" + id + "&pageSize=" + $("#pageSize").val();
			return confirmx('<spring:message code="确认要删除吗？"/>', deleteUrl);
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5><spring:message code="国际化配置"/></h5>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="sysSimpleLanguage" action="${ctx}/sys/sysSimpleLanguage/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span><spring:message code="语言内容"/>：</span>
				<form:input path="cn" htmlEscape="false" maxlength="255"  class=" form-control input-sm"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="sys:sysSimpleLanguage:add">
				<table:addRow url="${ctx}/sys/sysSimpleLanguage/form" title="国际化语言设置"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> <spring:message code="刷新"/></button>
		
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> <spring:message code="查询"/></button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> <spring:message code="重置"/></button>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column code"><spring:message code="标识"/></th>
				<th  class="sort-column cn"><spring:message code="简体中文"/></th>
				<th  class="sort-column hk"><spring:message code="繁体中文"/></th>
				<th  class="sort-column en"><spring:message code="英文"/></th>
				<th  class="sort-column vn"><spring:message code="越南文"/></th>
				<th  class="sort-column cam"><spring:message code="柬埔寨文"/></th>
				<th><spring:message code="操作"/></th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sysSimpleLanguage">
			<tr>
				<td> <input type="checkbox" id="${sysSimpleLanguage.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('<spring:message code="查看"/>', '${ctx}/sys/sysSimpleLanguage/form?id=${sysSimpleLanguage.id}','800px', '500px')">
					${sysSimpleLanguage.code}
				</a></td>
				<td>
					${sysSimpleLanguage.cn}
				</td>
				<td>
					${sysSimpleLanguage.hk}
				</td>
				<td>
					${sysSimpleLanguage.en}
				</td>
				<td>
					${sysSimpleLanguage.vn}
				</td>
				<td>
					${sysSimpleLanguage.cam}
				</td>
				<td>
					<shiro:hasPermission name="sys:sysSimpleLanguage:view">
						<a href="#" onclick="openDialogView('<spring:message code="查看"/>', '${ctx}/sys/sysSimpleLanguage/form?id=${sysSimpleLanguage.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> <spring:message code="查看"/></a>
					</shiro:hasPermission>
					<shiro:hasPermission name="sys:sysSimpleLanguage:edit">
    					<a href="#" onclick="openDialog('<spring:message code="修改"/>', '${ctx}/sys/sysSimpleLanguage/form?id=${sysSimpleLanguage.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> <spring:message code="修改"/></a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="sys:sysSimpleLanguage:del">
						<a href="#" onclick="deleteLanguage('${sysSimpleLanguage.id}')" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> <spring:message code="删除"/></a>
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