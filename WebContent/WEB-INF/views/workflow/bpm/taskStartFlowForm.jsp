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
<title>流程启动--${bpmDefinition.subject} --版本:${bpmDefinition.version}</title>
<script type="text/javascript">
var isExtForm=${isExtForm};
var isFormEmpty=${isFormEmpty};
var hasLoadComplete=false;
var actDefId="${bpmDefinition.actDefId}";
$(function(){
	//表单不为空的情况。
	if(isFormEmpty==false){
		if(isExtForm){
			var formUrl = $('#divExternalForm').attr("formUrl");
			$.ajax({
				url : formUrl,
				cache : false,
				success : function(html) {
					$("#divExternalForm").html(html);
					$.parser.parse($("#divExternalForm"));
					using("validate",function(){
						$("#workFlowForm").validate();
					});
				}
			});
		}else{
		}
	}
});

//启动流程
function startWorkFlow(){
	if(isFormEmpty){
		MessageUtil.alert("流程表单为空，请先设置流程表单!");
		return;
	}
	//前置事件处理
	var rtn = beforeClick(1);
	if( rtn==false){
		return;
	}
	if(isExtForm){
		if($("#workFlowForm").validate("validate")){
			var data = $("#workFlowForm").serialize();
			$("#startBtn").linkbutton("disable");
			$.postc("${ctx}/workflow/task/startFlow.html", data, function(data){
				var rtn=afterClick(1);
				if( rtn==false){
					return;
				}
				MessageUtil.show("流程启动成功!");
				parentDialog.markUpdated();
				parentDialog.close();
			},function(data){
				$("#startBtn").linkbutton("enable");
			});
		};
	}else{
		//TODO 在线表单
	};
}

//保存为草稿
function saveAsDraft(){
	if(isFormEmpty){
		MessageUtil.alert("流程表单为空，请先设置流程表单!");
		return;
	}
	var rtn=beforeClick(2);
	if( rtn==false){
		return;
	}
	var  action="";
	if($("#saveDraftBtn").hasClass('isDraft')){
		action="${ctx}/workflow/task/saveForm.html";
	}else{
		action="${ctx}/workflow/task/saveData.html";
	}
	if(isExtForm){
		if($("#workFlowForm").validate("validate")){
			var data = $("#workFlowForm").serialize();
			$("#saveDraftBtn").linkbutton("disable");
			$.postc(action, data, function(data){
				var rtn=afterClick(2);
				if( rtn==false){
					return;
				}
				MessageUtil.show("流程表单保存成功!");
				parentDialog.markUpdated();
				parentDialog.close();
			},function(data){
				$("#saveDraftBtn").linkbutton("enable");
			});
		};
	}else{
		//TODO 在线表单
	};
	
}

function showDiagram(){
	FlowUtil.showBpmDefGraph('${bpmDefinition.actDefId}');
}
</script>
</head>
<body>
<ui:LayoutContainer fit="true" >
	<ui:Layout region="north" style="height:60px;" title="流程启动--${bpmDefinition.subject}--V${bpmDefinition.version}" border="false" split="false">
		<%@include file="innToolBarStart.jsp" %>
	</ui:Layout>
	<ui:Layout region="center" border="false">
		<div style="padding: 6px 8px 3px 12px;" class="noprint">
			<b>流程简述：</b>${bpmDefinition.description}
		</div>
		<form id="workFlowForm" method="post">
				<input type="hidden" name="actDefId" value="${bpmDefinition.actDefId}" />
				<input type="hidden" name="defId" value="${bpmDefinition.defId}"/>
				<input type="hidden" name="businessKey" value="${businessKey}"/>
				<input type="hidden" name="runId" value="${runId}" />
				<c:choose>
					<c:when test="${isFormEmpty==true}">
						<div class="noForm">没有设置流程表单。</div>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${isExtForm}">
								<div id="divExternalForm" formUrl="${form}"></div>
							</c:when>
							<c:otherwise>
								<div id="onLineFormContainer">${form}</div>
								<input type="hidden" name="formKey" id="formKey" value="${formKey}"/>
								<input type="hidden" name="formData" id="formData" />
								<c:if test="${not empty  paraMap}">
									<c:forEach items="${paraMap}" var="item">
									<input type="hidden" name="${item.key}" value="${item.value}" />
	      							</c:forEach>
								</c:if>
							</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose>
		</form>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>