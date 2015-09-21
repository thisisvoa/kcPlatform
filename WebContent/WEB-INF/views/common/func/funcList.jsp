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
	<title>功能信息管理</title>
	<script type="text/javascript">
	var topWin = FrameHelper.getTop();
	function updateSYBZ(sybz) {
		var msg = "";
		if(sybz=='1'){
			msg = "确定要启用该功能吗?";
		}else{
			msg = "确定要禁用该功能吗?";
		}
		var zjIds = mutilSelect();
		if(zjIds != null && zjIds != ''){
			topWin.Dialog.confirm(msg,function(){
				$.postc(encodeURI("${ctx}/func/funcSave.html?type=sybz&zjIds=" + zjIds + "&sybz=" + sybz),null,function(data){
					if(data == 1){
						MessageUtil.show("操作成功!");
						$("#funcList").trigger('reloadGrid');
					}else if(data == 0){
						MessageUtil.show("操作失败，请联系系统管理员！");
					}
				});
			});
		}
	}
	
	function itemView() {
		var zjId = singleSelect();
		if(zjId != null && zjId != ''){
			topWin.Dialog.open({URL:"${ctx}/func/funcView.html?zjId=" + zjId, ShowCloseButton:true, Width:600, Height:400, Title:"查看功能信息", Modal:false});
		}
	}
	
	function itemEdit() {
		var zjId = singleSelect();
		if(zjId != null && zjId != ''){
			topWin.Dialog.open(
				{URL:"${ctx}/func/funcEdit.html?zjId=" + zjId, 
				 ShowCloseButton:true, 
				 Width:600, 
				 Height:400, 
				 Title:"修改功能信息",
				 RefreshHandler : function(){
					 $("#funcList").trigger('reloadGrid');
				 }
			});
		}
	}
	
	function refreshFuncList(){
		$("#funcList").trigger('reloadGrid');
	}
	
	function itemDelete() {
		var zjIds = mutilSelect();
		if(zjIds != null && zjIds != ''){
			MessageUtil.confirm("确定要删除所勾选的功能项吗?", function() {
				$.postc("${ctx}/func/funcSave.html?type=del&zjIds=" + zjIds,null,function(data){
					if(data == 1){
						MessageUtil.show("操作成功!");
						$("#funcList").trigger('reloadGrid');
					}else if(data == 0){
						MessageUtil.show("操作失败，请联系系统管理员！");
					}
				});
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
	
	function query(){
		var parms = $("#queryForm").serialize();
		$("#funcList").grid("setGridParam",{
			url:"${ctx}/func/funcListData.html?"+encodeURI(parms)
		});
		$("#funcList").trigger("reloadGrid");
	}
	
	function queryEnter(e){
		if(e == 13){
			$("#queryBtn").click();	
		}
	}
	
	//新增功能信息
	function addFunc(){
		var id = $("#id").val();
		if(id != null && id != ''){
			var topWin = FrameHelper.getTop();
			var diag = new topWin.Dialog();
			diag.Title = "新增功能";
			diag.URL = '${ctx}/func/funcAdd.html?menuId=' + id;
			diag.Width = 600;
			diag.Height = 400;
			diag.RefreshHandler = function(){
				$("#funcList").trigger("reloadGrid");
			};
			diag.show();
		}else{
			MessageUtil.alert("请先选择菜单!");
		}
	}
	
	//单选
	function singleSelect(){
		var selectedIds = $("#funcList").grid("getGridParam", "selarrrow");
		var returnValue = "";
		if (selectedIds.length == 0){
			MessageUtil.alert("请先最少选择一个功能项！");
		}else if(selectedIds.length > 1){
			MessageUtil.alert("只能选择一个功能项！");
		}else{
			returnValue = selectedIds;
		}
		
		return returnValue;
	}
	
	//多选
	function mutilSelect(){
		var selectedIds = $("#funcList").grid("getGridParam", "selarrrow");
		var returnValue = "";
		if (selectedIds.length == 0){
			MessageUtil.alert("请先选择一个功能项！");
		}else{
			returnValue = selectedIds;
		}
		
		return returnValue;
	}
	</script>
</head>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:60px" title="查询条件" radius="true">
	
		<form method="post" id="queryForm">
			<input type="hidden" value="${org.sjdw_zjid}" id="parentOrg"/>
			<input type="hidden" id="id" name="id" value="${id}">
				<table class="formTable">
					<tr>
						<td class="label" width="100px;">所属菜单：</td>
						<td width="130px;">
						<input type="text" id="belongMenu" name="belongMenu" watermark="支持模糊查询" class="easyui-textinput" value="" title="按下回车键可触发查询" style="width:110px" onkeydown="queryEnter(event.keyCode||event.which)"/>
						</td>
						<td class="label" width="100px;">功能名称：</td>
						<td width="130px;">	
							<input type="text" id="funName" name="funName" watermark="支持模糊查询" class="easyui-textinput" value="" title="按下回车键可触发查询" style="width:110px" onkeydown="queryEnter(event.keyCode||event.which)"/>
						</td>
						<td class="label" width="100px;">使用标识：</td>
						<td>
						<select class="easyui-combobox" id="useFlag" name="useFlag" selValue="" style="width:90px" dropdownHeight="75">
								<option value="">---请选择---</option>
								<option value="1" >启用</option>
								<option value="0" >禁用</option>
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
					<kpm:HasPermission permCode="SYS_FUNC_QUERY">
					<a id="queryBtn" href="#" iconCls="icon-search" onclick="query()">查询</a>
					</kpm:HasPermission>
					<kpm:HasPermission permCode="SYS_FUC_ADD">
						<a id="add" href="#" iconCls="icon-add" onclick="addFunc()">新增</a>
					</kpm:HasPermission>
					<kpm:HasPermission permCode="SYS_FUNC_UPDATE">
						<a id="edit" href="#" iconCls="icon-edit" onclick="itemEdit();">修改</a>
					</kpm:HasPermission>
					<kpm:HasPermission permCode="SYS_FUNC_DELETE">
						<a id="del" href="#" iconCls="icon-remove" onclick="itemDelete();">删除</a>
					</kpm:HasPermission>
					<kpm:HasPermission permCode="SYS_FUNC_VIEW">
					<a id="view" href="#" iconCls="icon-view" onclick="itemView();">查看</a>
					</kpm:HasPermission>
					<kpm:HasPermission permCode="SYS_FUNC_ENABLED">
						<a id="qy" href="#" iconCls="icon-ok" onclick="updateSYBZ(1);">启用</a>
					</kpm:HasPermission>
					<kpm:HasPermission permCode="SYS_FUNC_FORBID">
						<a id="jy" href="#" iconCls="icon-no" onclick="updateSYBZ(0);">禁用</a>
					</kpm:HasPermission>
				</div>
			</ui:Layout>
			<ui:Layout region="center" border="false" style="padding-top:2px">
				<ui:Grid id="funcList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="true"
					url="${ctx}/func/funcListData.html?id=${id}" multiselect="true"
					rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
					<ui:Column name="zjId" key="true" hidden="true"></ui:Column>
					<ui:Column header="所属菜单" name="cdxxCdmc" width="150" fixed="true"></ui:Column>
					<ui:Column header="功能名称" name="gnmc"  width="140" fixed="true"></ui:Column>
					<ui:Column header="功能代号" name="gndm" width="130" fixed="true"></ui:Column>
					<ui:Column header="使用标识" name="sybz" align="center" width="60" formatter="sybzFormater" fixed="true"></ui:Column>
				</ui:Grid>
			</ui:Layout>
		</ui:LayoutContainer>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>
