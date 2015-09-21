<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="/tags/ui" %>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
</head>
<body>
	<table class="tableView">
		<tr>
			<td colspan="4" align="center">
				<input type="button" class="easyui-button" value="关闭" onclick="javascript:parentDialog.close()">
			</td>
		</tr>
		<tr>
			<td class="label" style="width:100px">任务标识：</td>
			<td>&nbsp;${jobRunLog.jobName}</td>
			<td class="label"  style="width:100px">运行地址：</td>
			<td>&nbsp;${jobRunLog.scheduler}</td>
		</tr>
		<tr>
			<td class="label">开始运行时间：</td>
			<td>&nbsp;<fmt:formatDate value='${jobRunLog.beginTime}' pattern="yyyy-MM-dd HH:mm:ss"/></td>
			<td class="label">结束运行时间：</td>
			<td>&nbsp;<fmt:formatDate value='${jobRunLog.endTime}' pattern="yyyy-MM-dd HH:mm:ss"/></td>
		</tr>
		<tr>
			<td class="label"  style="width:100px">执行结果：</td>
			<td colspan="3">
				&nbsp;${jobRunLog.resultType=='1'?"成功":"失败"}
			</td>
		</tr>
		<tr>
			<td class="label" style="width:100px">执行结果信息：</td>
			<td colspan="3"><textarea class="easyui-textarea" readonly="readonly" style="width:450px;height:150px" >${jobRunLog.resultInfo}</textarea></td>
		</tr>
	</table>
</body>
</html>