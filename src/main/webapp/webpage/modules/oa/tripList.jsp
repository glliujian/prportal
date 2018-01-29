<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>出差管理</title>
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
		<h5>出差申请列表 </h5>
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
	<form:form id="searchForm" modelAttribute="trip" action="${ctx}/oa/trip/" method="post" class="form-inline">
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
			<shiro:hasPermission name="oa:trip:add">
				<table:addRow url="${ctx}/oa/trip/form" title="出差申请"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="oa:trip:edit">
			    <table:editRow url="${ctx}/oa/trip/form" title="出差申请" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="oa:trip:del">
				<table:delRow url="${ctx}/oa/trip/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="oa:trip:import">
				<table:importExcel url="${ctx}/oa/trip/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="oa:trip:export">
	       		<table:exportExcel url="${ctx}/oa/trip/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column processInstanceId" style="display:none;">流程实例编号</th>
				<th  class="sort-column remarks" style="display:none;">备注信息</th>
				<th  class="sort-column tripBeginDate">出差开始时间</th>
				<th  class="sort-column tripEndDate">出差结束时间</th>
				<th  class="sort-column destAddr">出差地点</th>
				<th  class="sort-column purposes">出差原因</th>
				<th  class="sort-column arrangements">出差安排</th>
				<th  class="sort-column other" style="display:none;">其他</th>
				<th  class="sort-column departManager" style="display:none;">部门审批领导</th>
				<th  class="sort-column departManagerOpinion" style="display:none;">部门审批领导意见</th>
				<th  class="sort-column generalManager" style="display:none;">总经理</th>
				<th  class="sort-column generalManagerOpinion" style="display:none;">总经理意见</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="trip">
			<tr>
				<td> <input type="checkbox" id="${trip.id}" class="i-checks"></td>
				<%-- <td><a  href="#" onclick="openDialogView('查看出差申请', '${ctx}/oa/trip/form?id=${trip.id}','800px', '500px')">
					${trip.processInstanceId}
				</a></td>
				<td>
					${trip.remarks}
				</td> --%>
				<td>
					<fmt:formatDate value="${trip.tripBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${trip.tripEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${trip.destAddr}
				</td>
				<td>
					${trip.purposes}
				</td>
				<td>
					${trip.arrangements}
				</td>
				<td>
					${trip.other}
				</td>
				<td>
					${trip.departManager}
				</td>
				<td>
					${trip.departManagerOpinion}
				</td>
				<td>
					${trip.generalManager}
				</td>
				<td>
					${trip.generalManagerOpinion}
				</td>
				<td>
					<shiro:hasPermission name="oa:trip:view">
						<a href="#" onclick="openDialogView('查看出差申请', '${ctx}/oa/trip/form?id=${trip.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="oa:trip:edit">
    					<a href="#" onclick="openDialog('修改出差申请', '${ctx}/oa/trip/form?id=${trip.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="oa:trip:del">
						<a href="${ctx}/oa/trip/delete?id=${trip.id}" onclick="return confirmx('确认要删除该出差申请吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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