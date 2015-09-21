<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="ui" uri="/tags/ui"%>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
<script type="text/javascript">
var selectedRoleIds=[];
$parsed(function(){
	setTimeout(function(){
		var selectedRoleStr = parentDialog.getData().selectedRole;
		if(selectedRoleStr!=null){
			selectedRoleIds = selectedRoleStr.split(',');
		}else{
			selectedRoleIds = [];
		}
		$("#selectedList").grid("setGridParam", {postData:{selectedRole:selectedRoleStr},url:"${ctx}/roleCtl/cmp/selectedRoleList.html"});
		$("#selectedList").trigger("reloadGrid");
	},10);
});

function queryRoleList(){
	$("#roleList").grid("setGridParam", {postData:{roleName:$("#roleName").val(), roleCode:$("#roleCode").val()}});
	$("#roleList").trigger("reloadGrid");
}

function radioFormat(cellVal,options,rowd){
	var isSelected = false;
	for(var i=0;i<selectedRoleIds.length;i++){
		if(rowd.ZJID == selectedRoleIds[i]){
			isSelected = true;
			break;
		}
	}
	if(isSelected){
		return "<input id=\"ra_"+rowd.ZJID+"\" type=\"radio\" name=\"roleSelectRadio\" value=\""+rowd.zjid+"\" checked=\"checked\" onclick=\"gridRadioCheck('"+rowd.zjid+"')\">";
	}else{
		return "<input id=\"ra_"+rowd.ZJID+"\" type=\"radio\" name=\"roleSelectRadio\" value=\""+rowd.zjid+"\" onclick=\"gridRadioCheck('"+rowd.zjid+"')\">";
	}
}

function gridRadioCheck(roleId){
	selectedRoleIds = [roleId];
	var rowd = $("#roleList").grid("getRowData", roleId);
	$("#selectedList").grid("clearGridData");
	$("#selectedList").grid("addRowData", roleId, rowd);
}

function roleListComplete(){
	if("${multiselect}"=="false") return;
	var dataIDs = $("#roleList").grid("getDataIDs");
	for(var i=0;i<dataIDs.length;i++){
		var isExist = false;
		for(var j=0;j<selectedRoleIds.length;j++){
			if(dataIDs[i]==selectedRoleIds[j]){
				isExist = true;
				break;
			}
		}
		if(isExist){
			$("#roleList").grid("setSelection",dataIDs[i],false);
		}
	}
}

function onSelectRow(rowid,status){
	if("${multiselect}"=="false") return;
	if(status){
		if($("#selectedList").grid("getInd", rowid)==false){
			var rowd = $("#roleList").grid("getRowData", rowid);
			$("#selectedList").grid("addRowData", rowid, rowd);	
		}
	}else{
		if($("#selectedList").grid("getRowData", rowid)!=null){
			$("#selectedList").grid("delRowData", rowid);
		}
	}
	selectedRoleIds = $("#selectedList").grid("getDataIDs");
}

function onSelectAll(rowids,status){
	if("${multiselect}"=="false") return;
	if(status){
		for(var i=0;i<rowids.length;i++){
			var rowid = rowids[i];
			if($("#selectedList").grid("getInd", rowid)==false){
				var rowd = $("#roleList").grid("getRowData", rowid);
				$("#selectedList").grid("addRowData", rowid, rowd);	
			}
		}
	}else{
		for(var i=0;i<rowids.length;i++){
			var rowid = rowids[i];
			if($("#selectedList").grid("getRowData", rowid)!=null){
				$("#selectedList").grid("delRowData", rowid);
			}
		}
	}
}

function operatorFormatter(cellVal,options,rowd){
	var html = "<a href=\"javascript:void(0)\" onclick=\"removeRole('"+rowd.zjid+"')\" ><img src='${ctx}/ui/css/icon/images/delete.gif' style='border:none'></img></a>";
	return html;
}

function removeRole(roleId){
	$("#selectedList").grid("delRowData", roleId);
	if($("#roleList").grid("getRowData", roleId)!=null){
		$("#roleList").grid("setSelection", roleId,false);	
	}
	selectedRoleIds = $("#selectedList").grid("getDataIDs");
}

function doSave(){
	var selectedRoleList = $("#selectedList").grid("getRowData");
	parentDialog.setData({selectedRoleList:selectedRoleList});
	parentDialog.markUpdated();
	parentDialog.close();
}
</script>
</head>
<body>
<ui:LayoutContainer fit="true">
		<ui:Layout region="north" style="height:33px">
			<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
				<a id="saveBtn" href="#" iconCls="icon-save" onclick="doSave()">保存</a>
				<a id="close" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
			</div>
		</ui:Layout>
		<ui:Layout region="center" title="角色列表">
			<ui:LayoutContainer fit="true">
				<ui:Layout region="north" style="height:65px">
					<table class="formTable">
					<tr>
						<td class="label" style="width:100px">
							角色名称：
						</td>
						<td>
							<input type="text" id="roleName" name="roleName" class="easyui-textinput" watermark="支持模糊查询"/>
						</td>
					</tr>
					<tr>
						<td class="label" style="width:100px">
							角色代码：
						</td>
						<td>
							<input type="text" id="roleCode" name="roleCode" class="easyui-textinput" watermark="支持模糊查询"/>
						</td>
						<td style="width:60px">
							<input id="queryBtn" type="button" class="easyui-button" value="查询" onclick="queryRoleList()">
						</td>
					</tr>
					</table>
				</ui:Layout>
				<ui:Layout region="center" style="padding-top:2px" border="false">
					<ui:Grid id="roleList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="true"
						url="${ctx}/roleCtl/roleList.html" multiselect="${multiselect}" multiboxonly="${!multiselect}"
						rowNum="20" rowList="[10,20,50]" onSelectRow="onSelectRow" gridComplete="roleListComplete" onSelectAll="onSelectAll">
						<ui:Column name="zjid" key="true" hidden="true"></ui:Column>
						<ui:Column header="" width="30" fixed="true" align="center" formatter="radioFormat" hidden="${multiselect}"></ui:Column>
						<ui:Column header="角色名称" name="jsmc" width="160"></ui:Column>
						<ui:Column header="角色代码" name="jsdm" width="160"></ui:Column>
					</ui:Grid>
				</ui:Layout>
			</ui:LayoutContainer>
		</ui:Layout>
		<ui:Layout region="east" style="width:200px" title="已选择" split="true">
			<ui:Grid id="selectedList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="false"
						url="" postData="" multiselect="false"
						rowNum="9999" rownumbers="false">
				<ui:Column name="zjid" key="true" hidden="true"></ui:Column>
				<ui:Column header="角色名称" name="jsmc"  width="130" fixed="true"></ui:Column>
				<ui:Column header="" width="40" formatter="operatorFormatter" align="center" fixed="true"></ui:Column>
			</ui:Grid>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>