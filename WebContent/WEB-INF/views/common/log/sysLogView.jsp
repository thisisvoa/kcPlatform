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
			${sysLog.terminalId}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			单位编号：
		</td>
		<td>
			${sysLog.orgNo}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			单位名称：
		</td>
		<td>
			${sysLog.orgName}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			身份证号：
		</td>
		<td>
			${sysLog.idCard}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			警号：
		</td>
		<td>
			${sysLog.policeId}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			用户名：
		</td>
		<td>
			${sysLog.userName}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			登录账号：
		</td>
		<td>
			${sysLog.loginId}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			操作时间：
		</td>
		<td>
			<fmt:formatDate value='${sysLog.operateTime}' pattern="yyyy-MM-dd HH:mm:ss"/>
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			操作内容：
		</td>
		<td>
			<textarea class="easyui-textarea" style="width:460px;height:80px;">${sysLog.operateContent}</textarea>
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			操作反馈：
		</td>
		<td>
			${sysLog.operateResult}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			操作描述：
		</td>
		<td>
			<textarea class="easyui-textarea" style="width:460px;height:80px;">${sysLog.operateDesc}</textarea>
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			功能模块：
		</td>
		<td>
			${sysLog.moduleName}
		</td>
	</tr>
	
	<tr>
		<td class="label" style="width:100px">
			日志类型：
		</td>
		<td>
			<c:if test="${sysLog.logType=='1'}">查询</c:if>
			<c:if test="${sysLog.logType=='2'}">新增</c:if>
			<c:if test="${sysLog.logType=='3'}">删除</c:if>
			<c:if test="${sysLog.logType=='4'}">修改</c:if>
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