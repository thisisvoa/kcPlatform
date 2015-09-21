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
<title>角色信息列表</title>
</head>
<script type="text/javascript">
	var topWin = FrameHelper.getTop();
  	// 查看角色详细信息
  	function viewRole(id){
  		var selectedIds = $("#roleList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择待查看的角色！");
			return;
		}else if(selectedIds.length > 1){
			MessageUtil.alert("只能选择一条记录进行查看！");
		}else{
			topWin.Dialog.open({
	  			URL : "${ctx}/roleCtl/toViewRole.html?id="+selectedIds[0],
	  			Width : 500,
				Height : 280,
	  			Modal : false,
	  			Title : "角色详细信息"
	  		});
		}
	}
	//新增角色信息
	function addRole(){
		topWin.Dialog.open({
			URL : "${ctx}/roleCtl/toAddRole.html",
			ShowCloseButton : true,
			Width : 500,
			Height : 250,
			Title : "新增角色",
  			RefreshHandler : function(){
  				$("#roleList").trigger("reloadGrid");
  			}
		});
	}
	//编辑角色信息
	function editRole(){
		var selectedIds = $("#roleList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择待修改的角色！");
			return;
		}else if(selectedIds.length > 1){
			MessageUtil.alert("只能选择一条记录进行修改！");
		}else{
			topWin.Dialog.open({
				URL : "${ctx}/roleCtl/toEditRole.html?id="+selectedIds[0],
				ShowCloseButton : true,
				Width : 500,
				Height : 250,
				Title : "修改角色",
				RefreshHandler : function(){
					$("#roleList").trigger("reloadGrid");
				}
			});
		}
	}
		
	//禁用角色(可批量)
	function forbidRole(){
		var selectedIds = $("#roleList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要禁用的角色！");
			return;
		}
		MessageUtil.confirm("您确定禁用选中的角色吗？",function() {
			var url = "${ctx}/roleCtl/forbidRole.html";
			var params = {
				ids:selectedIds.join(",")
			};
			$.postc(url,params,function(data){
				MessageUtil.show("操作成功!");
				$("#roleList").trigger("reloadGrid");
			});
			
		});
	}
	//启用角色(可批量)
	function enabledRole(){
		var selectedIds = $("#roleList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要启用的角色！");
			return;
		}
		MessageUtil.confirm("您确定启用选中的角色吗？",function() {
			var url = "${ctx}/roleCtl/enabledRole.html";
			var params = {
				ids:selectedIds.join(",")
			};
			$.postc(url,params,function(data){
				MessageUtil.show("操作成功!");
				$("#roleList").trigger("reloadGrid");
			});
			
		});
	}
		
	//逻辑删除角色(可批量)
	function deleteRole(){
		var selectedIds = $("#roleList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要删除的角色！");
			return;
		}
		MessageUtil.confirm("您确定删除选中的角色吗？",function(){
			var roleIds = selectedIds.join(",");
			var url = "${ctx}/roleCtl/logicDelRole.html";
			var params = {
				ids:roleIds
			};
			$.postc(url,params,function(data){
				MessageUtil.show("操作成功!");
				$("#roleList").trigger("reloadGrid");
			});
			
		});
	}
	function queryRoleList(){
		$("#roleList").grid("setGridParam", {postData:{roleName:$("#roleName").val(),
														roleCode:$("#roleCode").val(),
														useTarg:$("#useTarg").combobox("getValue")
														}});
		$("#roleList").trigger("reloadGrid");
	}
	
	function allocateFunctions(){
		var selectedIds = $("#roleList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择待分配的角色！");
			return;
		}else if(selectedIds.length > 1){
			MessageUtil.alert("只能选择一条记录进行功能分配！");
		}else{
			topWin.Dialog.open({
				URL :"${ctx }/roleFunc/assignFuncToRolePage.html?roleId="+selectedIds[0],
				ShowCloseButton : true,
				Width : 700,
				Height : 450,
				Title : "角色功能分配"
			});
		}
	}
	function allocateusers(){
		var selectedIds = $("#roleList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择待分配的角色！");
			return;
		}else if(selectedIds.length > 1){
			MessageUtil.alert("只能选择一条记录进行用户分配！");
		}else{
			topWin.Dialog.open({
				URL :"${ctx }/userRole/assignUserToRolePage.html?roleId="+selectedIds[0],
				ShowCloseButton : true,
				Width : 700,
				Height : 450,
				Title : "角色用户分配"
			});
		}
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
</script>
<body style="padding:1px">
	<ui:LayoutContainer fit="true">
		<ui:Layout region="north" style="height:60px" title="角色基本信息" radius="true">
				<table class="formTable">
					<tr>
						<td class="label" style="width:100px">
							角色名称：
						</td>
						<td style="width:130px">
							<input type="text" id="roleName" name="roleName" class="easyui-textinput" watermark="支持模糊查询" title="按下回车键可触发查询"  onkeydown="queryEnter(event.keyCode||event.which)"/>
						</td>
						<td class="label" style="width:100px">
							角色代码：
						</td>
						<td style="width:130px">
							<input type="text" id="roleCode" name="roleCode" class="easyui-textinput" watermark="支持模糊查询" title="按下回车键可触发查询"  onkeydown="queryEnter(event.keyCode||event.which)"/>
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
		</ui:Layout>
		<ui:Layout region="center" border="false">
			<ui:LayoutContainer fit="true">
				<ui:Layout region="north" style="height:35px;" border="false">
					<div id="toolbar" class="easyui-toolbar" style="width:100%;margin-top:2px;">
						<kpm:HasPermission permCode="SYS_ROLE_QUERY">
						<a id="queryBtn" href="#" iconCls="icon-search" onclick="queryRoleList()">查询</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_ROLE_ADD">
							<a id="remove" href="#" iconCls="icon-add" onclick="addRole()">新增</a>
						</kpm:HasPermission>
						
						<kpm:HasPermission permCode="SYS_ROLE_UPDATE">
							<a id="edit" href="#" iconCls="icon-edit" onclick="editRole()">修改</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_ROLE_DELETE">
							<a id="remove" href="#" iconCls="icon-remove" onclick="deleteRole()">删除</a>
						</kpm:HasPermission>
						<a id="view" href="#" iconCls="icon-view" onclick="viewRole()">查看</a>
						<kpm:HasPermission permCode="SYS_ROLE_ENABLED">
							<a id="enable" href="#" iconCls="icon-ok" onclick="enabledRole()">启用</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_ROLE_FORBID">
							<a id="forbid" href="#" iconCls="icon-no" onclick="forbidRole()">禁用</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_ROLE_ASSIGN_USER">
							<a id="allocateFunctions" href="#" iconCls="icon-edit" onclick="allocateusers()">用户分配</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_ROLE_ASSIGN_FUNC">
							<a id="allocateRoles" href="#" iconCls="icon-edit" onclick="allocateFunctions()">功能分配</a>
						</kpm:HasPermission>
					</div>
				</ui:Layout>
				<ui:Layout region="center" style="padding-top:2px" border="false">
					<ui:Grid id="roleList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="true"
						url="${ctx}/roleCtl/roleList.html" multiselect="true"
						rowNum="20" rowList="[10,20,50]">
						<ui:Column name="zjid" key="true" hidden="true"></ui:Column>
						<ui:Column header="角色名称" name="jsmc" width="160"></ui:Column>
						<ui:Column header="角色代码" name="jsdm" width="160"></ui:Column>
						<ui:Column header="角色级别" name="jsjb" width="60" edittype="select" formatter="'select'" editoptions="{value:\"1:省级;2:市级;3:县级;4:派出所\"}"></ui:Column>
						<ui:Column header="使用标识" name="sybz" width="65" formatter="sybzFormater" align="center"></ui:Column>
					</ui:Grid>
				</ui:Layout>
			</ui:LayoutContainer>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>
