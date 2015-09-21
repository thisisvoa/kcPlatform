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
	function doQuery(){
		var postData = {};
		postData.varName = $("#varName").val();
		postData.varKey = $("#varKey").val();
		$("#bpmDefVarList").grid("setGridParam", {postData:postData});
		$("#bpmDefVarList").trigger("reloadGrid");
	}
	
	function addBpmDefVar(){
		topWin.Dialog.open({
			Title : "新增流程变量",
			URL : '${ctx}/workflow/bpmDef/var/toAdd.html?defId={defId}',
			Width : 400,
			Height : 300,
			RefreshHandler : function(){
				$("#bpmDefVarList").trigger("reloadGrid");
			}});
	}
	
	function editBpmDefVar(id){
		topWin.Dialog.open({
			Title : "编辑流程变量",
			URL : '${ctx}/workflow/bpmDef/var/toUpdate.html?id='+id,
			Width : 400,
			Height : 300,
			RefreshHandler : function(){
				$("#bpmDefVarList").trigger("reloadGrid");
			}});
	}
	
	function viewBpmDefVar(id){
		topWin.Dialog.open({
			Title : "查看",
			URL : '${ctx}/workflow/bpmDef/var/toView.html?id='+id,
			Width : 400,
			Height : 300
			});
	}
	
	function delBpmDefVar(){
		var selectedIds = $("#bpmDefVarList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要删除的记录！");
			return;
		}
		var ids = selectedIds.join(",");
		MessageUtil.confirm("确定删除选择的记录吗？",function(){
			$.postc('${ctx}/workflow/bpmDef/var/deleteBpmDefVar.html', {ids:ids}, function(data){
				MessageUtil.show("删除记录成功！");
				$("#bpmDefVarList").trigger("reloadGrid");
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
		html += "<a id=\"edit\" plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-edit\" onclick=\"javascript:editBpmDefVar('"+rowd.varId+"')\">编辑</a>";
		return html;
	}
</script>
</head>
<body style="padding:1px">
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:33px;">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="addBtn" href="#" iconCls="icon-add" onclick="addBpmDefVar()">新增</a>
			<a id="removeBtn" href="#" iconCls="icon-remove" onclick="delBpmDefVar()">删除</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center" border="false" style="padding-top:1px">
		<ui:Grid id="bpmDefVarList" datatype="json" shrinkToFit="false" fit="true" viewrecords="false" pageable="false"
				url="${ctx}/workflow/bpmDef/var/list.html" multiselect="true" rownumbers="false"
				rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
				<ui:Column name="varId" key="true" hidden="true"></ui:Column>
				<ui:Column header="变量名称" name="varName" width="150"></ui:Column>
				<ui:Column header="变量Key" name="varKey" width="150"></ui:Column>
				<ui:Column header="操作" formatter="operaFormatter" width="80" fixed="true"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>