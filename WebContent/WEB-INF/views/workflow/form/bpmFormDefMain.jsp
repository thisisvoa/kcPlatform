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
	var data = [{id:'0',catalogName:"表单分类",isParent:true,icon:"${ctx}/ui/css/icon/images/home.gif"}]
	$parsed(function(){
		var treeObj = $("#catalogTree").tree("getZTreeObj");
		var node = treeObj.getNodeByParam("id", "0");
		treeObj.selectNode(node);
		treeObj.reAsyncChildNodes(node, "refresh");
	});
	function OnRightClick(event, treeId, treeNode) {
		if(treeNode==null){
			return;
		}
		if(treeNode.id=="0"){
			$("#edit").hide();
			$("#remove").hide();
		}else{
			$("#edit").show();
			$("#remove").show();
		}
		$('#menu').data("currentNode", treeNode);
		$('#menu').menu('show', {
			left: event.pageX,
			top: event.pageY
		});
	}
	
	function addBpmCatalog(){
		var node = $('#menu').data("currentNode");
		topWin.Dialog.open({
			Title : "新增表单分类",
			URL : '${ctx}/workflow/bpmCatalog/toAdd.html?catalogType=2&parentId='+node.id,
			Width : 500,
			Height : 250,
			RefreshHandler : function(){
				var treeObj = $("#catalogTree").tree("getZTreeObj");
				var node = treeObj.getNodeByParam("id", "0");
				treeObj.selectNode(node);
				treeObj.reAsyncChildNodes(node, "refresh");
			}});
	}
	
	function editBpmCatalog(){
		var node = $('#menu').data("currentNode");
		topWin.Dialog.open({
			Title : "修改表单分类",
			URL : '${ctx}/workflow/bpmCatalog/toUpdate.html?id='+node.id,
			Width : 500,
			Height : 250,
			RefreshHandler : function(){
				var treeObj = $("#catalogTree").tree("getZTreeObj");
				var node = treeObj.getNodeByParam("id", "0");
				treeObj.selectNode(node);
				treeObj.reAsyncChildNodes(node, "refresh");
			}});
	}
	
	function removeBpmCatalog(){
		MessageUtil.confirm("确定删除该节点吗？",function(){
			var node = $('#menu').data("currentNode");
			$.postc("${ctx}/workflow/bpmCatalog/delete.html", {id:node.id}, function(data){
				MessageUtil.show("删除成功!");
				var treeObj = $("#catalogTree").tree("getZTreeObj");
				var node = treeObj.getNodeByParam("id", "0");
				treeObj.selectNode(node);
				treeObj.reAsyncChildNodes(node, "refresh");
			});
		});
	}
	
	function expandTree(){
		var treeObj = $("#catalogTree").tree("getZTreeObj");
		treeObj.expandAll(true);
	}
	
	function closeTree(){
		var treeObj = $("#catalogTree").tree("getZTreeObj");
		treeObj.expandAll(false);
	}
	
	function refresh(){
		var treeObj = $("#catalogTree").tree("getZTreeObj");
		treeObj.reAsyncChildNodes(null, "refresh");
	}
	
	function onAsyncSuccess(event, treeId, treeNode, msg) {
		var treeObj = $("#catalogTree").tree("getZTreeObj");
		treeObj.expandAll(true);
	}
	
	function onClick(event, treeId, treeNode) {
		var id = treeNode.id=='0'?"":treeNode.id;
		$("#listFrame").attr("src","${ctx}/workflow/formDef/toList.html?catalogId="+id);
	}
</script>
</head>
<body>
	<ui:LayoutContainer fit="true" id="ws">
		<ui:Layout region="west" style="width:200px" split="true" title="表单分类管理">
			<div id="toolbar" class="easyui-toolbar" style="width:100%;">
				<a id="refresh" href="#" iconCls="icon-reload" onclick="refresh()">刷新</a>
				<a id="expand" href="#" iconCls="icon-expand" onclick="expandTree()">展开</a>
				<a id="close" href="#" iconCls="icon-close" onclick="closeTree()">缩起</a>
			</div>
			<ul id="catalogTree" class="easyui-tree" 
				asyncEnable="true" url="${ctx}/workflow/bpmCatalog/list.html?catalogType=2"
				idKey="id" pIdKey="parentId" nameKey="catalogName" data="data" 
  				rootPId="0" simpleDataEnable="true"
  				OnRightClick="OnRightClick" onAsyncSuccess="onAsyncSuccess" onClick="onClick"></ul>
		</ui:Layout>
		<ui:Layout region="center" title="表单管理" style="overflow:hidden;">
			<iframe id="listFrame" name="listFrame" style="width:100%;height:100%;"
						src="${ctx}/workflow/formDef/toList.html" frameBorder="0"></iframe>
		</ui:Layout>
	</ui:LayoutContainer>
	
	<div id="menu" class="easyui-menu" style="width:120px;">
		<div id="add" iconCls="icon-add" onclick="addBpmCatalog()">添加分类</div>
		<div id="edit" iconCls="icon-edit" onclick="editBpmCatalog()">编辑分类</div>
		<div id="remove" iconCls="icon-remove" onclick="removeBpmCatalog()">删除分类</div>
	</div>
</body>
</html>