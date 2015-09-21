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
<script type="text/javascript">
	function onAsyncSuccess(event, treeId, treeNode, msg){
		var treeObj = $("#permissionTree").tree("getZTreeObj");
		treeObj.expandAll(true);
	}
</script>
</head>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:30px;text-align:center;padding-top:3px;background-color:#F8F8F8" border="false">
		<input type="button" value="关 闭 " class="easyui-button" onclick="javascript:parentDialog.close();"/></td>
	</ui:Layout>
	<ui:Layout region="center">
		<ul id="permissionTree" class="easyui-tree" asyncEnable="true"  url="${ctx}/userCtl/getUserPermission.html?userId=${user.zjid}" 
	 				rootPId="0" simpleDataEnable="true" idKey="ID" pIdKey="PID" nameKey="NAME" onAsyncSuccess="onAsyncSuccess"
	 				></ul>
	</ui:Layout>
	<ui:Layout region="south" style="height:30px;padding-top:5px;background-color:#F8F8F8" border="false">
				<div class="icon-qbzc" style="width:16px;height:16px;float:left"></div>
				<div>
					已选用户：<b>${user.yhmc}</b>
				</div>
		
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>