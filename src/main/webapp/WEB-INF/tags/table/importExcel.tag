<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ attribute name="url" type="java.lang.String" required="true"%>
<%-- 使用方法： 1.将本tag写在查询的form之前；2.传入controller的url --%>
<button id="btnImport" class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" title="导入"><i class="fa fa-folder-open-o"></i> <spring:message code="导入"></spring:message></button>
<div id="importBox" class="hide">
		<form id="importForm" action="${url}" method="post" enctype="multipart/form-data"
			 style="padding-left:20px;text-align:center;" onsubmit="loading('<spring:message code='正在导入，请稍等...'></spring:message>');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px" value="<spring:message code='选择文件'></spring:message>" /><spring:message code="导入文件不能超过5M，仅允许导入xls或xlsx格式文件！"></spring:message><br/>　　
			
			
		</form>
</div>
<script type="text/javascript">
$(document).ready(function() {
	$("#btnImport").click(function(){
		top.layer.open({
		    type: 1, 
		    area: [500, 300],
		    title:"<spring:message code='导入数据'></spring:message>",
		    content:$("#importBox").html() ,
		    btn: ['<spring:message code='下载模板'></spring:message>','<spring:message code='确定'></spring:message>', '<spring:message code='关闭'></spring:message>'],
			    btn1: function(index, layero){
				  window.location.href='${url}/template';
			  },
		    btn2: function(index, layero){
			        var inputForm =top.$("#importForm");
			        var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
			        inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
    	       		top.$("#importForm").submit();
				    top.layer.close(index);
			  },
			 
			  btn3: function(index){ 
				  top.layer.close(index);
    	       }
		}); 
	});
    
});

</script>