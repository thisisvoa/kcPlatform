<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="/tags/ui"%>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
<script type="text/javascript">
	var topWin = FrameHelper.getTop();
	var data = [{id:'0',catalogName:"流程分类",isParent:true,icon:"${ctx}/ui/css/icon/images/home.gif"}]
	$parsed(function(){
		var treeObj = $("#bpmCatalogTree").tree("getZTreeObj");
		var node = treeObj.getNodeByParam("id", "0");
		treeObj.selectNode(node);
		treeObj.reAsyncChildNodes(node, "refresh");
	});
	
	function expandTree(){
		var treeObj = $("#bpmCatalogTree").tree("getZTreeObj");
		treeObj.expandAll(true);
	}
	
	function closeTree(){
		var treeObj = $("#bpmCatalogTree").tree("getZTreeObj");
		treeObj.expandAll(false);
	}
	
	function refresh(){
		var treeObj = $("#bpmCatalogTree").tree("getZTreeObj");
		var node = treeObj.getNodeByParam("id", "0");
		treeObj.reAsyncChildNodes(node, "refresh");
	}
	
	function onAsyncSuccess(event, treeId, treeNode, msg) {
		var treeObj = $("#bpmCatalogTree").tree("getZTreeObj");
		treeObj.expandAll(true);
	}
	
	function onClick(event, treeId, treeNode) {
		var id = treeNode.id=='0'?"":treeNode.id;
		$("#processDefFrame").attr("src","${ctx}/workflow/bpmDef/tolist.html?typeId="+id);
	}
	$parsed(function(){
		$("#ws").layout("panel","center").panel("body").html("<iframe id=\"processDefFrame\" name=\"processDefFrame\" style=\"width:100%;height:100%;\" "+
			"src=\"${ctx}/workflow/bpmDef/toMylist.html\" frameBorder=0 style=\"padding:0px;margin:0px\" ></iframe>");
	});
</script>
</head>
<body>
	<ui:LayoutContainer fit="true" id="ws">
		<ui:Layout region="west" style="width:200px" split="true" title="流程分类管理">
			<div id="toolbar" class="easyui-toolbar" style="width:100%;">
				<a id="refresh" href="#" iconCls="icon-reload" onclick="refresh()">刷新</a>
				<a id="expand" href="#" iconCls="icon-expand" onclick="expandTree()">展开</a>
				<a id="close" href="#" iconCls="icon-close" onclick="closeTree()">缩起</a>
			</div>
			<ul id="bpmCatalogTree" class="easyui-tree" 
				asyncEnable="true" url="${ctx}/workflow/bpmCatalog/list.html?catalogType=1"
				idKey="id" pIdKey="parentId" nameKey="catalogName" data="data" 
  				rootPId="0" simpleDataEnable="true" onAsyncSuccess="onAsyncSuccess" onClick="onClick"></ul>
		</ui:Layout>
		<ui:Layout region="center" title="新建流程">
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>