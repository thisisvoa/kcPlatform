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
<title></title>
<script type="text/javascript">
	var topWin = FrameHelper.getTop();
	
	function init(){
		MessageUtil.confirm("确定初始化全部操作按钮吗？",function(){
			$.postc('${ctx}/workflow/bpmDef/nodeButton/initNodeButtons.html?defId=${defId}', null, function(data){
				MessageUtil.show("操作按钮初始化成功！");
				$("#bpmNodeButtonList").trigger("reloadGrid");
			});
		});
	}
	
	function remove(){
		MessageUtil.confirm("确定删除所有按钮设置？",function(){
			$.postc('${ctx}/workflow/bpmDef/nodeButton/deleteByDefId.html?defId=${defId}', null, function(data){
				MessageUtil.show("删除成功！");
				$("#bpmNodeButtonList").trigger("reloadGrid");
			});
		});
	}
	
	function editBpmNodeButton(id){
		var rowd = $("#bpmNodeButtonList").grid("getRowData",id);
		var nodeId = rowd.nodeId;
		var defId = rowd.defId;
		var nodeType = rowd.nodeType;
		if(nodeId=="_startNode_"){
			nodeId = "";
		}
		topWin.Dialog.open({
			Title : "编辑任务节点按钮",
			URL : '${ctx}/workflow/bpmDef/nodeButton/toBtnList.html?nodeId='+nodeId+"&defId="+defId+"&nodeType="+nodeType,
			Width : 850,
			Height : 550,
			RefreshHandler : function(){
				$("#bpmNodeButtonList").trigger("reloadGrid");
			}});
	}
	
	function initNode(id){
		var rowd = $("#bpmNodeButtonList").grid("getRowData",id);
		var nodeId = rowd.nodeId;
		var defId = rowd.defId;
		var nodeType = rowd.nodeType;
		MessageUtil.confirm("确定初始化该节点操作按钮吗？",function(){
			$.postc('${ctx}/workflow/bpmDef/nodeButton/initNodeButton.html', {nodeId:nodeId,defId:defId,nodeType:nodeType}, function(data){
				MessageUtil.show("操作按钮初始化成功！");
				$("#bpmNodeButtonList").trigger("reloadGrid");
			});
		});
	}
	
	function delBpmNodeButton(){
		var selectedIds = $("#bpmNodeButtonList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要删除的记录！");
			return;
		}
		var ids = selectedIds.join(",");
		MessageUtil.confirm("确定删除选择的记录吗？",function(){
			$.postc('${ctx}/bpmNodeButton/deleteBpmNodeButton', {ids:ids}, function(data){
				MessageUtil.show("删除记录成功！");
				$("#bpmNodeButtonList").trigger("reloadGrid");
			});
		});
	}
	

	function onGridComplete(){
		using("linkbutton", function(){
			$(".easyui-linkbutton").linkbutton();
		});
	}
	
	function operaFormatter(cellVal,options,rowd){
		var html = "";
		html += "<a id=\"edit\" plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-edit\" onclick=\"editBpmNodeButton('"+rowd.nodeId+"')\">编辑</a>";
		html += "<a id=\"init\" plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-start\" onclick=\"initNode('"+rowd.nodeId+"')\">初始化</a>";
		html += "<a id=\"remove\" plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-remove\" onclick=\"remove('"+rowd.nodeId+"')\">删除</a>";
		return html;
	}
</script>
</head>
<body style="padding:1px">
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:33px;" border="false">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="saveBtn" href="#" iconCls="icon-start" onclick="init()">初始化全部按钮</a>
			<a id="remove" href="#" iconCls="icon-remove" onclick="remove()">清除按钮配置</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center" border="false" style="padding-top:1px">
		<ui:Grid id="bpmNodeButtonList" datatype="json" shrinkToFit="false" fit="true" viewrecords="false" pageable="false"
				url="${ctx}/workflow/bpmDef/nodeButton/nodeBtnList.html?defId=${defId}" multiselect="true" rownumbers="false"
				rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
				<ui:Column name="nodeId" key="true" hidden="true"></ui:Column>
				<ui:Column name="defId" hidden="true"></ui:Column>
				<ui:Column name="nodeType" hidden="true"></ui:Column>
				<ui:Column header="节点名" name="nodeName" width="90"></ui:Column>
				<ui:Column header="节点类型" name="nodeTypeName" width="60" ></ui:Column>
				<ui:Column header="操作按钮" name="btnDesc" width="300" ></ui:Column>
				<ui:Column header="管理" formatter="operaFormatter" width="180" fixed="true"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>