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
<script type="text/javascript" src="${ctx}/ui/js/plugins/jquery.qtip.js" ></script>
<link href="${ctx}/ui/css/default/jquery.qtip.css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/ui/js/util/easyTemplate.js"></script>
<title>任务管理列表</title>
<script type="text/javascript">
	var topWin = FrameHelper.getTop();
	
	function query(){
		var postData = {};
		postData.processName = $("#processName").val();
		postData.subject = $("#subject").val();
		postData.name = $("#name").val();
		$("#taskList").grid("setGridParam", {
			postData : postData
		});
		$("#taskList").trigger("reloadGrid");
	}
	
	//执行任务
	function executeTask(taskId){
		var rowd = $("#taskList").grid("getRowData", taskId);
		var height = $(topWin.document.body).height();
		var width = $(topWin.document.body).width();
		topWin.Dialog.open({
			Title : "流程任务-["+rowd.name+"]执行",
			URL : "${ctx}/workflow/task/doNext.html?taskId="+taskId,
			Width : width,
			Height : height,
			RefreshHandler:function(){
				$("#taskList").trigger("reloadGrid");
			}
		});
	}
	
	//为某个任务分配人员
	function assignTask(){
	}
	
	function statusFormatter(cellVal, options, rowd) {
		if(cellVal==null){
			return "待执行";
		}else{
			return cellVal;
		}
	}
	
	function dateFormatter(cellVal, options, rowd) {
		if (cellVal != null) {
			var d = new Date(cellVal);
			return DateFormat.format(d, "yyyy-MM-dd hh:mm:ss");
		} else {
			return "";
		}
	}
	//任务名称
	function subjectFormatter(cellVal, options, rowd) {
		return "<a href='#' style='color:blue' onclick=\"executeTask('"+rowd.id+"')\">"+rowd.subject+"</a>";
	}
	
	function onGridComplete(){
		$(".userlink").click(function(){
			var userId = $(this).attr("userId");
			showUser(userId);
		});
		$(".orglink").click(function(){
			var orgId = $(this).attr("orgId");
			showOrg(orgId)
			
		});
		$(".rolelink").click(function(){
			var roleId = $(this).attr("roleId");
			showRole(roleId);
			
		});
		$(".showMore").click(showResult);
	}
	
	function showResult(){
		var template=$("#txtReceiveTemplate").val();
		var jsonValue = $(this).next(":hidden") ;
		var html=easyTemplate(template,$.parseJSON(jsonValue.val())).toString();
		$(this).qtip({
			content:{
				text:html,
				title:{
					text: '执行人列表'
				}
			},
	        position: {
	        	at:'top left',
	        	target:'event',
				viewport:  $(window)
	        },
	        show:{
	        	event:"click",
  			    solo:true
	        },   			     	
	        hide: {
	        	event:'mouseleave',
	        	fixed:true
	        },  
	        style: {
	       	  classes:'ui-tooltip-light ui-tooltip-shadow'
	        } 			    
	 	});	
	}
	
	function showUser(userId){
		topWin.Dialog.open({
 			URL : "${ctx}/userCtl/toViewUser.html?id="+userId,
 			Width: 500,
			Height : 540,
 			Title : "用户详细信息"
 		});
	}
	
	function showOrg(orgId){
		topWin.Dialog.open({
 			URL : "${ctx}/orgCtl/orgUserInfo.html?id="+orgId,
 			Width: 800,
			Height : 600,
 			Title : "单位用户信息"
 		});
	}
	
	function showRole(roleId){
		topWin.Dialog.open({
 			URL : "${ctx}/roleCtl/roleUserInfo.html?id="+roleId,
 			Width: 800,
			Height : 600,
 			Title : "角色用户信息"
 		});
	}
	
</script>
</head>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:75px">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="query" href="#" iconCls="icon-search" onclick="query()">查询</a>
			<!-- <a id="query" href="#" iconCls="icon-user" onclick="assignTask()">分配人员</a> -->
		</div>
		<table class="formTable">
			<tr>
				<td class="label" style="width: 100px">事项名称：</td>
				<td style="width: 130px">
					<input type="text" class="easyui-textinput" id="subject" name="subject" watermark="支持模糊查询" />
				</td>
				<td class="label" style="width: 100px">当前任务：</td>
				<td>
					<input type="text" class="easyui-textinput" id="name" name="name" watermark="支持模糊查询" />
				</td>
			</tr>
		</table>
	</ui:Layout>
	<ui:Layout region="center">
		<ui:Grid id="taskList" datatype="json" shrinkToFit="false" fit="true" viewrecords="true" pageable="true" 
			url="${ctx}/workflow/task/list.html" multiselect="true"
			rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
			<ui:Column name="id" key="true" hidden="true"></ui:Column>
			<ui:Column name="name" hidden="true"></ui:Column>
			<ui:Column header="事项名称" name="subject" width="250" formatter="subjectFormatter"></ui:Column>
			<ui:Column header="当前任务" name="name" width="180"></ui:Column>
			<ui:Column header="执行人" name="assigneeStr" width="200"></ui:Column>
			<ui:Column header="状态" name="delegationState" width="60" formatter="statusFormatter"></ui:Column>
			<ui:Column header="任务创建时间" name="createTime" width="130" formatter="dateFormatter"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
<textarea id="txtReceiveTemplate"  style="display: none;">
    <div style="height:200px;width:200px;overflow:auto">
	    <table class="tableView">
	  		<#list data as obj>
	  		<tr>
	  			<td>\${obj_index+1}</td>
	  			<td>
	  			<#if ("org"==obj.type)>
					<img src='${ctx}/ui/css/icon/images/user_group.gif' style='margin:-5px 3px'/><a onclick=\"showOrg('\${obj.executeId}')\" style='color:blue'>\${obj.executor}</a>
				<#elseif ("role"==obj.type)>
					<img src='${ctx}/ui/css/icon/images/user_female.gif' style='margin:-5px 3px'/><a onclick=\"showRole('\${obj.executeId}')\" style='color:blue'>\${obj.executor}</a>
				<#elseif ("user"==obj.type)>
					<img src='${ctx}/ui/css/icon/images/user.gif' style='margin:-5px 3px'/><a onclick=\"showUser('\${obj.executeId}')\" style='color:blue'>\${obj.executor}</a>
				</#if>
	  			</td>
	  		</tr>
	  		</#list>
		</table>
  	</div>
  </textarea>
</body>
</html>