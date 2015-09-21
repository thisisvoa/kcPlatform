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
<title>单位管理</title>
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
	function refreshParentNode(orgId){
		var treeObj = $("#orgTree").tree("getZTreeObj");
		var treeNode = treeObj.getNodeByParam("id", orgId);
		if(treeNode!=null){
			var parentNode = treeNode.getParentNode();
			treeObj.reAsyncChildNodes(parentNode, "refresh");
		}
	}
	
	function refreshNode(orgId){
		var treeObj = $("#orgTree").tree("getZTreeObj");
		var treeNode = treeObj.getNodeByParam("id", orgId);
		treeObj.reAsyncChildNodes(treeNode, "refresh");
	}
	
	function cancelSelectedNode(){
		var treeObj = $("#orgTree").tree("getZTreeObj");
		treeObj.cancelSelectedNode();
	}
	
	function onOrgTreeClick(event, treeId, treeNode){
		if(treeNode.enable==false){
			return;
		}
		var id = treeNode.id;
		if (id == null || id == "") {
			MessageUtil.alter("请选择要操作的单位");
			return;
		}
		$("#orgInfoList").attr("src","${ctx}/orgCtl/toOrgInfoList.html?parentOrg="
			+ id + "&" + (new Date()).getTime());
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
			}
		}
	}
	
	$parsed(function(){
		$("#ws").layout("panel","center").panel("body").html("<iframe id=\"orgInfoList\" name=\"orgInfoList\" style=\"width:100%;height:100%;\" "+
			"src=\"${ctx}/orgCtl/toOrgInfoList.html\" frameBorder=0 style=\"padding:0px;margin:0px\" ></iframe>");
	});
</script>
</head>
<body>
	<ui:LayoutContainer id="ws" fit="true">
		<ui:Layout region="west" style="overflow:auto;width:230px" split="true">
			<ul id="orgTree" class="easyui-tree" asyncEnable="true"  url="${ctx}/orgCtl/asynOrgTree.html" 
  				rootPId="0" simpleDataEnable="true" autoParam="['id']" setting = "setting"
  				onClick="onOrgTreeClick" dataFilter="dataFilter" onNodeCreated="onNodeCreated"></ul>
		</ui:Layout>
		<ui:Layout region="center" style="overflow:hidden">
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>