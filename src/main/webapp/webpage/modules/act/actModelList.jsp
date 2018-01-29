<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>模型管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function(){
			top.$.jBox.tip.mess = null;
		});
		function updateCategory(id, category){
			layer.open({
				  type: 1, 
				  title:"设置分类",
				  area: ['400px', '200px'],
				  content: $("#categoryBox").html() //这里content是一个普通的String
				});
			$("#categoryBoxId").val(id);
			$("#categoryBoxCategory").val(category);
		}
	</script>
	<script type="text/template" id="categoryBox">
		<form id="categoryForm" action="${ctx}/act/model/updateCategory" method="post" enctype="multipart/form-data"
			style="text-align:center;"   onsubmit="loading('正在分类，请稍等...');"><br/>
		

		<div class="col-md-12">
   			<div class="form-group">
        		<label class="col-sm-3 control-label">选择分类：</label>
       			<div class="col-sm-9">
           			 <input id="categoryBoxId" type="hidden" name="id" value="" />
					<select id="categoryBoxCategory" name="category" class="form-control">
						<c:forEach items="${fns:getDictList('act_category')}" var="dict">
							<option value="${dict.value}">${dict.label}</option>
						</c:forEach>
					</select>
       			 </div>
   			 </div>
			</div>	
			
			<br/>
			<br/>　
			<br/>　
			<br/>　　
			<input id="categorySubmit" class="btn btn-primary" type="submit" value="   保    存   "/>　　
		</form>
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5><spring:message code="模型管理"></spring:message> </h5>
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
	<form:form id="searchForm" modelAttribute="category" action="${ctx}/act/model/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<select id="category" name="category" class="form-control">
				<option value=""><spring:message code="全部分类"></spring:message></option>
				<c:forEach items="${fns:getDictList('act_category')}" var="dict">
					<option value="${dict.value}" ${dict.value==category?'selected':''}>${dict.label}</option>
				</c:forEach>
			</select>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="act:model:create">
				<table:addRow url="${ctx}/act/model/create"  title="新建模型"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="act:model:del">
				<table:delRow url="${ctx}/act/model/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
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
				<th><spring:message code="流程分类"></spring:message></th>
				<th><spring:message code="模型ID"></spring:message></th>
				<th><spring:message code="模型标识"></spring:message></th>
				<th><spring:message code="模型名称"></spring:message></th>
				<th><spring:message code="版本号"></spring:message></th>
				<th><spring:message code="创建时间"></spring:message></th>
				<th><spring:message code="最后更新时间"></spring:message></th>
				<th><spring:message code="操作"></spring:message></th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="model">
			<tr>
				<td> <input type="checkbox" id="${model.id}" class="i-checks"></td>
				<td><a href="javascript:updateCategory('${model.id}', '${model.category}')" title="设置分类">${fns:getDictLabel(model.category,'act_category','无分类')}</a></td>
					<td>${model.id}</td>
					<td>${model.key}</td>
					<td>${model.name}</td>
					<td><b title='流程版本号'>V: ${fns:getSqlDictLabel('GET_PROC_VERSION', model.key, model.key)}</b></td>
					<td><fmt:formatDate value="${model.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td><fmt:formatDate value="${model.lastUpdateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>
						<shiro:hasPermission name="act:model:edit">
							<a href="${pageContext.request.contextPath}/act/rest/modeler.html?modelId=${model.id}" target="_blank"><spring:message code="在线设计"></spring:message></a>
						</shiro:hasPermission>
						<shiro:hasPermission name="act:model:deploy">
							<a href="${ctx}/act/model/deploy?id=${model.id}" onclick="return confirmx('<spring:message code="确认要部署该模型吗？"></spring:message>', this.href)"><spring:message code="部署"></spring:message></a>
						</shiro:hasPermission>
						<shiro:hasPermission name="act:model:export">
							<a href="${ctx}/act/model/export?id=${model.id}" target="_blank"><spring:message code="导出"></spring:message></a>
						</shiro:hasPermission>
						<shiro:hasPermission name="act:model:del">
	                    	<a href="${ctx}/act/model/delete?id=${model.id}" onclick="return confirmx('<spring:message code="确认要删除该模型吗？"></spring:message>', this.href)"><spring:message code="删除"></spring:message></a>
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
