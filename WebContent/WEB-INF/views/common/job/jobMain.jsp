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
<script type="text/javascript">
	var topWin = FrameHelper.getTop();
	
	function doQuery(){
		var title = $("#title").val();
		var jobGroupName = $("#jobGroupName").val();
		var jobName = $("#jobName").val();
		$("#jobList").grid("setGridParam", 
							{postData:{jobName:jobName, title:title, jobGroupName:jobGroupName}});
		$("#jobList").trigger("reloadGrid");
	}
	
	function addJob(){
		topWin.Dialog.open({
			Title:"新增任务",
			Width:600,
			Height:500,
			URL:"${ctx}/job/toJobAdd.html",
			RefreshHandler:function(){
				$("#jobList").trigger("reloadGrid");
			}
		});
	}
	
	function updateJob(){
		var selectedIds = $("#jobList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择待修改的任务！");
			return;
		}else if(selectedIds.length > 1){
			MessageUtil.alert("只能选择一条任务进行修改！");
		}else{
			var rowd = $("#jobList").grid("getRowData", selectedIds[0]);
			if(rowd.activeStatus=="1" && rowd.released=="1"){
				MessageUtil.alert("该任务处于活动状态不能修改，请先停止该活动！");
				return;
			}
			topWin.Dialog.open({
				URL : "${ctx}/job/toJobUpdate.html?jobId="+selectedIds[0],
				ShowCloseButton : true,
				Width:600,
				Height:500,
				Title : "修改任务",
				RefreshHandler : function(){
					$("#jobList").trigger("reloadGrid");
				}
			}); 
		}
	}
	
	function delJob(){
		var selectedIds = $("#jobList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择待删除的任务！");
			return;
		}else{
			for(var i=0;i<selectedIds.length;i++){
				var rowd = $("#jobList").grid("getRowData", selectedIds[i]);
				if(rowd.activeStatus=="1"){
					MessageUtil.alert("只有处于停止状态的任务才能删除！");
					return;
				}
			}
			$.postc("${ctx}/job/deleteJob.html",{ids:selectedIds.join(",")},function(data){
				MessageUtil.show("操作成功!");
				$("#jobList").trigger("reloadGrid");
			});
		}
	}
	
	function startJob(){
		var selectedIds = $("#jobList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择待启动的任务！");
			return;
		}else{
			for(var i=0;i<selectedIds.length;i++){
				var rowd = $("#jobList").grid("getRowData", selectedIds[i]);
				if(rowd.activeStatus=="1"){
					MessageUtil.alert("只有处于停止状态的任务才能启动！");
					return;
				}
			}
			$.postc("${ctx}/job/startJob.html",{ids:selectedIds.join(",")},function(data){
				MessageUtil.show("操作成功!");
				$("#jobList").trigger("reloadGrid");
			});
		}
	}
	
	function stopJob(){
		var selectedIds = $("#jobList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择待停止的任务！");
			return;
		}else{
			for(var i=0;i<selectedIds.length;i++){
				var rowd = $("#jobList").grid("getRowData", selectedIds[i]);
				if(rowd.activeStatus=="0"){
					MessageUtil.alert("只有处于活动状态的任务才能停止！");
					return;
				}
			}
			$.postc("${ctx}/job/stopJob.html",{ids:selectedIds.join(",")},function(data){
				MessageUtil.show("操作成功!");
				$("#jobList").trigger("reloadGrid");
			});
		}
	}
	
	function releaseJob(){
		var selectedIds = $("#jobList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择待发布的任务！");
			return;
		}else{
			for(var i=0;i<selectedIds.length;i++){
				var rowd = $("#jobList").grid("getRowData", selectedIds[i]);
				if(rowd.released=="1"){
					MessageUtil.alert("只有未发布的任务才能发布！");
					return;
				}
			}
			$.postc("${ctx}/job/releaseJob.html",{ids:selectedIds.join(",")},function(data){
				MessageUtil.show("操作成功!");
				$("#jobList").trigger("reloadGrid");
			});
		}
	}
	
	function jobStatusFormatter(cellVal,options,rowd){
		if(rowd.released=="1"){
			if(rowd.activeStatus=="1"){
				return "<img src=\"${ctx}/ui/css/icon/images/blue.gif\" style=\"vertical-align:middle;\" title=\"发布且处于活动状态的任务\"/>";
			}else{
				return "<img src=\"${ctx}/ui/css/icon/images/red.gif\" style=\"vertical-align:middle;\" title=\"发布且处于停止状态的任务\"/>";
			}
		}else{
			return "<img src=\"${ctx}/ui/css/icon/images/gray.gif\" style=\"vertical-align:middle;\" title=\"未发布的任务\"/>";
		}
	}
	
	function lastActiveTimeFormatter(cellVal,options,rowd){
		if(cellVal==null){
			return "";
		}else{
			var d = new Date(cellVal);
			return DateFormat.format(d, "yyyy-MM-dd hh:mm:ss");
		}
	}
</script>
</head>
<body style="padding:1px">
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" title="查询条件" style="height:60px">
		<table class="formTable">
			<tr>
				<td class="label" style="width:100px">
					任务名：
				</td>
				<td>
					<input type="text" class="easyui-textinput" name="title" id="title">
				</td>
				<td class="label" style="width:100px">
					任务组名：
				</td>
				<td>
					<input type="text" class="easyui-textinput" name="jobGroupName" id="jobGroupName">
				</td>
				<td class="label" style="width:100px">
					周知标识：
				</td>
				<td>
					<input type="text" class="easyui-textinput" name="jobName" id="jobName">
				</td>
			</tr>
		</table>
	</ui:Layout>
	<ui:Layout region="center" border="false" style="padding-top:1px">
		<ui:LayoutContainer fit="true">
			<ui:Layout region="north" style="height:33px" border="false">
				<div id="toolbar" class="easyui-toolbar" style="width:100%;">
					<kpm:HasPermission permCode="SYS_JOB_QUERY">
					<a id="queryBtn" href="#" iconCls="icon-search" onclick="doQuery()">查询</a>
					</kpm:HasPermission>
					<kpm:HasPermission permCode="SYS_JOB_ADD">
						<a id="addBtn" href="#" iconCls="icon-add" onclick="addJob()">新增</a>
					</kpm:HasPermission>
					<kpm:HasPermission permCode="SYS_JOB_UPDATE">
						<a id="updateBtn" href="#" iconCls="icon-edit" onclick="updateJob()">修改</a>
					</kpm:HasPermission>
					<kpm:HasPermission permCode="SYS_JOB_DEL">
						<a id="delBtn" href="#" iconCls="icon-remove" onclick="delJob()">删除</a>
					</kpm:HasPermission>
					<kpm:HasPermission permCode="SYS_JOB_START">
						<a id="startBtn" href="#" iconCls="icon-start" onclick="startJob()">启动</a>
					</kpm:HasPermission>
					<kpm:HasPermission permCode="SYS_JOB_STOP">
						<a id="stopBtn" href="#" iconCls="icon-stop" onclick="stopJob()">停止</a>
					</kpm:HasPermission>
					<kpm:HasPermission permCode="SYS_JOB_RELEASE">
						<a id="releaseBtn" href="#" iconCls="icon-job-relase" onclick="releaseJob()">发布</a>
					</kpm:HasPermission>
				</div>
			</ui:Layout>
			<ui:Layout region="center"  border="false">
				<ui:Grid id="jobList" datatype="json" shrinkToFit="true" fit="true" pageable="true"
					url="${ctx}/job/jobList.html" multiselect="true"
					rowNum="20" rowList="[10,20,50]">
					<ui:Column name="jobId" key="true" hidden="true"></ui:Column>
					<ui:Column header="" formatter="jobStatusFormatter" width="30" fixed="true" align="center"></ui:Column>
					<ui:Column header="任务名称" name="title" width="90"></ui:Column>
					<ui:Column header="任务组名" name="jobGroupName"  width="50"></ui:Column>
					<ui:Column header="周知标识" name="jobName"  width="50"></ui:Column>
					<ui:Column header="发布状态" width="50" align="center" name="released" edittype="select" formatter="'select'" editoptions="{value:\"0:未发布;1:已发布\"}"></ui:Column>
					<ui:Column header="运行状态" width="50" align="center" name="activeStatus" edittype="select" formatter="'select'" editoptions="{value:\"0:停止;1:活动\"}"></ui:Column>
					<ui:Column header="最后执行时间" width="80" name="lastActiveTime" formatter="lastActiveTimeFormatter"></ui:Column>
				</ui:Grid>
			</ui:Layout>
		</ui:LayoutContainer>
	</ui:Layout>
	<ui:Layout region="south" style="height:100px;padding:5px;color:#4040a0" border="false">
		<b>提示：</b><br>
		1、每条任务必须在程序中拥有对应的Bean，即实现com.casic27.platform.base.job.Job接口，且Bean名称为"任务组名.周知标识"，TAG可为空，而周知标识不能为空；<br>
		2、系统将会在启动时自动装配任务并运行，所以在配置任务后，必须编写Job并重新部署启动应用，任务才会运行；<br>
		3、只有"未装配"或处于"停止状态"的任务才能进行修改或删除；<br>
		4、<img src="${ctx}/ui/css/icon/images/blue.gif" style="vertical-align:middle;"/>&nbsp;发布且处于活动状态的任务&nbsp;&nbsp;
				<img src="${ctx}/ui/css/icon/images/red.gif" style="vertical-align:middle;"/>&nbsp;已经发布且处于停止状态的任务&nbsp;&nbsp;
				<img src="${ctx}/ui/css/icon/images/gray.gif" style="vertical-align:middle;"/>&nbsp;未发布的任务&nbsp;&nbsp;
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>