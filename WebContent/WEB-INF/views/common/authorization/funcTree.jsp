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
	var operateType = "${operateType}";
	
	function onFuncTreeClick(event, treeId, treeNode){
		var type = treeNode.type;
		if(type=="func"){
			var funcId = treeNode.funcId;
			if(operateType=="assignRole"){
				parent.frames["roleFrame"].document.location.href = "${ctx}/roleFunc/assignRoleToFuncPage.html"
					+"?funcId="+funcId;
			}else if(operateType=="assignOrg"){
				parent.frames["orgFrame"].document.location.href = "${ctx}/orgFunc/assignOrgToFuncPage.html"
					+"?funcId="+funcId;
			}else if(operateType=="assignUser"){
				parent.frames["userFrame"].document.location.href = "${ctx}/userFunc/assignUserToFuncPage.html"
					+"?funcId="+funcId;
			}
		}
	}
</script>
</head>
<body>
	<ui:Panel fit="true" border="false">
		<ul id="funcTree" class="easyui-tree" asyncEnable="true"  url="${ctx}/roleFunc/funcTreeJson.html" 
  				rootPId="0" simpleDataEnable="true" autoParam="['id']"
  				onClick="onFuncTreeClick"></ul>
	</ui:Panel>
</body>
</html>