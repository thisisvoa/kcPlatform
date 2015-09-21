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
<title>我的代理事宜</title>
<script type="text/javascript">
var topWin = FrameHelper.getTop();
function query(){
	var postData = {};
	postData.processName = $("#processName").val();
	postData.subject = $("#subject").val();
	postData.assigneeId = $("#assigneeId").val();
	postData.status = $("#status").combobox("getValue");
	postData.beginCreateTime = $("#beginCreateTime").val();
	postData.endCreateTime = $("#endCreateTime").val();
	$("#taskExeList").grid("setGridParam", {postData:postData});
	$("#taskExeList").trigger("reloadGrid");
}


function dateFormatter(cellVal, options, rowd) {
	if (cellVal != null) {
		var d = new Date(cellVal);
		return DateFormat.format(d, "yyyy-MM-dd");
	} else {
		return "";
	}
}

function subjectFormatter(cellVal, options, rowd) {
	return "<a href='#' style='color:blue;' onclick=\"detail('"+rowd.runId+"')\">"+rowd.subject+"</a>";
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

function userNameFormater1(cellVal, options, rowd) {
	return "<img src='${ctx}/ui/css/icon/images/user.gif' style='margin:-5px 3px'><a href='#' style='color:blue' onclick=\"showUser('"+rowd.creatorId+"')\">"+rowd.creator+"</a>";
}
function userNameFormater2(cellVal, options, rowd) {
	return "<img src='${ctx}/ui/css/icon/images/user.gif' style='margin:-5px 3px'><a href='#' style='color:blue' onclick=\"showUser('"+rowd.assigneeId+"')\">"+rowd.assigneeName+"</a>";
}
function statusFormater(cellVal, options, rowd) {
	switch(rowd.status){
		case 0:
			return "<span class='green'>初始状态</span>";
		case 1:
			return "<span class='green'>完成任务</span>";
		case 2:
			return "<span class='red'>取消代理</span>";
		case 3:
			return "<span class='green'>其他人完成</span>";
		default:
			return "";
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

function selectUser(){
	var diag = new topWin.Dialog({
		URL : "${ctx}/userCtl/cmp/userSelector.html?multiselect=false",
		Width: 900,
		Height: 450,
		Title : "用户选择器",
		RefreshHandler : function(){
			var selectedUserList = diag.getData().selectedUserList;
			if(selectedUserList!=null){
				var ids = "";
				var descs = "";
				var spliter='';
				for(var i=0;i<selectedUserList.length;i++){
					ids += spliter+selectedUserList[i].ZJID;
					descs += spliter+selectedUserList[i].YHMC;
					spliter=',';
				}
				$("#assigneeId").val(ids);
				$("#assigneeName").val(descs);
			}else{
				$("#assigneeId").val("");
				$("#assigneeName").val("");
			}
		}
	});
	diag.setData({selectedUser:$("#assigneeId").val()});
	diag.show();
}

function clearUser(){
	$("#assigneeId").val("");
	$("#assigneeName").val("");
}

function cancle(id){
	var diag = topWin.Dialog.open({
		Title : "取消代理",
		URL : "${ctx}/workflow/bpm/taskExe/toCancle.html",
		Width : 450,
		Height : 200,
		RefreshHandler:function(){
			var opinion = diag.getData();
			$.postc("${ctx}/workflow/bpm/taskExe/cancel.html",{id:id,opinion:opinion},function(){
				MessageUtil.show("代理取消成功！");
				$("#taskExeList").trigger("reloadGrid");
			});
		}
	});
}

function remove(id){
	MessageUtil.confirm("确认删除代理吗？？",function(){
		$.postc("${ctx}/workflow/bpm/taskExe/del.html",{id:id},function(){
			MessageUtil.show("代理删除成功！");
			$("#taskExeList").trigger("reloadGrid");
		});
	});
}

function batCancle(){
	var selectedIds = $("#taskExeList").grid("getGridParam", "selarrrow");
	if (selectedIds.length == 0){
		MessageUtil.alert("请选择待取消的代理！");
		return;
	}else{
		for(var i=0;i<selectedIds.length;i++){
			var rowd = $("#taskExeList").grid("getRowData",selectedIds[i]);
			if(rowd.status!=0){
				MessageUtil.alert("代理事项【"+rowd.subject+"】已经取消或完成，请重新选择！")
				return;
			}
		}
		var ids = selectedIds.join(",");
		var diag = topWin.Dialog.open({
			Title : "取消代理",
			URL : "${ctx}/workflow/bpm/taskExe/toCancle.html",
			Width : 450,
			Height : 200,
			RefreshHandler:function(){
				var opinion = diag.getData();
				$.postc("${ctx}/workflow/bpm/taskExe/cancelBat.html",{ids:ids,opinion:opinion},function(){
					MessageUtil.show("代理取消成功！");
					$("#taskExeList").trigger("reloadGrid");
				});
			}
		});
	}
}

function operationFormatter(cellVal, options, rowd) {
	var html = "";
	if(rowd.status==0){
		html += "<a plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-remove\" onclick=\"cancle('"+rowd.id+"')\">取消</a>";	
	}else{
		html += "<a plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-remove\" onclick=\"remove('"+rowd.id+"')\">删除</a>";
	}
	return html;
}

function gridComplete(){
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
			<a id="batBtn" href="#" iconCls="icon-remove" onclick="batCancle()">批量取消</a>
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
				<td class="label" style="width: 100px">代理人：</td>
				<td>
					<input type="hidden" id="assigneeId" name="assigneeId"/>
					<input type="text" class="easyui-textinput" id="assigneeName" name="assigneeName"/>
					<input type="button" class="easyui-button" value="请选择" onclick="selectUser();">
					<input type="button" class="easyui-button" value="清&nbsp;空" onclick="clearUser();">
				</td>
			</tr>
			<tr>
				<td class="label" style="width: 100px">创建时间：</td>
				<td  colspan="3">
					<table>
						<tr>
							<td><input class="easyui-datePicker" id="beginCreateTime" name="beginCreateTime" dateFmt="yyyy-MM-dd"></td>
							<td>至</td>
							<td><input id="endCreateTime" name="endCreateTime" type="text" class="easyui-datePicker" dateFmt="yyyy-MM-dd" /></td>
						</tr>
					</table>
				</td>
				<td class="label" style="width: 100px">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
				<td>
					<select id="status" name="status" class="easyui-combobox" dropdownHeight="125">
						<option value="" >所有</option>
						<option value="0" >初始状态</option>
						<option value="1" >完成任务</option>
						<option value="2" >取消代理</option>
						<option value="3" >其他人完成</option>
					</select>
				</td>
				
			</tr>
		</table>
	</ui:Layout>
	<ui:Layout region="center">
		<ui:Grid id="taskExeList" datatype="json" shrinkToFit="false" fit="true" viewrecords="true" pageable="true" 
			url="${ctx}/workflow/bpm/taskExe/accordingMattersList.html" multiselect="true"
			rowNum="20" rowList="[10,20,50]" gridComplete="gridComplete">
			<ui:Column name="id" key="true" hidden="true"></ui:Column>
			<ui:Column name="creatorId" hidden="true"></ui:Column>
			<ui:Column name="assigneeId" hidden="true"></ui:Column>
			<ui:Column name="status" hidden="true"></ui:Column>
			<ui:Column name="subject" hidden="true"></ui:Column>
			<ui:Column header="事项名称" width="250" formatter="subjectFormatter"></ui:Column>
			<ui:Column header="流程名称" name="processName" width="150"></ui:Column>
			<ui:Column header="流程创建人" name="creator" width="100" formatter="userNameFormater1"></ui:Column>
			<ui:Column header="代理人" name="assigneeName" width="100" formatter="userNameFormater2"></ui:Column>
			<ui:Column header="创建时间" name="createTime" width="100" formatter="dateFormatter"></ui:Column>
			<ui:Column header="状态" width="100" formatter="statusFormater"></ui:Column>
			<ui:Column header="管理" width="60" formatter="operationFormatter"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>