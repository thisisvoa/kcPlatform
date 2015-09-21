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
<title>单位详细信息查看</title>
</head>
<body>
	<table class="tableView">
		<tr>
			<td colspan="2" align="center">
				<input type="button" class="easyui-button" value="关 闭 " onclick="javascript:parentDialog.close();"/>
			</td>
		</tr>
		<tr>
			<td class="label" width="25%">单位编码：</td>
			<td width="75%">${orgInfo.dwbh}</td>
		</tr>
		<tr>
			<td class="label">单位名称：</td>
			<td>${orgInfo.dwmc}</td>
		</tr>
		<tr>
			<td class="label">单位简称：</td>
			<td>${orgInfo.dwjc}</td>
		</tr>
		<tr>
			<td class="label">上级单位：</td>
			<td>${parentOrg.dwmc}</td>
		</tr>
		<tr>
			<td class="label">单位负责人：</td>
			<td>${orgInfo.dwfzr}</td>
		</tr>
		<tr>
			<td class="label">单位电话：</td>
			<td>${orgInfo.dwdh}</td>
		</tr>
		<tr>
			<td class="label">单位邮箱：</td>
			<td>${orgInfo.dwyx}</td>
		</tr>
		<tr>
			<td class="label">单位类别：</td>
			<td>
							<c:if test="${orgInfo.dwlx == '1'}"> 普通</c:if>
							<c:if test="${orgInfo.dwlx == '0'}">高级</c:if>
			</td>
		</tr>
		<tr>
			<td class="label">单位级别：</td>
			<td>
							<c:if test="${orgInfo.dwjb == '1'}"> 省级 </c:if>
						
							<c:if test="${orgInfo.dwjb == '0'}"> 市级</c:if>
			</td>
		</tr>
		<tr>
			<td class="label">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td><textarea name="bz" class="easyui-textarea" readonly="readonly" style="width:435px;height:100px" >${orgInfo.bz}</textarea></td>
		</tr>
	</table>
</body>
</html>
