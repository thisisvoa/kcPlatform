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
</head>
<body>
	<table class="tableView">
		<tr>
			<td colspan="2" align="center">
				<input type="button" class="easyui-button" value="关闭" onclick="javascript:parentDialog.close()">
			</td>
		</tr>
		<tr>
			<td class="label" style="width:120px">功能名称：</td>
			<td>&nbsp;${func.gnmc}</td>
		</tr>
		<tr>
			<td class="label">功能代码：</td>
			<td>&nbsp;${func.gndm}</td>
		</tr>
		<tr>
			<td class="label">所属菜单：</td>
			<td>&nbsp;${menu.cdmc}</td>
		</tr>
		<tr>
			<td class="label">功能序号：</td>
			<td>&nbsp;${func.gnxh}</td>
		</tr>
		<tr>
			<td class="label">使用标识：</td>
			<td>&nbsp;<c:if test="${func.sybz == '0'}">
					<span class="text" style="color: red;">禁用</span>
				</c:if>
				<c:if test="${func.sybz == '1'}">
					<span class="text">启用</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="label">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td><textarea name="bz" class="easyui-textarea" readonly="readonly" style="width:470px;height:200px" >${func.bz}</textarea></td>
		</tr>
	</table>
</body>
</html>
