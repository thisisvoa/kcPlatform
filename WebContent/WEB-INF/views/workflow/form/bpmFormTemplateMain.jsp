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
		postData.templateName = $("#templateName").val();
		postData.templateType = $("#templateType").combobox("getValue");
		$("#bpmFormTemplateList").grid("setGridParam", {postData:postData});
		$("#bpmFormTemplateList").trigger("reloadGrid");
	}
	
	function addBpmFormTemplate(){
		var height = $(topWin.document.body).height();
		var width = $(topWin.document.body).width();
		topWin.Dialog.open({
			Title : "新增表单模板",
			URL : '${ctx}/workflow/formTemplate/toAdd.html',
			Width : width,
			Height : height,
			RefreshHandler : function(){
				$("#bpmFormTemplateList").trigger("reloadGrid");
			}});
	}
	function edit(){
		var selectedIds = $("#bpmFormTemplateList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择待修改的记录！");
			return;
		}else if(selectedIds.length > 1){
			MessageUtil.alert("只能选择一条记录进行修改！");
		}else{
			editBpmFormTemplate(selectedIds[0]);
		}
	}
	function editBpmFormTemplate(id){
		var height = $(topWin.document.body).height();
		var width = $(topWin.document.body).width();
		var rowd = $("#bpmFormTemplateList").grid("getRowData", id);
		if(rowd.canEdit=='0'){
			MessageUtil.alert("该模板是系统模板不能编辑！");
			return;
		}
		topWin.Dialog.open({
			Title : "修改表单模板",
			URL : '${ctx}/workflow/formTemplate/toUpdate.html?id='+id,
			Width : width,
			Height : height,
			RefreshHandler : function(){
				$("#bpmFormTemplateList").trigger("reloadGrid");
			}});
	}
	
	function viewBpmFormTemplate(id){
		var height = $(topWin.document.body).height();
		var width = $(topWin.document.body).width();
		topWin.Dialog.open({
			Title : "查看表单模板",
			URL : '${ctx}/workflow/formTemplate/toView.html?id='+id,
			Width : width,
			Height : height
			});
	}
	
	function delBpmFormTemplate(){
		var selectedIds = $("#bpmFormTemplateList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要删除的记录！");
			return;
		}
		var ids = selectedIds.join(",");
		MessageUtil.confirm("确定删除选择的记录吗？",function(){
			for(var i=0;i<selectedIds.length;i++){
				var rowd = $("#bpmFormTemplateList").grid("getRowData", selectedIds[i]);
				if(rowd.canEdit=='0'){
					MessageUtil.alert("存在系统模板不能进行删除！");
					return;
				}
			}
			$.postc('${ctx}/workflow/formTemplate/delete.html', {ids:ids}, function(data){
				MessageUtil.show("删除记录成功！");
				$("#bpmFormTemplateList").trigger("reloadGrid");
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
		if(rowd.canEdit=='1'){
			html += "<a href='#' class='easyui-linkbutton' plain='true' style='margin:1px' iconCls='icon-edit' onclick=\"javascript:editBpmFormTemplate('"+rowd.templateId+"');\">修改</a>";	
		}else{
			html += "<a href='#' class='easyui-linkbutton' disabled='true' plain='true' style='margin:1px' iconCls='icon-edit' onclick=\"javascript:editBpmFormTemplate('"+rowd.templateId+"');\">修改</a>";
		}
		html += "<a href='#' class='easyui-linkbutton' plain='true' style='margin:1px' iconCls='icon-view' onclick=\"javascript:viewBpmFormTemplate('"+rowd.templateId+"')\">查看</a>";
		return html;
	}
</script>
</head>
<body style="padding:1px">
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:95px;" title="表单模板管理">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="query" href="#" iconCls="icon-search" onclick="doQuery()">查询</a>
			<a id="add" href="#" iconCls="icon-add" onclick="addBpmFormTemplate()">新增</a>
			<a id="edit" href="#" iconCls="icon-edit" onclick="edit()">修改</a>
			<a id="remove" href="#" iconCls="icon-remove" onclick="delBpmFormTemplate()">删除</a>
		</div>
		<form id="queryForm">
			<table class="formTable">
				<tr>
					<td class="label">
						模板名称：
					</td>
					<td style="width:150px;">
						<input type="text" class="easyui-textinput" id="templateName" name="templateName" />
					</td>
					<td class="label">
						模板类型：
					</td>
					<td>
						<select id="templateType" name="templateType" class="easyui-combobox" dropdownHeight="75">
							<option value="">--请选择--</option>
							<option value="1">主表模板</option>
							<option value="3">宏模板</option>
						</select>
					</td>
				</tr>
			</table>
		</form>
	</ui:Layout>
	<ui:Layout region="center" border="false" style="padding-top:1px">
		<ui:Grid id="bpmFormTemplateList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="true"
				url="${ctx}/workflow/formTemplate/list.html" multiselect="true" rownumbers="false"
				rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
				<ui:Column name="templateId" key="true" hidden="true"></ui:Column>
				<ui:Column header="别名" name="alias" width="90"></ui:Column>
				<ui:Column header="模板名称" name="templateName" width="150"></ui:Column>
				<ui:Column header="模板类型" name="templateType" width="90" edittype="select" formatter="'select'" editoptions="{value:\"1:主表模板;3:宏模板\"}"></ui:Column>
				<ui:Column header="模板来源" name="canEdit" width="90" edittype="select" formatter="'select'" editoptions="{value:\"0:系统模板;1:自定义模板\"}"></ui:Column>
				<ui:Column header="操作" formatter="operaFormatter" width="120" fixed="true"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>