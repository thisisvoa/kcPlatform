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
	var topWin = FrameHelper.getTop();
	var isUpdate = false;
	function addPaticipant(){
		topWin.Dialog.open({
			URL : "${ctx}/workflow/bpmDef/nodeParticipant/toAdd.html?defId=${nodeConfig.defId}&nodeId=${nodeConfig.nodeId}",
			ShowCloseButton : true,
			Width: 500,
			Height : 300,
			Title : "新增参与者",
			RefreshHandler:function(){
				$("#paticipantList").trigger("reloadGrid");
			}
		});	
	}
	
	function updatePaticipant(){
		var selectedIds = $("#paticipantList").grid("getGridParam", "selarrrow");
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
					$("#paticipantList").trigger("reloadGrid");
				}
			});	
		}
		
	}
	
	function removePaticipant(){
		var selectedIds = $("#paticipantList").grid("getGridParam", "selarrrow");
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
				$("#paticipantList").trigger("reloadGrid");
			});
			
		});
	}
	
	function up(){
		var selectedIds = $("#paticipantList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要的移动的记录！");
			return;
		}
		if(selectedIds.length>1){
			MessageUtil.alert("只能选择一条记录进行移动 ！");
			return;
		}
		var rowInd = $("#paticipantList").grid("getInd", selectedIds[0]);
		if(rowInd==1){
			return;
		}else{
			var dataIds = $("#paticipantList").grid("getDataIDs");
			var rowd = $("#paticipantList").grid("getRowData", selectedIds[0]);
			var rowid = rowd.participantId;
			var preRowId =  dataIds[rowInd-2];
			$("#paticipantList").grid("delRowData", rowid);
			$("#paticipantList").grid("addRowData", rowid, rowd, "before", preRowId);
			$("#paticipantList").grid("setSelection",rowid);
			isUpdate = true;
		}
		
	}
	
	function down(){
		var selectedIds = $("#paticipantList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要的移动的记录！");
			return;
		}
		if(selectedIds.length>1){
			MessageUtil.alert("只能选择一条记录进行移动 ！");
			return;
		}
		var rowInd = $("#paticipantList").grid("getInd", selectedIds[0]);
		var dataIds = $("#paticipantList").grid("getDataIDs");
		if(rowInd==dataIds.length){
			return;
		}else{
			var rowd = $("#paticipantList").grid("getRowData", selectedIds[0]);
			var rowid = rowd.participantId;
			var afterRowId =  dataIds[rowInd];
			$("#paticipantList").grid("delRowData", rowid);
			$("#paticipantList").grid("addRowData", rowid, rowd, "after", afterRowId);
			$("#paticipantList").grid("setSelection",rowid);
			isUpdate = true;
		}
	}
	
	function saveSn(){
		if(isUpdate == false){
			MessageUtil.confirm("按钮排序没有变更！");
			return;
		}
		MessageUtil.confirm("确定保存设置排序吗？",function(){
			var list = $("#paticipantList").grid("getRowData");
			var postd = [];
			for(var i=0;i<list.length;i++){
				var d = {participantId:list[i].participantId, sn:(i+1)};
				postd.push(d);
			}
			$.postc("${ctx}/workflow/bpmDef/nodeParticipant/saveSn.html",{participantJson:JSON.stringify(postd)},function(){
				MessageUtil.show("序号保存成功！");
				$("#paticipantList").trigger("reloadGrid");
			});
		});
	}
	
	function participanDescFormatter(cellVal, options, rowd) {
		cellVal = cellVal==null?"":cellVal;
		return " <table style='border:none;'><tr><td style=\"vertical-align:middle;height:60px;overflow-y:auto;border:none;white-space:normal;\">"+cellVal+"</td></tr></table>";
	}
</script>
</head>
<body>
<ui:LayoutContainer fit="true" >
	<ui:Layout region="north" style="height:58px" title="流程节点人员设置-${nodeConfig.nodeName}" iconCls="icon-user">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="add" href="#" iconCls="icon-add" onclick="addPaticipant()">添加</a>
			<a id="update" href="#" iconCls="icon-edit" onclick="updatePaticipant()">修改</a>
			<a id="remove" href="#" iconCls="icon-remove" onclick="removePaticipant()">删除</a>
			<a id="up" href="#" iconCls="icon-moveup" onclick="up()">上移</a>
			<a id="down" href="#" iconCls="icon-movedown" onclick="down()">下移</a>
			<a id="saveSnBtn" href="#" iconCls="icon-save" onclick="saveSn()">保存排序</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center">
		<ui:Grid id="paticipantList" datatype="json" shrinkToFit="false"
			fit="true" viewrecords="true" pageable="false"
			url="${ctx}/workflow/bpmDef/nodeParticipant/list.html" multiselect="true"
			rowNum="9999" postData="{defId:'${nodeConfig.defId}',nodeId:'${nodeConfig.nodeId}'}">
			<ui:Column name="participantId" key="true" hidden="true"></ui:Column>
			<ui:Column header="参与者类型" name="participantType" width="120" edittype="select" formatter="'select'" align="center" editoptions="{value:\"${participantTypeDict}\"}"></ui:Column>
			<ui:Column header="参与者" name="participanDesc" width="320" formatter="participanDescFormatter"></ui:Column>
			<ui:Column header="抽取用户" name="extractUser" width="60" fixed="true" edittype="select" formatter="'select'" align="center" editoptions="{value:\"0:不抽取;1:抽取\"}"></ui:Column>
			<ui:Column header="运算类型" name="computeType" width="60" fixed="true" edittype="select" formatter="'select'" align="center" editoptions="{value:\"0:或;1:与;2:排除\"}"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>