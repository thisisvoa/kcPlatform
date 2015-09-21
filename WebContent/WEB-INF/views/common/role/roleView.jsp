<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="/tags/ui" %>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
<title>角色详细信息</title>
</head>
<body>
	<table class="tableView">
		<tr>
			<td colspan="2" align="center">
				<input type="button" class="easyui-button" value="关 闭 " onclick="javascript:parentDialog.close();"/>
			</td>
		</tr>
		<tr>
			<td width="25%" class="label">角色名称：</td>
			<td width="75%">${roleInfo.jsmc}</td>
		</tr>
		<tr>
			<td class="label">角色代码：</td>
			<td>${roleInfo.jsdm}</td>
		</tr>
		<tr>
			<td class="label">使用标识：</td>
			<td>
				<c:if test="${roleInfo.sybz == '1'}">启用</c:if>
				
				<c:if test="${roleInfo.sybz == '0'}"><span style="color:red;">禁用</span></c:if> 
			</td>
		</tr>
		<tr>
			<td class="label">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td><textarea name="bz" class="easyui-textarea" readonly="readonly" style="width:360px;height:160px" >${roleInfo.bz}</textarea></td>
		</tr>
	</table>
</body>
</html>
