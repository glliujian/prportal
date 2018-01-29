<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>PR审批单管理</title>
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
		<h5>PR审批单列表 </h5>
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
	<form:form id="searchForm" modelAttribute="pr" action="${ctx}/oa/pr/" method="post" class="form-inline">
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
			<shiro:hasPermission name="oa:pr:add">
				<table:addRow url="${ctx}/oa/pr/form" title="PR审批单"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="oa:pr:edit">
			    <table:editRow url="${ctx}/oa/pr/form" title="PR审批单" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="oa:pr:del">
				<table:delRow url="${ctx}/oa/pr/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="oa:pr:import">
				<table:importExcel url="${ctx}/oa/pr/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="oa:pr:export">
	       		<table:exportExcel url="${ctx}/oa/pr/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column remarks">备注信息</th>
				<th  class="sort-column costCenter">Name of Department & Cost Center Code</th>
				<th  class="sort-column processInstanceId">flowid</th>
				<th  class="sort-column payeeCompany">Name of Payee Company</th>
				<th  class="sort-column applicationNo">Application no.</th>
				<th  class="sort-column poNo">Purchase Order no.</th>
				<th  class="sort-column transferNo">Transfer Note no.</th>
				<th  class="sort-column equipment">Name of equipment</th>
				<th  class="sort-column brand">Brand Name</th>
				<th  class="sort-column modelNo">Model no.</th>
				<th  class="sort-column supplier">Supplier Name</th>
				<th  class="sort-column unitPrice">Unit Price</th>
				<th  class="sort-column quantity">Quantity</th>
				<th  class="sort-column requestStatus">Request status</th>
				<th  class="sort-column requestArrivalDate">Request arrival date</th>
				<th  class="sort-column importMethod">Import method</th>
				<th  class="sort-column actualArrivalDate">Actual arrival date</th>
				<th  class="sort-column budgetType">Budget</th>
				<th  class="sort-column purchaseLocation">Purchase location</th>
				<th  class="sort-column paidByFinance">Paid by finance</th>
				<th  class="sort-column priceAmount">Total Amount</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="pr">
			<tr>
				<td> <input type="checkbox" id="${pr.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看PR审批单', '${ctx}/oa/pr/form?id=${pr.id}','800px', '500px')">
					${pr.remarks}
				</a></td>
				<td>
					${pr.costCenter}
				</td>
				<td>
					${pr.processInstanceId}
				</td>
				<td>
					${pr.payeeCompany}
				</td>
				<td>
					${pr.applicationNo}
				</td>
				<td>
					${pr.poNo}
				</td>
				<td>
					${pr.transferNo}
				</td>
				<td>
					${pr.equipment}
				</td>
				<td>
					${pr.brand}
				</td>
				<td>
					${pr.modelNo}
				</td>
				<td>
					${pr.supplier}
				</td>
				<td>
					${pr.unitPrice}
				</td>
				<td>
					${pr.quantity}
				</td>
				<td>
					${pr.requestStatus}
				</td>
				<td>
					<fmt:formatDate value="${pr.requestArrivalDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${pr.importMethod}
				</td>
				<td>
					<fmt:formatDate value="${pr.actualArrivalDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${pr.budgetType}
				</td>
				<td>
					${pr.purchaseLocation}
				</td>
				<td>
					${pr.paidByFinance}
				</td>
				<td>
					${pr.priceAmount}
				</td>
				<td>
					<shiro:hasPermission name="oa:pr:view">
						<a href="#" onclick="openDialogView('查看PR审批单', '${ctx}/oa/pr/form?id=${pr.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="oa:pr:edit">
    					<a href="#" onclick="openDialog('修改PR审批单', '${ctx}/oa/pr/form?id=${pr.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="oa:pr:del">
						<a href="${ctx}/oa/pr/delete?id=${pr.id}" onclick="return confirmx('确认要删除该PR审批单吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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