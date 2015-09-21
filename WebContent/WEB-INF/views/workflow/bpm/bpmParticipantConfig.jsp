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
<title>流程人员设置</title>
<script type="text/javascript">
var topWin = FrameHelper.getTop();
function addPaticipant(defId, nodeId){
	topWin.Dialog.open({
		URL : "${ctx}/workflow/bpmDef/nodeParticipant/toAdd.html?defId="+defId+"&nodeId="+nodeId,
		ShowCloseButton : true,
		Width: 500,
		Height : 300,
		Title : "新增参与者",
		RefreshHandler:function(){
			$("#paticipantList_"+nodeId).trigger("reloadGrid");
		}
	});	
}

function updatePaticipant(defId, nodeId){
	var selectedIds = $("#paticipantList_"+nodeId).grid("getGridParam", "selarrrow");
	if (selectedIds.length == 0){
		MessageUtil.alert("请选择待修改的参与者！");
		return;
	}else if(selectedIds.length > 1){
		MessageUtil.alert("只能选择一条记录进行修改！");
	}else{
		topWin.Dialog.open({
			URL : "${ctx}/workflow/bpmDef/nodeParticipant/toUpdate.html?id="+selectedIds[0],
			ShowCloseButton : true,
			Width: 500,
			Height : 300,
			Title : "修改参与者",
			RefreshHandler:function(){
				$("#paticipantList_"+nodeId).trigger("reloadGrid");
			}
		});	
	}
	
}

function removePaticipant(defId, nodeId){
	var selectedIds = $("#paticipantList_"+nodeId).grid("getGridParam", "selarrrow");
	if (selectedIds.length <= 0) {
		MessageUtil.alert("请选择要删除的参与者！");
		return;
	}
	MessageUtil.confirm("您确定删除选中的参与者吗？", function() {
		var ids = selectedIds.join(",");
		var url = "${ctx}/workflow/bpmDef/nodeParticipant/delete.html";
		var params = {
			ids:ids
		};
		$.postc(url,params,function(data){
			MessageUtil.show("操作成功!");
			$("#paticipantList_"+nodeId).trigger("reloadGrid");
		});
		
	});
}
function participanDescFormatter(cellVal, options, rowd) {
	cellVal = cellVal==null?"":cellVal;
	return " <table style='border:none;'><tr><td style=\"vertical-align:middle;height:60px;overflow-y:auto;border:none;white-space:normal;\">"+cellVal+"</td></tr></table>";
}
function doRefresh(){
	$(".easyui-grid").trigger("reloadGrid");
}
</script>
</head>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:33px">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="refresh" href="#" iconCls="icon-refresh" onclick="doRefresh()">刷新</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center" border="false" style="padding:5px">
		<table class="tableView" style="margin-top:5px">
			<tr>
				<td style="width:35px;height:20px;font-weight:bold" class="ui-state-default" align="center">
					序号
				</td>
				<td style="width:150px;height:20px;font-weight:bold" class="ui-state-default">
					节点名称
				</td>
				<td style="height:20px;font-weight:bold;" class="ui-state-default">
					节点人员
				</td>
			</tr>
			<c:forEach var="nodeConfig" items="${nodeConfigList}" varStatus="status">
				<tr>
					<td align="center">
						${status.index+1}
					</td>
					<td>
						${nodeConfig.nodeName}
					</td>
					<td>
						<div id="toolbar_${nodeConfig.nodeId}"  style="width:510px;margin-bottom:1px;" fit="false">
							<a id="add_${nodeConfig.nodeId}" class="easyui-linkbutton" plain="true" href="#" iconCls="icon-add" onclick="addPaticipant('${nodeConfig.defId}','${nodeConfig.nodeId}')">添加</a>
							<a id="update_${nodeConfig.nodeId}" class="easyui-linkbutton" plain="true" href="#" iconCls="icon-edit" onclick="updatePaticipant('${nodeConfig.defId}','${nodeConfig.nodeId}')">修改</a>
							<a id="remove_${nodeConfig.nodeId}" class="easyui-linkbutton" plain="true" href="#" iconCls="icon-remove" onclick="removePaticipant('${nodeConfig.defId}','${nodeConfig.nodeId}')">删除</a>
						</div>
						<ui:Grid id="paticipantList_${nodeConfig.nodeId}" datatype="json" shrinkToFit="false" viewrecords="true" pageable="false"
							url="${ctx}/workflow/bpmDef/nodeParticipant/list.html" multiselect="true" width="570" height="auto"
							rowNum="20" rowList="[10,20,50]" postData="{defId:'${nodeConfig.defId}',nodeId:'${nodeConfig.nodeId}'}">
							<ui:Column name="participantId" key="true" hidden="true"></ui:Column>
							<ui:Column header="参与者类型" name="participantType" width="80" edittype="select" formatter="'select'" align="center" editoptions="{value:\"${participantTypeDict}\"}"></ui:Column>
							<ui:Column header="参与者" name="participanDesc" width="280" formatter="participanDescFormatter"></ui:Column>
							<ui:Column header="抽取用户" name="extractUser" width="60" fixed="true" edittype="select" formatter="'select'" align="center" editoptions="{value:\"0:不抽取;1:抽取\"}"></ui:Column>
							<ui:Column header="运算类型" name="computeType" width="60" fixed="true" edittype="select" formatter="'select'" align="center" editoptions="{value:\"0:或;1:与;2:排除\"}"></ui:Column>
						</ui:Grid>
					</td>
				</tr>
			</c:forEach>
		</table>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>