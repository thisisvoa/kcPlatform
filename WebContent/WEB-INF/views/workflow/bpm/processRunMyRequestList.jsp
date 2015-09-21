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
	postData.status = $("#status").combobox("getValue");
	postData.startTime = $("#startTime").val();
	postData.endTime = $("#endTime").val();
	$("#taskList").grid("setGridParam", {postData:postData});
	$("#taskList").trigger("reloadGrid");
}

function detail(id){
	var height = $(topWin.document.body).height();
	var width = $(topWin.document.body).width();
	topWin.Dialog.open({
		URL : "${ctx}/workflow/bpm/processRun/info.html?runId="+id,
		ShowCloseButton : true,
		Width: width,
		Height : height,
		Title : "流程明细",
		RefreshHandler:function(){
			$("#taskList").trigger("reloadGrid");
		}
	}); 
}

function recover(runId){
	topWin.Dialog.open({
		Title : "撤销原因",
		URL : '${ctx}/workflow/bpm/processRun/toRecover.html?runId='+runId,
		Width : 500,
		Height : 210,
		RefreshHandler:function(){
			MessageUtil.show("撤销任务成功！");
			$("#taskList").trigger("reloadGrid");
		}
	});
};

function dateFormatter(cellVal, options, rowd) {
	var val = rowd.createTime;
	if (val != null) {
		var d = new Date(val);
		return DateFormat.format(d, "yyyy-MM-dd hh:mm:ss");
	} else {
		return "";
	}
}

function subjectFormatter(cellVal, options, rowd) {
	return "<a href='#' style='color:blue;' onclick=\"detail('"+rowd.runId+"')\">"+cellVal+"</a>";
}

//流程状态
function statusFormater(cellVal, options, rowd) {
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
	if(rowd.status==1 || rowd.status==6){
		html += "<a plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-redo\" onclick=\"recover('"+rowd.runId+"')\">撤销</a>";	
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
	<ui:Layout region="north" style="height:100px" border="false">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="queryBtn" href="#" iconCls="icon-search" onclick="query()">查询</a>
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
				<td class="label" style="width: 100px">状态：</td>
				<td>
					<select id="status" name="status" class="easyui-combobox" dropdownHeight="100">
						<option value="">--所有--</option>
						<option value="1" >正在运行</option>
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
			url="${ctx}/workflow/bpm/processRun/myRequestList.html" multiselect="false"
			rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
			<ui:Column name="runId" key="true" hidden="true"></ui:Column>
			<ui:Column name="creatorId" hidden="true"></ui:Column>
			<ui:Column name="createTime" hidden="true"></ui:Column>
			<ui:Column header="事项名称" name="subject" width="250" formatter="subjectFormatter"></ui:Column>
			<ui:Column header="流程名称" name="processName" width="150"></ui:Column>
			<ui:Column header="创建时间" width="130" formatter="dateFormatter"></ui:Column>
			<ui:Column header="持续时间" width="130" formatter="durTimeFormatter"></ui:Column>
			<ui:Column header="状态" name="status" width="50" formatter="statusFormater" align="center"></ui:Column>
			<ui:Column header="管理" width="60" formatter="operationFormatter" align="center"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>