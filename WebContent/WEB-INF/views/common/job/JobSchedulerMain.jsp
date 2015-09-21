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
	function addJobScheduler(){
		topWin.Dialog.open({
			Title:"新增调度服务器",
			Width:500,
			Height:250,
			URL:"${ctx}/job/toJobSchedulerAdd.html",
			RefreshHandler:function(){
				$("#jobSchedulerList").trigger("reloadGrid");
			}
		});
	}
	
	function updateJobScheduler(){
		var selectedIds = $("#jobSchedulerList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择待修改的调度服务器！");
			return;
		}else if(selectedIds.length > 1){
			MessageUtil.alert("只能选择一条记录进行修改！");
		}else{
			topWin.Dialog.open({
				URL : "${ctx}/job/toJobSchedulerUpdate.html?jobSchedulerId="+selectedIds[0],
				ShowCloseButton : true,
				Width:500,
				Height:250,
				Title:"修改调度服务器",
				RefreshHandler : function(){
					$("#jobSchedulerList").trigger("reloadGrid");
				}
			}); 
		}
	}
	
	function delJobScheduler(){
		var selectedIds = $("#jobSchedulerList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择调度服务器！");
			return;
		}
		MessageUtil.confirm("确定删除选中的调度服务器吗？",function(){
			$.postc("${ctx}/job/deleteJobScheduler.html", {ids:selectedIds.join(",")}, function(data){
				$("#jobSchedulerList").trigger("reloadGrid");
			});
		});
		
	}
	
	function switchJobScheduler(){
		topWin.Dialog.open({
			URL : "${ctx}/job/toJobSchedulerSwitch.html",
			ShowCloseButton : true,
			Width:400,
			Height:220,
			Title:"切换当前调度服务器",
			RefreshHandler : function(){
				$("#jobSchedulerList").trigger("reloadGrid");
			}
		}); 
	}
	
	function refreshJobScheduler(){
		$("#jobSchedulerList").trigger("reloadGrid");
	}
	
	function jobStatusFormatter(cellVal,options,rowd){
		if(rowd.activeStatus=="1"){
			if(rowd.currScheduler=="1"){
				return "<img src=\"${ctx}/ui/css/icon/images/blue.gif\" style=\"vertical-align:middle;\" title=\"活动且是当前调度服务器\"/>";
			}else{
				return "<img src=\"${ctx}/ui/css/icon/images/yellow.gif\" style=\"vertical-align:middle;\" title=\"活动但不是当前调度服务器\"/>";
			}
		}else{
			if(rowd.mountStatus=="1"){
				return "<img src=\"${ctx}/ui/css/icon/images/red.gif\" style=\"vertical-align:middle;\" title=\"已装配但停止的调度服务器\"/>";
			}else{
				return "<img src=\"${ctx}/ui/css/icon/images/gray.gif\" style=\"vertical-align:middle;\" title=\"未装配的调度服务器\"/>";
			}
		}
	}
	
	function lastActiveTimeFormatter(cellVal,options,rowd){
		if(cellVal==null){
			return '';
		}else{
			var d = new Date(cellVal);
			return DateFormat.format(d, "yyyy-MM-dd hh:mm:ss");
		}
	}
</script>
</head>
<body>
	<ui:LayoutContainer fit="true">
		<ui:Layout region="north" style="height:32px" border="false">
			<div id="toolbar" class="easyui-toolbar" style="width:100%;">
				<kpm:HasPermission permCode="SYS_JOBSCHEDULER_ADD">
					<a id="addBtn" href="#" iconCls="icon-add" onclick="addJobScheduler()">新增</a>
				</kpm:HasPermission>
				<kpm:HasPermission permCode="SYS_JOBSCHEDULER_UPDATE">
					<a id="updateBtn" href="#" iconCls="icon-edit" onclick="updateJobScheduler()">修改</a>
				</kpm:HasPermission>
				<kpm:HasPermission permCode="SYS_JOBSCHEDULER_DEL">
					<a id="delBtn" href="#" iconCls="icon-remove" onclick="delJobScheduler()">删除</a>
				</kpm:HasPermission>
				<kpm:HasPermission permCode="SYS_JOBSCEHDULER_SWITCH">
					<a id="switchBtn" href="#" iconCls="icon-swith" onclick="switchJobScheduler()">切换</a>
				</kpm:HasPermission>
				<a id="refreshBtn" href="#" iconCls="icon-reload" onclick="refreshJobScheduler()">刷新</a>
			</div>
		</ui:Layout>
		<ui:Layout region="center" border="false"  style="padding:1px">
			<ui:Grid id="jobSchedulerList" datatype="json" shrinkToFit="true" fit="true" pageable="false"
				url="${ctx}/job/jobSchedulerList.html" multiselect="true" rowNum="20" rowList="[10,20,50]">
				<ui:Column name="jobSchedulerId" key="true" hidden="true"></ui:Column>
				<ui:Column header="" formatter="jobStatusFormatter" width="30" fixed="true" align="center"></ui:Column>
				<ui:Column header="调度器地址" name="hostUrl" width="120"></ui:Column>
				<ui:Column header="优先级" align="center" name="priority"  width="40"></ui:Column>
				<ui:Column header="最后活动时间" name="lastActiveTime" formatter="lastActiveTimeFormatter" width="90"></ui:Column>
				<ui:Column header="装配状态" width="50" align="center" name="mountStatus" edittype="select" formatter="'select'" editoptions="{value:\"0:未装配;1:已装配\"}"></ui:Column>
				<ui:Column header="活动状态" width="50"  align="center" name="activeStatus" edittype="select" formatter="'select'" editoptions="{value:\"0:停止;1:活动\"}"></ui:Column>
				<ui:Column header="当前调度器" width="50" align="center" name="currScheduler" edittype="select" formatter="'select'" editoptions="{value:\"0:否;1:是\"}"></ui:Column>
			</ui:Grid>
		</ui:Layout>
		<ui:Layout region="south" style="height:35px;padding-left:10px;color:#4040a0" border="false">
			<div style="float:left">
				<img src="${ctx}/ui/css/icon/images/blue.gif" style="vertical-align:middle;"/>&nbsp;活动且是当前调度服务器&nbsp;&nbsp;
				<img src="${ctx}/ui/css/icon/images/yellow.gif" style="vertical-align:middle;"/>&nbsp;活动但不是当前调度服务器&nbsp;&nbsp;
				<img src="${ctx}/ui/css/icon/images/red.gif" style="vertical-align:middle;"/>&nbsp;已装配但停止的调度服务器&nbsp;&nbsp;
				<img src="${ctx}/ui/css/icon/images/gray.gif" style="vertical-align:middle;"/>&nbsp;未装配的调度服务器&nbsp;&nbsp;
			</div>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>