
<%@page import="com.casic27.platform.util.DateUtils"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="ui" uri="/tags/ui" %>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
<script type="text/javascript" src="${ctx}/ui/js/util/json2.js"></script>
<title></title>
<script type="text/javascript">
	var topWin = FrameHelper.getTop();
	$parsed(function(){
		var authType = "${bpmAgentSetting.authType}";
		if(authType!="0"){
			$("#defInfo").show();
		}else{
			$("#defInfo").hide();
		}
	});
	function doSave(){
		if($("#editForm").validate("validate")){
			var id = $("#id").val();
			var authType = $("input[name='authType']:checked").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var agentId = $("#agentId").val();
			var agent = $("#agent").val();
			var enabled = $("input[name='enabled']:checked").val();
			var param = {id:id,authType:authType,startDate:startDate,endDate:endDate,agentId:agentId,agent:agent,enabled:enabled};
			var defList = $("#bpmDefList").grid("getRowData"); 
			if(authType=="1"){
				if(defList.length==0){
					MessageUtil.alert("请选择授权流程！");
					return;
				}
			}
			if(defList!=null){
				for(var i=0;i<defList.length;i++){
					delete defList[i]['tmp'];
					delete defList[i]['undefined'];
				}
			}
			param.defList = JSON.stringify(defList);
			$.postc("${ctx}/workflow/bpm/agentSetting/save.html", param, function(data){
				MessageUtil.show("代理保存成功!");
				parentDialog.markUpdated();
				parentDialog.close();
			});	
		}
	}
	function reAdd(){
		$("#editForm")[0].reset();
		$("#saveBtn").removeAttr("disabled");
		$("#reAddBtn").attr("disabled",true);
	}
	function selectUser(){
		var diag = new topWin.Dialog({
			URL : "${ctx}/userCtl/cmp/userSelector.html?multiselect=false",
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
					$("#agentId").val(ids);
					$("#agent").val(descs);
				}else{
					$("#agentId").val("");
					$("#agent").val("");
				}
				$("#agent").trigger("blur");
			}
		});
		diag.setData({selectedUser:$("#agentId").val()});
		diag.show();
	}
	
	function authTypeChange(){
		var authType = $("input[name='authType']:checked").val();
		if(authType=='0'){
			$("#defInfo").hide();
		}else if(authType=='1'){
			$("#defInfo").show();
		}
	}
	
	function onPicked(){
		var startTime = DateFormat.parse($("#startDate").val(),'yyyy-MM-dd');
		var endTime = DateFormat.parse($("#endDate").val(),'yyyy-MM-dd');
		if(endTime!=null){
			if(endTime.getTime()<startTime.getTime()){
				$("#endDate").val($("#startDate").val());
			}
		}
	}
	
	function operationFormatter(cellVal, options, rowd) {
		var html = "<a href=\"javascript:void(0)\" onclick=\"removeDef('"+rowd.flowKey+"')\" ><img src='${ctx}/ui/css/icon/images/delete.gif' style='border:none'></img></a>";
		return html;
	}
	
	function removeDef(id){
		$("#bpmDefList").grid("delRowData", id);
	}
	
	function addBpmDef(){
		var diag = new topWin.Dialog({
			URL : "${ctx}/workflow/bpmDef/toMyListSelector.html",
			Width: 750,
			Height: 450,
			Title : "流程选择器",
			RefreshHandler : function(){
				var selectedList = diag.getData();
				for(var i=0;i<selectedList.length;i++){
					if($("#bpmDefList").grid("getInd", selectedList[i].defKey)==false){
						$("#bpmDefList").grid("addRowData", selectedList[i].defKey, {flowKey:selectedList[i].defKey, flowName:selectedList[i].subject});	
					}
				}
			}
		});
		diag.show();
	}
</script>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:150px;" border="false">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="saveBtn" href="#" iconCls="icon-save" onclick="doSave()">保存</a>
			<a id="close" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
		</div>
		<form id="editForm" class="easyui-validate" style="padding:1px">
			<input type="hidden" name="id" id="id" value="${bpmAgentSetting.id}"/>
			<table class="tableView">
				<tr>
					<td class="label">
						代理类型：
					</td>
					<td colspan="3">
						<label><input type="radio" class="easyui-radiobox validate[required]" value="0" name="authType" onclick="authTypeChange()" <c:if test='${bpmAgentSetting.authType==0}'>checked="checked"</c:if>>全权代理</label>
						<label><input type="radio" class="easyui-radiobox validate[required]" value="1" name="authType" onclick="authTypeChange()" <c:if test='${bpmAgentSetting==null || bpmAgentSetting.authType==1}'>checked="checked"</c:if>>部分代理</label>
					</td>
				</tr>
				<tr>
					<td class="label">
						开始时间：
					</td>
					<td>
						<input type="text" id="startDate" name="startDate" class="easyui-datePicker validate[required]" 
							minDate="<%=DateUtils.getCurrentDate10()%>" dateFmt="yyyy-MM-dd" 
							value='<fmt:formatDate value='${bpmAgentSetting.startDate}' pattern='yyyy-MM-dd'/>' 
							onpicked="onPicked">
						<span class="star">*</span>
					</td>
					<td class="label">
						结束时间：
					</td>
					<td>
						<input type="text" id="endDate" name="endDate" class="easyui-datePicker validate[required]" 
							minDate="<%=DateUtils.getCurrentDate10()%>" dateFmt="yyyy-MM-dd" 
							value='<fmt:formatDate value='${bpmAgentSetting.endDate}' pattern='yyyy-MM-dd'/>'
							onpicked="onPicked">
						<span class="star">*</span>
					</td>
				</tr>
				<tr>
					<td class="label">
						代理人：
					</td>
					<td colspan="3">
						<input type="hidden" id="agentId" name="agentId" value="${bpmAgentSetting.agentId}" />
						<input type="text" class="easyui-textinput validate[required]" id="agent" name="agent" value="${bpmAgentSetting.agent}" readonly="readonly" promptPosition="bottom" onclick="selectUser();"/>
						<input type="button" class="easyui-button" value="请选择" onclick="selectUser();">
						<span class="star">*</span>
					</td>
				</tr>
				<tr>
					<td class="label">
						是否启用：
					</td>
					<td colspan="3">
						<input id="enabled_y" name="enabled" class="easyui-radiobox validate[required]" value="0" type="radio" <c:if test='${bpmAgentSetting.enabled==0}'>checked="checked"</c:if>/> <label for="enabled_y">禁止</label>
						<input id="enabled_n" name="enabled" class="easyui-radiobox validate[required]" value="1" type="radio" <c:if test='${bpmAgentSetting==null || bpmAgentSetting.enabled==1}'>checked="checked"</c:if>/> <label for="enabled_n">启用</label>
					</td>
				</tr>
			</table>
		</form>
	</ui:Layout>
	<ui:Layout id="defInfo" region="center" title="授权流程">
		<ui:LayoutContainer fit="true">
			<ui:Layout region="north" style="height:31px;" border="false">
				<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
					<a id="addBtn" href="#" iconCls="icon-add" onclick="addBpmDef()">添加流程</a>
				</div>
			</ui:Layout>
			<ui:Layout region="center">
				<ui:Grid id="bpmDefList" datatype="json" shrinkToFit="false" fit="true" viewrecords="true" pageable="false"
					url="${ctx}/workflow/bpm/agentDef/list.html?settingId=${bpmAgentSetting.id}"
					multiselect="false">
					<ui:Column name="flowKey" key="true" hidden="true"></ui:Column>
					<ui:Column header="流程名称" name="flowName" width="240"></ui:Column>
					<ui:Column header="管理" name="" width="40" fixed="true" formatter="operationFormatter"></ui:Column>
				</ui:Grid>
			</ui:Layout>
		</ui:LayoutContainer>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>