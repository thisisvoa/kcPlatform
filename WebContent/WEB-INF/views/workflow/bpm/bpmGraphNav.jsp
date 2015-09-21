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
$parsed(function(){
	$("div.flowNode").click(function(event){                	
    	currentObj=$(this);
    	var info={id:currentObj.attr("id"), type:currentObj.attr("type")};
    	switch(info.type){
    		case 'startEvent':
    		case 'endEvent':
    			$('#eventMenu').data("activity", info);
       			$('#eventMenu').menu('show', {
       				left: event.pageX,
       				top: event.pageY
       			});
       			break;
    		case 'userTask':
    			$('#taskMenu').data("activity", info);
       			$('#multi').hide();
       			$('#taskMenu').menu('show', {
       				left: event.pageX,
       				top: event.pageY
       			});
       			break;
    		case 'multiUserTask':
    			$('#taskMenu').data("activity", info);
       			$('#multi').show();
       			$('#taskMenu').menu('show', {
       				left: event.pageX,
       				top: event.pageY
       			});
       			break;
    		case 'parallelGateway':
    		case 'exclusiveGateway':
    		case 'inclusiveGateway':
    			$('#gateWayMenu').data("activity", info);
       			$('#gateWayMenu').menu('show', {
       				left: event.pageX,
       				top: event.pageY
       			});
       			break;
    		case 'script':
    			$('#scriptTaskMenu').data("activity", info);
       			$('#scriptTaskMenu').menu('show', {
       				left: event.pageX,
       				top: event.pageY
       			});
       			break;
    	}
	});
});

var topWin = FrameHelper.getTop();

function userConfig(){
	var activity = $('#taskMenu').data("activity");
	var nodeId = activity.id;
	topWin.Dialog.open({
		URL : "${ctx}/workflow/bpmDef/nodeParticipant/single.html?defId=${bpmDefinition.defId}&nodeId="+nodeId,
		ShowCloseButton : true,
		Width: 700,
		Height : 450,
		Title : "人员设置"
	});
}

function formConfig(){
	var activity = $('#taskMenu').data("activity");
	var nodeId = activity.id;
	topWin.Dialog.open({
		URL : "${ctx}/workflow/bpmDef/nodeConfig/toUpdate.html?defId=${bpmDefinition.defId}&nodeId="+nodeId,
		ShowCloseButton : true,
		Width: 700,
		Height : 400,
		Title : "表单设置"
	});
}


function scriptConfig(id){
	var activity = $('#'+id).data("activity");
	var nodeId = activity.id;
	var type="";
	type = activity.type;
	topWin.Dialog.open({
		URL : "${ctx}/workflow/bpmDef/nodeScript/edit.html?defId=${bpmDefinition.defId}&actDefId=${bpmDefinition.actDefId}&nodeId="+nodeId+"&type="+type,
		ShowCloseButton : true,
		Width: 800,
		Height : 450,
		Title : "事件设置"
	});
}
function setCondition(){
	var activity = $('#gateWayMenu').data("activity");
	var nodeId = activity.id;
	topWin.Dialog.open({
		URL : "${ctx}/workflow/bpmDef/setCondition.html?defId=${bpmDefinition.defId}&deployId=${bpmDefinition.actDeployId}&nodeId="+nodeId,
		ShowCloseButton : true,
		Width: 800,
		Height : 400,
		Title : "设置分支条件"
	});
}

function setSign(){
	var activity = $('#taskMenu').data("activity");
	var nodeId = activity.id;
	topWin.Dialog.open({
		URL : "${ctx}/workflow/bpmDef/nodeSign/edit.html?defId=${bpmDefinition.defId}&actDefId=${bpmDefinition.actDefId}&nodeId="+nodeId,
		ShowCloseButton : true,
		Width: 800,
		Height : 500,
		Title : "会签设置"
	});
}
</script>
</head>
<body>
	<ui:Panel style="overflow:auto" fit="true">
		<div style=" margin:20px auto 0 2px;position: relative;background:url('${ctx}/workflow/bpmImage.html?deployId=${bpmDefinition.actDeployId}')  no-repeat;width:${shapeMeta.width}px;height:${shapeMeta.height+100}px;">
				${shapeMeta.xml} 
		</div>
	</ui:Panel>
	<div id="taskMenu" class="easyui-menu" style="width:120px;">
		<div id="form" iconCls="icon-tab" onclick="formConfig()">表单设置</div>
		<div id="user" iconCls="icon-user" onclick="userConfig()">人员设置</div>
		<div id="multi" iconCls="icon-key" onclick="setSign()">会签规则设置</div>
		<div id="script" iconCls="icon-code" onclick="scriptConfig('taskMenu')">事件设置</div>
	</div>
	<div id="scriptTaskMenu" class="easyui-menu" style="width:120px;">
		<div id="script" iconCls="icon-code" onclick="scriptConfig('scriptTaskMenu')">脚本设置</div>
	</div>
	
	<div id="gateWayMenu" class="easyui-menu" style="width:120px;">
		<div id="gateWay" iconCls="icon-swith" onclick="setCondition()">设置分支条件</div>
	</div>
	<div id="eventMenu" class="easyui-menu" style="width:120px;">
		<div id="script" iconCls="icon-code" onclick="scriptConfig('eventMenu')">事件设置</div>
	</div>
</body>
</html>