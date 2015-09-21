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
	function exportAuth(){
		var treeObj = $("#menuTree").tree("getZTreeObj");
		var checkItems = treeObj.getCheckedNodes(true);
		if(checkItems.length==0){
			MessageUtil.alert("请选择需要导出权限的菜单");
			return ;
		}
		var selMenus = [];
		for(var i=0;i<checkItems.length;i++){
			selMenus.push(checkItems[i].zjId);
		}
		
		var menuStr = selMenus.join(",");
		$("#selMenus").val(menuStr);
		$("#exportForm")[0].submit();
		
	}
	function onAsyncSuccess(event, treeId, treeNode, msg){
		var treeObj = $("#menuTree").tree("getZTreeObj");
		treeObj.expandAll(true);
	}
</script>
</head>
<body>
<form id="exportForm" action="${ctx}/menu/authExport.html" target="_blank">
	<input type="hidden" id="selMenus" name="selMenus">
</form>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:33px" border="false">
		<div id="toolbar" class="easyui-toolbar" style="width:100%;">
			<a id="export-btn" href="#" iconCls="icon-xls" onclick="exportAuth()">导出</a>
			<a id="export-btn" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center" border="false">
		<ul id="menuTree" class="easyui-tree" asyncEnable="true"  url="${ctx}/menu/loadAllMenu.html" chkboxType="{'Y': 's', 'N': 's'}" 
  				idKey="zjId" nameKey="cdmc" pIdKey="sjcd" rootPId="0" simpleDataEnable="true" chkEnable="true" onAsyncSuccess="onAsyncSuccess"></ul>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>