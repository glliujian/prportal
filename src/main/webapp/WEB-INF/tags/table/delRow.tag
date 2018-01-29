<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true"%>
<%@ attribute name="url" type="java.lang.String" required="true"%>
<%@ attribute name="label" type="java.lang.String" required="false"%>
<button class="btn btn-white btn-sm" onclick="deleteAll()" data-toggle="tooltip" data-placement="top"><i class="fa fa-trash-o"> <spring:message code="${label==null?'删除':label}"></spring:message></i>
                        </button>
<%-- 使用方法： 1.将本tag写在查询的form之前；2.传入table的id和controller的url --%>
<script type="text/javascript">
$(document).ready(function() {
    $('#${id} thead tr th input.i-checks').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
    	  $('#${id} tbody tr td input.i-checks').iCheck('check');
    	});

    $('#${id} thead tr th input.i-checks').on('ifUnchecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
    	  $('#${id} tbody tr td input.i-checks').iCheck('uncheck');
    	});
    
});

	function deleteAll(){

		// var url = $(this).attr('data-url');
		  var str="";
		  var ids="";
		  $("#${id} tbody tr td input.i-checks:checkbox").each(function(){
		    if(true == $(this).is(':checked')){
		      str+=$(this).attr("id")+",";
		    }
		  });
		  if(str.substr(str.length-1)== ','){
		    ids = str.substr(0,str.length-1);
		  }
		  if(ids == ""){
			top.layer.alert("<spring:message code='请至少选择一条数据'></spring:message>!", {icon: 0, title:"<spring:message code='警告'></spring:message>!"});
			return;
		  }
			top.layer.confirm("<spring:message code='确认要彻底删除数据吗'></spring:message>?", {icon: 3, title:"<spring:message code='系统提示'></spring:message>!"}, function(index){
			window.location = "${url}?ids="+ids;
		    top.layer.close(index);
		});
		 

	}
</script>