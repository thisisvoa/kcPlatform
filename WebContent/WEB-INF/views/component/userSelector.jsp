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
var selectedUserIds = [];
var setting = {
		view: {
			fontCss: function(treeId, node){
				if(node.enable==false){
					return {color:"#ccc"};	
				}
			}
		}
};
$parsed(function(){
	setTimeout(function(){
		var selectedUserStr = window.parentDialog.getData().selectedUser;
		if(selectedUserStr!=null){
			selectedUserIds = selectedUserStr.split(',');
		}else{
			selectedUserIds = [];
		}
		$("#selectedList").grid("setGridParam", {postData:{selectedUser:selectedUserStr},url:"${ctx}/userCtl/cmp/selectedUserList.html"});
		$("#selectedList").trigger("reloadGrid");
	},10);
});
function dataFilter(event, parentNode, childNodes){
	if(!childNodes) return null;
	for(var i=0;i<childNodes.length;i++){
		childNodes[i].icon = "${ctx}/ui/css/icon/images/user_group.gif";
	}
	return childNodes;
}

function onOrgTreeClick(event, treeId, treeNode){
	if(treeNode.enable==false){
		return;
	}
	$("#userName").val("");
	$("#userNum").val("");
	$("#userList").grid("setGridParam", {postData:{orgId:treeNode.id,userName:"", userNum:""},url:"${ctx}/userCtl/userList.html?useTarg=1"});
	$("#userList").trigger("reloadGrid");
}

function queryUserList() {
	$("#userList").grid("setGridParam", {postData:{orgId:"",userName:$("#userName").val(), userNum:$("#userNum").val()},url:"${ctx}/userCtl/userList.html?useTarg=1"});
	$("#userList").trigger("reloadGrid");
	var treeObj = $("#orgTree").tree("getZTreeObj");
	treeObj.cancelSelectedNode();
}

function radioFormat(cellVal,options,rowd){
	var isSelected = false;
	for(var i=0;i<selectedUserIds.length;i++){
		if(rowd.ZJID == selectedUserIds[i]){
			isSelected = true;
			break;
		}
	}
	if(isSelected){
		return "<input id=\"ra_"+rowd.ZJID+"\" type=\"radio\" name=\"userSelectRadio\" value=\""+rowd.ZJID+"\" checked=\"checked\" onclick=\"gridRadioCheck('"+rowd.ZJID+"')\">";
	}else{
		return "<input id=\"ra_"+rowd.ZJID+"\" type=\"radio\" name=\"userSelectRadio\" value=\""+rowd.ZJID+"\" onclick=\"gridRadioCheck('"+rowd.ZJID+"')\">";
	}
}

function gridRadioCheck(userId){
	selectedUserIds = [userId];
	var rowd = $("#userList").grid("getRowData", userId);
	$("#selectedList").grid("clearGridData");
	$("#selectedList").grid("addRowData", userId, rowd);
}

function userListComplete(){
	if("${multiselect}"=="false") return;
	var dataIDs = $("#userList").grid("getDataIDs");
	for(var i=0;i<dataIDs.length;i++){
		var isExist = false;
		for(var j=0;j<selectedUserIds.length;j++){
			if(dataIDs[i]==selectedUserIds[j]){
				isExist = true;
				break;
			}
		}
		if(isExist){
			$("#userList").grid("setSelection",dataIDs[i],false);
		}
	}
}

function onSelectRow(rowid,status){
	if("${multiselect}"=="false") return;
	if(status){
		if($("#selectedList").grid("getRowData", rowid)!=null){
			var rowd = $("#userList").grid("getRowData", rowid);
			$("#selectedList").grid("addRowData", rowid, rowd);	
		}
	}else{
		if($("#selectedList").grid("getRowData", rowid)!=null){
			$("#selectedList").grid("delRowData", rowid);
		}
	}
	selectedUserIds = $("#selectedList").grid("getDataIDs");
}

function onSelectAll(rowids,status){
	if("${multiselect}"=="false") return;
	if(status){
		for(var i=0;i<rowids.length;i++){
			var rowid = rowids[i];
			if($("#selectedList").grid("getInd", rowid)==false){
				var rowd = $("#userList").grid("getRowData", rowid);
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
	var html = "<a href=\"javascript:void(0)\" onclick=\"removeUser('"+rowd.ZJID+"')\" ><img src='${ctx}/ui/css/icon/images/delete.gif' style='border:none'></img></a>";
	return html;
}

function removeUser(userId){
	$("#selectedList").grid("delRowData", userId);
	if($("#userList").grid("getRowData", userId)!=null){
		$("#userList").grid("setSelection", userId,false);	
	}
	selectedUserIds = $("#selectedList").grid("getDataIDs");
}

function doSave(){
	var selectedUserList = $("#selectedList").grid("getRowData");
	parentDialog.setData({selectedUserList:selectedUserList});
	parentDialog.markUpdated();
	parentDialog.close();
}

function queryEnter(e){
	if(e == 13){
		$("#roleList").grid("setGridParam", {postData:{roleName:$("#roleName").val()}});
		$("#roleList").trigger("reloadGrid");
	}
}

function roleRowDbClick(rowid,iRow,iCol,e){
	$("#userName").val("");
	$("#userNum").val("");
	$("#userList").grid("setGridParam", {postData:{roleId:rowid},url:"${ctx}/userCtl/findUserByRole.html"});
	$("#userList").trigger("reloadGrid");
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
		<ui:Layout region="west" style="width:210px" iconCls="icon-user" split="true">
			<div style="overflow: hidden;" id="myaccrdion" border="true" fit="true" class="easyui-accordion">
				<div title="按单位查找" icon="icon-user">
					<ul id="orgTree" class="easyui-tree" asyncEnable="true"  url="${ctx}/orgCtl/asynOrgTree.html" 
		  				rootPId="0" simpleDataEnable="true" autoParam="['id']"
		  				dataFilter="dataFilter" onClick="onOrgTreeClick" setting="setting"></ul>
				</div>
				<div title="按角色查找" icon="icon-user" fit="true">
					<div>
						<table class="formTable">
						<tr>
							<td class="label" style="width:70px">
								角色名称：
							</td>
							<td>
								<input type="text" id="roleName" name="roleName"
									class="easyui-textinput" watermark="支持模糊查询"
									style="width: 100px" title="按下回车键可触发查询"
									onkeydown="queryEnter(event.keyCode||event.which)" />
								</td>
						</tr>
					</table>
					</div>
					<div style="height:300px;width:208px">
						<ui:Grid id="roleList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="true"
							url="${ctx}/roleCtl/roleList.html?useTarg=1" multiselect="false" rownumbers="false"
							rowNum="20" rowList="[10,20,50]" pginput="false" ondblClickRow="roleRowDbClick">
							<ui:Column name="zjid" key="true" hidden="true"></ui:Column>
							<ui:Column header="角色名称" name="jsmc" width="160" align="center"></ui:Column>
						</ui:Grid>
					</div>
				</div>
        	</div>
		</ui:Layout>
		<ui:Layout region="center" title="用户列表">
			<ui:LayoutContainer fit="true">
				<ui:Layout region="north" style="height:40px">
					<table class="formTable">
						<tr>
							<td class="label" style="width:100px">
								警员编号：
							</td>
							<td>
								<input type="text" class="easyui-textinput" id="userNum" name="userNum" watermark="支持模糊查询"/>
							</td>
							
							<td class="label" style="width:100px">
								用户姓名：
							</td>
							<td>
								<input type="text" class="easyui-textinput" id="userName" name="userName" watermark="支持模糊查询"/>
							</td>
							<td style="width:60px">
								<input id="queryBtn" type="button" class="easyui-button" value="查询" onclick="queryUserList()">
							</td>
						</tr>
					</table>
				</ui:Layout>
				<ui:Layout region="center" style="padding-top:2px" border="false">
					<ui:Grid id="userList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="true"
							multiselect="${multiselect}" multiboxonly="${!multiselect}" gridComplete="userListComplete" url="${ctx}/userCtl/userList.html?useTarg=1"
							rowNum="20" rowList="[10,20,50]" onSelectRow="onSelectRow" onSelectAll="onSelectAll">
							<ui:Column name="ZJID" key="true" hidden="true"></ui:Column>
							<ui:Column header="" width="30" fixed="true" align="center" formatter="radioFormat" hidden="${multiselect}"></ui:Column>
							<ui:Column header="警员编号" name="JYBH" width="100" fixed="true"></ui:Column>
							<ui:Column header="所在单位" name="DWMC"  width="180" fixed="true"></ui:Column>
							<ui:Column header="用户姓名" name="YHMC" width="90" fixed="true"></ui:Column>
					</ui:Grid>
				</ui:Layout>
			</ui:LayoutContainer>
		</ui:Layout>
		<ui:Layout region="east" style="width:200px" title="已选择" split="true">
			<ui:Grid id="selectedList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="false"
						url="" postData="" multiselect="false"
						rowNum="9999" rownumbers="false">
				<ui:Column name="ZJID" key="true" hidden="true"></ui:Column>
				<ui:Column header="姓名" name="YHMC"  width="130" fixed="true"></ui:Column>
				<ui:Column header="" width="40" formatter="operatorFormatter" align="center" fixed="true"></ui:Column>
			</ui:Grid>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>