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
<title>流程任务-[${task.name}]执行</title>
<script type="text/javascript">
var taskId="${task.id}";
var isExtForm=${isExtForm};
var isEmptyForm=${isEmptyForm};
var topWin = FrameHelper.getTop();
$(function(){
	//表单不为空的情况。
	if(isEmptyForm==false){
		if(isExtForm){//处理URL表单
			var formUrl = $('#divExternalForm').attr("formUrl");
			$.postc(formUrl, null, function(html) {
					$("#divExternalForm").html(html);
					$.parser.parse($("#divExternalForm"));
					using("validate",function(){
						$("#workFlowForm").validate();	
					});
				});
		}else{
		};
	};
});
//提交
function commit(){
	var operatorType = 1;
	$("#back").val("0");
	completeTask(operatorType,this);
}
//同意
function agree(){
	var operatorType = 3;
	var isDirectComlete = getIsDirectComplete();
	//同意:5，一票通过。
	var tmp =isDirectComlete?"5":"1";
	$("#voteAgree").val(tmp);
	$("#back").val("0");
	completeTask(operatorType,this);
}

//会签：不同意
function notAgree(){
	var operatorType = 4;
	var isDirectComlete = getIsDirectComplete();
	////直接一票通过
	var tmp =isDirectComlete?'6':'2';
	$("#voteAgree").val(tmp);
	$("#back").val("0");
	completeTask(operatorType,this);
}
//会签:弃权
function abandon(){
	var operatorType = 9;
	$("#voteAgree").val('0');
	$("#back").val("0");
	completeTask(operatorType,this);
}
//驳回
function reject(){
	var voteContent = $("#voteContent");
	var content = voteContent.val();
	if (voteContent.length == 0 || (content && content.trim() != '')) {
		var operatorType = 4;
		$("#voteAgree").val("3");
		$("#back").val("1");
		completeTask(operatorType,this);
	} else {
		MessageUtil.alert("请填写驳回意见");
	};
}

//驳回到发起人
function rejectToStart(){
	var voteContent = $("#voteContent");
	var content = voteContent.val();
	if(voteContent.length==0||(content && content.trim()!='')){
		var operatorType = 5;
		//驳回到发起人
		$("#voteAgree").val("3");
		$("#back").val("2");
		completeTask(operatorType,this);
	}else{
		MessageUtil.alert("请填写驳回意见！","提示信息");
	};
}

//完成任务
function completeTask(operateType,btn) {
	var rtn = beforeClick(operateType);
	if( rtn==false){
		return;
	}
	var action="${ctx}/workflow/task/complete.html";
	MessageUtil.confirm("您确定执行此操作吗？",function(){
		submitForm(action,btn,operateType);
	});
}

function addSign(){
	var diag = new topWin.Dialog({
		URL : "${ctx}/workflow/task/toAddSign.html?taskId=${task.id}",
		Width: 500,
		Height: 200,
		Title : "选择补签人员",
		RefreshHandler : function(){
			parentDialog.markUpdated();
			parentDialog.close();
		}
	});
	diag.show();
}

function submitForm(action,button,operatorType){
	if(isEmptyForm){
		MessageUtil.alert("还没有设置表单！",'提示信息');
		return;
	}
	if(isExtForm){//URL表单处理
		if($("#workFlowForm").validate("validate")){
			var data = $("#workFlowForm").serialize();
			$.postc(action, data, function(data){
				var rtn=afterClick(operatorType);
				if( rtn==false){
					return;
				}
				MessageUtil.show("执行任务成功!");
				parentDialog.markUpdated();
				parentDialog.close();
			},function(data){
			});
		};
	}else{
		//TODO 处理在线表单
	}
}
//审批历史
function showProRunHistory(){
	FlowUtil.showProRunHistory('${processRun.runId}');
}

//流程图
function showProRunGraph(){
	FlowUtil.showProRunGraph('${processRun.runId}');
}

function endProcess(){
	var diag = topWin.Dialog.open({
		Title : "终止意见",
		URL : '${ctx}/workflow/task/toEnd.html',
		Width : 500,
		Height : 200,
		RefreshHandler:function(){
			var memo = diag.getData(); 
			$.postc("${ctx}/workflow/task/endProcess.html",{taskId:taskId,memo:memo},function(){
				MessageUtil.show("终止任务成功!");
				parentDialog.markUpdated();
				parentDialog.close();
			});
		}
	});
}

//保存表单
function saveForm(){
	if(isEmptyForm){
		MessageUtil.alert("流程表单为空，请先设置流程表单!");
		return;
	}
	var rtn=beforeClick(2);
	if( rtn==false){
		return;
	}
	var	action="${ctx}/workflow/task/saveData.html";
	if(isExtForm){
		if($("#workFlowForm").validate("validate")){
			var data = $("#workFlowForm").serialize();
			$("#saveFormBtn").linkbutton("disable");
			$.postc(action, data, function(data){
				var rtn=afterClick(2);
				if( rtn==false){
					return;
				}
				MessageUtil.show("流程表单保存成功!");
				parentDialog.markUpdated();
				parentDialog.close();
			},function(data){
				$("#saveFormBtn").linkbutton("enable");
			});
		};
	}else{
		//TODO 在线表单
	};
	
}
//获取是否允许直接结束。
function getIsDirectComplete() {
	var isDirectComlete = false;
	if ($("#chkDirectComplete").length > 0) {
		if ($("#chkDirectComplete").attr("checked") == "checked") {
			isDirectComlete = true;
		};
	}
	return isDirectComlete;
};
</script>
</head>
<body>
<ui:LayoutContainer fit="true" >
<form id="workFlowForm" method="post" class="easyui-validate"> 
	<ui:Layout region="north" style="height:56px;" title="任务审批处理--<b>${task.name}</b>--<i>[${bpmDefinition.subject}-V${bpmDefinition.version}]</i>" border="false" split="false">
		<c:choose>
           	<c:when test="${(empty task.executionId) && task.description=='15' }">
           		<jsp:include page="innToolBarNotify.jsp"></jsp:include>
           	</c:when>
           	<c:when test="${(empty task.executionId) && task.description=='38' }">
           		<jsp:include page="innToolBarTransTo.jsp"></jsp:include>
           	</c:when>
           	<c:otherwise>
           		<jsp:include page="innToolBarNode.jsp"></jsp:include>
           	</c:otherwise>
         </c:choose>
	</ui:Layout>
	<ui:Layout region="center" border="false">
		<!-- 审批意见栏 -->
         <c:choose>
			<c:when test="${task.description!='15' &&  task.description!='38'}">
				<div style="margin-top:2px;padding:1px">
					<jsp:include page="innTaskOpinion.jsp"></jsp:include>
				</div>
			</c:when>
		</c:choose>
		<div class="printForm" style="overflow: auto;padding:1px;margin-top:2px">
			<c:choose>
				<c:when test="${isEmptyForm==true}">
					<div class="noForm">没有设置流程表单。</div>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${isExtForm}">
							<div class="printForm" id="divExternalForm" formUrl="${form}"></div>
						</c:when>
						<c:otherwise>
							<div class="printForm" type="custform">${form}</div>
							<input type="hidden" name="formData" id="formData" />
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</div>
		<input type="hidden" id="taskId" name="taskId" value="${task.id}" />
		<%--驳回和撤销投票为再次提交 --%>
		<c:choose>
			<c:when test="${processRun.status eq 5 or processRun.status eq 6}">
				<input type="hidden" id="voteAgree" name="voteAgree" value="34" />
			</c:when>
			<c:otherwise>
				<input type="hidden" id="voteAgree" name="voteAgree" value="1" />
			</c:otherwise>
		</c:choose>
		<input type="hidden" id="back" name="back" value="" />
		<input type="hidden" name="actDefId" value="${bpmDefinition.actDefId}" />
		<input type="hidden" name="defId" value="${bpmDefinition.defId}" />
		<input type="hidden" id="isManage" name="isManage" value="${isManage}" />
		<input type="hidden" id="businessKey" name="businessKey" value="${processRun.businessKey}" />
	</ui:Layout>
</form>
</ui:LayoutContainer>
</body>
</html>