<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title><spring:message code="菜单管理"></spring:message></title>
	<meta name="decorator" content="default"/>
	<%@include file="/webpage/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 1,column:1}).show();
		});
    	function updateSort() {
			loading("<spring:message code="<spring:message code='正在提交，请稍等...'></spring:message>"></spring:message>");
	    	$("#listForm").attr("action", "${ctx}/sys/menu/updateSort");
	    	$("#listForm").submit();
    	}
		function refresh(){//刷新
			
			window.location="${ctx}/sys/menu/";
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
			<h5><spring:message code="菜单列表"></spring:message></h5>
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
	
		
			<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="sys:menu:add">
				<table:addRow url="${ctx}/sys/menu/form" title="菜单"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:menu:edit">
			    <table:editRow url="${ctx}/sys/menu/form" id="treeTable"  title="菜单"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:menu:del">
				<table:delRow url="${ctx}/sys/menu/deleteAll" id="treeTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:menu:updateSort">
				<button id="btnSubmit" class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="updateSort()" title="保存排序"><i class="fa fa-save"></i> <spring:message code="保存排序"></spring:message></button>
			</shiro:hasPermission>
				<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> <spring:message code="刷新"></spring:message></button>
		
		</div>
	</div>
	</div>
	<form id="listForm" method="post">
		<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
			<thead><tr><th><input type="checkbox" class="i-checks"></th><th><spring:message code="名称"></spring:message></th><th><spring:message code="链接"></spring:message></th><th style="text-align:center;"><spring:message code="排序"></spring:message></th><th><spring:message code="可见"></spring:message></th><th><spring:message code="权限标识"></spring:message></th><shiro:hasPermission name="sys:menu:edit"><th><spring:message code="操作"></spring:message></th></shiro:hasPermission></tr></thead>
			<tbody><c:forEach items="${list}" var="menu">
				<tr id="${menu.id}" pId="${menu.parent.id ne '1'?menu.parent.id:'0'}">
					<td> <input type="checkbox" id="${menu.id}" class="i-checks"></td>
					<td nowrap><i class="icon-${not empty menu.icon?menu.icon:' hide'}"></i><a  href="#" onclick="openDialogView('查看菜单', '${ctx}/sys/menu/form?id=${menu.id}','800px', '500px')">${menu.name}</a></td>
					<td title="${menu.href}">${fns:abbr(menu.href,30)}</td>
					<td style="text-align:center;">
						<shiro:hasPermission name="sys:menu:updateSort">
							<input type="hidden" name="ids" value="${menu.id}"/>
							<input name="sorts" type="text" value="${menu.sort}" class="form-control" style="width:100px;margin:0;padding:0;text-align:center;">
						</shiro:hasPermission><shiro:lacksPermission name="sys:menu:updateSort">
							${menu.sort}
						</shiro:lacksPermission>
					</td>
					<td><spring:message code="${menu.isShow eq '1'?'显示':'隐藏'}"></spring:message></td>
					<td title="${menu.permission}">${fns:abbr(menu.permission,30)}</td>
					<td nowrap>
						<shiro:hasPermission name="sys:menu:view">
							<a href="#" onclick="openDialogView('<spring:message code='查看菜单'></spring:message>', '${ctx}/sys/menu/form?id=${menu.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> <spring:message code="查看"></spring:message></a>
						</shiro:hasPermission>
						<shiro:hasPermission name="sys:menu:edit">
	    					<a href="#" onclick="openDialog('<spring:message code='修改菜单'></spring:message>', '${ctx}/sys/menu/form?id=${menu.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> <spring:message code="修改"></spring:message></a>
	    				</shiro:hasPermission>
	    				<shiro:hasPermission name="sys:menu:del">
							<a href="${ctx}/sys/menu/delete?id=${menu.id}" onclick="return confirmx('<spring:message code='要删除该菜单及所有子菜单项吗？'></spring:message>', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> <spring:message code="删除"></spring:message></a>
						</shiro:hasPermission>
						<shiro:hasPermission name="sys:menu:add">
							<a href="#" onclick="openDialog('<spring:message code='添加下级菜单'></spring:message>', '${ctx}/sys/menu/form?parent.id=${menu.id}','800px', '500px')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i> <spring:message code="添加下级菜单"></spring:message></a>
						</shiro:hasPermission>
					</td>
				</tr>
			</c:forEach></tbody>
		</table>
	 </form>
	 </div>
	</div>
	</div>
</body>
</html>