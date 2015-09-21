<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="ui" uri="/tags/ui" %>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
<title>流程实例明细</title>
</head>
<body>
<ui:TabNavigator fit="true">
	<ui:Tab id="index" title="流程运行明细" iconCls="icon-tab" style="padding:1px">
			<table class="tableView">
				<tr>
					<td class="label" style="width:200px;">流程实例标题:</td>
					<td>${processRun.subject}</td>
				</tr>
				<tr>
					<td class="label">创建人:</td>
					<td>${processRun.creator}</td>
				</tr>
				<tr>
					<td class="label">创建时间:</td>
					<td><fmt:formatDate value="${processRun.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				</tr>
				<tr>
					<td class="label">业务表单简述:</td>
					<td>${processRun.busDescp}</td>
				</tr>
				<tr>
					<td class="label">状态:</td>
					<td>
						<c:choose>
							<c:when test="${processRun.status==1}">
								正在运行
							</c:when>
							<c:when test="${processRun.status==2}">
								结束
							</c:when>
							<c:when test="${processRun.status==3}">
								人工结束
							</c:when>
							<c:when test="${processRun.status==4}">
								草稿
							</c:when>
							<c:when test="${processRun.status==5}">
								撤销
							</c:when>
							<c:when test="${processRun.status==6}">
								驳回
							</c:when>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="label">ACT流程实例ID:</td>
					<td>${processRun.actInstId}</td>
				</tr>
				<tr>
					<td class="label">ACT流程定义ID:</td>
					<td>${processRun.actDefId}</td>
				</tr>
				<tr>
					<td class="label">businessKey:</td>
					<td>${processRun.businessKey}</td>
				</tr>
			</table>
		</ui:Tab>
		<ui:Tab id="graph" title="流程图" iconCls="icon-tab" url="${ctx}/workflow/bpm/processRun/graph.html?runId=${processRun.runId}">
		</ui:Tab>
		<ui:Tab id="history" title="审批历史" iconCls="icon-tab" url="${ctx}/workflow/bpm/taskOpinion/toList.html?runId=${processRun.runId}">
		</ui:Tab>
		<ui:Tab id="log" title="操作日志" iconCls="icon-tab" url="${ctx}/workflow/bpm/runLog/toList.html?runId=${processRun.runId}">
		</ui:Tab>
		<ui:Tab id="form" title="业务表单" iconCls="icon-tab" url="${ctx}/workflow/bpm/processRun/getForm.html?runId=${processRun.runId}">
		</ui:Tab>
</ui:TabNavigator>
</body>
</html>