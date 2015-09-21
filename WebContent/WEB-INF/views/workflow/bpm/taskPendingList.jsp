<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="ui" uri="/tags/ui" %>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
<title>任务管理列表</title>
<script type="text/javascript">
var topWin = FrameHelper.getTop();
function query(){
	var postData = {};
	postData.processName = $("#processName").val();
	postData.subject = $("#subject").val();
	postData.creator = $("#creator").val();
	postData.taskStatus = $("#taskStatus").combobox("getValue");
	postData.description = $("#description").combobox("getValue");
	postData.hasRead = $("#hasRead").combobox("getValue");
	postData.startTime = $("#startTime").val();
	postData.endTime = $("#endTime").val();
	$("#taskList").grid("setGridParam", {postData:postData});
	$("#taskList").trigger("reloadGrid");
}

function batOperator(){
	var selectedIds = $("#taskList").grid("getGridParam", "selarrrow");
	if (selectedIds.length == 0){
		MessageUtil.alert("请选择待审批的事项！");
		return;
	}
	for(var i=0;i<selectedIds.length;i++){
		var id = selectedIds[i];
		var rowd = $("#taskList").grid("getRowData", id);
		if(rowd.description==15||rowd.description==38){
			MessageUtil.alert("【"+rowd.subject+"】是沟通意见或者流转意见不能进行批量审批！");
			return;
		}
		if(rowd.taskStatus=='5' || rowd.taskStatus=='6'){
			MessageUtil.alert("【"+rowd.subject+"】是属于撤销或驳回任务不能进行批量审批！");
			return;
		}
	}
	topWin.Dialog.open({
		Title : "流程任务批量审批",
		URL : "${ctx}/workflow/task/toBatComplete.html?taskIds="+selectedIds.join(","),
		Width : 450,
		Height : 200,
		RefreshHandler:function(){
			$("#taskList").trigger("reloadGrid");
		}
	});
}
function executeTask(taskId){
	var rowd = $("#taskList").grid("getRowData", taskId);
	var height = $(topWin.document.body).height();
	var width = $(topWin.document.body).width();
	topWin.Dialog.open({
		Title : "流程任务-【"+rowd.name+"】执行",
		URL : "${ctx}/workflow/task/toStart.html?taskId="+taskId,
		Width : width,
		Height : height,
		RefreshHandler:function(){
			$("#taskList").trigger("reloadGrid");
		}
	});
}

function dateFormatter(cellVal, options, rowd) {
	if (cellVal != null) {
		var d = new Date(cellVal);
		return DateFormat.format(d, "yyyy-MM-dd hh:mm:ss");
	} else {
		return "";
	}
}

function subjectFormatter(cellVal, options, rowd) {
	if(rowd.hasRead=='0'){
		return "<a href='#' style='color:blue;font-weight:bold' onclick=\"executeTask('"+rowd.id+"')\"><image src='${ctx}/ui/css/icon/images/message-close.gif' style='border:none;margin:-5px 3px' />"+rowd.subject+"</a>";	
	}else{
		return "<a href='#' style='color:blue' onclick=\"executeTask('"+rowd.id+"')\"><image src='${ctx}/ui/css/icon/images/message-open.gif' style='border:none;margin:-5px 3px'/>"+rowd.subject+"</a>";
	}
	
}

function userNameFormater(cellVal, options, rowd) {
	return "<img src='${ctx}/ui/css/icon/images/user.gif' style='margin:-5px 3px'><a href='#' style='color:blue' onclick=\"showUser('"+rowd.creatorId+"')\">"+rowd.creator+"</a>";
}

function pendingTypeFormater(cellVal, options, rowd) {
	if(rowd.description=='-1'){
		return "<span class=\"green\">待办</span>";
	}else if(rowd.description=='21'){
		return "<span class=\"brown\">转办</span>";
	}else if(rowd.description=='15'){
		return "<span class=\"orange\">沟通意见</span>";
	}else if(rowd.description=='26'){
		return "<span class=\"brown\">代理</span>";
	}else if(rowd.description=='38'){
		return "<span class=\"red\">流转意见</span>";
	}
	return "";
}
function statusFormater(cellVal, options, rowd) {
	if(rowd.taskStatus=='1'){
		return "<span class=\"green\">审批中</span>";
	}else if(rowd.taskStatus=='5'){
		return "<span class=\"red\">已撤销</span>";
	}else if(rowd.taskStatus=='6'){
		return "<span class=\"red\">已驳回</span>";
	}
	return "";
}
function showUser(userId){
	topWin.Dialog.open({
			URL : "${ctx}/userCtl/toViewUser.html?id="+userId,
			Width: 500,
			Height : 540,
			Title : "用户详细信息"
		});
}
</script>
</head>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:130px" border="false">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="queryBtn" href="#" iconCls="icon-search" onclick="query()">查询</a>
			<a id="runBtn" href="#" iconCls="icon-save" onclick="batOperator()">批量审批</a>
		</div>
		<table class="formTable">
			<tr>
				<td class="label" style="width: 100px">事项名称：</td>
				<td style="width: 130px">
					<input type="text" class="easyui-textinput" id="subject" name="subject" watermark="支持模糊查询" />
				</td>
				<td class="label" style="width: 100px">流程名称：</td>
				<td style="width: 130px">
					<input type="text" class="easyui-textinput" id="processName" name="processName" watermark="支持模糊查询" />
				</td>
				<td class="label" style="width: 100px">创建人：</td>
				<td>
					<input type="text" class="easyui-textinput" id="creator" name="creator" watermark="支持模糊查询"/>
				</td>
			</tr>
			<tr>
				<td class="label" style="width: 100px">待办类型：</td>
				<td style="width: 130px">
					<select id="description" name="description" class="easyui-combobox" dropdownHeight="100">
						<option value="">--所有--</option>
						<option value="-1">待办</option>
						<option value="21">转办</option>
						<option value="26">代理</option>
					</select>
				</td>
				<td class="label" style="width: 100px">状态：</td>
				<td style="width: 130px">
					<select id="taskStatus" name="taskStatus" class="easyui-combobox" dropdownHeight="100">
						<option value="">--所有--</option>
						<option value="1">审批中</option>
						<option value="5">已撤销</option>
						<option value="6">已驳回</option>
					</select>
				</td>
				<td class="label" style="width: 100px">阅读状态：</td>
				<td>
					<select id="hasRead" name="hasRead" class="easyui-combobox" dropdownHeight="75">
						<option value="">--所有--</option>
						<option value="0">未读</option>
						<option value="1">已读</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="label" style="width: 100px">创建时间：</td>
				<td colspan="5">
					<table>
						<tr>
							<td><input class="easyui-datePicker" id="startTime" name="startTime" dateFmt="yyyy-MM-dd"></td>
							<td>至</td>
							<td><input id="endTime" name="endTime" type="text" class="easyui-datePicker" dateFmt="yyyy-MM-dd" /></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</ui:Layout>
	<ui:Layout region="center">
		<ui:Grid id="taskList" datatype="json" shrinkToFit="false" fit="true" viewrecords="true" pageable="true" 
			url="${ctx}/workflow/task/pendingList.html" multiselect="true"
			rowNum="20" rowList="[10,20,50]">
			<ui:Column name="id" key="true" hidden="true"></ui:Column>
			<ui:Column name="hasRead" hidden="true"></ui:Column>
			<ui:Column name="name" hidden="true"></ui:Column>
			<ui:Column name="taskStatus" hidden="true"></ui:Column>
			<ui:Column name="creatorId" hidden="true"></ui:Column>
			<ui:Column name="subject" hidden="true"></ui:Column>
			<ui:Column header="事项名称" width="250" formatter="subjectFormatter"></ui:Column>
			<ui:Column header="当前任务" name="name" width="150"></ui:Column>
			<ui:Column header="流程名称" name="processName" width="150"></ui:Column>
			<ui:Column header="创建人" name="creator" width="100" formatter="userNameFormater"></ui:Column>
			<ui:Column header="任务创建时间" name="createTime" width="130" formatter="dateFormatter"></ui:Column>
			<ui:Column header="状态" width="50" formatter="statusFormater" align="center"></ui:Column>
			<ui:Column header="待办类型" width="60" formatter="pendingTypeFormater"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>