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
<title>权限分配管理>>角色功能关系管理</title>
</head>
<body>
	<ui:TabNavigator fit="true" border="false">
		<kpm:HasPermission permCode="SYS_USER_FUNC_ASSIGN">
			<ui:Tab id="tab1" title="按用户分配功能" iconCls="icon-tab" style="overflow:hidden">
				<iframe width="100%" height="100%" frameBorder=0 id="frmrightChild" 
								name=frmrightChild src="${ctx}/userFunc/assignFuncByUser.html" 
								allowTransparency="true"></iframe>
			</ui:Tab>
		</kpm:HasPermission>
		<kpm:HasPermission permCode="SYS_FUNC_USER_ASSIGN">
			<ui:Tab id="tab2" title="按功能分配用户" iconCls="icon-tab" style="overflow:hidden">
				<iframe width="100%" height="100%" frameBorder=0 id=frmrightChild 
								name=frmrightChild src="${ctx}/userFunc/assignUserByFunc.html"
								allowTransparency="true"></iframe>
			</ui:Tab>
		</kpm:HasPermission>
	</ui:TabNavigator>
</body>
</html>