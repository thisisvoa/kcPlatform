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
		postData.beginStartDate = $("#beginStartDate").val();
		postData.endStartDate = $("#endStartDate").val();
		postData.startDate = $("#startDate").val();
		postData.beginEndDate = $("#beginEndDate").val();
		postData.endEndDate = $("#endEndDate").val();
		postData.enabled = $("#enabled").combobox("getValue");
		$("#bpmAgentSettingList").grid("setGridParam", {postData:postData});
		$("#bpmAgentSettingList").trigger("reloadGrid");
	}
	
	function addBpmAgentSetting(){
		topWin.Dialog.open({
			Title : "新增委托代理",
			URL : '${ctx}/workflow/bpm/agentSetting/toAdd.html',
			Width : 800,
			Height : 500,
			RefreshHandler : function(){
				$("#bpmAgentSettingList").trigger("reloadGrid");
			}});
	}
	
	function editBpmAgentSetting(){
		var selectedIds = $("#bpmAgentSettingList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择待修改的委托代理！");
			return;
		}else if(selectedIds.length > 1){
			MessageUtil.alert("只能选择一条记录进行修改！");
		}else{
			topWin.Dialog.open({
				Title : "修改委托代理",
				URL : '${ctx}/workflow/bpm/agentSetting/toUpdate.html?id='+selectedIds[0],
				Width : 800,
				Height : 500,
				RefreshHandler : function(){
					$("#bpmAgentSettingList").trigger("reloadGrid");
				}});
		}
		
	}
	
	function delBpmAgentSetting(){
		var selectedIds = $("#bpmAgentSettingList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要删除的记录！");
			return;
		}
		var ids = selectedIds.join(",");
		MessageUtil.confirm("确定删除选择的记录吗？",function(){
			$.postc('${ctx}/workflow/bpm/agentSetting/del.html', {ids:ids}, function(data){
				MessageUtil.show("删除记录成功！");
				$("#bpmAgentSettingList").trigger("reloadGrid");
			});
		});
	}
	

	function onGridComplete(){
		using("button", function(){
			$(".easyui-button").button();
		});
	}
	function dateFormatter(cellVal, options, rowd) {
		if (cellVal != null) {
			var d = new Date(cellVal);
			return DateFormat.format(d, "yyyy-MM-dd");
		} else {
			return "";
		}
	}
	function showUser(userId){
		topWin.Dialog.open({
				URL : "${ctx}/userCtl/toViewUser.html?id="+userId,
				Width: 500,
				Height : 540,
				Title : "用户详细信息"
			});
	}
	function userNameFormater(cellVal, options, rowd) {
		return "<img src='${ctx}/ui/css/icon/images/user.gif' style='margin:-5px 3px'><a href='#' style='color:blue' onclick=\"showUser('"+rowd.agentId+"')\">"+cellVal+"</a>";
	}
</script>
</head>
<body style="padding:1px">
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:110px;">
		<div class="easyui-toolbar">
			<a id="btnQuery" href="#" iconCls="icon-search" onclick="doQuery()">查询</a>
			<a id="btnAdd" href="#" iconCls="icon-add" onclick="addBpmAgentSetting()">新增</a>
			<a id="btnUpd" href="#" iconCls="icon-edit" onclick="editBpmAgentSetting()">修改</a>
			<a id="btnRemove" href="#" iconCls="icon-remove" onclick="delBpmAgentSetting()">删除</a>	
		</div>
		<form id="queryForm">
			<table class="formTable">
				<tr>
					<td class="label">
						代理开始时间：
					</td>
					<td style="width:200px;">
						<table>
							<tr>
								<td><input class="easyui-datePicker" id="beginStartDate" name="beginStartDate" dateFmt="yyyy-MM-dd"></td>
								<td>至</td>
								<td><input class="easyui-datePicker" id="endStartDate" name="endStartDate" dateFmt="yyyy-MM-dd"></td>
							</tr>
						</table>
					</td>
					<td class="label">
						启用状态：
					</td>
					<td>
						<select id="enabled" name="enabled" class="easyui-combobox" dropdownHeight="75">
							<option value="">--请选择--</option>
							<option value="1">启用</option>
							<option value="0">禁用</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="label">
						结束时间：
					</td>
					<td colspan="2">
						<table>
							<tr>
								<td><input class="easyui-datePicker" id="beginEndDate"
									name="beginEndDate" dateFmt="yyyy-MM-dd">
								</td>
								<td>至</td>
								<td><input class="easyui-datePicker" id="endEndDate"
									name="endEndDate" dateFmt="yyyy-MM-dd">
								</td>
							</tr>
						</table>
					</td>
					
				</tr>
			</table>
		</form>
	</ui:Layout>
	<ui:Layout region="center" border="false" style="padding-top:1px">
		<ui:Grid id="bpmAgentSettingList" datatype="json" shrinkToFit="false" fit="true" viewrecords="true" pageable="true"
				url="${ctx}/workflow/bpm/agentSetting/list.html" multiselect="true" rownumbers="false"
				rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
				<ui:Column name="id" key="true" hidden="true"></ui:Column>
				<ui:Column name="agentId" hidden="true"></ui:Column>
				<ui:Column header="代理类型" name="authType" width="90" edittype="select" formatter="'select'" align="center" editoptions="{value:\"0:全权代理;1:部分代理\"}"></ui:Column>
				<ui:Column header="开始时间" name="startDate" width="120" formatter="dateFormatter"></ui:Column>
				<ui:Column header="结束时间" name="endDate" width="120" formatter="dateFormatter"></ui:Column>
				<ui:Column header="是否启用" name="enabled" width="90" edittype="select" formatter="'select'" align="center" editoptions="{value:\"1:启用;0:禁用\"}"></ui:Column>
				<ui:Column header="代理人" name="agent" width="90" formatter="userNameFormater"></ui:Column>
				<ui:Column header="创建时间" name="createTime" width="120" formatter="dateFormatter"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>