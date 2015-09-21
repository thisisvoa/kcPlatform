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
<title>基础信息管理>>代码管理</title>
</head>
<body>
	<div id="tt" class="easyui-tabs" fit="true" border="false">
		<div id="tab1" style="overflow:hidden" title="全部" iconCls="icon-tab" url="${ctx}/codeCtl/toCodeValidList.html">
		</div>
		<div id="tab2" style="overflow:hidden" title="启用" iconCls="icon-tab" url="${ctx}/codeCtl/toCodeValidList.html?useTarg=1">
		</div>
		<div id="tab3" style="overflow:hidden" title="禁用" iconCls="icon-tab" url="${ctx}/codeCtl/toCodeValidList.html?useTarg=0">
		</div>
	</div>
</body>
</html>