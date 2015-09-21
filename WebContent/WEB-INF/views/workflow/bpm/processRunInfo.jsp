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
<script type="text/javascript" src="${ctx}/ui/platform/workflow/bpm/FlowUtil.js"></script>
<title>流程明细</title>
<script type="text/javascript">
var isExtForm=${isExtForm};
var runId='${processRun.runId}';
var topWin = FrameHelper.getTop();
$(function(){
	if(isExtForm){
		var formUrl = $('#divExternalForm').attr("formUrl");
		$.ajax({
			url : formUrl,
			cache : false,
			success : function(html) {
				$("#divExternalForm").html(html);
				$.parser.parse($("#divExternalForm"));
			}
		});
	}
});

function redo(){
	var runId = '${processRun.runId}';
	topWin.Dialog.open({
		Title : "召回原因",
		URL : '${ctx}/workflow/bpm/processRun/toRedo.html?runId='+runId,
		Width : 500,
		Height : 210,
		RefreshHandler:function(){
			MessageUtil.alert("召回任务成功！",function(){
				parentDialog.markUpdated();
				parentDialog.close();
			});
		}
	});
};

function recover(){
	var runId = '${processRun.runId}';
	topWin.Dialog.open({
		Title : "撤销原因",
		URL : '${ctx}/workflow/bpm/processRun/toRecover.html?runId='+runId,
		Width : 500,
		Height : 210,
		RefreshHandler:function(){
			MessageUtil.alert("撤销任务成功！",function(){
				parentDialog.markUpdated();
				parentDialog.close();
			});
		}
	});
};

//重新提交
function executeTask(){
	var instanceId = '${processRun.actInstId}';
	var height = $(topWin.document.body).height();
	var width = $(topWin.document.body).width();
	topWin.Dialog.open({
		Title : "流程实例【${processRun.subject}】重新提交",
		URL : "${ctx}/workflow/task/toStart.html?instanceId="+instanceId+"&voteArgee=34",
		Width : width,
		Height : height,
		RefreshHandler:function(){
			MessageUtil.alert("任务提交成功！",function(){
				parentDialog.markUpdated();
				parentDialog.close();
			});
		}
	});
};

/**
 * 删除
 */
function delByInstId(){
	var instanceId = '${processRun.actInstId}';
	topWin.Dialog.open({
		Title : "删除原因",
		URL : '${ctx}/workflow/bpm/processRun/toLogicDel.html?instanceId='+instanceId,
		Width : 500,
		Height : 210,
		RefreshHandler:function(){
			MessageUtil.alert("删除任务成功！",function(){
				parentDialog.markUpdated();
				parentDialog.close();
			});
		}
	});
}

//审批历史
function showProRunHistory(){
	FlowUtil.showProRunHistory('${processRun.runId}');
}

//流程图
function showProRunGraph(){
	FlowUtil.showProRunGraph('${processRun.runId}');
}
</script>
</head>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:58px" border="false" title="流程明细--【${processRun.subject}】">
		<div id="toolbar" class="easyui-toolbar">
			<c:if test="${isCanRedo}">
				<a id="redoBtn" href="#" iconCls="icon-rejectToStart" onclick="redo()">召回</a>
			</c:if>
			<c:if test="${isCanRecover}">
				<a id="recoverBtn" href="#" iconCls="icon-redo" onclick="recover()">撤销</a>
			</c:if>
			<c:if test="${isFirst and (processRun.status==4 or processRun.status==5)}">
				<a id="runBtn" href="#" iconCls="icon-run" onclick="executeTask()">重新提交</a>
				<a id="removeBtn" href="#" iconCls="icon-remove" onclick="delByInstId()">删除</a>
			</c:if>
			<a id="diagramBtn" href="#" iconCls="icon-flowDesign" onclick="showProRunGraph()">流程图</a>
			<a id="hisBtn" href="#" iconCls="icon-search" onclick="showProRunHistory()">审批历史</a>
			<a id="removeBtn" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center" border="false">
		<c:choose>
			<c:when test="${isExtForm==true }">
				<div id="divExternalForm" formUrl="${form}"></div>
			</c:when>
			<c:otherwise>
				${form}
			</c:otherwise>
		</c:choose>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>