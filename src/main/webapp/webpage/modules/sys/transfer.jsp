<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>PR审批单管理</title>
	<meta name="decorator" content="default"/>
<script src="${ctxStatic}/common/inspinia.js?v=3.2.0"></script>
	<script src="${ctxStatic}/common/contabs.js"></script> 
	
	<script type="text/javascript">
		$(document).ready(function() {
			console.log($("#page-wrapper",window.parent.document).children('.row .content-tabs').attr('class'));
			$(".J_menuTab .active").find(".fa .fa-times-circle").eq(0).trigger("click");
			
			
			//top.closeTab();
			//top.openTab('/prportal/a/oa/pr/tracing/','我的申请', false)
			//top.refreshTab();
		});
	</script>
	
</head>
<body>
	
</body>
</html>