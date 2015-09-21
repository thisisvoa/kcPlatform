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
		<td class="label" style="width:120px">
			调用时间：
		</td>
		<td>
			<fmt:formatDate value='${interfaceLog.callTime}' pattern="yyyy-MM-dd"/>
		</td>
	</tr>
	<tr>
		<td class="label" style="width:120px">
			服务请求方名称：
		</td>
		<td>
			${interfaceLog.callerName}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:120px">
			终端标识(IP)：
		</td>
		<td>
			${interfaceLog.terminalId}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:120px">
			返回数据条目数：
		</td>
		<td>
			${interfaceLog.resultCount}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:120px">
			接口描述：
		</td>
		<td>
			${interfaceLog.interfaceDesc}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:120px">
			调用用户单位名称：
		</td>
		<td>
			${interfaceLog.orgName}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:120px">
			调用用户名：
		</td>
		<td>
			${interfaceLog.userName}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:120px">
			调用用户账号：
		</td>
		<td>
			${interfaceLog.loginId}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:120px">
			调用结果：
		</td>
		<td>
			${interfaceLog.callSuccess=="0"?"<span style='color:red'>调用失败</span>":"调用成功"}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:120px">
			调用参数记录：
		</td>
		<td>
			${interfaceLog.paramVariable}
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