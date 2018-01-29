<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>分配角色</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	
	<div class="wrapper wrapper-content">
	<div class="container-fluid breadcrumb">
		<div class="row-fluid span12">
			<span class="span4"><spring:message code='角色名称'></spring:message>: <b>${role.name}</b></span>
			<span class="span4"><spring:message code='归属机构'></spring:message>: ${role.office.name}</span>
			<span class="span4"><spring:message code='英文名称'></spring:message>: ${role.enname}</span>
		</div>
		<div class="row-fluid span8">
			<span class="span4"><spring:message code='角色类型'></spring:message>: ${role.roleType}</span>
			<c:set var="dictvalue" value="${role.dataScope}" scope="page" />
			<span class="span4"><spring:message code='数据范围'></spring:message>: ${fns:getDictLabel(dictvalue, 'sys_data_scope', '')}</span>
		</div>
	</div>
	<sys:message content="${message}"/>
	<div class="breadcrumb">
		<form id="assignRoleForm" action="${ctx}/sys/role/assignrole" method="post" class="hide">
			<input type="hidden" name="id" value="${role.id}"/>
			<input id="idsArr" type="hidden" name="idsArr" value=""/>
		</form>
		<button id="assignButton" type="submit"  class="btn btn-outline btn-primary btn-sm" title="添加人员"><i class="fa fa-plus"></i> <spring:message code="添加人员"></spring:message></button>
		<script type="text/javascript">
			$("#assignButton").click(function(){
				
		top.layer.open({
		    type: 2, 
		    area: ['800px', '600px'],
		    title:"<spring:message code='选择用户'></spring:message>",
	        maxmin: true, //开启最大化最小化按钮
		    content: "${ctx}/sys/role/usertorole?id=${role.id}" ,
		    btn: ["<spring:message code='确定'></spring:message>", "<spring:message code='关闭'></spring:message>"],
		    yes: function(index, layero){
    	       var pre_ids = layero.find("iframe")[0].contentWindow.pre_ids;
				var ids = layero.find("iframe")[0].contentWindow.ids;
				if(ids[0]==''){
						ids.shift();
						pre_ids.shift();
					}
					if(pre_ids.sort().toString() == ids.sort().toString()){
						top.$.jBox.tip("未给角色【${role.name}】分配新成员！", 'info');
						return false;
					};
			    	// 执行保存
			    	loading("<spring:message code="<spring:message code='正在提交，请稍等...'></spring:message>"></spring:message>");
			    	var idsArr = "";
			    	for (var i = 0; i<ids.length; i++) {
			    		idsArr = (idsArr + ids[i]) + (((i + 1)== ids.length) ? '':',');
			    	}
			    	$('#idsArr').val(idsArr);
			    	$('#assignRoleForm').submit();
				    top.layer.close(index);
			  },
			  cancel: function(index){ 
    	       }
		}); 
			});
		</script>
	</div>
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead><tr><th><spring:message code="归属公司"></spring:message></th><th><spring:message code="归属部门"></spring:message></th><th><spring:message code="登录名"></spring:message></th><th><spring:message code="姓名"></spring:message></th><th><spring:message code="电话"></spring:message></th><th><spring:message code="手机"></spring:message></th><shiro:hasPermission name="sys:user:edit"><th><spring:message code="操作"></spring:message></th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${userList}" var="user">
			<tr>
				<td>${user.company.name}</td>
				<td>${user.office.name}</td>
				<td><a href="${ctx}/sys/user/form?id=${user.id}">${user.loginName}</a></td>
				<td>${user.name}</td>
				<td>${user.phone}</td>
				<td>${user.mobile}</td>
				<shiro:hasPermission name="sys:role:edit"><td>
					<a href="${ctx}/sys/role/outrole?userId=${user.id}&roleId=${role.id}" 
						onclick="return confirmx('<spring:message code="确认要将用户"></spring:message><b>[${user.name}]</b><spring:message code="从"></spring:message><b>[${role.name}]</b><spring:message code="角色中移除吗？"></spring:message>', this.href)"><spring:message code='移除'></spring:message></a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	</div>
</body>
</html>
