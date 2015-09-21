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
<title>流程实例列表</title>
<script type="text/javascript">
var topWin = FrameHelper.getTop();
function query(){
	var postData = {};
	postData.processName = $("#processName").val();
	postData.subject = $("#subject").val();
	postData.status = $("#status").combobox("getValue");
	postData.startTime = $("#startTime").val();
	postData.endTime = $("#endTime").val();
	$("#processRunList").grid("setGridParam", {
		postData : postData
	});
	$("#processRunList").trigger("reloadGrid");
}
/**
 * 明细
 */
function showDetail(){
	var selectedIds = $("#processRunList").grid("getGridParam", "selarrrow");
	if (selectedIds.length == 0){
		MessageUtil.alert("请选择要查看明细的流程实例！");
		return;
	}else if(selectedIds.length > 1){
		MessageUtil.alert("只能选择一条流程实例查看明细！");
	}else{
		detail(selectedIds[0]);
	}
}
function detail(id){
	var height = $(topWin.document.body).height();
	var width = $(topWin.document.body).width();
	topWin.Dialog.open({
		URL : "${ctx}/workflow/bpm/processRun/detail.html?runId="+id,
		ShowCloseButton : true,
		Width: width,
		Height : height,
		Title : "流程实例明细信息"
	}); 
}

function startFlow(runId){
	var height = $(topWin.document.body).height();
	var width = $(topWin.document.body).width();
	topWin.Dialog.open({
		Title : "启动流程",
		URL : '${ctx}/workflow/task/startFlowForm.html?runId='+runId,
		Width : width,
		Height : height,
		RefreshHandler:function(){
			$("#processRunList").trigger("reloadGrid");
		}
	});
}

function deleteProcessRuns(){
	var selectedIds = $("#processRunList").grid("getGridParam", "selarrrow");
	if (selectedIds.length == 0){
		MessageUtil.alert("请选择要删除的流程实例！");
		return;
	}else{
		remove(selectedIds.join(","));
	}
}

function remove(ids){
	MessageUtil.confirm("确定要删除选中的流程实例?",function(){
		$.postc("${ctx}/workflow/bpm/processRun/del.html?ids="+ids,null,function(data){
			MessageUtil.show("删除成功");
			$("#processRunList").trigger("reloadGrid");
		});	
	});
}
function subjectFormatter(cellVal, options, rowd) {
	return "<a href='#' style='color:blue;' onclick=\"detail('"+rowd.runId+"')\">"+cellVal+"</a>";
}

function dateFormatter(cellVal, options, rowd) {
	if (cellVal != null) {
		var d = new Date(cellVal);
		return DateFormat.format(d, "yyyy-MM-dd hh:mm:ss");
	} else {
		return "";
	}
}
function statusFormatter(cellVal, options, rowd) {
	switch(rowd.status){
		case 1:
			return "<span class='green'>正在运行</span>";
		case 2:
			return "<span class='red'>结束</span>";
		case 3:
			return "<span class='brown'>人工结束</span>";
		case 4:
			return "<span class='red'>草稿</span>";
		case 5:
			return "<span class='brown'>撤销</span>";
		case 6:
			return "<span class='brown'>驳回</span>";
		default:
			return "";
	}
	return "";
	
}

function operationFormatter(cellVal, options, rowd) {
	var html = "";
	html += "<a plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-view\" onclick=\"detail('"+rowd.runId+"')\">明细</a>";
	html += "<a plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-remove\" onclick=\"remove('"+rowd.runId+"')\">删除</a>";
	if(rowd.status==4){
		html += "<a plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-run\" onclick=\"startFlow('"+rowd.runId+"')\">启动</a>";	
	}
	return html;
}



function onGridComplete() {
	using("linkbutton", function() {
		$(".easyui-linkbutton").linkbutton();
	});
}

</script>
</head>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:105px;">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="query" href="#" iconCls="icon-search" onclick="query()">查询</a>
			<a id="view" href="#" iconCls="icon-view" onclick="showDetail()">明细</a>
			<a id="remove" href="#" iconCls="icon-remove" onclick="deleteProcessRuns()">删除</a>
		</div>
		<table class="formTable">
			<tr>
				<td class="label" style="width: 100px">流程名称：</td>
				<td style="width: 130px">
					<input type="text" class="easyui-textinput" id="processName" name="processName" watermark="支持模糊查询" />
				</td>
				<td class="label" style="width: 100px">事项名称：</td>
				<td style="width: 130px">
					<input type="text" class="easyui-textinput" id="subject" name="subject" watermark="支持模糊查询" />
				</td>
				<td class="label" style="width: 100px">状态：</td>
				<td>
					<select class="easyui-combobox" id="status" name="status">
						<option value="">--所有--</option>
						<option value="1" >正在运行</option>
						<option value="2" >结束</option>
						<option value="3" >人工结束</option>
						<option value="4" >草稿</option>
						<option value="5" >撤销</option>
						<option value="6" >驳回</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="label" style="width: 100px">创建时间：</td>
				<td colspan="5">
					<table>
						<tr>
							<td>
								<input class="easyui-datePicker" id="startTime" name="startTime" dateFmt="yyyy-MM-dd HH:mm:ss">
							</td>
							<td>至</td>
							<td>
								<input id="endTime" name="endTime" type="text" class="easyui-datePicker" dateFmt="yyyy-MM-dd HH:mm:ss" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</ui:Layout>
	<ui:Layout region="center">
		<ui:Grid id="processRunList" datatype="json" shrinkToFit="false"
			fit="true" viewrecords="true" pageable="true"
			url="${ctx}/workflow/bpm/processRun/list.html" multiselect="true"
			rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
			<ui:Column name="runId" key="true" hidden="true"></ui:Column>
			<ui:Column name="status" hidden="true"></ui:Column>
			<ui:Column header="事项名称" name="subject" width="200" formatter="subjectFormatter"></ui:Column>
			<ui:Column header="流程名称" name="processName" width="150"></ui:Column>
			<ui:Column header="创建人" name="creator" width="100"></ui:Column>
			<ui:Column header="创建时间" name="createTime" width="130" formatter="dateFormatter"></ui:Column>
			<ui:Column header="结束时间" name="endTime" width="130" formatter="dateFormatter"></ui:Column>
			<ui:Column header="状态" width="60" formatter="statusFormatter"></ui:Column>
			<ui:Column header="操作" name="" width="200" formatter="operationFormatter"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>