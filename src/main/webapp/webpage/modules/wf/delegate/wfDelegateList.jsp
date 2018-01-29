<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>工作流委托設置管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
	});
</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>
					<spring:message code="工作流委托設置列表"></spring:message>
				</h5>
				<div class="ibox-tools">
					<a class="collapse-link"> <i class="fa fa-chevron-up"></i>
					</a> <a class="dropdown-toggle" data-toggle="dropdown" href="#"> <i
						class="fa fa-wrench"></i>
					</a>
					<ul class="dropdown-menu dropdown-user">
						<li><a href="#">选项1</a></li>
						<li><a href="#">选项2</a></li>
					</ul>
					<a class="close-link"> <i class="fa fa-times"></i>
					</a>
				</div>
			</div>

			<div class="ibox-content">
				<sys:message hideType="0" type="${MType }" content="${message}" />

				<!--查询条件-->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="wfDelegate"
							action="${ctx}/wf/delegate/wfDelegate/" method="post"
							class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden"
								value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden"
								value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy"
								value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
								<span><spring:message code="委托人"></spring:message>
									<spring:message code="："></spring:message></span>
								<sys:treeselect id="owner" name="owner.id"
									value="${wfDelegate.owner.id}" labelName="owner.name"
									labelValue="${wfDelegate.owner.name}" title="用户"
									url="/sys/office/treeData?type=3"
									cssClass="form-control input-sm" allowClear="true"
									notAllowSelectParent="true" />
								<span><spring:message code="代理人"></spring:message>
									<spring:message code="："></spring:message> </span>
								<sys:treeselect id="delegate" name="delegate.id"
									value="${wfDelegate.delegate.id}" labelName="delegate.name"
									labelValue="${wfDelegate.delegate.name}" title="用户"
									url="/sys/office/treeData?type=3"
									cssClass="form-control input-sm" allowClear="true"
									notAllowSelectParent="true" />
							</div>
						</form:form>
						<br />
					</div>
				</div>

				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="wf:delegate:wfDelegate:add">
								<table:addRow url="${ctx}/wf/delegate/wfDelegate/form"
									title="工作流委托設置"></table:addRow>
								<!-- 增加按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="wf:delegate:wfDelegate:edit">
								<table:editRow url="${ctx}/wf/delegate/wfDelegate/form"
									title="工作流委托設置" id="contentTable"></table:editRow>
								<!-- 编辑按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="wf:delegate:wfDelegate:del">
								<table:delRow url="${ctx}/wf/delegate/wfDelegate/deleteAll"
									id="contentTable"></table:delRow>
								<!-- 删除按钮 -->
							</shiro:hasPermission>
							<%--<shiro:hasPermission name="wf:delegate:wfDelegate:import">
								<table:importExcel url="${ctx}/wf/delegate/wfDelegate/import"></table:importExcel>
								<!-- 导入按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="wf:delegate:wfDelegate:export">
								<table:exportExcel url="${ctx}/wf/delegate/wfDelegate/export"></table:exportExcel>
								<!-- 导出按钮 -->
							</shiro:hasPermission> --%>
							<button class="btn btn-white btn-sm " data-toggle="tooltip"
								data-placement="left" onclick="sortOrRefresh()" title="刷新">
								<i class="glyphicon glyphicon-repeat"></i>
								<spring:message code="刷新"></spring:message>
							</button>

						</div>
						<div class="pull-right">
							<button class="btn btn-primary btn-rounded btn-outline btn-sm "
								onclick="search()">
								<i class="fa fa-search"></i>
								<spring:message code="查询"></spring:message>
							</button>
							<button class="btn btn-primary btn-rounded btn-outline btn-sm "
								onclick="reset()">
								<i class="fa fa-refresh"></i>
								<spring:message code="重置"></spring:message>
							</button>
						</div>
					</div>
				</div>

				<!-- 表格 -->
				<table id="contentTable"
					class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th><input type="checkbox" class="i-checks"></th>
							<%-- <th  class="sort-column remarks"><spring:message code="备注信息"></spring:message></th> --%>
							<th class="sort-column owner.name"><spring:message
									code="委托人"></spring:message></th>
							<th class="sort-column delegate.name"><spring:message
									code="代理人"></spring:message></th>
							<%-- <th class="sort-column flowType"><spring:message code="流程类型"></spring:message></th> --%>
							<th ><spring:message code="指定任务单号"></spring:message></th>
							<th ><spring:message code="指定任务"></spring:message></th>
							<th class="sort-column startTime"><spring:message
									code="开始时间"></spring:message></th>
							<th class="sort-column endTime"><spring:message code="结束时间"></spring:message></th>
							<th><spring:message code="操作"></spring:message></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="wfDelegate">
							<tr>
								<td><input type="checkbox" id="${wfDelegate.id}"
									class="i-checks"></td>
								<%-- <td><a  href="#" onclick="openDialogView('查看工作流委托設置', '${ctx}/wf/delegate/wfDelegate/form?id=${wfDelegate.id}','800px', '500px')">
					${wfDelegate.remarks}
				</a></td> --%>
								<td>${wfDelegate.owner.name}</td>
								<td>${wfDelegate.delegate.name}</td>
								<%-- <td>${fns:getSqlDictLabel('GET_FLOW_TYPE', '', wfDelegate.flowType)}
								</td> --%>
								<td>${wfDelegate.applicationNo}</td>
								<td>${wfDelegate.taskName}<%--${wfDelegate.taskId} --%></td>
								<td><fmt:formatDate value="${wfDelegate.startTime}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td><fmt:formatDate value="${wfDelegate.endTime}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td><shiro:hasPermission name="wf:delegate:wfDelegate:view">
										<a href="#"
											onclick="openDialogView('<spring:message code="查看工作流委托設置"></spring:message>', '${ctx}/wf/delegate/wfDelegate/form?id=${wfDelegate.id}','800px', '500px')"
											class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>
											<spring:message code="查看"></spring:message></a>
									</shiro:hasPermission> 
									<c:if test="${empty wfDelegate.applicationNo}">
										<shiro:hasPermission name="wf:delegate:wfDelegate:edit">
											<a href="#"
												onclick="openDialog('<spring:message code="修改工作流委托設置"></spring:message>', '${ctx}/wf/delegate/wfDelegate/form?id=${wfDelegate.id}','800px', '500px')"
												class="btn btn-success btn-xs"><i class="fa fa-edit"></i>
												<spring:message code="修改"></spring:message></a>
										</shiro:hasPermission> 
									</c:if>
									<shiro:hasPermission name="wf:delegate:wfDelegate:del">
										<a
											href="${ctx}/wf/delegate/wfDelegate/delete?id=${wfDelegate.id}"
											onclick="return confirmx('<spring:message code="确认要删除该工作流委托設置吗？"></spring:message>', this.href)"
											class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>
											<spring:message code="删除"></spring:message></a>
									</shiro:hasPermission></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>

				<!-- 分页代码 -->
				<table:page page="${page}"></table:page>
				<br /> <br />
			</div>
		</div>
	</div>
</body>
</html>