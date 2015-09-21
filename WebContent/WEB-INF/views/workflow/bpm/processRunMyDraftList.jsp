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
<title>我的请求</title>
<script type="text/javascript">
var topWin = FrameHelper.getTop();
function query(){
	var postData = {};
	postData.processName = $("#processName").val();
	postData.subject = $("#subject").val();
	postData.startTime = $("#startTime").val();
	postData.endTime = $("#endTime").val();
	$("#taskList").grid("setGridParam", {postData:postData});
	$("#taskList").trigger("reloadGrid");
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
			$("#taskList").trigger("reloadGrid");
		}
	});
}
function deleteDrafts(){
	var selectedIds = $("#taskList").grid("getGridParam", "selarrrow");
	if (selectedIds.length == 0){
		MessageUtil.alert("请选择要删除的流程实例！");
		return;
	}else{
		remove(selectedIds.join(","));
	}
}

function remove(ids){
	MessageUtil.confirm("确定要删除选中的草稿？",function(){
		$.postc("${ctx}/workflow/bpm/processRun/del.html?ids="+ids,null,function(data){
			MessageUtil.show("删除成功");
			$("#taskList").trigger("reloadGrid");
		});	
	});
}
function dateFormatter(cellVal, options, rowd) {
	var val = rowd.createTime;
	if (val != null) {
		var d = new Date(val);
		return DateFormat.format(d, "yyyy-MM-dd hh:mm:ss");
	} else {
		return "";
	}
}


function userNameFormater(cellVal, options, rowd) {
	return "<img src='${ctx}/ui/css/icon/images/user.gif' style='margin:-5px 3px'><a href='#' style='color:blue' onclick=\"showUser('"+rowd.creatorId+"')\">"+rowd.creator+"</a>";
}

//处理时长
function durTimeFormatter(cellVal, options, rowd){
	var val = new Date().getTime()-rowd.createTime;
	var time = "";
     if (val == null) {
       return "";
     }
     var days = parseInt(val / 1000 / 60 / 60 / 24);
     if (days > 0) {
       time = time + days + "天";
     }
     var hourMillseconds = val - days * 1000 * 60 * 60 * 24;
     var hours = parseInt(hourMillseconds / 1000 / 60 / 60);
     if (hours > 0) {
       time = time + hours + "小时";
     }
     var minuteMillseconds = val - days * 1000 * 60 * 60 * 24 - hours * 1000 * 60 * 60;
     var minutes = parseInt(minuteMillseconds / 1000 / 60);
     if (minutes > 0) {
       time = time + minutes + "分钟";
     }
     return time;
}

function showUser(userId){
	topWin.Dialog.open({
			URL : "${ctx}/userCtl/toViewUser.html?id="+userId,
			Width: 500,
			Height : 540,
			Title : "用户详细信息"
		});
}

function operationFormatter(cellVal, options, rowd) {
	var html = "";
	html += "<a plain=\"true\" class=\"easyui-linkbutton\" title='启动' href=\"#\" iconCls=\"icon-run\" onclick=\"startFlow('"+rowd.runId+"')\">启动</a>";
	html += "<a plain=\"true\" class=\"easyui-linkbutton\" title='删除' href=\"#\" iconCls=\"icon-remove\" onclick=\"remove('"+rowd.runId+"')\">删除</a>";
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
	<ui:Layout region="north" style="height:75px" border="false">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="queryBtn" href="#" iconCls="icon-search" onclick="query()">查询</a>
			<a id="delBtn" href="#" iconCls="icon-remove" onclick="deleteDrafts()">删除</a>
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
			url="${ctx}/workflow/bpm/processRun/myDraftList.html" multiselect="true"
			rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
			<ui:Column name="runId" key="true" hidden="true"></ui:Column>
			<ui:Column name="createTime" hidden="true"></ui:Column>
			<ui:Column header="事项名称" name="subject" width="250"></ui:Column>
			<ui:Column header="流程名称" name="processName" width="150"></ui:Column>
			<ui:Column header="创建时间" width="130" formatter="dateFormatter"></ui:Column>
			<ui:Column header="管理" width="130" formatter="operationFormatter" align="center"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>