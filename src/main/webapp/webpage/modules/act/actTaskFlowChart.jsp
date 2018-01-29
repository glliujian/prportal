<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<table id="CurrentFlowTable" class="table table-striped table-bordered ">
	<tr>
		<td colspan=6><font size="4pt"><spring:message code="该PR的节点流程"></spring:message></font></td>
	</tr>
	<c:forEach items="${histoicFlowList}" var="act">
		<c:if test="${act.histIns.endTime !=null}">
			<td id="start" style="width:80px;text-align:center;background-color:#f5f5f5;"><span style="color:#000000;">${act.histIns.activityName}>></span></td>
		</c:if>
		<c:if test="${act.histIns.endTime ==null}">
			<td id="start" style="width:80px;text-align:center;background-color:#5BA276;"><span style="color:#FFFFFF;">${act.histIns.activityName}</span></td>
		</c:if>
	</c:forEach>
</table>