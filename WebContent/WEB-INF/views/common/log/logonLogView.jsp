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
		<td class="label" style="width:100px">
			终端标识(IP)：
		</td>
		<td>
			${logonLog.terminalId}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			单位编号：
		</td>
		<td>
			${logonLog.orgNo}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			单位名称：
		</td>
		<td>
			${logonLog.orgName}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			身份证号：
		</td>
		<td>
			${logonLog.idCard}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			警号：
		</td>
		<td>
			${logonLog.policeId}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			用户名：
		</td>
		<td>
			${logonLog.userName}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			登录账号：
		</td>
		<td>
			${logonLog.loginId}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			登录时间：
		</td>
		<td>
			<fmt:formatDate value='${logonLog.logonTime}' pattern="yyyy-MM-dd HH:mm:ss"/>
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			登出时间：
		</td>
		<td>
			<fmt:formatDate value='${logonLog.logoutTime}' pattern="yyyy-MM-dd HH:mm:ss"/>
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			登录结果：
		</td>
		<td>
			${logonLog.logonResult=="0"?"<span style='color:red'>登录失败</span>":"登录成功"}
		</td>
	</tr>
	<tr>
		<td colspan="2" align="center">
			<input type="button" class="easyui-button" value="关闭" onclick="javascript:parentDialog.close()">
		</td>
	</tr>
</table>
</ui:Panel>
</body>
</html>