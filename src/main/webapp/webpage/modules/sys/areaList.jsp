<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>区域管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/webpage/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
			var data = ${fns:toJson(list)}, rootId = "0";
			addRow("#treeTableList", tpl, data, rootId, true);
			$("#treeTable").treeTable({expandLevel : 5});
		});
		function addRow(list, tpl, data, pid, root){
			for (var i=0; i<data.length; i++){
				var row = data[i];
				if ((${fns:jsGetVal('row.parentId')}) == pid){
					$(list).append(Mustache.render(tpl, {
						dict: {
							type: getDictLabel(${fns:toJson(fns:getDictList('sys_area_type'))}, row.type)
						}, pid: (root?0:pid), row: row
					}));
					addRow(list, tpl, data, row.id);
				}
			}
		}

		function refresh(){//刷新
			
			window.location="${ctx}/sys/area/";
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
			<h5><spring:message code="区域列表"></spring:message> </h5>
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
			<shiro:hasPermission name="sys:area:add">
				<table:addRow url="${ctx}/sys/area/form" title="区域"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> <spring:message code="刷新"></spring:message></button>
		
		</div>
	</div>
	</div>
	
	<table id="treeTable"  class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead><tr><th><spring:message code="区域名称"></spring:message></th><th><spring:message code="区域编码"></spring:message></th><th><spring:message code="区域类型"></spring:message></th><th><spring:message code="备注"></spring:message></th><th><spring:message code="操作"></spring:message></th></tr></thead>
		<tbody id="treeTableList"></tbody>
	</table>
	<br/>
	</div>
	</div>
</div>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td><a  href="#" onclick="openDialogView('<spring:message code="查看区域"></spring:message>', '${ctx}/sys/area/form?id={{row.id}}','800px', '500px')">{{row.name}}</a></td>
			<td>{{row.code}}</td>
			<td>{{dict.type}}</td>
			<td>{{row.remarks}}</td>
			<td>
				<shiro:hasPermission name="sys:area:view">
				<a href="#" onclick="openDialogView('<spring:message code="查看区域"></spring:message>', '${ctx}/sys/area/form?id={{row.id}}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i>  <spring:message code="查看"></spring:message></a>
				</shiro:hasPermission>
				<shiro:hasPermission name="sys:area:edit">
					<a href="#" onclick="openDialog('<spring:message code="修改区域"></spring:message>', '${ctx}/sys/area/form?id={{row.id}}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> <spring:message code="修改"></spring:message></a>
				</shiro:hasPermission>
				<shiro:hasPermission name="sys:area:del">
					<a href="${ctx}/sys/area/delete?id={{row.id}}" onclick="return confirmx('<spring:message code="要删除该区域及所有子区域项吗？"></spring:message>', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> <spring:message code="删除"></spring:message></a>
				</shiro:hasPermission>
				<shiro:hasPermission name="sys:area:add">
					<a href="#" onclick="openDialog('<spring:message code="添加下级区域"></spring:message>', '${ctx}/sys/area/form?parent.id={{row.id}}','800px', '500px')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i> <spring:message code="添加下级区域"></spring:message></a>
				</shiro:hasPermission>
			</td>
		</tr>
	</script>
</body>
</html>