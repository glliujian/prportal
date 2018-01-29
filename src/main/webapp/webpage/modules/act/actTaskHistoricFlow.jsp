<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<table id="HistoryFlowTable" class="table table-striped table-bordered table-condensed">
	<tr>
		<td colspan=4><font size="4pt"><spring:message code="历史审批记录"></spring:message></font></td>
	</tr>
	<tr><%--<th><spring:message code="执行环节"></spring:message> </th>--%>
		<th><spring:message code="执行人"></spring:message></th>
		<%--<th><spring:message code="开始时间"></spring:message></th> --%>
		<th><spring:message code="日期与时间"></spring:message></th>
		<th><spring:message code="审批状态"></spring:message></th>
		<th><spring:message code="提交意见"></spring:message></th>
		<%-- <th><spring:message code="任务历时"></spring:message></th>--%>
	</tr>
	<c:forEach items="${histoicFlowList}" var="act">
	<c:if test="${not empty act.assigneeName}">
		<tr>
			<%--<td>${act.histIns.activityName}</td> --%>
			<td>${act.assigneeName}</td>
			<%--<td><fmt:formatDate value="${act.histIns.startTime}" type="both" timeZone="${timeZone}" pattern="yyyy-MM-dd HH:mm:ss"/></td> --%>
			<td><fmt:formatDate value="${act.histIns.endTime}" type="both" timeZone="${timeZone}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			<td>${act.approvalAction }</td>
			<td style="word-wrap:break-word;word-break:break-all;">${act.comment}</td>
			<%--<td>${act.durationTime}</td> --%>
		</tr>
	</c:if>
		
	</c:forEach>
</table>