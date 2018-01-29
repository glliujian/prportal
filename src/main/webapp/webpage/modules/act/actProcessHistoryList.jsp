<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>已结束的流程</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function(){
			top.$.jBox.tip.mess = null;
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5><spring:message code="已结束的流程"></spring:message> </h5>
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
	<form:form id="searchForm"  action="${ctx}/act/process/historyList/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<label><spring:message code="流程实例ID"></spring:message>：</label><input type="text" id="procInsId" name="procInsId" value="${procInsId}" class="form-control"/>
			<label><spring:message code="流程定义Key"></spring:message>：</label><input type="text" id="procDefKey" name="procDefKey" value="${procDefKey}" class="form-control"/>
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
				<th><spring:message code="流程发起人"></spring:message></th>
				<th><spring:message code="执行ID"></spring:message></th>
				<th><spring:message code="流程实例ID"></spring:message></th>
				<th><spring:message code="流程定义ID"></spring:message></th>
				<th><spring:message code="流程启动时间"></spring:message></th>
				<th><spring:message code="流程结束时间"></spring:message></th>
				<th><spring:message code="流程状态"></spring:message></th>
				<th><spring:message code="操作"></spring:message></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="procIns">
				<tr>
					<td>${procIns.startUserId}</td>
					<td>${procIns.id}</td>
					<td>${procIns.processInstanceId}</td>
					<td>${procIns.processDefinitionId}</td>
					<td><fmt:formatDate value="${procIns.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td><fmt:formatDate value="${procIns.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td><c:if test="${procIns.deleteReason != null}">[<spring:message code="流程作废"></spring:message>]<spring:message code="原因"></spring:message>：${procIns.deleteReason}</c:if>  <c:if test="${procIns.deleteReason == null}">[<spring:message code="正常结束"></spring:message>]</c:if></td>
					<td>
						<a  href="javascript:openDialog('查看流程历史','${ctx}/act/task/trace/photo/${procIns.processDefinitionId}/${procIns.id}','1000px', '600px')"><spring:message code="历史"></spring:message></a>
						
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