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
<script>
var topWin = FrameHelper.getTop();
function participantTypeChange(){
	var participantType = $("#participantType").combobox("getValue");
	if(participantType=="1" || participantType=="8"){
		$("#participanDom").hide();
		clearParticipan();
	}else{
		$("#participanDom").show();
		clearParticipan();
	}
}

function clearParticipan(){
	$("#participan").val("");
	$("#participanDesc").val("");
}

function doSave(){
	if($("#editForm").validate("validate")){
		var parms = $("#editForm").serialize();
		var type = "${type}";
		if(type=="add"){
			$.postc("${ctx}/workflow/bpmDef/nodeParticipant/add.html", parms, function(data){
				MessageUtil.show("添加成功！");
				$("#saveBtn").linkbutton("disable");
				$("#reAddBtn").linkbutton("enable");
				parentDialog.markUpdated();
				parentDialog.close();
			});
		}else if(type=="update"){
			$.postc("${ctx}/workflow/bpmDef/nodeParticipant/update.html", parms, function(data){
				MessageUtil.show("修改成功！");
				parentDialog.markUpdated();
				parentDialog.close();
			});
		}
	}
}

function reAdd(){
	$("#editForm")[0].reset();
	$("#saveBtn").linkbutton("enable");
	$("#reAddBtn").linkbutton("disable");
}


function selectParticiPant(){
	var participantType = $("#participantType").combobox("getValue");
	if(participantType=='2'){
		var diag = new topWin.Dialog({
			URL : "${ctx}/userCtl/cmp/userSelector.html",
			Width: 900,
			Height: 450,
			Title : "用户选择器",
			RefreshHandler : function(){
				var selectedUserList = diag.getData().selectedUserList;
				if(selectedUserList!=null){
					var ids = "";
					var descs = "";
					var spliter='';
					for(var i=0;i<selectedUserList.length;i++){
						ids += spliter+selectedUserList[i].ZJID;
						descs += spliter+selectedUserList[i].YHMC;
						spliter=',';
					}
					$("#participan").val(ids);
					$("#participanDesc").val(descs);
				}else{
					$("#participan").val("");
					$("#participanDesc").val("");
				}
			}
		});
		diag.setData({selectedUser:$("#participan").val()});
		diag.show();
	}else if(participantType=='3' ||  participantType=='9'){
		var multiselect = (participantType=='3'?true:false);
		var diag = new topWin.Dialog({
			URL : "${ctx}/roleCtl/cmp/roleSelector.html?multiselect="+multiselect,
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
					$("#participan").val(ids);
					$("#participanDesc").val(descs);
				}else{
					$("#participan").val("");
					$("#participanDesc").val("");
				}
			}
		});
		diag.setData({selectedRole:$("#participan").val()});
		diag.show();
	}else if(participantType=='4'){
		var diag = new topWin.Dialog({
			URL : "${ctx}/orgCtl/cmp/orgSelector.html",
			Width: 900,
			Height: 450,
			Title : "单位选择器",
			RefreshHandler : function(){
				var selectedOrgList = diag.getData().selectedOrgList;
				if(selectedOrgList!=null){
					var ids = "";
					var descs = "";
					var spliter='';
					for(var i=0;i<selectedOrgList.length;i++){
						ids += spliter+selectedOrgList[i].ZJID;
						descs += spliter+selectedOrgList[i].DWMC;
						spliter=',';
					}
					$("#participan").val(ids);
					$("#participanDesc").val(descs);
				}else{
					$("#participan").val("");
					$("#participanDesc").val("");
				}
			}
		});
		diag.setData({selectedOrg:$("#participan").val()});
		diag.show();
	}else if(participantType=='5' || participantType=='6'){
		var diag = new topWin.Dialog({
			URL : "${ctx}/workflow/bpmDef/taskNodeSelector.html?actDefId=${nodeParticipant.actDefId}&nodeId=${nodeParticipant.nodeId}",
			Width: 700,
			Height: 450,
			Title : "流程任务节点选择器",
			RefreshHandler : function(){
				var selectedNode = diag.getData().selectedNode;
				if(selectedNode!=null){
					$("#participan").val(selectedNode.id);
					$("#participanDesc").val(selectedNode.name);
				}else{
					$("#participan").val("");
					$("#participanDesc").val("");
				}
			}
		});
		diag.show();
	}else if(participantType=='7'){
		var diag = new topWin.Dialog({
			URL : "${ctx}/workflow/bpmDef/taskNodeRoleSelector.html?actDefId=${nodeParticipant.actDefId}&nodeId=${nodeParticipant.nodeId}",
			Width: 500,
			Height: 300,
			Title : "流程任务节点及角色选择器",
			RefreshHandler : function(){
				var data = diag.getData();
				if(data!=null){
					var role = data.role;
					var node = data.node;
					$("#participan").val(node.nodeId+","+role.roleId);
					$("#participanDesc").val("节点名称："+node.nodeName+"，角色："+role.roleName);
				}else{
					$("#participan").val("");
					$("#participanDesc").val("");
				}
			}
		});
		diag.show();
	}else if(participantType=='20'){
		var diag = new topWin.Dialog({
			URL : "${ctx}/workflow/bpmDef/nodeParticipant/scriptEdit.html",
			Width: 600,
			Height: 350,
			Title : "流程节点人员脚本编辑器",
			RefreshHandler : function(){
				var script = diag.getData().script;
				if(script!=null){
					$("#participan").val("");
					$("#participanDesc").val(script);
				}else{
					$("#participan").val("");
					$("#participanDesc").val("");
				}
			}
		});
		diag.setData({script:$("#participanDesc").val()});
		diag.show();
	}
}
</script>
</head>
<body>
	<ui:LayoutContainer fit="true">
		<ui:Layout region="north" style="height:33px;">
			<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
				<a id="saveBtn" href="#" iconCls="icon-save" onclick="doSave()">保存</a>
				<c:if test="${type=='add'}">
					<a id="reAddBtn" href="#" iconCls="icon-redo" onclick="reAdd();">再次添加</a>
				</c:if>
				<a id="close" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
			</div>
		</ui:Layout>
		<ui:Layout region="center">
			<form id="editForm" class="easyui-validate" style="margin-top:5px;">
				<input type="hidden" id="participantId" name="participantId" value="${nodeParticipant.participantId}">
				<input type="hidden" id="defId" name="defId" value="${nodeParticipant.defId}">
				<input type="hidden" id="actDefId" name="actDefId" value="${nodeParticipant.actDefId}">
				<input type="hidden" id="nodeId" name="nodeId" value="${nodeParticipant.nodeId}">
				<input type="hidden" id="configId" name="configId" value="${nodeParticipant.configId}">
				<table class="tableView">
					<tr>
						<td class="label" style="width:100px;">
							参与类型：
						</td>
						<td>
							<select id="participantType" name="participantType" class="easyui-combobox validate[required]" style="width:200px"
									url="${ctx}/codeCtl/getCodeItemList.html?dmjc=BPM_PARTICIPANT" showEmptySelect="true"
									valueField="dmx_bh" textField="dmx_mc" onChange="participantTypeChange" selValue="${nodeParticipant.participantType}">
							</select>
						</td>
					</tr>
					<tr id="participanDom" <c:if test="${nodeParticipant.participantType eq '1'}">style='display:none'</c:if>>
						<td class="label" style="width:100px;">
							参与者：
						</td>
						<td>
							<textarea id="participanDesc" class="easyui-textarea" name="participanDesc" style="width:300px;height:100px;" readonly="readonly">${nodeParticipant.participanDesc}</textarea>
							<input type="button" class="easyui-button" value="请选择" onclick="selectParticiPant()">
							<input type="hidden" id="participan" name="participan" value="${nodeParticipant.participan}"/>
						</td>
					</tr>
					<tr>
						<td class="label" style="width:100px;">
							运算类型：
						</td>
						<td>
							<select id="computeType" name="computeType" class="easyui-combobox validate[required]" selValue="${nodeParticipant.computeType}" dropdownHeight="75">
								<option value="0">或</option>
								<option value="1">与</option>
								<option value="2">排除</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="label" style="width:100px;">
							抽取用户：
						</td>
						<td>
							<select id="extractUser" name="extractUser" class="easyui-combobox validate[required]" selValue="${nodeParticipant.extractUser}" dropdownHeight="50">
								<option value="0">不抽取</option>
								<option value="1">抽取</option>
							</select>
						</td>
					</tr>
				</table>
			</form>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>