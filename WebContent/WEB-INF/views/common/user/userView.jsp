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
<title>编辑单位</title>
</head>
<body>
	<table class="tableView">
		<tr>
			<td colspan="2" align="center"><input type="button" class="easyui-button" value="关 闭 " onclick="javascript:parentDialog.close();"/></td>
		</tr>
		<tr>
			<td width="25%" class="label">用户姓名：</td>
			<td width="75%">${userInfo.YHMC}</td>
		</tr>
		<tr>
			<td class="label">登录账号：</td>
			<td>${userInfo.YHDLZH}</td>
		</tr>
		<tr>
			<td class="label">登录密码：</td>
			<td>********</td>
		</tr>
		<tr>
			<td class="label">警员编号：</td>
			<td>${userInfo.JYBH}</td>
		</tr>

		<tr>
			<td class="label">所属单位：</td>
			<td>${userInfo.DWMC}</td>
		</tr>
		<tr>
			<td class="label">性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别：</td>
			<td>
			
						<c:if test="${userInfo.XB == '1'}">男 </c:if>
				
						<c:if test="${userInfo.XB == '0'}">女</c:if>
			
			</td>
		</tr>
		<tr>
			<td class="label">身份证号：</td>
			<td>${userInfo.SFZH}</td>
		</tr>
		<tr>
			<td class="label">联系电话：</td>
			<td>${userInfo.LXDH}</td>
		</tr>
		<tr>
			<td class="label">移动电话：</td>
			<td>${userInfo.YDDH}</td>
		</tr>
		<tr>
			<td class="label">单位邮箱：</td>
			<td>${userInfo.DZYX}</td>
		</tr>
		<tr>
			<td class="label">用户类别：</td>
			<td>
				<c:if test="${userInfo.YHLX == '1'}">普通</c:if>
				<c:if test="${userInfo.YHLX == '0'}">高级</c:if>
			</td>
		</tr>
		<tr>
			<td class="label">用户级别：</td>
			<td>							
				<c:if test="${userInfo.YHJB == '1'}"> 省级 </c:if>				
				<c:if test="${userInfo.YHJB == '2'}"> 市级 </c:if>
				<c:if test="${userInfo.YHJB == '3'}"> 县级 </c:if>
				<c:if test="${userInfo.YHJB == '4'}"> 派出所</c:if>
			</td>
		</tr>
		<tr>
			<td class="label">使用标识：</td>
			<td>
				<c:if test="${userInfo.SYBZ == '1'}"> 启用</c:if>
				<c:if test="${userInfo.SYBZ == '0'}"> 禁用 </c:if> 
			</td>
		</tr>
		<tr>
			<td class="label">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td><textarea name="bz" class="easyui-textarea" readonly="readonly" style="width:365px;height:100px" >${userInfo.BZ}</textarea></td>
		</tr>
	</table>
</body>
</html>
