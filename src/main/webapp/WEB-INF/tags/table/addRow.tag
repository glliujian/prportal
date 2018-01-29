<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ attribute name="url" type="java.lang.String" required="true"%>
<%@ attribute name="title" type="java.lang.String" required="true"%>
<%@ attribute name="width" type="java.lang.String" required="false"%>
<%@ attribute name="height" type="java.lang.String" required="false"%>
<%@ attribute name="target" type="java.lang.String" required="false"%>
<%@ attribute name="label" type="java.lang.String" required="false"%>
<button class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left" onclick="add()" title="新增"><i class="fa fa-plus"></i> <spring:message code="${label==null?'新增':label}"></spring:message></button>
<%-- 使用方法： 1.将本tag写在查询的form之前；2.传入table的id和controller的url --%>
<script type="text/javascript">
	function add(){
		openDialog("<spring:message code='新增'></spring:message>"/* +"<spring:message code='${title}'></spring:message>" */,"${url}","${width==null?'800px':width}", "${height==null?'500px':height}","${target}");
	}
</script>