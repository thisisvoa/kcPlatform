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
		postData.tableName = $("#tableName").val();
		postData.tableDesc = $("#tableDesc").val();
		postData.isMain = $("#isMain").combobox("getValue");
		$("#bpmFormTableList").grid("setGridParam", {postData:postData});
		$("#bpmFormTableList").trigger("reloadGrid");
	}
	
	function addBpmFormTable(){
		$("#ws").hide();
		$("#tableEdit").attr("src","${ctx}/workflow/formTable/toAdd.html");
		$("#config").show();
	}
	
	function editBpmFormTable(id){
		$("#ws").hide();
		$("#tableEdit").attr("src","${ctx}/workflow/formTable/toUpdate.html?id="+id);
		$("#config").show();
	}
	
	function viewBpmFormTable(id){
		$("#ws").hide();
		$("#tableEdit").attr("src","${ctx}/workflow/formTable/toView.html?id="+id);
		$("#config").show();
	}
	
	function delBpmFormTable(){
		var selectedIds = $("#bpmFormTableList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要删除的业务表！");
			return;
		}
		var ids = selectedIds.join(",");
		MessageUtil.confirm("确定删除选择的业务表及其列信息吗？",function(){
			$.postc('${ctx}/workflow/formTable/delete.html', {ids:ids}, function(data){
				MessageUtil.show("删除成功！");
				$("#bpmFormTableList").trigger("reloadGrid");
			});
		});
	}
	

	function onGridComplete(){
		using("button", function(){
			$(".easyui-linkbutton").linkbutton();
		});
	}
	
	function operaFormatter(cellVal,options,rowd){
		var html = "";
		html += "<a href='#' class='easyui-linkbutton' style='margin:1px' plain='true' iconCls='icon-edit' onclick=\"javascript:editBpmFormTable('"+rowd.tableId+"')\" >编辑</a>";
		html += "<a href='#' class='easyui-linkbutton' style='margin:1px' plain='true' iconCls='icon-view' onclick=\"javascript:viewBpmFormTable('"+rowd.tableId+"')\" >明细</a>";
		return html;
	}
	
	function back(){
		$("#ws").show();
		$("#config").hide();
	}
	
	function saveSuccess(){
		back();
		MessageUtil.show("保存成功！");
		$("#bpmFormTableList").trigger("reloadGrid");
	}
</script>
</head>
<body style="padding:1px">
<ui:LayoutContainer fit="true" id="ws">
	<ui:Layout region="north" style="height:95px;" title="自定义表管理">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="query" href="#" iconCls="icon-search" onclick="doQuery()">查询</a>
			<a id="add" href="#" iconCls="icon-add" onclick="addBpmFormTable()">新增</a>
			<a id="remove" href="#" iconCls="icon-remove" onclick="delBpmFormTable()">删除</a>
			<!-- <a id="import" href="#" iconCls="icon-import" onclick="deployProcess()">导入</a> -->
			<!-- <a id="import" href="#" iconCls="icon-export" onclick="deployProcess()">导出</a> -->
		</div>
		<form id="queryForm">
			<table class="formTable">
				<tr>
					<td class="label">
						表名：
					</td>
					<td style="width:150px">
						<input type="text" class="easyui-textinput" id="tableName" name="tableName" />
					</td>
					<td class="label">
						表描述：
					</td>
					<td style="width:150px">
						<input type="text" class="easyui-textinput" id="tableDesc" name="tableDesc" />
					</td>
					<td class="label">
						是否主表：
					</td>
					<td>
						<select id="isMain" name="isMain" class="easyui-combobox" dropdownHeight="75" style="width:80px">
							<option value="">全部</option>
							<option value="1">主表</option>
							<option value="0">子表</option>
						</select>
					</td>
				</tr>
				<tr>
				</tr>
			</table>
		</form>
	</ui:Layout>
	<ui:Layout region="center" border="false" style="padding-top:1px">
		<ui:Grid id="bpmFormTableList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="true"
				url="${ctx}/workflow/formTable/list.html" multiselect="true" rownumbers="false"
				rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
				<ui:Column name="tableId" key="true" hidden="true"></ui:Column>
				<ui:Column header="表名" name="tableName" width="90"></ui:Column>
				<ui:Column header="描述" name="tableDesc" width="90"></ui:Column>
				<ui:Column header="属性" name="isMain" width="90" edittype="select" formatter="'select'" align="center" editoptions="{value:\"0:从表;1:主表\"}" fixed="true"></ui:Column>
				<ui:Column header="版本号" name="versionNo" width="90" fixed="true" align="center"></ui:Column>
				<ui:Column header="操作" formatter="operaFormatter" width="120" fixed="true"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
<ui:LayoutContainer fit="true" id="config">
	<ui:Layout region="center" border="false" style="overflow:hidden">
		<iframe id="tableEdit" name="tableEdit" src="" style="width:100%;height:100%;" frameborder="0"></iframe>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>