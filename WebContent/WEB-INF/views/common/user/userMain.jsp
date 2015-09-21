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
<title>组织架构管理>>用户信息管理</title>
<script type="text/javascript">
	var topWin = FrameHelper.getTop();
	var setting = {
			view: {
				fontCss: function(treeId, node){
					if(node.enable==false){
						return {color:"#ccc"};	
					}
				}
			}
	};
	function cancelSelectedNode(){
		var treeObj = $("#orgTree").tree("getZTreeObj");
		treeObj.cancelSelectedNode();
	}
	
	function onOrgTreeClick(event, treeId, treeNode){
		if(treeNode.enable==false){
			return;
		}
		var orgId = treeNode.id;
		$("#horgId").val(orgId);
		$("#orgUserList").attr("src","${ctx}/userCtl/toOrgUserList.html?orgId="
			+ orgId + "&" + (new Date()).getTime());
		
	}
	
	function dataFilter(event, parentNode, childNodes){
		if(!childNodes) return null;
		for(var i=0;i<childNodes.length;i++){
			childNodes[i].icon = "${ctx}/ui/css/icon/images/user_group.gif";
		}
		return childNodes;
	}
	
	/**
	 *第一次加载时候展开第一层节点
	 */
	function onNodeCreated(event, treeId, treeNode){
		if(treeNode.pId=="0"){
			var treeObj = $("#orgTree").tree("getZTreeObj");
			if(treeNode.children==null){
				treeObj.reAsyncChildNodes(treeNode, "refresh");	
			}else{
				//treeObj.expandAll(true);
			}
		}
	}
	
	$parsed(function(){
		$("#ws").layout("panel","center").panel("body").html("<iframe id=\"orgUserList\" name=\"orgUserList\" style=\"width:100%;height:100%;\" "+
			"src=\"${ctx}/userCtl/toOrgUserList.html\" frameBorder=0 style=\"padding:0px;margin:0px\" ></iframe>");
	});
</script>
</head>
<body>
	<ui:LayoutContainer id="ws" fit="true">
		<ui:Layout region="west" style="overflow:auto;width:230px;padding:1px" split="true">
				<ul id="orgTree" class="easyui-tree" asyncEnable="true"  url="${ctx}/orgCtl/asynOrgTree.html" 
	 				rootPId="0" simpleDataEnable="true" autoParam="['id']"
	 				onClick="onOrgTreeClick" dataFilter="dataFilter" onNodeCreated="onNodeCreated" setting="setting"></ul>
		</ui:Layout>
		<ui:Layout region="center" style="overflow:hidden">
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>