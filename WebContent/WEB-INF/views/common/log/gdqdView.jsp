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
<title></title>
<body>
<ui:Panel fit="true">
<table class="tableView">
	<tr>
		<td colspan="4" align="center">
			<input type="button" class="easyui-button" value="关闭" onclick="javascript:parentDialog.close()">
		</td>
	</tr>
	<tr>
		<td class="label" style="width:110px">
			归档名称：
		</td>
		<td>
			${gdqd.gdmc}
		</td>
		<td class="label" style="width:110px">
			归档表：
		</td>
		<td>
			${gdqd.gdbmc}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:110px">
			存储表：
		</td>
		<td>
			${gdqd.gdhbmc}
		</td>
		<td class="label" style="width:110px">
			归档数据开始时间：
		</td>
		<td>
			<fmt:formatDate value='${gdqd.gdsjKssj}' pattern="yyyy-MM-dd"/>
		</td>
	</tr>
	<tr>
		<td class="label" style="width:110px">
			归档数据结束时间：
		</td>
		<td>
			<fmt:formatDate value='${gdqd.gdsjJssj}' pattern="yyyy-MM-dd"/>
		</td>
		<td class="label" style="width:110px">
			归档记录数：
		</td>
		<td>
			${gdqd.gdjls}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:110px">
			归档时间：
		</td>
		<td colspan="3">
			<fmt:formatDate value='${gdqd.jlcjsj}' pattern="yyyy-MM-dd HH:mm:ss"/>
		</td>
		
	</tr>
</table>
</ui:Panel>
</body>
</html>