<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="/tags/ui"%>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
<script type="text/javascript" src="${ctx}/ui/platform/workflow/bpm/FlowUtil.js"></script>
<script type="text/javascript">
	var topWin = FrameHelper.getTop();
	//查询
	function query() {
		var postData = {deploymentId:""};
		postData.subject = $("#subject").val();
		postData.defKey = $("#defKey").val();
		postData.startTime = $("#startTime").val();
		postData.endTime = $("#endTime").val();
		$("#bpmDefList").grid("setGridParam", {
			postData : postData
		});
		$("#bpmDefList").trigger("reloadGrid");
	}
	
	function onGridComplete() {
		using("linkbutton", function() {
			$(".easyui-linkbutton").linkbutton();
		});
	}
	
	function refreshGrid(){
		$("#bpmDefList").trigger("reloadGrid");
	}
	
	function onSelectRow(rowid,status){
		if(status){
			if($("#selectedList").grid("getRowData", rowid)!=null){
				var rowd = $("#bpmDefList").grid("getRowData", rowid);
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
		if(status){
			for(var i=0;i<rowids.length;i++){
				var rowid = rowids[i];
				if($("#selectedList").grid("getInd", rowid)==false){
					var rowd = $("#bpmDefList").grid("getRowData", rowid);
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
	
	function doSave(){
		var selectedList = $("#selectedList").grid("getRowData");
		parentDialog.setData(selectedList);
		parentDialog.markUpdated();
		parentDialog.close();
	}
	
	function operatorFormatter(cellVal,options,rowd){
		var html = "<a href=\"javascript:void(0)\" onclick=\"removeDef('"+rowd.defId+"')\" ><img src='${ctx}/ui/css/icon/images/delete.gif' style='border:none'></img></a>";
		return html;
	}
	
	function removeDef(id){
		$("#selectedList").grid("delRowData", id);
		if($("#bpmDefList").grid("getRowData", id)!=null){
			$("#bpmDefList").grid("setSelection", id,false);
		}
	}
</script>
</head>
<body>
	<ui:LayoutContainer fit="true" id="main">
		<ui:Layout region="north" style="height:70px;">
			<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
				<a id="saveBtn" href="#" iconCls="icon-save" onclick="doSave()">保存</a>
				<a id="close" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
			</div>
			<form method="post" id="queryForm">
				<table class="formTable">
					<tr>
						<td class="label" style="width: 100px">流程名称：</td>
						<td style="width: 130px">
							<input type="text" class="easyui-textinput" id="subject" name="subject" watermark="支持模糊查询" />
						</td>
						<td class="label" style="width: 100px">流程编号：</td>
						<td >
							<input type="text" class="easyui-textinput" id="defKey" name="defKey" watermark="支持模糊查询" />
						</td>
						<td style="width:60px">
								<input id="queryBtn" type="button" class="easyui-button" value="查询" onclick="query()">
							</td>
					</tr>
				</table>
			</form>
		</ui:Layout>
		<ui:Layout region="center" border="false" style="padding-top:2px">
			<ui:Grid id="bpmDefList" datatype="json" shrinkToFit="false"
				fit="true" viewrecords="true" pageable="true" multiboxonly="false"
				url="${ctx}/workflow/bpmDef/myList.html" multiselect="true"
				rowNum="20" rowList="[10,20,50]" onSelectRow="onSelectRow" onSelectAll="onSelectAll">
				<ui:Column name="defId" key="true" hidden="true"></ui:Column>
				<ui:Column header="标题" name="subject" width="180"></ui:Column>
				<ui:Column header="流程编号" name="defKey" width="100"></ui:Column>
				<ui:Column header="流程分类" name="catalogName" width="80"></ui:Column>
				<ui:Column header="版本号" name="version" width="60" align="center"></ui:Column>
			</ui:Grid>
		</ui:Layout>
		<ui:Layout region="east" style="width:200px" title="已选中流程">
			<ui:Grid id="selectedList" datatype="json" shrinkToFit="false"
					fit="true" viewrecords="true" pageable="false"
					multiselect="false"
					rowNum="9999">
					<ui:Column name="defId" key="true" hidden="true"></ui:Column>
					<ui:Column name="defKey" hidden="true"></ui:Column>
					<ui:Column header="标题" name="subject" width="160"></ui:Column>
					<ui:Column header="" width="40" formatter="operatorFormatter" align="center" fixed="true"></ui:Column>
			</ui:Grid>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>