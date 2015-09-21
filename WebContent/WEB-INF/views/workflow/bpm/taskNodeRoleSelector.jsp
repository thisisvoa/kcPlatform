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
function doSave(){
	if($("#editForm").validate("validate")){
		var role = {roleId:$("#roleId").val(), roleName:$("#roleName").val()};
		var node = {nodeId:$("#nodeId1").val(), nodeName:$("#nodeName1").val()};
		parentDialog.setData({role:role,node:node});
		parentDialog.markUpdated();
		parentDialog.close();
	}
}
function selectNode(){
	var diag = new topWin.Dialog({
		URL : "${ctx}/workflow/bpmDef/taskNodeSelector.html?actDefId=${actDefId}&nodeId=${nodeId}",
		Width: 700,
		Height: 450,
		Title : "流程任务节点选择器",
		RefreshHandler : function(){
			var selectedNode = diag.getData().selectedNode;
			if(selectedNode!=null){
				$("#nodeId1").val(selectedNode.id);
				$("#nodeName1").val(selectedNode.name);
			}else{
				$("#nodeId1").val("");
				$("#nodeName1").val("");
			}
			$("#nodeName1").trigger("blur");
		}
	});
	diag.show();
}

function selectRole(){
	var diag = new topWin.Dialog({
		URL : "${ctx}/roleCtl/cmp/roleSelector.html?multiselect=false",
		Width: 700,
		Height: 450,
		Title : "角色选择器",
		RefreshHandler : function(){
			var selectedRoleList = diag.getData().selectedRoleList;
			if(selectedRoleList!=null){
				var ids = "";
				var descs = "";
				var spliter='';
				for(var i=0;i<selectedRoleList.length;i++){
					ids += spliter+selectedRoleList[i].zjid;
					descs += spliter+selectedRoleList[i].jsmc;
					spliter=',';
				}
				$("#roleId").val(ids);
				$("#roleName").val(descs);
			}else{
				$("#roleId").val("");
				$("#roleName").val("");
			}
			$("#roleName").trigger("blur");
		}
	});
	diag.setData({selectedRole:$("#roleId").val()});
	diag.show();
}
</script>
</head>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:33px" border="false">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="saveBtn" href="#" iconCls="icon-save" onclick="doSave()">选择</a>
			<a id="close" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center" border="false">
		<form id="editForm" class="easyui-validate">
			<table class="tableView">
				<tr>
					<td class="label">
						任务节点：
					</td>
					<td>
						<input type="hidden" id="nodeId1" name="nodeId1" /> 
						<input type="text" id="nodeName1" onclick="selectNode();"  class="easyui-textinput validate[required]" promptPosition="bottom"/>
						<input type="button" class="easyui-button" value="请选择" onclick="selectNode()">
						<span class="star">*</span>
					</td>
				</tr>
				<tr>
					<td class="label">
						角&nbsp;&nbsp;色：
					</td>
					<td>
						<input type="hidden" id="roleId" name="roleId" /> 
						<input type="text" id="roleName" onclick="selectRole();" readonly="readonly" class="easyui-textinput validate[required]" promptPosition="bottom"/>
						<input type="button" class="easyui-button" value="请选择" onclick="selectRole()">
						<span class="star">*</span>
					</td>
				</tr>
			</table>
		</form>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>