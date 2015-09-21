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
  	
	//新增用户信息
	function addUser(){
		topWin.Dialog.open({
			URL : "${ctx}/userCtl/toAddUser.html?orgId="+$("#orgId").val(),
			ShowCloseButton : true,
			Width: 500,
			Height : 540,
			Title : "新增用户",
			RefreshHandler : function(){
				$("#userList").trigger("reloadGrid");
			}
		});   
	}
	
	//编辑用户信息
	function eidtUser(){
		var selectedIds = $("#userList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择待修改的用户！");
			return;
		}else if(selectedIds.length > 1){
			MessageUtil.alert("只能选择一条记录进行修改！");
		}else{
			topWin.Dialog.open({
				URL : "${ctx}/userCtl/toEditUser.html?id="+selectedIds[0],
				ShowCloseButton : true,
				Width: 500,
				Height : 540,
				Title : "修改用户",
				RefreshHandler : function(){
					$("#userList").trigger("reloadGrid");
				}
			}); 
		}
		
	}
	//禁用用户(批量)
	function forbidUsers(){
		var selectedIds = $("#userList").grid("getGridParam", "selarrrow");
		forbidUser(selectedIds.join(","));
	}
	
	//禁用用户(可批量)
	function forbidUser(userIds){
		if (userIds == null || userIds.length <= 0) {
			MessageUtil.alert("请选择要禁用的用户！");
			return;
		}
		MessageUtil.confirm("您确定禁用选中的用户吗？",function() {				
			var url = "${ctx}/userCtl/forbidUser.html";
			var params = {
				ids:userIds
			};
			$.postc(url,params,function(data){
				MessageUtil.show("操作成功");
				$("#userList").trigger("reloadGrid");
			});
		});
	}
	
	//禁用用户(批量)
	function enabledUsers(){
		var selectedIds = $("#userList").grid("getGridParam", "selarrrow");
		enabledUser(selectedIds.join(","));
	}
	//启用用户(可批量)
	function enabledUser(userIds){
		if (userIds == null || userIds.length <= 0) {
			MessageUtil.alert("请选择要启用的用户！");
			return;
		}
		MessageUtil.confirm("您确定启用选中的用户吗？",function() {
				var url = "${ctx}/userCtl/enabledUser.html";
				var params = {
					ids:userIds
				};
				$.postc(url,params,function(data){
					MessageUtil.show("操作成功");
					$("#userList").trigger("reloadGrid");
				});
				
			});
	}				
	//重置密码(可批量)
	function resetPsw(){
		var selectedIds = $("#userList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择密码重置的用户！");
			return;
		}
		MessageUtil.confirm("您确定重置选中的用户密码吗？", function() {
			var userIds = selectedIds.join(",");
			var url = "${ctx}/userCtl/resetPsw.html";
			var params = {
				ids:userIds
			};
			$.postc(url,params,function(data){
					MessageUtil.show("操作成功");
				});
			
		}, function(){
			return;
		});			
	}
		
	//逻辑删除用户(可批量)
	function deleteUser(){
		var selectedIds = $("#userList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要删除的用户！");
			return;
		}
		MessageUtil.confirm("您确定删除选中的用户吗？", function() {
			var userIds = selectedIds.join(",");
			var url = "${ctx}/userCtl/logicDelUser.html";
			var params = {
				ids:userIds
			};
			$.postc(url,params,function(data){
				MessageUtil.show("操作成功!");
				$("#userList").trigger("reloadGrid");
			});
			
		});
	}
		
	//用户调动(可批量)
	function transfer(){
		var selectedIds = $("#userList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要调动的用户！");
			return;
		}
		var userIds = selectedIds.join(",");
		topWin.Dialog.open(
				{URL:"${ctx}/userCtl/toTransfer.html?ids="+userIds,
				ShowCloseButton:true,
				Width:500,
				Height:500,
				Title:"请选择调动单位信息",
				RefreshHandler : function(){
					$("#userList").trigger("reloadGrid");
				}});
	}
	
	/**
	 * 查询
	 */
	function queryUserList() {
		parent.window.cancelSelectedNode();
		$("#orgId").val("");
		$("#userList").grid("setGridParam", {postData:{orgId:"",userName:$("#userName").val(), 
				loginName:$("#loginName").val(), userNum:$("#userNum").val(), useTarg:$("#useTarg").combobox("getValue")}});
		$("#userList").trigger("reloadGrid");
	}
	
	function allocateRoles(){
		var selectedIds = $("#userList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择待分配的用户！");
			return;
		}else if(selectedIds.length > 1){
			MessageUtil.alert("只能选择一条记录进行角色分配！");
		}else{
			topWin.Dialog.open({
				URL : "${ctx}/userRole/assignRoleToUserPage.html?userId="+selectedIds[0],
				ShowCloseButton : true,
				Width : 700,
				Height : 450,
				Title : "用户角色分配"
			});
		}
	}
	
	function viewUser(userId){
		topWin.Dialog.open({
 			URL : "${ctx}/userCtl/toViewUser.html?id="+userId,
 			Width: 500,
			Height : 540,
 			Modal : false,
 			Title : "用户详细信息"
 		});
	}
	
	function viewUserFormatter(cellVal,options,rowd){
		var html = "";
		html += "<a style=\"color: blue\" onclick=\"javascript:viewUser('"+rowd.ZJID+"');\">" + cellVal + "</a>";
		return html;
	}
	
	function sybzFormater(cellVal,options,rowd){
		if(cellVal=="0"){
			return "<span style='color:red'>禁用</span>";
		}else{
			return "启用";
		}
	}
	function onGridComplete(){
		using("button", function(){
			$(".easyui-button").button();	
		});
	}
	function queryEnter(e){
		if(e == 13){
			$("#queryBtn").click();	
		}
	}
	
	function viewQx(){
		var selectedIds = $("#userList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择要查看的用户！");
			return;
		}else if(selectedIds.length > 1){
			MessageUtil.alert("只能选择一条记录！");
		}else{
			topWin.Dialog.open({
				URL : "${ctx}/userCtl/toViewUserPermission.html?userId="+selectedIds[0],
				ShowCloseButton : true,
				Width : 500,
				Height : 450,
				Title : "用户权限一览"
			});
		}
	}
</script>
</head>
<body style="padding:1px"  >
	<input id="orgId" type="hidden" value="${orgId}">
	<ui:LayoutContainer fit="true">
		<ui:Layout region="north" style="height:100px" title="查询条件" radius="true">
			<form method="post" id="searchForm">
				<table class="formTable">
					<tr>
						<td class="label" style="width:100px">
							警员编号：
						</td>
						<td>
							<input type="text" class="easyui-textinput" id="userNum" name="userNum" watermark="支持模糊查询" title="按下回车键可触发查询" onkeydown="queryEnter(event.keyCode||event.which)"/>
						</td>
						
						<td class="label" style="width:100px">
							用户姓名：
						</td>
						<td>
							<input type="text" class="easyui-textinput" id="userName" name="userName" watermark="支持模糊查询" title="按下回车键可触发查询" onkeydown="queryEnter(event.keyCode||event.which)"/>
						</td>
					</tr>
					<tr>
						<td class="label" style="width:100px">
							用户账号：
						</td>
						<td>
							<input type="text" class="easyui-textinput" id="loginName" name="loginName" watermark="支持模糊查询" title="按下回车键可触发查询" onkeydown="queryEnter(event.keyCode||event.which)"/>
						</td>
						<td class="label" style="width:100px">
							使用标识：
						</td>
						<td>
							<select class="easyui-combobox" id="useTarg" name="useTarg" dropdownHeight="80">
								<option value="">--请选择--</option>
								<option value="1">启用</option>
								<option value="0">禁用</option>
							</select>
						</td>
					</tr>
				</table>
			</form>
		</ui:Layout>
		<ui:Layout region="center" border="false">
			<ui:LayoutContainer fit="true">
				<ui:Layout region="north" style="height:35px;" border="false">
					<div id="toolbar" class="easyui-toolbar" style="width:100%;margin-top:2px">
						<kpm:HasPermission permCode="SYS_USER_QUERY">
						<a id="queryBtn" href="#" iconCls="icon-search" onclick="queryUserList()">查询</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_USER_ADD">
							<a id="remove" href="#" iconCls="icon-add" onclick="addUser()">新增</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_USER_UPDATE">
							<a id="remove" href="#" iconCls="icon-edit" onclick="eidtUser()">修改</a>
						</kpm:HasPermission>
						
						<kpm:HasPermission permCode="SYS_USER_ASSIGN_ROLE">
							<a id="allocateRoles" href="#" iconCls="icon-edit" onclick="allocateRoles()">角色分配</a>
						</kpm:HasPermission>
						
						<kpm:HasPermission permCode="SYS_USER_RESET_PWD">
							<a id="resetPwd" href="#" iconCls="icon-key" onclick="resetPsw()">密码重置</a>
						</kpm:HasPermission>
						
						<kpm:HasPermission permCode="SYS_USER_TRANSFER">
							<a id="allocateRoles" href="#" iconCls="icon-edit" onclick="transfer()">调动</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_USER_ENABLED">
							<a id="enable" href="#" iconCls="icon-ok" onclick="enabledUsers()">启用</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_USER_FORBID">
							<a id="forbid" href="#" iconCls="icon-no" onclick="forbidUsers()">禁用</a>
						</kpm:HasPermission>
						<a id="viewQx" href="#" iconCls="icon-search" onclick="viewQx()">查看权限</a>
					</div>
				</ui:Layout>
				<ui:Layout region="center" style="padding-top:2px" border="false">
					<ui:Grid id="userList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="true"
							url="${ctx}/userCtl/userList.html" postData="{orgId:'${orgId}'}" multiselect="true"
							rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
							<ui:Column name="ZJID" key="true" hidden="true"></ui:Column>
							<ui:Column header="警员编号" name="JYBH" width="100" fixed="true" formatter="viewUserFormatter"></ui:Column>
							<ui:Column header="所在单位" name="DWMC"  width="180" fixed="true"></ui:Column>
							<ui:Column header="用户姓名" name="YHMC" width="90" fixed="true"></ui:Column>
							<ui:Column header="用户账号" name="YHDLZH" width="70" fixed="true"></ui:Column>
							<ui:Column header="性别" width="45" name="XB" edittype="select" formatter="'select'" align="center" editoptions="{value:\"0:女;1:男\"}" fixed="true"></ui:Column>
							<ui:Column header="用户级别" name="YHJB" fixed="true" width="60" edittype="select" formatter="'select'" editoptions="{value:\"1:省级;2:市级;3:县级;4:派出所\"}"></ui:Column>
							<ui:Column header="使用标识" width="60" name="SYBZ" formatter="sybzFormater" fixed="true" align="center"></ui:Column>
					</ui:Grid>
				</ui:Layout>
			</ui:LayoutContainer>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>
