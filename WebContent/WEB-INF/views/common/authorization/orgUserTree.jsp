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
	
	var setting = {
			view: {
				fontCss: function(treeId, node){
					if(node.enable==false){
						return {color:"#ccc"};	
					}
				}
			}
	};
	function onOrgUserTreeClick(event, treeId, treeNode){
		var type = treeNode.type;
		if(type=="user"){
			var userId = treeNode.userId;
			if(operateType=="assignFunc"){
				parent.frames["funcFrame"].document.location.href = "${ctx}/userFunc/assignFuncToUserPage.html"
					+"?userId="+userId;
			}else if(operateType=="assignRole"){
				parent.frames["roleFrame"].document.location.href = "${ctx}/userRole/assignRoleToUserPage.html"
					+"?userId="+userId;
			}
		}
	}
	
	function dataFilter(event, parentNode, childNodes){
		if(!childNodes) return null;
		for(var i=0;i<childNodes.length;i++){
			var id = childNodes[i].id;
			var type = childNodes[i].type;
			if(type=="org"){
				childNodes[i].icon = "${ctx}/ui/css/icon/images/user_group.gif";
			}else{
				childNodes[i].icon = "${ctx}/ui/css/icon/images/user.gif";
			}
		}
		return childNodes;
	}
	
	/**
	 *第一次加载时候展开第一层节点
	 */
	function onNodeCreated(event, treeId, treeNode){
		if(treeNode.pId=="0"){
			var treeObj = $("#orgUserTree").tree("getZTreeObj");
			if(treeNode.children==null){
				treeObj.reAsyncChildNodes(treeNode, "refresh");	
			}else{
			}
		}
	}
</script>
</head>
<body>
	<ui:Panel fit="true" border="false">
		<ul id="orgUserTree" class="easyui-tree" asyncEnable="true"  url="${ctx}/userFunc/orgUserTreeJson.html" 
  				rootPId="0" simpleDataEnable="true" autoParam="['id']" setting="setting"
  				onClick="onOrgUserTreeClick" dataFilter="dataFilter" onNodeCreated="onNodeCreated"></ul>
	</ui:Panel>
</body>
</html>