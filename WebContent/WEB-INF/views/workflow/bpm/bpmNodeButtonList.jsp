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
	var defId = "${defId}";
	var nodeId = "${nodeId}";
	var nodeType = "${nodeType}";
	var isUpdate = false;
	function doQuery(){
		var postData = {};
		$("#bpmNodeButtonList").grid("setGridParam", {postData:postData});
		$("#bpmNodeButtonList").trigger("reloadGrid");
	}
	
	function addBpmNodeButton(){
		topWin.Dialog.open({
			Title : "新增按钮",
			URL : '${ctx}/workflow/bpmDef/nodeButton/toAdd.html?defId=${defId}&nodeId=${nodeId}&nodeType=${nodeType}',
			Width : 800,
			Height : 500,
			RefreshHandler : function(){
				$("#bpmNodeButtonList").trigger("reloadGrid");
				parentDialog.markUpdated();
			}});
	}
	
	function editBpmNodeButton(id){
		topWin.Dialog.open({
			Title : "修改按钮",
			URL : '${ctx}/workflow/bpmDef/nodeButton/toUpdate.html?id='+id,
			Width : 800,
			Height : 500,
			RefreshHandler : function(){
				$("#bpmNodeButtonList").trigger("reloadGrid");
				parentDialog.markUpdated();
			}});
	}
	
	function saveSn(){
		if(isUpdate == false){
			MessageUtil.confirm("按钮排序没有变更！");
			return;
		}
		MessageUtil.confirm("确定保存设置排序吗？",function(){
			var btnList = $("#bpmNodeButtonList").grid("getRowData");
			var postd = [];
			for(var i=0;i<btnList.length;i++){
				var d = {btnId:btnList[i].btnId, sn:(i+1)};
				postd.push(d);
			}
			$.postc("${ctx}/workflow/bpmDef/nodeButton/saveSn.html",{btnJson:JSON.stringify(postd)},function(){
				MessageUtil.show("序号保存成功！");
				$("#bpmNodeButtonList").trigger("reloadGrid");
				parentDialog.markUpdated();
			});
		});
	}
	
	function initBtn(){
		MessageUtil.confirm("初始化该节点操作按钮，将会删除原来设置，确定初始化？",function(){
			$.postc('${ctx}/workflow/bpmDef/nodeButton/initNodeButton.html?defId=${defId}&nodeId=${nodeId}&nodeType=${nodeType}', null, function(data){
				MessageUtil.show("操作按钮初始化成功！");
				$("#bpmNodeButtonList").trigger("reloadGrid");
				parentDialog.markUpdated();
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
			$.postc('${ctx}/workflow/bpmDef/nodeButton/delete.html', {ids:ids}, function(data){
				MessageUtil.show("删除记录成功！");
				$("#bpmNodeButtonList").trigger("reloadGrid");
				parentDialog.markUpdated();
			});
		});
	}
	
	function up(){
		var selectedIds = $("#bpmNodeButtonList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要的移动的记录！");
			return;
		}
		if(selectedIds.length>1){
			MessageUtil.alert("只能选择一条记录进行移动 ！");
			return;
		}
		var rowInd = $("#bpmNodeButtonList").grid("getInd", selectedIds[0]);
		if(rowInd==1){
			return;
		}else{
			var dataIds = $("#bpmNodeButtonList").grid("getDataIDs");
			var rowd = $("#bpmNodeButtonList").grid("getRowData", selectedIds[0]);
			var rowid = rowd.btnId;
			var preRowId =  dataIds[rowInd-2];
			$("#bpmNodeButtonList").grid("delRowData", rowid);
			$("#bpmNodeButtonList").grid("addRowData", rowid, rowd, "before", preRowId);
			$("#bpmNodeButtonList").grid("setSelection",rowid);
			isUpdate = true;
		}
		
	}
	
	function down(){
		var selectedIds = $("#bpmNodeButtonList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要的移动的记录！");
			return;
		}
		if(selectedIds.length>1){
			MessageUtil.alert("只能选择一条记录进行移动 ！");
			return;
		}
		var rowInd = $("#bpmNodeButtonList").grid("getInd", selectedIds[0]);
		var dataIds = $("#bpmNodeButtonList").grid("getDataIDs");
		if(rowInd==dataIds.length){
			return;
		}else{
			var rowd = $("#bpmNodeButtonList").grid("getRowData", selectedIds[0]);
			var rowid = rowd.btnId;
			var afterRowId =  dataIds[rowInd];
			$("#bpmNodeButtonList").grid("delRowData", rowid);
			$("#bpmNodeButtonList").grid("addRowData", rowid, rowd, "after", afterRowId);
			$("#bpmNodeButtonList").grid("setSelection",rowid);
			isUpdate = true;
		}
	}

	function onGridComplete(){
		using("linkbutton", function(){
			$(".easyui-linkbutton").linkbutton();
		});
	}
	
	function operaFormatter(cellVal,options,rowd){
		var html = "";
		html += "<a id=\"edit\" plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-edit\" onclick=\"editBpmNodeButton('"+rowd.btnId+"')\">编辑</a>";
		return html;
	}
</script>
</head>
<body style="padding:1px">
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:33px;" border="false">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="saveBtn" href="#" iconCls="icon-add" onclick="addBpmNodeButton()">新增</a>
			<a id="saveSnBtn" href="#" iconCls="icon-save" onclick="saveSn()">保存排序</a>
			<a id="initBtn" href="#" iconCls="icon-start" onclick="initBtn()">初始化按钮</a>
			<a id="removeBtn" href="#" iconCls="icon-remove" onclick="delBpmNodeButton()">删除</a>
			<a id="up" href="#" iconCls="icon-moveup" onclick="up()">上移</a>
			<a id="down" href="#" iconCls="icon-movedown" onclick="down()">下移</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center" border="false" style="padding-top:1px">
		<ui:Grid id="bpmNodeButtonList" datatype="json" shrinkToFit="false" fit="true" viewrecords="false" pageable="false"
				url="${ctx}/workflow/bpmDef/nodeButton/list.html?defId=${defId}&nodeId=${nodeId}&nodeType=${nodeType}" multiselect="true" rownumbers="false"
				rowNum="9999" rowList="[10,20,50]" gridComplete="onGridComplete">
				<ui:Column name="btnId" key="true" hidden="true"></ui:Column>
				<ui:Column header="按钮名称" name="btnName" width="150"></ui:Column>
				<ui:Column header="操作类型" name="type" width="150" edittype="select" formatter="'select'" align="center" editoptions="{value:\"${btnTypesEditoptions}\"}"></ui:Column>
				<ui:Column header="排序号" name="sn" width="60" align="center"></ui:Column>
				<ui:Column header="管理" formatter="operaFormatter" width="120" fixed="true"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>