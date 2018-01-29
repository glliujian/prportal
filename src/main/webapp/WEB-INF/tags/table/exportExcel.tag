<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ attribute name="url" type="java.lang.String" required="true"%>
<%-- 使用方法： 1.将本tag写在查询的form之前；2.传入url --%>
<button id="btnExport" class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" title="导出"><i class="fa fa-file-excel-o"></i> <spring:message code="导出"></spring:message></button>
<script type="text/javascript">
$(document).ready(function() {

	$("#btnExport").click(function(){
		top.layer.confirm('<spring:message code='确认要导出Excel吗?'></spring:message>', {icon: 3, title:'<spring:message code='系统提示'></spring:message>'}, function(index){
		    //do something
		    	//导出之前备份
		    	var url =  $("#searchForm").attr("action");
		    	var pageNo =  $("#pageNo").val();
		    	var pageSize = $("#pageSize").val();
		    	//导出excel
		        $("#searchForm").attr("action","${url}");
			    $("#pageNo").val(-1);
				$("#pageSize").val(-1);
				$("#searchForm").submit();

				//导出excel之后还原
				$("#searchForm").attr("action",url);
			    $("#pageNo").val(pageNo);
				$("#pageSize").val(pageSize);
		    top.layer.close(index);
		});
	});
    
});


</script>