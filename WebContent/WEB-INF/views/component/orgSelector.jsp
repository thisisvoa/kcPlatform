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
var selectedOrgIds = [];
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
	var selectedOrgStr = parentDialog.getData().selectedOrg;
	if(selectedOrgStr!=null){
		selectedOrgIds = selectedOrgStr.split(',');
	}else{
		selectedOrgIds = [];
	}
	$("#selectedList").grid("setGridParam", {postData:{selectedOrg:selectedOrgStr},url:"${ctx}/orgCtl/cmp/selectedOrgList.html"});
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
	$("#orgNum").val("");
	$("#orgName").val("");
	$("#orgList").grid("setGridParam", {postData:{parentOrg:treeNode.id,orgNum:"", orgName:""}});
	$("#orgList").trigger("reloadGrid");
}
function queryOrg() {
	$("#orgList").grid("setGridParam", {postData:{parentOrg:"",orgNum:$("#orgNum").val(), orgName:$("#orgName").val()}});
	$("#orgList").trigger("reloadGrid");
	var treeObj = $("#orgTree").tree("getZTreeObj");
	treeObj.cancelSelectedNode();
}

function radioFormat(cellVal,options,rowd){
	var isSelected = false;
	for(var i=0;i<selectedOrgIds.length;i++){
		if(rowd.ZJID == selectedOrgIds[i]){
			isSelected = true;
			break;
		}
	}
	if(isSelected){
		return "<input id=\"ra_"+rowd.ZJID+"\" type=\"radio\" name=\"orgSelectRadio\" value=\""+rowd.ZJID+"\" checked=\"checked\" onclick=\"gridRadioCheck('"+rowd.ZJID+"')\">";
	}else{
		return "<input id=\"ra_"+rowd.ZJID+"\" type=\"radio\" name=\"orgSelectRadio\" value=\""+rowd.ZJID+"\" onclick=\"gridRadioCheck('"+rowd.ZJID+"')\">";
	}
}

function gridRadioCheck(orgId){
	selectedOrgIds = [orgId];
	var rowd = $("#orgList").grid("getRowData", orgId);
	$("#selectedList").grid("clearGridData");
	$("#selectedList").grid("addRowData", orgId, rowd);
}

function orgListComplete(){
	if("${multiselect}"=="false") return;
	var dataIDs = $("#orgList").grid("getDataIDs");
	for(var i=0;i<dataIDs.length;i++){
		var isExist = false;
		for(var j=0;j<selectedOrgIds.length;j++){
			if(dataIDs[i]==selectedOrgIds[j]){
				isExist = true;
				break;
			}
		}
		if(isExist){
			$("#orgList").grid("setSelection",dataIDs[i],false);
		}
	}
}

function onSelectRow(rowid,status){
	if("${multiselect}"=="false") return;
	if(status){
		if($("#selectedList").grid("getInd", rowid)==false){
			var rowd = $("#orgList").grid("getRowData", rowid);
			$("#selectedList").grid("addRowData", rowid, rowd);	
		}
	}else{
		if($("#selectedList").grid("getRowData", rowid)!=null){
			$("#selectedList").grid("delRowData", rowid);
		}
	}
	selectedOrgIds = $("#selectedList").grid("getDataIDs");
}

function onSelectAll(rowids,status){
	if("${multiselect}"=="false") return;
	if(status){
		for(var i=0;i<rowids.length;i++){
			var rowid = rowids[i];
			if($("#selectedList").grid("getInd", rowid)==false){
				var rowd = $("#orgList").grid("getRowData", rowid);
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
	var html = "<a href=\"javascript:void(0)\" onclick=\"removeOrg('"+rowd.ZJID+"')\" ><img src='${ctx}/ui/css/icon/images/delete.gif' style='border:none'></img></a>";
	return html;
}

function removeOrg(orgId){
	$("#selectedList").grid("delRowData", orgId);
	if($("#orgList").grid("getRowData", orgId)!=null){
		$("#orgList").grid("setSelection", orgId,false);	
	}
	selectedOrgIds = $("#selectedList").grid("getDataIDs");
}

function doSave(){
	var selectedOrgList = $("#selectedList").grid("getRowData");
	parentDialog.setData({selectedOrgList:selectedOrgList});
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
		<ui:Layout region="west" style="width:200px" title="单位树" split="true">
			<ul id="orgTree" class="easyui-tree" asyncEnable="true"  url="${ctx}/orgCtl/asynOrgTree.html" 
  				rootPId="0" simpleDataEnable="true" autoParam="['id']"
  				dataFilter="dataFilter" onClick="onOrgTreeClick" setting="setting"></ul>
		</ui:Layout>
		<ui:Layout region="center" title="单位列表">
			<ui:LayoutContainer fit="true">
				<ui:Layout region="north" style="height:80px">
					<form method="post" id="searchForm">
						<table class="formTable">
							<tr>
								<td class="label" style="width:100px">
									单位编码：
								</td>
								<td>
									<input type="text" id="orgNum" name="orgNum" class="easyui-textinput" watermark="支持模糊查询"/>
								</td>
								<td>
								</td>
							</tr>
							<tr>
								<td class="label" style="width:100px">
									单位名称：
								</td>
								<td style="width:150px">
									<input type="text" id="orgName" name="orgName" class="easyui-textinput" watermark="支持模糊查询"/>
								</td>
								<td>
									<input type="button" class="easyui-button" value="查询" onclick="queryOrg()">
								</td>
							</tr>
						</table>
					</form>
				</ui:Layout>
				<ui:Layout region="center">
					<ui:Grid id="orgList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="true"
						url="${ctx}/orgCtl/orgList.html" postData="" multiselect="${multiselect}" multiboxonly="${!multiselect}"
						rowNum="20" rowList="[10,20,50]" onSelectRow="onSelectRow" gridComplete="orgListComplete" onSelectAll="onSelectAll">
						<ui:Column name="ZJID" key="true" hidden="true"></ui:Column>
						<ui:Column header="" width="30" fixed="true" align="center" formatter="radioFormat" hidden="${multiselect}"></ui:Column>
						<ui:Column header="单位编码" name="DWBH" width="90" fixed="true"></ui:Column>
						<ui:Column header="单位名称" name="DWMC"  width="250" fixed="true"></ui:Column>
					</ui:Grid>
				</ui:Layout>
			</ui:LayoutContainer>
		</ui:Layout>
		<ui:Layout region="east" style="width:300px" title="已选择" split="true">
			<ui:Grid id="selectedList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="false"
						url="" postData="" multiselect="false"
						rowNum="9999" rownumbers="false">
				<ui:Column name="ZJID" key="true" hidden="true"></ui:Column>
				<ui:Column header="单位名称" name="DWMC"  width="220" fixed="true"></ui:Column>
				<ui:Column header="" width="40" formatter="operatorFormatter" align="center" fixed="true"></ui:Column>
			</ui:Grid>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>